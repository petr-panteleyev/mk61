// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import org.panteleyev.mk61.jaxb.Mk61MemoryCell;
import org.panteleyev.mk61.jaxb.Mk61Register;

public final class LibraryConverter {

    public static Mk61MemoryCell convert(MemoryCell cell, int index) {
        var xmlStep = new Mk61MemoryCell();
        xmlStep.setAddr(index);
        xmlStep.setOpCode(cell.opCode());
        xmlStep.setOpCodeHex(String.format("%02X", cell.opCode()));
        xmlStep.setMnemonics(cell.mnemonics());
        return xmlStep;
    }

    public static Mk61Register convert(ProgramRegister register, int index) {
        var xmlRegister = new Mk61Register();
        xmlRegister.setNumber(index);
        xmlRegister.setValue(register.value());
        return xmlRegister;
    }

    public static ProgramRegister convert(Mk61Register xmlRegister) {
        return new ProgramRegister(xmlRegister.getValue());
    }

    private LibraryConverter() {
    }
}
