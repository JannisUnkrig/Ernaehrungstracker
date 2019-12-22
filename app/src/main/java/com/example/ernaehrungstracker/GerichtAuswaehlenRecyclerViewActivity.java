package com.example.ernaehrungstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GerichtAuswaehlenRecyclerViewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gericht_auswaehlen_recycler_view);

        ArrayList<String> gerichteListe = new ArrayList<>();
        gerichteListe.add("Apfel");
        gerichteListe.add("Birne");
        gerichteListe.add("Banane");
        gerichteListe.add("Kiwi");

        ArrayList<String> beschreibungsListe = new ArrayList<>();
        beschreibungsListe.add("großer Apfel (150g)");
        beschreibungsListe.add("");
        beschreibungsListe.add("110g");
        beschreibungsListe.add("komplett hart");

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter(this, gerichteListe, beschreibungsListe);
        adapter.setmClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public  void onItemClick(View view, int position) {
        Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
        finish();
    }

}
