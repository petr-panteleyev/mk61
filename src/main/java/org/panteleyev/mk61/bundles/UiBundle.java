// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.mk61.bundles;

import java.util.ListResourceBundle;

import static org.panteleyev.mk61.bundles.Internationalization.I18N_ABOUT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_CLOSE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_EXIT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_FILE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_HELP;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_LOAD;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS_AND_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_STACK;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_WINDOW;

public class UiBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {I18N_ABOUT, "About"},
                {I18N_CLOSE, "Close"},
                {I18N_EXIT, "Exit"},
                {I18N_FILE, "File"},
                {I18N_HELP, "Help"},
                {I18N_LOAD, "Load"},
                {I18N_SAVE, "Save"},
                {I18N_WINDOW, "Window"},
                //
                {I18N_STACK, "Stack"},
                {I18N_REGISTERS, "Registers"},
                {I18N_REGISTERS_AND_MEMORY, "Registers and Memory"},
        };
    }
}
