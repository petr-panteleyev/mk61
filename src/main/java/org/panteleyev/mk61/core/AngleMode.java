/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.core;

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
