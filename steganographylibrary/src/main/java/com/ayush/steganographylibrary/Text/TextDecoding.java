package com.ayush.steganographylibrary.Text;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ayush.steganographylibrary.Utils.Utility;

import java.io.File;
import java.util.List;

public class TextDecoding extends AsyncTask<File, Void, TextSteganography> {

    private final static String TAG = TextDecoding.class.getName();
    private ProgressDialog progressDialog;
    private boolean isImageDecoded;

    public TextDecoding() {
        super();
        isImageDecoded = false;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (progressDialog != null){
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.setTitle("Decoding Message");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
    }

    @Override
    protected void onPostExecute(TextSteganography textSteganography) {
        super.onPostExecute(textSteganography);

        if(progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        if(progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected TextSteganography doInBackground(File... files) {
        TextSteganography result = null;
        publishProgress();
        Bitmap bitmap = BitmapFactory.decodeFile(files[0].getAbsolutePath());
        if (bitmap == null)
            return result;
        List<Bitmap> srcEncodedList = Utility.splitImage(bitmap);
        String decoded = EncodeDecode.decodeMessage(srcEncodedList);

        //free memory
        for (Bitmap bitm : srcEncodedList)
            bitm.recycle();

        if (Utility.isStringEmpty(decoded)) {
            try {
                isImageDecoded = true;
                result.setEncrypted_zip(decoded.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return result;
    }
}
