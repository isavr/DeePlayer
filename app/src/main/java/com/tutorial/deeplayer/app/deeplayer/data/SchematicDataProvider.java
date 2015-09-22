package com.tutorial.deeplayer.app.deeplayer.data;

import android.net.Uri;

import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.GenreColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.PlaylistColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.RadioColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.TrackColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.UserColumns;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya.savritsky on 28.08.2015.
 */
@ContentProvider(authority = SchematicDataProvider.AUTHORITY,
        database = Database.class)
public class SchematicDataProvider {
    public static final String AUTHORITY = "com.tutorial.deeplayer.app.deeplayer.authority";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String GENRES = "genres";
        String RADIOS = "radios";
        String USER = "users";
        String ARTISTS = "artists";
        String ALBUMS = "albums";
        String ALBUMS_FROM_ARTIST = "albums_from_artist";
        String ALBUMS_WITH_ARTISTS = "albums_with_artists";
        String REC_ALBUMS_WITH_ARTISTS = "rec_albums_with_artists";
        String CHARTED_ALBUMS_WITH_ARTISTS = "charted_albums_with_artists";
        String TRACKS = "tracks";
        String PLAYLISTS = "playlists";
    }

    @TableEndpoint(table = Database.Tables.GENRES)
    public static class Genres {
        @ContentUri(
                path = Path.GENRES,
                type = "vnd.android.cursor.dir/genre",
                defaultSort = GenreColumns.NAME + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.GENRES);

        @InexactContentUri(
                name = "GENRE_ID",
                path = Path.GENRES + "/#",
                type = "vnd.android.cursor.item/genre",
                whereColumn = GenreColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.GENRES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = Database.Tables.RADIOS)
    public static class Radios {
        @ContentUri(
                path = Path.RADIOS,
                type = "vnd.android.cursor.dir/radio",
                defaultSort = RadioColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RADIOS);

        @InexactContentUri(
                name = "RADIO_ID",
                path = Path.RADIOS + "/#",
                type = "vnd.android.cursor.item/radio",
                whereColumn = RadioColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.RADIOS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = Database.Tables.USER)
    public static class User {
        @ContentUri(
                path = Path.USER,
                type = "vnd.android.cursor.dir/users",
                defaultSort = UserColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.USER);

        @InexactContentUri(
                name = "USER_ID",
                path = Path.USER + "/#",
                type = "vnd.android.cursor.item/users",
                whereColumn = UserColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.USER, String.valueOf(id));
        }
    }

    @TableEndpoint(table = Database.Tables.ARTISTS)
    public static class Artists {
        @ContentUri(
                path = Path.ARTISTS,
                type = "vnd.android.cursor.dir/artists",
                defaultSort = ArtistColumns.NAME + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.ARTISTS);

        @InexactContentUri(
                name = "ARTIST_ID",
                path = Path.ARTISTS + "/#",
                type = "vnd.android.cursor.item/artists",
                whereColumn = ArtistColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.ARTISTS, String.valueOf(id));
        }


    }

    @TableEndpoint(table = Database.Tables.ALBUMS)
    public static class Albums {

        @ContentUri(
                path = Path.ALBUMS,
                type = "vnd.android.cursor.dir/albums",
                defaultSort = AlbumColumns.ARTIST_ID + ", " + AlbumColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.ALBUMS);
        static final String ARTIST_NAME_SELECT = "(SELECT " + Database.Tables.ARTISTS
                + "." + ArtistColumns.NAME + " FROM "
                + Database.Tables.ALBUMS + " INNER JOIN " + Database.Tables.ARTISTS
                + " ON "
                + Database.Tables.ARTISTS
                + "."
                + ArtistColumns.ID
                + "="
                + Database.Tables.ALBUMS
                + "."
                + AlbumColumns.ARTIST_ID
                + ")";

        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();

            map.put(AlbumColumns.ARTIST_NAME, ARTIST_NAME_SELECT);

            return map;
        }

        @InexactContentUri(
                name = "ALBUM_ID",
                path = Path.ALBUMS + "/#",
                type = "vnd.android.cursor.item/albums",
                whereColumn = AlbumColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.ALBUMS, String.valueOf(id));
        }

        @InexactContentUri(
                name = "ALBUMS_FROM_ARTIST",
                path = Path.ALBUMS + "/" + Path.ALBUMS_FROM_ARTIST + "/#",
                type = "vnd.android.cursor.item/albums/albums_from_artist",
                whereColumn = AlbumColumns.ARTIST_ID,
                pathSegment = 2)
        public static Uri fromArtist(long artistId) {
            return buildUri(Path.ALBUMS, Path.ALBUMS_FROM_ARTIST, String.valueOf(artistId));
        }

        @InexactContentUri(
                name = "ALBUMS_WITH_ARTISTS",
                table = "albums",
                join = " INNER JOIN (SELECT artists._id, artists.name From artists) as artistSelection ON artistSelection._id=albums.artist_id ",//" INNER JOIN artists ON artists._id=albums.artist_id ",
                path = Path.ALBUMS_WITH_ARTISTS + "/#",
                type = "vnd.android.cursor.item/albums/albums_with_artists",
                whereColumn = Database.Tables.ALBUMS + "." + AlbumColumns.IS_FAVOURITE,
                pathSegment = 1,
        allowInsert = false,
        allowDelete = false,
        allowUpdate = false)
        public static Uri queryWithArtists(boolean isFavourite) {
            return buildUri(Path.ALBUMS_WITH_ARTISTS, String.valueOf(isFavourite ? 1 : 0) );
        }

        @InexactContentUri(
                name = "REC_ALBUMS_WITH_ARTISTS",
                table = "albums",
                join = " INNER JOIN (SELECT artists._id, artists.name From artists) as artistSelection ON artistSelection._id=albums.artist_id ",//" INNER JOIN artists ON artists._id=albums.artist_id ",
                path = Path.REC_ALBUMS_WITH_ARTISTS + "/#",
                type = "vnd.android.cursor.item/albums/rec_albums_with_artists",
                whereColumn = Database.Tables.ALBUMS + "." + AlbumColumns.IS_RECOMMENDED,
                pathSegment = 1,
                allowInsert = false,
                allowDelete = false,
                allowUpdate = false)
        public static Uri recommendedQueryWithArtists(boolean isRecommended) {
            return buildUri(Path.REC_ALBUMS_WITH_ARTISTS, String.valueOf(isRecommended ? 1 : 0) );
        }

        @InexactContentUri(
                name = "CHARTED_ALBUMS_WITH_ARTISTS",
                table = "albums",
                join = " INNER JOIN (SELECT artists._id, artists.name From artists) as artistSelection ON artistSelection._id=albums.artist_id ",//" INNER JOIN artists ON artists._id=albums.artist_id ",
                path = Path.CHARTED_ALBUMS_WITH_ARTISTS + "/#",
                type = "vnd.android.cursor.item/albums/charted_albums_with_artists",
                whereColumn = Database.Tables.ALBUMS + "." + AlbumColumns.POSITION,
                pathSegment = 1,
                allowInsert = false,
                allowDelete = false,
                allowUpdate = false)
        public static Uri chartedQueryWithArtists(int notInChartPositionVal) {
            return buildUri(Path.CHARTED_ALBUMS_WITH_ARTISTS, String.valueOf(notInChartPositionVal));
        }
