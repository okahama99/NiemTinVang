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

    public enum EXTERNAL_FILE {
        BY_ID,
        BY_NAME,
        BY_LINK,
    }
    public enum ALL_EXTERNAL_FILE {
        BY_FILETYPE,
        BY_NAME_CONTAINS,
        BY_LINK_CONTAINS,
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

    public enum POST{
        BY_POST_ID,
    }
    public enum ALL_POST{
        BY_POST_CATEGORY_ID,
        BY_AUTHOR_NAME,
        BY_POST_TITLE,
        BY_OWNER_NAME,
        BY_ADDRESS,
        BY_SCALE,
    }

    public enum  POST_CATEGORY{
        BY_POST_CATEGORY_ID,
    }
    public enum ALL_POST_CATEGORY{
        BY_POST_CATEGORY_NAME,
        BY_POST_CATEGORY_DESC,
    }

    public enum PROJECT {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_PROJECT {
        BY_LOCATION_ID,
        BY_NAME_CONTAINS,
        BY_MANAGER_ID,
        BY_WORKER_ID,
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

    public enum PROJECT_WORKER {
        BY_ID,
    }
    public enum ALL_PROJECT_WORKER {
        BY_PROJECT_ID,
        BY_WORKER_ID,
    }
    public enum REPORT {
        BY_ID,
    }
    public enum ALL_REPORT {
        BY_NAME,
        BY_NAME_CONTAINS,
        BY_PROJECT_ID,
        BY_REPORTER_ID,
        BY_REPORT_TYPE_ID,
        BY_REPORT_DATE,
    }

    public enum REPORT_DETAIL {
        BY_ID,
    }
    public enum ALL_REPORT_DETAIL {
        BY_REPORT_ID,
    }

    public enum REPORT_TYPE {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REPORT_TYPE {
        BY_NAME_CONTAINS,
    }

    public enum REQUEST {
        BY_ID,
    }
    public enum ALL_REQUEST {
        BY_NAME,
        BY_NAME_CONTAINS,
        BY_PROJECT_ID,
        BY_REQUESTER_ID,
        BY_VERIFIER_ID,
        BY_REQUEST_TYPE_ID,
        BY_REQUEST_DATE,
    }

    public enum REQUEST_DETAIL {
        BY_ID,
    }
    public enum ALL_REQUEST_DETAIL {
        BY_REQUEST_ID,
    }

    public enum REQUEST_TYPE {
        BY_ID,
        BY_NAME,
    }
    public enum ALL_REQUEST_TYPE {
        BY_NAME_CONTAINS,
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
    }
    public enum ALL_TASK {
        BY_NAME,
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

    public enum USER {
        BY_ID,
        BY_USERNAME,
        BY_PHONE,
        BY_EMAIL,
    }
    public enum ALL_USER {
        BY_ROLE_ID,
        BY_USERNAME_CONTAINS,
        BY_PHONE_CONTAINS,
        BY_EMAIL_CONTAINS
    }

    public enum WORKER {
        BY_ID,
        BY_CITIZEN_ID,
    }
    public enum ALL_WORKER {
        BY_FULL_NAME,
        BY_FULL_NAME_CONTAINS,
        BY_CITIZEN_ID_CONTAINS,
        BY_ADDRESS_ID,
    }

    public enum MESSAGE{
        BY_CONVERSATION_ID_AUTHENTICATED,
        BY_CONVERSATION_ID_UNAUTHENTICATED,
    }

}
