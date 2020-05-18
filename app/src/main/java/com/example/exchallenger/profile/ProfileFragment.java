package com.example.exchallenger.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.group.CreateGroupFragment;
import com.example.exchallenger.group.JoinGroupFragment;
import com.example.exchallenger.stats.StatisticFragment;
import com.google.android.material.tabs.TabLayout;
import com.utilityview.customview.CustomTextviewFonts;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_rank_point)
    CustomTextviewFonts tvRankPoint;
    @BindView(R.id.tv_mission_count)
    CustomTextviewFonts tvMissionCount;
    @BindView(R.id.tv_username)
    CustomTextviewFonts tvUsername;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.iv_rank)
    ImageView ivRank;
    @BindView(R.id.tv_rank)
    CustomTextviewFonts tvRank;

    private ProfilePagerAdapter profilePagerAdapter;
    private PopupWindow popup;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void setUp() {
        profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);
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
        createMenuPopup();
        showUserData();
    }

    private void showUserData() {
        Glide.with(this).asBitmap()
                .load(R.drawable.ava_joey)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.ava_height) / 2)))
                .into(ivProfile);
    }

    @OnClick(R.id.btn_more)
    void onClickMore() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, StatisticFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.btn_add)
    public void onClickAddGroup() {

    }

    public void createMenuPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_group, null);
        popup = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        View btnCreateGroup = view.findViewById(R.id.btn_create);
        View btnJoinGroup = view.findViewById(R.id.btn_join);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                showCreateGroup();
            }
        });
        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                showJoinGroup();
            }
        });
    }

    private void showJoinGroup() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, JoinGroupFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void showCreateGroup() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, CreateGroupFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
