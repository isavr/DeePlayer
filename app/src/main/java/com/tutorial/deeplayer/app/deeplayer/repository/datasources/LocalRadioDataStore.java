package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class LocalRadioDataStore extends BaseLocalDataStore {
    private static final String TAG = LocalRadioDataStore.class.getSimpleName();

    private Callable<List<Radio>> getRadiosFromDb() {
        return () -> {
            final Context context = DeePlayerApp.get().getApplicationContext();
            List<Radio> radios = new ArrayList<>();
            Cursor c = context.getContentResolver().query(SchematicDataProvider.Radios.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                for (c.moveToFirst();c.moveToNext();) {
                    Radio radio = DataContract.RadioConverter.convertFromCursor(c);
                    radios.add(radio);
                }
            }
            c.close();
            return radios;
        };
    }

//    public final Action1<List<ContentValues>> saveToDb = radioList -> {
//        if (radioList != null) {
//            final Context context = DeePlayerApp.get().getApplicationContext();
//            ContentValues[] values = new ContentValues[radioList.size()];
//            values = radioList.toArray(values);
//            context.getContentResolver().delete(SchematicDataProvider.Radios.CONTENT_URI, null, null);
//            int insertedRows = context.getContentResolver().bulkInsert(SchematicDataProvider.Radios.CONTENT_URI,
//                    values);
//            Log.d(TAG, "Rows inserted -> " + insertedRows);
//        }
//    };

    public Observable<List<Radio>> getRadios() {
        return makeObservable(getRadiosFromDb());
    }

}
