package com.example.hkn18.mynewapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int sayi = 1;

    public void degis(View view){

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Random a = new Random();
        sayi = a.nextInt(2);

        switch (sayi){
            case 0:
                imageView.setImageResource(R.drawable.tesla2);
                break;

            case 1:
                imageView.setImageResource(R.drawable.tesla11);
                break;
        }

    }

}
