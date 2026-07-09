// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

final class Accelerators {
    public static final KeyCombination SHORTCUT_1
            = new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN);
    public static final KeyCombination SHORTCUT_2
            = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN);

    public static final KeyCombination SHORTCUT_DOWN
            = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHORTCUT_DOWN);
    public static final KeyCombination SHORTCUT_M
            = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);
    public static final KeyCombination SHORTCUT_O
            = new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN);
    public static final KeyCombination SHORTCUT_S
            = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);
    public static final KeyCombination SHORTCUT_UP
            = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHORTCUT_DOWN);

    private Accelerators() {
    }
}
