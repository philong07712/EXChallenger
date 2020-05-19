package com.example.exchallenger.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.exchallenger.R;
import com.example.exchallenger.RelaxActivity;
import com.example.exchallenger.WFHDemoActivity;
import com.example.exchallenger.WorkFromHomeManager;

public class WorkFromHomeReceiver extends BroadcastReceiver {
    private static final String TAG = WorkFromHomeReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = WorkFromHomeReceiver.class.getSimpleName();
    public static final int NOTIFICATION_RELAX_ID = 123;
    public static final int NOTIFICATION_WORK_ID = 124;
    public static long[] vibratePattern = new long[]{0, 200, 500, 200, 500, 200};

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.isEmpty(intent.getAction()))
            return;
        int action = Integer.parseInt(intent.getAction());
        Log.e(TAG, "onReceive: " + (action == NOTIFICATION_RELAX_ID ? "relax" : "work") + " " + WorkFromHomeManager.isOnWorkingTime());
        if (WorkFromHomeManager.isOnWorkingTime()) {
            if (action == NOTIFICATION_RELAX_ID && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                Intent intentRelax = new Intent(context, RelaxActivity.class);
                intentRelax.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentRelax);
                return;
            }
            showNotification(context, action);
        }
    }

    public void showNotification(Context context, int action) {
        createNotificationChannel(context);
        Intent intent = new Intent(context, WFHDemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        int titleId = action == NOTIFICATION_RELAX_ID ? R.string.title_time_relax : R.string.title_time_work;
        int desId = action == NOTIFICATION_RELAX_ID ? R.string.des_time_relax : R.string.des_time_work;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(titleId))
                .setContentText(context.getString(desId))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVibrate(vibratePattern)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_RELAX_ID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableVibration(true);
            channel.setVibrationPattern(vibratePattern);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}