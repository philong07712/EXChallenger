package com.example.exchallenger.group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.utilityview.customview.CustomButtonFonts;
import com.utilityview.customview.CustomEditTextFonts;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GroupDetailFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt_code)
    CustomEditTextFonts edtCode;
    @BindView(R.id.btn_done)
    CustomButtonFonts btnDone;
    @BindView(R.id.tv_group)
    CustomTextviewFonts tvGroup;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_code)
    CustomTextviewFonts tvCode;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private GroupDetailPagerAdapter groupDetailPagerAdapter;

    public static GroupDetailFragment newInstance() {
        GroupDetailFragment groupFragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        groupFragment.setArguments(args);
        return groupFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_detail;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void setUp() {
        groupDetailPagerAdapter = new GroupDetailPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(groupDetailPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    getActivity().onBackPressed();
                }, Throwable::printStackTrace);

        RxView.clicks(btnDone).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    submitGroupCode();
                }, Throwable::printStackTrace);

        RxTextView.afterTextChangeEvents(edtCode)
                .skipInitialValue()
                .map(event -> event.getEditable().toString().trim().length() > 0)
                .subscribe(hasText -> btnDone.setEnabled(hasText), Throwable::printStackTrace);

        edtCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == EditorInfo.IME_ACTION_GO) {
                    if (edtCode.getText().toString().trim().isEmpty()) {
                        return false;
                    } else {
                        submitGroupCode();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void submitGroupCode() {

    }
}
