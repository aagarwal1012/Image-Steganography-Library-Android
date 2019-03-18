package com.ayush.imagesteganographylibrary.Text;

import android.graphics.Bitmap;
import android.util.Log;

import com.ayush.imagesteganographylibrary.Utils.Crypto;
import com.ayush.imagesteganographylibrary.Utils.Utility;

/**
 * This main class of the text steganography
 */
public class ImageSteganography {

    //Tag for Log
    private static final String TAG = ImageSteganography.class.getName();

    private String message;
    private String secret_key;
    private String encrypted_message;
    private Bitmap image;
    private Bitmap encoded_image;
    private byte[] encrypted_zip;
    private Boolean encoded;
    private Boolean decoded;
    private Boolean secretKeyWrong;

    public ImageSteganography() {
        this.encoded = false;
        this.decoded = false;
        this.secretKeyWrong = true;
        this.message = "";
        this.secret_key = "";
        this.encrypted_message = "";
        this.image = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encoded_image = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encrypted_zip = new byte[0];
    }

    public ImageSteganography(String message, String secret_key, Bitmap image) {

        this.message = message;
        this.secret_key = convertKeyTo128bit(secret_key);
        this.image = image;
        /*try {
            this.encrypted_zip = Zipping.compress(message);
        } catch (Exception e) {
            e.printStackTrace();
        } */

        this.encrypted_zip = message.getBytes();
        this.encrypted_message = encryptMessage(message, this.secret_key);

        this.encoded = false;
        this.decoded = false;
        this.secretKeyWrong = true;

        this.encoded_image = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);

    }

    public ImageSteganography(String secret_key, Bitmap image) {
        this.secret_key = convertKeyTo128bit(secret_key);
        this.image = image;

        this.encoded = false;
        this.decoded = false;
        this.secretKeyWrong = true;

        this.message = "";
        this.encrypted_message = "";
        this.encoded_image = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        this.encrypted_zip = new byte[0];
    }

    private static String encryptMessage(String message, String secret_key) {
        Log.d(TAG, "Message : " + message);

        String encrypted_message = "";
        if (message != null) {
            if (!Utility.isStringEmpty(secret_key)) {
                try {
                    encrypted_message = Crypto.encryptMessage(message, secret_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                encrypted_message = message;
            }
        }

        Log.d(TAG, "Encrypted_message : " + encrypted_message);

        return encrypted_message;
    }

    public static String decryptMessage(String message, String secret_key) {
        String decrypted_message = "";
        if (message != null) {
            if (!Utility.isStringEmpty(secret_key)) {
                try {
                    decrypted_message = Crypto.decryptMessage(message, secret_key);
                } catch (Exception e) {
                    Log.d(TAG, "Error : " + e.getMessage() + " , may be due to wrong key.");
                }
            } else {
                decrypted_message = message;
            }
        }

        return decrypted_message;
    }

    private static String convertKeyTo128bit(String secret_key) {

        StringBuilder result = new StringBuilder(secret_key);

        if (secret_key.length() <= 16) {
            for (int i = 0; i < (16 - secret_key.length()); i++) {
                result.append("#");
            }
        } else {
            result = new StringBuilder(result.substring(0, 15));
        }

        Log.d(TAG, "Secret Key Length : " + result.toString().getBytes().length);

        return result.toString();
    }

    public Bitmap getEncoded_image() {
        return encoded_image;
    }

    public void setEncoded_image(Bitmap encoded_image) {
        this.encoded_image = encoded_image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public byte[] getEncrypted_zip() {
        return encrypted_zip;
    }

    public void setEncrypted_zip(byte[] encrypted_zip) {
        this.encrypted_zip = encrypted_zip;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public void setEncrypted_message(String encrypted_message) {
        this.encrypted_message = encrypted_message;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Boolean isEncoded() {
        return encoded;
    }

    public void setEncoded(Boolean encoded) {
        this.encoded = encoded;
    }

    public Boolean isDecoded() {
        return decoded;
    }

    public void setDecoded(Boolean decoded) {
        this.decoded = decoded;
    }

    public Boolean isSecretKeyWrong() {
        return secretKeyWrong;
    }

    public void setSecretKeyWrong(Boolean secretKeyWrong) {
        this.secretKeyWrong = secretKeyWrong;
    }
}
