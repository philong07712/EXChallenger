package com.example.exchallenger.ui.work_from_home;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.exchallenger.R;
import com.example.exchallenger.WorkFromHomeManager;

import java.util.Locale;

import static com.example.exchallenger.WorkFromHomeManager.AFTERNOON_FROM;
import static com.example.exchallenger.WorkFromHomeManager.AFTERNOON_TO;
import static com.example.exchallenger.WorkFromHomeManager.MORNING_FROM;
import static com.example.exchallenger.WorkFromHomeManager.MORNING_TO;

public class WorkingTimeDialog extends DialogFragment {

    private String timeFormat = "%02d:%02d";

    private TextView txtMoringFrom, txtMoringTo, txtAfternoonFrom, txtAfternoonTo, txtSave;
    public OnChangeWorkingTimeListener onChangeWorkingTimeListener;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    private int[] time = new int[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_working_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        time[0] = WorkFromHomeManager.getMorningFrom();
        time[1] = WorkFromHomeManager.getMorningTo();
        time[2] = WorkFromHomeManager.getAfternoonFrom();
        time[3] = WorkFromHomeManager.getAfternoonTo();

        initView(view);
        setListener(view);
        setView();
    }

    private void initView(View view) {
        txtMoringFrom = view.findViewById(R.id.txt_from_1);
        txtMoringTo = view.findViewById(R.id.txt_to_1);
        txtAfternoonFrom = view.findViewById(R.id.txt_from_2);
        txtAfternoonTo = view.findViewById(R.id.txt_to_2);
        txtSave = view.findViewById(R.id.txt_save);
    }

    private void setListener(View view) {
        txtMoringFrom.setOnClickListener(v -> showDialog(0));
        txtMoringTo.setOnClickListener(v -> showDialog(1));
        txtAfternoonFrom.setOnClickListener(v -> showDialog(2));
        txtAfternoonTo.setOnClickListener(v -> showDialog(3));

        txtSave.setOnClickListener(v -> {
            WorkFromHomeManager.saveData(MORNING_FROM, time[0]);
            WorkFromHomeManager.saveData(MORNING_TO, time[1]);
            WorkFromHomeManager.saveData(AFTERNOON_FROM, time[2]);
            WorkFromHomeManager.saveData(AFTERNOON_TO, time[3]);
            Toast.makeText(getContext(), R.string.your_data_saved, Toast.LENGTH_SHORT).show();
            dismiss();
        });

        view.findViewById(R.id.img_back).setOnClickListener(v -> dismiss());
    }

    private void setView() {
        txtMoringFrom.setText(getTimeStr(time[0]));
        txtMoringTo.setText(getTimeStr(time[1]));
        txtAfternoonFrom.setText(getTimeStr(time[2]));
        txtAfternoonTo.setText(getTimeStr(time[3]));
    }

    public void showDialog(final int defaultPosition) {
        int[] data = getTime(time[defaultPosition]);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int result = (hourOfDay * 60 * 60 + minute * 60) * 1000;
                        if ((defaultPosition - 1 >= 0 && result <= time[defaultPosition - 1])
                                || (defaultPosition + 1 < time.length && result >= time[defaultPosition + 1])) {
                            Toast.makeText(getContext(), R.string.time_is_invalid, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        time[defaultPosition] = result;
                        setView();
                    }
                }, data[0], data[1], true);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public int[] getTime(int defaultValue) {
        int time = defaultValue / (1000 * 60);

        int[] timeArr = new int[2];
        timeArr[0] = time / 60;
        timeArr[1] = time % 60;
        return timeArr;
    }

    public String getTimeStr(int defaultValue) {
        int time = defaultValue / (1000 * 60);
        return String.format(Locale.US, timeFormat, time / 60, time % 60);
    }

    public void setOnChangeWorkingTimeListener(OnChangeWorkingTimeListener onChangeWorkingTimeListener) {
        this.onChangeWorkingTimeListener = onChangeWorkingTimeListener;
    }

    public interface OnChangeWorkingTimeListener {
        void onChanged();
    }
}
