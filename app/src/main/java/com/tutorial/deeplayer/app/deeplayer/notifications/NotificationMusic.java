package com.tutorial.deeplayer.app.deeplayer.notifications;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.deezer.sdk.model.Track;
import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.kMP;
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
    Notification.Builder notificationBuilder = null;

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
     * @param radio   Song which we'll display information.
     * @param track
     * @note By calling this function multiple times, it'll
     * update the old notification.
     */
    public void notifyRadio(Context context, Service service, Radio radio, Track track) {

        if (this.context == null)
            this.context = context;
        if (this.service == null)
            this.service = service;

        // Intent that launches the "Now Playing" Activity
        Intent notifyIntent = new Intent(context, MixActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Letting the Intent be executed later by other application.
        PendingIntent pendingIntent = PendingIntent.getActivity
                (context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setting our custom appearance for the notification
        notificationView = new RemoteViews(kMP.packageName, R.layout.notification);

        // Manually settings the buttons and text
        // (ignoring the defaults on the XML)
//        notificationView.setImageViewResource(R.id.notification_button_play, R.drawable.ic_action_play);
//        notificationView.setImageViewResource(R.id.notification_button_skip, R.drawable.ic_action_next);
        notificationView.setTextViewText(R.id.notification_text_title, radio.getTitle());
        String artistText = track.getTitle() + " by " + track.getArtist().getName();
        notificationView.setTextViewText(R.id.notification_text_artist, artistText);


        // On the notification we have two buttons - Play and Skip
        // Here we make sure the class `NotificationButtonHandler`
        // gets called when user selects one of those.
        //
        // First, building the play button and attaching it.
        Intent buttonPlayIntent = new Intent(context, NotificationPlayButtonHandler.class);
        buttonPlayIntent.putExtra("action", "togglePause");

        PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(context, 0, buttonPlayIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_play, buttonPlayPendingIntent);

        // And now, building and attaching the Skip button.
        Intent buttonSkipIntent = new Intent(context, NotificationSkipButtonHandler.class);
        buttonSkipIntent.putExtra("action", "skip");

        PendingIntent buttonSkipPendingIntent = PendingIntent.getBroadcast(context, 0, buttonSkipIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_skip, buttonSkipPendingIntent);

//        Intent cancelNotificationIntent = new Intent(context, NotificationCancelHandler.class);
//        cancelNotificationIntent.putExtra("action", "stop");
//
//        PendingIntent cancelNotificationPendingIntent = PendingIntent.getBroadcast(context, 0,
//                cancelNotificationIntent, 0);

        // Finally... Actually creating the Notification
        notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("DeePlayer: Playing " + radio.getType() + " - " + radio.getTitle() + " Track: " + artistText + "'")
                .setOngoing(true)
//                .setAutoCancel(true)
//                .setDeleteIntent(cancelNotificationPendingIntent)
                .setContentTitle(radio.getTitle())
                .setContentText(artistText)
                .setContent(notificationView);

        Notification notification = notificationBuilder.build();

        Picasso.with(context).load(R.drawable.ic_action_play).into(notificationView, R.id.notification_button_play, NOTIFICATION_ID, notification);
        Picasso.with(context).load(R.drawable.ic_action_next).into(notificationView, R.id.notification_button_skip, NOTIFICATION_ID, notification);
        Picasso.with(context).load(radio.getPictureSmall()).into(notificationView, R.id.notification_item_image, NOTIFICATION_ID, notification);


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(NOTIFICATION_ID, notification);

        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        service.startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Called when user clicks the "play/pause" button on the on-going system Notification.
     */
    public static class NotificationPlayButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            kMP.musicService.togglePlayback();
        }
    }

    /**
     * Called when user clicks the "skip" button on the on-going system Notification.
     */
    public static class NotificationSkipButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            kMP.musicService.next(true);
            //kMP.musicService.playRadio();
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

    /**
     * Updates the Notification icon if the music is paused.
     */
    public void notifyPaused(boolean isPaused) {
        if ((notificationView == null) || (notificationBuilder == null))
            return;

        int iconID = ((isPaused) ?
                R.drawable.ic_action_play :
                R.drawable.ic_action_pause);

        notificationView.setImageViewResource(R.id.notification_button_play, iconID);

        notificationBuilder.setContent(notificationView);

//		notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        // Sets the notification to run on the foreground.
        // (why not the former commented line?)
        service.startForeground(NOTIFICATION_ID, notificationBuilder.build());
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
