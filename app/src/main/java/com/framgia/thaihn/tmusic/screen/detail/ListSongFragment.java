package com.framgia.thaihn.tmusic.screen.detail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.personal.PersonalAdapter;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSongFragment extends BaseFragment implements
        PersonalAdapter.OnPersonalClick {

    private RecyclerView mRecycle;
    private PersonalAdapter mAdapter;
    private ProgressBar mProgressBar;

    public ListSongFragment() {
        // Required empty public constructor
    }

    public static ListSongFragment newInstance() {
        ListSongFragment listSongFragment = new ListSongFragment();
        return listSongFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        mRecycle = rootView.findViewById(R.id.recycle_song);
        mProgressBar = rootView.findViewById(R.id.progress);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        configRecycle(getActivity());
    }

    @Override
    public void onPersonalClicked(int position) {
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra(Constants.BUNDLE_POSITION_SONG, position);
        intent.setAction(MusicService.INTENT_ACTION_START_MUSIC);
        intent.putParcelableArrayListExtra(Constants.BUNDLE_LIST_MUSIC_PLAY,
                (ArrayList<? extends Parcelable>) mAdapter.getSongs());
        getActivity().startService(intent);
    }

    @Override
    public void onMoreClicked(int position) {

    }

    private void configRecycle(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false);
        mRecycle.setHasFixedSize(true);
        mAdapter = new PersonalAdapter(new ArrayList<Song>());
        mAdapter.setOnPersonalClick(this);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void loadSongs(List<Song> list) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setSongs(list);
        mAdapter.notifyDataSetChanged();
    }
}
