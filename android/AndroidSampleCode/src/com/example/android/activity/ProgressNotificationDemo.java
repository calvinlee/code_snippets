/**
 * 
 */

package com.example.android.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;

import com.example.android.R;

/**
 * @author calvin
 */
public class ProgressNotificationDemo extends Activity {
    private static final int MSG_UPDATE_COPY_PROGRESS = 1;

    private static final int TASK_PREPARE_NOTIFICATION_ID = 2;

    private NotificationManager mNotificationMgr;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_COPY_PROGRESS:
                    int progress = msg.arg1;
                    if (progress >= 100) {
                        mNotificationMgr.cancel(TASK_PREPARE_NOTIFICATION_ID);
                        return;
                    }

                    // update the progress view on the notification bar
                    RemoteViews contentView = ((NotificationHolder) msg.obj).progressNotify.contentView;
                    contentView.setProgressBar(R.id.pb, 100, progress, false);
                    contentView.setTextViewText(R.id.rate, progress + "%");
                    sendNotification("Supper", contentView);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mNotificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = sendNotification("Supper", null);
        NotificationHolder holder = new NotificationHolder();
        holder.progressNotify = notify;

        Message msg = mHandler.obtainMessage(MSG_UPDATE_COPY_PROGRESS);
        msg.obj = holder;
        msg.arg1 = 40;
        mHandler.sendMessageDelayed(msg, 2000);

    }

    private Notification sendNotification(String taskName, RemoteViews contentView) {
        String tickerText = getString(R.string.notification_ticker_text);
        if (contentView == null) {
            contentView = new RemoteViews(getPackageName(), R.layout.progress_notification);
            contentView.setTextViewText(R.id.title,
                    String.format(getString(R.string.notification_content_text), taskName));
            contentView.setProgressBar(R.id.pb, 100, 10, false);
            contentView.setTextViewText(R.id.rate, "10%");
        }
        return sendNotification(TASK_PREPARE_NOTIFICATION_ID, tickerText, null, null, contentView);
    }

    private Notification sendNotification(int id, String tickerText, String contentTitle,
            String contentText, RemoteViews contentView) {
        Notification notification = new Notification();
        // TODO:user Notification.Builder above api level 11
        notification.when = System.currentTimeMillis();
        notification.tickerText = tickerText;

        // TODO:
        // must be set, or the notification will never be sent
        notification.icon = R.drawable.ic_launcher;

        Intent intent = new Intent(this, EmptyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (contentView != null) {
            notification.contentView = contentView;
            notification.contentIntent = contentIntent;
        } else {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText,
                    contentIntent);
        }

        mNotificationMgr.notify(id, notification);
        return notification;
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(MSG_UPDATE_COPY_PROGRESS);
        super.onDestroy();
    }

    private class NotificationHolder {

        /**
         * Notification object showing progress
         */
        Notification progressNotify;

    }

}
