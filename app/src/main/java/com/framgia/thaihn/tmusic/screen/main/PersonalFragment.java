package com.framgia.thaihn.tmusic.screen.main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment {

    public static final String FRAGMENT_STACK_PERSONAL = "PERSONAL_FRAGMENT";

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
    }
}
