package com.ayush.steganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TextEncode extends AsyncTask<TextStegnography, Integer, TextStegnography> {

    private static String TAG = TextEncode.class.getName();

    private int maximumProgress;

    private Activity activity;

    private ProgressDialog progressDialog;

    public TextEncode(Activity activity) {
        this.activity = activity;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TextStegnography textStegnography) {
        super.onPostExecute(textStegnography);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected TextStegnography doInBackground(TextStegnography... textStegnographies) {
        return null;
    }
}
