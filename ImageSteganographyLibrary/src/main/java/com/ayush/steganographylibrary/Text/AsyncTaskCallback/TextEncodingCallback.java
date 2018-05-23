package com.ayush.steganographylibrary.Text.AsyncTaskCallback;

import com.ayush.steganographylibrary.Text.TextSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(TextSteganography result);

}
