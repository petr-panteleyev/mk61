// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.engine;

public record Indicator(long indicator, int dots) {
    public static final Indicator EMPTY = new Indicator(0xFFFF_FFFF_FFFFL);

    public Indicator() {
        this(EMPTY.indicator);
    }

    public Indicator(long indicator) {
        this(indicator, 0);
    }

    @Override
    public String toString() {
        return String.format("IR[indicator=%X, dots=%d]", indicator, dots);
    }
}
