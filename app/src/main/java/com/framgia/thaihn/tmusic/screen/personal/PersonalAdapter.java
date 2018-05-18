package com.framgia.thaihn.tmusic.screen.personal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.widget.CircleImageView;

import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ViewHolder> {

    private List<Song> mSongs;

    public PersonalAdapter(List<Song> list) {
        this.mSongs = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_offline, parent, false);
        PersonalAdapter.ViewHolder viewHolder = new PersonalAdapter.ViewHolder(layoutItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextTitle, mTextSinger;
        private CircleImageView mImageAvatar;
        private ImageView mImageMore;

        public ViewHolder(View view) {
            super(view);
            mTextTitle = view.findViewById(R.id.text_name);
            mTextSinger = view.findViewById(R.id.text_singer);
            mImageAvatar = view.findViewById(R.id.image_avatar_offline);
            mImageMore = view.findViewById(R.id.image_more);
            mImageMore.setOnClickListener(this);
        }

        public void setData(Song song) {
            if (song == null) return;
            mTextTitle.setText(song.getTitle());
            mTextSinger.setText(song.getUsername());
        }

        @Override
        public void onClick(View v) {
        }
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }
}
