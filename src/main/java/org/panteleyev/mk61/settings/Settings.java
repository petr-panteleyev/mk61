/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.settings;

import org.panteleyev.fx.Controller;
import org.panteleyev.mk61.ApplicationFiles;

import java.util.function.Consumer;

import static org.panteleyev.mk61.ApplicationFiles.files;

public final class Settings {
    private static final Settings SETTINGS = new Settings(files());

    private final ApplicationFiles files;

    private final WindowsSettings windowsSettings = new WindowsSettings();

    public static Settings settings() {
        return SETTINGS;
    }

    private Settings(ApplicationFiles files) {
        this.files = files;
    }

    public void update(Consumer<Settings> block) {
        block.accept(this);
    }

    public void load() {
        files.read(ApplicationFiles.AppFile.WINDOWS, windowsSettings::load);
    }

    public void loadStagePosition(Controller controller) {
        windowsSettings.restoreWindowPosition(controller);
    }

    public void saveWindowsSettings() {
        files.write(ApplicationFiles.AppFile.WINDOWS, windowsSettings::save);
    }
}
