package com.tutorial.deeplayer.app.deeplayer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.GenreColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.RadioColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.TrackColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.UserColumns;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;

import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
@net.simonvt.schematic.annotation.Database(className = "Database",
        fileName = "database.db", version = Database.VERSION)
public final class Database {
    public static final int VERSION = 1;

    private Database() {
    }

    public static class Tables {
        @Table(TrackColumns.class)
        public static final String TRACKS = "tracks";

        @Table(AlbumColumns.class)
        public static final String ALBUMS = "albums";

        @Table(GenreColumns.class)
        public static final String GENRES = "genres";

        @Table(RadioColumns.class)
        public static final String RADIOS = "radios";

        @Table(UserColumns.class)
        public static final String USER = "user";

        @Table(ArtistColumns.class)
        public static final String ARTISTS = "artists";
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

}
