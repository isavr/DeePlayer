package com.tutorial.deeplayer.app.deeplayer.data.tables;

import net.simonvt.schematic.annotation.DataType;

/**
 * Created by ilya.savritsky on 31.08.2015.
 */
public interface ArtistColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT) String NAME = "name";
    @DataType(DataType.Type.TEXT) String LINK = "link";
    @DataType(DataType.Type.INTEGER) String ALBUM_COUNT = "album_count";
    @DataType(DataType.Type.INTEGER) String FANS_COUNT = "fans_count";
    @DataType(DataType.Type.INTEGER) String HAS_RADIO = "has_radio";
    @DataType(DataType.Type.TEXT) String TRACKLIST = "tracklist";
}
