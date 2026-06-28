// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.SegmentedButton;
import org.panteleyev.fx.WindowManager;
import org.panteleyev.mk61.engine.AngleMode;
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

import static javafx.scene.layout.GridPane.setHalignment;
import static org.panteleyev.fx.factories.FileChooserFactory.fileChooser;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.MenuFactory.menu;
import static org.panteleyev.fx.factories.MenuFactory.menuBar;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.fx.factories.StringFactory.ELLIPSIS;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.mk61.Mk61Application.UI;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_ABOUT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_EXIT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_FILE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_HELP;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_LOAD;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS_AND_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_WINDOW;
import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.settings.Settings.settings;
import static org.panteleyev.mk61.ui.Accelerators.SHORTCUT_1;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_BLACK_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_BUTTON_GRID;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_DOT_LCD;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_F_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_F_LABEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_GRAY_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_KEYPAD_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_K_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_K_LABEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_LCD;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_LCD_PANEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_RED_BUTTON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_E_LABEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_LABEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_ROOT;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_SWITCH_PANEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_TITLE_LABEL;

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

    private final ToggleButton powerOnButton = powerOnButton();

    private final Label[] digitCells = new Label[]{
            label(" "), label(" "), label(" "), label(" "),
            label(" "), label(" "), label(" "), label(" "),
            label(" "), label(" "), label(" "), label(" ")
    };
    private final Label[] dotCells = new Label[]{
            label(" "), label(" "), label(" "), label(" "),
            label(" "), label(" "), label(" "), label(" "),
            label(" "), label(" "), label(" "), label(" ")
    };

    private final String[] LCD_MAP = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "L", "C", "Г", "E", " "
    };

    private final StackAndMemoryController stackAndMemoryController = new StackAndMemoryController();

    public Mk61Controller(Stage stage) {
        super(stage);
        stage.setResizable(false);
        stage.getIcons().add(Picture.ICON.getImage());

        setupWindow(root());
        getStage().sizeToScene();

        animationTimer.start();
        powerOnButton.fire();

        settings().loadStagePosition(this);
    }

    @Override
    public String getTitle() {
        return APP_TITLE;
    }

    private Parent root() {
        var root = new BorderPane(centerBox(), createMenuBar(), new HBox(10), null, null);
        root.getStyleClass().add(CSS_ROOT);
        return root;
    }

    private Node centerBox() {
        var titleLabel = new Label("ЭЛЕКТРОНИКА  МК 61");
        titleLabel.getStyleClass().add(CSS_TITLE_LABEL);

        var box = new VBox(createDisplay(), titleLabel, createSwitches(), createKeyboardGrid());
        box.setAlignment(Pos.CENTER);
        box.setFillWidth(true);
        return box;
    }

    private MenuBar createMenuBar() {
        return menuBar(
                menu(string(UI, I18N_FILE),
                        menuItem(string(UI, I18N_SAVE, ELLIPSIS), _ -> onSaveMemoryDump()),
                        menuItem(string(UI, I18N_LOAD, ELLIPSIS), _ -> onLoadMemoryDump()),
                        new SeparatorMenuItem(),
                        menuItem(string(UI, I18N_EXIT), _ -> onExit())
                ),
                menu(string(UI, I18N_WINDOW),
                        registersAndMemoryMenuItem()
                ),
                menu(string(UI, I18N_HELP),
                        menuItem(string(UI, I18N_ABOUT, ELLIPSIS), _ -> new AboutDialog().showAndWait())
                )
        );
    }

    private MenuItem registersAndMemoryMenuItem() {
        var menuItem = menuItem(string(UI, I18N_REGISTERS_AND_MEMORY), this::onRegistersAndStackWindow);
        menuItem.setAccelerator(SHORTCUT_1);
        return menuItem;
    }

    private BorderPane createDisplay() {
        for (var cell : digitCells) {
            cell.getStyleClass().add(CSS_LCD);
            HBox.setMargin(cell, new Insets(0, 0, 0, -7));
        }
        for (var cell : dotCells) {
            cell.getStyleClass().add(CSS_DOT_LCD);
            HBox.setMargin(cell, new Insets(0, 0, 0, -6));
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

        var pane = new BorderPane(cellsPane);
        pane.getStyleClass().add(CSS_LCD_PANEL);
        pane.setMouseTransparent(true);
        return pane;
    }

    private GridPane createSwitches() {
        var pane = gridPane(List.of(gridRow(powerSwitch(), trigonometricSwitch(engine))));
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);
        pane.getStyleClass().add(CSS_SWITCH_PANEL);
        return pane;
    }

    private Node createKeyboardGrid() {
        var constraints = new ColumnConstraints();
        constraints.setPercentWidth(20);

        return gridPane(List.of(
                        gridRow(yellowButton("F", KeyboardButton.F),
                                blackButton("ШГ→", "x<0", KeyboardButton.STEP_RIGHT),
                                blackButton("←ШГ", "x=0", KeyboardButton.STEP_LEFT),
                                blackButton("В/О", "x≥0", KeyboardButton.RETURN),
                                blackButton("С/П", "x≠0", KeyboardButton.RUN_STOP)),
                        gridRow(blueButton("K", KeyboardButton.K),
                                blackButton("П→x", "L0", KeyboardButton.LOAD),
                                blackButton("x→П", "L1", KeyboardButton.STORE),
                                blackButton("БП", "L2", KeyboardButton.GOTO),
                                blackButton("ПП", "L3", KeyboardButton.GOSUB)),
                        gridRow(grayButton("7", "sin", "[x]", KeyboardButton.D7),
                                grayButton("8", "cos", "{x}", KeyboardButton.D8),
                                grayButton("9", "tg", "max", KeyboardButton.D9),
                                grayButton("➖", "√¯", "", KeyboardButton.MINUS),
                                grayButton("➗", "1/x", "", KeyboardButton.DIVISION)),
                        gridRow(grayButton("4", "sin⁻¹", "|x|", KeyboardButton.D4),
                                grayButton("5", "cos⁻¹", "ЗН", KeyboardButton.D5),
                                grayButton("6", "tg⁻¹", ".⃖,", KeyboardButton.D6),
                                grayButton("➕", "π", ".⃗,", KeyboardButton.PLUS),
                                grayButton("✖", "x²", "", KeyboardButton.MULTIPLICATION)),
                        gridRow(grayButton("1", "eˣ", "", KeyboardButton.D1),
                                grayButton("2", "lg", "", KeyboardButton.D2),
                                grayButton("3", "ln", ".‚⃖„", KeyboardButton.D3),
                                grayButton("←→", "xʸ", "․‚⃗„", KeyboardButton.SWAP),
                                grayButton("В↑", "Вх", "СЧ", KeyboardButton.PUSH),
                                eLabel()),
                        gridRow(grayButton("0", "10ˣ", "НОП", KeyboardButton.D0),
                                grayButton("∙", "Ѻ", "⋀", KeyboardButton.DOT),
                                grayButton("/-/", "АВТ", "⋁", KeyboardButton.SIGN),
                                grayButton("ВП", "ПРГ", "⨁", KeyboardButton.EE),
                                redButton("Cx", "CF", "ИНВ", KeyboardButton.CLEAR_X)),
                        gridRow(label(""), abcdLabel("a"), abcdLabel("b"), abcdLabel("c"), abcdLabel("d"))),
                List.of(constraints, constraints, constraints, constraints, constraints),
                List.of(CSS_BUTTON_GRID));
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
        var opacity = 1.0;

        for (int i = 0; i < 12; i++) {
            digitCells[i].setText(LCD_MAP[(int) (ri & 0xF)]);
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
        stackAndMemoryController.getStage().toFront();
    }

    private void onSaveMemoryDump() {
        var file = fileChooser("Сохранить дамп памяти", List.of(EXTENSION_FILTER)).showSaveDialog(getStage());
        if (file == null) return;

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
        if (file == null) return;

        try (var reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            var codes = new int[PROGRAM_MEMORY_SIZE];
            var index = 0;

            var lines = reader.lines().toList();
            outerLoop:
            for (var line : lines) {
                if (line.startsWith("#")) continue;

                var strings = line.trim().split(" ");
                for (var str : strings) {
                    if (index >= codes.length) break outerLoop;
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
        WindowManager.windowManager().getControllerStream()
                .filter(c -> c != this)
                .toList()
                .forEach(c -> ((BaseController) c).onClose());
    }

    private static Label registerLabel(String text) {
        var label = new Label(text);
        label.getStyleClass().add(CSS_REGISTER_LABEL);
        return label;
    }

    private static Label abcdLabel(String text) {
        var label = registerLabel(text);
        setHalignment(label, HPos.CENTER);
        return label;
    }

    private static Label eLabel() {
        var label = registerLabel("e");
        label.getStyleClass().add(CSS_REGISTER_E_LABEL);
        return label;
    }

    private Node blackButton(String text, String fText, KeyboardButton keyboardButton) {
        return buttonNode(text, fText, null, CSS_BLACK_BUTTON, keyboardButton);
    }

    private Node grayButton(String text, String fText, String kText, KeyboardButton keyboardButton) {
        return buttonNode(text, fText, kText, CSS_GRAY_BUTTON, keyboardButton);
    }

    @SuppressWarnings("SameParameterValue")
    private Node redButton(String text, String fText, String kText, KeyboardButton keyboardButton) {
        return buttonNode(text, fText, kText, CSS_RED_BUTTON, keyboardButton);
    }

    @SuppressWarnings("SameParameterValue")
    private Node yellowButton(String text, KeyboardButton keyboardButton) {
        return buttonNode(text, null, null, CSS_F_BUTTON, keyboardButton);
    }

    @SuppressWarnings("SameParameterValue")
    private Node blueButton(String text, KeyboardButton keyboardButton) {
        return buttonNode(text, null, null, CSS_K_BUTTON, keyboardButton);
    }

    private Node buttonNode(String text,
            String fText,
            String kText,
            String buttonClass,
            KeyboardButton keyboardButton)
    {
        var box = new VBox();
        if (fText != null || kText != null) {
            int col = 0;
            var column = new ColumnConstraints();
            column.setPercentWidth(50);

            var upperBox = buttonNodeUpperBox();

            if (fText != null) {
                upperBox.add(buttonNodeUpperLabel(fText, CSS_F_LABEL), col++, 0);
                upperBox.getColumnConstraints().add(column);
            }
            if (kText != null) {
                upperBox.add(buttonNodeUpperLabel(kText, CSS_K_LABEL), col, 0);
                upperBox.getColumnConstraints().add(column);
            }
            box.getChildren().add(upperBox);
        }

        box.getChildren().add(keyPadButton(text, buttonClass, keyboardButton, engine));
        box.setAlignment(Pos.BOTTOM_CENTER);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMaxHeight(Double.MAX_VALUE);
        return box;
    }

    private static GridPane buttonNodeUpperBox() {
        var gridPane = new GridPane(0, 0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        return gridPane;
    }

    private static Label buttonNodeUpperLabel(String text, String cssClass) {
        var label = new Label(text);
        label.getStyleClass().add(cssClass);
        setHalignment(label, HPos.CENTER);
        return label;
    }

    private static Button keyPadButton(String text, String cssClass, KeyboardButton keyboardButton, Engine engine) {
        var button = new Button(text);
        button.getStyleClass().add(CSS_KEYPAD_BUTTON);
        button.getStyleClass().add(cssClass);
        button.setOnAction(_ -> engine.processButton(keyboardButton));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setFocusTraversable(false);
        return button;
    }

    private ToggleButton powerOnButton() {
        var button = new ToggleButton("Вкл");
        button.setOnAction(_ -> onPowerOn());
        button.setFocusTraversable(false);
        return button;
    }

    private ToggleButton powerOffButton() {
        var button = new ToggleButton(" ");
        button.setOnAction(_ -> onPowerOff());
        button.setFocusTraversable(false);
        return button;
    }

    private SegmentedButton powerSwitch() {
        var button = new SegmentedButton(powerOffButton(), powerOnButton);
        setHalignment(button, HPos.LEFT);
        GridPane.setHgrow(button, Priority.ALWAYS);
        return button;
    }

    private static ToggleButton trigonometricButton(AngleMode mode, Engine engine, boolean fire) {
        var button = new ToggleButton(mode.label());
        button.setOnAction(_ -> engine.deviceModel().setAngleMode(mode));
        button.setFocusTraversable(false);
        if (fire) {
            button.fire();
        }
        return button;
    }

    private static SegmentedButton trigonometricSwitch(Engine engine) {
        var button = new SegmentedButton(
                trigonometricButton(AngleMode.RADIAN, engine, true),
                trigonometricButton(AngleMode.GRAD, engine, false),
                trigonometricButton(AngleMode.DEGREE, engine, false)
        );
        setHalignment(button, HPos.RIGHT);
        GridPane.setHgrow(button, Priority.ALWAYS);
        return button;
    }
}