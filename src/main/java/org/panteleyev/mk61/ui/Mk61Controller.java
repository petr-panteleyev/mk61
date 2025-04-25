/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.ui;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.SegmentedButton;
import org.panteleyev.fx.WindowManager;
import org.panteleyev.mk61.core.AngleMode;
import org.panteleyev.mk61.engine.Engine;
import org.panteleyev.mk61.engine.Indicator;
import org.panteleyev.mk61.engine.KeyboardButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.function.Consumer;

import static org.panteleyev.fx.LabelFactory.label;
import static org.panteleyev.fx.MenuFactory.menu;
import static org.panteleyev.fx.MenuFactory.menuBar;
import static org.panteleyev.fx.MenuFactory.menuItem;
import static org.panteleyev.fx.dialogs.FileChooserBuilder.fileChooser;
import static org.panteleyev.fx.grid.GridBuilder.gridPane;
import static org.panteleyev.fx.grid.GridRowBuilder.gridRow;
import static org.panteleyev.mk61.core.Mk61DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.settings.Settings.settings;
import static org.panteleyev.mk61.ui.Accelerators.SHORTCUT_1;

public class Mk61Controller extends BaseController {
    public static final String APP_TITLE = "МК-61";

    private final Engine engine = new Engine();

    private final AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            var deviceModel = engine.deviceModel();
            showIndicator(deviceModel.getIndicator());
            if (stackAndMemoryController.isVisible()) {
                stackAndMemoryController.renderDeviceModel(deviceModel);
            }
        }
    };

    private static final FileChooser.ExtensionFilter EXTENSION_FILTER =
            new FileChooser.ExtensionFilter("Дамп памяти", "*.txt");

    private final BorderPane root = new BorderPane();
    private final HBox toolBox = new HBox(10);

    private final ToggleButton onButton = new ToggleButton("Вкл");

    private final Label[] digitCells = new Label[]{
            new Label("F"), new Label("F"), new Label("F"), new Label("F"),
            new Label("F"), new Label("F"), new Label("F"), new Label("F"),
            new Label("F"), new Label("F"), new Label("F"), new Label("F")
    };
    private final Label[] dotCells = new Label[]{
            new Label(" "), new Label(" "), new Label(" "), new Label(" "),
            new Label(" "), new Label(" "), new Label(" "), new Label(" "),
            new Label(" "), new Label(" "), new Label(" "), new Label(" ")
    };

    private final StackAndMemoryController stackAndMemoryController = new StackAndMemoryController();

    private final Consumer<KeyboardButton> keyboardButtonConsumer = engine::processButton;

    public Mk61Controller(Stage stage) {
        super(stage);
        stage.setResizable(false);
        stage.getIcons().add(Picture.ICON.getImage());
        root.getStyleClass().add("root");

        root.setTop(createMenuBar());

        var titleLabel = new Label("ЭЛЕКТРОНИКА  МК 61");
        titleLabel.getStyleClass().add("titleLabel");

        var center = new VBox(
                createDisplay(),
                titleLabel,
                createSwitches(),
                createKeyboardGrid()
        );
        center.setAlignment(Pos.CENTER);
        center.setFillWidth(true);
        root.setCenter(center);
        root.setRight(toolBox);

        setupWindow(root);
        getStage().sizeToScene();

        animationTimer.start();
        onButton.fire();

        settings().loadStagePosition(this);
    }

    @Override
    public String getTitle() {
        return APP_TITLE;
    }

    private MenuBar createMenuBar() {
        return menuBar(
                menu("Файл",
                        menuItem("Сохранить...", _ -> onSaveMemoryDump()),
                        menuItem("Загрузить...", _ -> onLoadMemoryDump()),
                        new SeparatorMenuItem(),
                        menuItem("Выход", _ -> onExit())
                ),
                menu("Окно",
                        menuItem("Регистры и память", SHORTCUT_1, this::onRegistersAndStackWindow)
//                        checkMenuItem("Память", false, SHORTCUT_2, this::onMemoryPanel)
                )//,
//                menu("Справка"
//                        menuItem("О программе", _ -> new AboutDialog(this).showAndWait())
//                )
        );
    }

    private BorderPane createDisplay() {
        var pane = new BorderPane();
        pane.getStyleClass().add("lcdPanel");
        pane.setMouseTransparent(true);

        for (var l : digitCells) {
            l.getStyleClass().add("lcd");
        }
        for (var d : dotCells) {
            d.getStyleClass().add("dotLcd");
        }
        var cellsPane = new HBox(0);
        cellsPane.getChildren().addAll(
                // Знак мантиссы
                digitCells[0], dotCells[0],
                // Мантисса
                digitCells[1], dotCells[1],
                digitCells[2], dotCells[2],
                digitCells[3], dotCells[3],
                digitCells[4], dotCells[4],
                digitCells[5], dotCells[5],
                digitCells[6], dotCells[6],
                digitCells[7], dotCells[7],
                digitCells[8], dotCells[8],
                // Знак порядка
                digitCells[9], dotCells[9],
                // Порядок
                digitCells[10], dotCells[10],
                digitCells[11], dotCells[11]
        );
        cellsPane.setAlignment(Pos.BOTTOM_CENTER);
        for (var dc : digitCells) {
            HBox.setMargin(dc, new Insets(0, 0, 0, -3));
        }

        pane.setCenter(cellsPane);
        return pane;
    }

    private GridPane createSwitches() {
        var offButton = new ToggleButton(" ");
        offButton.setOnAction(_ -> onPowerOff());
        offButton.setFocusTraversable(false);
        onButton.setOnAction(_ -> onPowerOn());
        onButton.setFocusTraversable(false);
        var powerSwitch = new SegmentedButton(offButton, onButton);

        var radianButton = new ToggleButton("Р");
        radianButton.setOnAction(_ -> engine.setAngleMode(AngleMode.RADIAN));
        radianButton.setFocusTraversable(false);
        var gRadianButton = new ToggleButton("ГРД");
        gRadianButton.setOnAction(_ -> engine.setAngleMode(AngleMode.GRAD));
        gRadianButton.setFocusTraversable(false);
        var degreeButton = new ToggleButton("Г");
        degreeButton.setOnAction(_ -> engine.setAngleMode(AngleMode.DEGREE));
        degreeButton.setFocusTraversable(false);
        var trigonometricSwitch = new SegmentedButton(radianButton, gRadianButton, degreeButton);
        radianButton.fire();

        var pane = gridPane(List.of(gridRow(
                powerSwitch,
                trigonometricSwitch
        )));
        GridPane.setHalignment(powerSwitch, HPos.LEFT);
        GridPane.setHgrow(powerSwitch, Priority.ALWAYS);
        GridPane.setHalignment(trigonometricSwitch, HPos.RIGHT);
        GridPane.setHgrow(trigonometricSwitch, Priority.ALWAYS);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);
        pane.getStyleClass().add("switchPanel");
        return pane;
    }

    private GridPane createKeyboardGrid() {
        var aLabel = new RegisterLabel("a");
        var bLabel = new RegisterLabel("b");
        var cLabel = new RegisterLabel("c");
        var dLabel = new RegisterLabel("d");
        var eLabel = new RegisterLabel("e");

        var grid = gridPane(List.of(
                gridRow(
                        new ButtonNode("F", "", "", "fButton", KeyboardButton.F, keyboardButtonConsumer).node(),
                        new ButtonNode("ШГ>", "x<0", "", "blackButton", KeyboardButton.STEP_RIGHT,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("<ШГ", "x=0", "", "blackButton", KeyboardButton.STEP_LEFT,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("В/О", "x≥0", "", "blackButton", KeyboardButton.RETURN,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("С/П", "x≠0", "", "blackButton", KeyboardButton.RUN_STOP,
                                keyboardButtonConsumer).node()
                ),
                gridRow(
                        new ButtonNode("K", "", "", "kButton", KeyboardButton.K, keyboardButtonConsumer).node(),
                        new ButtonNode("П→x", "L0", "", "blackButton", KeyboardButton.LOAD,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("x→П", "L1", "", "blackButton", KeyboardButton.STORE,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("БП", "L2", "", "blackButton", KeyboardButton.GOTO,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("ПП", "L3", "", "blackButton", KeyboardButton.GOSUB,
                                keyboardButtonConsumer).node()
                ),
                gridRow(
                        new ButtonNode("7", "sin", "[x]", "grayButton", KeyboardButton.D7,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("8", "cos", "{x}", "grayButton", KeyboardButton.D8,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("9", "tg", "max", "grayButton", KeyboardButton.D9,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("➖", "√", "", "grayButton", KeyboardButton.MINUS, keyboardButtonConsumer).node(),
                        new ButtonNode("➗", "1/x", "", "grayButton", KeyboardButton.DIVISION,
                                keyboardButtonConsumer).node()
                ),
                gridRow(
                        new ButtonNode("4", "sin⁻¹", "|x|", "grayButton", KeyboardButton.D4,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("5", "cos⁻¹", "ЗН", "grayButton", KeyboardButton.D5,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("6", "tg⁻¹", "°←′", "grayButton", KeyboardButton.D6,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("➕", "π", "°→′", "grayButton", KeyboardButton.PLUS,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("✖", "x²", "", "grayButton", KeyboardButton.MULTIPLICATION,
                                keyboardButtonConsumer).node()
                ),
                gridRow(
                        new ButtonNode("1", "eˣ", "", "grayButton", KeyboardButton.D1,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("2", "lg", "", "grayButton", KeyboardButton.D2,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("3", "ln", "°←‴", "grayButton", KeyboardButton.D3,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("←→", "xy", "°→‴", "grayButton", KeyboardButton.SWAP,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("В↑", "Вх", "СЧ", "grayButton", KeyboardButton.PUSH,
                                keyboardButtonConsumer).node(),
                        eLabel
                ),
                gridRow(
                        new ButtonNode("0", "10ˣ", "НОП", "grayButton", KeyboardButton.D0,
                                keyboardButtonConsumer).node(),
                        new ButtonNode(".", "⟳", "⋀", "grayButton", KeyboardButton.DOT, keyboardButtonConsumer).node(),
                        new ButtonNode("/-/", "АВТ", "⋁", "grayButton", KeyboardButton.SIGN,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("ВП", "ПРГ", "⨁", "grayButton", KeyboardButton.EE,
                                keyboardButtonConsumer).node(),
                        new ButtonNode("Cx", "CF", "ИНВ", "redButton", KeyboardButton.CLEAR_X,
                                keyboardButtonConsumer).node()
                ),
                gridRow(label(""), aLabel, bLabel, cLabel, dLabel)
        ));

        grid.getStyleClass().add("buttonGrid");
        GridPane.setHalignment(aLabel, HPos.CENTER);
        GridPane.setHalignment(bLabel, HPos.CENTER);
        GridPane.setHalignment(cLabel, HPos.CENTER);
        GridPane.setHalignment(dLabel, HPos.CENTER);
        return grid;
    }

    private void onExit() {
        engine.powerOff();
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void onPowerOn() {
        engine.powerOn();
        stackAndMemoryController.turnOn();
        animationTimer.start();
    }

    private void onPowerOff() {
        animationTimer.stop();
        engine.powerOff();
        stackAndMemoryController.turnOff();
        showIndicator(Indicator.EMPTY);
    }

    private void showIndicator(Indicator indicator) {
        var ri = indicator.indicator();
        var dots = indicator.dots();
//            var opacity = engine.automaticMode().get() ? 0.3 : 1.0;
        var opacity = 1.0;

        for (int i = 0; i < 12; i++) {
            digitCells[i].setText(Long.toString(ri & 0xF, 16).toUpperCase());
            digitCells[i].setOpacity(opacity);
            dotCells[i].setText((dots & 1) == 1 ? "." : " ");
            dotCells[i].setOpacity(opacity);
            ri = ri >> 4;
            dots = dots >> 1;
        }
    }

    private void onRegistersAndStackWindow(ActionEvent event) {
        if (!stackAndMemoryController.isVisible()) {
            stackAndMemoryController.show();
        }
//
//
//        if (event.getSource() instanceof CheckMenuItem menuItem) {
//            if (menuItem.isSelected()) {
//                toolBox.getChildren().addFirst(stackAndMemoryController);
//            } else {
//                toolBox.getChildren().remove(stackAndMemoryController);
//            }
//            getStage().sizeToScene();
//        }
    }

    private void onSaveMemoryDump() {
        var file = fileChooser("Сохранить дамп памяти", List.of(EXTENSION_FILTER)).showSaveDialog(getStage());
        if (file == null) {
            return;
        }

        try (var out = new OutputStreamWriter(new FileOutputStream(file))) {
            var bytes = engine.deviceModel().getMemory();
            for (int i = 0; i < bytes.length; i++) {
                if (i != 0 && i % 10 == 0) {
                    out.write("\n");
                }
                out.write(String.format("%02X ", bytes[i]));
            }
            out.flush();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void onLoadMemoryDump() {
        var file = fileChooser("Загрузить дамп памяти", List.of(EXTENSION_FILTER)).showOpenDialog(getStage());
        if (file == null) {
            return;
        }

        try (var reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            var codes = new int[PROGRAM_MEMORY_SIZE];
            var index = 0;

            var lines = reader.lines().toList();
            outerLoop:
            for (var line : lines) {
                if (line.startsWith("#")) {
                    continue;
                }
                var strings = line.trim().split(" ");
                for (var str : strings) {
                    if (index >= codes.length) {
                        break outerLoop;
                    }
                    codes[index++] = Integer.parseInt(str, 16);
                }
            }
            engine.deviceModel().setMemoryUpload(codes);
            engine.deviceModel().setMemoryUploadFlag(true);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }


    @Override
    protected void onWindowHiding() {
        super.onWindowHiding();
        closeChildWindows();
        settings().saveWindowsSettings();
    }

    private void closeChildWindows() {
        WindowManager.newInstance().getControllerStream()
                .filter(c -> c != this)
                .toList()
                .forEach(c -> ((BaseController) c).onClose());
    }
}