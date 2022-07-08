package com.ntv.ntvcons_backend.constants;

public class SearchType {
    public enum BLUEPRINT {
        BY_ID,
        BY_PROJECT_ID,
        BY_NAME,
    }
    public enum ALL_BLUEPRINT {
        BY_NAME_CONTAINS,
        BY_DESIGNER_NAME,
        BY_DESIGNER_NAME_CONTAINS,
    }

    public enum LOCATION {
        BY_ID,
        BY_COORDINATE,
    }
    public enum ALL_LOCATION {
        BY_STREET,
        BY_WARD,
        BY_AREA,
        BY_DISTRICT,
        BY_CITY,
        BY_PROVINCE,
        BY_COUNTRY,
    }

    public enum PROJECT {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_PROJECT {
        BY_LOCATION_ID,
        BY_NAME_CONTAINS,
        BY_PLAN_START_DATE,
        BY_PLAN_END_DATE,
        BY_ESTIMATED_COST,
        BY_ACTUAL_START_DATE,
        BY_ACTUAL_END_DATE,
        BY_ACTUAL_COST,
    }

    public enum PROJECT_MANAGER {
        BY_ID,
    }
    public enum ALL_PROJECT_MANAGER {
        BY_PROJECT_ID,
        BY_MANAGER_ID,
    }

    public enum REPORT_DETAIL {
        BY_ID,
    }
    public enum ALL_REPORT_DETAIL {
        BY_REPORT_ID,
        BY_ITEM_DESC,
    }

    public enum REQUEST_DETAIL {
        BY_ID,
    }
    public enum ALL_REQUEST_DETAIL {
        BY_REQUEST_ID,
        BY_ITEM_DESC,
    }

    public enum FILE_TYPE {
        BY_ID,
        BY_NAME,
        BY_EXTENSION,
    }
    public enum ALL_FILE_TYPE {
        BY_NAME_CONTAINS,
        BY_EXTENSION_CONTAINS,
    }

    public enum REPORT {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REPORT {
        BY_PROJECT_ID,
        BY_REPORTER_ID,
        BY_REPORT_TYPE_ID,
        BY_NAME_CONTAINS,
        BY_REPORT_DATE,
    }

    public enum REQUEST {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REQUEST {
        BY_PROJECT_ID,
        BY_REQUESTER_ID,
        BY_VERIFIER_ID,
        BY_REQUEST_TYPE_ID,
        BY_NAME_CONTAINS,
        BY_REQUEST_DATE,
    }

    public enum REPORT_TYPE {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REPORT_TYPE {
        BY_NAME_CONTAINS,
    }

    public enum REQUEST_TYPE {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REQUEST_TYPE {
        BY_NAME_CONTAINS,
    }

    public enum USER {
        BY_ID,
        BY_USERNAME,
        BY_PHONE,
    }
    public enum ALL_USER {
        BY_ROLE_ID,
        BY_USERNAME_CONTAINS,
        BY_PHONE_CONTAINS,
    }

    public enum ROLE {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_ROLE {
        BY_NAME_CONTAINS,
    }

    public enum TASK {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_TASK {
        BY_NAME_CONTAINS,
        BY_PROJECT_ID,
        BY_PLAN_START_DATE,
        BY_PLAN_END_DATE,
        BY_ACTUAL_START_DATE,
        BY_ACTUAL_END_DATE,
    }

    public enum TASK_ASSIGNMENT {
        BY_ID,
        BY_TASK_ID,
    }
    public enum ALL_TASK_ASSIGNMENT {
        BY_ASSIGNER_ID,
        BY_ASSIGNEE_ID,
        BY_ASSIGN_DATE,
        BY_REMOVE_DATE,
    }

}
