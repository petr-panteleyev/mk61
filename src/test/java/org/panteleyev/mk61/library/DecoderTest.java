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

public class DecoderTest {
    private static List<Arguments> testDecodeOpcodeArguments() {
        return List.of(
                Arguments.of(0x00, "0"),
                Arguments.of(0x01, "1"),
                Arguments.of(0x02, "2"),
                Arguments.of(0x03, "3"),
                Arguments.of(0x04, "4"),
                Arguments.of(0x05, "5"),
                Arguments.of(0x06, "6"),
                Arguments.of(0x07, "7"),
                Arguments.of(0x08, "8"),
                Arguments.of(0x09, "9"),
                //
                Arguments.of(0x10, "+"),
                Arguments.of(0x11, "-"),
                Arguments.of(0x12, "×"),
                Arguments.of(0x13, "÷"),
                //
                Arguments.of(0x14, "↔"),
                Arguments.of(0x0E, "В↑"),
                Arguments.of(0x0A, "∙"),
                Arguments.of(0x0B, "/-/"),
                Arguments.of(0x0C, "ВП"),
                Arguments.of(0x0D, "Cx"),
                Arguments.of(0x50, "С/П"),
                Arguments.of(0x51, "БП"),
                Arguments.of(0x52, "В/О"),
                Arguments.of(0x53, "ПП"),
                //
                Arguments.of(0x15, "F 10ˣ"),
                Arguments.of(0x17, "F lg"),
                Arguments.of(0x18, "F ln"),
                Arguments.of(0x16, "F eˣ"),
                Arguments.of(0x19, "F sin⁻¹"),
                Arguments.of(0x1A, "F cos⁻¹"),
                Arguments.of(0x1B, "F tg⁻¹"),
                Arguments.of(0x1C, "F sin"),
                Arguments.of(0x1D, "F cos"),
                Arguments.of(0x1E, "F tg"),
                Arguments.of(0x20, "F π"),
                Arguments.of(0x21, "F √‾"),
                Arguments.of(0x22, "F x²"),
                Arguments.of(0x23, "F 1/x"),
                Arguments.of(0x24, "F xʸ"),
                Arguments.of(0x0F, "F Вх"),
                Arguments.of(0x25, "F \uD83D\uDDD8"),
                //
                Arguments.of(0x5C, "F x<0"),
                Arguments.of(0x5E, "F x=0"),
                Arguments.of(0x59, "F x≥0"),
                Arguments.of(0x57, "F x≠0"),
                //
                Arguments.of(0x5D, "F L0"),
                Arguments.of(0x5B, "F L1"),
                Arguments.of(0x58, "F L2"),
                Arguments.of(0x5A, "F L3"),
                // x→П
                Arguments.of(0x40, "x→П 0"),
                Arguments.of(0x41, "x→П 1"),
                Arguments.of(0x42, "x→П 2"),
                Arguments.of(0x43, "x→П 3"),
                Arguments.of(0x44, "x→П 4"),
                Arguments.of(0x45, "x→П 5"),
                Arguments.of(0x46, "x→П 6"),
                Arguments.of(0x47, "x→П 7"),
                Arguments.of(0x48, "x→П 8"),
                Arguments.of(0x49, "x→П 9"),
                Arguments.of(0x4A, "x→П a"),
                Arguments.of(0x4B, "x→П b"),
                Arguments.of(0x4C, "x→П c"),
                Arguments.of(0x4D, "x→П d"),
                Arguments.of(0x4E, "x→П e"),
                // П→x
                Arguments.of(0x60, "П→x 0"),
                Arguments.of(0x61, "П→x 1"),
                Arguments.of(0x62, "П→x 2"),
                Arguments.of(0x63, "П→x 3"),
                Arguments.of(0x64, "П→x 4"),
                Arguments.of(0x65, "П→x 5"),
                Arguments.of(0x66, "П→x 6"),
                Arguments.of(0x67, "П→x 7"),
                Arguments.of(0x68, "П→x 8"),
                Arguments.of(0x69, "П→x 9"),
                Arguments.of(0x6A, "П→x a"),
                Arguments.of(0x6B, "П→x b"),
                Arguments.of(0x6C, "П→x c"),
                Arguments.of(0x6D, "П→x d"),
                Arguments.of(0x6E, "П→x e"),
                //
                Arguments.of(0x54, "K НОП"),
                // K БП
                Arguments.of(0x80, "K БП 0"),
                Arguments.of(0x81, "K БП 1"),
                Arguments.of(0x82, "K БП 2"),
                Arguments.of(0x83, "K БП 3"),
                Arguments.of(0x84, "K БП 4"),
                Arguments.of(0x85, "K БП 5"),
                Arguments.of(0x86, "K БП 6"),
                Arguments.of(0x87, "K БП 7"),
                Arguments.of(0x88, "K БП 8"),
                Arguments.of(0x89, "K БП 9"),
                Arguments.of(0x8A, "K БП a"),
                Arguments.of(0x8B, "K БП b"),
                Arguments.of(0x8C, "K БП c"),
                Arguments.of(0x8D, "K БП d"),
                Arguments.of(0x8E, "K БП e"),
                // K ПП
                Arguments.of(0xA0, "K ПП 0"),
                Arguments.of(0xA1, "K ПП 1"),
                Arguments.of(0xA2, "K ПП 2"),
                Arguments.of(0xA3, "K ПП 3"),
                Arguments.of(0xA4, "K ПП 4"),
                Arguments.of(0xA5, "K ПП 5"),
                Arguments.of(0xA6, "K ПП 6"),
                Arguments.of(0xA7, "K ПП 7"),
                Arguments.of(0xA8, "K ПП 8"),
                Arguments.of(0xA9, "K ПП 9"),
                Arguments.of(0xAA, "K ПП a"),
                Arguments.of(0xAB, "K ПП b"),
                Arguments.of(0xAC, "K ПП c"),
                Arguments.of(0xAD, "K ПП d"),
                Arguments.of(0xAE, "K ПП e"),
                // K x=0
                Arguments.of(0xE0, "K x=0 0"),
                Arguments.of(0xE1, "K x=0 1"),
                Arguments.of(0xE2, "K x=0 2"),
                Arguments.of(0xE3, "K x=0 3"),
                Arguments.of(0xE4, "K x=0 4"),
                Arguments.of(0xE5, "K x=0 5"),
                Arguments.of(0xE6, "K x=0 6"),
                Arguments.of(0xE7, "K x=0 7"),
                Arguments.of(0xE8, "K x=0 8"),
                Arguments.of(0xE9, "K x=0 9"),
                Arguments.of(0xEA, "K x=0 a"),
                Arguments.of(0xEB, "K x=0 b"),
                Arguments.of(0xEC, "K x=0 c"),
                Arguments.of(0xED, "K x=0 d"),
                Arguments.of(0xEE, "K x=0 e"),
                // K x<0
                Arguments.of(0xC0, "K x<0 0"),
                Arguments.of(0xC1, "K x<0 1"),
                Arguments.of(0xC2, "K x<0 2"),
                Arguments.of(0xC3, "K x<0 3"),
                Arguments.of(0xC4, "K x<0 4"),
                Arguments.of(0xC5, "K x<0 5"),
                Arguments.of(0xC6, "K x<0 6"),
                Arguments.of(0xC7, "K x<0 7"),
                Arguments.of(0xC8, "K x<0 8"),
                Arguments.of(0xC9, "K x<0 9"),
                Arguments.of(0xCA, "K x<0 a"),
                Arguments.of(0xCB, "K x<0 b"),
                Arguments.of(0xCC, "K x<0 c"),
                Arguments.of(0xCD, "K x<0 d"),
                Arguments.of(0xCE, "K x<0 e"),
                // K x≥0
                Arguments.of(0x90, "K x≥0 0"),
                Arguments.of(0x91, "K x≥0 1"),
                Arguments.of(0x92, "K x≥0 2"),
                Arguments.of(0x93, "K x≥0 3"),
                Arguments.of(0x94, "K x≥0 4"),
                Arguments.of(0x95, "K x≥0 5"),
                Arguments.of(0x96, "K x≥0 6"),
                Arguments.of(0x97, "K x≥0 7"),
                Arguments.of(0x98, "K x≥0 8"),
                Arguments.of(0x99, "K x≥0 9"),
                Arguments.of(0x9A, "K x≥0 a"),
                Arguments.of(0x9B, "K x≥0 b"),
                Arguments.of(0x9C, "K x≥0 c"),
                Arguments.of(0x9D, "K x≥0 d"),
                Arguments.of(0x9E, "K x≥0 e"),
                // K x≠0
                Arguments.of(0x70, "K x≠0 0"),
                Arguments.of(0x71, "K x≠0 1"),
                Arguments.of(0x72, "K x≠0 2"),
                Arguments.of(0x73, "K x≠0 3"),
                Arguments.of(0x74, "K x≠0 4"),
                Arguments.of(0x75, "K x≠0 5"),
                Arguments.of(0x76, "K x≠0 6"),
                Arguments.of(0x77, "K x≠0 7"),
                Arguments.of(0x78, "K x≠0 8"),
                Arguments.of(0x79, "K x≠0 9"),
                Arguments.of(0x7A, "K x≠0 a"),
                Arguments.of(0x7B, "K x≠0 b"),
                Arguments.of(0x7C, "K x≠0 c"),
                Arguments.of(0x7D, "K x≠0 d"),
                Arguments.of(0x7E, "K x≠0 e"),
                // K x→П
                Arguments.of(0xB0, "K x→П 0"),
                Arguments.of(0xB1, "K x→П 1"),
                Arguments.of(0xB2, "K x→П 2"),
                Arguments.of(0xB3, "K x→П 3"),
                Arguments.of(0xB4, "K x→П 4"),
                Arguments.of(0xB5, "K x→П 5"),
                Arguments.of(0xB6, "K x→П 6"),
                Arguments.of(0xB7, "K x→П 7"),
                Arguments.of(0xB8, "K x→П 8"),
                Arguments.of(0xB9, "K x→П 9"),
                Arguments.of(0xBA, "K x→П a"),
                Arguments.of(0xBB, "K x→П b"),
                Arguments.of(0xBC, "K x→П c"),
                Arguments.of(0xBD, "K x→П d"),
                Arguments.of(0xBE, "K x→П e"),
                // K П→x
                Arguments.of(0xD0, "K П→x 0"),
                Arguments.of(0xD1, "K П→x 1"),
                Arguments.of(0xD2, "K П→x 2"),
                Arguments.of(0xD3, "K П→x 3"),
                Arguments.of(0xD4, "K П→x 4"),
                Arguments.of(0xD5, "K П→x 5"),
                Arguments.of(0xD6, "K П→x 6"),
                Arguments.of(0xD7, "K П→x 7"),
                Arguments.of(0xD8, "K П→x 8"),
                Arguments.of(0xD9, "K П→x 9"),
                Arguments.of(0xDA, "K П→x a"),
                Arguments.of(0xDB, "K П→x b"),
                Arguments.of(0xDC, "K П→x c"),
                Arguments.of(0xDD, "K П→x d"),
                Arguments.of(0xDE, "K П→x e"),
                //
                Arguments.of(0x34, "K [x]"),
                Arguments.of(0x35, "K {x}"),
                Arguments.of(0x36, "K max"),
                Arguments.of(0x31, "K |x|"),
                Arguments.of(0x32, "K ЗН"),
                Arguments.of(0x33, "K .⃖,"),
                Arguments.of(0x26, "K .⃗,"),
                Arguments.of(0x2A, "K ․‚⃗„"),
                Arguments.of(0x30, "K .‚⃖„"),
                Arguments.of(0x3B, "K СЧ"),
                Arguments.of(0x37, "K ⋀"),
                Arguments.of(0x38, "K ⋁"),
                Arguments.of(0x39, "K ⨁"),
                Arguments.of(0x3A, "K ИНВ")
        );
    }

