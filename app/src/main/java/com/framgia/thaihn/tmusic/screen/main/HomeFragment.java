package com.framgia.thaihn.tmusic.screen.main;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements SongContract.View {

    public static final String FRAGMENT_STACK_HOME = "HOME_FRAGMENT";

    private ProgressBar mProgressHome;
    private RecyclerView mRecycleAllMusic;
    private SongContract.Presenter mPresenter;
    private SongsAdapter mSongAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        mRecycleAllMusic = rootView.findViewById(R.id.recycle_all_music);
        mProgressHome = rootView.findViewById(R.id.progress_home);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mPresenter = new SongPresenter();
        mPresenter.setView(this);
        configRecycle(getActivity());
        mPresenter.loadMusic(Constants.GENRE_ALL_MUSIC, Constants.DEFAULT_LIMIT, Constants.DEFAULT_OFFSET);
    }

    @Override
    public void showProgress() {
        mProgressHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressHome.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Song> list) {
        mRecycleAllMusic.setVisibility(View.VISIBLE);
        mSongAdapter.setSongs(list);
        mSongAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        ToastUtils.quickToast(getActivity(), message, Toast.LENGTH_SHORT);
    }

    private void configRecycle(Context context) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecycleAllMusic.setHasFixedSize(true);
        mSongAdapter = new SongsAdapter(context, new ArrayList<Song>());
        mRecycleAllMusic.setLayoutManager(layoutManager);
        mRecycleAllMusic.setAdapter(mSongAdapter);
        mSongAdapter.notifyDataSetChanged();
    }
}
