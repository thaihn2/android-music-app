package com.framgia.thaihn.tmusic.screen.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> ls_fm = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ls_fm.get(position);
    }

    @Override
    public int getCount() {
        return ls_fm.size();
    }

    public void addFragment(Fragment fragment) {
        ls_fm.add(fragment);
    }
}
