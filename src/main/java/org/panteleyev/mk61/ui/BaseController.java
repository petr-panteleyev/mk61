/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.ui;

import javafx.stage.Stage;
import org.panteleyev.fx.Controller;

public class BaseController extends Controller {

    public BaseController() {
        super("/main.css");
    }

    public BaseController(Stage stage) {
        super(stage, "/main.css");
    }

    public void onClose() {
        getStage().close();
    }
}
