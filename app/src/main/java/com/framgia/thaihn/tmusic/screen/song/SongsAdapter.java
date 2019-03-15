package com.framgia.thaihn.tmusic.screen.song;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

public class SongsAdapter
        extends RecyclerView.Adapter<SongsAdapter.ItemViewHolder> {

    private List<Song> mSongs;
    private OnClickListenerSong mOnClickListenerSong;

    public void setOnClickListenerSong(OnClickListenerSong onClickListenerSong) {
        mOnClickListenerSong = onClickListenerSong;
    }

    public SongsAdapter(List<Song> songs) {
        this.mSongs = songs;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new ItemViewHolder(layoutItem, mOnClickListenerSong);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.setData(mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs != null ? mSongs.size() : 0;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAvatarSong, mIconMore;
        private TextView mTextNameSong, mTextSingerSong;
        private OnClickListenerSong mClickListenerSong;

        ItemViewHolder(View rootView, OnClickListenerSong listener) {
            super(rootView);
            mAvatarSong = rootView.findViewById(R.id.image_avatar);
            mTextNameSong = rootView.findViewById(R.id.text_name);
            mTextSingerSong = rootView.findViewById(R.id.text_singer);
            mIconMore = rootView.findViewById(R.id.image_icon_more);
            mClickListenerSong = listener;
            mIconMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
            mTextNameSong.setSelected(true);
        }

        public void setData(Song song) {
            if (song != null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_music_player_large)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);
                Glide.with(mAvatarSong.getContext())
                        .applyDefaultRequestOptions(options)
                        .load(song.getArtworkUrl())
                        .into(mAvatarSong);
                mTextNameSong.setText(song.getTitle());
                mTextSingerSong.setText(song.getUsername());
            }
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mClickListenerSong.onItemClicked(getAdapterPosition());
            } else if (v == mIconMore) {
                mClickListenerSong.onMoreClicked(getAdapterPosition());
            }
        }
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        if (songs != null) {
            this.mSongs = songs;
        }
    }

    public interface OnClickListenerSong {
        void onMoreClicked(int position);

        void onItemClicked(int position);
    }
}
