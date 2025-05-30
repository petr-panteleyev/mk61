/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61;

import org.panteleyev.freedesktop.directory.XDGBaseDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static org.panteleyev.freedesktop.Utility.isLinux;

public class ApplicationFiles {
    public enum AppFile {
        WINDOWS("windows.xml");

        static final Set<AppFile> CONFIG_FILES = Set.of(
                WINDOWS
        );

        private final String fileName;

        AppFile(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private static final ApplicationFiles FILES = new ApplicationFiles();

    private static final String PACKAGE_NAME = "panteleyev.org";
    private static final String APP_NAME = "mk61";
    private static final String LOGS_DIR_NAME = "logs";

    private final Path configDirectory;
    private final Path dataDirectory;
    private final Path logDirectory;

    private final Map<AppFile, Path> fileMap = new EnumMap<>(AppFile.class);

    public ApplicationFiles() {
        if (isLinux()) {
            configDirectory = XDGBaseDirectory.getConfigHome()
                    .resolve(PACKAGE_NAME)
                    .resolve(APP_NAME);
            dataDirectory = XDGBaseDirectory.getDataHome()
                    .resolve(PACKAGE_NAME)
                    .resolve(APP_NAME);
        } else {
            configDirectory = Path.of(System.getProperty("user.home"), ".mk61");
            dataDirectory = configDirectory;
        }
        logDirectory = dataDirectory.resolve(LOGS_DIR_NAME);

        for (var appFile : AppFile.CONFIG_FILES) {
            fileMap.put(appFile, configDirectory.resolve(appFile.getFileName()));
        }
    }

    public static ApplicationFiles files() {
        return FILES;
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public void initialize() {
        initDirectory(configDirectory, "Application");
        initDirectory(dataDirectory, "Data");
        initDirectory(logDirectory, "Log");
    }

    public void write(AppFile appFile, Consumer<OutputStream> fileConsumer) {
        try (var out = Files.newOutputStream(fileMap.get(appFile))) {
            fileConsumer.accept(out);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void read(AppFile appFile, Consumer<InputStream> fileConsumer) {
        var file = fileMap.get(appFile);
        if (!Files.exists(file)) {
            return;
        }

        try (var in = Files.newInputStream(file)) {
            fileConsumer.accept(in);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static void initDirectory(Path path, String name) {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ex) {
            // Do nothing
        } catch (IOException ex) {
            throw new RuntimeException(name + " directory cannot be created");
        }
    }
}