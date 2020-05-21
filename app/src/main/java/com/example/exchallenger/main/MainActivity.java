package com.example.exchallenger.main;

import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Models.event.LoginSuccessEvent;
import com.example.exchallenger.Models.event.LogoutEvent;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseActivity;
import com.example.exchallenger.utils.AppUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private MainPagerAdapter mainPagerAdapter;
    private int currentPos = 0;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setUp() {
        firebaseAuth = FirebaseAuth.getInstance();

        viewPager.setOffscreenPageLimit(3);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            MyApplication.getInstance().getUserHelper().getUsersInfo(firebaseUser.getUid(),
                    new UserHelper.GetUserInfo() {
                        @Override
                        public void onRead(Map<String, Object> user) {

                            Log.d("LOGIN__", "Map<String, Object> user " + user.toString());
                            MyApplication.user = AppUtils.convertMapToUser(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), true);
                                    viewPager.setAdapter(mainPagerAdapter);
                                }
                            });
                        }
                    });
        } else {
            mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), false);
            viewPager.setAdapter(mainPagerAdapter);
        }

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
                currentPos = position;
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginSuccessEvent loginSuccessEvent) {
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), true);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(currentPos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LogoutEvent logoutEvent) {
        firebaseAuth.signOut();
        MyApplication.user = null;
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), false);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(currentPos);
    }
}
