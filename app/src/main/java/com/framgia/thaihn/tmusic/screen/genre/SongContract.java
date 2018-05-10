package com.framgia.thaihn.tmusic.screen.genre;

import com.framgia.thaihn.tmusic.BasePresenter;
import com.framgia.thaihn.tmusic.BaseView;
import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

public interface SongContract {

    interface View extends BaseView {

        void showProgress();

        void hideProgress();

        void showData(int position, List<Song> list);

        void showError(String message);
    }

    interface Presenter extends BasePresenter<SongContract.View> {
        void loadMusic(int position, int limit, int offset);

        void loadAllMusic(int limit, int offset);
    }
}
