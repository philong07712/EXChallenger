package com.example.exchallenger.group;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.challenge.ChallengeAdapter;
import com.example.exchallenger.challenge.CreateChallengeDialog;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.utilityview.customview.CustomButtonFonts;
import com.utilityview.customview.CustomEditTextFonts;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateGroupFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt_group_name)
    CustomEditTextFonts edtGroupName;
    @BindView(R.id.btn_add)
    CustomTextviewFonts btnAdd;
    @BindView(R.id.rcv_challenge)
    RecyclerView rcvChallenge;
    @BindView(R.id.btn_done)
    CustomButtonFonts btnDone;
    private ChallengeAdapter challengeAdapter;

    public static CreateGroupFragment newInstance() {
        return new CreateGroupFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_group;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void setUp() {
        challengeAdapter = new ChallengeAdapter(getContext());
        rcvChallenge.setAdapter(challengeAdapter);
        rcvChallenge.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        RxTextView.afterTextChangeEvents(edtGroupName)
                .skipInitialValue()
                .map(event -> event.getEditable().toString().trim().isEmpty())
                .subscribe(empty -> btnDone.setEnabled(challengeAdapter.getItemCount() > 0 && !empty),
                        Throwable::printStackTrace);

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    getActivity().onBackPressed();
                }, Throwable::printStackTrace);

        RxView.clicks(btnDone).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    createGroup();
                }, Throwable::printStackTrace);

        RxView.clicks(btnAdd).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    showCreateChallengeDialog();
                }, Throwable::printStackTrace);

    }

    private void showCreateChallengeDialog() {
        CreateChallengeDialog createChallengeDialog = CreateChallengeDialog.newInstance(new CreateChallengeDialog.OnSubmitListener() {
            @Override
            public void onSubmit(ChallengeItem challengeItem) {
                challengeAdapter.addItem(challengeItem);
                if (!edtGroupName.getText().toString().trim().isEmpty()) {
                    btnDone.setEnabled(true);
                }
            }
        });
        createChallengeDialog.show(getChildFragmentManager(), null);
    }

    private void createGroup() {
//        MyApplication.getInstance().getGroupHelper().getGroups();

        getActivity().onBackPressed();
    }
}
