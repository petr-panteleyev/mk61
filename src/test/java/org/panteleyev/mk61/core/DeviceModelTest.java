// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.panteleyev.mk61.engine.DeviceModel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class DeviceModelTest {

    private static List<Arguments> testGetRealPc10Arguments() {
        return List.of(
                argumentSet("Main branch", 0, 0),
                argumentSet("Main branch", 0x98, 98),
                argumentSet("Main branch", 0x99, 99),
                argumentSet("Main branch", 0xA0, 100),
                argumentSet("Main branch", 0xA1, 101),
                argumentSet("Main branch", 0xA4, 104),
                argumentSet("Short additional branch", 0xA5, 0),
                argumentSet("Short additional branch", 0xA6, 1),
                argumentSet("Short additional branch", 0xA7, 2),
                argumentSet("Short additional branch", 0xA8, 3),
                argumentSet("Short additional branch", 0xA9, 4),
                argumentSet("Short additional branch", 0xB0, 5),
                argumentSet("Short additional branch", 0xB1, 6),
                argumentSet("Long additional branch", 0xB2, 0),
                argumentSet("Long additional branch", 0xB3, 1),
                argumentSet("Long additional branch", 0xC0, 8),
                argumentSet("Long additional branch", 0xD0, 18),
                argumentSet("Long additional branch", 0xE0, 28),
                argumentSet("Long additional branch", 0xF0, 38),
                argumentSet("Long additional branch", 0xFA, 48),
                argumentSet("Long additional branch", 0xFB, 49),
                argumentSet("Long additional branch", 0xFC, 50),
                argumentSet("Long additional branch", 0xFD, 51),
                argumentSet("Long additional branch", 0xFE, 52),
                argumentSet("Long additional branch", 0xFF, 53)
        );
    }

    @ParameterizedTest
    @MethodSource("testGetRealPc10Arguments")
    public void testGetRealPc10(int pc, int expected) {
        assertThat(DeviceModel.getRealPc10(pc)).isEqualTo(expected);
    }
}
