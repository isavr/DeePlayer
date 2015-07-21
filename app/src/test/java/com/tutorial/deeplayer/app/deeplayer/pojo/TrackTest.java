package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.Gson;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */

//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, emulateSdk = 21)
public class TrackTest extends TestCase {
    private static final String DATA_EXAMPLE = "{\n" +
            "  \"id\": \"3135556\",\n" +
            "  \"readable\": true,\n" +
            "  \"title\": \"Harder Better Faster Stronger\",\n" +
            "  \"title_short\": \"Harder Better Faster Stronger\",\n" +
            "  \"title_version\": \"\",\n" +
            "  \"isrc\": \"GBDUW0000059\",\n" +
            "  \"link\": \"http://www.deezer.com/track/3135556\",\n" +
            "  \"share\": \"http://www.deezer.com/track/3135556?utm_source=deezer&utm_content=track-3135556&utm_term=700316071_1437394280&utm_medium=web\",\n" +
            "  \"duration\": \"226\",\n" +
            "  \"track_position\": 4,\n" +
            "  \"disk_number\": 1,\n" +
            "  \"rank\": \"934427\",\n" +
            "  \"release_date\": \"2001-03-07\",\n" +
            "  \"explicit_lyrics\": false,\n" +
            "  \"preview\": \"http://cdn-preview-5.deezer.com/stream/51afcde9f56a132096c0496cc95eb24b-4.mp3\",\n" +
            "  \"bpm\": 123,\n" +
            "  \"gain\": -6.48,\n" +
            "  \"available_countries\": [\n" +
            "    \"AD\",\n" +
            "    \"AE\",\n" +
            "    \"AF\",\n" +
            "    \"AG\",\n" +
            "    \"AI\",\n" +
            "    \"AL\",\n" +
            "    \"AM\",\n" +
            "    \"AO\",\n" +
            "    \"AQ\",\n" +
            "    \"AR\",\n" +
            "    \"AS\",\n" +
            "    \"AT\",\n" +
            "    \"AU\",\n" +
            "    \"AW\",\n" +
            "    \"AX\",\n" +
            "    \"AZ\",\n" +
            "    \"BA\",\n" +
            "    \"BB\",\n" +
            "    \"BD\",\n" +
            "    \"BE\",\n" +
            "    \"BF\",\n" +
            "    \"BG\",\n" +
            "    \"BH\",\n" +
            "    \"BI\",\n" +
            "    \"BJ\",\n" +
            "    \"BM\",\n" +
            "    \"BN\",\n" +
            "    \"BO\",\n" +
            "    \"BR\",\n" +
            "    \"BS\",\n" +
            "    \"BT\",\n" +
            "    \"BV\",\n" +
            "    \"BW\",\n" +
            "    \"BY\",\n" +
            "    \"BZ\",\n" +
            "    \"CA\",\n" +
            "    \"CC\",\n" +
            "    \"CD\",\n" +
            "    \"CF\",\n" +
            "    \"CG\",\n" +
            "    \"CH\",\n" +
            "    \"CI\",\n" +
            "    \"CK\",\n" +
            "    \"CL\",\n" +
            "    \"CM\",\n" +
            "    \"CN\",\n" +
            "    \"CO\",\n" +
            "    \"CR\",\n" +
            "    \"CV\",\n" +
            "    \"CX\",\n" +
            "    \"CY\",\n" +
            "    \"CZ\",\n" +
            "    \"DE\",\n" +
            "    \"DJ\",\n" +
            "    \"DK\",\n" +
            "    \"DM\",\n" +
            "    \"DO\",\n" +
            "    \"DZ\",\n" +
            "    \"EC\",\n" +
            "    \"EE\",\n" +
            "    \"EG\",\n" +
            "    \"EH\",\n" +
            "    \"ER\",\n" +
            "    \"ES\",\n" +
            "    \"ET\",\n" +
            "    \"FI\",\n" +
            "    \"FJ\",\n" +
            "    \"FK\",\n" +
            "    \"FM\",\n" +
            "    \"FO\",\n" +
            "    \"FR\",\n" +
            "    \"GA\",\n" +
            "    \"GB\",\n" +
            "    \"GD\",\n" +
            "    \"GE\",\n" +
            "    \"GF\",\n" +
            "    \"GG\",\n" +
            "    \"GH\",\n" +
            "    \"GI\",\n" +
            "    \"GL\",\n" +
            "    \"GM\",\n" +
            "    \"GN\",\n" +
            "    \"GP\",\n" +
            "    \"GQ\",\n" +
            "    \"GR\",\n" +
            "    \"GS\",\n" +
            "    \"GT\",\n" +
            "    \"GU\",\n" +
            "    \"GW\",\n" +
            "    \"GY\",\n" +
            "    \"HK\",\n" +
            "    \"HM\",\n" +
            "    \"HN\",\n" +
            "    \"HR\",\n" +
            "    \"HT\",\n" +
            "    \"HU\",\n" +
            "    \"ID\",\n" +
            "    \"IE\",\n" +
            "    \"IL\",\n" +
            "    \"IM\",\n" +
            "    \"IN\",\n" +
            "    \"IO\",\n" +
            "    \"IQ\",\n" +
            "    \"IS\",\n" +
            "    \"IT\",\n" +
            "    \"JE\",\n" +
            "    \"JM\",\n" +
            "    \"JO\",\n" +
            "    \"KE\",\n" +
            "    \"KG\",\n" +
            "    \"KH\",\n" +
            "    \"KI\",\n" +
            "    \"KM\",\n" +
            "    \"KN\",\n" +
            "    \"KR\",\n" +
            "    \"KW\",\n" +
            "    \"KY\",\n" +
            "    \"KZ\",\n" +
            "    \"LA\",\n" +
            "    \"LB\",\n" +
            "    \"LC\",\n" +
            "    \"LI\",\n" +
            "    \"LK\",\n" +
            "    \"LR\",\n" +
            "    \"LS\",\n" +
            "    \"LT\",\n" +
            "    \"LU\",\n" +
            "    \"LV\",\n" +
            "    \"LY\",\n" +
            "    \"MA\",\n" +
            "    \"MC\",\n" +
            "    \"MD\",\n" +
            "    \"ME\",\n" +
            "    \"MG\",\n" +
            "    \"MH\",\n" +
            "    \"MK\",\n" +
            "    \"ML\",\n" +
            "    \"MM\",\n" +
            "    \"MN\",\n" +
            "    \"MO\",\n" +
            "    \"MP\",\n" +
            "    \"MQ\",\n" +
            "    \"MR\",\n" +
            "    \"MS\",\n" +
            "    \"MT\",\n" +
            "    \"MU\",\n" +
            "    \"MV\",\n" +
            "    \"MW\",\n" +
            "    \"MX\",\n" +
            "    \"MY\",\n" +
            "    \"MZ\",\n" +
            "    \"NA\",\n" +
            "    \"NC\",\n" +
            "    \"NE\",\n" +
            "    \"NF\",\n" +
            "    \"NG\",\n" +
            "    \"NI\",\n" +
            "    \"NL\",\n" +
            "    \"NO\",\n" +
            "    \"NP\",\n" +
            "    \"NR\",\n" +
            "    \"NU\",\n" +
            "    \"NZ\",\n" +
            "    \"OM\",\n" +
            "    \"PA\",\n" +
            "    \"PE\",\n" +
            "    \"PF\",\n" +
            "    \"PG\",\n" +
            "    \"PH\",\n" +
            "    \"PK\",\n" +
            "    \"PL\",\n" +
            "    \"PM\",\n" +
            "    \"PN\",\n" +
            "    \"PR\",\n" +
            "    \"PS\",\n" +
            "    \"PT\",\n" +
            "    \"PW\",\n" +
            "    \"PY\",\n" +
            "    \"QA\",\n" +
            "    \"RE\",\n" +
            "    \"RO\",\n" +
            "    \"RS\",\n" +
            "    \"RU\",\n" +
            "    \"RW\",\n" +
            "    \"SA\",\n" +
            "    \"SB\",\n" +
            "    \"SC\",\n" +
            "    \"SD\",\n" +
            "    \"SE\",\n" +
            "    \"SG\",\n" +
            "    \"SH\",\n" +
            "    \"SI\",\n" +
            "    \"SJ\",\n" +
            "    \"SK\",\n" +
            "    \"SL\",\n" +
            "    \"SM\",\n" +
            "    \"SN\",\n" +
            "    \"SO\",\n" +
            "    \"SR\",\n" +
            "    \"ST\",\n" +
            "    \"SV\",\n" +
            "    \"SY\",\n" +
            "    \"SZ\",\n" +
            "    \"TC\",\n" +
            "    \"TD\",\n" +
            "    \"TF\",\n" +
            "    \"TG\",\n" +
            "    \"TH\",\n" +
            "    \"TJ\",\n" +
            "    \"TK\",\n" +
            "    \"TL\",\n" +
            "    \"TM\",\n" +
            "    \"TN\",\n" +
            "    \"TO\",\n" +
            "    \"TR\",\n" +
            "    \"TT\",\n" +
            "    \"TV\",\n" +
            "    \"TW\",\n" +
            "    \"TZ\",\n" +
            "    \"UA\",\n" +
            "    \"UG\",\n" +
            "    \"US\",\n" +
            "    \"UY\",\n" +
            "    \"UZ\",\n" +
            "    \"VA\",\n" +
            "    \"VC\",\n" +
            "    \"VE\",\n" +
            "    \"VG\",\n" +
            "    \"VI\",\n" +
            "    \"VN\",\n" +
            "    \"VU\",\n" +
            "    \"WF\",\n" +
            "    \"WS\",\n" +
            "    \"YE\",\n" +
            "    \"YT\",\n" +
            "    \"ZA\",\n" +
            "    \"ZM\",\n" +
            "    \"ZW\"\n" +
            "  ],\n" +
            "  \"contributors\": [\n" +
            "    {\n" +
            "      \"id\": 27,\n" +
            "      \"name\": \"Daft Punk\",\n" +
            "      \"link\": \"http://www.deezer.com/artist/27\",\n" +
            "      \"share\": \"http://www.deezer.com/artist/27?utm_source=deezer&utm_content=artist-27&utm_term=700316071_1437394280&utm_medium=web\",\n" +
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
            "    \"link\": \"http://www.deezer.com/artist/27\",\n" +
            "    \"share\": \"http://www.deezer.com/artist/27?utm_source=deezer&utm_content=artist-27&utm_term=700316071_1437394280&utm_medium=web\",\n" +
            "    \"picture\": \"https://api.deezer.com/artist/27/image\",\n" +
            "    \"picture_small\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/56x56-000000-80-0-0.jpg\",\n" +
            "    \"picture_medium\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/250x250-000000-80-0-0.jpg\",\n" +
            "    \"picture_big\": \"http://cdn-images.deezer.com/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg\",\n" +
            "    \"radio\": true,\n" +
            "    \"tracklist\": \"https://api.deezer.com/artist/27/top?limit=50\",\n" +
            "    \"type\": \"artist\"\n" +
            "  },\n" +
            "  \"album\": {\n" +
            "    \"id\": \"302127\",\n" +
            "    \"title\": \"Discovery\",\n" +
            "    \"link\": \"http://www.deezer.com/album/302127\",\n" +
            "    \"cover\": \"https://api.deezer.com/album/302127/image\",\n" +
            "    \"cover_small\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/56x56-000000-80-0-0.jpg\",\n" +
            "    \"cover_medium\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/250x250-000000-80-0-0.jpg\",\n" +
            "    \"cover_big\": \"http://cdn-images.deezer.com/images/cover/2e018122cb56986277102d2041a592c8/500x500-000000-80-0-0.jpg\",\n" +
            "    \"release_date\": \"2001-03-07\",\n" +
            "    \"tracklist\": \"https://api.deezer.com/album/302127/tracks\",\n" +
            "    \"type\": \"album\"\n" +
            "  },\n" +
            "  \"type\": \"track\"\n" +
            "}";

    @Test
    public void testTrackParsing() {
        Gson gson = new Gson();
        Track track = gson.fromJson(DATA_EXAMPLE, Track.class);
        assertNotNull(track);
        assertEquals(track.getId(), 3135556);
        assertEquals(track.getTitle(), "Harder Better Faster Stronger");
        assertNotNull(track.getAlbum());
        assertEquals(track.getAlbum().getId(), 302127);
        assertEquals(track.getAlbum().getTitle(), "Discovery");
        assertNotNull(track.getContributors());
        assertEquals(track.getContributors().size(), 1);
        assertEquals(track.getType(), "track");
    }
}
