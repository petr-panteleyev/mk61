// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import java.util.ArrayList;
import java.util.List;

import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.engine.DeviceModel.REGISTERS_SIZE;

public record Program(ProgramInfo info, List<MemoryCell> cells, List<ProgramRegister> registers) {
    public Program {
        if (info == null) {
            info = new ProgramInfo();
        }

        if (cells == null || cells.isEmpty()) {
            cells = new ArrayList<>(PROGRAM_MEMORY_SIZE);
            for (int index = 0; index < PROGRAM_MEMORY_SIZE; index++) {
                cells.add(MemoryCell.EMPTY);
            }
        } else if (cells.size() != PROGRAM_MEMORY_SIZE) {
            throw new IllegalArgumentException("Cells must be of size " + PROGRAM_MEMORY_SIZE);
        }

        if (registers == null || registers.isEmpty()) {
            registers = new ArrayList<>(REGISTERS_SIZE);
            for (int index = 0; index < REGISTERS_SIZE; index++) {
                registers.add(ProgramRegister.ZERO);
            }
        } else if (registers.size() != REGISTERS_SIZE) {
            throw new IllegalArgumentException("Registers must be of size " + REGISTERS_SIZE);
        }
    }

    public Program() {
        this(null, null, null);
    }
}
