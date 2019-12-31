package com.example.ernaehrungstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GerichtAuswaehlenRecyclerViewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    public static Gericht uebergabeGericht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gericht_auswaehlen_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter(this, Speicher.loadGerichteListe(this));
        adapter.setmClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, adapter.getFoodName(position), Toast.LENGTH_SHORT).show();
        uebergabeGericht = adapter.getFood(position);

        ArrayList<Gericht> cur = Speicher.loadGerichteListe(this);
        cur.remove(position);
        cur.add(0, uebergabeGericht);
        Speicher.saveGerichteListe(this, cur);

        Intent returnGericht = new Intent();
        setResult(RESULT_OK, returnGericht);
        finish();
    }

}
