/*
 Copyright © 2025 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.mk61.settings;

import org.panteleyev.commons.xml.XMLEventReaderWrapper;
import org.panteleyev.commons.xml.XMLStreamWriterWrapper;
import org.panteleyev.fx.Controller;
import org.panteleyev.fx.StagePosition;
import org.panteleyev.fx.WindowManager;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class WindowsSettings {
    private static final QName ROOT_ELEMENT = new QName("windows");
    private static final QName WINDOW_ELEMENT = new QName("window");
    private static final QName CLASS_ATTR = new QName("class");
    private static final QName X_ATTR = new QName("x");
    private static final QName Y_ATTR = new QName("y");

    private final Map<String, StagePosition> windowMap = new ConcurrentHashMap<>();

    void storeWindowPosition(Controller controller) {
        windowMap.put(controller.getClass().getSimpleName(), controller.getStagePosition());
    }

    void restoreWindowPosition(Controller controller) {
        controller.setStagePosition(
                windowMap.get(controller.getClass().getSimpleName())
        );
    }

    void save(OutputStream out) {
        WindowManager.newInstance().getControllerStream().forEach(this::storeWindowPosition);

        try (var w = XMLStreamWriterWrapper.newInstance(out)) {
            w.document(ROOT_ELEMENT, () -> {
                for (var entry : windowMap.entrySet()) {
                    var position = entry.getValue();

                    w.element(WINDOW_ELEMENT, Map.of(
                            CLASS_ATTR, entry.getKey(),
                            X_ATTR, position.x(),
                            Y_ATTR, position.y()
                    ));
                }
            });
        }
    }

    void load(InputStream in) {
        windowMap.clear();
        try (var reader = XMLEventReaderWrapper.newInstance(in)) {
            while (reader.hasNext()) {
                var event = reader.nextEvent();
                event.ifStartElement(WINDOW_ELEMENT, element -> {
                    var className = element.getAttributeValue(CLASS_ATTR).orElseThrow();
                    windowMap.put(className, new StagePosition(
                            element.getAttributeValue(X_ATTR, 0.0),
                            element.getAttributeValue(Y_ATTR, 0.0)
                    ));
                });
            }
        }
    }
}
