package com.ntv.ntvcons_backend.constants;

public class Regex {
    /** yyyy-MM-dd HH:mm */
    public static final String DATETIME_REGEX_1 =
            /* 0000 - 9999 */
            "^([0-9]{4})-(" +
                    /* Month with 31 days */
                    "((0[13578]|1[02])-(0[1-9]|[1-2][0-9]|3[0-1]))|" +
                    /* Month with 30 days */
                    "((0[469]|11)-(0[1-9]|[1-2][0-9]|30))|" +
                    /* February */
                    "((02)-(0[1-9]|[1-2][0-9])))" +
                    /* 00:00 - 23:59 */
                    "\\s([0-1][0-9]|2[0-3]):([0-5][0-9])$";

    /** yyyy-MM-dd */
    public static final String DATE_REGEX_1 =
            /* 0000 - 9999 */
            "^([0-9]{4})-(" +
                    /* Month with 31 days */
                    "((0[13578]|1[02])-(0[1-9]|[1-2][0-9]|3[0-1]))|" +
                    /* Month with 30 days */
                    "((0[469]|11)-(0[1-9]|[1-2][0-9]|30))|" +
                    /* February */
                    "((02)-(0[1-9]|[1-2][0-9])))$";

    /** 0 + (9-14) digits */
    public static final String PHONE_REGEX_1 = "^(0)([0-9]{9,14})$";

}
