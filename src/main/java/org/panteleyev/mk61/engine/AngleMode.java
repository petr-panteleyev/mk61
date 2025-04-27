/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.engine;

public enum AngleMode {
    RADIAN(10),
    DEGREE(11),
    GRAD(12);

    private final int mode;

    AngleMode(int mode) {
        this.mode = mode;
    }

    public int mode() {
        return mode;
    }
}
