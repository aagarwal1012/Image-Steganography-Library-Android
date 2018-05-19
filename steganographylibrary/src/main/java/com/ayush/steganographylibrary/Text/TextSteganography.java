package com.ayush.steganographylibrary.Text;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Crypto;
import com.ayush.steganographylibrary.Utils.Utility;
import com.ayush.steganographylibrary.Utils.Zipping;

import java.io.File;

/**
 * This main class of the text steganography
 */
public class TextSteganography {

    String message;
    String secret_key;
    String encrypted_message;
    Uri filepath;
    File bitmap;
    Bitmap image;
    Bitmap encrypted_image;
    byte[] encrypted_zip;

    public TextSteganography() {
    }

    public TextSteganography(String message, String secret_key, File bitmap) {

        this.message = message;
        this.secret_key = convertKeyTo128bit(secret_key);
        this.bitmap = bitmap;
        this.encrypted_message = encryptMessage(message, this.secret_key);
        try {
            this.encrypted_zip = Zipping.compress(encrypted_message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextSteganography(String secret_key, File bitmap) {
        this.secret_key = convertKeyTo128bit(secret_key);
        this.bitmap = bitmap;
    }

    public Bitmap getEncrypted_image() {
        return encrypted_image;
    }

    public void setEncrypted_image(Bitmap encrypted_image) {
        this.encrypted_image = encrypted_image;
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

    public File getBitmap() {
        return bitmap;
    }

    public void setBitmap(File bitmap) {
        this.bitmap = bitmap;
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

    public Uri getFilepath() {
        return filepath;
    }

    public void setFilepath(Uri filepath) {
        this.filepath = filepath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public static String encryptMessage(String message, String secret_key){
        String encrypted_message = null;
        if (message != null){
            if (!Utility.isStringEmpty(secret_key)){
                try {
                    encrypted_message = Crypto.encryptMessage(message, secret_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                encrypted_message = message;
            }
        }
        return encrypted_message;
    }

    public static String decryptMessage(String message, String secret_key){
        String decrypted_message = null;
        if (message != null){
            if (!Utility.isStringEmpty(secret_key)){
                try {
                    decrypted_message = Crypto.decryptMessage(message, secret_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                decrypted_message = message;
            }
        }
        return decrypted_message;
    }

    public static String convertKeyTo128bit(String secret_key){

        String result = secret_key;

        if (secret_key.length() <= 16){
            for (int i = 0; i < (16 - secret_key.length()); i++){
                result += "#";
            }
        }
        else {
            result = result.substring(0, 15);
        }

        Log.d("Secret key length", "" + result.getBytes().length);

        return result;
    }
}
