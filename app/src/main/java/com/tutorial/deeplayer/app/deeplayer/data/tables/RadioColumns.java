package com.tutorial.deeplayer.app.deeplayer.data.tables;

import net.simonvt.schematic.annotation.DataType;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
public interface RadioColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT) String TITLE = "title";
    @DataType(DataType.Type.TEXT) String DESCRIPTION = "description";
    @DataType(DataType.Type.INTEGER) String TIME_ADD = "time_added";
    @DataType(DataType.Type.TEXT) String TRACK_LIST = "track_list";
}
