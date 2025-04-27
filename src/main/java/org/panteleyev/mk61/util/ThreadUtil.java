/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
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
