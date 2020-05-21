package com.example.exchallenger.stats;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Models.User;
import com.example.exchallenger.Models.event.LogoutEvent;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.profile.ProfilePagerAdapter;
import com.example.exchallenger.utils.AppUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utilityview.customview.CustomTextviewFonts;
import com.utilityview.customview.tools.SimpleSpanBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class StatisticFragment extends BaseFragment {
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.view_limit)
    View viewLimit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    CustomTextviewFonts tvBack;
    @BindView(R.id.btn_back)
    View btnBack;
    @BindView(R.id.tv_rank_point)
    CustomTextviewFonts tvRankPoint;
    @BindView(R.id.tv_mission_count)
    CustomTextviewFonts tvMissionCount;
    @BindView(R.id.text1)
    CustomTextviewFonts text1;
    @BindView(R.id.char_rank)
    BarChart charRank;
    @BindView(R.id.view_point)
    LinearLayout viewPoint;
    @BindView(R.id.chart_calories)
    LineChart chartCalories;
    @BindView(R.id.view_calories)
    LinearLayout viewCalories;

    public static StatisticFragment newInstance() {
        StatisticFragment statsFragment = new StatisticFragment();
        return statsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stats;
    }

    @Override
    protected void setUp() {
        setUpPointChart();
    }

    private void setUpPointChart() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            MyApplication.getInstance().getUserHelper().getUsersInfoUpdate(firebaseUser.getUid(), new UserHelper.GetUserInfo() {
                @Override
                public void onRead(Map<String, Object> user) {
                    if (tvMissionCount == null) {
                        return;
                    }
                    if (user == null) {
                        EventBus.getDefault().post(new LogoutEvent());
                    }
                    User newUser = AppUtils.convertMapToUser(user);

                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.appendWithSpace(newUser.getTotalPoints() + "");
                    ssb.append("Total Points",
                            new RelativeSizeSpan(0.7f));
                    tvRankPoint.setText(ssb.build());
                    SimpleSpanBuilder ssb2 = new SimpleSpanBuilder();
                    ssb2.appendWithSpace(newUser.getNumChallenge() + "");
                    ssb2.append("Mission Complete",
                            new RelativeSizeSpan(0.7f));
                    tvMissionCount.setText(ssb2.build());
                    showChartData(newUser);

                }
            });
        }
    }

    private void showChartData(User newUser) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        // faker
        entries.add(new BarEntry(1, Math.round(newUser.getTotalPoints() * 0.6)));
        entries.add(new BarEntry(2, Math.round(newUser.getTotalPoints() * 0.7)));
        entries.add(new BarEntry(3, Math.round(newUser.getTotalPoints() * 0.7)));
        entries.add(new BarEntry(4, Math.round(newUser.getTotalPoints() * 0.5)));
        entries.add(new BarEntry(5, Math.round(newUser.getTotalPoints() * 0.8)));
        entries.add(new BarEntry(6, Math.round(newUser.getTotalPoints() * 0.9)));
        //not faker
        entries.add(new BarEntry(7, newUser.getTotalPoints()));

        ArrayList<String> labels = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        labels.add(AppUtils.convertDateToString(calendar.getTime(), "dd/MM"));
        calendar.add(Calendar.DATE, -1);

        Collections.reverse(labels);

        BarDataSet set = new BarDataSet(entries, "Point");
        set.setValueTextSize(14f);
        BarData data = new BarData(set);
        charRank.setFitBars(true);
        data.setValueTextColor(Color.parseColor("#1B76FF"));
        charRank.setDescription(null);
        data.setBarWidth(0.8f);
        ValueFormatter formatter = new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                return labels.get(((int) value) - 1);
            }
        };
        charRank.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        charRank.getXAxis().setValueFormatter(formatter);
        charRank.getAxisRight().setEnabled(false);
        charRank.setData(data);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
