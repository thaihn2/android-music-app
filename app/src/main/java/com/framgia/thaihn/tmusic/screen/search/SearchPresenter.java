package com.framgia.thaihn.tmusic.screen.search;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.repository.SongRepository;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.util.StringUtils;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter,
        SongDataSource.OnGetDataListener<Song> {

    private SearchContract.View mView;
    private SongRepository mSongRepository;

    public SearchPresenter() {
        mSongRepository = SongRepository.getInstance();
    }

    @Override
    public void searchSong(int limit, int offset, String key) {
        mView.showProgress();
        mSongRepository.searchSong(StringUtils.createUrlSearch(key, limit), this);
    }

    @Override
    public void setView(SearchContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onGetDataSuccess(List<Song> list) {
        mView.hideProgress();
        mView.showData(list);
    }

    @Override
    public void onGetDataError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }
}
