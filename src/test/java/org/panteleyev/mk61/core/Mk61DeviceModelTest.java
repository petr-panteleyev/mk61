/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class Mk61DeviceModelTest {

    private static List<Arguments> testGetRealPc10Arguments() {
        return List.of(
                argumentSet("Главная ветвь", 0, 0),
                argumentSet("Главная ветвь", 0x98, 98),
                argumentSet("Главная ветвь", 0x99, 99),
                argumentSet("Главная ветвь", 0xA0, 100),
                argumentSet("Главная ветвь", 0xA1, 101),
                argumentSet("Главная ветвь", 0xA4, 104),
                argumentSet("Короткая побочная ветвь", 0xA5, 0),
                argumentSet("Короткая побочная ветвь", 0xA6, 1),
                argumentSet("Короткая побочная ветвь", 0xA7, 2),
                argumentSet("Короткая побочная ветвь", 0xA8, 3),
                argumentSet("Короткая побочная ветвь", 0xA9, 4),
                argumentSet("Короткая побочная ветвь", 0xB0, 5),
                argumentSet("Короткая побочная ветвь", 0xB1, 6),
                argumentSet("Длинная побочная ветвь", 0xB2, 0),
                argumentSet("Длинная побочная ветвь", 0xB3, 1),
                argumentSet("Длинная побочная ветвь", 0xC0, 8),
                argumentSet("Длинная побочная ветвь", 0xD0, 18),
                argumentSet("Длинная побочная ветвь", 0xE0, 28),
                argumentSet("Длинная побочная ветвь", 0xF0, 38),
                argumentSet("Длинная побочная ветвь", 0xFA, 48),
                argumentSet("Длинная побочная ветвь", 0xFB, 49),
                argumentSet("Длинная побочная ветвь", 0xFC, 50),
                argumentSet("Длинная побочная ветвь", 0xFD, 51),
                argumentSet("Длинная побочная ветвь", 0xFE, 52),
                argumentSet("Длинная побочная ветвь", 0xFF, 53)
        );
    }

    @ParameterizedTest
    @MethodSource("testGetRealPc10Arguments")
    public void testGetRealPc10(int pc, int expected) {
        assertEquals(expected, Mk61DeviceModel.getRealPc10(pc));
    }

}
