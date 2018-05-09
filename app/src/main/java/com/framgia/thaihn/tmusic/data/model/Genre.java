package com.framgia.thaihn.tmusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Genre implements Parcelable {
    private String mTitle;
    private List<Song> mSongs;

    public Genre(String title, List<Song> songs) {
        mTitle = title;
        mSongs = songs;
    }

    public Genre(Parcel in) {
        mTitle = in.readString();
        mSongs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeTypedList(mSongs);
    }
}
