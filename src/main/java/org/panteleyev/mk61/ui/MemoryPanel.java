// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.panteleyev.mk61.library.Decoder;
import org.panteleyev.mk61.library.MemoryCell;

import java.util.List;

import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_CONTENT;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT_LABEL;
import static org.panteleyev.mk61.util.StringUtil.padRight;

public class MemoryPanel extends GridPane {
    private final Label[] cells = new Label[PROGRAM_MEMORY_SIZE];
    private static final int MEMORY_CELL_WIDTH = 6;

    public MemoryPanel() {
        super(20, 10);

        for (int i = 0; i <= 9; i++) {
            add(registerNameLabel(Integer.toString(i)), i + 1, 0);
        }

        int row = 0;
        int column = 0;

        for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
            if (i % 10 == 0) {
                row++;
                column = 1;
                add(registerNameLabel(Integer.toString(row - 1, 16).toUpperCase() + " "), 0, row);
            }

            cells[i] = registerContentLabel(padRight("00", MEMORY_CELL_WIDTH));
            add(cells[i], column++, row);
        }
    }

    public void showMemory(int[] bytes, boolean showMnemonics) {
        showMemory(Decoder.decodeMemory(bytes), showMnemonics);
    }

    public void showMemory(List<MemoryCell> memoryCells, boolean showMnemonics) {
        for (int i = 0; i < Math.min(memoryCells.size(), cells.length); i++) {
            if (showMnemonics) {
                cells[i].setText(padRight(memoryCells.get(i).mnemonics(), MEMORY_CELL_WIDTH));
            } else {
                cells[i].setText(padRight(String.format("%02X", memoryCells.get(i).opCode()), MEMORY_CELL_WIDTH));
            }
        }
    }

    public void showPc(int pc) {
        for (var cell : cells) {
            var style = cell.getStyleClass();
            style.remove(CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED);
            if (!style.contains(CSS_MEMORY_CELL_CONTENT)) {
                style.add(CSS_MEMORY_CELL_CONTENT);
            }
        }

        var style = cells[pc].getStyleClass();
        style.remove(CSS_MEMORY_CELL_CONTENT);
        if (!style.contains(CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED)) {
            style.add(CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED);
        }
    }

    public void turnOn() {
        for (var cell : cells) {
            cell.setText(padRight("00", MEMORY_CELL_WIDTH));
        }
    }

    public void turnOff() {
        for (var cell : cells) {
            cell.setText(padRight("", MEMORY_CELL_WIDTH));
        }
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
        label.getStyleClass().add(CSS_MEMORY_CELL_CONTENT);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }
}
