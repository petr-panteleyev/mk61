/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.engine;

import org.panteleyev.mk61.core.AngleMode;
import org.panteleyev.mk61.core.Emulator;
import org.panteleyev.mk61.util.ThreadUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.panteleyev.mk61.engine.Constants.DISPLAY_DELAY;

public class Engine {
    private static class ExecutionThread extends Thread {
        ExecutionThread(Runnable runnable) {
            super(runnable);
            setDaemon(true);
            setName("Processor");
        }
    }

    private final AtomicInteger angleMode = new AtomicInteger(AngleMode.RADIAN.mode());

    private final AtomicReference<Emulator> emulator = new AtomicReference<>(null);

    private final Executor mk61Executor = Executors.newSingleThreadExecutor(ExecutionThread::new);
    private final IndicatorCallback indicatorCallback;

    public Engine(IndicatorCallback indicatorCallback) {
        this.indicatorCallback = indicatorCallback;
    }

    public void powerOn() {
        if (emulator.get() != null) {
            // Дважды не включаем
            return;
        }

        var e = new Emulator(angleMode, ir -> {
            indicatorCallback.display(ir);
            ThreadUtil.sleep(DISPLAY_DELAY);
        });

        emulator.set(e);
        mk61Executor.execute(e);
    }

    public void powerOff() {
        if (emulator.get() == null) {
            return;
        }

        emulator.get().stopEmulator(true);
        emulator.set(null);
        indicatorCallback.display(IR.EMPTY);
    }

    public void processButton(KeyboardButton button) {
        if (emulator.get() != null) {
            emulator.get().keypad(button.keyCode());
        }
    }

    public void setAngleMode(AngleMode mode) {
        angleMode.set(mode.mode());
    }
}
