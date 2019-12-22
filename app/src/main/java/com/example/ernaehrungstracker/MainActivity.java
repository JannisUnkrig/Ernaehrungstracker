package com.example.ernaehrungstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    HeuteSpeicher heuteSpeicher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //draw menu
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //heute speicher
        if(heuteSpeicher == null) heuteSpeicher = new HeuteSpeicher();
    }

    public void gerichtAuswählenButtonPressed(View view) {
        Intent intent = new Intent(this, GerichtAuswaehlenRecyclerViewActivity.class);
        startActivity(intent);
    }

    public void mampfButtonPressed(View view) {
        /*String kcal = ((EditText) findViewById(R.id.toAddKcal)).getText().toString();
        String prot = ((EditText) findViewById(R.id.toAddProt)).getText().toString();
        String kh = ((EditText) findViewById(R.id.toAddKh)).getText().toString();
        String fett = ((EditText) findViewById(R.id.toAddFett)).getText().toString();*/

        int kcal = 0;
        int prot = 0;
        int kh   = 0;
        int fett = 0;

        try {
            kcal = Integer.parseInt(((EditText) findViewById(R.id.toAddKcal)).getText().toString());
        } catch (Exception e) {}
        try {
            prot = Integer.parseInt(((EditText) findViewById(R.id.toAddProt)).getText().toString());
        } catch (Exception e) {}
        try {
            kh = Integer.parseInt(((EditText) findViewById(R.id.toAddKh)).getText().toString());
        } catch (Exception e) {}
        try {
            fett = Integer.parseInt(((EditText) findViewById(R.id.toAddFett)).getText().toString());
        } catch (Exception e) {}

        //abbruch wenn alles 0
        if (kcal == 0 && prot == 0 && kh == 0 && fett == 0) return;

        Gericht temp = new Gericht(kcal, prot, kh, fett);
        heuteSpeicher.gerichtEssen(temp);

        //update upper editViews
        ((EditText) findViewById(R.id.curKcal)).setText("" + heuteSpeicher.getKcalHeute());
        ((EditText) findViewById(R.id.curProt)).setText("" + heuteSpeicher.getProtHeute());
        ((EditText) findViewById(R.id.curKh)).setText("" + heuteSpeicher.getKhHeute());
        ((EditText) findViewById(R.id.curFett)).setText("" + heuteSpeicher.getFettHeute());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.historie:
                Toast.makeText(MainActivity.this, "Historie ausgewählt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gewichtSpeichern:
                Toast.makeText(MainActivity.this, "Gewicht speichern ausgewählt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gerichteBearbeiten:
                Toast.makeText(MainActivity.this, "Gerichte bearbeiten ausgewählt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, GerichteBearbeitenActivity.class);
                startActivity(intent);
                break;
            case R.id.einstellungen:
                Toast.makeText(MainActivity.this, "Einstellungen ausgewählt", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
