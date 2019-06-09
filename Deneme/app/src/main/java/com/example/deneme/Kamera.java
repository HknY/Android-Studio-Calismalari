package com.example.deneme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;


public class Kamera extends AppCompatActivity {

    TextView mTextView;
    SurfaceView mCameraView;
    CameraSource mCameraSource;
    String okunan = "İçindekiler: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera);

        mTextView = findViewById(R.id.text_view);
        mCameraView = findViewById(R.id.surfaceView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            startCameraSource(getApplicationContext());
        }

        Button btn = findViewById(R.id.btn_tmm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.textView.setText(okunan);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraSource(getApplicationContext());
        }
    }

    private void startCameraSource(final Context applicationContext) {

        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("Hata", "Detector dependencies not loaded yet");
        } else {

            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions((Activity) applicationContext, new String[]{Manifest.permission.CAMERA}, 2);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {

                                for (int i = 0; i < items.size(); i++) {
                                    String[] satirlar = items.valueAt(i).getValue().split("\n");
                                    for (String satir : satirlar) {
                                        String[] kelimeler = satir.split(" ");
                                        for (String kelime : kelimeler) {
                                            boolean izin = true;
                                            boolean gida = false;
                                            for (int j = 0; j < MainActivity.veritabani.size(); j++) {
                                                if (kelime.equals(MainActivity.veritabani.keyAt(j))) {
                                                    gida = true;
                                                    String[] kodlar = okunan.split(", ");

                                                    for (int k = 0; k < kodlar.length; k++) {

                                                        String yazi = kodlar[k];

                                                        if (k == 0) {
                                                            if (yazi.split(": ").length > 1)
                                                                yazi = kodlar[k].split(": ")[1];
                                                        }

                                                        if (yazi.equals(kelime))
                                                            izin = false;

                                                    }
                                                }
                                            }
                                            if (izin && gida) {
                                                okunan += kelime + ", ";
                                            }
                                        }
                                    }
                                }
                                mTextView.setText(okunan);
                            }
                        });
                    }
                }
            });
        }
    }

}
