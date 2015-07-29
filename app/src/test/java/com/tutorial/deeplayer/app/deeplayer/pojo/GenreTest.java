package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, emulateSdk = 21)
public class GenreTest {
    private static final String DATA_EXAMPLE = "{\n" +
            "      \"id\": \"0\",\n" +
            "      \"name\": \"All\",\n" +
            "      \"picture\": \"https://api.deezer.com/genre/0/image\",\n" +
            "      \"picture_small\": \"http://cdn-images.deezer.com/images/misc//56x56-000000-80-0-0.jpg\",\n" +
            "      \"picture_medium\": \"http://cdn-images.deezer.com/images/misc//250x250-000000-80-0-0.jpg\",\n" +
            "      \"picture_big\": \"http://cdn-images.deezer.com/images/misc//500x500-000000-80-0-0.jpg\",\n" +
            "      \"type\": \"genre\"\n" +
            "    }";

    @Test
    public void testGenreParsing() {
        Gson gson = new Gson();
        Genre genre = gson.fromJson(DATA_EXAMPLE, Genre.class);
        Assert.assertNotNull(genre);
        Assert.assertEquals(genre.getId(), Long.valueOf(0));
        Assert.assertEquals(genre.getName(), "All");
        Assert.assertEquals(genre.getType(), "genre");
    }
}
