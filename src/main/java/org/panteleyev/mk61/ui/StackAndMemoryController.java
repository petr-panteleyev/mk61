// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.panteleyev.mk61.engine.DeviceModel;
import org.panteleyev.mk61.engine.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.panteleyev.fx.factories.BoxFactory.hBox;
import static org.panteleyev.fx.factories.BoxFactory.vBox;
import static org.panteleyev.fx.factories.MenuFactory.checkMenuItem;
import static org.panteleyev.fx.factories.MenuFactory.menu;
import static org.panteleyev.fx.factories.MenuFactory.menuBar;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.fx.factories.StringFactory.COLON;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.mk61.Mk61Application.UI;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_CLOSE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_FILE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS_AND_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SHOW_MNEMONICS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_STACK;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_VIEW;
import static org.panteleyev.mk61.engine.DeviceModel.CALL_STACK_SIZE;
import static org.panteleyev.mk61.settings.Settings.settings;
import static org.panteleyev.mk61.ui.Accelerators.SHORTCUT_M;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_AND_STACK_PANEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT_LABEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_ROOT;
import static org.panteleyev.mk61.ui.StyleSheet.SMALL_SPACING;
import static org.panteleyev.mk61.util.StringUtil.padToDisplay;
import static org.panteleyev.mk61.util.StringUtil.pcToString;

public class StackAndMemoryController extends BaseController {
    private static final String INITIAL_ADDRESS = "00";
    private static final String INITIAL_REGISTER = padToDisplay(Register.toString(0));
    private static final String REGISTER_OFF = padToDisplay("");

    private final List<Label> callStack = new ArrayList<>(CALL_STACK_SIZE);

    private final Label xLabel = registerContentLabel("");
    private final Label yLabel = registerContentLabel("");
    private final Label zLabel = registerContentLabel("");
    private final Label tLabel = registerContentLabel("");
    private final Label x1Label = registerContentLabel("");

    private final Label pcLabel = registerContentLabel(INITIAL_ADDRESS);

//    private final long[] registerValues = new long[REGISTERS_SIZE];

    // Registers panel
    private final RegistersPanel registersPanel = new RegistersPanel();

    // Memory panel
    private final MemoryPanel memoryPanel = new MemoryPanel();

    private final AtomicBoolean showMnemonics = new AtomicBoolean(true);

    public StackAndMemoryController() {
        for (int i = 0; i < CALL_STACK_SIZE; i++) {
            callStack.add(registerContentLabel(INITIAL_ADDRESS));
        }

        var center = vBox(20.0,
                hBox(10.0,
                        buildRegistersPanel(),
                        buildStackPanel(),
                        vBox(10.0, buildCallStackPanel(), buildPcPanel())
                ),
                buildMemoryPanel()
        );
        center.getStyleClass().add(CSS_REGISTER_AND_STACK_PANEL);
        center.setMouseTransparent(true);

        var root = new BorderPane(center, createMenuBar(), null, null, null);
        root.getStyleClass().add(CSS_ROOT);
        setupWindow(root);

        getStage().setResizable(false);
        settings().loadStagePosition(this);
    }

    @Override
    public String getTitle() {
        return string(UI, I18N_REGISTERS_AND_MEMORY);
    }

    private MenuBar createMenuBar() {
        return menuBar(
                menu(string(UI, I18N_FILE),
                        menuItem(string(UI, I18N_CLOSE), _ -> onClose())
                ),
                menu(string(UI, I18N_VIEW),
                        showMnemonicsMenuItem())
        );
    }

    public void turnOn() {
        for (var label : callStack) {
            label.setText("00");
        }

        pcLabel.setText("00");

        xLabel.setText(INITIAL_REGISTER);
        yLabel.setText(INITIAL_REGISTER);
        zLabel.setText(INITIAL_REGISTER);
        tLabel.setText(INITIAL_REGISTER);
        x1Label.setText(INITIAL_REGISTER);

        registersPanel.turnOn();
        memoryPanel.turnOn();
    }

