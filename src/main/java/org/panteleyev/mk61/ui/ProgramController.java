// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.panteleyev.mk61.engine.DeviceModel;
import org.panteleyev.mk61.library.Decoder;
import org.panteleyev.mk61.library.LibrarySerializer;
import org.panteleyev.mk61.library.Program;
import org.panteleyev.mk61.library.ProgramInfo;
import org.panteleyev.mk61.library.ProgramRegister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.panteleyev.fx.factories.FileChooserFactory.fileChooser;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.MenuFactory.menu;
import static org.panteleyev.fx.factories.MenuFactory.menuBar;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.fx.factories.StringFactory.COLON;
import static org.panteleyev.fx.factories.StringFactory.ELLIPSIS;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.fx.factories.TabFactory.tab;
import static org.panteleyev.fx.factories.TextFieldFactory.textField;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.mk61.Mk61Application.UI;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_AUTHOR;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_CLOSE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_DESCRIPTION;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_FILE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_MK61_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_OPEN;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_OPEN_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_READ_FROM_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_RESET;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_AS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_TO_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SOURCE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_TITLE;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_PROGRAM_DESCRIPTION;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_PROGRAM_INFO_GRID;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_ROOT;

public class ProgramController extends BaseController {
    static final FileChooser.ExtensionFilter EXTENSION_FILTER =
            new FileChooser.ExtensionFilter(string(UI, I18N_MK61_PROGRAM), "*.mk61");

    private final DeviceModel deviceModel;

    private final MemoryPanel memoryPanel = new MemoryPanel();
    private final RegistersPanel registersPanel = new RegistersPanel();

    private final TextField titleField = infoTextField();
    private final TextField authorField = infoTextField();
    private final TextField sourceField = infoTextField();
    private final TextArea descriptionArea = new TextArea();

    private Program program = new Program();
    private File file;

    private final AtomicBoolean edited = new AtomicBoolean(false);
    private final ChangeListener<String> textFieldChangeListener = (_, _, _) -> {
        edited.set(true);
        updateTitle();
    };

    public ProgramController(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;

        var center = new BorderPane();
        center.getStyleClass().add("programWindowTab");
        center.setTop(infoPane());

        var tabPane = new TabPane(
                programTab(),
                registersTab(),
                descriptionTab()
        );
        tabPane.getSelectionModel().selectFirst();
        center.setCenter(tabPane);
        BorderPane.setMargin(tabPane, new Insets(10));

        var root = new BorderPane(center, createMenuBar(), null, null, null);
        root.getStyleClass().add(CSS_ROOT);

        setupWindow(root);
        getStage().setResizable(false);
    }

    @Override
    public String getTitle() {
        var builder = new StringBuilder(string(UI, I18N_PROGRAM));
        if (file != null) {
            builder.append(" - ").append(file.getAbsolutePath());
        }
        if (edited.get()) {
            builder.append(" (*)");
        }
        return builder.toString();
    }

    private void updateTitle() {
        getStage().setTitle(getTitle());
    }

    private MenuBar createMenuBar() {
        return menuBar(
                menu(string(UI, I18N_FILE),
                        openMenuItem(),
                        new SeparatorMenuItem(),
                        saveMenuItem(),
                        menuItem(string(UI, I18N_SAVE_AS, ELLIPSIS), this::onSaveAs),
                        new SeparatorMenuItem(),
                        menuItem(string(UI, I18N_CLOSE), _ -> onClose())
                ),
                menu(string(UI, I18N_PROGRAM),
                        readFromMemoryMenuItem(),
                        saveToMemoryMenuItem(),
                        new SeparatorMenuItem(),
                        menuItem(string(UI, I18N_RESET), this::onReset)
                )
        );
    }

    private GridPane infoPane() {
        var grid = gridPane(List.of(
                gridRow(infoLabel(string(UI, I18N_TITLE, COLON)), titleField),
                gridRow(infoLabel(string(UI, I18N_AUTHOR, COLON)), authorField),
                gridRow(infoLabel(string(UI, I18N_SOURCE, COLON)), sourceField))
        );
        grid.getStyleClass().add(CSS_PROGRAM_INFO_GRID);
        BorderPane.setMargin(grid, new Insets(10));

        titleField.textProperty().addListener(textFieldChangeListener);
        authorField.textProperty().addListener(textFieldChangeListener);
        sourceField.textProperty().addListener(textFieldChangeListener);

        return grid;
    }

    private Tab programTab() {
        var pane = new BorderPane(memoryPanel);
        BorderPane.setMargin(memoryPanel, new Insets(20, 5, 5, 5));
        return tab(string(UI, I18N_PROGRAM), pane);
    }

