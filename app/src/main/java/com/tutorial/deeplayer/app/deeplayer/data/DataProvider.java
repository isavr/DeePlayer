package com.tutorial.deeplayer.app.deeplayer.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.ProviderCompartment;

public class DataProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CupboardDbHelper dbHelper;
    //static final int USER = 100;
    static final int USER_WITH_ID = 101;
    static final int RADIO_WITH_ID = 201;

    public DataProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = CupboardFactory.cupboard().withContext(getContext()).delete(uri, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USER_WITH_ID: {
                return DataContract.UserEntry.CONTENT_TYPE;
            }
            case RADIO_WITH_ID: {
                return DataContract.RadioEntry.CONTENT_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

    }

    @Override
    public boolean onCreate() {
        dbHelper = new CupboardDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        ProviderCompartment pc = CupboardFactory.cupboard().withContext(getContext());
        Uri returnUrl;
        switch (match) {
            case USER_WITH_ID: {
                //returnUrl = pc.put(uri, values);
                //break;
            }
            case RADIO_WITH_ID: {
                returnUrl = pc.put(uri, values);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUrl;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        final int match = sUriMatcher.match(uri);
        ProviderCompartment pc = CupboardFactory.cupboard().withContext(getContext());
        Cursor retCursor;
        switch (match) {
            case USER_WITH_ID: {
                retCursor = pc.query(uri, User.class)
                        .withProjection(projection).withSelection(selection, selectionArgs)
                        .orderBy(sortOrder).getCursor();
                break;
            }
            case RADIO_WITH_ID: {
                retCursor = pc.query(uri, Radio.class)
                        .withProjection(projection).withSelection(selection, selectionArgs)
                        .orderBy(sortOrder).getCursor();
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int rowsUpdated = CupboardFactory.cupboard().withContext(getContext()).
                update(uri, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // TODO: bulk insert
//    @Override
//    public int bulkInsert(Uri uri, ContentValues[] values) {
//        final int match = sUriMatcher.match(uri);
//        ProviderCompartment pc = CupboardFactory.cupboard().withContext(getContext());
//        int itemsAdded = 0;
//        switch (match) {
//            case USER_WITH_ID: {
//
//            }
//            default: {
//                return super.bulkInsert(uri, values);
//            }
//        }
//        /*final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        switch (match) {
//            case WEATHER:
//                db.beginTransaction();
//                int returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        normalizeDate(value);
//                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }*/
//    }

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.

        // matcher.addURI(authority, DataContract.PATH_USER, USER);
        matcher.addURI(authority, DataContract.PATH_USER + "/*", USER_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_RADIO + "/*", RADIO_WITH_ID);
        return matcher;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
