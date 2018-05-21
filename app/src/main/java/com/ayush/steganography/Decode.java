package com.ayush.steganography;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayush.steganographylibrary.Text.TextDecoding;
import com.ayush.steganographylibrary.Text.TextEncoding;
import com.ayush.steganographylibrary.Text.TextSteganography;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Decode extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";

    private Uri filepath;

    private Bitmap original_image;

    TextView whether_decoded;
    ImageView imageView;
    EditText message, secret_key;
    Button choose_image_button, decode_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        whether_decoded = (TextView) findViewById(R.id.whether_decoded);

        imageView = (ImageView) findViewById(R.id.imageview);

        message = (EditText) findViewById(R.id.message);
        secret_key = (EditText) findViewById(R.id.secret_key);

        choose_image_button = (Button) findViewById(R.id.choose_image_button);
        decode_button = (Button) findViewById(R.id.decode_button);

        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null){
                    TextSteganography textSteganography = new TextSteganography(secret_key.getText().toString(),
                            original_image);
                    TextDecoding textDecoding = new TextDecoding(Decode.this);

                    textDecoding.execute(textSteganography);

                    TextSteganography result = null;
                    try {
                        result = textDecoding.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (result != null && result.isDecoded() ){
                        whether_decoded.setText("Decoded");
                        message.setText("" + result.getMessage());
                    }

                    if (result != null && result.isSecretKeyWrong() != null && result.isSecretKeyWrong()){
                        whether_decoded.setText("Wrong secret key");
                    }
                }
            }
        });


    }

    void ImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){

            filepath = data.getData();
            try{
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                imageView.setImageBitmap(original_image);
            }
            catch (IOException e){
                Log.d(TAG, "Error : " + e);
            }
        }

    }
}