    public void turnOff() {
        for (var label : callStack) {
            label.setText("  ");
        }

        pcLabel.setText("  ");

        xLabel.setText(REGISTER_OFF);
        yLabel.setText(REGISTER_OFF);
        zLabel.setText(REGISTER_OFF);
        tLabel.setText(REGISTER_OFF);
        x1Label.setText(REGISTER_OFF);

        registersPanel.turnOff();
        memoryPanel.turnOff();
    }

    private Node buildStackPanel() {
        return vBox(SMALL_SPACING,
                registerNameLabel(string(UI, I18N_STACK, COLON)),
                gridPane(List.of(
                        gridRow(registerNameLabel("T:"), tLabel),
                        gridRow(registerNameLabel("Z:"), zLabel),
                        gridRow(registerNameLabel("Y:"), yLabel),
                        gridRow(registerNameLabel("X:"), xLabel),
                        gridRow(registerNameLabel("X1:"), x1Label)
                ))
        );
    }

    private Node buildCallStackPanel() {
        return vBox(SMALL_SPACING,
                registerNameLabel("В/О:"),
                gridPane(List.of(
                        gridRow(callStack.get(4)),
                        gridRow(callStack.get(3)),
                        gridRow(callStack.get(2)),
                        gridRow(callStack.get(1)),
                        gridRow(callStack.get(0))
                ))
        );
    }

    private Node buildRegistersPanel() {
        return vBox(SMALL_SPACING,
                registerNameLabel(string(UI, I18N_REGISTERS, COLON)),
                registersPanel
        );
    }

    private Node buildMemoryPanel() {
        return vBox(SMALL_SPACING,
                registerNameLabel("Память:"),
                memoryPanel
        );
    }

    private Node buildPcPanel() {
        return hBox(SMALL_SPACING, registerNameLabel("PC:"), pcLabel);
    }

    public void showPc(int pc) {
        memoryPanel.showPc(DeviceModel.getRealPc10(pc));
    }

    public void showMemory(int[] bytes) {
        memoryPanel.showMemory(bytes, showMnemonics.get());
        getStage().sizeToScene();
    }

    public void renderDeviceModel(DeviceModel deviceModel) {
        xLabel.setText(padToDisplay(Register.toString(deviceModel.getX())));
        yLabel.setText(padToDisplay(Register.toString(deviceModel.getY())));
        zLabel.setText(padToDisplay(Register.toString(deviceModel.getZ())));
        tLabel.setText(padToDisplay(Register.toString(deviceModel.getT())));
        x1Label.setText(padToDisplay(Register.toString(deviceModel.getX1())));

        registersPanel.showRegisters(deviceModel.getRegisters());

        var callStackValues = deviceModel.getCallStack();
        for (int i = 0; i < CALL_STACK_SIZE; i++) {
            callStack.get(i).setText(pcToString(callStackValues[i]));
        }

        pcLabel.setText(pcToString(deviceModel.getPc()));
        showMemory(deviceModel.getMemory());
        showPc(deviceModel.getPc());
    }

    private static Label registerNameLabel(String text) {
        var label = new Label(text);
        label.setEllipsisString("");
        label.getStyleClass().add(CSS_REGISTER_CONTENT_LABEL);
        return label;
    }

    private static Label registerContentLabel(String text) {
        var label = new Label(text);
        label.setEllipsisString("");
        label.getStyleClass().add(CSS_REGISTER_CONTENT);
        return label;
    }

    private MenuItem showMnemonicsMenuItem() {
        var menuItem = checkMenuItem(string(UI, I18N_SHOW_MNEMONICS));
        menuItem.setSelected(showMnemonics.get());
        menuItem.setOnAction(_ -> showMnemonics.set(!showMnemonics.get()));
        menuItem.setAccelerator(SHORTCUT_M);
        return menuItem;
    }
}
