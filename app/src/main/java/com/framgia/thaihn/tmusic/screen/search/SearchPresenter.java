package com.framgia.thaihn.tmusic.screen.search;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.repository.SearchRepository;
import com.framgia.thaihn.tmusic.data.source.SearchDataSource;
import com.framgia.thaihn.tmusic.util.Utils;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter,
        SearchDataSource.OnSearchDataListener<Song> {

    private SearchContract.View mView;
    private SearchRepository mSearchRepository;

    public SearchPresenter() {
        mSearchRepository = SearchRepository.getInstance();
    }

    @Override
    public void searchSong(int limit, int offset, String key) {
        mView.showProgress();
        mSearchRepository.searchSong(Utils.createUrlSearch(key, limit), this);
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
    public void onSearchDataSuccess(List<Song> list) {
        mView.hideProgress();
        mView.showData(list);
    }

    @Override
    public void onSearchDataError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }
}
