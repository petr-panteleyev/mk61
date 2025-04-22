/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: GPL-3.0-only
 */
package org.panteleyev.mk61.ui;

import javafx.scene.image.Image;

import static java.util.Objects.requireNonNull;

public enum Picture {
    ICON("icon.png");

    private final Image image;

    Picture(String fileName) {
        image = new Image(requireNonNull(getClass().getResourceAsStream("/images/" + fileName)));
    }

    public Image getImage() {
        return image;
    }
}
