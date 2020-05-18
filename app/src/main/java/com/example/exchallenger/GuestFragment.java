package com.example.exchallenger;

import com.example.exchallenger.base.BaseFragment;

public class GuestFragment extends BaseFragment {
    public static GuestFragment newInstance() {
        GuestFragment guestFragment = new GuestFragment();
        return guestFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guest;
    }

    @Override
    protected void setUp() {

    }
}
