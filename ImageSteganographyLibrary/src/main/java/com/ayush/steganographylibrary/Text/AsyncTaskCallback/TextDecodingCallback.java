package com.ayush.steganographylibrary.Text.AsyncTaskCallback;

import com.ayush.steganographylibrary.Text.TextSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(TextSteganography result);

}
