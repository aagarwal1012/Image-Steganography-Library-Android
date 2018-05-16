package com.ayush.steganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TextEncode extends AsyncTask<TextStegnography, Integer, TextStegnography> {

    private static String TAG = TextEncode.class.getName();

    private int maximumProgress;

    private Activity activity;

    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TextStegnography textStegnography) {
        super.onPostExecute(textStegnography);
    }

    @Override
    protected TextStegnography doInBackground(TextStegnography... textStegnographies) {
        return null;
    }
}
