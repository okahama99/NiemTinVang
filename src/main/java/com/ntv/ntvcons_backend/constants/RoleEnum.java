package com.ntv.ntvcons_backend.constants;

import java.util.ArrayList;
import java.util.List;

/** If change enum please add new enum here,
 * update all old in DB to new enum.
 * Only then deleted old enum here */
public enum RoleEnum {
    USER("User", 4L),
    CUSTOMER("Customer", 14L),
    STAFF("Staff", 24L),
    CONSULTANT("Consultant", 34L),
    ENGINEER("Engineer", 44L),
    ADMIN("Admin", 54L);

    private final String stringValue;
    private final long roleId;

    RoleEnum(String stringValue, long roleId) {
        this.stringValue = stringValue;
        this.roleId = roleId;
    }

    public String getStringValue() {
        return stringValue;
    }

    public long getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static List<Long> getAllNTVManager() {
        List<Long> roleIdList = new ArrayList<>();

        roleIdList.add(STAFF.getRoleId());
        roleIdList.add(CONSULTANT.getRoleId());
        roleIdList.add(ENGINEER.getRoleId());
        roleIdList.add(ADMIN.getRoleId());

        return roleIdList;
    }
}
