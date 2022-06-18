package com.ntv.ntvcons_backend.constants;

public enum SearchType {
    /* FileType */
    FILE_TYPE_BY_ID,
    FILE_TYPE_BY_NAME_CONTAINS,
    FILE_TYPE_BY_EXTENSION_CONTAINS,

    /* Report */
    REPORT_BY_ID,
    REPORT_BY_PROJECT_ID,
    REPORT_BY_REPORTER_ID,
    REPORT_BY_REPORT_TYPE_ID,
    REPORT_BY_REPORT_DATE,

    /* ReportType */
    REPORT_TYPE_BY_ID,
    REPORT_TYPE_BY_NAME_CONTAINS,

    /* RequestType */
    REQUEST_TYPE_BY_ID,
    REQUEST_TYPE_BY_NAME_CONTAINS,

    /* Role */
    ROLE_BY_ID,
    ROLE_BY_NAME_CONTAINS,

    /* Task */
    TASK_BY_ID,
    TASK_BY_NAME_CONTAINS,
    TASK_BY_PROJECT_ID,
    TASK_BY_PLAN_START_DATE,
    TASK_BY_PLAN_END_DATE,
    TASK_BY_ACTUAL_START_DATE,
    TASK_BY_ACTUAL_END_DATE
}
