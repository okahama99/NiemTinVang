package com.ntv.ntvcons_backend.constants;

public enum SearchType {
    /* FileType */
    FILE_TYPE_BY_NAME_CONTAINS,
    FILE_TYPE_BY_EXTENSION_CONTAINS,

    /* Task */
    TASK_BY_NAME_CONTAINS,
    TASK_BY_PROJECT_ID,
    TASK_BY_PLAN_START_DATE,
    TASK_BY_PLAN_END_DATE,
    TASK_BY_ACTUAL_START_DATE,
    TASK_BY_ACTUAL_END_DATE
}
