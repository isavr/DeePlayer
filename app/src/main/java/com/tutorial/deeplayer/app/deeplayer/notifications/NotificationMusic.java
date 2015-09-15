package com.tutorial.deeplayer.app.deeplayer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.Patterns;
import android.widget.RemoteViews;

import com.deezer.sdk.model.Track;
import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.activities.RecommendationsActivity;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.FavouriteItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

/**
 * Specific way to stick an on-going message on the system
 * with the current song I'm playing.
 * <p>
 * This is a rather complicated set of functions because
 * it interacts with a great deal of the Android API.
 * Read with care.
 * <p>
 * Thanks:
 * <p>
 * - Gave me a complete example on how to add a custom
 * action to a button click on the Notification:
 * http://stackoverflow.com/a/21927248
 */
public class NotificationMusic extends NotificationSimple {
    private static final String TAG = NotificationMusic.class.getSimpleName();

    public static final String ACTION_KEY = "action";
    public static final String ACTION_TRACK_ID_KEY = "track_id";
    public static final String ACTION_SKIP_VAL = "skip";
    public static final String ACTION_LIKE_VAL = "like";
    public static final String ACTION_TOGGLE_PAUSE_VAL = "toggle_pause";


    /**
     * Reference to the context that notified.
     */
    Context context = null;

    /**
     * Reference to the service we're attached to.
     */
    Service service = null;

    /**
     * Used to create and update the same notification.
     */
    NotificationCompat.Builder notificationBuilder = null;

    /**
     * Custom appearance of the notification, also updated.
     */
    RemoteViews notificationView = null;

    /**
     * Used to actually broadcast the notification.
     * Depends on the Activity that originally called
     * the nofitication.
     */
    NotificationManager notificationManager = null;

