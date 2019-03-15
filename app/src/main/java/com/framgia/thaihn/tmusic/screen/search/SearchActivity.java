package com.framgia.thaihn.tmusic.screen.search;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchContract.View,
        SearchView.OnQueryTextListener,
        SearchAdapter.OnClickListenerSong {

    private SearchContract.Presenter mPresenter;
    private SearchAdapter mSearchAdapter;

    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private TextView mTextNotification;
    private RecyclerView mRecyclerSearch;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_search;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mProgressBar = findViewById(R.id.progress);
        mTextNotification = findViewById(R.id.text_notification);
        mRecyclerSearch = findViewById(R.id.recycle_search);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_persian_green)));
        mPresenter = new SearchPresenter();
        mPresenter.setView(this);
        configRecycle(this);
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
    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_aqua_deep));
        }
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerSearch.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(List<Song> list) {
        mSearchAdapter.addData(list);
        if (mSearchAdapter.getItemCount() == 0) {
            showError(Constants.ERROR_NO_DATA);
        } else {
            mTextNotification.setVisibility(View.GONE);
            mRecyclerSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        mTextNotification.setVisibility(View.VISIBLE);
        mTextNotification.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_main, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) itemSearch.getActionView();
        configSearchView();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (Utils.checkNetwork(this)) {
            if (!TextUtils.isEmpty(newText)) {
                mPresenter.searchSong(Constants.DEFAULT_LIMIT_SEARCH,
                        Constants.DEFAULT_OFFSET, newText);
            }
        } else {
            showError(getString(R.string.error_no_internet));
        }
        return true;
    }

    private void configRecycle(Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        mRecyclerSearch.setHasFixedSize(true);
        mSearchAdapter = new SearchAdapter(new ArrayList<Song>());
        mSearchAdapter.setOnClickListenerSong(this);
        mRecyclerSearch.setLayoutManager(layoutManager);
        mRecyclerSearch.setAdapter(mSearchAdapter);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreClicked(int position) {
    }

    @Override
    public void onItemClicked(int position) {
        gotoPlayMusic(position, mSearchAdapter.getSongs());
    }

    private void configSearchView() {
        mSearchView.setQueryHint(getString(R.string.str_hint_search_view));
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();
        mSearchView.setOnQueryTextListener(this);
    }
}
