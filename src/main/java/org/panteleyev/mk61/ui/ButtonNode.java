/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.ui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.panteleyev.mk61.engine.KeyboardButton;

import java.util.function.Consumer;

public class ButtonNode extends VBox {

    public ButtonNode(String text,
            String fText,
            String kText,
            String buttonClass,
            KeyboardButton keyboardButton,
            Consumer<KeyboardButton> handler) {

        if (!fText.isEmpty() || !kText.isEmpty()) {
            int col = 0;
            var upperBox = new GridPane(5, 5);
            var column = new ColumnConstraints();
            column.setPercentWidth(50);

            upperBox.setAlignment(Pos.CENTER);
            upperBox.setMaxWidth(Double.MAX_VALUE);
            if (!fText.isEmpty()) {
                var fLabel = new Label(fText);
                fLabel.getStyleClass().add("fLabel");
                upperBox.add(fLabel, col++, 0);
                upperBox.getColumnConstraints().add(column);
                GridPane.setHalignment(fLabel, HPos.CENTER);
            }
            if (!kText.isEmpty()) {
                var kLabel = new Label(kText);
                kLabel.getStyleClass().add("kLabel");
                upperBox.add(kLabel, col, 0);
                upperBox.getColumnConstraints().add(column);
                GridPane.setHalignment(kLabel, HPos.CENTER);
            }
            getChildren().add(upperBox);
        }

        var button = new Button(text);
        button.getStyleClass().add("keyPadButton");
        button.getStyleClass().add(buttonClass);
        button.setOnAction(_ -> handler.accept(keyboardButton));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setFocusTraversable(false);

        getChildren().add(button);
        setAlignment(Pos.BOTTOM_CENTER);

        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }
}
