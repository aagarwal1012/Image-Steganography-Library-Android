package com.ayush.steganographylibrary.Text.Class;


import android.app.Activity;

import com.ayush.steganographylibrary.Text.AsyncTaskCallback.CallbackInterface;
import com.ayush.steganographylibrary.Text.TextEncodingClass;
import com.ayush.steganographylibrary.Text.TextSteganography;

public class TextEncoding implements CallbackInterface<TextSteganography>{

    TextSteganography result;

    public void launchTask(Activity activity, TextSteganography textSteganography){
        TextEncodingClass textEncodingClass = new TextEncodingClass(activity, this);
        textEncodingClass.execute(textSteganography);
    }

    public TextSteganography getResult() {
        return result;
    }

    public void setResult(TextSteganography result) {
        this.result = result;
    }

    @Override
    public void onTaskComplete(TextSteganography textSteganography) {
        this.result = textSteganography;
    }
}
