/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.engine;

public record IR(long indicator, int dots) {
    public static final IR EMPTY = new IR(0xFFFF_FFFF_FFFFL);

    public IR(long indicator) {
        this(indicator, 0);
    }

    @Override
    public String toString() {
        return String.format("IR[indicator=%X, dots=%d]", indicator, dots);
    }
}
