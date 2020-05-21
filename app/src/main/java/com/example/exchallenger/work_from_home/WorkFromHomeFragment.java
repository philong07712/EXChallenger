package com.example.exchallenger.work_from_home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.exchallenger.R;
import com.example.exchallenger.WorkFromHomeManager;
import com.example.exchallenger.customviews.CircleProgressView;
import com.example.exchallenger.receivers.WorkFromHomeReceiver;
import com.example.exchallenger.working_time.WorkingTimeDialog;

import java.util.Calendar;
import java.util.Locale;

import static com.example.exchallenger.WorkFromHomeManager.getMorningTo;
import static com.example.exchallenger.WorkFromHomeManager.setAlarmTime;

public class WorkFromHomeFragment extends Fragment {

    boolean lock = false;
    private static final String TAG = WorkFromHomeFragment.class.getSimpleName();
    private static final int REQUEST_RELAX = 2;
    private static final int REQUEST_WORK = 3;

    private static final int VIEW_WORK = 0;
    private static final int VIEW_RELAX = 1;

    private int workTime;
    private int relaxTime;

    private int currentView = VIEW_WORK;

    private TextView txtTime, txtStart, txtCurrentView, txtWorkTime, txtNextAlarm, txtTimeIndicator;
    private CircleProgressView circleProgressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_from_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workTime = WorkFromHomeManager.getWorkTime();
        relaxTime = WorkFromHomeManager.getRelaxTime();

        txtTime = view.findViewById(R.id.txt_time);
        txtStart = view.findViewById(R.id.txt_start);
        circleProgressView = view.findViewById(R.id.circle_progress);
        txtCurrentView = view.findViewById(R.id.txt_current_view);
        txtWorkTime = view.findViewById(R.id.txt_work_time);
        txtNextAlarm = view.findViewById(R.id.txt_next_alarm);
        txtTimeIndicator = view.findViewById(R.id.txt_time_indicator);

        circleProgressView.setMax(60 * 60 * 1000); // 1 hour
        setViewWorkTime();
        circleProgressView.setOnProgressChangeListener(new CircleProgressView.OnProgressChangeListener() {
            @Override
            public void onChanged(float angle, float value) {
                value = value / 1000;
                int minutes = ((int) value) / 60;
                int sec = (int) (value - minutes * 60);
                sec = roundQuaterSec(sec);
                if (sec == 60) {
                    minutes = minutes + 1;
                    sec = 0;
                }
                if (currentView == VIEW_WORK) {
                    workTime = (minutes * 60 + sec) * 1000;
                    WorkFromHomeManager.setWorkTime(workTime);
                } else {
                    relaxTime = (minutes * 60 + sec) * 1000;
                    WorkFromHomeManager.setRelaxTime(relaxTime);
                }
                txtTime.setText(String.format(Locale.US, "%s\n%02d:%02d",
                            (currentView == VIEW_WORK ? "Working" : "Relax"),
                            minutes, sec));
                if (!lock) {
                    updateTextTimeIndicator();
                }
            }
        });

