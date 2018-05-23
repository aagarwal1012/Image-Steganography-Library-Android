package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
