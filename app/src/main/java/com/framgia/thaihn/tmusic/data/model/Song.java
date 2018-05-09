package com.framgia.thaihn.tmusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String mArtworkUrl;
    private int mCommentCount;
    private String mDescription;
    private boolean mDownloadable;
    private String mDownloadUrl;
    private int mDuration;
    private String mGenre;
    private int mId;
    private String mKind;
    private int mLikesCount;
    private String mPermalinkUrl;
    private int mPlaybackCount;
    private String mTitle;
    private String mUri;
    private int mUserId;
    private boolean mPublic;
    private String mAvatarUrl;
    private String mUsername;

    public Song() {
    }

    protected Song(Parcel in) {
        mArtworkUrl = in.readString();
        mCommentCount = in.readInt();
        mDescription = in.readString();
        mDownloadable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        mDuration = in.readInt();
        mGenre = in.readString();
        mId = in.readInt();
        mKind = in.readString();
        mLikesCount = in.readInt();
        mPermalinkUrl = in.readString();
        mPlaybackCount = in.readInt();
        mTitle = in.readString();
        mUri = in.readString();
        mUserId = in.readInt();
        mPublic = in.readByte() != 0;
        mAvatarUrl = in.readString();
        mUsername = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mDownloadable = downloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    public String getPermalinkUrl() {
        return mPermalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        mPermalinkUrl = permalinkUrl;
    }

    public int getPlaybackCount() {
        return mPlaybackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        mPlaybackCount = playbackCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public boolean isPublic() {
        return mPublic;
    }

    public void setPublic(boolean aPublic) {
        mPublic = aPublic;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mArtworkUrl);
        dest.writeInt(mCommentCount);
        dest.writeString(mDescription);
        dest.writeByte((byte) (mDownloadable ? 1 : 0));
        dest.writeString(mDownloadUrl);
        dest.writeInt(mDuration);
        dest.writeString(mGenre);
        dest.writeInt(mId);
        dest.writeString(mKind);
        dest.writeInt(mLikesCount);
        dest.writeString(mPermalinkUrl);
        dest.writeInt(mPlaybackCount);
        dest.writeString(mTitle);
        dest.writeString(mUri);
        dest.writeInt(mUserId);
        dest.writeByte((byte) (mPublic ? 1 : 0));
        dest.writeString(mAvatarUrl);
        dest.writeString(mUsername);
    }
}
