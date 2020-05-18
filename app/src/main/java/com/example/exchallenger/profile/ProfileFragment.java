package com.example.exchallenger.profile;

import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.stats.StatisticFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ProfilePagerAdapter profilePagerAdapter;

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

        showUserData();
    }

    private void showUserData() {
        Glide.with(this).asBitmap()
                .load(R.drawable.ava_joey)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.ava_height) / 2)))
                .into(ivProfile);
    }

    @OnClick(R.id.btn_more)
    void onClickMore(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, StatisticFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
