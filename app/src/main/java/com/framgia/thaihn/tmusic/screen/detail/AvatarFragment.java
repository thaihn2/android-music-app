package com.framgia.thaihn.tmusic.screen.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.framgia.thaihn.tmusic.BaseFragment;
import com.framgia.thaihn.tmusic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvatarFragment extends BaseFragment {

    private ImageView mImageAvatar;

    public AvatarFragment() {
        // Required empty public constructor
    }

    public static AvatarFragment newInstance() {
        AvatarFragment avatarFragment = new AvatarFragment();
        return avatarFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_avatar;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        mImageAvatar = rootView.findViewById(R.id.image_avatar);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
    }

    public void loadImage(String url) {
        Glide.with(mImageAvatar.getContext())
                .load(url)
                .into(mImageAvatar);
    }
}
