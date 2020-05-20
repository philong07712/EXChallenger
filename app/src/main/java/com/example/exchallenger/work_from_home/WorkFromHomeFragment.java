package com.example.exchallenger.work_from_home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class WorkFromHomeFragment extends Fragment {
    private static final String TAG = WorkFromHomeFragment.class.getSimpleName();
    private static final int REQUEST_RELAX = 2;
    private static final int REQUEST_WORK = 3;

    private static final int VIEW_WORK = 0;
    private static final int VIEW_RELAX = 1;

    private int workTime;
    private int relaxTime;

    private int currentView = VIEW_WORK;

    private TextView txtTime, txtTitle, txtStart, txtCurrentView, txtWorkTime;
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
        txtTitle = view.findViewById(R.id.txt_title);
        txtStart = view.findViewById(R.id.txt_start);
        circleProgressView = view.findViewById(R.id.circle_progress);
        txtCurrentView = view.findViewById(R.id.txt_current_view);
        txtWorkTime = view.findViewById(R.id.txt_work_time);

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
                txtTime.setText(String.format(Locale.US, "%02d:%02d", minutes, sec));
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

        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),
                currentView == VIEW_WORK ? WorkFromHomeReceiver.NOTIFICATION_RELAX_ID : WorkFromHomeReceiver.NOTIFICATION_WORK_ID
                , intent, PendingIntent.FLAG_NO_CREATE);

        boolean alarmUp = (alarmIntent != null);

        if (alarmUp)
            txtStart.setText(R.string.cancel);

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
                setAlarm();
            }
        });

        txtWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkingTimeDialog workingTimeFragment = new WorkingTimeDialog();
                workingTimeFragment.show(getFragmentManager(), WorkingTimeDialog.class.getSimpleName());
            }
        });
    }

    private void removeAlarm(int id) {
        AlarmManager alarmManager =
                (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), WorkFromHomeReceiver.class);
        intent.setAction(String.valueOf(id));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), id,
                intent, PendingIntent.FLAG_NO_CREATE);

        if (alarmIntent != null)
            alarmManager.cancel(alarmIntent);
    }

    private void setViewWorkTime() {
        if (currentView == VIEW_WORK) {
            txtCurrentView.setSelected(false);
            txtStart.setSelected(false);
            txtWorkTime.setSelected(false);
            txtTitle.setText(R.string.working);
            txtCurrentView.setText(String.format(getString(R.string.relax_s), getMinuteString(relaxTime)));

            circleProgressView.setProgressWithAnimate(workTime);
            circleProgressView.setMainColor(ContextCompat.getColor(getContext(), R.color.color_blue));
            circleProgressView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_blue_dimmer));
        } else {
            txtCurrentView.setSelected(true);
            txtStart.setSelected(true);
            txtWorkTime.setSelected(true);
            txtTitle.setText(R.string.relax);
            txtCurrentView.setText(String.format(getString(R.string.work_s), getMinuteString(workTime)));

            circleProgressView.setProgressWithAnimate(relaxTime);
            circleProgressView.setMainColor(ContextCompat.getColor(getContext(), R.color.color_orange));
            circleProgressView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_orange_dimmer));
        }
    }

    public void setAlarm() {
        AlarmManager alarmManager =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (currentView == VIEW_WORK) {
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
