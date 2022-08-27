package com.ntv.ntvcons_backend.constants;

/** If change enum please add new enum here,
 * update all old in DB to new enum.
 * Only then deleted old enum here */
public enum Gender {
    MALE("MALE", "Nam"),
    FEMALE("FEMALE", "Nữ"),
    UNKNOWN("UNKNOWN", "Không rõ");

    private final String stringValue;
    private final String stringValueVie;

    Gender(String stringValue, String stringValueVie) {
        this.stringValue = stringValue;
        this.stringValueVie = stringValueVie;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getStringValueVie() {
        return stringValueVie;
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
