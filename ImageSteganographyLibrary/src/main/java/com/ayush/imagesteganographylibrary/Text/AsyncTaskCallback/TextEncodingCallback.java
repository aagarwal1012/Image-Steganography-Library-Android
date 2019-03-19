package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    void onStartTextEncoding();

    @RequiresNonNull("result")
    void onCompleteTextEncoding(ImageSteganography result);

}
