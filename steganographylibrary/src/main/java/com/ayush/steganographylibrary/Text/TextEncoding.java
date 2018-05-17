package com.ayush.steganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Crypto;
import com.ayush.steganographylibrary.Utils.Utility;

import java.util.List;

public class TextEncoding extends AsyncTask<TextStegnography, Integer, TextStegnography> {

    private static String TAG = TextEncoding.class.getName();

    private int maximumProgress;

    private Activity activity;

    private ProgressDialog progressDialog;

    public TextEncoding(Activity activity) {
        super();
        this.activity = activity;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading, Please Wait...");
        progressDialog.setTitle("Encoding Message");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPostExecute(TextStegnography textStegnography) {
        super.onPostExecute(textStegnography);
        progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.show();
        progressDialog.incrementProgressBy(values[0]);
        Log.d(TAG, "Progress : " + values[0]);
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

            int pixels_needed = EncodeDecode.numberOfPixelForMessage(textStegnography.getEncrypted_zip().toString());
            int square_blocks_needed = Utility.squareBlockNeeded(pixels_needed);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int sample = calculateInSampleSize(options, square_blocks_needed * Utility.SQUARE_BLOCK_SIZE, square_blocks_needed * Utility.SQUARE_BLOCK_SIZE);
            options.inJustDecodeBounds = false;
            options.inSampleSize = sample;

            Bitmap bitmap = BitmapFactory.decodeFile(textStegnography.getBitmap().getAbsolutePath(), options);
            int originalHeight = bitmap.getHeight();
            int originalWidth = bitmap.getWidth();

            List<Bitmap> srclist = Utility.splitImage(bitmap);

            List<Bitmap> encodedList = EncodeDecode.encodeMessage(srclist, textStegnography.getEncrypted_zip().toString(), new EncodeDecode.ProgressHandler() {
                @Override
                public void setTotal(int tot) {
                    maximumProgress = tot;
                    progressDialog.setMax(maximumProgress);
                    Log.d(TAG, "Total Length : " + tot);
                }

                @Override
                public void increment(int inc) {
                    publishProgress(inc);
                }

                @Override
                public void finished() {
                    Log.d(TAG, "Message Encoding end....");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Merging images...");
                }
            });

            //free Memory
            bitmap.recycle();
            for (Bitmap bitm : srclist)
                bitm.recycle();

            System.gc();


            Bitmap srcEncoded = Utility.mergeImage(encodedList, originalHeight, originalWidth);
            result.setEncrypted_image(bitmap);


        }

        return result;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
