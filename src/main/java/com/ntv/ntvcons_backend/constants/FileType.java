package com.ntv.ntvcons_backend.constants;

import java.util.ArrayList;
import java.util.List;

public enum FileType {
    BLUEPRINT_DOC("BLUEPRINT_DOC"),
    PROJECT_DOC("PROJECT_DOC"),
    REPORT_DOC("REPORT_DOC"),
    REQUEST_DOC("REQUEST_DOC"),
    TASK_DOC("TASK_DOC"),
    USER_AVATAR("USER_AVATAR"),
    WORKER_AVATAR("WORKER_AVATAR"),
    WORKER_DOC("WORKER_DOC");

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

            case "REPORT_DOC":
                return FileType.REPORT_DOC;

            case "REQUEST_DOC":
                return FileType.REQUEST_DOC;

            case "TASK_DOC":
                return FileType.TASK_DOC;

            case "USER_AVATAR":
                return FileType.USER_AVATAR;

            case "WORKER_AVATAR":
                return FileType.WORKER_AVATAR;

            case "WORKER_DOC":
                return FileType.WORKER_DOC;

            default:
                throw new IllegalArgumentException("stringValue: '" + stringValue + "' not supported.");
        }
    }
}
