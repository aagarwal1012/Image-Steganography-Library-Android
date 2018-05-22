package com.ayush.steganographylibrary.Text.AsyncTaskCallback;

import com.ayush.steganographylibrary.Text.TextSteganography;

public interface TextEncodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(TextSteganography result);

}
