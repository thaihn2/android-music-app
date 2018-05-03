package com.framgia.thaihn.tmusic;

public interface BasePresenter<T> {

    void setView(T view);

    void onStart();

    void onStop();
}
