package com.tutorial.deeplayer.app.deeplayer.data.tables;

import net.simonvt.schematic.annotation.DataType;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
public interface GenreColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT) String NAME = "name";
}
