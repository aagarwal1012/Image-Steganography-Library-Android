package com.ayush.steganographylibrary.Utils;

import android.util.Log;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private final static String TAG = Crypto.class.getName();

    String message;
    String secret_key;
    String encrypted_message;
    byte[] encrypted_zip;

    //Constructor
    public Crypto(String message, String secret_key) {

        //Initilization
        this.message = message;
        this.secret_key = secret_key;
        this.encrypted_message = encryptMessage(message, secret_key);

        try {
            this.encrypted_zip = Zipping.compress(encrypted_message);
        } catch (IOException e) {
            Log.d(TAG, "Error : " + e);
        }
    }

    //Encryption Method
    /*
    @parameter : Message {String}, Secret key {String}
    @return : Encrypted Message {String}
     */
    private String encryptMessage(String message, String secret_key) {

        // Creating key and cipher
        Key aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher = null;

        //AES cipher
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // encrypt the text
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] encrypted = new byte[0];

        try {
            encrypted = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encrypted.toString();
    }

    //Decryption Method
    /*
    @parameter : Encrypted Message {String}, Secret key {String}
    @return : Message {String}
     */
    public static String decryptMessage(String encrypted_message, String secret_key) {
        // Creating key and cipher
        Key aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher = null;

        //AES cipher
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // decrypting the text
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        String decrypted = new String();
        try {
            decrypted = new String(cipher.doFinal(encrypted_message.getBytes()));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        //returning decrypted text
        return decrypted;
    }

    //Getters and Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public byte[] getEncrypted_zip() {
        return encrypted_zip;
    }

    public void setEncrypted_zip(byte[] encrypted_zip) {
        this.encrypted_zip = encrypted_zip;
    }

}
