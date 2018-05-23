package com.framgia.thaihn.tmusic.screen.more;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.search.SearchAdapter;
import com.framgia.thaihn.tmusic.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends BaseActivity implements MoreContract.View,
        SearchAdapter.OnClickListenerSong {

    private int mIdGenre;
    private MoreContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecycle;
    private SearchAdapter mAdapter;
    private TextView mTextNotification;

    // TODO: show small player

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_more;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mProgressBar = findViewById(R.id.progress);
        mRecycle = findViewById(R.id.recycle_song);
        mTextNotification = findViewById(R.id.text_notification);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mIdGenre = getIntent().getIntExtra(Constants.EXTRAS_ID_GENRE, 0);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.color_persian_green)));
        getSupportActionBar().setTitle(GenreSong.getGenres()[mIdGenre].toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new MorePresenter();
        mPresenter.setView(this);
        configRecycle(this);
        mPresenter.loadMusic(mIdGenre, 100, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Song> list) {
        mAdapter.addData(list);
        if (mAdapter.getSongs().size() == 0) {
            showError(Constants.ERROR_NO_DATA);
        } else {
            mTextNotification.setVisibility(View.GONE);
            mRecycle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        mTextNotification.setVisibility(View.VISIBLE);
        mTextNotification.setText(message);
    }

    private void configRecycle(Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        mRecycle.setHasFixedSize(true);
        mAdapter = new SearchAdapter(new ArrayList<Song>());
        mAdapter.setOnClickListenerSong(this);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreClicked(int position) {
    }

    @Override
    public void onItemClicked(int position) {
        gotoPlayMusic(position, mAdapter.getSongs());
    }

    @Override
    public void changeColorStatusBar() {
        super.changeColorStatusBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_aqua_deep));
        }
    }
}
