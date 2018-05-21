package com.framgia.thaihn.tmusic.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;

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
            return "";
        }

        @Override
        protected void onPostExecute(String file_url) {
            // TODO show notification when done
        }
    }
}