        view.findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setListener();
    }

    private void setListener() {
        Intent intent = new Intent(getContext(), WorkFromHomeReceiver.class);
        if (currentView == VIEW_WORK)
            intent.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_RELAX_ID));
        else
            intent.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_WORK_ID));

        setButtonAlarm();

        txtCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = (currentView + 1) % 2;
                setViewWorkTime();
            }
        });

        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtStart.getText().toString().trim().equals(getString(R.string.start)))
                    setAlarm();
                else
                    stopAlarm();
            }
        });

        txtWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkingTimeDialog workingTimeFragment = new WorkingTimeDialog();
                workingTimeFragment.setOnChangeWorkingTimeListener(new WorkingTimeDialog.OnChangeWorkingTimeListener() {
                    @Override
                    public void onChanged() {
                        getNextAlarm();
                    }
                });
                workingTimeFragment.show(getFragmentManager(), WorkingTimeDialog.class.getSimpleName());
            }
        });

        getNextAlarm();
    }

    private void setViewWorkTime() {
        lock = true;
        if (currentView == VIEW_WORK) {
            txtCurrentView.setSelected(false);
            txtStart.setSelected(false);
            txtWorkTime.setSelected(false);
            txtTime.setSelected(false);
            txtTimeIndicator.setSelected(false);
            txtCurrentView.setText(String.format(getString(R.string.relax_s), getMinuteString(relaxTime)));

            circleProgressView.setProgressWithAnimate(workTime);
            circleProgressView.setMainColor(ContextCompat.getColor(getContext(), R.color.color_blue));
            circleProgressView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_blue_dimmer));
        } else {
            txtCurrentView.setSelected(true);
            txtStart.setSelected(true);
            txtWorkTime.setSelected(true);
            txtTime.setSelected(true);
            txtTimeIndicator.setSelected(true);

            txtCurrentView.setText(String.format(getString(R.string.work_s), getMinuteString(workTime)));
            circleProgressView.setProgressWithAnimate(relaxTime);
            circleProgressView.setMainColor(ContextCompat.getColor(getContext(), R.color.color_orange));
            circleProgressView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_orange_dimmer));
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lock = false;
                updateTextTimeIndicator();
            }
        }, 500);
    }

    public void setAlarm() {
        AlarmManager alarmManager =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (currentView == VIEW_WORK) {
            setAlarmTime(0);
            Intent intent = new Intent(getActivity(), WorkFromHomeReceiver.class);
            intent.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_RELAX_ID));
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_RELAX_ID,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + workTime,
                    workTime + relaxTime,
                    alarmIntent);

            Intent intent2 = new Intent(getActivity(), WorkFromHomeReceiver.class);
            intent2.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_WORK_ID));
            PendingIntent alarmIntent2 = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_WORK_ID,
                    intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + workTime + relaxTime,
                    workTime + relaxTime,
                    alarmIntent2);

        } else {
            setAlarmTime(WorkFromHomeManager.getRelaxTime() - 1000);
            Intent intent = new Intent(getActivity(), WorkFromHomeReceiver.class);
            intent.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_WORK_ID));
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_WORK_ID,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + relaxTime,
                    workTime + relaxTime,
                    alarmIntent);

            Intent intent2 = new Intent(getActivity(), WorkFromHomeReceiver.class);
            intent2.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_RELAX_ID));
            PendingIntent alarmIntent2 = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_RELAX_ID,
                    intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + workTime + relaxTime,
                    workTime + relaxTime,
                    alarmIntent2);
        }
        WorkFromHomeManager.setAlarm(true);
        getNextAlarm();
        setButtonAlarm();
    }

    private void updateTextTimeIndicator() {
        txtTimeIndicator.setText(String.format(Locale.US, "%s to work and %s to relax",
                getTimeString(workTime),
                getTimeString(relaxTime)
        ));
    }

    private void stopAlarm() {
        AlarmManager alarmManager =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), WorkFromHomeReceiver.class);
        intent.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_RELAX_ID));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_RELAX_ID,
                intent, PendingIntent.FLAG_NO_CREATE);


        Intent intent2 = new Intent(getActivity(), WorkFromHomeReceiver.class);
        intent2.setAction(String.valueOf(WorkFromHomeReceiver.NOTIFICATION_WORK_ID));
        PendingIntent alarmIntent2 = PendingIntent.getBroadcast(getContext(), WorkFromHomeReceiver.NOTIFICATION_WORK_ID,
                intent2, PendingIntent.FLAG_NO_CREATE);

        if (alarmIntent != null)
            alarmManager.cancel(alarmIntent);

        if (alarmIntent2 != null)
            alarmManager.cancel(alarmIntent2);

        WorkFromHomeManager.setAlarm(false);
        setButtonAlarm();

    }

    private void setButtonAlarm() {
        if (WorkFromHomeManager.isAlarmOn()) {
            txtStart.setText(R.string.stop);
        } else {
            txtStart.setText(R.string.start);
            txtNextAlarm.setText("");
            txtNextAlarm.setVisibility(View.INVISIBLE);
        }
    }

    private void getNextAlarm() {
        txtNextAlarm.setVisibility(View.INVISIBLE);
        if (WorkFromHomeManager.isAlarmOn()) {
            int[] data = WorkFromHomeManager.getAlarmTime();
            int currentTimeToday = WorkFromHomeManager.getCurrentTimeToday();
            if (!WorkFromHomeManager.isOnWorkingTime(currentTimeToday)) {
                txtNextAlarm.setText(R.string.outside_office_hours);
            } else {
                for (int workTime : data) {
                    if (workTime > currentTimeToday) {
                        if (workTime - currentTimeToday > (WorkFromHomeManager.getWorkTime() + WorkFromHomeManager.getRelaxTime())) {
                            int timeToLunchBreak = getMorningTo() - currentTimeToday;
                            if (timeToLunchBreak > 0 && timeToLunchBreak < (WorkFromHomeManager.getWorkTime() + WorkFromHomeManager.getRelaxTime())) {
                                txtNextAlarm.setText(String.format(Locale.US, "%s to lunch break", getTimeString(timeToLunchBreak)));
                                break;
                            }
                        } else if (workTime - currentTimeToday < WorkFromHomeManager.getRelaxTime()) {
                            int timeToRelax = workTime - currentTimeToday;
                            txtNextAlarm.setText(String.format(Locale.US, "%s left to relax", getTimeString(timeToRelax)));
                        } else if (workTime - currentTimeToday >= WorkFromHomeManager.getRelaxTime()) {
                            int timeToWork = workTime - currentTimeToday - WorkFromHomeManager.getRelaxTime();
                            txtNextAlarm.setText(String.format(Locale.US, "%s left to work", getTimeString(timeToWork)));
                        }
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(txtNextAlarm.getText().toString().trim())) {
                txtNextAlarm.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getTimeString(int time) {
        int minute = time / 1000 / 60;
        int second = (time - time / 1000 / 60) / 1000;
        if (minute > 0) {
            return String.format(Locale.US, "%d minute%s", minute, minute > 1 ? "s" : "");
        } else {
            return String.format(Locale.US, "%d second%s", second, second > 1 ? "s" : "");
        }
    }

    private int roundQuaterSec(int sec) {
        int quater = 15;
        int count = sec / quater;
        if (sec - count * quater > 0)
            count = count + 1;
        return count * quater;
    }

    public String getMinuteString(int time) {
        int minute = time / (60 * 1000);
        if (minute == 1 || minute == 0) {
            return minute + " min";
        } else
            return minute + " mins";
    }
}
