package com.example.hkn18.tez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

public class MainActivity extends AppCompatActivity{

    Button btngaleri, btnkamera;
    public static TextView txtyazi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtyazi = findViewById(R.id.txtyazi);
        txtyazi.setTextColor(0xff000000);

        btngaleri = findViewById(R.id.btngaleri);
        btngaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resim.deger = "Galeri";
                Intent intent = new Intent(getApplicationContext(), resim.class);
                startActivity(intent);
            }
        });

        btnkamera = findViewById(R.id.btnkamera);
        btnkamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resim.deger = "Kamera";
                Intent intent = new Intent(getApplicationContext(), resim.class);
                startActivity(intent);
            }
        });
    }
}
