// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.panteleyev.mk61.library.Decoder;
import org.panteleyev.mk61.library.MemoryCell;

import java.util.List;

import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.ui.MemoryPanel.MEMORY_CELL_WIDTH;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_CONTENT;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_PANEL;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_MEMORY_CELL_PANEL_HIGHLIGHTED;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_REGISTER_CONTENT_LABEL;
import static org.panteleyev.mk61.util.StringUtil.padRight;

class MemoryCellPanel extends HBox {
    private final Label[] labels = new Label[]{
            label(""), label(""), label(""), label("")
    };

    public MemoryCellPanel() {
        super(3);
        getStyleClass().add(CSS_MEMORY_CELL_PANEL);
        getChildren().addAll(labels);

        for (var l : labels) {
            l.setEllipsisString("");
            l.setMaxWidth(Double.MAX_VALUE);
        }

        highlight(false);
    }

    public void highlight(boolean highlight) {
        var panelStyleClass = getStyleClass();

        var panelClassToRemove = highlight ? CSS_MEMORY_CELL_PANEL : CSS_MEMORY_CELL_PANEL_HIGHLIGHTED;
        var panelClassToAdd = !highlight ? CSS_MEMORY_CELL_PANEL : CSS_MEMORY_CELL_PANEL_HIGHLIGHTED;

        var labelClassToRemove = highlight ? CSS_MEMORY_CELL_CONTENT : CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED;
        var labelClassToAdd = !highlight ? CSS_MEMORY_CELL_CONTENT : CSS_MEMORY_CELL_CONTENT_HIGHLIGHTED;

        panelStyleClass.remove(panelClassToRemove);
        if (!panelStyleClass.contains(panelClassToAdd)) {
            panelStyleClass.add(panelClassToAdd);
        }

        for (var l : labels) {
            l.getStyleClass().remove(labelClassToRemove);
            if (!l.getStyleClass().contains(labelClassToAdd)) {
                l.getStyleClass().add(labelClassToAdd);
            }
        }
    }

    public void show(MemoryCell cell, boolean showMnemonics) {
        if (showMnemonics) {
            var parts = cell.mnemonics().split(" ");
            var index = 0;
            for (; index < parts.length; index++) {
                labels[index].setText(parts[index]);
            }
            for (; index < labels.length - 1; index++) {
                labels[index].setText("");
            }

            var totalLength = 0;
            for (var p : parts) {
                totalLength += p.length();
            }
            labels[3].setText(padRight("", MEMORY_CELL_WIDTH - totalLength));
        } else {
            labels[0].setText(String.format("%02X", cell.opCode()));
            labels[1].setText("");
            labels[2].setText("");
            labels[3].setText(padRight("", MemoryPanel.MEMORY_CELL_WIDTH - 2));
        }
    }

    public void show(String s) {
        labels[0].setText(s);
        labels[3].setText(padRight("", MemoryPanel.MEMORY_CELL_WIDTH - s.length()));
    }
}

public class MemoryPanel extends GridPane {
    private final MemoryCellPanel[] cells = new MemoryCellPanel[PROGRAM_MEMORY_SIZE];
    static final int MEMORY_CELL_WIDTH = 6;

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

            cells[i] = new MemoryCellPanel();
            add(cells[i], column++, row);
        }
    }

    public void showMemory(int[] bytes, boolean showMnemonics) {
        showMemory(Decoder.decodeMemory(bytes), showMnemonics);
    }

    public void showMemory(List<MemoryCell> memoryCells, boolean showMnemonics) {
        for (int i = 0; i < Math.min(memoryCells.size(), cells.length); i++) {
            cells[i].show(memoryCells.get(i), showMnemonics);
        }
    }

    public void showPc(int pc) {
        for (var cell : cells) {
            cell.highlight(false);
        }

        cells[pc].highlight(true);
    }

    public void turnOn() {
    }

    public void turnOff() {
        for (var cell : cells) {
            cell.show("");
        }
    }

    private static Label registerNameLabel(String text) {
        var label = new Label(text);
        label.setEllipsisString("");
        label.getStyleClass().add(CSS_REGISTER_CONTENT_LABEL);
        return label;
    }
}
