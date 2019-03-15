package com.framgia.thaihn.tmusic.screen.personal;

import com.framgia.thaihn.tmusic.BasePresenter;
import com.framgia.thaihn.tmusic.BaseView;
import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

public interface PersonalContract {

    interface View extends BaseView {

        void showDataOffline(List<Song> list);

        void showDataError(String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter<View> {
        void loadAllMusicOffline();
    }
}