//
//        @NotifyInsert(paths = Path.ALBUMS)
//        public static Uri[] onInsert(ContentValues values) {
//            final long artistId = values.getAsLong(AlbumColumns.ARTIST_ID);
//            return new Uri[] {
//                    Artists.withId(artistId), fromArtist(artistId),
//            };
//        }
//
//        @NotifyBulkInsert(paths = Path.ALBUMS)
//        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
//            return new Uri[]{
//                    uri,
//            };
//        }
//
//        @NotifyUpdate(paths = Path.ALBUMS + "/#")
//        public static Uri[] onUpdate(Context context,
//                                     Uri uri, String where, String[] whereArgs) {
//            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
//            Cursor c = context.getContentResolver().query(uri, new String[]{
//                    AlbumColumns.ARTIST_ID,
//            }, null, null, null);
//            c.moveToFirst();
//            final long artistId = c.getLong(c.getColumnIndex(AlbumColumns.ARTIST_ID));
//            c.close();
//
//            return new Uri[]{
//                    withId(noteId), fromArtist(artistId), Artists.withId(artistId),
//            };
//        }
    }

    @TableEndpoint(table = Database.Tables.TRACKS)
    public static class Tracks {
        @ContentUri(
                path = Path.TRACKS,
                type = "vnd.android.cursor.dir/tracks",
                defaultSort = TrackColumns.ARTIST_ID + ", " + TrackColumns.ALBUM_ID + ", " + TrackColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.TRACKS);

        @InexactContentUri(
                name = "TRACKS_ID",
                path = Path.TRACKS + "/#",
                type = "vnd.android.cursor.item/tracks",
                whereColumn = TrackColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.TRACKS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = Database.Tables.PLAYLISTS)
    public static class Playlists {
        @ContentUri(
                path = Path.PLAYLISTS,
                type = "vnd.android.cursor.dir/playlists",
                defaultSort = PlaylistColumns.POSITION + " , " + PlaylistColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.PLAYLISTS);

        @InexactContentUri(
                name = "PLAYLISTS_ID",
                path = Path.PLAYLISTS + "/#",
                type = "vnd.android.cursor.item/playlists",
                whereColumn = PlaylistColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.PLAYLISTS, String.valueOf(id));
        }
    }
}
