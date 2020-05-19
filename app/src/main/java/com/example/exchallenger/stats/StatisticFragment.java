package com.example.exchallenger.stats;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.ArrayList;

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
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry( 1, 1200f));
        entries.add(new BarEntry(2, 1300f));
        entries.add(new BarEntry(3, 1450f));
        entries.add(new BarEntry(4, 1350f));
        entries.add(new BarEntry(5, 1480f));
        entries.add(new BarEntry(6, 1560f));
        entries.add(new BarEntry(7, 1460f));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("16/05");
        labels.add("17/05");
        labels.add("18/05");
        labels.add("19/05");
        labels.add("20/05");
        labels.add("21/05");
        labels.add("22/05");
        BarDataSet set = new BarDataSet(entries, "Point");
        BarData data = new BarData(set);
        charRank.setFitBars(true);
        data.setValueTextColor(Color.parseColor("#1B76FF"));
        charRank.setDescription(null);
        data.setBarWidth(0.8f);
        charRank.getXAxis().setEnabled(false);
        charRank.getAxisRight().setEnabled(false);
        charRank.setData(data);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
