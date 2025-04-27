/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.core;

import org.panteleyev.mk61.engine.Indicator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class Mk61DeviceModel {
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

    private final AtomicLongArray registers = new AtomicLongArray(REGISTERS_SIZE);
    private final AtomicIntegerArray callStack = new AtomicIntegerArray(CALL_STACK_SIZE);

    private final AtomicBoolean executionFlag = new AtomicBoolean(false);

    private final AtomicIntegerArray memory = new AtomicIntegerArray(PROGRAM_MEMORY_SIZE);

    private final AtomicIntegerArray memoryUpload = new AtomicIntegerArray(PROGRAM_MEMORY_SIZE);
    private final AtomicBoolean memoryUploadFlag = new AtomicBoolean(false);

    public void powerOn() {
        pc.set(0);

        x2.set(Indicator.EMPTY.indicator());
        dots.set(0);

        x.set(0);
        x1.set(0);
        y.set(0);
        z.set(0);
        t.set(0);
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

    public void setRegister(int i, long value) {
        registers.set(i, value);
    }

    public long getRegister(int i) {
        return registers.get(i);
    }

    public void setCallStack(int i, int value) {
        callStack.set(i, value);
    }

    public int getCallStack(int i) {
        return callStack.get(i);
    }

    public void setExecutionFlag(boolean flag) {
        executionFlag.set(flag);
    }

    public boolean getExecutionFlag() {
        return executionFlag.get();
    }

    public void setMemory(int[] array) {
        synchronized (memory) {
            for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
                memory.set(i, array[i] & 0xFF);
            }
        }
    }

    public int[] getMemory() {
        synchronized (memory) {
            int[] mem = new int[PROGRAM_MEMORY_SIZE];
            for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
                mem[i] = memory.get(i);
            }
            return mem;
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
                memoryUpload.set(i, array[i] & 0xFF);
            }
        }
    }

    public int[] getMemoryUpload() {
        synchronized (memoryUpload) {
            int[] mem = new int[PROGRAM_MEMORY_SIZE];
            for (int i = 0; i < PROGRAM_MEMORY_SIZE; i++) {
                mem[i] = memoryUpload.get(i);
            }
            return mem;
        }
    }

    public static int getRealPc10(int pc) {
        var absValue = ((pc & 0xF0) >> 4) * 10 + (pc & 0xF);
        if (absValue <= 104) {
            return absValue;
        }

        if (absValue <= 111) {
            return absValue - 105;
        }

        return absValue - 112;
    }
}
