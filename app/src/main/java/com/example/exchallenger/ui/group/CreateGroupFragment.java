package com.example.exchallenger.ui.group;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.helpers.GroupHelper;
import com.example.exchallenger.models.ChallengeItem;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.ui.challenge.ChallengeAdapter;
import com.example.exchallenger.ui.challenge.CreateChallengeDialog;
import com.example.exchallenger.utils.AppUtils;
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

    @BindView(R.id.text3)
    TextView tvAdmin;
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
        tvAdmin.setVisibility(View.VISIBLE);
        challengeAdapter = new ChallengeAdapter(getContext(), true);
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
                    showCreateChallengeDialog(null, -1);
                }, Throwable::printStackTrace);

        edtGroupName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AppUtils.hideKeyboard(getContext(), edtGroupName);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 0) {//edit
            showCreateChallengeDialog(challengeAdapter.getDataItem(item.getGroupId()), item.getGroupId());
            return true;
        }

        if (item.getItemId() == 1) {//delete
            new AlertDialog.Builder(getContext())
                    .setMessage("Do you really want to delete this challenge?")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            challengeAdapter.getDataList().remove(item.getGroupId());
                            challengeAdapter.notifyItemRemoved(item.getGroupId());
                            if (challengeAdapter.getDataList().size() == 0) {
                                btnDone.setEnabled(false);
                            }
                            dialog.dismiss();
                        }
                    })
                    .show();

            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void showCreateChallengeDialog(ChallengeItem challengeItem, int pos) {
        CreateChallengeDialog createChallengeDialog = CreateChallengeDialog.newInstance(challengeItem, pos,
                new CreateChallengeDialog.OnSubmitListener() {
                    @Override
                    public void onSubmit(int pos, ChallengeItem challengeItem) {
                        if (pos < 0) {
                            challengeAdapter.addItem(challengeItem);
                            if (!edtGroupName.getText().toString().trim().isEmpty()) {
                                btnDone.setEnabled(true);
                            }
                        } else {
                            challengeAdapter.getDataList().set(pos, challengeItem);
                            challengeAdapter.notifyItemChanged(pos);
                        }
                    }
                });
        createChallengeDialog.show(getChildFragmentManager(), null);
    }

    private void createGroup() {
//        MyApplication.getInstance().getGroupHelper().getGroups();
        btnDone.setEnabled(false);
        MyApplication.getInstance().getGroupHelper().createGroup(edtGroupName.getText().toString().trim(),
                challengeAdapter.getDataList(), new GroupHelper.CreateGroupListener() {
                    @Override
                    public void onCreateSuccess(String groupId, String key) {
                        CreateGroupSuccessDialog.newInstance(groupId, key,
                                new CreateGroupSuccessDialog.OnSubmitListener() {
                                    @Override
                                    public void onSubmit() {
                                        btnDone.postDelayed(() -> {
                                                    getActivity().onBackPressed();
                                                },
                                                500);
                                    }
                                }).show(getChildFragmentManager(), null);
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        btnDone.setEnabled(true);
                    }
                });
    }
}
