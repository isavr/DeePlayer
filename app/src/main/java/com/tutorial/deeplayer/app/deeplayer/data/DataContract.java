package com.tutorial.deeplayer.app.deeplayer.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.AlbumsValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.ArtistsValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.GenresValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.RadiosValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.TracksValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.generated.values.UserValuesBuilder;
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.GenreColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.RadioColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.TrackColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.UserColumns;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Genre;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya.savritsky on 21.07.2015.
 */
public class DataContract {
    private enum KeyIndex {
        AlbumIndex,
        ArtistIndex,
        TrackIndex
    }

    public static int getAlbumIndex() {
        return KeyIndex.AlbumIndex.ordinal();
    }

    public static int getArtistIndex() {
        return KeyIndex.ArtistIndex.ordinal();
    }

    public static int getTrackIndex() {
        return KeyIndex.TrackIndex.ordinal();
    }

    public static final class RadioConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(RadioColumns.ID, cursor.getColumnIndex(RadioColumns.ID));
                mappedColumns.put(RadioColumns.TYPE, cursor.getColumnIndex(RadioColumns.TYPE));
                mappedColumns.put(RadioColumns.SHARE, cursor.getColumnIndex(RadioColumns.SHARE));
                mappedColumns.put(RadioColumns.PICTURE, cursor.getColumnIndex(RadioColumns.PICTURE));
                mappedColumns.put(RadioColumns.PICTURE_SMALL, cursor.getColumnIndex(RadioColumns.PICTURE_SMALL));
                mappedColumns.put(RadioColumns.PICTURE_MEDIUM, cursor.getColumnIndex(RadioColumns.PICTURE_MEDIUM));
                mappedColumns.put(RadioColumns.PICTURE_BIG, cursor.getColumnIndex(RadioColumns.PICTURE_BIG));
                mappedColumns.put(RadioColumns.TITLE, cursor.getColumnIndex(RadioColumns.TITLE));
                mappedColumns.put(RadioColumns.DESCRIPTION, cursor.getColumnIndex(RadioColumns.DESCRIPTION));
                mappedColumns.put(RadioColumns.TRACK_LIST, cursor.getColumnIndex(RadioColumns.TRACK_LIST));
                mappedColumns.put(RadioColumns.IS_FAVOURITE, cursor.getColumnIndex(RadioColumns.IS_FAVOURITE));
                mappedColumns.put(RadioColumns.TIME_ADD, cursor.getColumnIndex(RadioColumns.TIME_ADD));
                mappedColumns.put(RadioColumns.IS_RECOMMENDED, cursor.getColumnIndex(RadioColumns.IS_RECOMMENDED));
            }
        }

        public static Radio convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(RadioColumns.ID));
            String type = cursor.getString(mappedColumns.get(RadioColumns.TYPE));
            String share = cursor.getString(mappedColumns.get(RadioColumns.SHARE));
            String picture = cursor.getString(mappedColumns.get(RadioColumns.PICTURE));
            String picture_small = cursor.getString(mappedColumns.get(RadioColumns.PICTURE_SMALL));
            String picture_medium = cursor.getString(mappedColumns.get(RadioColumns.PICTURE_MEDIUM));
            String picture_big = cursor.getString(mappedColumns.get(RadioColumns.PICTURE_BIG));
            String title = cursor.getString(mappedColumns.get(RadioColumns.TITLE));
            String description = cursor.getString(mappedColumns.get(RadioColumns.DESCRIPTION));
            String trackList = cursor.getString(mappedColumns.get(RadioColumns.TRACK_LIST));
            int favouriteVal = cursor.getInt(mappedColumns.get(RadioColumns.IS_FAVOURITE));
            int timeAdded = cursor.getInt(mappedColumns.get(RadioColumns.TIME_ADD));
            int recommendedVal = cursor.getInt(mappedColumns.get(RadioColumns.IS_RECOMMENDED));

            Radio r = new Radio();
            r.setShare(share);
            r.setPictureMedium(picture_medium);
            r.setPicture(picture);
            r.setPictureSmall(picture_small);
            r.setPictureBig(picture_big);
            r.setTracklist(trackList);
            r.setTitle(title);
            r.setDescription(description);
            r.setFavourite(favouriteVal == 1);
            r.setId(Long.valueOf(id));
            r.setType(type);
            r.setTime_add(timeAdded);
            r.setIsRecommended(recommendedVal == 1);
            return r;
        }

        public static ContentValues convertFrom(Radio radio) {
            ContentValues values = new RadiosValuesBuilder().id(radio.getId()).title(radio.getTitle())
                    .description(radio.getDescription()).isFavourite(radio.isFavourite() ? 1 : 0)
                    .picture(radio.getPicture()).pictureSmall(radio.getPictureSmall())
                    .pictureMedium(radio.getPictureMedium()).pictureBig(radio.getPictureBig())
                    .share(radio.getShare()).trackList(radio.getTracklist()).timeAdd(radio.getTime_add())
                    .type(radio.getType()).isRecommended(radio.isRecommended() ? 1 : 0).values();
            return values;
        }
    }

    public static final class ArtistConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(ArtistColumns.ID, cursor.getColumnIndex(ArtistColumns.ID));
                mappedColumns.put(ArtistColumns.TYPE, cursor.getColumnIndex(ArtistColumns.TYPE));
                mappedColumns.put(ArtistColumns.SHARE, cursor.getColumnIndex(ArtistColumns.SHARE));
                mappedColumns.put(ArtistColumns.PICTURE, cursor.getColumnIndex(ArtistColumns.PICTURE));
                mappedColumns.put(ArtistColumns.PICTURE_SMALL, cursor.getColumnIndex(ArtistColumns.PICTURE_SMALL));
                mappedColumns.put(ArtistColumns.PICTURE_MEDIUM, cursor.getColumnIndex(ArtistColumns.PICTURE_MEDIUM));
                mappedColumns.put(ArtistColumns.PICTURE_BIG, cursor.getColumnIndex(ArtistColumns.PICTURE_BIG));
                mappedColumns.put(ArtistColumns.NAME, cursor.getColumnIndex(ArtistColumns.NAME));
                mappedColumns.put(ArtistColumns.LINK, cursor.getColumnIndex(ArtistColumns.LINK));
                mappedColumns.put(ArtistColumns.TRACKLIST, cursor.getColumnIndex(ArtistColumns.TRACKLIST));
                mappedColumns.put(ArtistColumns.IS_FAVOURITE, cursor.getColumnIndex(ArtistColumns.IS_FAVOURITE));
                mappedColumns.put(ArtistColumns.FANS_COUNT, cursor.getColumnIndex(ArtistColumns.FANS_COUNT));
                mappedColumns.put(ArtistColumns.ALBUM_COUNT, cursor.getColumnIndex(ArtistColumns.ALBUM_COUNT));
                mappedColumns.put(ArtistColumns.HAS_RADIO, cursor.getColumnIndex(ArtistColumns.HAS_RADIO));
                mappedColumns.put(ArtistColumns.IS_RECOMMENDED, cursor.getColumnIndex(ArtistColumns.IS_RECOMMENDED));
            }
        }

        public static Artist convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(ArtistColumns.ID));
            String type = cursor.getString(mappedColumns.get(ArtistColumns.TYPE));
            String share = cursor.getString(mappedColumns.get(ArtistColumns.SHARE));
            String picture = cursor.getString(mappedColumns.get(ArtistColumns.PICTURE));
            String picture_small = cursor.getString(mappedColumns.get(ArtistColumns.PICTURE_SMALL));
            String picture_medium = cursor.getString(mappedColumns.get(ArtistColumns.PICTURE_MEDIUM));
            String picture_big = cursor.getString(mappedColumns.get(ArtistColumns.PICTURE_BIG));
            String name = cursor.getString(mappedColumns.get(ArtistColumns.NAME));
            String link = cursor.getString(mappedColumns.get(ArtistColumns.LINK));
            String trackList = cursor.getString(mappedColumns.get(ArtistColumns.TRACKLIST));
            int favouriteVal = cursor.getInt(mappedColumns.get(ArtistColumns.IS_FAVOURITE));
            int fansCount = cursor.getInt(mappedColumns.get(ArtistColumns.FANS_COUNT));
            int albumCount = cursor.getInt(mappedColumns.get(ArtistColumns.ALBUM_COUNT));
            int hasRadioVal = cursor.getInt(mappedColumns.get(ArtistColumns.HAS_RADIO));
            int isRecommendedVal = cursor.getInt(mappedColumns.get(ArtistColumns.IS_RECOMMENDED));

            Artist artist = new Artist();
            artist.setId(Long.valueOf(id));
            artist.setName(name);
            artist.setShare(share);
            artist.setPictureMedium(picture_medium);
            artist.setPicture(picture);
            artist.setPictureSmall(picture_small);
            artist.setPictureBig(picture_big);
            artist.setType(type);
            artist.setFavourite(favouriteVal == 1);
            artist.setFansCount(fansCount);
            artist.setAlbumCount(albumCount);
            artist.setHasRadio(hasRadioVal == 1);
            artist.setTracklist(trackList);
            artist.setLink(link);
            artist.setIsRecommended(isRecommendedVal == 1);
            return artist;
        }

        public static ContentValues convertFrom(Artist artist) {
            ContentValues values = new ArtistsValuesBuilder().id(artist.getId()).name(artist.getName())
                    .share(artist.getShare()).isFavourite(artist.isFavourite() ? 1 : 0)
                    .picture(artist.getPicture()).pictureSmall(artist.getPictureSmall())
                    .pictureMedium(artist.getPictureMedium()).pictureBig(artist.getPictureBig())
                    .fansCount(artist.getFansCount()).albumCount(artist.getAlbumCount())
                    .hasRadio(artist.isHasRadio() ? 1 : 0).tracklist(artist.getTracklist()).link(artist.getLink())
                    .isRecommended(artist.isRecommended() ? 1 : 0).type(artist.getType()).values();
            return values;
        }
    }

    public static final class UserConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(UserColumns.ID, cursor.getColumnIndex(UserColumns.ID));
                mappedColumns.put(UserColumns.TYPE, cursor.getColumnIndex(UserColumns.TYPE));
                mappedColumns.put(UserColumns.PICTURE, cursor.getColumnIndex(UserColumns.PICTURE));
                mappedColumns.put(UserColumns.PICTURE_SMALL, cursor.getColumnIndex(UserColumns.PICTURE_SMALL));
                mappedColumns.put(UserColumns.PICTURE_MEDIUM, cursor.getColumnIndex(UserColumns.PICTURE_MEDIUM));
                mappedColumns.put(UserColumns.PICTURE_BIG, cursor.getColumnIndex(UserColumns.PICTURE_BIG));
                mappedColumns.put(UserColumns.NAME, cursor.getColumnIndex(UserColumns.NAME));
                mappedColumns.put(UserColumns.FIRST_NAME, cursor.getColumnIndex(UserColumns.FIRST_NAME));
                mappedColumns.put(UserColumns.LAST_NAME, cursor.getColumnIndex(UserColumns.LAST_NAME));
                mappedColumns.put(UserColumns.TRACKLIST, cursor.getColumnIndex(UserColumns.TRACKLIST));
                mappedColumns.put(UserColumns.LINK, cursor.getColumnIndex(ArtistColumns.LINK));
                mappedColumns.put(UserColumns.BIRTHDAY, cursor.getColumnIndex(UserColumns.BIRTHDAY));
                mappedColumns.put(UserColumns.IS_FAVOURITE, cursor.getColumnIndex(UserColumns.IS_FAVOURITE));
                mappedColumns.put(UserColumns.GENDER, cursor.getColumnIndex(UserColumns.GENDER));
                mappedColumns.put(UserColumns.COUNTRY, cursor.getColumnIndex(UserColumns.COUNTRY));
                mappedColumns.put(UserColumns.STATUS, cursor.getColumnIndex(UserColumns.STATUS));
                mappedColumns.put(UserColumns.INSCRIPTION_DATE, cursor.getColumnIndex(UserColumns.INSCRIPTION_DATE));
            }
        }

        public static User convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(UserColumns.ID));
            String type = cursor.getString(mappedColumns.get(UserColumns.TYPE));
            String picture = cursor.getString(mappedColumns.get(UserColumns.PICTURE));
            String picture_small = cursor.getString(mappedColumns.get(UserColumns.PICTURE_SMALL));
            String picture_medium = cursor.getString(mappedColumns.get(UserColumns.PICTURE_MEDIUM));
            String picture_big = cursor.getString(mappedColumns.get(UserColumns.PICTURE_BIG));
            String name = cursor.getString(mappedColumns.get(UserColumns.NAME));
            String firstName = cursor.getString(mappedColumns.get(UserColumns.FIRST_NAME));
            String lastName = cursor.getString(mappedColumns.get(UserColumns.LAST_NAME));
            String birthday = cursor.getString(mappedColumns.get(UserColumns.BIRTHDAY));
            int favouriteVal = cursor.getInt(mappedColumns.get(UserColumns.IS_FAVOURITE));
            String gender = cursor.getString(mappedColumns.get(UserColumns.GENDER));
            String country = cursor.getString(mappedColumns.get(UserColumns.COUNTRY));
            int status = cursor.getInt(mappedColumns.get(UserColumns.STATUS));
            String inscriptionDate = cursor.getString(mappedColumns.get(UserColumns.INSCRIPTION_DATE));
            String link = cursor.getString(mappedColumns.get(UserColumns.LINK));
            String trackList = cursor.getString(mappedColumns.get(UserColumns.TRACKLIST));

            User user = new User();
            user.setId(Long.valueOf(id));
            user.setPictureMedium(picture_medium);
            user.setPicture(picture);
            user.setPictureSmall(picture_small);
            user.setPictureBig(picture_big);
            user.setTracklist(trackList);
            user.setName(name);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setFavourite(favouriteVal == 1);
            user.setGender(gender);
            user.setBirthday(birthday);
            user.setLink(link);
            user.setStatus(status);
            user.setCountry(country);
            user.setInscriptionDate(inscriptionDate);
            user.setType(type);
            return user;
        }

        public static ContentValues convertFrom(User user) {
            ContentValues values = new UserValuesBuilder().id(user.getId()).picture(user.getPicture())
                    .pictureSmall(user.getPictureSmall()).pictureMedium(user.getPictureMedium())
                    .pictureBig(user.getPictureBig()).tracklist(user.getTracklist()).name(user.getName())
                    .firstName(user.getFirstName()).lastName(user.getLastName()).isFavourite(user.isFavourite() ? 1 : 0)
                    .gender(user.getGender()).birthday(user.getBirthday()).link(user.getLink()).status(user.getStatus())
                    .country(user.getCountry()).inscriptionDate(user.getInscriptionDate()).lang(user.getLang())
                    .type(user.getType()).values();
            return values;
        }

    }

    public static final class GenreConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(GenreColumns.ID, cursor.getColumnIndex(GenreColumns.ID));
                mappedColumns.put(GenreColumns.NAME, cursor.getColumnIndex(GenreColumns.NAME));
                mappedColumns.put(GenreColumns.TYPE, cursor.getColumnIndex(GenreColumns.TYPE));
                mappedColumns.put(GenreColumns.PICTURE, cursor.getColumnIndex(GenreColumns.PICTURE));
                mappedColumns.put(GenreColumns.PICTURE_SMALL, cursor.getColumnIndex(GenreColumns.PICTURE_SMALL));
                mappedColumns.put(GenreColumns.PICTURE_MEDIUM, cursor.getColumnIndex(GenreColumns.PICTURE_MEDIUM));
                mappedColumns.put(GenreColumns.PICTURE_BIG, cursor.getColumnIndex(GenreColumns.PICTURE_BIG));
                mappedColumns.put(GenreColumns.IS_FAVOURITE, cursor.getColumnIndex(GenreColumns.IS_FAVOURITE));
                mappedColumns.put(GenreColumns.IS_RECOMMENDED, cursor.getColumnIndex(GenreColumns.IS_RECOMMENDED));
            }
        }

        public static Genre convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(GenreColumns.ID));
            String name = cursor.getString(mappedColumns.get(GenreColumns.NAME));
            String type = cursor.getString(mappedColumns.get(GenreColumns.TYPE));
            String picture = cursor.getString(mappedColumns.get(GenreColumns.PICTURE));
            String picture_small = cursor.getString(mappedColumns.get(GenreColumns.PICTURE_SMALL));
            String picture_medium = cursor.getString(mappedColumns.get(GenreColumns.PICTURE_MEDIUM));
            String picture_big = cursor.getString(mappedColumns.get(GenreColumns.PICTURE_BIG));
            int favouriteVal = cursor.getInt(mappedColumns.get(GenreColumns.IS_FAVOURITE));
            int recommendedVal = cursor.getInt(mappedColumns.get(GenreColumns.IS_RECOMMENDED));

            Genre r = new Genre();
            r.setId(Long.valueOf(id));
            r.setName(name);
            r.setPictureMedium(picture_medium);
            r.setPicture(picture);
            r.setPictureSmall(picture_small);
            r.setPictureBig(picture_big);
            r.setFavourite(favouriteVal == 1);
            r.setType(type);
            r.setIsRecommended(recommendedVal == 1);
            return r;
        }

        public static ContentValues convertFrom(Genre genre) {
            ContentValues values = new GenresValuesBuilder().id(genre.getId()).name(genre.getName())
                    .isFavourite(genre.isFavourite() ? 1 : 0)
                    .picture(genre.getPicture()).pictureSmall(genre.getPictureSmall())
                    .pictureMedium(genre.getPictureMedium()).pictureBig(genre.getPictureBig())
                    .type(genre.getType()).isRecommended(genre.isRecommended() ? 1 : 0).values();
            return values;
        }
    }

    public static final class AlbumConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(AlbumColumns.ID, cursor.getColumnIndex(AlbumColumns.ID));
                mappedColumns.put(AlbumColumns.TYPE, cursor.getColumnIndex(AlbumColumns.TYPE));
                mappedColumns.put(AlbumColumns.SHARE, cursor.getColumnIndex(AlbumColumns.SHARE));
                mappedColumns.put(AlbumColumns.PICTURE, cursor.getColumnIndex(AlbumColumns.PICTURE));
                mappedColumns.put(AlbumColumns.PICTURE_SMALL, cursor.getColumnIndex(AlbumColumns.PICTURE_SMALL));
                mappedColumns.put(AlbumColumns.PICTURE_MEDIUM, cursor.getColumnIndex(AlbumColumns.PICTURE_MEDIUM));
                mappedColumns.put(AlbumColumns.PICTURE_BIG, cursor.getColumnIndex(AlbumColumns.PICTURE_BIG));
                mappedColumns.put(AlbumColumns.TITLE, cursor.getColumnIndex(AlbumColumns.TITLE));
                mappedColumns.put(AlbumColumns.LINK, cursor.getColumnIndex(AlbumColumns.LINK));
                mappedColumns.put(AlbumColumns.TRACKLIST, cursor.getColumnIndex(AlbumColumns.TRACKLIST));
                mappedColumns.put(AlbumColumns.IS_FAVOURITE, cursor.getColumnIndex(AlbumColumns.IS_FAVOURITE));
                mappedColumns.put(AlbumColumns.FANS_COUNT, cursor.getColumnIndex(AlbumColumns.FANS_COUNT));
                mappedColumns.put(AlbumColumns.IS_RECOMMENDED, cursor.getColumnIndex(AlbumColumns.IS_RECOMMENDED));
                mappedColumns.put(AlbumColumns.LABEL, cursor.getColumnIndex(AlbumColumns.LABEL));
                mappedColumns.put(AlbumColumns.DURATION, cursor.getColumnIndex(AlbumColumns.DURATION));
                mappedColumns.put(AlbumColumns.HAS_EXPLICIT_LYRICS, cursor.getColumnIndex(AlbumColumns.HAS_EXPLICIT_LYRICS));
                mappedColumns.put(AlbumColumns.IS_AVAILABLE, cursor.getColumnIndex(AlbumColumns.IS_AVAILABLE));
                mappedColumns.put(AlbumColumns.RATING, cursor.getColumnIndex(AlbumColumns.RATING));
                mappedColumns.put(AlbumColumns.ARTIST_ID, cursor.getColumnIndex(AlbumColumns.ARTIST_ID));
                mappedColumns.put(AlbumColumns.GENRE_ID, cursor.getColumnIndex(AlbumColumns.GENRE_ID));
                mappedColumns.put(AlbumColumns.TRACKS_COUNT, cursor.getColumnIndex(AlbumColumns.TRACKS_COUNT));
                mappedColumns.put(AlbumColumns.RELEASE_DATE, cursor.getColumnIndex(AlbumColumns.RELEASE_DATE));
                mappedColumns.put(AlbumColumns.RECORD_TYPE, cursor.getColumnIndex(AlbumColumns.RECORD_TYPE));
            }
        }

        public static Album convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(AlbumColumns.ID));
            String share = cursor.getString(mappedColumns.get(AlbumColumns.SHARE));
            String type = cursor.getString(mappedColumns.get(AlbumColumns.TYPE));
            String label = cursor.getString(mappedColumns.get(AlbumColumns.LABEL));
            String picture = cursor.getString(mappedColumns.get(AlbumColumns.PICTURE));
            String picture_small = cursor.getString(mappedColumns.get(AlbumColumns.PICTURE_SMALL));
            String picture_medium = cursor.getString(mappedColumns.get(AlbumColumns.PICTURE_MEDIUM));
            String picture_big = cursor.getString(mappedColumns.get(AlbumColumns.PICTURE_BIG));
            String title = cursor.getString(mappedColumns.get(AlbumColumns.TITLE));
            String link = cursor.getString(mappedColumns.get(AlbumColumns.LINK));
            String trackList = cursor.getString(mappedColumns.get(AlbumColumns.TRACKLIST));
            int favouriteVal = cursor.getInt(mappedColumns.get(AlbumColumns.IS_FAVOURITE));
            int fansCount = cursor.getInt(mappedColumns.get(AlbumColumns.FANS_COUNT));
            int duration = cursor.getInt(mappedColumns.get(AlbumColumns.DURATION));
            int hasExplicitLyricsVal = cursor.getInt(mappedColumns.get(AlbumColumns.HAS_EXPLICIT_LYRICS));
            int isRecommendedVal = cursor.getInt(mappedColumns.get(AlbumColumns.IS_RECOMMENDED));
            int isAvailableVal = cursor.getInt(mappedColumns.get(AlbumColumns.IS_AVAILABLE));
            int ratingVal = cursor.getInt(mappedColumns.get(AlbumColumns.RATING));
            int artistId = cursor.getInt(mappedColumns.get(AlbumColumns.ARTIST_ID));
            int genreId = cursor.getInt(mappedColumns.get(AlbumColumns.GENRE_ID));
            int trackCount = cursor.getInt(mappedColumns.get(AlbumColumns.TRACKS_COUNT));
            String releaseDate = cursor.getString(mappedColumns.get(AlbumColumns.RELEASE_DATE));
            String recordType = cursor.getString(mappedColumns.get(AlbumColumns.RECORD_TYPE));

            Album album = new Album();
            album.setId(Long.valueOf(id));
            album.setTitle(title);
            album.setShare(share);
            album.setPictureMedium(picture_medium);
            album.setPicture(picture);
            album.setPictureSmall(picture_small);
            album.setPictureBig(picture_big);
            album.setType(type);
            album.setFavourite(favouriteVal == 1);
            album.setFansCount(fansCount);
            album.setTracklist(trackList);
            album.setLink(link);
            album.setIsRecommended(isRecommendedVal == 1);
            album.setHasExplicitLyrics(hasExplicitLyricsVal == 1);
            album.setAvailable(isAvailableVal == 1);
            album.setDuration(duration);
            album.setLabel(label);
            album.setRating(ratingVal);
            album.setReleaseDate(releaseDate);
            album.setRecordType(recordType);
            album.setGenreID(genreId);
            album.setTracksCount(trackCount);
            Cursor artistCursor = DeePlayerApp.get().getApplicationContext().getContentResolver()
                    .query(SchematicDataProvider.Artists.withId(artistId), null, null, null, null);
            if (artistCursor != null) {
                if (artistCursor.moveToFirst()) {
                    Artist artist = ArtistConverter.convertFromCursor(artistCursor);
                    album.setArtist(artist);
                }
            }
            // TODO: add fields
            return album;
        }

        public static ContentValues[] convertFrom(Album album) {
            // TODO: add fields
            ContentValues albumVal = new AlbumsValuesBuilder().id(album.getId()).title(album.getTitle())
                    .share(album.getShare()).isFavourite(album.isFavourite() ? 1 : 0)
                    .picture(album.getPicture()).pictureSmall(album.getPictureSmall())
                    .pictureMedium(album.getPictureMedium()).pictureBig(album.getPictureBig())
                    .fansCount(album.getFansCount()).duration(album.getDuration())
                    .genreId(album.getGenreID()).fansCount(album.getFansCount()).tracksCount(album.getTracksCount())
                    .tracklist(album.getTracklist()).link(album.getLink()).artistId(album.getArtist().getId())
                    .isRecommended(album.isRecommended() ? 1 : 0).type(album.getType()).values();
            ContentValues artist = ArtistConverter.convertFrom(album.getArtist());
            ContentValues[] result = new ContentValues[2];
            final int albumIndex = getAlbumIndex();
            final int artistIndex = getArtistIndex();
            result[albumIndex] = albumVal;
            result[artistIndex] = artist;
            return result;
        }
    }

    public static final class TrackConverter {
        private static Map<String, Integer> mappedColumns;

        private static void init(Cursor cursor) {
            if (mappedColumns == null) {
                mappedColumns = new HashMap<>();
            }
            if (mappedColumns.size() == 0) {
                mappedColumns.put(TrackColumns.ID, cursor.getColumnIndex(TrackColumns.ID));
                mappedColumns.put(TrackColumns.TYPE, cursor.getColumnIndex(TrackColumns.TYPE));
                mappedColumns.put(TrackColumns.SHARE, cursor.getColumnIndex(TrackColumns.SHARE));
                mappedColumns.put(TrackColumns.TITLE, cursor.getColumnIndex(TrackColumns.TITLE));
                mappedColumns.put(TrackColumns.TITLE_SHORT, cursor.getColumnIndex(TrackColumns.TITLE_SHORT));
                mappedColumns.put(TrackColumns.TITLE_VERSION, cursor.getColumnIndex(TrackColumns.TITLE_VERSION));
                mappedColumns.put(TrackColumns.LINK, cursor.getColumnIndex(TrackColumns.LINK));
                mappedColumns.put(TrackColumns.IS_FAVOURITE, cursor.getColumnIndex(TrackColumns.IS_FAVOURITE));
                mappedColumns.put(TrackColumns.TRACK_POSITION, cursor.getColumnIndex(TrackColumns.TRACK_POSITION));
                mappedColumns.put(TrackColumns.IS_RECOMMENDED, cursor.getColumnIndex(TrackColumns.IS_RECOMMENDED));
                mappedColumns.put(TrackColumns.PREVIEW, cursor.getColumnIndex(TrackColumns.PREVIEW));
                mappedColumns.put(TrackColumns.DURATION, cursor.getColumnIndex(TrackColumns.DURATION));
                mappedColumns.put(TrackColumns.HAS_EXPLICIT_LYRICS, cursor.getColumnIndex(TrackColumns.HAS_EXPLICIT_LYRICS));
                mappedColumns.put(TrackColumns.IS_READABLE, cursor.getColumnIndex(TrackColumns.IS_READABLE));
                mappedColumns.put(TrackColumns.DISK_NUMBER, cursor.getColumnIndex(TrackColumns.DISK_NUMBER));
                mappedColumns.put(TrackColumns.ARTIST_ID, cursor.getColumnIndex(TrackColumns.ARTIST_ID));
                mappedColumns.put(TrackColumns.ALBUM_ID, cursor.getColumnIndex(TrackColumns.ALBUM_ID));
                mappedColumns.put(TrackColumns.RELEASE_DATE, cursor.getColumnIndex(TrackColumns.RELEASE_DATE));
            }
        }

        public static Track convertFromCursor(Cursor cursor) {
            init(cursor);
            int id = cursor.getInt(mappedColumns.get(TrackColumns.ID));
            String type = cursor.getString(mappedColumns.get(TrackColumns.TYPE));
            String share = cursor.getString(mappedColumns.get(TrackColumns.SHARE));
            String title = cursor.getString(cursor.getColumnIndex(TrackColumns.TITLE));
            String title_short = cursor.getString(cursor.getColumnIndex(TrackColumns.TITLE_SHORT));
            String title_version = cursor.getString(cursor.getColumnIndex(TrackColumns.TITLE_VERSION));
            String link = cursor.getString(cursor.getColumnIndex(TrackColumns.LINK));
            int favouriteVal = cursor.getInt(cursor.getColumnIndex(TrackColumns.IS_FAVOURITE));
            int recommendedVal = cursor.getInt(cursor.getColumnIndex(TrackColumns.IS_RECOMMENDED));
            int trackPosition = cursor.getInt(cursor.getColumnIndex(TrackColumns.TRACK_POSITION));
            String preview = cursor.getString(cursor.getColumnIndex(TrackColumns.PREVIEW));
            int duration = cursor.getInt(cursor.getColumnIndex(TrackColumns.DURATION));
            int explicitLyricsVal = cursor.getInt(cursor.getColumnIndex(TrackColumns.HAS_EXPLICIT_LYRICS));
            int readableVal = cursor.getInt(cursor.getColumnIndex(TrackColumns.IS_READABLE));
            int diskNumber = cursor.getInt(cursor.getColumnIndex(TrackColumns.DISK_NUMBER));
            int artistId = cursor.getInt(cursor.getColumnIndex(TrackColumns.ARTIST_ID));
            int albumId = cursor.getInt(cursor.getColumnIndex(TrackColumns.ALBUM_ID));
            String releaseDate = cursor.getString(cursor.getColumnIndex(TrackColumns.RELEASE_DATE));

            Track track = new Track();
            track.setId(Long.valueOf(id));
            track.setType(type);
            track.setShare(share);
            track.setTitle(title);
            track.setShortTitle(title_short);
            track.setTitleVersion(title_version);
            track.setDeezerURL(link);
            track.setTrackPosition(trackPosition);
            track.setPreview(preview);
            track.setDuration(duration);
            track.setHasExplicitLyrics(explicitLyricsVal == 1);
            track.setFavourite(favouriteVal == 1);
            track.setReadable(readableVal == 1);
            track.setDiskNumber(diskNumber);
            track.setReleaseDate(releaseDate);
            track.setIsRecommended(recommendedVal == 1);
            // TODO: fix album + artist conversion
            Cursor albumCursor = DeePlayerApp.get().getApplicationContext().getContentResolver()
                    .query(SchematicDataProvider.Albums.withId(albumId), null, null, null, null);
            if (albumCursor != null) {
                if (albumCursor.moveToFirst()) {
                    Album album = AlbumConverter.convertFromCursor(albumCursor);
                    track.setAlbum(album);
                    if (album.getArtist() == null || album.getArtist().getId() != artistId) {
                        Cursor artistCursor = DeePlayerApp.get().getApplicationContext().getContentResolver()
                                .query(SchematicDataProvider.Artists.withId(artistId), null, null, null, null);
                        if (artistCursor != null) {
                            if (artistCursor.moveToFirst()) {
                                Artist artist = ArtistConverter.convertFromCursor(artistCursor);
                                album.setArtist(artist);
                            }
                        }
                    }
                    track.setArtist(album.getArtist());
                }
            }
            return track;
        }

        public static ContentValues[] convertFrom(Track track) {
            // TODO: fix album + artist conversion
            TracksValuesBuilder builder = new TracksValuesBuilder().id(track.getId()).title(track.getTitle())
                    .titleShort(track.getShortTitle()).titleVersion(track.getTitleVersion())
                    .isFavourite(track.isFavourite() ? 1 : 0).duration(track.getDuration())
                    .hasExplicitLyrics(track.isHasExplicitLyrics() ? 1 : 0).link(track.getDeezerURL())
                    .releaseDate(track.getReleaseDate()).preview(track.getPreview()).share(track.getShare())
                    .diskNumber(track.getDiskNumber()).trackPosition(track.getTrackPosition())
                    .type(track.getType()).isReadable(track.isReadable() ? 1 : 0)
                    .isRecommended(track.isRecommended() ? 1 : 0);
            ContentValues[] result = new ContentValues[3];

            if (track.getArtist() != null && track.getAlbum() != null) {
                builder.artistId(track.getArtist().getId());
                builder.albumId(track.getAlbum().getId());
                track.getAlbum().setArtist(track.getArtist());
                ContentValues[] album = AlbumConverter.convertFrom(track.getAlbum());
                final int albumIndex = getAlbumIndex();
                final int artistIndex = getArtistIndex();
                result[albumIndex] = album[albumIndex];
                result[artistIndex] = album[artistIndex];
            }
            final int trackIndex = getTrackIndex();
            result[trackIndex] = builder.values();
            return result;
        }
    }
}
