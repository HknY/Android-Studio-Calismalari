package com.example.deneme;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent git;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btngaleri = findViewById(R.id.btngaleri);
        Button btnkamera = findViewById(R.id.btnkamera);

        btngaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fotograf.btnYazi = "Galeri";
                git = new Intent(getApplicationContext(),Fotograf.class);
                startActivity(git);
            }
        });

        btnkamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fotograf.btnYazi = "Kamera";
                git = new Intent(getApplicationContext(),Fotograf.class);
                startActivity(git);
            }
        });
    }
}
