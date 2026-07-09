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
                Arguments.of(0x14, "←→"),
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
                Arguments.of(0x15, "F\u200910ˣ"),
                Arguments.of(0x17, "F\u2009lg"),
                Arguments.of(0x18, "F\u2009ln"),
                Arguments.of(0x16, "F\u2009eˣ"),
                Arguments.of(0x19, "F\u2009sin⁻¹"),
                Arguments.of(0x1A, "F\u2009cos⁻¹"),
                Arguments.of(0x1B, "F\u2009tg⁻¹"),
                Arguments.of(0x1C, "F\u2009sin"),
                Arguments.of(0x1D, "F\u2009cos"),
                Arguments.of(0x1E, "F\u2009tg"),
                Arguments.of(0x20, "F\u2009π"),
                Arguments.of(0x21, "F\u2009√¯"),
                Arguments.of(0x22, "F\u2009x²"),
                Arguments.of(0x23, "F\u20091/x"),
                Arguments.of(0x24, "F\u2009xʸ"),
                Arguments.of(0x0F, "F\u2009Вх"),
                Arguments.of(0x25, "F\u2009\uD83D\uDDD8"),
                //
                Arguments.of(0x5C, "F\u2009x<0"),
                Arguments.of(0x5E, "F\u2009x=0"),
                Arguments.of(0x59, "F\u2009x≥0"),
                Arguments.of(0x57, "F\u2009x≠0"),
                //
                Arguments.of(0x5D, "F\u2009L0"),
                Arguments.of(0x5B, "F\u2009L1"),
                Arguments.of(0x58, "F\u2009L2"),
                Arguments.of(0x5A, "F\u2009L3"),
                // x→П
                Arguments.of(0x40, "x→П\u20090"),
                Arguments.of(0x41, "x→П\u20091"),
                Arguments.of(0x42, "x→П\u20092"),
                Arguments.of(0x43, "x→П\u20093"),
                Arguments.of(0x44, "x→П\u20094"),
                Arguments.of(0x45, "x→П\u20095"),
                Arguments.of(0x46, "x→П\u20096"),
                Arguments.of(0x47, "x→П\u20097"),
                Arguments.of(0x48, "x→П\u20098"),
                Arguments.of(0x49, "x→П\u20099"),
                Arguments.of(0x4A, "x→П\u2009a"),
                Arguments.of(0x4B, "x→П\u2009b"),
                Arguments.of(0x4C, "x→П\u2009c"),
                Arguments.of(0x4D, "x→П\u2009d"),
                Arguments.of(0x4E, "x→П\u2009e"),
                // П→x
                Arguments.of(0x60, "П→x\u20090"),
                Arguments.of(0x61, "П→x\u20091"),
                Arguments.of(0x62, "П→x\u20092"),
                Arguments.of(0x63, "П→x\u20093"),
                Arguments.of(0x64, "П→x\u20094"),
                Arguments.of(0x65, "П→x\u20095"),
                Arguments.of(0x66, "П→x\u20096"),
                Arguments.of(0x67, "П→x\u20097"),
                Arguments.of(0x68, "П→x\u20098"),
                Arguments.of(0x69, "П→x\u20099"),
                Arguments.of(0x6A, "П→x\u2009a"),
                Arguments.of(0x6B, "П→x\u2009b"),
                Arguments.of(0x6C, "П→x\u2009c"),
                Arguments.of(0x6D, "П→x\u2009d"),
                Arguments.of(0x6E, "П→x\u2009e"),
                //
                Arguments.of(0x54, "K\u2009НОП"),
                // K БП
                Arguments.of(0x80, "K\u2009БП\u20090"),
                Arguments.of(0x81, "K\u2009БП\u20091"),
                Arguments.of(0x82, "K\u2009БП\u20092"),
                Arguments.of(0x83, "K\u2009БП\u20093"),
                Arguments.of(0x84, "K\u2009БП\u20094"),
                Arguments.of(0x85, "K\u2009БП\u20095"),
                Arguments.of(0x86, "K\u2009БП\u20096"),
                Arguments.of(0x87, "K\u2009БП\u20097"),
                Arguments.of(0x88, "K\u2009БП\u20098"),
                Arguments.of(0x89, "K\u2009БП\u20099"),
                Arguments.of(0x8A, "K\u2009БП\u2009a"),
                Arguments.of(0x8B, "K\u2009БП\u2009b"),
                Arguments.of(0x8C, "K\u2009БП\u2009c"),
                Arguments.of(0x8D, "K\u2009БП\u2009d"),
                Arguments.of(0x8E, "K\u2009БП\u2009e"),
                // K ПП
                Arguments.of(0xA0, "K\u2009ПП\u20090"),
                Arguments.of(0xA1, "K\u2009ПП\u20091"),
                Arguments.of(0xA2, "K\u2009ПП\u20092"),
                Arguments.of(0xA3, "K\u2009ПП\u20093"),
                Arguments.of(0xA4, "K\u2009ПП\u20094"),
                Arguments.of(0xA5, "K\u2009ПП\u20095"),
                Arguments.of(0xA6, "K\u2009ПП\u20096"),
                Arguments.of(0xA7, "K\u2009ПП\u20097"),
                Arguments.of(0xA8, "K\u2009ПП\u20098"),
                Arguments.of(0xA9, "K\u2009ПП\u20099"),
                Arguments.of(0xAA, "K\u2009ПП\u2009a"),
                Arguments.of(0xAB, "K\u2009ПП\u2009b"),
                Arguments.of(0xAC, "K\u2009ПП\u2009c"),
                Arguments.of(0xAD, "K\u2009ПП\u2009d"),
                Arguments.of(0xAE, "K\u2009ПП\u2009e"),
                // K x=0
                Arguments.of(0xE0, "K\u2009x=0\u20090"),
                Arguments.of(0xE1, "K\u2009x=0\u20091"),
                Arguments.of(0xE2, "K\u2009x=0\u20092"),
                Arguments.of(0xE3, "K\u2009x=0\u20093"),
                Arguments.of(0xE4, "K\u2009x=0\u20094"),
                Arguments.of(0xE5, "K\u2009x=0\u20095"),
                Arguments.of(0xE6, "K\u2009x=0\u20096"),
                Arguments.of(0xE7, "K\u2009x=0\u20097"),
                Arguments.of(0xE8, "K\u2009x=0\u20098"),
                Arguments.of(0xE9, "K\u2009x=0\u20099"),
                Arguments.of(0xEA, "K\u2009x=0\u2009a"),
                Arguments.of(0xEB, "K\u2009x=0\u2009b"),
                Arguments.of(0xEC, "K\u2009x=0\u2009c"),
                Arguments.of(0xED, "K\u2009x=0\u2009d"),
                Arguments.of(0xEE, "K\u2009x=0\u2009e"),
                // K x<0
                Arguments.of(0xC0, "K\u2009x<0\u20090"),
                Arguments.of(0xC1, "K\u2009x<0\u20091"),
                Arguments.of(0xC2, "K\u2009x<0\u20092"),
                Arguments.of(0xC3, "K\u2009x<0\u20093"),
                Arguments.of(0xC4, "K\u2009x<0\u20094"),
                Arguments.of(0xC5, "K\u2009x<0\u20095"),
                Arguments.of(0xC6, "K\u2009x<0\u20096"),
                Arguments.of(0xC7, "K\u2009x<0\u20097"),
                Arguments.of(0xC8, "K\u2009x<0\u20098"),
                Arguments.of(0xC9, "K\u2009x<0\u20099"),
                Arguments.of(0xCA, "K\u2009x<0\u2009a"),
                Arguments.of(0xCB, "K\u2009x<0\u2009b"),
                Arguments.of(0xCC, "K\u2009x<0\u2009c"),
                Arguments.of(0xCD, "K\u2009x<0\u2009d"),
                Arguments.of(0xCE, "K\u2009x<0\u2009e"),
                // K x≥0
                Arguments.of(0x90, "K\u2009x≥0\u20090"),
                Arguments.of(0x91, "K\u2009x≥0\u20091"),
                Arguments.of(0x92, "K\u2009x≥0\u20092"),
                Arguments.of(0x93, "K\u2009x≥0\u20093"),
                Arguments.of(0x94, "K\u2009x≥0\u20094"),
                Arguments.of(0x95, "K\u2009x≥0\u20095"),
                Arguments.of(0x96, "K\u2009x≥0\u20096"),
                Arguments.of(0x97, "K\u2009x≥0\u20097"),
                Arguments.of(0x98, "K\u2009x≥0\u20098"),
                Arguments.of(0x99, "K\u2009x≥0\u20099"),
                Arguments.of(0x9A, "K\u2009x≥0\u2009a"),
                Arguments.of(0x9B, "K\u2009x≥0\u2009b"),
                Arguments.of(0x9C, "K\u2009x≥0\u2009c"),
                Arguments.of(0x9D, "K\u2009x≥0\u2009d"),
                Arguments.of(0x9E, "K\u2009x≥0\u2009e"),
                // K x≠0
                Arguments.of(0x70, "K\u2009x≠0\u20090"),
                Arguments.of(0x71, "K\u2009x≠0\u20091"),
                Arguments.of(0x72, "K\u2009x≠0\u20092"),
                Arguments.of(0x73, "K\u2009x≠0\u20093"),
                Arguments.of(0x74, "K\u2009x≠0\u20094"),
                Arguments.of(0x75, "K\u2009x≠0\u20095"),
                Arguments.of(0x76, "K\u2009x≠0\u20096"),
                Arguments.of(0x77, "K\u2009x≠0\u20097"),
                Arguments.of(0x78, "K\u2009x≠0\u20098"),
                Arguments.of(0x79, "K\u2009x≠0\u20099"),
                Arguments.of(0x7A, "K\u2009x≠0\u2009a"),
                Arguments.of(0x7B, "K\u2009x≠0\u2009b"),
                Arguments.of(0x7C, "K\u2009x≠0\u2009c"),
                Arguments.of(0x7D, "K\u2009x≠0\u2009d"),
                Arguments.of(0x7E, "K\u2009x≠0\u2009e"),
                // K x→П
                Arguments.of(0xB0, "K\u2009x→П\u20090"),
                Arguments.of(0xB1, "K\u2009x→П\u20091"),
                Arguments.of(0xB2, "K\u2009x→П\u20092"),
                Arguments.of(0xB3, "K\u2009x→П\u20093"),
                Arguments.of(0xB4, "K\u2009x→П\u20094"),
                Arguments.of(0xB5, "K\u2009x→П\u20095"),
                Arguments.of(0xB6, "K\u2009x→П\u20096"),
                Arguments.of(0xB7, "K\u2009x→П\u20097"),
                Arguments.of(0xB8, "K\u2009x→П\u20098"),
                Arguments.of(0xB9, "K\u2009x→П\u20099"),
                Arguments.of(0xBA, "K\u2009x→П\u2009a"),
                Arguments.of(0xBB, "K\u2009x→П\u2009b"),
                Arguments.of(0xBC, "K\u2009x→П\u2009c"),
                Arguments.of(0xBD, "K\u2009x→П\u2009d"),
                Arguments.of(0xBE, "K\u2009x→П\u2009e"),
                // K П→x
                Arguments.of(0xD0, "K\u2009П→x\u20090"),
                Arguments.of(0xD1, "K\u2009П→x\u20091"),
                Arguments.of(0xD2, "K\u2009П→x\u20092"),
                Arguments.of(0xD3, "K\u2009П→x\u20093"),
                Arguments.of(0xD4, "K\u2009П→x\u20094"),
                Arguments.of(0xD5, "K\u2009П→x\u20095"),
                Arguments.of(0xD6, "K\u2009П→x\u20096"),
                Arguments.of(0xD7, "K\u2009П→x\u20097"),
                Arguments.of(0xD8, "K\u2009П→x\u20098"),
                Arguments.of(0xD9, "K\u2009П→x\u20099"),
                Arguments.of(0xDA, "K\u2009П→x\u2009a"),
                Arguments.of(0xDB, "K\u2009П→x\u2009b"),
                Arguments.of(0xDC, "K\u2009П→x\u2009c"),
                Arguments.of(0xDD, "K\u2009П→x\u2009d"),
                Arguments.of(0xDE, "K\u2009П→x\u2009e"),
                //
                Arguments.of(0x34, "K\u2009[x]"),
                Arguments.of(0x35, "K\u2009{x}"),
                Arguments.of(0x36, "K\u2009max"),
                Arguments.of(0x31, "K\u2009|x|"),
                Arguments.of(0x32, "K\u2009ЗН"),
                Arguments.of(0x33, "K\u2009.⃖,"),
                Arguments.of(0x26, "K\u2009.⃗,"),
                Arguments.of(0x2A, "K\u2009․‚⃗„"),
                Arguments.of(0x30, "K\u2009.‚⃖„"),
                Arguments.of(0x3B, "K\u2009СЧ"),
                Arguments.of(0x37, "K\u2009⋀"),
                Arguments.of(0x38, "K\u2009⋁"),
                Arguments.of(0x39, "K\u2009⨁"),
                Arguments.of(0x3A, "K\u2009ИНВ")
        );
    }

    @ParameterizedTest
    @MethodSource("testDecodeOpcodeArguments")
    public void testDecodeOpcode(int opCode, String expected) {
        assertThat(Decoder.decodeOpCode(opCode)).isEqualTo(expected);
    }

    private static List<Arguments> testDecodeMemoryArguments() {
        return List.of(
                argumentSet("K\u2009П→x\u20096 1 Fln", new int[]{0xD6, 0x01, 0x18}, List.of(
                        new MemoryCell(0xD6, "K\u2009П→x\u20096", false),
                        new MemoryCell(0x01, "1", false),
                        new MemoryCell(0x18, "F\u2009ln", false)
                )),
                argumentSet("БП 1 [x]", new int[]{0x51, 0x01, 0x34}, List.of(
                        new MemoryCell(0x51, "БП", false),
                        new MemoryCell(0x01, "01", false),
                        new MemoryCell(0x34, "K\u2009[x]", false)
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
                        new MemoryCell(0x5C, "F\u2009x<0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x=0", new int[]{0x5E, 0x01}, List.of(
                        new MemoryCell(0x5E, "F\u2009x=0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x≥0", new int[]{0x59, 0x01}, List.of(
                        new MemoryCell(0x59, "F\u2009x≥0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("x≠0", new int[]{0x57, 0x01}, List.of(
                        new MemoryCell(0x57, "F\u2009x≠0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L0", new int[]{0x5D, 0x01}, List.of(
                        new MemoryCell(0x5D, "F\u2009L0", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L1", new int[]{0x5B, 0x01}, List.of(
                        new MemoryCell(0x5B, "F\u2009L1", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L2", new int[]{0x58, 0x01}, List.of(
                        new MemoryCell(0x58, "F\u2009L2", false),
                        new MemoryCell(0x01, "01", false)
                )),
                argumentSet("L3", new int[]{0x5A, 0x01}, List.of(
                        new MemoryCell(0x5A, "F\u2009L3", false),
                        new MemoryCell(0x01, "01", false)
                )),
                // Jump at the end
                argumentSet("L3 at the end", new int[]{0x5A}, List.of(
                        new MemoryCell(0x5A, "F\u2009L3", false)
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
