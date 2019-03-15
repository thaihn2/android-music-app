package com.framgia.thaihn.tmusic.screen.more;

import com.framgia.thaihn.tmusic.BasePresenter;
import com.framgia.thaihn.tmusic.BaseView;
import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

public interface MoreContract {

    interface View extends BaseView {
        void showProgress();

        void hideProgress();

        void showData(List<Song> list);

        void showError(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void loadMusic(int position, int limit, int offset);
    }
}
