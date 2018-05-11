package com.ayush.steganographylibrary.Utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    String message;
    String secret_key;
    String encrypted_message;

    public Crypto(String message, String secret_key) {
        this.message = message;
        this.secret_key = secret_key;
        this.encrypted_message = encryptMessage(message, secret_key);
    }

    private String encryptMessage(String message, String secret_key) {
        // Create key and cipher
        Key aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher = null;
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

    private String decryptMessage(String encrypted_message, String secret_key) {
        Key aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        // decrypt the text
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

        return decrypted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

}
