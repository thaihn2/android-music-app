package com.framgia.thaihn.tmusic.screen.search;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    private List<Song> mSongs;
    private SearchAdapter.OnClickListenerSong mOnClickListenerSong;

    public SearchAdapter(List<Song> songs) {
        this.mSongs = songs;
    }

    public void setOnClickListenerSong(SearchAdapter.OnClickListenerSong onClickListenerSong) {
        mOnClickListenerSong = onClickListenerSong;
    }

    @NonNull
    @Override
    public SearchAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new SearchAdapter.ItemViewHolder(layoutItem, mOnClickListenerSong);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ItemViewHolder holder, final int position) {
        holder.setData(mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs != null ? mSongs.size() : 0;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAvatarSong, mIconMore;
        private TextView mTextNameSong, mTextSingerSong;
        private SearchAdapter.OnClickListenerSong mClickListenerSong;
        private ConstraintLayout itemView;

        ItemViewHolder(View rootView, SearchAdapter.OnClickListenerSong listener) {
            super(rootView);
            mAvatarSong = rootView.findViewById(R.id.image_avatar);
            mTextNameSong = rootView.findViewById(R.id.text_name);
            mTextSingerSong = rootView.findViewById(R.id.text_singer);
            mIconMore = rootView.findViewById(R.id.image_icon_more);
            itemView = rootView.findViewById(R.id.itemView);
            mClickListenerSong = listener;
            mIconMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
            mTextNameSong.setSelected(true);
        }

        public void setData(Song song) {
            if (song != null) {
                Glide.with(mAvatarSong.getContext())
                        .load(song.getArtworkUrl())
                        .into(mAvatarSong);
                mTextNameSong.setText(song.getTitle());
                mTextSingerSong.setText(song.getUsername());
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_icon_more: {
                    mClickListenerSong.onMoreClicked(getAdapterPosition());
                    break;
                }
                case R.id.itemView: {
                    mClickListenerSong.onItemClicked(getAdapterPosition());
                    break;
                }
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

    public void addData(List<Song> list) {
        mSongs.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        mSongs.clear();
        notifyDataSetChanged();
    }

    public interface OnClickListenerSong {
        void onMoreClicked(int position);

        void onItemClicked(int position);
    }
}