    /**
     * Sends a system notification with a song's information.
     * <p>
     * If the user clicks the notification, will be redirected
     * to the "Now Playing" Activity.
     * <p>
     * If the user clicks on any of the buttons inside it,
     * custom actions will be executed on the
     * `NotificationButtonHandler` class.
     *
     * @param context Activity that calls this function.
     * @param service Service that calls this function.
     *                Required so the Notification can
     *                run on the background.
     * @param data
     * @param track   Current track info
     * @apiNote By calling this function multiple times, it'll
     * update the old notification.
     */
    public void notifyPlayer(Context context, Service service, FavouriteItem data, Track track) {
        if (this.context == null) {
            this.context = context;
        }
        if (this.service == null) {
            this.service = service;
        }

        Intent notifyIntent;
        if (data instanceof Radio) {
            notifyIntent = new Intent(context, MixActivity.class);
        } else {
            notifyIntent = new Intent(context, RecommendationsActivity.class);
        }
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity
                (context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setting our custom appearance for the notification
        notificationView = new RemoteViews(kMP.packageName, R.layout.notification);
        // Manually settings the buttons and text
        // (ignoring the defaults on the XML)
        String artistName = extractArtistString(data, track);
        notificationView.setTextViewText(R.id.notification_text_type, extractTypeString(data));
        notificationView.setTextViewText(R.id.notification_text_artist, artistName);
        notificationView.setTextViewText(R.id.notification_text_album, extractAlbumName(data, track));
        notificationView.setTextViewText(R.id.notification_text_track, track.getTitle());

        String artistText = track.getTitle() + " by " + artistName;

        // On the notification we have two buttons - Play and Skip
        // Here we make sure the class `NotificationButtonHandler`
        // gets called when user selects one of those.
        //
        // First, building the play button and attaching it.
        Intent buttonPlayIntent = new Intent(context, NotificationPlayButtonHandler.class);
        buttonPlayIntent.putExtra(NotificationMusic.ACTION_KEY, NotificationMusic.ACTION_TOGGLE_PAUSE_VAL);

        PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(context, 0, buttonPlayIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_play, buttonPlayPendingIntent);

        // And now, building and attaching the Skip button.
        Intent buttonSkipIntent = new Intent(context, NotificationSkipButtonHandler.class);
        buttonSkipIntent.putExtra(NotificationMusic.ACTION_KEY, NotificationMusic.ACTION_SKIP_VAL);

        PendingIntent buttonSkipPendingIntent = PendingIntent.getBroadcast(context, 0, buttonSkipIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_skip, buttonSkipPendingIntent);

        // And now, building and attaching the Like button.
        Intent buttonLikeIntent = new Intent(context, NotificationLikeButtonHandler.class);
        buttonLikeIntent.putExtra(NotificationMusic.ACTION_KEY, NotificationMusic.ACTION_LIKE_VAL);
        buttonLikeIntent.putExtra(NotificationMusic.ACTION_TRACK_ID_KEY, track.getId());

        PendingIntent buttonLikePendingIntent = PendingIntent.getBroadcast(context, 0, buttonLikeIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_like, buttonLikePendingIntent);
        //        Intent cancelNotificationIntent = new Intent(context, NotificationCancelHandler.class);
//        cancelNotificationIntent.putExtra("action", "stop");
//
//        PendingIntent cancelNotificationPendingIntent = PendingIntent.getBroadcast(context, 0,
//                cancelNotificationIntent, 0);


        // Finally... Actually creating the Notification
        notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("DeePlayer: Playing " + data.getType() + " - " + data.getTitle() + " Track: " + artistText)
                .setOngoing(true)
                .setContentTitle(data.getTitle())
                .setContentText(artistText)
                .setContent(notificationView);

        Notification notification = notificationBuilder.build();
        notification.bigContentView = notificationView;

        Picasso.with(context).load(R.drawable.ic_action_next).into(notificationView, R.id.notification_button_skip, NOTIFICATION_ID, notification);
        //Picasso.with(context).load(R.drawable.ic_action_like_deselected).into(notificationView, R.id.notification_button_like, NOTIFICATION_ID, notification);
        String itemUrl = extractPictureUrl(data, track);
        if (itemUrl != null && Patterns.WEB_URL.matcher(itemUrl).matches()) {
            Picasso.with(context).load(itemUrl).into(notificationView, R.id.notification_item_image, NOTIFICATION_ID, notification);
        }

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(NOTIFICATION_ID, notification);

        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        service.startForeground(NOTIFICATION_ID, notification);
    }

    private String extractTypeString(FavouriteItem data) {
        String type = data.getType();
        if (data instanceof Radio) {
            type = data.getType() + " - " + data.getTitle();
        }
        return type;
    }

    private String extractArtistString(FavouriteItem data, Track track) {
        if (track.getArtist() != null) {
            return track.getArtist().getName();
        }
        String artist = "unknown";
        if (data instanceof Artist) {
            artist = ((Artist) data).getName();
        } else if (data instanceof Album) {
            artist = ((Album) data).getArtist().getName();
        }
        return artist;
    }

    private String extractAlbumName(FavouriteItem data, Track track) {
        if (track.getAlbum() != null) {
            return track.getAlbum().getTitle();
        }
        String album = "unknown";
        if (data instanceof Album) {
            album = data.getTitle();
        }
        return album;
    }

    private String extractPictureUrl(FavouriteItem data, Track track) {
        String url = data.getPictureSmall();
        if (url == null && track.getAlbum() != null) {
            url = track.getAlbum().getCoverUrl();
        }
//        if (data instanceof Radio) {
//            url = data.getPictureSmall();
//            if (url == null && track.getAlbum() != null) {
//                url = track.getAlbum().getCoverUrl();
//            }
//        } else if (data instanceof Artist) {
//            url = data.getPictureSmall();
//        } else if (data instanceof Album) {
//            url = data.getPictureSmall();
//        } else {
//            url = track.getAlbum().getCoverUrl();
//        }
        return url;
    }


    /**
     * Called when user clicks the "play/pause" button on the on-going system Notification.
     */
    public static class NotificationPlayButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (kMP.musicService != null) {
                kMP.musicService.togglePlayback();
            }
        }
    }

    /**
     * Called when user clicks the "skip" button on the on-going system Notification.
     */
    public static class NotificationSkipButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (kMP.musicService != null) {
                kMP.musicService.next(true);
                //kMP.musicService.playRadio();
            }
        }
    }

    /**
     * Called when user clicks the "like" button on the on-going system Notification.
     */
    public static class NotificationLikeButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (kMP.musicService != null) {
                long trackId = intent.getLongExtra(NotificationMusic.ACTION_TRACK_ID_KEY, -1);
                kMP.musicService.toggleTrackFavouriteStatus(trackId);
                //kMP.musicService.playRadio();
            }
        }
    }

//    /**
//     * Called when user cancels Notification e.g stops the playback
//     */
//    public static class NotificationCancelHandler extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            kMP.stopMusicService(context);
//        }
//    }

    public void notifyIsFavourite(boolean isFavourite) {
        if ((notificationView == null) || (notificationBuilder == null)) {
            Log.d(TAG, "nothing to notify");
            return;
        }
        int iconID = ((isFavourite) ?
                R.drawable.ic_action_like :
                R.drawable.ic_action_like_deselected);
        Notification notification = notificationBuilder.build();
        notification.bigContentView = notificationView;
        Picasso.with(context).load(iconID).into(notificationView, R.id.notification_button_like, NOTIFICATION_ID, notification);
        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        service.startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Updates the Notification icon if the music is paused.
     */
    public void notifyPaused(boolean isPaused) {
        if ((notificationView == null) || (notificationBuilder == null)) {
            Log.d(TAG, "nothing to pause");
            return;
        }

        int iconID = ((isPaused) ?
                R.drawable.ic_action_play :
                R.drawable.ic_action_pause);
//        notificationBuilder.setContent(notificationView);
        Notification notification = notificationBuilder.build();
        notification.bigContentView = notificationView;
        Picasso.with(context).load(iconID).into(notificationView, R.id.notification_button_play, NOTIFICATION_ID, notification);

//        notificationView.setImageViewResource(R.id.notification_button_play, iconID);
//		notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        service.startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Cancels this notification.
     */
    public void cancel() {
        service.stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * Cancels all sent notifications.
     */
    public static void cancelAll(Context c) {
        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
