/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.engine;

import org.panteleyev.mk61.core.Emulator;

import java.util.concurrent.atomic.AtomicReference;

public class Engine {
    private final AtomicReference<Emulator> emulator = new AtomicReference<>(null);

    private final Mk61DeviceModel deviceModel = new Mk61DeviceModel();

    public Engine() {
    }

    public Mk61DeviceModel deviceModel() {
        return deviceModel;
    }

    public void powerOn() {
        synchronized (emulator) {
            if (emulator.get() != null) {
                // Дважды не включаем
                return;
            }

            deviceModel.powerOn();
            var e = new Emulator(deviceModel);
            emulator.set(e);
            e.start();
        }
    }

    public void powerOff() {
        synchronized (emulator) {
            if (emulator.get() == null) {
                return;
            }

            emulator.get().interrupt();
            emulator.set(null);
        }
    }

    public void processButton(KeyboardButton button) {
        synchronized (emulator) {
            if (emulator.get() != null) {
                emulator.get().keypad(button.keyCode());
            }
        }
    }
}
