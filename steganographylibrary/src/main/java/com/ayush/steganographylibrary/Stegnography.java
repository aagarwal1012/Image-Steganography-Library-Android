package com.ayush.steganographylibrary;

public class Stegnography {

    String message;
    String secret_key;
    String imagePath;

    public Stegnography(String message, String secret_key, String imagePath) {
        this.message = message;
        this.secret_key = secret_key;
        this.imagePath = imagePath;
    }


}
