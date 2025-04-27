/*
 https://github.com/cax/pmk-android/blob/master/pmk/src/com/cax/pmk/emulator/MCU.java
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.core;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class MCU {
    private static final int ARRAY_SIZE = 42;
    private static final int IND_COMMA_SIZE = 14;

    private final int[] ucmd_rom;
    private final int[] cmd_rom;
    private final int[] synchro_rom;
    private final int ik130x;

    final int[] R = new int[ARRAY_SIZE];
    final int[] M = new int[ARRAY_SIZE];
    final int[] ST = new int[ARRAY_SIZE];

    final AtomicInteger keyb_x = new AtomicInteger(0);
    final AtomicInteger keyb_y = new AtomicInteger(0);

    int S, S1, L, T, P, microtick, mcmd, comma, in, out, AMK, ASP, AK, MOD;
    final boolean[] ind_comma = new boolean[IND_COMMA_SIZE];
    boolean redrawIndic;

    private static final int[] J = {
            0, 1, 2, 3, 4, 5,
            3, 4, 5, 3, 4, 5,
            3, 4, 5, 3, 4, 5,
            3, 4, 5, 3, 4, 5,
            6, 7, 8, 0, 1, 2,
            3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5
    };

    public MCU(int[] ucmd_rom, int[] cmd_rom, int[] synchro_rom, int ik130x) {
        this.ucmd_rom = ucmd_rom;
        this.cmd_rom = cmd_rom;
        this.synchro_rom = synchro_rom;
        this.ik130x = ik130x;

        Arrays.fill(R, 0);
        Arrays.fill(M, 0);
        Arrays.fill(ST, 0);

        S = 0;
        S1 = 0;
        L = 0;
        T = 0;
        P = 0;
        microtick = 0;
        mcmd = 0;
        comma = 0;
        in = 0;
        out = 0;
        AMK = 0;
        ASP = 0;
        AK = 0;
        MOD = 0;
        redrawIndic = false;

        Arrays.fill(ind_comma, false);
    }

    public void tick() {
        int tick_0123 = microtick & 3;
        int chetv_1248 = 1 << tick_0123;
        int signal_I = microtick >>> 2;
        int signal_D = microtick / 12;
        //int signal_E = (microtick >>> 2) % 3;
        boolean keyb_processed = false;

        if (microtick == 0) {
            AK = R[36] + 16 * R[39];
            if ((cmd_rom[AK] & 0xfc0000) == 0) {
                T = 0;
            }
        }

        if (chetv_1248 == 1) {
            int k = microtick / 36;
            if (k < 3) ASP = 0xff & cmd_rom[AK];
            else if (k == 3) ASP = 0xff & cmd_rom[AK] >>> 8;
            else if (k == 4) {
                ASP = 0xff & cmd_rom[AK] >>> 16;
                if (ASP > 0x1f) {
                    if (microtick == 144) {
                        R[37] = ASP & 0xf;
                        R[40] = ASP >>> 4;
                    }
                    ASP = 0x5f;
                }
            }
            MOD = 0xff & cmd_rom[AK] >>> 24;
            AMK = synchro_rom[ASP * 9 + J[microtick >>> 2]];
            AMK = AMK & 0x3f;
            if (AMK > 59) {
                AMK = (AMK - 60) * 2;
                if (L == 0) {
                    AMK++;
                }
                AMK += 60;
            }
            mcmd = ucmd_rom[AMK];
        }

        int alpha = 0, beta = 0, gamma = 0;

        switch (mcmd >>> 24 & 3) {
            case 2:
            case 3:
                if ((microtick / 12) != keyb_x.get() - 1) {
                    if (keyb_y.get() > 0) {
                        if (chetv_1248 == 1) S1 |= keyb_y.get();
                        keyb_processed = true;
                    }
                }
                break;
        }

        if ((mcmd & 1) > 0) {
            alpha |= R[signal_I];
        }
        if ((mcmd & 2) > 0) {
            alpha |= M[signal_I];
        }
        if ((mcmd & 4) > 0) {
            alpha |= ST[signal_I];
        }
        if ((mcmd & 8) > 0) {
            alpha |= ~R[signal_I] & 0xF;
        }
        if ((mcmd & 16) > 0) {
            if (L == 0) {
                alpha |= 0xA;
            }
        }
        if ((mcmd & 32) > 0) {
            alpha |= S;
        }
        if ((mcmd & 64) > 0) {
            alpha |= 4;
        }
        if ((mcmd >>> 7 & 16) > 0) {
            beta |= 1;
        }
        if ((mcmd >>> 7 & 8) > 0) {
            beta |= 6;
        }
        if ((mcmd >>> 7 & 4) > 0) {
            beta |= S1;
        }
        if ((mcmd >>> 7 & 2) > 0) {
            beta |= ~S & 0xF;
        }
        if ((mcmd >>> 7 & 1) > 0) {
            beta |= S;
        }
        if ((cmd_rom[AK] & 0xfc0000) > 0) {
            if (keyb_y.get() == 0) {
                T = 0;
            }
        } else {
            redrawIndic = true;
            if ((microtick / 12) == keyb_x.get() - 1)
                if (keyb_y.get() > 0) {
                    S1 = keyb_y.get();
                    T = 1;
                    keyb_processed = true;
                }
            if (tick_0123 == 0) {
                if (signal_D >= 0 && signal_D < 12) {
                    if (L > 0) {
                        comma = signal_D;
                    }
                }
            }
            ind_comma[signal_D] = L > 0;
        }
        if ((mcmd >>> 12 & 4) > 0) {
            gamma = ~T & 1;
        }
        if ((mcmd >>> 12 & 2) > 0) {
            gamma |= ~L & 1;
        }
        if ((mcmd >>> 12 & 1) > 0) {
            gamma |= L & 1;
        }

        int sum = alpha + beta + gamma;
        int sigma = sum & 0xf;
        P = sum >>> 4;

        if (MOD == 0 || (microtick >>> 2) >= 36) {
            switch (mcmd >>> 15 & 7) {
                case 1:
                    R[signal_I] = R[(signal_I + 3) % ARRAY_SIZE];
                    break;
                case 2:
                    R[signal_I] = sigma;
                    break;
                case 3:
                    R[signal_I] = S;
                    break;
                case 4:
                    R[signal_I] = R[signal_I] | S | sigma;
                    break;
                case 5:
                    R[signal_I] = S | sigma;
                    break;
                case 6:
                    R[signal_I] = R[signal_I] | S;
                    break;
                case 7:
                    R[signal_I] = R[signal_I] | sigma;
                    break;
            }
            if ((mcmd >>> 18 & 1) > 0) {
                R[(signal_I + 41) % ARRAY_SIZE] = sigma;
            }
            if ((mcmd >>> 19 & 1) > 0) {
                R[(signal_I + 40) % ARRAY_SIZE] = sigma;
            }
        }
        if ((mcmd >>> 21 & 1) > 0) {
            L = 1 & P;
        }
        if ((mcmd >>> 20 & 1) > 0) {
            M[signal_I] = S;
        }

        switch (mcmd >>> 22 & 3) {
            case 1:
                S = S1;
                break;
            case 2:
                S = sigma;
                break;
            case 3:
                S = S1 | sigma;
                break;
        }

        switch (mcmd >>> 24 & 3) {
            case 1:
                S1 = sigma;
                break;
            //case 2: S1 = S1; break;
            case 3:
                S1 = S1 | sigma;
                break;
        }

        int x, y, z;
        switch (mcmd >>> 26 & 3) {
            case 1:
                ST[(signal_I + 2) % ARRAY_SIZE] = ST[(signal_I + 1) % ARRAY_SIZE];
                ST[(signal_I + 1) % ARRAY_SIZE] = ST[signal_I];
                ST[signal_I] = sigma;
                break;
            case 2:
                x = ST[signal_I];
                ST[signal_I] = ST[(signal_I + 1) % ARRAY_SIZE];
                ST[(signal_I + 1) % ARRAY_SIZE] = ST[(signal_I + 2) % ARRAY_SIZE];
                ST[(signal_I + 2) % ARRAY_SIZE] = x;
                break;
            case 3:
                x = ST[signal_I];
                y = ST[(signal_I + 1) % ARRAY_SIZE];
                z = ST[(signal_I + 2) % ARRAY_SIZE];
                ST[(signal_I) % ARRAY_SIZE] = sigma | y;
                ST[(signal_I + 1) % ARRAY_SIZE] = x | z;
                ST[(signal_I + 2) % ARRAY_SIZE] = y | x;
                break;
        }

        out = 0xf & M[signal_I];
        M[signal_I] = in;
        microtick += 4;
        if (microtick > 167) {
            microtick = 0;
        }

        if (keyb_processed && ik130x != 3) {
            keyb_x.set(0);
            keyb_y.set(0);
        }
    }
}
