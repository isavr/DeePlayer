package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public class AlbumTest extends TestCase {
    private static final String DATA_EXAMPLE = "{\n" +
            "  \"id\": \"302127\",\n" +
            "  \"title\": \"Discovery\",\n" +
            "  \"upc\": \"724384960650\",\n" +
            "  \"link\": \"http://www.deezer.com/album/302127\",\n" +
            "  \"share\": \"http://www.deezer.com/album/302127?utm_source=deezer&utm_content=album-302127&utm_term=700316071_1437395878&utm_medium=web\",\n" +
            "  \"cover\": \"https://api.deezer.com/album/302127/image\",\n" +
            "  \"cover_small\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/56x56-000000-80-0-0.jpg\",\n" +
            "  \"cover_medium\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/250x250-000000-80-0-0.jpg\",\n" +
            "  \"cover_big\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/500x500-000000-80-0-0.jpg\",\n" +
            "  \"genre_id\": 113,\n" +
            "  \"genres\": {\n" +
            "    \"data\": [\n" +
            "      {\n" +
            "        \"id\": 113,\n" +
            "        \"name\": \"Dance\",\n" +
            "        \"picture\": \"https://api.deezer.com/genre/113/image\",\n" +
            "        \"type\": \"genre\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"label\": \"Parlophone France\",\n" +
            "  \"nb_tracks\": 14,\n" +
            "  \"duration\": 3663,\n" +
            "  \"fans\": 129653,\n" +
            "  \"rating\": 0,\n" +
            "  \"release_date\": \"2001-03-07\",\n" +
            "  \"record_type\": \"album\",\n" +
            "  \"available\": true,\n" +
            "  \"tracklist\": \"https://api.deezer.com/album/302127/tracks\",\n" +
            "  \"explicit_lyrics\": false,\n" +
            "  \"contributors\": [\n" +
            "    {\n" +
            "      \"id\": 27,\n" +
            "      \"name\": \"Daft Punk\",\n" +
            "      \"link\": \"http://www.deezer.com/artist/27\",\n" +
            "      \"share\": \"http://www.deezer.com/artist/27?utm_source=deezer&utm_content=artist-27&utm_term=700316071_1437395878&utm_medium=web\",\n" +
            "      \"picture\": \"https://api.deezer.com/artist/27/image\",\n" +
            "      \"picture_small\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/56x56-000000-80-0-0.jpg\",\n" +
            "      \"picture_medium\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/250x250-000000-80-0-0.jpg\",\n" +
            "      \"picture_big\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg\",\n" +
            "      \"radio\": true,\n" +
            "      \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "      \"type\": \"artist\",\n" +
            "      \"role\": \"Main\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"artist\": {\n" +
            "    \"id\": \"27\",\n" +
            "    \"name\": \"Daft Punk\",\n" +
            "    \"picture\": \"https://api.deezer.com/artist/27/image\",\n" +
            "    \"picture_small\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/56x56-000000-80-0-0.jpg\",\n" +
            "    \"picture_medium\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/250x250-000000-80-0-0.jpg\",\n" +
            "    \"picture_big\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg\",\n" +
            "    \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "    \"type\": \"artist\"\n" +
            "  },\n" +
            "  \"type\": \"album\",\n" +
            "  \"tracks\": {\n" +
            "    \"data\": [\n" +
            "      {\n" +
            "        \"id\": \"3135553\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"One More Time\",\n" +
            "        \"title_short\": \"One More Time\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135553\",\n" +
            "        \"duration\": \"320\",\n" +
            "        \"rank\": \"972146\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-4.deezer.com/stream/43808a3ac856cc117362ab94718603ba-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135554\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Aerodynamic\",\n" +
            "        \"title_short\": \"Aerodynamic\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135554\",\n" +
            "        \"duration\": \"213\",\n" +
            "        \"rank\": \"925086\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-f.deezer.com/stream/f3e8e90f2b7e02cdb399a4a08bd9dfcf-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135555\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Digital Love\",\n" +
            "        \"title_short\": \"Digital Love\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135555\",\n" +
            "        \"duration\": \"301\",\n" +
            "        \"rank\": \"917409\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-0.deezer.com/stream/0a2f1d19dcf9134803c4b94fbc35500a-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135556\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Harder Better Faster Stronger\",\n" +
            "        \"title_short\": \"Harder Better Faster Stronger\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135556\",\n" +
            "        \"duration\": \"226\",\n" +
            "        \"rank\": \"934427\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-5.deezer.com/stream/51afcde9f56a132096c0496cc95eb24b-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135557\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Crescendolls\",\n" +
            "        \"title_short\": \"Crescendolls\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135557\",\n" +
            "        \"duration\": \"211\",\n" +
            "        \"rank\": \"842178\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-9.deezer.com/stream/905a3fe7c2a51c18be8700a602985228-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135558\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Nightvision\",\n" +
            "        \"title_short\": \"Nightvision\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135558\",\n" +
            "        \"duration\": \"104\",\n" +
            "        \"rank\": \"815886\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-e.deezer.com/stream/ebb50b959fae952c40452c2cdb0e4f34-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135559\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Superhereos\",\n" +
            "        \"title_short\": \"Superhereos\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135559\",\n" +
            "        \"duration\": \"237\",\n" +
            "        \"rank\": \"835638\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-5.deezer.com/stream/5f1b7e28a4ba1ffe7d128fb6d49d6d34-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135560\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"High Life\",\n" +
            "        \"title_short\": \"High Life\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135560\",\n" +
            "        \"duration\": \"201\",\n" +
            "        \"rank\": \"818647\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-0.deezer.com/stream/0cf156298e20cfca1dd3972c95210877-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135561\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Something About Us\",\n" +
            "        \"title_short\": \"Something About Us\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135561\",\n" +
            "        \"duration\": \"232\",\n" +
            "        \"rank\": \"920403\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-2.deezer.com/stream/2e92d03c585229e663a02e8015264fa3-3.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135562\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Voyager\",\n" +
            "        \"title_short\": \"Voyager\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135562\",\n" +
            "        \"duration\": \"227\",\n" +
            "        \"rank\": \"863130\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-1.deezer.com/stream/1185a3bf419cca5b50e6188e76771313-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135563\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Veridis Quo\",\n" +
            "        \"title_short\": \"Veridis Quo\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135563\",\n" +
            "        \"duration\": \"345\",\n" +
            "        \"rank\": \"878173\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-1.deezer.com/stream/13bbc2dc1ed9a93cc752f7307769106a-3.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135564\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Short Circuit\",\n" +
            "        \"title_short\": \"Short Circuit\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135564\",\n" +
            "        \"duration\": \"206\",\n" +
            "        \"rank\": \"775613\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-9.deezer.com/stream/965e18f81a7cbbebbcf2c9e197b0952e-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135565\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Face To Face\",\n" +
            "        \"title_short\": \"Face To Face\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135565\",\n" +
            "        \"duration\": \"240\",\n" +
            "        \"rank\": \"864951\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-4.deezer.com/stream/4126c1c81293d88cedb136a2d0841618-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3135566\",\n" +
            "        \"readable\": true,\n" +
            "        \"title\": \"Too Long\",\n" +
            "        \"title_short\": \"Too Long\",\n" +
            "        \"title_version\": \"\",\n" +
            "        \"link\": \"http://www.deezer.com/track/3135566\",\n" +
            "        \"duration\": \"600\",\n" +
            "        \"rank\": \"830956\",\n" +
            "        \"explicit_lyrics\": false,\n" +
            "        \"preview\": \"http://cdn-preview-2.deezer.com/stream/20814c8bbbe442662b0ad5ebabb5fd23-4.mp3\",\n" +
            "        \"artist\": {\n" +
            "          \"id\": \"27\",\n" +
            "          \"name\": \"Daft Punk\",\n" +
            "          \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "          \"type\": \"artist\"\n" +
            "        },\n" +
            "        \"type\": \"track\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Test
    public void testAlbumParsing() {
        Gson gson = new Gson();
        Album album = gson.fromJson(DATA_EXAMPLE, Album.class);
        assertNotNull(album);
        assertEquals(album.getId(), 302127);
        assertEquals(album.getTitle(), "Discovery");
        assertEquals(album.getDuration(), 3663);
        assertEquals(album.getTracksCount(), 14);
        assertNotNull(album.getContributors());
        assertEquals(album.getContributors().size(), 1);
        assertEquals(album.getContributors().get(0).getId(), 27);
        assertEquals(album.getTracklist(), "https://api.deezer.com/album/302127/tracks");
        assertEquals(album.getGenreID(), 113);
        assertEquals(album.getType(), "album");
        assertNotNull(album.getTracks());
        assertEquals(album.getTracks().getData().size(), 14);

    }
}
