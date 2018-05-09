package com.framgia.thaihn.tmusic.screen.genre;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Genre;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.song.SongsAdapter;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> mGenres;
    private OnClickListenerGenre mOnClickListenerGenre;

    public void setOnClickListenerGenre(OnClickListenerGenre onClickListenerGenre) {
        mOnClickListenerGenre = onClickListenerGenre;
    }

    public GenreAdapter(List<Genre> list) {
        this.mGenres = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setData(mGenres.get(position));
    }

    @Override
    public int getItemCount() {
        return mGenres == null ? 0 : mGenres.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            SongsAdapter.OnClickListenerSong {

        private TextView mTextTitle;
        private RecyclerView mRecycleSong;
        private SongsAdapter mSongsAdapter;

        public ViewHolder(View rootView) {
            super(rootView);
            mTextTitle = rootView.findViewById(R.id.text_title_genre);
            mRecycleSong = rootView.findViewById(R.id.recycle_song);
            mSongsAdapter = new SongsAdapter(new ArrayList<Song>());
            initRecycleView(mRecycleSong, mSongsAdapter);
        }

        public void setData(Genre genre) {
            if (genre != null) {
                mTextTitle.setText(genre.getTitle().toUpperCase());
                mTextTitle.setOnClickListener(this);
                mSongsAdapter.setSongs(genre.getSongs());
                mSongsAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onClick(View v) {
            if (v == mTextTitle) {
                mOnClickListenerGenre.onGenreClicked(getAdapterPosition());
            }
        }

        @Override
        public void onMoreClicked(Song song, int position) {
            mOnClickListenerGenre.onMoreClicked(song, position);
        }

        @Override
        public void onItemClicked(Song song, int position) {
            mOnClickListenerGenre.onItemClicked(song, position);
        }

        private void initRecycleView(RecyclerView mRecycle, SongsAdapter adapter) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            mRecycle.setHasFixedSize(true);
            mRecycle.setLayoutManager(layoutManager);
            adapter.setOnClickListenerSong(this);
            mRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public List<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(List<Genre> genres) {
        mGenres = genres;
    }

    public interface OnClickListenerGenre {
        void onItemClicked(Song song, int position);

        void onMoreClicked(Song song, int position);

        void onGenreClicked(int positionGenre);
    }
}
