/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.engine;

import java.time.Duration;

public abstract class Constants {
    public static final Duration DISPLAY_DELAY = Duration.ofMillis(23);

    public static final int REGISTERS_SIZE = 15;
    public static final int CALL_STACK_SIZE = 5;

    public static final int DISPLAY_SIZE = 14;

    private Constants() {
    }
}
