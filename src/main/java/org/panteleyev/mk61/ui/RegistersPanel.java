// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.panteleyev.mk61.engine.Register;
import org.panteleyev.mk61.library.ProgramRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.panteleyev.mk61.engine.DeviceModel.REGISTERS_SIZE;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT_LABEL;
import static org.panteleyev.mk61.util.StringUtil.padToDisplay;

public class RegistersPanel extends GridPane {
    private static final String INITIAL_REGISTER = padToDisplay(Register.toString(0));
    private static final String REGISTER_OFF = padToDisplay("");

    private final List<Label> registers = new ArrayList<>(REGISTERS_SIZE);
    private final long[] registerValues = new long[REGISTERS_SIZE];

    public RegistersPanel() {
        super(15, 5);

        Arrays.fill(registerValues, 0);
        for (int i = 0; i < REGISTERS_SIZE; i++) {
            registers.add(registerContentLabel(""));
        }

        int row = 0;
        int column = 0;
        for (var i = 0; i < REGISTERS_SIZE; i++) {
            if (i != 0 && i % 8 == 0) {
                row = 0;
                column += 2;
            }
            add(registerNameLabel((Integer.toString(i, 16) + ":").toUpperCase()), column, row);
            add(registers.get(i), column + 1, row++);
        }
    }

    public void turnOn() {
        Arrays.fill(registerValues, 0);
        for (var label : registers) {
            label.setText(INITIAL_REGISTER);
        }
    }

    public void turnOff() {
        for (var label : registers) {
            label.setText(REGISTER_OFF);
        }
    }

    public void showRegisters(long[] newRegisterValues) {
        for (int i = 0; i < REGISTERS_SIZE; i++) {
            var newValue = newRegisterValues[i];
            if (registerValues[i] != newValue) {
                registerValues[i] = newValue;
                registers.get(i).setText(padToDisplay(Register.toString(newValue)));
            }
        }
    }

    public void showRegisters(List<ProgramRegister> programRegisters) {
        turnOn();
        for (int index = 0; index < programRegisters.size(); index++) {
            var value = programRegisters.get(index).value();
            registerValues[index] = value;
            registers.get(index).setText(padToDisplay(Register.toString(value)));
        }
    }

    public long[] getRegisterValues() {
        return Arrays.copyOf(registerValues, registerValues.length);
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
}
