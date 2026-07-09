// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

public record MemoryCell(int opCode, String mnemonics, Boolean invalid) {
    public static MemoryCell EMPTY = new MemoryCell(0, "00", false);

    public MemoryCell {
        invalid = invalid != null ? invalid : false;
    }
}
