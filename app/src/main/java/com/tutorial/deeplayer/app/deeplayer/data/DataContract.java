package com.tutorial.deeplayer.app.deeplayer.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilya.savritsky on 21.07.2015.
 */
public class DataContract {
    public static final String CONTENT_AUTHORITY = "com.tutorial.deeplayer.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_RADIO = "radio";

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        /*public static final String TABLE_NAME = "user";

        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_LAST_NAME = "lastName";
        public static final String COLUMN_USER_FIRST_NAME = "firstName";
        public static final String COLUMN_USER_BIRTHDAY = "birthday";
        public static final String COLUMN_USER_INSCRIPTION_DATE = "inscriptionDate";
        public static final String COLUMN_USER_GENDER = "gender";
        public static final String COLUMN_USER_COUNTRY = "country";
        public static final String COLUMN_USER_LANGUAGE = "lang";
        public static final String COLUMN_USER_STATUS = "status";
        public static final String COLUMN_USER_LINK = "link";
        public static final String COLUMN_USER_TRACKLIST = "tracklist";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }*/
    }

    public static final class RadioEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RADIO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RADIO;

        /*public static final String TABLE_NAME = "radio";

        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_LAST_NAME = "lastName";
        public static final String COLUMN_USER_FIRST_NAME = "firstName";
        public static final String COLUMN_USER_BIRTHDAY = "birthday";
        public static final String COLUMN_USER_INSCRIPTION_DATE = "inscriptionDate";
        public static final String COLUMN_USER_GENDER = "gender";
        public static final String COLUMN_USER_COUNTRY = "country";
        public static final String COLUMN_USER_LANGUAGE = "lang";
        public static final String COLUMN_USER_STATUS = "status";
        public static final String COLUMN_USER_LINK = "link";
        public static final String COLUMN_USER_TRACKLIST = "tracklist";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }*/
    }

}
