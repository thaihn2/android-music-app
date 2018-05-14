package com.framgia.thaihn.tmusic.screen.genre;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreFragment extends BaseFragment implements
        GenreContract.View,
        GenreAdapter.OnClickListenerGenre {

    private ProgressBar mProgressHome;
    private RecyclerView mRecycleAllMusic;
    private GenreContract.Presenter mPresenter;
    private GenreAdapter mGenreAdapter;
    private OnItemSongListener mOnItemSongListener;

    public GenreFragment() {
        // Required empty public constructor
    }

    public static GenreFragment newInstance() {
        GenreFragment genreFragment = new GenreFragment();
        return genreFragment;
    }

    public void setOnItemSongListener(OnItemSongListener onItemSongListener) {
        mOnItemSongListener = onItemSongListener;
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
        mPresenter = new GenrePresenter();
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
    public void onItemClicked(int positionGenre, int position) {
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra(Constants.BUNDLE_POSITION_SONG, position);
        intent.setAction(MusicService.INTENT_ACTION_START_MUSIC);
        List<Song> list = mGenreAdapter.getGenres().get(positionGenre).getSongs();
        intent.putParcelableArrayListExtra(Constants.BUNDLE_LIST_MUSIC_PLAY, (ArrayList<? extends Parcelable>) list);
        getActivity().startService(intent);
        Intent intentDetail = new Intent(getActivity(), DetailActivity.class);
        startActivity(intentDetail);
    }

    @Override
    public void onMoreClicked(int positionGenre, int position) {
    }

    @Override
    public void onGenreClicked(int positionGenre) {
    }

    public interface OnItemSongListener {
    }
}
