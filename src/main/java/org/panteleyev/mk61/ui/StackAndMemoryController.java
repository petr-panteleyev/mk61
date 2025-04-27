/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.panteleyev.mk61.core.Mk61DeviceModel;
import org.panteleyev.mk61.engine.Register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.panteleyev.fx.BoxFactory.hBox;
import static org.panteleyev.fx.BoxFactory.vBox;
import static org.panteleyev.fx.MenuFactory.menu;
import static org.panteleyev.fx.MenuFactory.menuBar;
import static org.panteleyev.fx.MenuFactory.menuItem;
import static org.panteleyev.fx.grid.GridBuilder.gridPane;
import static org.panteleyev.fx.grid.GridRowBuilder.gridRow;
import static org.panteleyev.mk61.core.Mk61DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.engine.Constants.CALL_STACK_SIZE;
import static org.panteleyev.mk61.engine.Constants.REGISTERS_SIZE;
import static org.panteleyev.mk61.settings.Settings.settings;
import static org.panteleyev.mk61.util.StringUtil.addrToString;
import static org.panteleyev.mk61.util.StringUtil.padToDisplay;
import static org.panteleyev.mk61.util.StringUtil.pcToString;

public class StackAndMemoryController extends BaseController {
    private static final String INITIAL_ADDRESS = "00";
    private static final String INITIAL_REGISTER = padToDisplay(Register.toString(0));
    private static final String REGISTER_OFF = padToDisplay("");

    private final List<Label> registers = new ArrayList<>(REGISTERS_SIZE);
    private final List<Label> callStack = new ArrayList<>(CALL_STACK_SIZE);

    private final Label xLabel = new RegisterContentLabel("");
    private final Label yLabel = new RegisterContentLabel("");
    private final Label zLabel = new RegisterContentLabel("");
    private final Label tLabel = new RegisterContentLabel("");
    private final Label x1Label = new RegisterContentLabel("");

    private final Label pcLabel = new RegisterContentLabel(INITIAL_ADDRESS);

    private final long[] registerValues = new long[REGISTERS_SIZE];

    private final BorderPane root = new BorderPane();

    // Memory panel
    private final Label[] addrs = new Label[PROGRAM_MEMORY_SIZE];
    private final Label[] cells = new Label[PROGRAM_MEMORY_SIZE];
    private int previousPc = 0;

    public StackAndMemoryController() {
        Arrays.fill(registerValues, 0);

        for (int i = 0; i < REGISTERS_SIZE; i++) {
            registers.add(new RegisterContentLabel(""));
        }

        for (int i = 0; i < CALL_STACK_SIZE; i++) {
            callStack.add(new RegisterContentLabel(INITIAL_ADDRESS));
        }

        var center = vBox(10.0,
                hBox(10.0,
                        buildRegistersPanel(),
                        buildStackPanel(),
                        vBox(10.0, buildCallStackPanel(), buildPcPanel())
                ),
                buildMemoryPanel()
        );
        center.getStyleClass().add("registerAndStackPanel");
        center.setMouseTransparent(true);

        root.setTop(createMenuBar());
        root.setCenter(center);

        setupWindow(root);
        getStage().setResizable(false);
        settings().loadStagePosition(this);
    }

    @Override
    public String getTitle() {
        return "Регистры и память";
    }

    private MenuBar createMenuBar() {
        return menuBar(
                menu("Файл",
                        menuItem("Закрыть", _ -> onClose())
                )
        );
    }

    @Override
    public boolean isVisible() {
        return getStage().isShowing();
    }

    public void show() {
        getStage().show();
    }

    public void turnOn() {
        Arrays.fill(registerValues, 0);

        for (var label : registers) {
            label.setText(INITIAL_REGISTER);
        }

        for (var label : callStack) {
            label.setText("00");
        }

        pcLabel.setText("00");

        xLabel.setText(INITIAL_REGISTER);
        yLabel.setText(INITIAL_REGISTER);
        zLabel.setText(INITIAL_REGISTER);
        tLabel.setText(INITIAL_REGISTER);
        x1Label.setText(INITIAL_REGISTER);

        for (var cell : cells) {
            cell.setText("00");
        }
    }

