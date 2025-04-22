/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.ui;

import javafx.scene.control.Label;

class RegisterLabel extends Label {
    public RegisterLabel(String text) {
        super(text);
        getStyleClass().add("registerLabel");
    }
}
