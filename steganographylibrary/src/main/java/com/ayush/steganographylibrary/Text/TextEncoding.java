package com.ayush.steganographylibrary.Text;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Crypto;
import com.ayush.steganographylibrary.Utils.Utility;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to encode secret message in image.
 * All the tasks will run in background.
 */
public class TextEncoding extends AsyncTask<TextSteganography, Integer, TextSteganography> {

    //Tag for Log
    private static String TAG = TextEncoding.class.getName();

    private int maximumProgress;

    private ProgressDialog progressDialog;

    public TextEncoding() {
        super();
    }

    //setting progress dialog if wanted
    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    //pre execution of method
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //setting parameters of progress dialog
        if (progressDialog != null){
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.setTitle("Encoding Message");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }
    }

    @Override
    protected void onPostExecute(TextSteganography textStegnography) {
        super.onPostExecute(textStegnography);

        //dismiss progress dialog
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        //Updating progress dialog
        if (progressDialog != null){
            progressDialog.show();
            progressDialog.incrementProgressBy(values[0]);
        }
    }

    @Override
    protected TextSteganography doInBackground(TextSteganography... textSteganographies) {

        //making result object
        TextSteganography result = null;

        maximumProgress = 0;

        if (textSteganographies.length > 0){

            TextSteganography textStegnography = textSteganographies[0];

            //Encrypting and compressing the message with the secret key
            if (Utility.isStringEmpty(textStegnography.getSecret_key()))
            {
                try{
                    Crypto encryption = new Crypto(textStegnography.getMessage(), textStegnography.getSecret_key());
                    textStegnography.setEncrypted_zip(encryption.getEncrypted_zip());

                    //free encryption
                    encryption = null;

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //calculating square blocks needed
            int pixels_needed = EncodeDecode.numberOfPixelForMessage(textStegnography.getEncrypted_zip().toString());
            int square_blocks_needed = Utility.squareBlockNeeded(pixels_needed);

            //making bitmap factory options for decoding file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int sample = calculateInSampleSize(options, square_blocks_needed * Utility.SQUARE_BLOCK_SIZE, square_blocks_needed * Utility.SQUARE_BLOCK_SIZE);
            options.inJustDecodeBounds = false;
            options.inSampleSize = sample;

            //decoding image file to bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(textStegnography.getBitmap().getAbsolutePath(), options);
            int originalHeight = bitmap.getHeight();
            int originalWidth = bitmap.getWidth();

            //splitting bitmap
            List<Bitmap> src_list = Utility.splitImage(bitmap);

            //encoding encrypted compressed message into image
            List<Bitmap> encoded_list = EncodeDecode.encodeMessage(src_list, textStegnography.getEncrypted_zip().toString(), new EncodeDecode.ProgressHandler() {

                //Progress Handler
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
            for (Bitmap bitm : src_list)
                bitm.recycle();

            //Java Garbage collector
            System.gc();

            //merging the split encoded image
            Bitmap srcEncoded = Utility.mergeImage(encoded_list, originalHeight, originalWidth);
            result.setEncrypted_image(bitmap);
        }

        return result;
    }

    /**
     * This method is used to calculate sample size.
     * @parameter : options {BitmapFactory options}
     * @parameter : required_width {Integer}
     * @parameter : required_height {Integer}
     * @return : inSamplesize {Integer}
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int required_width, int required_height) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > required_height || width > required_width) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > required_height
                    && (halfWidth / inSampleSize) > required_width) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
