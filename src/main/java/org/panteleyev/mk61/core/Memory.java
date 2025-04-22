/*
 https://github.com/cax/pmk-android/blob/master/pmk/src/com/cax/pmk/emulator/Memory.java
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.core;

import java.util.Arrays;

class Memory {
    private static final int MEM_SIZE = 252;

    public int in = 0;
    public int out = 0;
    public int microtick = 0;

    private final int[] M = new int[MEM_SIZE];

    public Memory() {
        Arrays.fill(M, 0);
    }

    public final void tick() {
        if (microtick == MEM_SIZE) microtick = 0;
        out = M[microtick];
        M[(microtick + MEM_SIZE) % MEM_SIZE] = in;
        microtick++;
    }
}
