package com.framgia.thaihn.tmusic.screen.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends BaseFragment {


    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {

    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

}
