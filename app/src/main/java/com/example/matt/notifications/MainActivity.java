package com.example.matt.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View viewById = findViewById(R.id.button);
        if (viewById!=null) {
            viewById.setOnClickListener(new Notifier());
        }

    }

    private class Notifier implements View.OnClickListener {

        private boolean started;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            if (started) {
                executorService.shutdown();
                started = false;
            }
            else {
                executorService.scheduleAtFixedRate(new NotifierRunnable(), 0, 500, TimeUnit.MILLISECONDS);
                started=true;
            }

        }
    }

    private class NotifierRunnable implements Runnable {
        boolean bool = false;
        int progress = 0;
        private final Notification.Builder notificationBuilder1 = new Notification.Builder(MainActivity.this);
        private final Notification.Builder notificationBuilder2 = new Notification.Builder(MainActivity.this)
                .setOngoing(true);
        private final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        @Override
        public void run() {
            if (bool) {
                mNotificationManager.notify(1, notificationBuilder1
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setProgress(100, progress++, false)
                        .setContentText("some text")
                        .setOngoing(true)
                        .setContentTitle("Notification 1: " + progress)
                        .build());
            }
            else {
                mNotificationManager.notify(2, notificationBuilder2
                        .setSmallIcon(R.drawable.me)
                        .setOnlyAlertOnce(true)
                        .setContentText("some text")
                        .setProgress(100, progress++, false)
                        .setContentTitle("Notification 2:" + progress)
                        .build());
            }
            bool = !bool;
        }
    }
}
