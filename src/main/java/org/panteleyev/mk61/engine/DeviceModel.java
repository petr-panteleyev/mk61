// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.mk61.engine;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class DeviceModel {
    public static final int PROGRAM_MEMORY_SIZE = 105;
    public static final int REGISTERS_SIZE = 15;
    public static final int CALL_STACK_SIZE = 5;

    private final AtomicInteger pc = new AtomicInteger(0);

    private final AtomicLong x2 = new AtomicLong(0);
    private final AtomicInteger dots = new AtomicInteger(0);

    private final AtomicLong x = new AtomicLong(0);
    private final AtomicLong x1 = new AtomicLong(0);
    private final AtomicLong y = new AtomicLong(0);
    private final AtomicLong z = new AtomicLong(0);
    private final AtomicLong t = new AtomicLong(0);

    private final long[] registers = new long[REGISTERS_SIZE];
    private final int[] callStack = new int[CALL_STACK_SIZE];

    private final AtomicBoolean executionFlag = new AtomicBoolean(false);

    private final int[] memory = new int[PROGRAM_MEMORY_SIZE];
    private final int[] memoryUpload = new int[PROGRAM_MEMORY_SIZE];
    private final AtomicBoolean memoryUploadFlag = new AtomicBoolean(false);

    private final AtomicInteger angleMode = new AtomicInteger(AngleMode.RADIAN.mode());

    public void powerOn() {
        pc.set(0);

        x2.set(Indicator.EMPTY.indicator());
        dots.set(0);

        x.set(0);
        x1.set(0);
        y.set(0);
        z.set(0);
        t.set(0);

        Arrays.fill(registers, 0);
        Arrays.fill(memory, 0);
        Arrays.fill(memoryUpload, 0);
    }

    public void setPc(int pc) {
        this.pc.set(pc);
    }

    public int getPc() {
        return pc.get();
    }

    public void setIndicator(long x2, int dots) {
        this.x2.set(x2);
        this.dots.set(dots);
    }

    public Indicator getIndicator() {
        return new Indicator(x2.get(), dots.get());
    }

    public void setX(long x) {
        this.x.set(x);
    }

    public long getX() {
        return x.get();
    }

    public void setX1(long x1) {
        this.x1.set(x1);
    }

    public long getX1() {
        return x1.get();
    }

    public void setY(long y) {
        this.y.set(y);
    }

    public long getY() {
        return y.get();
    }

    public void setZ(long z) {
        this.z.set(z);
    }

    public long getZ() {
        return z.get();
    }

    public void setT(long t) {
        this.t.set(t);
    }

    public long getT() {
        return t.get();
    }

    public void setRegisters(long[] values) {
        if (values.length != REGISTERS_SIZE) {
            throw new IllegalArgumentException("Registers array must be of length " + REGISTERS_SIZE);
        }

        synchronized (registers) {
            System.arraycopy(values, 0, registers, 0, REGISTERS_SIZE);
        }
    }

    public long[] getRegisters() {
        synchronized (registers) {
            return Arrays.copyOf(registers, registers.length);
        }
    }


    public void setCallStack(int[] values) {
        if (values.length != CALL_STACK_SIZE) {
            throw new IllegalArgumentException("Call stack array must be of size " + CALL_STACK_SIZE);
        }

        synchronized (callStack) {
            System.arraycopy(values, 0, callStack, 0, CALL_STACK_SIZE);
        }
    }

    public int[] getCallStack() {
        synchronized (callStack) {
            return Arrays.copyOf(callStack, callStack.length);
        }
    }

    public void setExecutionFlag(boolean flag) {
        executionFlag.set(flag);
    }

    public boolean getExecutionFlag() {
        return executionFlag.get();
    }

    public void setAngleMode(AngleMode mode) {
        angleMode.set(mode.mode());
    }

    public int getAngleMode() {
        return angleMode.get();
    }

    public void setMemory(int[] array) {
        synchronized (memory) {
            for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
                memory[i] = array[i] & 0xFF;
            }
        }
    }

    public int[] getMemory() {
        synchronized (memory) {
            return Arrays.copyOf(memory, memory.length);
        }
    }

    public void setMemoryUploadFlag(boolean flag) {
        memoryUploadFlag.set(flag);
    }

    public boolean getMemoryUploadFlag() {
        return memoryUploadFlag.get();
    }

    public void setMemoryUpload(int[] array) {
        synchronized (memoryUpload) {
            for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
                memoryUpload[i] = array[i] & 0xFF;
            }
        }
    }

    public int[] getMemoryUpload() {
        synchronized (memoryUpload) {
            return Arrays.copyOf(memoryUpload, memoryUpload.length);
        }
    }

    public static int getRealPc10(int pc) {
        int absValue = ((pc & 0xF0) >> 4) * 10 + (pc & 0xF);
        if (absValue <= 104) return absValue;
        if (absValue <= 111) return absValue - 105;
        return absValue - 112;
    }
}
