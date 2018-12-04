package com.ayush.imagesteganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Utils.Utility;
import com.ayush.imagesteganographylibrary.Utils.Zipping;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to decode secret message in image.
 * All the tasks will run in background.
 */
public class TextDecoding extends AsyncTask<ImageSteganography, Void, ImageSteganography> {

    //Tag for Log
    private final static String TAG = TextDecoding.class.getName();

    Activity activity;

    private ProgressDialog progressDialog;

    ImageSteganography result;

    //Callback interface for AsyncTask
    TextDecodingCallback textDecodingCallback;

    public TextDecoding(Activity activity, TextDecodingCallback textDecodingCallback) {
        super();
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);
        this.textDecodingCallback = textDecodingCallback;
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
    protected void onPostExecute(ImageSteganography imageSteganography) {
        super.onPostExecute(imageSteganography);

        //dismiss progress dialog
        if(progressDialog != null)
            progressDialog.dismiss();

        //sending result to callback
        textDecodingCallback.onCompleteTextEncoding(result);
    }

    @Override
    protected ImageSteganography doInBackground(ImageSteganography... imageSteganographies) {

        //making result object
        result = new ImageSteganography();;

        //If it is not already decoded
        if (imageSteganographies.length > 0){

            ImageSteganography imageSteganography = imageSteganographies[0];

            //getting bitmap image from file
            Bitmap bitmap = imageSteganography.getImage();

            //return null if bitmap is null
            if (bitmap == null)
                return null;

            //splitting images
            List<Bitmap> srcEncodedList = Utility.splitImage(bitmap);

            //decoding encrypted zipped message
            String decoded_message = EncodeDecode.decodeMessage(srcEncodedList);

            Log.d(TAG , "Decoded_Message : " + decoded_message);

            //text decoded = true
            if (decoded_message != null){
                result.setDecoded(true);
            }

            //decrypting the encoded message
            String decrypted_message = imageSteganography.decryptMessage(decoded_message, imageSteganography.getSecret_key());
            Log.d(TAG, "Decrypted message : " + decrypted_message);

            String decompressed_message = null;

            //If decrypted_message is null it means that the secret key is wrong otherwise secret key is right.
            if (decrypted_message != null){

                //secret key provided is right
                result.setSecretKeyWrong(false);

                //decompressing the decrypted_message
                try {
                    decompressed_message = Zipping.decompress(decrypted_message.getBytes("ISO-8859-1"));
                    Log.d(TAG, "Original Message : " + decompressed_message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!Utility.isStringEmpty(decompressed_message)) {
                    try {
                        //Setting message to result
                        if (result != null && result.isDecoded())
                            result.setMessage(decompressed_message);

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
        }

        return result;
    }
}
