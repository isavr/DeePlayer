package com.tutorial.deeplayer.app.deeplayer.data.tables;

import com.tutorial.deeplayer.app.deeplayer.data.Database;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by ilya.savritsky on 01.09.2015.
 */
public interface TrackColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey(onConflict = ConflictResolutionType.REPLACE) String ID = "_id";
    @DataType(DataType.Type.TEXT) String TYPE = "type";
    @DataType(DataType.Type.TEXT) String SHARE = "share";
    @DataType(DataType.Type.INTEGER) String IS_FAVOURITE = "is_favourite";
    @DataType(DataType.Type.INTEGER) String IS_RECOMMENDED = "is_recommended";
    @DataType(DataType.Type.TEXT) String TITLE = "title";
    @DataType(DataType.Type.TEXT) String TITLE_SHORT = "title_short";
    @DataType(DataType.Type.TEXT) String TITLE_VERSION = "title_version";
    @DataType(DataType.Type.TEXT) String LINK = "link";
    @DataType(DataType.Type.TEXT) String PREVIEW = "preview";
    @DataType(DataType.Type.TEXT) String ISRC = "isrc";
    @DataType(DataType.Type.TEXT) String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.INTEGER) String IS_READABLE = "is_readable";
    @DataType(DataType.Type.INTEGER) String DURATION = "duration"; // in seconds
    @DataType(DataType.Type.INTEGER) String TRACK_POSITION = "track_position";
    @DataType(DataType.Type.INTEGER) String DISK_NUMBER = "disk_number";
    @DataType(DataType.Type.INTEGER) String HAS_EXPLICIT_LYRICS = "explicit_lyrics";
    @DataType(DataType.Type.INTEGER)
    String POSITION = "position"; // in chart
    @DataType(DataType.Type.INTEGER)
    @References(table = Database.Tables.ARTISTS, column = ArtistColumns.ID) String ARTIST_ID = "artist_id";
    @DataType(DataType.Type.INTEGER)
    @References(table = Database.Tables.ALBUMS, column = AlbumColumns.ID) String ALBUM_ID = "album_id";
}
