package com.framgia.thaihn.tmusic.screen.personal;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.repository.SongRepository;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;

import java.util.List;

public class PersonalPresenter implements PersonalContract.Presenter,
        SongDataSource.OnGetDataLocalListener<Song> {

    private PersonalContract.View mView;
    private SongRepository mSongRepository;

    public PersonalPresenter() {
        mSongRepository = SongRepository.getInstance();
    }

    @Override
    public void loadAllMusicOffline() {
        mView.showProgress();
        mSongRepository.getAllSongLocal(this);
    }

    @Override
    public void setView(PersonalContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onGetDataLocalSuccess(List<Song> list) {
        mView.hideProgress();
        mView.showDataOffline(list);
    }

    @Override
    public void onGetDataLocalError(String message) {
        mView.hideProgress();
        mView.showDataError(message);
    }
}
