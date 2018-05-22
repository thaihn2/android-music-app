package com.framgia.thaihn.tmusic.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.screen.main.MainActivity;
import com.framgia.thaihn.tmusic.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadMusicService extends IntentService {

    public static final String END_OF_FILE_MUSIC = ".mp3";
    public static final String FOLDER_MUSIC = "/TMusic/";
    public static final int ID_NOTIFICATION = 111;
    public static final String CHANNEL_ID = "Download";

    public DownloadMusicService() {
        super(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        String urlSong = intent.getStringExtra(Constants.INTENT_URL_DOWNLOAD);
        String title = intent.getStringExtra(Constants.INTENT_TITLE_DOWNLOAD);
        DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
        downloadFileFromURL.execute(urlSong, title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showNotification(getString(R.string.str_downloading));
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            InputStream inputStream = null;

            try {
                url = new URL(params[0]);
                inputStream = url.openStream();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection != null) {
                    String fileName = params[1] + END_OF_FILE_MUSIC;
                    String rootPath = Environment.getExternalStorageDirectory().toString()
                            + FOLDER_MUSIC;
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File file = new File(rootPath + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    long total = 0;
                    int len1 = 0;
                    if (inputStream != null) {
                        while ((len1 = inputStream.read(buffer)) > 0) {
                            total += len1;
                            fileOutputStream.write(buffer, 0, len1);
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    return "";
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException ioe) {
                    // just going to ignore this one
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (file_url == null) {
                showNotification(getString(R.string.str_download_error));
            } else {
                showNotification(getString(R.string.str_download_complete));
            }
        }
    }

    private void showNotification(String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Constants.INTENT_ACTION_DOWNLOAD);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_to_storage_drive)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(ID_NOTIFICATION, mBuilder.build());

    }
}
