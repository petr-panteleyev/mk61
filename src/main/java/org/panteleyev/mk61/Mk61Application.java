// Copyright © 2025-2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.panteleyev.mk61.bundles.UiBundle;
import org.panteleyev.mk61.ui.Mk61Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.ResourceBundle.getBundle;
import static javafx.application.Platform.runLater;
import static org.panteleyev.mk61.ApplicationFiles.files;
import static org.panteleyev.mk61.settings.Settings.settings;

public class Mk61Application extends Application {
    private static final Logger LOGGER = Logger.getLogger(Mk61Application.class.getName());

    public static final ResourceBundle UI = getBundle(UiBundle.class.getCanonicalName());
    public static final ResourceBundle BUILD_INFO_BUNDLE = getBundle("buildInfo");

    private final static String LOG_PROPERTIES = """
            handlers                                = java.util.logging.FileHandler
            
            java.util.logging.FileHandler.level     = ALL
            java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
            java.util.logging.FileHandler.pattern   = %FILE_PATTERN%
            java.util.logging.FileHandler.append    = true
            
            java.util.logging.SimpleFormatter.format = %1$tF %1$tk:%1$tM:%1$tS %2$s%n%4$s: %5$s%6$s%n
            """;

    private static final String LOG_FILE_NAME = "mk52.log";

    @Override
    public void start(Stage stage) throws Exception {
        files().initialize();
        settings().load();

        Font.loadFont(getResourceUrl("/fonts/Pixel-LCD-7.ttf"), 14);
        Font.loadFont(getResourceUrl("/fonts/JetBrainsMono-Medium.ttf"), 14);

        var logProperties = LOG_PROPERTIES.replace("%FILE_PATTERN%",
                files().getLogDirectory().resolve(LOG_FILE_NAME).toString().replace("\\", "/"));
        try (var inputStream = new ByteArrayInputStream(logProperties.getBytes(UTF_8))) {
            LogManager.getLogManager().readConfiguration(inputStream);
        }

        setDefaultUncaughtExceptionHandler((_, e) -> uncaughtException(e));

        new Mk61Controller(stage);
        stage.show();
    }

    private static void uncaughtException(Throwable e) {
        LOGGER.log(Level.SEVERE, "Uncaught exception", e);
        runLater(() -> new Alert(Alert.AlertType.ERROR, e.toString()).showAndWait());
    }

    public static Logger logger() {
        return LOGGER;
    }

    static void main(String[] args) {
        launch(args);
    }

    public static String getResourceAsString(String name) {
        try (var in = Mk61Application.class.getResourceAsStream(name)) {
            return new String(requireNonNull(in, "Resource " + name + " not found").readAllBytes(), UTF_8);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String getResourceUrl(String name) {
        return requireNonNull(Mk61Application.class.getResource(name), "Resource " + name + " not found").toString();
    }
}
