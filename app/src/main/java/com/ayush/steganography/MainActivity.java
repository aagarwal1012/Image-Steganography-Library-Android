package com.ayush.steganography;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @LayoutRes int activityMainLayout = R.layout.activity_main;

    @IdRes int encodeButtonId = R.id.encode_button;
    @IdRes int decodeButtonId = R.id.decode_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityMainLayout);

        Button encode = findViewById(encodeButtonId);
        Button decode = findViewById(decodeButtonId);

        // Warning given by android - lint
        // Ensures that resource id's passed to APIs are of the right type
//        testAnnotation(R.layout.activity_decode);     // Flagged by android-lint
//        @IdRes int id = R.layout.activity_main;       // Flagged by android-lint
//        testAnnotation(activityMainLayout);           // Flagged by android-lint


//        encodeButtonId -= decodeButtonId;             // Arithmetic Operations should not be allowed
//        activityMainLayout += encodeButtonId;         // Arithmetic Operations should not be allowed
//        encodeButtonId = activityMainLayout;          // @IdRes should not equal to @LayoutRes
//        encodeButtonId = R.layout.activity_main;
//        ^^^ Not flagged by android-lint. ^^^

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Encode.class));
            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Decode.class));
            }
        });

    }


    //Test method
    void testAnnotation(@IdRes int id){

    }

}
