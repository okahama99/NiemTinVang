package com.ntv.ntvcons_backend.constants;

import java.util.ArrayList;
import java.util.List;

public enum FileType {
    BLUEPRINT_DOC("BLUEPRINT_DOC"),
    PROJECT_DOC("PROJECT_DOC");
    private final String stringValue;

    FileType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /* For @Converter */
    public static FileType fromStringValue(String stringValue) {
        switch (stringValue) {
            case "BLUEPRINT_DOC":
                return FileType.BLUEPRINT_DOC;

            case "PROJECT_DOC":
                return FileType.PROJECT_DOC;

            default:
                throw new IllegalArgumentException("stringValue: '" + stringValue + "' not supported.");
        }
    }
}
