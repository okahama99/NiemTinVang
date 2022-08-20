package com.ntv.ntvcons_backend.constants;

import java.util.ArrayList;
import java.util.List;

/** If change enum please add new enum here,
 * update all old in DB to new enum.
 * Only then deleted old enum here */
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    UNKNOWN("UNKNOWN");

    private final String stringValue;

    Gender(String stringValue) {
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
    public static Gender fromStringValue(String stringValue) {
        switch (stringValue) {
            case "MALE":
                return Gender.MALE;

            case "FEMALE":
                return Gender.FEMALE;

            case "UNKNOWN":
                return Gender.UNKNOWN;

            default:
                throw new IllegalArgumentException("stringValue: '" + stringValue + "' not supported.");
        }
    }
}