    public void turnOff() {
        for (var label : registers) {
            label.setText(REGISTER_OFF);
        }

        for (var label : callStack) {
            label.setText("  ");
        }

        pcLabel.setText("  ");

        xLabel.setText(REGISTER_OFF);
        yLabel.setText(REGISTER_OFF);
        zLabel.setText(REGISTER_OFF);
        tLabel.setText(REGISTER_OFF);
        x1Label.setText(REGISTER_OFF);

        for (var cell : cells) {
            cell.setText("  ");
        }
    }

    private Node buildStackPanel() {
        var grid = gridPane(List.of(
                gridRow(new RegisterNameLabel("T:"), tLabel),
                gridRow(new RegisterNameLabel("Z:"), zLabel),
                gridRow(new RegisterNameLabel("Y:"), yLabel),
                gridRow(new RegisterNameLabel("X:"), xLabel),
                gridRow(new RegisterNameLabel("X1:"), x1Label)
        ));
        return vBox(5.0,
                new RegisterNameLabel("Стек:"),
                grid
        );
    }

    private Node buildCallStackPanel() {
        var grid = gridPane(List.of(
                gridRow(callStack.get(4)),
                gridRow(callStack.get(3)),
                gridRow(callStack.get(2)),
                gridRow(callStack.get(1)),
                gridRow(callStack.get(0))
        ));
        return vBox(5.0,
                new RegisterNameLabel("В/О:"),
                grid
        );
    }

    private Node buildRegistersPanel() {
        var grid1 = new GridPane();

        int row = 0;
        int column = 0;
        for (var i = 0; i < REGISTERS_SIZE; i++) {
            if (i != 0 && i % 8 == 0) {
                row = 0;
                column += 2;
            }
            grid1.add(new RegisterNameLabel(" " + (Integer.toString(i, 16) + ":").toUpperCase()), column, row);
            grid1.add(registers.get(i), column + 1, row++);
        }

        return vBox(5.0,
                new RegisterNameLabel(" Регистры:"),
                grid1
        );
    }

    private Node buildPcPanel() {
        return hBox(5.0, new RegisterNameLabel("PC:"), pcLabel);
    }

    private Node buildMemoryPanel() {
        var grid = new GridPane(10, 5);

        int row = -1;
        int column = 0;

        for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
            if (i % 10 == 0) {
                row++;
                column = 0;
            }

            addrs[i] = new RegisterNameLabel(addrToString(i) + ":");
            cells[i] = new RegisterContentLabel("00");
            grid.add(addrs[i], column++, row);
            grid.add(cells[i], column++, row);
        }

        var panel = vBox(10, new RegisterNameLabel("Память:"), grid);
        panel.getStyleClass().add("memoryPanel");
        grid.getStyleClass().add("memoryPanel");
        return grid;
    }


    public void showPc(int pc) {
        var effectivePc = Mk61DeviceModel.getRealPc10(pc);

        if (effectivePc == previousPc) {
            return;
        }

        addrs[previousPc].getStyleClass().remove("registerContentHighlighted");
        addrs[previousPc].getStyleClass().add("registerContentLabel");

        addrs[effectivePc].getStyleClass().remove("registerContentLabel");
        addrs[effectivePc].getStyleClass().add("registerContentHighlighted");

        previousPc = effectivePc;
    }

    public void showMemory(int[] bytes) {
        for (int i = 0; i < Math.min(bytes.length, cells.length); i++) {
            cells[i].setText(String.format("%02X", bytes[i]));
        }
    }

    public void renderDeviceModel(Mk61DeviceModel deviceModel) {
        xLabel.setText(padToDisplay(Register.toString(deviceModel.getX())));
        yLabel.setText(padToDisplay(Register.toString(deviceModel.getY())));
        zLabel.setText(padToDisplay(Register.toString(deviceModel.getZ())));
        tLabel.setText(padToDisplay(Register.toString(deviceModel.getT())));
        x1Label.setText(padToDisplay(Register.toString(deviceModel.getX1())));

        for (int i = 0; i < REGISTERS_SIZE; i++) {
            var newValue = deviceModel.getRegister(i);
            if (registerValues[i] != newValue) {
                registerValues[i] = newValue;
                registers.get(i).setText(padToDisplay(Register.toString(newValue)));
            }
        }

        for (int i = 0; i < CALL_STACK_SIZE; i++) {
            callStack.get(i).setText(pcToString(deviceModel.getCallStack(i)));
        }

        pcLabel.setText(pcToString(deviceModel.getPc()));
        showPc(deviceModel.getPc());
        showMemory(deviceModel.getMemory());
    }
}
