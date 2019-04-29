package com.example.deneme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Intent git;
    public static TextView textView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("gida");
    public static ArrayMap<String,String> veritabani = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        Button btngaleri = findViewById(R.id.btngaleri);
        Button btnkamera = findViewById(R.id.btnkamera);
        Button btnvideo = findViewById(R.id.btnvideo);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    veritabani.put(ds.getKey(),ds.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btngaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Fotograf.btnYazi = "Galeri";
                git = new Intent(getApplicationContext(),Fotograf.class);
                startActivity(git);
                Fotograf.enabled = false;
            }
        });

        btnkamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                Fotograf.btnYazi = "Kamera";
                git = new Intent(getApplicationContext(),Fotograf.class);
                startActivity(git);
                Fotograf.enabled = false;
            }
        });
        textView.setText(Fotograf.yazi);
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                git = new Intent(getApplicationContext(), Kamera.class);
                startActivity(git);
            }
        });
    }
}
