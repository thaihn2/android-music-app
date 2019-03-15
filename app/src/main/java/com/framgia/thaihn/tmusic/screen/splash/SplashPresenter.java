package com.framgia.thaihn.tmusic.screen.splash;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.framgia.thaihn.tmusic.util.Constants;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    @Override
    public void waitScreen() {
        threadWait.start();
    }

    /**
     * Thread for wait screen three seconds
     */
    private Thread threadWait = new Thread() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            try {
                sleep(Constants.SPLASH_SCREEN_DISPLAY_LENGTH);
            } catch (InterruptedException e) {
                e.printStackTrace();
                mView.waitFail();
            } finally {
                mView.waitSuccess();
            }
        }
    };


    @Override
    public void setView(SplashContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
