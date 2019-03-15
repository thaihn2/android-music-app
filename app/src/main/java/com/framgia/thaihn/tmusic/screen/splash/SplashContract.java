package com.framgia.thaihn.tmusic.screen.splash;

import com.framgia.thaihn.tmusic.BasePresenter;
import com.framgia.thaihn.tmusic.BaseView;

public interface SplashContract {

    interface View extends BaseView {

        void waitSuccess();

        void waitFail();
    }

    interface Presenter extends BasePresenter<View> {

        void waitScreen();
    }
}
