package com.ayush.steganographylibrary.Utils;

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
        return null;
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
