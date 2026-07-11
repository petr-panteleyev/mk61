// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.bundles;

import java.util.ListResourceBundle;

import static org.panteleyev.mk61.bundles.Internationalization.I18N_ABOUT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_APP_DESCRIPTION;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_APP_TITLE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_AUTHOR;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_BUILD;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_CLOSE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_COPYRIGHT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_DESCRIPTION;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_EXIT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_FILE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_HELP;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_LIBRARY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_LOAD;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_MK61_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_OPEN;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_OPEN_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_READ_FROM_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_REGISTERS_AND_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_RESET;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_AS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_PROGRAM;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SAVE_TO_MEMORY;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SHOW_MNEMONICS;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_SOURCE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_STACK;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_TITLE;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_VIEW;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_WINDOW;

public class UiBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {I18N_ABOUT, "About"},
                {I18N_AUTHOR, "Author"},
                {I18N_BUILD, "Build"},
                {I18N_CLOSE, "Close"},
                {I18N_DESCRIPTION, "Description"},
                {I18N_EXIT, "Exit"},
                {I18N_FILE, "File"},
                {I18N_HELP, "Help"},
                {I18N_LIBRARY, "Library"},
                {I18N_LOAD, "Load"},
                {I18N_OPEN, "Open"},
                {I18N_PROGRAM, "Program"},
                {I18N_RESET, "Reset"},
                {I18N_SAVE, "Save"},
                {I18N_SAVE_AS, "Save As"},
                {I18N_SOURCE, "Source"},
                {I18N_TITLE, "Title"},
                {I18N_VIEW, "View"},
                {I18N_WINDOW, "Window"},
                //
                {I18N_APP_TITLE, "Elektronika MK-61"},
                {I18N_MK61_PROGRAM, "MK-61 Program"},
                {I18N_OPEN_PROGRAM, "Open Program"},
                {I18N_READ_FROM_MEMORY, "Read from Memory"},
                {I18N_SAVE_TO_MEMORY, "Write to Memory"},
                {I18N_STACK, "Stack"},
                {I18N_REGISTERS, "Registers"},
                {I18N_REGISTERS_AND_MEMORY, "Registers and Memory"},
                {I18N_SAVE_PROGRAM, "Save Program"},
                {I18N_SHOW_MNEMONICS, "Show Mnemonics"},
                //
                {I18N_COPYRIGHT, """
                    Copyright © 2025-2026  Petr Panteleyev <petr@panteleyev.org>
                    
                    License GPLv3: GNU GPL version 3.
                    This is free software: you are free to change and redistribute it.
                    There is NO WARRANTY, to the extent permitted by law.
                    """},
                {I18N_APP_DESCRIPTION, """
                    Emulator of the RPN programmable calculator Electronika MK-61
                    """},
        };
    }
}