    private Tab registersTab() {
        registersPanel.turnOn();
        var pane = new BorderPane(registersPanel);
        BorderPane.setMargin(registersPanel, new Insets(20, 5, 5, 5));
        return tab(string(UI, I18N_REGISTERS), pane);
    }

    private Tab descriptionTab() {
        descriptionArea.getStyleClass().add(CSS_PROGRAM_DESCRIPTION);
        descriptionArea.setWrapText(true);
        descriptionArea.textProperty().addListener(textFieldChangeListener);
        var pane = new BorderPane(descriptionArea);
        BorderPane.setMargin(descriptionArea, new Insets(20, 5, 5, 5));
        return tab(string(UI, I18N_DESCRIPTION), pane);
    }

    private void updateControls() {
        titleField.setText(program.info().title());
        authorField.setText(program.info().author());
        sourceField.setText(program.info().source());
        descriptionArea.setText(program.info().description());

        memoryPanel.showMemory(program.cells(), true);
        registersPanel.showRegisters(program.registers());

        updateTitle();
        getStage().sizeToScene();
    }

    private void readFromMemory(ActionEvent ignored) {
        var steps = Decoder.decodeMemory(deviceModel.getMemory());

        var registers = Arrays.stream(deviceModel.getRegisters())
                .mapToObj(ProgramRegister::new)
                .toList();

        program = new Program(program.info(), steps, registers);
        edited.set(true);
        updateControls();
    }

    private void saveToMemory(ActionEvent ignored) {
        if (program == null) return;
        deviceModel.uploadProgram(program);
        deviceModel.uploadRegisters(program);
    }

    private void onReset(ActionEvent ignored) {
        program = new Program();
        file = null;
        updateControls();
    }

    private void onOpen(ActionEvent ignored) {
        var newFile = fileChooser(string(UI, I18N_OPEN_PROGRAM), List.of(EXTENSION_FILTER)).showOpenDialog(getStage());
        if (newFile == null) return;

        try (var in = new FileInputStream(newFile)) {
            program = LibrarySerializer.loadProgram(in);
            file = newFile;
            updateControls();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void onSave(ActionEvent ignored) {
        if (file == null) {
            file = fileChooser(string(UI, I18N_SAVE_PROGRAM), List.of(EXTENSION_FILTER)).showSaveDialog(getStage());
            if (file == null) return;
        }

        var title = titleField.getText();
        var author = authorField.getText();
        var source = sourceField.getText();
        var description = descriptionArea.getText();

        var newProgramRegisters = new ArrayList<ProgramRegister>();
        for (long value : registersPanel.getRegisterValues()) {
            newProgramRegisters.add(new ProgramRegister(value));
        }

        program = new Program(new ProgramInfo(title, author, source, description), program.cells(),
                newProgramRegisters);

        try (var out = new FileOutputStream(file)) {
            LibrarySerializer.saveProgram(program, out);
            edited.set(false);
            updateTitle();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void onSaveAs(ActionEvent ignored) {
        var newFile = fileChooser(string(UI, I18N_SAVE_PROGRAM), List.of(EXTENSION_FILTER)).showSaveDialog(getStage());
        if (newFile == null) return;

        file = newFile;
        onSave(ignored);
    }

    private static Label infoLabel(String text) {
        var label = label(text);
        label.getStyleClass().add("programInfoLabel");
        return label;
    }

    private static TextField infoTextField() {
        return textField("", 40);
    }

    private MenuItem openMenuItem() {
        var menuItem = menuItem(string(UI, I18N_OPEN, ELLIPSIS), this::onOpen);
        menuItem.setAccelerator(Accelerators.SHORTCUT_O);
        return menuItem;
    }

    private MenuItem saveMenuItem() {
        var menuItem = menuItem(string(UI, I18N_SAVE), this::onSave);
        menuItem.setAccelerator(Accelerators.SHORTCUT_S);
        return menuItem;
    }

    private MenuItem readFromMemoryMenuItem() {
        var menuItem = menuItem(string(UI, I18N_READ_FROM_MEMORY), this::readFromMemory);
        menuItem.setAccelerator(Accelerators.SHORTCUT_DOWN);
        return menuItem;
    }

    private MenuItem saveToMemoryMenuItem() {
        var menuItem = menuItem(string(UI, I18N_SAVE_TO_MEMORY), this::saveToMemory);
        menuItem.setAccelerator(Accelerators.SHORTCUT_UP);
        return menuItem;
    }

    public void setProgram(File file, Program program) {
        this.file = file;
        this.program = program;
        updateControls();
        edited.set(false);
        updateTitle();
    }
}
