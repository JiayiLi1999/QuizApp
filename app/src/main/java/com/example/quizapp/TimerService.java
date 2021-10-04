package com.example.quizapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class TimerService extends Service {

    final String CHANNEL_ID = "1234";
    final int NOTIFICATION_ID = 5354;

    final IBinder binder = new TimerBinder();
    int lastTimeA = 0;
    int lastTimeB = 0;


    public class TimerBinder extends Binder{
        TimerService getTimer(){
            return TimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getTimeA(){
        lastTimeA++;
        return lastTimeA;
    }

    public int getTimeB(){
        lastTimeB++;
        return lastTimeB;
    }

    public void setTimeA(int timeA){
        lastTimeA = timeA;
        return;
    }

    public void setTimeB(int timeB){
        lastTimeA = timeB;
        return;
    }

    public void passTime(){
        overtimeNoti();
        showToasts();
    }

    private void showToasts() {
        CharSequence text = getString(R.string.exceed_time_text);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private void overtimeNoti() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.exceed_time_title))
                .setContentText(getString(R.string.exceed_time_text))
                .setVibrate(new long[]{0, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(actionPendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Running On a SDK 26 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        manager.notify(NOTIFICATION_ID, builder.build());

    }


}
