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

public class UiBundle_ru_RU extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {I18N_ABOUT, "О программе"},
                {I18N_CLOSE, "Закрыть"},
                {I18N_EXIT, "Выход"},
                {I18N_FILE, "Файл"},
                {I18N_HELP, "Справка"},
                {I18N_LOAD, "Загрузить"},
                {I18N_SAVE, "Сохранить"},
                {I18N_WINDOW, "Окно"},
                //
                {I18N_STACK, "Стек"},
                {I18N_REGISTERS, "Регистры"},
                {I18N_REGISTERS_AND_MEMORY, "Регистры и память"},
        };
    }
}
