package com.tutorial.deeplayer.app.deeplayer.rest.data;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class ArtistTest extends AndroidTestCase {
    private static final String DATA_EXAMPLE = "{\n" +
            "  \"id\": \"27\",\n" +
            "  \"name\": \"Daft Punk\",\n" +
            "  \"link\": \"http://www.deezer.com/artist/27\",\n" +
            "  \"share\": \"http://www.deezer.com/artist/27?utm_source=deezer&utm_content=artist-27&utm_term=700316071_1437146192&utm_medium=web\",\n" +
            "  \"picture\": \"https://api.deezer.com/artist/27/image\",\n" +
            "  \"picture_small\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/56x56-000000-80-0-0.jpg\",\n" +
            "  \"picture_medium\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/250x250-000000-80-0-0.jpg\",\n" +
            "  \"picture_big\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg\",\n" +
            "  \"nb_album\": 26,\n" +
            "  \"nb_fan\": 2154117,\n" +
            "  \"radio\": true,\n" +
            "  \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "  \"type\": \"artist\"\n" +
            "}";

    public void testArtistParsing() {
        Gson gson = new Gson();
        Artist artist = gson.fromJson(DATA_EXAMPLE, Artist.class);
        assertNotNull(artist);
        assertEquals(artist.getId(), 27);
        assertEquals(artist.getName(), "Daft Punk");
        assertEquals(artist.getTracklist(), "https://api.deezer.com/artist/27/top?limit=50");
    }
}