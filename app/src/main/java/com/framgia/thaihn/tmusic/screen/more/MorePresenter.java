package com.framgia.thaihn.tmusic.screen.more;

import com.framgia.thaihn.tmusic.data.repository.SongRepository;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;

import java.util.List;

public class MorePresenter implements MoreContract.Presenter,
        SongDataSource.OnFetchDataListener {

    private MoreContract.View mView;
    private SongRepository mSongRepository;

    public MorePresenter() {
        mSongRepository = SongRepository.getInstance();
    }

    @Override
    public void loadMusic(int position, int limit, int offset) {
        mSongRepository.getSongsRemote(position, limit, offset, this);
        mView.showProgress();
    }

    @Override
    public void setView(MoreContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onFetchDataSuccess(int position, List list) {
        mView.hideProgress();
        mView.showData(list);
    }

    @Override
    public void onFetchDataError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }
}
