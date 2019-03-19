package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    void onStartTextEncoding();

    @RequiresNonNull("result")
    void onCompleteTextEncoding(ImageSteganography result);

}
