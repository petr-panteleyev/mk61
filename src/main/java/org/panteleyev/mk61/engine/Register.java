/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.engine;

import java.util.Arrays;

import static org.panteleyev.mk61.engine.Constants.DISPLAY_SIZE;

public final class Register {
    // Мантисса
    public static final long MANTISSA_MASK = 0xFFFF_FFFFL;

    private static final long MANTISSA_SIGN_MASK = 0xF_0000_0000L;

    // Экспонента
    private static final long EXPONENT_LO_MASK = 0x0F0_0000_0000L;
    private static final int EXPONENT_LO_SHIFT = 36;
    private static final long EXPONENT_HI_MASK = 0xF00_0000_0000L;
    private static final int EXPONENT_HI_SHIFT = 40;
    private static final long EXPONENT_SIGN_MASK = 0xF000_0000_0000L;
    private static final int EXPONENT_SIGN_SHIFT = 44;

    private static final int TETRAD_MASK = 0xF;

    public static boolean isNegative(long register) {
        return (register & MANTISSA_SIGN_MASK) != 0;
    }

    public static int getExponent(long register) {
        var exp = (int) (((register & EXPONENT_SIGN_MASK) >> EXPONENT_SIGN_SHIFT) * 100
                + ((register & EXPONENT_HI_MASK) >> EXPONENT_HI_SHIFT) * 10
                + ((register & EXPONENT_LO_MASK) >> EXPONENT_LO_SHIFT));
        if (exp > 900) {
            exp = exp - 1000;
        }
        return exp;
    }

    public static String toString(long register) {
        var charBuffer = new char[DISPLAY_SIZE - 1];
        Arrays.fill(charBuffer, ' ');

        var negative = isNegative(register);
        if (negative) {
            charBuffer[0] = '-';
        }

        int exponent = getExponent(register);

        var mantissaBits = register & MANTISSA_MASK;
        for (int i = 7; i >= 0; i--) {
            var digit = mantissaBits & TETRAD_MASK;
            charBuffer[i + 1] = Long.toString(digit & 0xF, 16).toUpperCase().charAt(0);
            mantissaBits >>= 4;
        }
        if (charBuffer[1] == ' ') {
            charBuffer[1] = '0';
        }

        var dotPosition = 2;
        if (exponent >= 0 && exponent <= 7) {
            dotPosition += exponent;
            exponent = 0;
        }

        // Убираем концевые нули
        for (int i = 8; i >= 0; i--) {
            if (i == dotPosition - 1) {
                break;
            }
            if (charBuffer[i] == '0') {
                charBuffer[i] = ' ';
            } else {
                break;
            }
        }

        if (exponent != 0) {
            var expStr = String.format("%03d", Math.abs(exponent));
            if (exponent < 0) {
                charBuffer[10] = '-';
            } else {
                if (exponent > 99) {
                    charBuffer[10] = expStr.charAt(0);
                }
            }
            charBuffer[11] = expStr.charAt(1);
            charBuffer[12] = expStr.charAt(2);
        }

        var result = new StringBuilder().append(charBuffer);
        result.insert(dotPosition, '.');
        return result.toString().stripTrailing();
    }


    /**
     * Устанавливает тетраду с заданным индексом.
     *
     * @param x     регистр
     * @param index номер тетрады [0..11]
     * @param value значение тетрады
     * @return модифицированное значение регистра
     */
    public static long setTetrad(long x, int index, int value) {
        int shift = index * 4;
        long clearMask = ~((long) TETRAD_MASK << shift);
        return x & clearMask | ((long) (value & 0xF) << shift);
    }
}
