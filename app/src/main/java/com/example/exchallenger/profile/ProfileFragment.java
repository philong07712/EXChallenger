package com.example.exchallenger.profile;

import android.view.Gravity;
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
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Models.User;
import com.example.exchallenger.Models.event.LogoutEvent;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.group.CreateGroupFragment;
import com.example.exchallenger.group.JoinGroupFragment;
import com.example.exchallenger.stats.StatisticFragment;
import com.example.exchallenger.utils.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utilityview.customview.CustomTextviewFonts;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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
    @BindView(R.id.tv_times)
    CustomTextviewFonts tvTimes;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.iv_rank)
    ImageView ivRank;
    @BindView(R.id.tv_rank)
    CustomTextviewFonts tvRank;
    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    private ProfilePagerAdapter profilePagerAdapter;
    private PopupWindow popup, logoutPopup;
    private FirebaseAuth firebaseAuth;

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
        createLogoutMenuPopup();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // TODO: Testing create new user

        if (firebaseUser != null) {
            MyApplication.getInstance().getUserHelper().getUsersInfo(firebaseUser.getUid(), new UserHelper.GetUserInfo() {
                @Override
                public void onRead(Map<String, Object> user) {
                    if(user==null){
                        EventBus.getDefault().post(new LogoutEvent());
                    }
                    User newUser = AppUtils.convertMapToUser(user);
                    MyApplication.user = newUser;
                    if(isAdded()) {
                        profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
                        viewPager.setAdapter(profilePagerAdapter);
                        showUserData(newUser);
                    }

                }
            });
        }

        // testing
    }

    private void showUserData(User user) {
        if (getActivity() == null) {
            return;
        }
        Glide.with(this)
                .load(MyApplication.user.getPhoto())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.ava_height) / 2)))
                .into(ivProfile);
        // https://lh3.googleusercontent.com/-yAI0WFsZPMA/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuclBXQ04KoBFO0BH0uSuH3qXsLukRw/s96-c/photo.jpg
        tvMissionCount.setText(user.getNumChallenger() + "");
        tvUsername.setText(user.getName());
        tvRankPoint.setText(user.getTotalPoints() + "");
        tvTimes.setText("Total workout time: " + user.getTotalTimes() + " minutes");
        showRankView(user.getTotalPoints());


    }

    private void showRankView(long totalPoints) {
        switch (Long.valueOf(totalPoints / 250).intValue()) {
            case 0:
                Glide.with(this)
                        .load(R.drawable.rank_rat_1)
                        .into(ivRank);
                tvRank.setText(R.string.rank_rat_1);
                break;
            case 1:
                Glide.with(this)
                        .load(R.drawable.rank_rat_1)
                        .into(ivRank);
                tvRank.setText(R.string.rank_rat_2);
                break;
            case 2:
                Glide.with(this)
                        .load(R.drawable.rank_rat_1)
                        .into(ivRank);
                tvRank.setText(R.string.rank_rat_3);
                break;
            case 3:
                Glide.with(this)
                        .load(R.drawable.rank_rat_1)
                        .into(ivRank);
                tvRank.setText(R.string.rank_rat_4);
                break;
            case 4:
                Glide.with(this)
                        .load(R.drawable.rank_buffalo)
                        .into(ivRank);
                tvRank.setText(R.string.rank_buffalo_1);
                break;
            case 5:
                Glide.with(this)
                        .load(R.drawable.rank_buffalo)
                        .into(ivRank);
                tvRank.setText(R.string.rank_buffalo_2);
                break;
            case 6:
                Glide.with(this)
                        .load(R.drawable.rank_buffalo)
                        .into(ivRank);
                tvRank.setText(R.string.rank_buffalo_3);
                break;
            case 7:
                Glide.with(this)
                        .load(R.drawable.rank_buffalo)
                        .into(ivRank);
                tvRank.setText(R.string.rank_buffalo_4);
                break;
            case 8:
                Glide.with(this)
                        .load(R.drawable.rank_tiger)
                        .into(ivRank);
                tvRank.setText(R.string.rank_tiger_1);
                break;
            case 9:
                Glide.with(this)
                        .load(R.drawable.rank_tiger)
                        .into(ivRank);
                tvRank.setText(R.string.rank_tiger_2);
                break;
            case 10:
                Glide.with(this)
                        .load(R.drawable.rank_tiger)
                        .into(ivRank);
                tvRank.setText(R.string.rank_tiger_3);
                break;
            case 11:
                Glide.with(this)
                        .load(R.drawable.rank_tiger)
                        .into(ivRank);
                tvRank.setText(R.string.rank_tiger_4);
                break;
            case 12:
                Glide.with(this)
                        .load(R.drawable.rank_dragon)
                        .into(ivRank);
                tvRank.setText(R.string.rank_dragon_1);
                break;
            case 13:
                Glide.with(this)
                        .load(R.drawable.rank_dragon)
                        .into(ivRank);
                tvRank.setText(R.string.rank_dragon_2);
                break;
            case 14:
                Glide.with(this)
                        .load(R.drawable.rank_dragon)
                        .into(ivRank);
                tvRank.setText(R.string.rank_dragon_3);
                break;
            case 15:
                Glide.with(this)
                        .load(R.drawable.rank_dragon)
                        .into(ivRank);
                tvRank.setText(R.string.rank_dragon_4);
                break;
        }
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
        popup.showAsDropDown(btnAdd, -72, -btnAdd.getHeight() - 300, Gravity.TOP | Gravity.START);
    }

    @OnClick(R.id.iv_setting)
    public void onClickSetting() {
        logoutPopup.showAsDropDown(ivSetting, 0, 0);
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

    public void createLogoutMenuPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_logout, null);
        logoutPopup = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        View btnLogout = view.findViewById(R.id.btn_sign_up);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutPopup.dismiss();
                EventBus.getDefault().post(new LogoutEvent());
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
