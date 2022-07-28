package com.ntv.ntvcons_backend.constants;

import java.util.ArrayList;
import java.util.List;

/** If change enum please add new enum here,
 * update all old in DB to new enum.
 * Only then deleted old enum here */
public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    DONE("DONE"),
    APPROVED("APPROVED"),
    DENIED("DENIED"),
    /* Other avoid query status must start with 'DELETED_'
     * to call query: findBy...AndStatusNotContains('DELETED')  */
    DELETED("DELETED"),
    REMOVED("REMOVED");

    private final String stringValue;

    Status(String stringValue) {
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
    public static Status fromStringValue(String stringValue) {
        switch (stringValue) {
            case "ACTIVE":
                return Status.ACTIVE;

            case "INACTIVE":
                return Status.INACTIVE;

            case "PENDING":
                return Status.PENDING;

            case "PROCESSING":
                return Status.PROCESSING;

            case "DONE":
                return Status.DONE;

            case "APPROVED":
                return Status.APPROVED;

            case "DENIED":
                return Status.DENIED;

            case "DELETED":
                return Status.DELETED;

            case "REMOVED":
                return Status.REMOVED;

            default:
                throw new IllegalArgumentException("stringValue: '" + stringValue + "' not supported.");
        }
    }

    /* For @Repository */
    public static List<Status> getAllNonDefaultSearchStatus() {
        List<Status> nonDefaultSearchStatusList = new ArrayList<>();

        /* Add any status that aren't visible by default */
        nonDefaultSearchStatusList.add(Status.DELETED);
        nonDefaultSearchStatusList.add(Status.REMOVED);

        return nonDefaultSearchStatusList;
    }
}
