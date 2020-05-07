package com.example.ernaehrungstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GerichtAuswaehlenRecyclerViewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener {

    MyRecyclerViewAdapter adapter;
    public static Gericht uebergabeGericht;

    public Note gesucht = Note.NEUTRAL;
    public int naehrwert = 0;    //0=nix 1=kcal 2=prot 3=kh 4=fett
    public static GerichtAuswaehlenRecyclerViewActivity curGARVA;

    private androidx.appcompat.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gericht_auswaehlen_recycler_view);

        curGARVA = this;

        //spinner
        Spinner spinner = findViewById(R.id.vielWenigSpinner);
        ArrayAdapter<CharSequence> adapterS1 = ArrayAdapter.createFromResource(this, R.array.vielWenig, android.R.layout.simple_spinner_item);
        adapterS1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS1);
        spinner.setOnItemSelectedListener(this);

        //spinner2
        Spinner spinner2 = findViewById(R.id.wovonSpinner);
        ArrayAdapter<CharSequence> adapterS2 = ArrayAdapter.createFromResource(this, R.array.wovon, android.R.layout.simple_spinner_item);
        adapterS2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterS2);
        spinner2.setOnItemSelectedListener(this);

        //recy view
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
        for (Gericht gericht : cur) {
            if (gericht.getName().equals(uebergabeGericht.getName()) && gericht.getDescription().equals(uebergabeGericht.getDescription())) {
                cur.remove(gericht);
                break;
            }
        }
        cur.add(0, uebergabeGericht);
        Speicher.saveGerichteListe(this, cur);

        Intent returnGericht = new Intent();
        setResult(RESULT_OK, returnGericht);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //farbe und größe
        ((TextView) parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
        ((TextView) parent.getChildAt(0)).setTextSize(14);

        if (parent.getId() == R.id.vielWenigSpinner) {
            //was ist gesucht?
            if (position == 0) {
                gesucht = Note.NEUTRAL;
            } else if (position == 1) {
                gesucht = Note.HIGH;
            } else if (position == 2) {
                gesucht = Note.LOW;
            }
        } else if (parent.getId() == R.id.wovonSpinner) {
            //was ist gesucht?
            if (position == 0) {
                naehrwert = 0;
            } else if (position == 1) {
                naehrwert = 1;
            } else if (position == 2) {
                naehrwert = 4;
            } else if (position == 3) {
                naehrwert = 3;
            } else if (position == 4) {
                naehrwert = 2;
            }
        }

        adapter.getFilter().filter(searchView.getQuery());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
