// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.panteleyev.mk61.util.StringUtil.pcToString;

public class Decoder {
    private static final List<Integer> DOUBLE_STEPS = List.of(
            0x51, 0x53, 0x5C, 0x5E, 0x59, 0x57, 0x5D, 0x5B, 0x58, 0x5A
    );

    private static final char[] REGISTER_LETTERS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e'
    };

    private static final char SPACE = ' ';
    private static final String F = "F" + SPACE;
    private static final String K = "K" + SPACE;

    private static final Map<Integer, String> CODE_MAP = Map.ofEntries(
            entry(0x10, "+"),
            entry(0x11, "-"),
            entry(0x12, "×"),
            entry(0x13, "÷"),
            entry(0x14, "↔"),
            entry(0x0E, "В↑"),
            entry(0x0A, "∙"),
            entry(0x0B, "/-/"),
            entry(0x0C, "ВП"),
            entry(0x0D, "Cx"),
            entry(0x50, "С/П"),
            entry(0x51, "БП"),
            entry(0x52, "В/О"),
            entry(0x53, "ПП"),
            //
            entry(0x15, F + "10ˣ"),
            entry(0x17, F + "lg"),
            entry(0x18, F + "ln"),
            entry(0x16, F + "eˣ"),
            entry(0x19, F + "sin⁻¹"),
            entry(0x1A, F + "cos⁻¹"),
            entry(0x1B, F + "tg⁻¹"),
            entry(0x1C, F + "sin"),
            entry(0x1D, F + "cos"),
            entry(0x1E, F + "tg"),
            entry(0x20, F + "π"),
            entry(0x21, F + "√‾"),
            entry(0x22, F + "x²"),
            entry(0x23, F + "1/x"),
            entry(0x24, F + "xʸ"),
            entry(0x0F, F + "Вх"),
            entry(0x25, F + "\uD83D\uDDD8"),
            //
            entry(0x5C, F + "x<0"),
            entry(0x5E, F + "x=0"),
            entry(0x59, F + "x≥0"),
            entry(0x57, F + "x≠0"),
            //
            entry(0x5D, F + "L0"),
            entry(0x5B, F + "L1"),
            entry(0x58, F + "L2"),
            entry(0x5A, F + "L3"),
            //
            entry(0x54, K + "НОП"),
            entry(0x34, K + "[x]"),
            entry(0x35, K + "{x}"),
            entry(0x31, K + "|x|"),
            entry(0x36, K + "max"),
            entry(0x32, K + "ЗН"),
            entry(0x33, K + ".⃖,"),
            entry(0x26, K + ".⃗,"),
            entry(0x2A, K + "․‚⃗„"),
            entry(0x30, K + ".‚⃖„"),
            entry(0x3B, K + "СЧ"),
            entry(0x37, K + "⋀"),
            entry(0x38, K + "⋁"),
            entry(0x39, K + "⨁"),
            entry(0x3A, K + "ИНВ"),
            // Undocumented
            entry(0x27, K + "-"),
            entry(0x28, K + "×"),
            entry(0x29, K + "÷"),
            entry(0x55, K + "1"),
            entry(0x56, K + "2")
    );

    public static String decodeOpCode(int opCode) {
        // Цифры
        if (opCode >= 0 && opCode <= 9) {
            return Integer.toString(opCode);
        }
        // x→П
        if (opCode >= 0x40 && opCode <= 0x4E) {
            return "x→П" + SPACE + registerLetter(opCode);
        }
        // П→x
        if (opCode >= 0x60 && opCode <= 0x6E) {
            return "П→x" + SPACE + registerLetter(opCode);
        }
        // K БП
        if (opCode >= 0x80 && opCode <= 0x8E) {
            return K + "БП" + SPACE + registerLetter(opCode);
        }
        // K ПП
        if (opCode >= 0xA0 && opCode <= 0xAE) {
            return K + "ПП" + SPACE + registerLetter(opCode);
        }
        // K x=0
        if (opCode >= 0xE0 && opCode <= 0xEE) {
            return K + "x=0" + SPACE + registerLetter(opCode);
        }
        // K x<0
        if (opCode >= 0xC0 && opCode <= 0xCE) {
            return K + "x<0" + SPACE + registerLetter(opCode);
        }
        // K x≥0
        if (opCode >= 0x90 && opCode <= 0x9E) {
            return K + "x≥0" + SPACE + registerLetter(opCode);
        }
        // K x≠0
        if (opCode >= 0x70 && opCode <= 0x7E) {
            return K + "x≠0" + SPACE + registerLetter(opCode);
        }
        // K x→П
        if (opCode >= 0xB0 && opCode <= 0xBE) {
            return K + "x→П" + SPACE + registerLetter(opCode);
        }
        // K П→x
        if (opCode >= 0xD0 && opCode <= 0xDE) {
            return K + "П→x" + SPACE + registerLetter(opCode);
        }

        return CODE_MAP.get(opCode);
    }

    public static List<MemoryCell> decodeMemory(int[] memory) {
        var result = new ArrayList<MemoryCell>(memory.length);

        for (int i = 0; i < memory.length; i++) {
            var opCode = memory[i];
            var mnemonics = decodeOpCode(opCode);
            var invalid = false;
            if (mnemonics == null) {
                mnemonics = String.format("%02X", opCode);
                invalid = true;
            }

            var step = new MemoryCell(opCode, mnemonics, invalid);
            result.add(step);

            if (DOUBLE_STEPS.contains(opCode) && i < memory.length - 1) {
                opCode = memory[++i];
                result.add(new MemoryCell(opCode, pcToString(opCode), false));
            }
        }

        return result;
    }

    private static char registerLetter(int opCode) {
        return REGISTER_LETTERS[opCode & 0xF];
    }
}
