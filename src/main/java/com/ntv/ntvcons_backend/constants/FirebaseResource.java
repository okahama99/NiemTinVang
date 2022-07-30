package com.ntv.ntvcons_backend.constants;

public class FirebaseResource {
    public static final String PROJECT_ID = "niemtinvang-bbc35";
    public static final String BUCKET_NAME = PROJECT_ID + ".appspot.com";
    public static final String DOWNLOAD_URL =
            "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";
    public static final String PRIVATE_KEY =
            "src/main/java/com/ntv/ntvcons_backend/services/fileStorage/niemtinvang-bbc35-firebase-adminsdk-oqxbq-c788387848.json";
}