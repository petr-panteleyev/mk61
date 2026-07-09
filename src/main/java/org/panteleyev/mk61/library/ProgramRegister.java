// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

public record ProgramRegister(long value) {
    public static final ProgramRegister ZERO = new ProgramRegister(0);

    public ProgramRegister {
        if (value < 0) {
            throw new IllegalArgumentException("Register value must be >= 0");
        }
    }
}
