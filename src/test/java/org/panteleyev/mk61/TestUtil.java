// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61;

import java.util.Random;
import java.util.UUID;

public final class TestUtil {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static int randomInt() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    public static int randomByte() {
        return RANDOM.nextInt(255);
    }

    public static long randomLong() {
        return RANDOM.nextLong(Long.MAX_VALUE);
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
