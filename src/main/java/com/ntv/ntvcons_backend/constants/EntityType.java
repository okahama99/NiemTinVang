package com.ntv.ntvcons_backend.constants;

public enum EntityType {
    BLUEPRINT_ENTITY("Blueprint", "blueprintId", "BlueprintId"),
    POST_ENTITY("Post", "postId", "PostId"),
    PROJECT_ENTITY("Project", "projectId", "ProjectId"),
    REPORT_ENTITY("Report", "reportId", "ReportId"),
    REQUEST_ENTITY("Request", "requestId", "RequestId"),
    TASK_ENTITY("Task", "taskId", "TaskId"),
    USER_ENTITY("User", "userId", "UserId"),
    WORKER_ENTITY("Worker", "workerId", "WorkerId");

    public final String EntityName;
    /** camelCase */
    public final String EntityIdCCName;
    /** PascalCase */
    public final String EntityIdPCName;

    EntityType(String entityName, String entityIdCCName, String entityIdPCName) {
        EntityName = entityName;
        EntityIdCCName = entityIdCCName;
        EntityIdPCName = entityIdPCName;
    }
}
