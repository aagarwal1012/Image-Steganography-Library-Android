package com.ayush.imagesteganographylibrary.Utils;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    //Encryption Method
    /*
    @parameter : Message {String}, Secret key {String}
    @return : Encrypted Message {String}
     */
    public static String encryptMessage(String message, String secret_key) throws Exception {

        // Creating key and cipher
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher;

        //AES cipher
        cipher = Cipher.getInstance("AES");

        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] encrypted;

        encrypted = cipher.doFinal(message.getBytes());

        return new String(encrypted, "ISO-8859-1");
    }

    //Decryption Method
    /*
    @parameter : Encrypted Message {String}, Secret key {String}
    @return : Message {String}
     */
    public static String decryptMessage(String encrypted_message, String secret_key) throws Exception {

        // Creating key and cipher
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher;

        //AES cipher
        cipher = Cipher.getInstance("AES");

        // decrypting the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        String decrypted;

        decrypted = new String(cipher.doFinal(encrypted_message.getBytes(Charset.forName("ISO-8859-1"))));

        //returning decrypted text
        return decrypted;
    }

}
