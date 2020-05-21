package com.example.exchallenger.ui.profile;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;

public class EmptyFragment extends BaseFragment {
    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void setUp() {

    }
}
