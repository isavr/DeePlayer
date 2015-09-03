package com.tutorial.deeplayer.app.deeplayer.data.tables;

import net.simonvt.schematic.annotation.DataType;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
public interface UserColumns extends BaseItemWithPictureColumns {
    @DataType(DataType.Type.TEXT) String NAME = "name";
    @DataType(DataType.Type.TEXT) String LAST_NAME = "last_name";
    @DataType(DataType.Type.TEXT) String FIRST_NAME = "first_name";
    @DataType(DataType.Type.TEXT) String BIRTHDAY = "birthday";
    @DataType(DataType.Type.TEXT) String INSCRIPTION_DATE = "inscription_date";
    @DataType(DataType.Type.TEXT) String GENDER = "gender";
    @DataType(DataType.Type.TEXT) String COUNTRY = "country";
    @DataType(DataType.Type.TEXT) String LANG = "lang";
    @DataType(DataType.Type.INTEGER) String STATUS = "status";
    @DataType(DataType.Type.TEXT) String LINK = "link";
    @DataType(DataType.Type.TEXT) String TRACKLIST = "tracklist";
}
