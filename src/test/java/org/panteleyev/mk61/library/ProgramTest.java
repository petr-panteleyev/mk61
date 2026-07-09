// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.engine.DeviceModel.REGISTERS_SIZE;

public class ProgramTest {

    private static List<Arguments> testEmptyProgramArguments() {
        return List.of(
                argumentSet("No-args constructor", new Program()),
                argumentSet("Null values", new Program(null, null, null)),
                argumentSet("Empty lists", new Program(null, List.of(), List.of()))
        );
    }

    @ParameterizedTest
    @MethodSource("testEmptyProgramArguments")
    @DisplayName("Empty program should have memory and registers of correct length")
    public void testEmptyProgram(Program program) {
        assertThat(program.info().title()).isEmpty();
        assertThat(program.info().source()).isEmpty();
        assertThat(program.info().source()).isEmpty();
        assertThat(program.info().description()).isEmpty();

        assertThat(program.registers()).hasSize(REGISTERS_SIZE);
        assertThat(program.cells()).hasSize(PROGRAM_MEMORY_SIZE);
    }
}
