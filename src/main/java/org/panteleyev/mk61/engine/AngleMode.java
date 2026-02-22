// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.engine;

public enum AngleMode {
    RADIAN(10, "Р"),
    DEGREE(11, "Г"),
    GRAD(12, "ГРД");

    private final int mode;
    private final String label;

    AngleMode(int mode, String label) {
        this.mode = mode;
        this.label = label;
    }

    public int mode() {
        return mode;
    }

    public String label() {
        return label;
    }
}
