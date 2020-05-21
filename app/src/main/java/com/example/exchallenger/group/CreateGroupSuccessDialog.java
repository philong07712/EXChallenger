package com.example.exchallenger.group;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.R;
import com.utilityview.customview.CustomTextviewFonts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateGroupSuccessDialog extends DialogFragment {

    public static final String K_CODE = "code";
    public static final String K_ID = "group_id";
    @BindView(R.id.tv_code)
    CustomTextviewFonts tvCode;
    private Unbinder mUnBinder;
    private String groupCode;


    public static CreateGroupSuccessDialog newInstance(String groupId, String groupCode, OnSubmitListener onSubmitListener) {
        CreateGroupSuccessDialog dialog = new CreateGroupSuccessDialog();
        Bundle args = new Bundle();
        args.putString(K_CODE, groupCode);
        args.putString(K_ID, groupId);
        dialog.setArguments(args);
        dialog.setOnSubmitListener(onSubmitListener);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_group_success, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp();
    }


    public void setUp() {
        groupCode = getArguments().getString(K_CODE);
        tvCode.setText(groupCode);
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    private OnSubmitListener onSubmitListener;

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    @OnClick(R.id.btn_ok)
    public void onBtnOkClicked() {
        if(onSubmitListener!=null){
            onSubmitListener.onSubmit();
        }
        dismiss();
    }

    @OnClick(R.id.btn_copy)
    public void onBtnCopyClicked() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("group_code", groupCode);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied code to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnSubmitListener {
        void onSubmit();
    }
}
