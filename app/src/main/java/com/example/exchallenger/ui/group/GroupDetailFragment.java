package com.example.exchallenger.ui.group;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.helpers.GroupHelper;
import com.example.exchallenger.models.Group;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding3.view.RxView;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GroupDetailFragment extends BaseFragment {
    private static final String K_GROUP = "group";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_group)
    CustomTextviewFonts tvGroup;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_code)
    CustomTextviewFonts tvCode;
    @BindView(R.id.tv_count)
    CustomTextviewFonts tvCount;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private GroupDetailPagerAdapter groupDetailPagerAdapter;
    private Group group;
    private PopupWindow popup;
    private boolean isAdmin;

    public static GroupDetailFragment newInstance(Group group) {
        GroupDetailFragment groupFragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        groupFragment.setArguments(args);
        args.putSerializable(K_GROUP, group);
        return groupFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_detail;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void setUp() {
        group = (Group) getArguments().getSerializable(K_GROUP);
        if (group == null) {
            getActivity().onBackPressed();
            return;
        }
        isAdmin = MyApplication.firebaseUser != null && MyApplication.firebaseUser.getUid().equals(group.getAdmin());
        createMenuPopup();
        tvGroup.setText(group.getName());
        tvCode.setText(group.getKey());
        tvCount.setText(group.getMembers() != null ? group.getMembers().size() + " members" : "");
        Glide.with(this)
                .load(group.getPhoto())
                .apply(
                        new RequestOptions()
                                .error(R.drawable.ic_group_default)
                                .transforms(new CenterCrop(),
                                        new RoundedCorners(getContext().getResources()
                                                .getDimensionPixelSize(R.dimen.ava_height) / 2))
                )
                .into(ivAvatar);
        boolean isAdmin = !TextUtils.isEmpty(group.getAdmin())
                && MyApplication.firebaseUser != null
                && group.getAdmin().equals(MyApplication.firebaseUser.getUid());
        groupDetailPagerAdapter = new GroupDetailPagerAdapter(getChildFragmentManager(), group.getGroupKey(), isAdmin);
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

    }

    public void createMenuPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_delete, null);
        popup = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        TextView btnLeave = view.findViewById(R.id.btn_delete);
        btnLeave.setText(isAdmin ? "Delete Group" : "Leave Group");
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                if (isAdmin) {
                    deleteGroup();
                } else {
                    leaveGroup();
                }

            }
        });

    }

    private void deleteGroup() {
        MyApplication.getInstance().getGroupHelper()
                .deleteGroup(group, new GroupHelper.CustomCompleteListener() {
                    @Override
                    public void onSuccess() {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void leaveGroup() {
        MyApplication.getInstance().getGroupHelper().leaveGroup(group, MyApplication.firebaseUser.getUid(), new GroupHelper.CustomCompleteListener() {
            @Override
            public void onSuccess() {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(String message) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_copy)
    public void onBtnCopyClicked() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("group_code", group.getKey());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied code to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.iv_setting)
    public void onClickSetting() {
        popup.showAsDropDown(ivSetting, 0, 0);
    }
}
