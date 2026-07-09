// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.util;

import static org.panteleyev.mk61.engine.Constants.DISPLAY_SIZE;

public final class StringUtil {
    public static String pcToString(int pc) {
        var result = Integer.toString((pc & 0xF0) >> 4, 16) + Integer.toString(pc & 0xF, 16);
        return result.toUpperCase();
    }

    public static String addrToString(int addr) {
        return addr <= 99 ? String.format("%02d", addr) : String.format("A%1d", addr - 100);
    }

    public static String padToDisplay(String s) {
        return padRight(s, DISPLAY_SIZE);
    }

    public static String padRight(String s, int size) {
        var padCount = size - s.length();
        return padCount > 0 ? s + " ".repeat(padCount) : s;
    }

    private StringUtil() {
    }
}
