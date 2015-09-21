package com.tutorial.deeplayer.app.deeplayer.data.tables;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
public interface BaseItemWithPictureColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey(onConflict = ConflictResolutionType.REPLACE) String ID = "_id";
    @DataType(DataType.Type.TEXT) String TYPE = "type";
    @DataType(DataType.Type.TEXT) String SHARE = "share";

    @DataType(DataType.Type.INTEGER) String IS_FAVOURITE = "is_favourite";
    @DataType(DataType.Type.INTEGER) String IS_RECOMMENDED = "is_recommended";
    @DataType(DataType.Type.INTEGER)
    String POSITION = "position";

    @DataType(DataType.Type.TEXT) String PICTURE = "picture";
    @DataType(DataType.Type.TEXT) String PICTURE_SMALL = "picture_small";
    @DataType(DataType.Type.TEXT) String PICTURE_MEDIUM = "picture_medium";
    @DataType(DataType.Type.TEXT) String PICTURE_BIG = "picture_big";
}
