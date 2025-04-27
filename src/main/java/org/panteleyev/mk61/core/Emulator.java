/*
 https://github.com/cax/pmk-android/blob/master/pmk/src/com/cax/pmk/emulator/Emulator.java
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.core;

import org.panteleyev.mk61.engine.Indicator;
import org.panteleyev.mk61.engine.Mk61DeviceModel;
import org.panteleyev.mk61.engine.Register;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.panteleyev.mk61.core.MCommands.IK1302_MROM;
import static org.panteleyev.mk61.core.MCommands.IK1303_MROM;
import static org.panteleyev.mk61.core.MCommands.IK1306_MROM;
import static org.panteleyev.mk61.engine.Constants.DISPLAY_DELAY;
import static org.panteleyev.mk61.engine.Mk61DeviceModel.PROGRAM_MEMORY_SIZE;
import static org.panteleyev.mk61.core.Synchro.IK1302_SROM;
import static org.panteleyev.mk61.core.Synchro.IK1303_SROM;
import static org.panteleyev.mk61.core.Synchro.IK1306_SROM;
import static org.panteleyev.mk61.core.UCommands.IK1302_UROM;
import static org.panteleyev.mk61.core.UCommands.IK1303_UROM;
import static org.panteleyev.mk61.core.UCommands.IK1306_UROM;

public final class Emulator extends Thread {
    private static final int REG_2_OFFSET = 0;
    private static final int REG_3_OFFSET = REG_2_OFFSET + 42;
    private static final int REG_A_OFFSET = 0;
    private static final int REG_B_OFFSET = 0;
    private static final int REG_C_OFFSET = 0;
    private static final int REG_D_OFFSET = REG_3_OFFSET + 42;
    private static final int REG_E_OFFSET = REG_D_OFFSET + 42;
    private static final int REG_0_OFFSET = REG_E_OFFSET + 42;
    private static final int REG_1_OFFSET = REG_0_OFFSET + 42;
    private static final int REG_8_OFFSET = 0;
    private static final int REG_9_OFFSET = REG_8_OFFSET + 42;
    private static final int REG_4_OFFSET = REG_9_OFFSET + 42;
    private static final int REG_5_OFFSET = REG_4_OFFSET + 42;
    private static final int REG_6_OFFSET = REG_5_OFFSET + 42;
    private static final int REG_7_OFFSET = REG_6_OFFSET + 42;

    private static final int[][] MEMORY_ADDRESS_SWAPS = {
            {1, 2, 3, 4, 5, 14, 13, 12, 6, 7, 8, 9, 10, 11, 0},
            {10, 11, 6, 7, 2, 3, 4, 5, 0, 1, 14, 13, 12, 8, 9},
            {14, 13, 12, 10, 11, 6, 7, 8, 9, 4, 5, 0, 1, 2, 3}
    };
    private static final int[][] MEMORY_ADDRESS_PAGES = {
            {1, 41}, {1, 83}, {1, 125}, {1, 167}, {1, 209}, {1, 251}, {2, 41}, {2, 83}, {2, 125},
            {2, 167}, {2, 209}, {2, 251}, {3, 41}, {4, 41}, {5, 41}
    };

    private final MCU IK1302 = new MCU(IK1302_UROM, IK1302_MROM, IK1302_SROM, 2);
    private final MCU IK1303 = new MCU(IK1303_UROM, IK1303_MROM, IK1303_SROM, 3);
    private final MCU IK1306 = new MCU(IK1306_UROM, IK1306_MROM, IK1306_SROM, 6);

    private final Memory IR2_1 = new Memory();
    private final Memory IR2_2 = new Memory();

    private final AtomicInteger speedMode = new AtomicInteger(1);  // 0=fast, 1=real speed

    private int syncCounter = 0;
    private final int[] indicator = new int[12];
    private final int[] indicatorOld = new int[12];
    private final boolean[] indComma = new boolean[12];
    private final boolean[] indCommaOld = new boolean[12];

    private final Mk61DeviceModel deviceModel;

    public Emulator(Mk61DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
        setDaemon(true);
        setName("Emulator");
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                step();
            }
        } catch (InterruptedException _) {
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

    void show_indicator() throws InterruptedException {
        long ir = 0;
        int dots = 0;

        int shift = 0;

        for (int ix = 0; ix < 12; ix++) {
            ir |= (long) (indicator[ix] & 0xF) << shift;
            if (indComma[ix]) {
                dots |= 1 << ix;
            }
            shift += 4;
        }

        deviceModel.setIndicator(ir, dots);
        // Задержка нужна, чтобы мигание экрана было более или менее заметно.
        if (ir != Indicator.EMPTY.indicator()) {
            Thread.sleep(DISPLAY_DELAY);
        }
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
            if (IK1302.redrawIndic && syncCounter == 4) {
                if (deviceModel.getMemoryUploadFlag()) {
                    loadMemory(deviceModel.getMemoryUpload());
                }

                dumpMemory();
                return true;
            }
        }
        return false;
    }

    void step() throws InterruptedException {
        int i, idx;
        IK1303.keyb_y.set(1);
        IK1303.keyb_x.set(deviceModel.getAngleMode());
        for (int ix = 0; ix < 560; ix++) {
            if (isInterrupted()) {
                break;
            }
            if (speedMode.get() > 0) {
                Thread.sleep(Duration.ofMillis(1));
            }
            tick42();

            if (IK1302.redrawIndic) {
                for (i = 0; i <= 8; i++) indicator[i] = IK1302.R[(8 - i) * 3];
                for (i = 0; i <= 2; i++) indicator[i + 9] = IK1302.R[(11 - i) * 3];
                for (i = 0; i <= 8; i++) indComma[i] = IK1302.ind_comma[9 - i];
                for (i = 0; i <= 2; i++) indComma[i + 9] = IK1302.ind_comma[12 - i];
                IK1302.redrawIndic = false;
            } else {
                Arrays.fill(indicator, 0xF);
                Arrays.fill(indComma, false);
            }

            var renew = false;
            for (idx = 0; idx < 12; idx++) {
                if (indicatorOld[idx] != indicator[idx]) {
                    renew = true;
                    indicatorOld[idx] = indicator[idx];
                }
                if (indCommaOld[idx] != indComma[idx]) {
                    renew = true;
                    indCommaOld[idx] = indComma[idx];
                }
            }
            if (renew) {
                deviceModel.setPc(getProgramCounter());
                updateCallStack();
                show_indicator();
            }
            updateModel();
        }
    }

    // Сбор состояния в пригодном для отображения вид

    private void updateCallStack() {
        int callStackIndex = 4;
        for (int i = 1; i <= 25; i += 6) {
            var r = ((IK1302.R[i + 3] & 0xF) << 4) | (IK1302.R[i] & 0xF);
            deviceModel.setCallStack(callStackIndex--, r);
        }
    }

    private void updateModel() {
        if (syncCounter == 4 && IR2_1.microtick == 84) {
            deviceModel.setX(getRegister(IK1303.M, 1));
            deviceModel.setY(getRegister(IK1302.M, 1));
            deviceModel.setZ(getRegister(IR2_2.M, 85));
            deviceModel.setT(getRegister(IR2_2.M, 127));
            deviceModel.setX1(getRegister(IK1306.M, 1));

            deviceModel.setRegister(0, getRegister(IR2_2.M, REG_0_OFFSET));
            deviceModel.setRegister(1, getRegister(IR2_2.M, REG_1_OFFSET));
            deviceModel.setRegister(2, getRegister(IR2_2.M, REG_2_OFFSET));
            deviceModel.setRegister(3, getRegister(IR2_2.M, REG_3_OFFSET));
            deviceModel.setRegister(4, getRegister(IR2_1.M, REG_4_OFFSET));
            deviceModel.setRegister(5, getRegister(IR2_1.M, REG_5_OFFSET));
            deviceModel.setRegister(6, getRegister(IR2_1.M, REG_6_OFFSET));
            deviceModel.setRegister(7, getRegister(IR2_1.M, REG_7_OFFSET));
            deviceModel.setRegister(8, getRegister(IR2_1.M, REG_8_OFFSET));
            deviceModel.setRegister(9, getRegister(IR2_1.M, REG_9_OFFSET));
            deviceModel.setRegister(10, getRegister(IK1306.M, REG_A_OFFSET));
            deviceModel.setRegister(11, getRegister(IK1303.M, REG_B_OFFSET));
            deviceModel.setRegister(12, getRegister(IK1302.M, REG_C_OFFSET));
            deviceModel.setRegister(13, getRegister(IR2_2.M, REG_D_OFFSET));
            deviceModel.setRegister(14, getRegister(IR2_2.M, REG_E_OFFSET));
        }
    }


    private int[] cmdAddress(int address, int page) {
        int addr1 = address / 7;
        int addr2 = address % 7;
        if (addr2 == 0) {
            return MEMORY_ADDRESS_PAGES[MEMORY_ADDRESS_SWAPS[page][addr1]];
        } else {
            return new int[]{
                    MEMORY_ADDRESS_PAGES[MEMORY_ADDRESS_SWAPS[page][addr1]][0],
                    MEMORY_ADDRESS_PAGES[MEMORY_ADDRESS_SWAPS[page][addr1]][1] - 42 + addr2 * 6,
            };
        }
    }

    public void saveCmd(int address, int cmdCode) {
        int hi = cmdCode / 16;
        int lo = cmdCode % 16;
        int[] addr = cmdAddress(address, IR2_1.microtick / 84);
        switch (addr[0]) {
            case 1:
                IR2_1.M[addr[1]] = hi;
                IR2_1.M[addr[1] - 3] = lo;
                break;
            case 2:
                IR2_2.M[addr[1]] = hi;
                IR2_2.M[addr[1] - 3] = lo;
                break;
            case 3:
                IK1302.M[addr[1]] = hi;
                IK1302.M[addr[1] - 3] = lo;
                break;
            case 4:
                IK1303.M[addr[1]] = hi;
                IK1303.M[addr[1] - 3] = lo;
                break;
            case 5:
                IK1306.M[addr[1]] = hi;
                IK1306.M[addr[1] - 3] = lo;
                break;
        }
    }

    private void dumpMemory() {
        int[] memory = new int[PROGRAM_MEMORY_SIZE];

        for (int address = 0; address < PROGRAM_MEMORY_SIZE; address++) {
            int[] addr = cmdAddress(address, IR2_1.microtick / 84);
            int cmdCode = switch (addr[0]) {
                case 1 -> IR2_1.M[addr[1]] * 16 + IR2_1.M[addr[1] - 3];
                case 2 -> IR2_2.M[addr[1]] * 16 + IR2_2.M[addr[1] - 3];
                case 3 -> IK1302.M[addr[1]] * 16 + IK1302.M[addr[1] - 3];
                case 4 -> IK1303.M[addr[1]] * 16 + IK1303.M[addr[1] - 3];
                case 5 -> IK1306.M[addr[1]] * 16 + IK1306.M[addr[1] - 3];
                default -> 0;
            };
            memory[address] = cmdCode;
        }

        deviceModel.setMemory(memory);
    }

    private void loadMemory(int[] memory) {
        for (int i = 0; i < memory.length; i++) {
            saveCmd(i, memory[i]);
        }
        deviceModel.setMemoryUploadFlag(false);
    }

    private int getProgramCounter() {
        return (IK1302.R[31] & 0xF) | ((IK1302.R[34] & 0xF) << 4);
    }

    private long getRegister(int[] mcuRegister, int startBit) {
        long register = 0;
        int ind = startBit;
        for (int i = 0; i < 12; i++) {
            register = Register.setTetrad(register, i, mcuRegister[ind]);
            ind += 3;
        }
        return register;
    }
}
