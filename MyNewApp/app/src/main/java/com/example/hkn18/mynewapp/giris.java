package com.example.hkn18.mynewapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class giris extends AppCompatActivity {

    int kro = 0;
    Handler handler;
    Runnable run;

    TextView sayac;
    TextView mesaj;
    Button durdur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        Intent intent = getIntent();
        String ad = intent.getStringExtra("ad");
        String soyad = intent.getStringExtra("soyad");

        durdur = findViewById(R.id.durdur);

        sayac = findViewById(R.id.sayac);
        mesaj = findViewById(R.id.mesaj);
        mesaj.setText("Hoşgeldiniz, " + ad + " " + soyad);

        new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

                sayac.setText(String.valueOf((int) (millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Kronometre Başladı", Toast.LENGTH_SHORT).show();

                handler = new Handler();
                run = new Runnable() {
                    @Override
                    public void run() {
                        sayac.setText(String.valueOf(kro++));
                        handler.postDelayed(this,1000);
                    }
                };
                handler.post(run);
                durdur.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void sifirla(View view){

        if(durdur.getText().equals("Sıfırla")){
            handler.removeCallbacks(run);
            kro=0;
            sayac.setText(String.valueOf(kro));
            durdur.setText("Başlat");
        }
        else{
            handler = new Handler();
            run = new Runnable() {
                @Override
                public void run() {
                    sayac.setText(String.valueOf(kro++));
                    handler.postDelayed(this,1000);
                }
            };
            handler.post(run);
            durdur.setText("Sıfırla");
        }


    }

}
