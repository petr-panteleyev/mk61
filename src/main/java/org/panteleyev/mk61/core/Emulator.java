/*
 https://github.com/cax/pmk-android/blob/master/pmk/src/com/cax/pmk/emulator/Emulator.java
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.core;

import org.panteleyev.mk61.engine.IR;
import org.panteleyev.mk61.engine.IndicatorCallback;
import org.panteleyev.mk61.util.ThreadUtil;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.panteleyev.mk61.core.MCommands.ik1302_mrom;
import static org.panteleyev.mk61.core.MCommands.ik1303_mrom;
import static org.panteleyev.mk61.core.MCommands.ik1306_mrom;
import static org.panteleyev.mk61.core.Synchro.ik1302_srom;
import static org.panteleyev.mk61.core.Synchro.ik1303_srom;
import static org.panteleyev.mk61.core.Synchro.ik1306_srom;
import static org.panteleyev.mk61.core.UCommands.ik1302_urom;
import static org.panteleyev.mk61.core.UCommands.ik1303_urom;
import static org.panteleyev.mk61.core.UCommands.ik1306_urom;

public class Emulator implements Runnable {
    enum RunningState {
        RUNNING(0), STOPPED(1), STOPPING_NORMAL(2), STOPPING_FORCED(3);

        RunningState(int state) {
            this.state = state;
        }

        private final int state;
    }

    private final MCU IK1302 = new MCU(ik1302_urom, ik1302_mrom, ik1302_srom, 2);
    private final MCU IK1303 = new MCU(ik1303_urom, ik1303_mrom, ik1303_srom, 3);
    private final MCU IK1306 = new MCU(ik1306_urom, ik1306_mrom, ik1306_srom, 6);

    private final Memory IR2_1 = new Memory();
    private final Memory IR2_2 = new Memory();

    private final AtomicInteger angleMode;
    private final AtomicInteger speedMode = new AtomicInteger(1);  // 0=fast, 1=real speed

    private int syncCounter = 0;
    private final int[] indicator = new int[12];
    private final int[] indicator_old = new int[12];
    private final boolean[] ind_comma = new boolean[12];
    private final boolean[] ind_comma_old = new boolean[12];

    private final AtomicInteger runningState = new AtomicInteger(RunningState.STOPPED.state);

    private final IndicatorCallback indicatorCallback;

    public Emulator(AtomicInteger angleMode, IndicatorCallback indicatorCallback) {
        this.angleMode = angleMode;
        this.indicatorCallback = indicatorCallback;
    }

    public void run() {
        runningState.set(RunningState.RUNNING.state);
        while (runningState.get() == RunningState.RUNNING.state) {
            step();
        }

        if (runningState.get() != RunningState.STOPPING_FORCED.state) {
            while (!(IR2_1.microtick == 84 && syncCounter == 0))
                tick42();
        }

        runningState.set(RunningState.STOPPED.state);
    }

    public void stopEmulator(boolean force) {
        if (force) {
            runningState.set(RunningState.STOPPING_FORCED.state);
        } else {
            runningState.set(RunningState.STOPPING_NORMAL.state);
        }

        while (runningState.get() == RunningState.STOPPING_NORMAL.state
                || runningState.get() == RunningState.STOPPING_FORCED.state) {
            ThreadUtil.sleep(Duration.ofMillis(10));
        }
    }

    public void setSpeedMode(int mode) {
        speedMode.set(mode);
    }

    public int getSpeedMode() {
        return speedMode.get();
    }

    public void keypad(int keycode) {
        IK1302.keyb_x.set((keycode % 10) + 2);
        keycode /= 10;
        IK1302.keyb_y.set(keycode == 2 ? 8 : (keycode == 3 ? 9 : 1));

    	/*
    	11,9	7,9		9,9		4,9		2,9		<-   39 37 35 32 30
    	10,9	8,9		6,9		3,9		5,9     <-   38 36 34 31 33
    	9,1		10,1	11,1	3,8		5,8     <-   17 18 19 21 23
    	6,1		7,1		8,1		2,8		4,8     <-   14 15 16 20 22
    	3,1		4,1		5,1		6,8		11,8    <-   11 12 13 24 29
    	2,1		7,8		8,8		9,8		10,8    <-   10 25 26 27 28
    	*/
    }

    void show_indicator() {
        long ir = 0;
        int dots = 0;

        int shift = 0;

        for (int ix = 0; ix < 12; ix++) {
            ir |= (long) (indicator[ix] & 0xF) << shift;
            if (ind_comma[ix]) {
                dots |= 1 << ix;
            }
            shift += 4;
        }

        indicatorCallback.display(new IR(ir, dots));
    }

    void tick() {
        IK1302.in = IR2_2.out;
        IK1302.tick();
        IK1303.in = IK1302.out;
        IK1303.tick();

        IK1306.in = IK1303.out;
        IK1306.tick();
        IR2_1.in = IK1306.out;
        IR2_1.tick();

        IR2_2.in = IR2_1.out;
        IR2_2.tick();
        IK1302.M[((IK1302.microtick >>> 2) + 41) % 42] = IR2_2.out;
    }

    boolean tick42() {
        for (int j = 0; j < 42; j++) {
            tick();
        }

        if (IR2_1.microtick == 84) {
            syncCounter = (syncCounter + 1) % 5;
            if (IK1302.redraw_indic && syncCounter == 4) {
                return true;
            }
        }
        return false;
    }

    void step() {
        int i, idx;
        boolean renew = false;
        IK1303.keyb_y.set(1);
        IK1303.keyb_x.set(angleMode.get());
        for (int ix = 0; ix < 560; ix++) {
            if (runningState.get() == RunningState.STOPPING_FORCED.state) break;
            if (speedMode.get() > 0) {
                ThreadUtil.sleep(Duration.ofMillis(1));
            }
            tick42();

            if (IK1302.redraw_indic) {
                for (i = 0; i <= 8; i++) indicator[i] = IK1302.R[(8 - i) * 3];
                for (i = 0; i <= 2; i++) indicator[i + 9] = IK1302.R[(11 - i) * 3];
                for (i = 0; i <= 8; i++) ind_comma[i] = IK1302.ind_comma[9 - i];
                for (i = 0; i <= 2; i++) ind_comma[i + 9] = IK1302.ind_comma[12 - i];
                IK1302.redraw_indic = false;
            } else {
                for (i = 0; i < 12; i++) {
                    indicator[i] = 15;
                    ind_comma[i] = false;
                    IK1302.redraw_indic = false;
                }
            }

            renew = false;
            for (idx = 0; idx < 12; idx++) {
                if (indicator_old[idx] != indicator[idx]) renew = true;
                indicator_old[idx] = indicator[idx];
                if (ind_comma_old[idx] != ind_comma[idx]) renew = true;
                ind_comma_old[idx] = ind_comma[idx];
            }
            if (renew) show_indicator();
        }
    }
}
