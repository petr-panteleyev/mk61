// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
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
}
