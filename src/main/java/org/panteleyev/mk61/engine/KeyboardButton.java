/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.engine;

public enum KeyboardButton {
    D0(10), D1(11), D2(12), D3(13), D4(14),
    D5(15), D6(16), D7(17), D8(18), D9(19),
    PLUS(20), MINUS(21), MULTIPLICATION(22), DIVISION(23),
    SWAP(24),
    DOT(25),
    SIGN(26),
    EE(27),
    CLEAR_X(28),
    PUSH(29),
    RUN_STOP(30),
    GOTO(31),
    RETURN(32),
    GOSUB(33),
    STORE(34),
    STEP_RIGHT(35),
    LOAD(36),
    STEP_LEFT(37),
    K(38),
    F(39);

    private final int keyCode;

    KeyboardButton(int keyCode) {
        this.keyCode = keyCode;
    }

    public int keyCode() {
        return keyCode;
    }
}
