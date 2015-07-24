package com.tutorial.deeplayer.app.deeplayer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import nl.qbusict.cupboard.CupboardFactory;

/**
 * Created by ilya.savritsky on 21.07.2015.
 */
public class CupboardDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RxCupboard.db";
    private static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase database;

    static {
        // Register our models with Cupboard as usual
        CupboardFactory.cupboard().register(User.class);
        CupboardFactory.cupboard().register(Radio.class);
    }

    public synchronized static SQLiteDatabase getConnection(Context context) {
        if (database == null) {
            // Construct the single helper and open the unique(!) db connection for the app
            database = new CupboardDbHelper(context.getApplicationContext()).getWritableDatabase();
        }
        return database;
    }

    public CupboardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CupboardFactory.cupboard().withDatabase(sqLiteDatabase).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        CupboardFactory.cupboard().withDatabase(sqLiteDatabase).upgradeTables();
    }
}
