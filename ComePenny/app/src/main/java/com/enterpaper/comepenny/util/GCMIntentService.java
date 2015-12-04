package com.enterpaper.comepenny.util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {
    public static final String TAG = "push Test";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
//        Used to name the worker thread, important only for debugging.
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                 String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");

                sendNotification(message);

                //단말기 깨운후 wakeLock해제를 해줘야하기때문에 시간을 두고 wakelock해제
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }


                // WakeLock 해제.
                PushWakeLock.releaseCpuLock();

            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        // 잠든 단말을 깨워라.
        PushWakeLock.acquireCpuWakeLock(this);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        //push 눌럿을떄 이동하는 페이지 설정
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.homo_appicon)
                        .setContentTitle("Today's Homo")
                        .setTicker("Today's Homo")
                                // .setNumber(1)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)
                                .setSummaryText("더보기"))
                        .setContentText("당겨서 살펴보세요")
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}