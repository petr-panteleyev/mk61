/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
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
