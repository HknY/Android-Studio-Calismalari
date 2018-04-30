package com.example.hkn18.kennyyakalama;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView sure;
    TextView skorum;
    TextView bestSkor;
    Button basla;
    Switch titre;
    Vibrator vib;

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
    boolean chec = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        best = this.getSharedPreferences("com.example.hkn18.kennyyakalama", Context.MODE_PRIVATE);

        skorum = findViewById(R.id.skor);
        sure = findViewById(R.id.sure);
        basla = findViewById(R.id.btnoyna);
        bestSkor = findViewById(R.id.bestSkor);
        titre = findViewById(R.id.switch1);

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

        if(best.getInt("skor",0) == 0){
            bestSkor.setText("En Yüksek Skor\n\t0");
        }
        else{
            bestSkor.setText("En Yüksek Skor\n\t\t\t\t\t" + best.getInt("skor",0) );
        }

        titre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chec = isChecked;
            }
        });

        sakla();
    }

    public void oyna(View view){
        skorum.setText("Skor : 0");
        skoru = 0;
        hareket();
        basla.setVisibility(View.INVISIBLE);
        new CountDownTimer(10000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                sure.setText("Süre : " + millisUntilFinished/1000);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {

                handler.removeCallbacks(run);
                sakla();
                basla.setVisibility(View.VISIBLE);
                resimler[index].setVisibility(View.INVISIBLE);

                if(best.getInt("skor",0) == 0){
                    best.edit().putInt("skor",skoru).apply();
                    bestSkor.setText("En Yüksek Skor\n\t0");
                }
                else{
                    if(best.getInt("skor",0) < skoru){
                        best.edit().putInt("skor",skoru).apply();
                    }
                    bestSkor.setText("En Yüksek Skor\n\t\t\t\t\t" + best.getInt("skor",0) );
                }
                sure.setText("Süre Doldu");

            }
        }.start();
    }


    @SuppressLint("SetTextI18n")
    public void arttir (View view){
        skorum.setText("Skor : " + ++skoru);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(chec){
            vib.vibrate(200);
        }

    }

    public void hareket(){

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

                handler.postDelayed(this, 400);
            }
        };
        handler.post(run);
    }

    public void sakla(){
        for(ImageView resim : resimler){
            resim.setVisibility(View.INVISIBLE);
        }
        resimler[7].setVisibility(View.VISIBLE);
        resimler[7].setEnabled(false);
    }
}
