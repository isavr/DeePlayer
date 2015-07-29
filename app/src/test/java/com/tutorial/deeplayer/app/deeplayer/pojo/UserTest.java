package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */

//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, emulateSdk = 21)
public class UserTest {
    private static final String DATA_EXAMPLE = "{\n" +
            "  \"id\": \"701236078\",\n" +
            "  \"name\": \"Test user\",\n" +
            "  \"lastname\": \"\",\n" +
            "  \"firstname\": \"\",\n" +
            "  \"birthday\": \"1991-01-01\",\n" +
            "  \"inscription_date\": \"2015-05-04\",\n" +
            "  \"gender\": \"M\",\n" +
            "  \"link\": \"http://www.deezer.com/profile/700316071\",\n" +
            "  \"picture\": \"https://api.deezer.com/user/700316071/image\",\n" +
            "  \"picture_small\": \"http://cdn-images.deezer.com/images/user//56x56-000000-80-0-0.jpg\",\n" +
            "  \"picture_medium\": \"http://cdn-images.deezer.com/images/user//250x250-000000-80-0-0.jpg\",\n" +
            "  \"picture_big\": \"http://cdn-images.deezer.com/images/user//500x500-000000-80-0-0.jpg\",\n" +
            "  \"country\": \"BY\",\n" +
            "  \"lang\": \"EN\",\n" +
            "  \"tracklist\": \"https://api.deezer.com/user/700316071/flow\",\n" +
            "  \"type\": \"user\",\n" +
            "  \"status\": 0\n" +
            "}";

    @Test
    public void testUserParsing() {
        Gson gson = new Gson();
        User user = gson.fromJson(DATA_EXAMPLE, User.class);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId(), Long.valueOf(701236078));
        Assert.assertEquals(user.getName(), "Test user");
        Assert.assertEquals(user.getTracklist(), "https://api.deezer.com/user/700316071/flow");
        Assert.assertEquals(user.getBirthday(), "1991-01-01");
        Assert.assertEquals(user.getType(), "user");
    }
}
