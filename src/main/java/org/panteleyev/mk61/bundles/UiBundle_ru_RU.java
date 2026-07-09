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

public class UiBundle_ru_RU extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {I18N_ABOUT, "О программе"},
                {I18N_AUTHOR, "Автор"},
                {I18N_BUILD, "Сборка"},
                {I18N_CLOSE, "Закрыть"},
                {I18N_DESCRIPTION, "Описание"},
                {I18N_EXIT, "Выход"},
                {I18N_FILE, "Файл"},
                {I18N_HELP, "Справка"},
                {I18N_LIBRARY, "Библиотека"},
                {I18N_LOAD, "Загрузить"},
                {I18N_OPEN, "Открыть"},
                {I18N_PROGRAM, "Программа"},
                {I18N_RESET, "Сбросить"},
                {I18N_SAVE, "Сохранить"},
                {I18N_SAVE_AS, "Сохранить как"},
                {I18N_SOURCE, "Источник"},
                {I18N_TITLE, "Название"},
                {I18N_VIEW, "Вид"},
                {I18N_WINDOW, "Окно"},
                //
                {I18N_APP_TITLE, "Электроника МК-61"},
                {I18N_MK61_PROGRAM, "Программа МК-61"},
                {I18N_OPEN_PROGRAM, "Открыть программу"},
                {I18N_READ_FROM_MEMORY, "Считать из памяти"},
                {I18N_SAVE_TO_MEMORY, "Записать в память"},
                {I18N_STACK, "Стек"},
                {I18N_REGISTERS, "Регистры"},
                {I18N_REGISTERS_AND_MEMORY, "Регистры и память"},
                {I18N_SAVE_PROGRAM, "Сохранить программу"},
                {I18N_SHOW_MNEMONICS, "Показывать мнемоники"},
                //
                {I18N_COPYRIGHT, """
                    Copyright © 2025-2026  Пётр Пантелеев <petr-panteleyev@yandex.ru>
                    
                    Лицензия GPLv3: GNU GPL версии 3.
                    Это свободное ПО: вы можете изменять и распространять его.
                    Нет НИКАКИХ ГАРАНТИЙ в пределах действующего законодательства.
                    """},
                {I18N_APP_DESCRIPTION, """
                    Эмулятор программируемого микрокалькулятора Электроника МК-61
                    """}
        };
    }
}
