/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.util;

import java.time.Duration;

public abstract class ThreadUtil {
    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            // ничего не делаем
        }
    }

    private ThreadUtil() {
    }
}