    @ParameterizedTest
    @MethodSource("testDecodeOpcodeArguments")
    public void testDecodeOpcode(int opCode, String expected) {
        assertThat(Decoder.decodeOpCode(opCode)).isEqualTo(expected);
    }

    private static List<Arguments> testDecodeMemoryArguments() {
        return List.of(
                argumentSet("K П→x 6 1 Fln", new int[]{0xD6, 0x01, 0x18}, List.of(
                        new MemoryCell(0xD6, "K П→x 6", false),
                        new MemoryCell(0x01, "1", false),
                        new MemoryCell(0x18, "F ln", false)
                )),
                argumentSet("БП 1 [x]", new int[]{0x51, 0x01, 0x34}, List.of(
                        new MemoryCell(0x51, "БП", false),
                        new MemoryCell(0x01, "01", false),
                        new MemoryCell(0x34, "K [x]", false)
                )),
                // Jumps
                argumentSet("ПП 0", new int[]{0x53, 0x00}, List.of(
                        new MemoryCell(0x53, "ПП", false),
                        new MemoryCell(0x00, "00", false)
                )),
                argumentSet("ПП 1", new int[]{0x53, 0x01}, List.of(
                        new MemoryCell(0x53, "ПП", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x<0", new int[]{0x5C, 0x01}, List.of(
                        new MemoryCell(0x5C, "F x<0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x=0", new int[]{0x5E, 0x01}, List.of(
                        new MemoryCell(0x5E, "F x=0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x≥0", new int[]{0x59, 0x01}, List.of(
                        new MemoryCell(0x59, "F x≥0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x≠0", new int[]{0x57, 0x01}, List.of(
                        new MemoryCell(0x57, "F x≠0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L0", new int[]{0x5D, 0x01}, List.of(
                        new MemoryCell(0x5D, "F L0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L1", new int[]{0x5B, 0x01}, List.of(
                        new MemoryCell(0x5B, "F L1", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L2", new int[]{0x58, 0x01}, List.of(
                        new MemoryCell(0x58, "F L2", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L3", new int[]{0x5A, 0x01}, List.of(
                        new MemoryCell(0x5A, "F L3", false),
                        new MemoryCell(0x01, "01", false)
                )),
                // Jump at the end
                argumentSet("L3 at the end", new int[]{0x5A}, List.of(
                        new MemoryCell(0x5A, "F L3", false)
                )),
                //
                argumentSet("Invalid", new int[]{0xFF}, List.of(
                        new MemoryCell(0xFF, "FF", true)
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("testDecodeMemoryArguments")
    @DisplayName("should decode memory bytes")
    public void testDecodeMemory(int[] memory, List<MemoryCell> expected) {
        var actual = Decoder.decodeMemory(memory);
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
