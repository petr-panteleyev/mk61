/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.ui;

import javafx.scene.control.Label;

class RegisterNameLabel extends Label {
    public RegisterNameLabel(String text) {
        super(text);
        getStyleClass().add("registerContentLabel");
    }
}