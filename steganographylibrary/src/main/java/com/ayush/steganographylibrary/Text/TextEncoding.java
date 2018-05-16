package com.ayush.steganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.ayush.steganographylibrary.Utils.Crypto;
import com.ayush.steganographylibrary.Utils.Utility;

public class TextEncoding extends AsyncTask<TextStegnography, Integer, TextStegnography> {

    private static String TAG = TextEncoding.class.getName();

    private int maximumProgress;

    private Activity activity;

    private ProgressDialog progressDialog;

    public TextEncoding(Activity activity) {
        super();
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

        TextStegnography result = null;

        maximumProgress = 0;

        if (textStegnographies.length > 0){

            TextStegnography textStegnography = textStegnographies[0];

            //Encrypt
            if (Utility.isStringEmpty(textStegnography.getSecret_key()))
            {
                try{
                    Crypto encryption = new Crypto(textStegnography.getMessage(), textStegnography.getSecret_key());
                    textStegnography.setEncrypted_zip(encryption.getEncrypted_zip());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        return null;
    }
}
