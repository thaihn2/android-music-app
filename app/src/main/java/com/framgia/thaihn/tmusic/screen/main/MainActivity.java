package com.framgia.thaihn.tmusic.screen.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.screen.genre.GenreFragment;
import com.framgia.thaihn.tmusic.util.FragmentUtils;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomTabMain;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mBottomTabMain = findViewById(R.id.bottom_navigation);
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomTabMain.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_persian_green)));
        changeColorStatusBar();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mBottomTabMain.setSelectedItemId(R.id.menu_home);
        switchTab(mBottomTabMain.getSelectedItemId());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switchTab(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return true;
    }

    /**
     * Switch tab
     *
     * @param id
     */
    private void switchTab(int id) {
        switch (id) {
            case R.id.menu_home: {
                FragmentUtils.replaceFragmentNotStack(
                        this,
                        GenreFragment.newInstance(),
                        R.id.frame_main);
                break;
            }
            case R.id.menu_personal: {
                FragmentUtils.replaceFragmentNotStack(
                        this,
                        PersonalFragment.newInstance(),
                        R.id.frame_main);
                break;
            }
        }
    }

    /**
     * Change color of status bar
     */
    private void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_aqua_deep));
        }
    }
}
