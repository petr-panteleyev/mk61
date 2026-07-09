// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.panteleyev.mk61.jaxb.Mk61Register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.panteleyev.mk61.TestUtil.randomBoolean;
import static org.panteleyev.mk61.TestUtil.randomByte;
import static org.panteleyev.mk61.TestUtil.randomInt;
import static org.panteleyev.mk61.TestUtil.randomLong;
import static org.panteleyev.mk61.TestUtil.randomString;

@DisplayName("JAXB conversion")
public class LibraryConverterTest {

    @Test
    @DisplayName("should convert memory cell to JAXB memory cell")
    public void testConvertMemoryCell() {
        var addr = randomInt();
        var cell = new MemoryCell(randomByte(), randomString(), randomBoolean());

        var mk61MemoryCell = LibraryConverter.convert(cell, addr);
        assertThat(mk61MemoryCell.getAddr()).isEqualTo(addr);
        assertThat(mk61MemoryCell.getOpCode()).isEqualTo(cell.opCode());
        assertThat(mk61MemoryCell.getOpCodeHex()).isEqualTo(String.format("%02X", cell.opCode()));
        assertThat(mk61MemoryCell.getMnemonics()).isEqualTo(cell.mnemonics());
    }

    @Test
    @DisplayName("should convert JAXB register to program register")
    public void testConvertMk61Register() {
        var value = randomLong();

        var mk61Register = new Mk61Register();
        mk61Register.setValue(value);

        var actual = LibraryConverter.convert(mk61Register);
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("should convert program register to JAXB register")
    public void testConvertProgramRegister() {
        var number = randomInt();
        var value = randomLong();
        var register = new ProgramRegister(value);

        var actual = LibraryConverter.convert(register, number);
        assertThat(actual.getNumber()).isEqualTo(number);
        assertThat(actual.getValue()).isEqualTo(value);
    }
}
