package com.example.hkn18.instagram_deneme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnaSayfa extends AppCompatActivity {

    private Intent intent;

    ArrayList<String> ana_eposta;
    ArrayList<String> ana_resim;
    ArrayList<String> ana_not;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    ListView listView;
    postAnaSayfa bagla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        ana_eposta = new ArrayList<String>();
        ana_resim = new ArrayList<String>();
        ana_not = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        bagla = new postAnaSayfa(ana_eposta,ana_resim,ana_not,this);

        listView = findViewById(R.id.ana_list_post);
        listView.setAdapter(bagla);

        post();

    }

    protected void post(){
        DatabaseReference newRef = firebaseDatabase.getReference("Posts");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    //ana_eposta.add(hashMap.get("eposta"));
                    ana_resim.add(hashMap.get("downloadurl"));
                    ana_not.add(hashMap.get("aciklama"));
                    Toast.makeText(getApplicationContext(), ana_resim.get(0),Toast.LENGTH_LONG).show();
                    bagla.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ekle_btn(View view){
        intent = new Intent(getApplicationContext(), ResimEkle.class);
        startActivity(intent);
    }

}
