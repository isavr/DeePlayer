package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, emulateSdk = 21)
public class RadioTest extends TestCase {
    private static final String DATA_EXAMPLE = "{\n" +
            "      \"id\": \"37675\",\n" +
            "      \"title\": \"Coffee lounge\",\n" +
            "      \"picture\": \"https://api.deezer.com/radio/37675/image\",\n" +
            "      \"picture_small\": \"http://cdn-images.deezer.com/images/misc/4935322ecb1371cd1481d15790ad8e63/56x56-000000-80-0-0.jpg\",\n" +
            "      \"picture_medium\": \"http://cdn-images.deezer.com/images/misc/4935322ecb1371cd1481d15790ad8e63/250x250-000000-80-0-0.jpg\",\n" +
            "      \"picture_big\": \"http://cdn-images.deezer.com/images/misc/4935322ecb1371cd1481d15790ad8e63/500x500-000000-80-0-0.jpg\",\n" +
            "      \"tracklist\": \"https://api.deezer.com/radio/37675/tracks\",\n" +
            "      \"type\": \"radio\"\n" +
            "    }";

    @Test
    public void testRadioParsing() {
        Gson gson = new Gson();
        Radio artist = gson.fromJson(DATA_EXAMPLE, Radio.class);
        assertNotNull(artist);
        assertEquals(artist.getId(), Long.valueOf(37675));
        assertEquals(artist.getTitle(), "Coffee lounge");
        assertEquals(artist.getTracklist(), "https://api.deezer.com/radio/37675/tracks");
        assertEquals(artist.getType(), "radio");
    }
}
