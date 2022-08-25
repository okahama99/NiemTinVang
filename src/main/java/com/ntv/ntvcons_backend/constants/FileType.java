package com.ntv.ntvcons_backend.constants;

public enum FileType {
    BLUEPRINT_DOC("BLUEPRINT_DOC"),
    PROJECT_DOC("PROJECT_DOC"),
    REPORT_DOC("REPORT_DOC"),
    REQUEST_DOC("REQUEST_DOC"),
    TASK_DOC("TASK_DOC"),
    USER_AVATAR("USER_AVATAR"),
    WORKER_AVATAR("WORKER_AVATAR"),
    MESSAGE_FILE("MESSAGE_FILE"),
    POST_FILE("POST_FILE");

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

            case "MESSAGE_FILE":
                return FileType.MESSAGE_FILE;

            case "POST_FILE":
                return FileType.POST_FILE;

            default:
                throw new IllegalArgumentException("stringValue: '" + stringValue + "' not supported.");
        }
    }
}
