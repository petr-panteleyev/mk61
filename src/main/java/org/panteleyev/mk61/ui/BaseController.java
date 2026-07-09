// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.stage.Stage;
import org.panteleyev.fx.Controller;

public class BaseController extends Controller {

    public BaseController() {
        super(StyleSheet.MAIN);
    }

    public BaseController(Stage stage) {
        super(stage, StyleSheet.MAIN);
    }

    public void onClose() {
        getStage().close();
    }

    @Override
    public boolean isVisible() {
        return getStage().isShowing();
    }

    public void show() {
        getStage().show();
    }
}
