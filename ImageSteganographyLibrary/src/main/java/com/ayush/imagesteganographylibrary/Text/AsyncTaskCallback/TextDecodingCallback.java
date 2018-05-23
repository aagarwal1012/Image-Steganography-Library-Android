package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
