package com.tutorial.deeplayer.app.deeplayer.data.tables;

import com.tutorial.deeplayer.app.deeplayer.data.Database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.References;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public interface PlaylistColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT)
    String TITLE = "title";
    @DataType(DataType.Type.TEXT)
    String LINK = "link";
    @DataType(DataType.Type.INTEGER)
    String DURATION = "duration";
    @DataType(DataType.Type.INTEGER)
    String IS_PUBLIC = "is_public";
    @DataType(DataType.Type.INTEGER)
    String LOVED_TRACK = "is_loved_track";
    @DataType(DataType.Type.INTEGER)
    String IS_COLLABORATIVE = "collaborative";
    @DataType(DataType.Type.INTEGER)
    String TRACKS_NUMBER = "nb_tracks";
    @DataType(DataType.Type.TEXT)
    String TRACKLIST = "tracklist";
    @DataType(DataType.Type.INTEGER)
    @References(table = Database.Tables.USER, column = UserColumns.ID)
    String USER_ID = "user_id";
}
