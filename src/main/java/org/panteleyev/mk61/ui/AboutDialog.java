// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.ui;

import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import org.panteleyev.fx.BaseDialog;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import static org.panteleyev.fx.factories.BoxFactory.vBox;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.mk61.Mk61Application.BUILD_INFO_BUNDLE;
import static org.panteleyev.mk61.Mk61Application.UI;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_ABOUT;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_APP_DESCRIPTION;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_BUILD;
import static org.panteleyev.mk61.bundles.Internationalization.I18N_COPYRIGHT;
import static org.panteleyev.mk61.ui.Mk61Controller.APP_TITLE;
import static org.panteleyev.mk61.ui.StyleSheet.BIG_SPACING;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_ABOUT_ICON;
import static org.panteleyev.mk61.ui.StyleSheet.CSS_ABOUT_LABEL;

public class AboutDialog extends BaseDialog<Object> {
    private record BuildInformation(String version, String timestamp) {
        static BuildInformation load() {
            return new BuildInformation(
                    BUILD_INFO_BUNDLE.getString("version"),
                    BUILD_INFO_BUNDLE.getString("timestamp")
            );
        }
    }

    private static final String RUNTIME = System.getProperty("java.vm.name")
            + " " + System.getProperty("java.vm.version")
            + " " + System.getProperty("os.arch");
    private static final BuildInformation BUILD = BuildInformation.load();

    private static final ZoneId LOCAL_TIME_ZONE = TimeZone.getDefault().toZoneId();
    private static final DateTimeFormatter TIMESTAMP_PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV");
    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AboutDialog() {
        super(StyleSheet.ABOUT_DIALOG);

        setHeaderText(APP_TITLE + " " + BUILD.version());
        var iconView = new ImageView(Picture.ICON.getImage());
        iconView.getStyleClass().add(CSS_ABOUT_ICON);
        setGraphic(iconView);

        setTitle(string(UI, I18N_ABOUT));

        var aboutLabel = label(APP_TITLE + " " + BUILD.version());
        aboutLabel.getStyleClass().add(CSS_ABOUT_LABEL);

        var timestamp = ZonedDateTime.parse(BUILD.timestamp(), TIMESTAMP_PARSER)
                .withZoneSameInstant(LOCAL_TIME_ZONE);

        var buildString = string(UI, I18N_BUILD) + ": " + LOCAL_FORMATTER.format(timestamp);

        var box = vBox(BIG_SPACING,
                label(string(UI, I18N_APP_DESCRIPTION)),
                label(buildString + "\nVM: " + RUNTIME),
                label(string(UI, I18N_COPYRIGHT))
        );

        getDialogPane().setContent(box);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        centerOnScreen();
    }
}
