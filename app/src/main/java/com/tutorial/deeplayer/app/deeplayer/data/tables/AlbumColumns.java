package com.tutorial.deeplayer.app.deeplayer.data.tables;

import com.tutorial.deeplayer.app.deeplayer.data.Database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.References;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
public interface AlbumColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT) String TITLE = "title";
    @DataType(DataType.Type.TEXT) String UPC = "upc";

    @DataType(DataType.Type.INTEGER)
    @References(table = Database.Tables.GENRES, column = GenreColumns.ID) String GENRE_ID = "genre_id";

    @DataType(DataType.Type.TEXT) String LABEL = "label";
    @DataType(DataType.Type.TEXT) String LINK = "link";
    @DataType(DataType.Type.INTEGER) String TRACKS_COUNT = "tracks_count";
    @DataType(DataType.Type.INTEGER) String DURATION = "duration";
    @DataType(DataType.Type.INTEGER) String FANS_COUNT = "fans_count";
    @DataType(DataType.Type.INTEGER) String RATING = "rating";
    @DataType(DataType.Type.TEXT) String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT) String RECORD_TYPE = "record_type";
    @DataType(DataType.Type.INTEGER) String IS_AVAILABLE = "is_available";
    @DataType(DataType.Type.INTEGER) String HAS_EXPLICIT_LYRICS = "explicit_lyrics";
    @DataType(DataType.Type.INTEGER)
    @References(table = Database.Tables.ARTISTS, column = ArtistColumns.ID) String ARTIST_ID = "artist_id";
    @DataType(DataType.Type.TEXT) String TRACKLIST = "tracklist";
}
