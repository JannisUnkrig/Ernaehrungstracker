package com.example.ernaehrungstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter(this, Gericht.gerichteListe);
        adapter.setmClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public  void onItemClick(View view, int position) {
        Toast.makeText(this, adapter.getFoodName(position), Toast.LENGTH_SHORT).show();

        Gericht food = adapter.getFood(position);
        Gericht.currentGericht = food;

        Intent returnGericht = new Intent();
        setResult(RESULT_OK, returnGericht);
        finish();
    }

}
