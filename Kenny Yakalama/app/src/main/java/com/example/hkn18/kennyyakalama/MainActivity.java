package com.example.hkn18.kennyyakalama;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView sure;
    TextView skorum;

    ImageView k1;
    ImageView k2;
    ImageView k3;
    ImageView k4;
    ImageView k5;
    ImageView k6;
    ImageView k7;
    ImageView k8;
    ImageView k9;
    ImageView [] resimler;
    SharedPreferences best;
    Handler handler;
    Runnable run;
    int skoru = 0;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        best = this.getSharedPreferences("com.example.hkn18.kennyyakalama", Context.MODE_PRIVATE);

        skorum = findViewById(R.id.skor);
        k1 = findViewById(R.id.imageView);
        k2 = findViewById(R.id.imageView2);
        k3 = findViewById(R.id.imageView3);
        k4 = findViewById(R.id.imageView4);
        k5 = findViewById(R.id.imageView5);
        k6 = findViewById(R.id.imageView6);
        k7 = findViewById(R.id.imageView7);
        k8 = findViewById(R.id.imageView8);
        k9 = findViewById(R.id.imageView9);

        resimler = new ImageView[] {k1, k2, k3, k4, k5, k6, k7, k8, k9};
        sakla();

        sure = findViewById(R.id.sure);

        new CountDownTimer(10000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                sure.setText("Süre : " + millisUntilFinished/1000);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                sure.setText("Süre Doldu");
                handler.removeCallbacks(run);
                resimler[index].setVisibility(View.INVISIBLE);

                if(best.getInt("skor",0) == 0){
                    best.edit().putInt("skor",skoru).apply();
                    alert("Skorunuz : " + skoru);
                }
                else{
                    if(best.getInt("skor",0) < skoru){
                        best.edit().putInt("skor",skoru).apply();
                        alert("Tebrikler\nEn Yüksek Skor : " + best.getInt("skor",0) + "\nSizin Skorunuz  : " + skoru);
                    }
                    else
                        alert("En Yüksek Skor : " + best.getInt("skor",0) + "\nSizin Skorunuz  : " + skoru);
                }
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    public void arttir (View view){
        skorum.setText("Skor : " + ++skoru);
    }

    public void alert(String yazı){
        AlertDialog.Builder tablo = new AlertDialog.Builder(this);
        tablo.setTitle("SKOR");
        tablo.setMessage(yazı + "\n\nTekrar Oynamak İster Misiniz?");
        tablo.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                assert i != null;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        tablo.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        tablo.show();
    }

    public void sakla(){

        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                for(ImageView resim : resimler){
                    resim.setVisibility(View.INVISIBLE);
                }

                Random r = new Random();
                index = r.nextInt(8);
                resimler[index].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 300);
            }
        };

        handler.post(run);
    }
}
