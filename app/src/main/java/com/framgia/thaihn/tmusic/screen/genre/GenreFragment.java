package com.framgia.thaihn.tmusic.screen.genre;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Genre;
import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.detail.DetailActivity;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreFragment extends BaseFragment implements SongContract.View,
        GenreAdapter.OnClickListenerGenre {

    private ProgressBar mProgressHome;
    private RecyclerView mRecycleAllMusic;
    private SongContract.Presenter mPresenter;
    private GenreAdapter mGenreAdapter;

    public GenreFragment() {
        // Required empty public constructor
    }

    public static GenreFragment newInstance() {
        GenreFragment genreFragment = new GenreFragment();
        return genreFragment;
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
        mPresenter.loadAllMusic(Constants.DEFAULT_LIMIT, Constants.DEFAULT_OFFSET);
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
    public void showData(int position, List<Song> list) {
        mRecycleAllMusic.setVisibility(View.VISIBLE);
        mGenreAdapter.getGenres().add(new Genre(GenreSong.getGenres()[position], list));
        mGenreAdapter.notifyItemInserted(position);
    }

    @Override
    public void showError(String message) {
        ToastUtils.quickToast(getActivity(), message, Toast.LENGTH_SHORT);
    }

    private void configRecycle(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false);
        mRecycleAllMusic.setHasFixedSize(true);
        mGenreAdapter = new GenreAdapter(new ArrayList<Genre>());
        mGenreAdapter.setOnClickListenerGenre(this);
        mRecycleAllMusic.setLayoutManager(layoutManager);
        mRecycleAllMusic.setAdapter(mGenreAdapter);
        mGenreAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Song song, int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMoreClicked(Song song, int position) {
    }

    @Override
    public void onGenreClicked(int positionGenre) {
    }
}
