/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
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
        var padCount = DISPLAY_SIZE - s.length();
        return padCount > 0 ? s + " ".repeat(DISPLAY_SIZE - s.length()) : s;
    }

    private StringUtil() {
    }
}
