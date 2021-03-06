package com.example.exchallenger.ui.group;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exchallenger.helpers.GroupHelper;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.utilityview.customview.CustomButtonFonts;
import com.utilityview.customview.CustomEditTextFonts;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class JoinGroupFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt_code)
    CustomEditTextFonts edtCode;
    @BindView(R.id.btn_done)
    CustomButtonFonts btnDone;

    public static JoinGroupFragment newInstance() {
        return new JoinGroupFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_join_group;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void setUp() {
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

        edtCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
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
        GroupHelper groupHelper = MyApplication.getInstance().getGroupHelper();
        groupHelper.joinGroup(MyApplication.user.getUserID(), edtCode.getText().toString().trim(),
                new GroupHelper.GroupJoinListener() {
                    @Override
                    public void onSuccess() {
                        if(getActivity()!=null) {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onCancel(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
