// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.mk61.ui;

import static org.panteleyev.fx.Controller.encodeStyleSheet;
import static org.panteleyev.mk61.Mk61Application.getResourceAsString;

final class StyleSheet {
    static final String CSS_REGISTER_CONTENT = "registerContent";
    static final String CSS_REGISTER_CONTENT_LABEL = "registerContentLabel";
    static final String CSS_REGISTER_CONTENT_HIGHLIGHTED = "registerContentHighlighted";
    static final String CSS_REGISTER_LABEL = "registerLabel";
    static final String CSS_REGISTER_E_LABEL = "registerELabel";
    static final String CSS_ROOT = "root";
    static final String CSS_TITLE_LABEL = "titleLabel";
    static final String CSS_LCD = "lcd";
    static final String CSS_DOT_LCD = "dotLcd";
    static final String CSS_LCD_PANEL = "lcdPanel";
    static final String CSS_SWITCH_PANEL = "switchPanel";
    static final String CSS_MEMORY_PANEL = "memoryPanel";
    static final String CSS_BLACK_BUTTON = "blackButton";
    static final String CSS_GRAY_BUTTON = "grayButton";
    static final String CSS_RED_BUTTON = "redButton";
    static final String CSS_F_BUTTON = "fButton";
    static final String CSS_K_BUTTON = "kButton";
    static final String CSS_BUTTON_GRID = "buttonGrid";
    static final String CSS_KEYPAD_BUTTON = "keyPadButton";
    static final String CSS_F_LABEL = "fLabel";
    static final String CSS_K_LABEL = "kLabel";

    public static final String MAIN = encodeStyleSheet(getResourceAsString("/main.css"));
    public static final String ABOUT_DIALOG = encodeStyleSheet(getResourceAsString("/about-dialog.css"));

    private StyleSheet() {
    }
}
