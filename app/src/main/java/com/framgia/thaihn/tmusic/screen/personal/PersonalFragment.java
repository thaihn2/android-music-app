package com.framgia.thaihn.tmusic.screen.personal;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment implements PersonalContract.View {

    private PersonalContract.Presenter mPresenter;

    private PersonalAdapter mPersonalAdapter;
    private RecyclerView mRecycleMusicOffline;

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        mRecycleMusicOffline = rootView.findViewById(R.id.recycle_offline);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mPresenter = new PersonalPresenter();
        mPresenter.setView(this);
        configRecycle(getActivity());
        mPresenter.loadAllMusicOffline();
    }

    @Override
    public void showDataOffline(List<Song> list) {
        if (list == null) return;
        mPersonalAdapter.setSongs(list);
        mPersonalAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDataError(String message) {
        ToastUtils.quickToast(getActivity(),
                getString(R.string.error_get_song_from_sd_card), Toast.LENGTH_SHORT);
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    private void configRecycle(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false);
        mRecycleMusicOffline.setHasFixedSize(true);
        mPersonalAdapter = new PersonalAdapter(new ArrayList<Song>());
        mRecycleMusicOffline.setLayoutManager(layoutManager);
        mRecycleMusicOffline.setAdapter(mPersonalAdapter);
        mPersonalAdapter.notifyDataSetChanged();
    }
}
