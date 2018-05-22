package com.ayush.steganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Utility;
import com.ayush.steganographylibrary.Utils.Zipping;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to decode secret message in image.
 * All the tasks will run in background.
 */
public class TextDecoding extends AsyncTask<TextSteganography, Void, TextSteganography> {

    //Tag for Log
    private final static String TAG = TextDecoding.class.getName();

    Activity activity;

    private ProgressDialog progressDialog;

    public TextDecoding(Activity activity) {
        super();
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);
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
            progressDialog.setTitle("Decoding Message");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

    @Override
    protected void onPostExecute(TextSteganography textSteganography) {
        super.onPostExecute(textSteganography);

        //dismiss progress dialog
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected TextSteganography doInBackground(TextSteganography... textSteganographies) {

        //making result object
        TextSteganography result = null;

        //If it is not already decoded
        if (textSteganographies.length > 0){

            TextSteganography textSteganography = textSteganographies[0];

            if (!textSteganography.isDecoded()){
                //getting bitmap image from file
                Bitmap bitmap = textSteganography.getImage();

                //return null if bitmap is null
                if (bitmap == null)
                    return result;

                //splitting images
                List<Bitmap> srcEncodedList = Utility.splitImage(bitmap);

                //decoding encrypted zipped message
                String decoded_message = EncodeDecode.decodeMessage(srcEncodedList);

                Log.d(TAG , "Decoded_Message : " + decoded_message);

                //decrypting the message
                String message = textSteganography.decryptMessage(decoded_message, textSteganography.getSecret_key());

                //If message is null it means that the secret key is wrong otherwise secret key is right.
                if (message != null){
                    //decompressing the message
                    try {
                        message = Zipping.decompress(message.getBytes("ISO-8859-1"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (message != null && Utility.isStringEmpty(message)) {
                        try {
                            //Setting message to result and decoded = true
                            result.setMessage(message);
                            result.setDecoded(true);
                            textSteganography.setDecoded(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //free memory
                    for (Bitmap bitm : srcEncodedList)
                        bitm.recycle();

                    //Java Garbage Collector
                    System.gc();
                }
                else {
                    //secret key provided is wrong
                    textSteganography.setSecretKeyWrong(true);
                }
            }
            else
                Log.d(TAG , "Already Decoded");

        }

        return result;
    }
}
