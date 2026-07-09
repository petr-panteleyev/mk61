// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

public record ProgramInfo(String title, String author, String source, String description) {
    public ProgramInfo {
        title = title == null ? "" : title;
        author = author == null ? "" : author;
        source = source == null ? "" : source;
        description = description == null ? "" : description;
    }

    public ProgramInfo() {
        this ("", "", "", "");
    }
}
