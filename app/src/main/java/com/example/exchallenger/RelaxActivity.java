package com.example.exchallenger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exchallenger.receivers.WorkFromHomeReceiver;

import static com.example.exchallenger.receivers.WorkFromHomeReceiver.vibratePattern;

public class RelaxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax);
        setDismisKeyguard();

        setListener();
        removeAlarm(WorkFromHomeReceiver.NOTIFICATION_RELAX_ID);
        removeAlarm(WorkFromHomeReceiver.NOTIFICATION_WORK_ID);
        vibrate();
    }

    private void setListener() {
        findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void removeAlarm(int id) {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, WorkFromHomeReceiver.class);
        intent.setAction(String.valueOf(id));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmIntent != null)
            alarmManager.cancel(alarmIntent);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createWaveform(vibratePattern, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(vibratePattern, -1);
        }
    }


    public void setDismisKeyguard() {
        KeyguardManager.KeyguardLock lock = ((KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE)).newKeyguardLock(KEYGUARD_SERVICE);
        PowerManager powerManager = ((PowerManager) getSystemService(Context.POWER_SERVICE));
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wake = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TuanFirebase");

        lock.disableKeyguard();
        wake.acquire();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }
}
