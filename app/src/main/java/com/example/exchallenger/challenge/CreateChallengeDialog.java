package com.example.exchallenger.challenge;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.R;
import com.example.exchallenger.utils.AppUtils;
import com.utilityview.customview.CustomTextviewFonts;
import com.utilityview.customview.TagLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateChallengeDialog extends DialogFragment {
    @BindView(R.id.switch_repeat)
    SwitchCompat switchRepeat;
    private Unbinder mUnBinder;

    @BindView(R.id.tag_layout)
    TagLayout tagLayout;
    @BindView(R.id.seek_bar_number)
    AppCompatSeekBar seekBarNumber;
    @BindView(R.id.tv_number)
    CustomTextviewFonts tvNumber;
    @BindView(R.id.text2)
    CustomTextviewFonts tvUnit;
    @BindView(R.id.time_picker)
    TimePicker timePicker;
    @BindView(R.id.tv_sunday)
    CustomTextviewFonts tvSunday;
    @BindView(R.id.tv_monday)
    CustomTextviewFonts tvMonday;
    @BindView(R.id.tv_tuesday)
    CustomTextviewFonts tvTuesday;
    @BindView(R.id.tv_wed)
    CustomTextviewFonts tvWed;
    @BindView(R.id.tv_thur)
    CustomTextviewFonts tvThur;
    @BindView(R.id.tv_fri)
    CustomTextviewFonts tvFri;
    @BindView(R.id.tv_sat)
    CustomTextviewFonts tvSat;
    @BindView(R.id.view_pick_date)
    LinearLayout viewPickDate;
    @BindView(R.id.btn_ok)
    CustomTextviewFonts btnOk;

    List<String> types = Arrays.asList("Push up", "Plank", "Squat", "Push up", "Plank", "Squat");
    private String exercise = types.get(0);


    public static CreateChallengeDialog newInstance(OnSubmitListener onSubmitListener) {
        CreateChallengeDialog dialog = new CreateChallengeDialog();
        dialog.setOnSubmitListener(onSubmitListener);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_challenge, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp();
    }

    private int selectTypePos = 0;

    public void setUp() {
        timePicker.setIs24HourView(true);
        switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewPickDate.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        seekBarNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumber.setText(progress + AppUtils.getUnitStrOfExercise(exercise));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        for (int i = 0; i < types.size(); ++i) {
            String type = types.get(i);
            View itemTag = getLayoutInflater().inflate(R.layout.item_type_tag, null, false);
            TextView tvTag = itemTag.findViewById(R.id.tv_tag);
            tvTag.setText(type);
            tagLayout.addTagView(itemTag);
            if (i == selectTypePos) {
                tvTag.setSelected(true);
            }
            int finalI = i;

            // change data and calendar when click tag
            tvTag.setOnClickListener(v -> {
                if (finalI != selectTypePos) {
                    View oldSelectedTag = tagLayout.getTagAt(selectTypePos);
                    if (oldSelectedTag != null) {
                        oldSelectedTag.findViewById(R.id.tv_tag).setSelected(false);
                    }
                    tvTag.setSelected(true);
                    selectTypePos = finalI;
                    exercise = types.get(selectTypePos).toLowerCase();
                    if (exercise.equals(ChallengeItem.PLANK)) {
                        seekBarNumber.setMax(10);
                        seekBarNumber.setProgress(3);
                    } else {
                        seekBarNumber.setMax(50);
                        seekBarNumber.setProgress(20);
                    }
                    tvNumber.setText(seekBarNumber.getProgress() + AppUtils.getUnitStrOfExercise(exercise));
                }
            });
        }
    }

    @OnClick(R.id.btn_ok)
    public void onBtnOkClicked() {
        ArrayList<Long> repeat = new ArrayList<>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L));
        if (switchRepeat.isChecked()) {
            repeat.set(0, tvSunday.isSelected() ? 1L : 0L);
            repeat.set(1, tvMonday.isSelected() ? 1L : 0L);
            repeat.set(2, tvTuesday.isSelected() ? 1L : 0L);
            repeat.set(3, tvWed.isSelected() ? 1L : 0L);
            repeat.set(4, tvThur.isSelected() ? 1L : 0L);
            repeat.set(5, tvFri.isSelected() ? 1L : 0L);
            repeat.set(6, tvSat.isSelected() ? 1L : 0L);
        }
        String type = types.get(selectTypePos);
        int number = seekBarNumber.getProgress();
        int point = number * AppUtils.getPointOfExercise(type);
        ChallengeItem challengeItem = new ChallengeItem(type, seekBarNumber.getProgress(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute(), repeat, AppUtils.getUnitOfExercise(type),
                point, AppUtils.getPhotoOfExercise(type));
        if (onSubmitListener != null) {
            onSubmitListener.onSubmit(challengeItem);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    public void onBtnCancelClicked() {
        dismiss();
    }

    @OnClick({R.id.tv_sunday, R.id.tv_monday, R.id.tv_tuesday, R.id.tv_wed, R.id.tv_thur, R.id.tv_fri, R.id.tv_sat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sunday:
                tvSunday.setSelected(!tvSunday.isSelected());
                break;
            case R.id.tv_monday:
                tvMonday.setSelected(!tvMonday.isSelected());
                break;
            case R.id.tv_tuesday:
                tvTuesday.setSelected(!tvTuesday.isSelected());
                break;
            case R.id.tv_wed:
                tvWed.setSelected(!tvWed.isSelected());
                break;
            case R.id.tv_thur:
                tvThur.setSelected(!tvThur.isSelected());
                break;
            case R.id.tv_fri:
                tvFri.setSelected(!tvFri.isSelected());
                break;
            case R.id.tv_sat:
                tvSat.setSelected(!tvSat.isSelected());
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    private OnSubmitListener onSubmitListener;

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener {
        void onSubmit(ChallengeItem challengeItem);
    }
}
