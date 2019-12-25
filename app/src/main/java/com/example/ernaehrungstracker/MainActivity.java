package com.example.ernaehrungstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    HeuteSpeicher heuteSpeicher;

    boolean portionenLiveUpdaterActive = true;


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

        //heute speicher initialisieren
        if(heuteSpeicher == null) heuteSpeicher = new HeuteSpeicher();


        //initale Gerichte (konstruktor speichert)
        if (Gericht.gerichteListe == null) {
            Gericht.gerichteListe = new ArrayList<>();
            Gericht.gerichteListe.add(new Gericht("Apfel", "recht groß (150g)", 1, true, 80, 1, 13, 0));
            Gericht.gerichteListe.add(new Gericht("Birnenmuß", "", 150, false, 85, 1, 15, 0));
            Gericht.gerichteListe.add(new Gericht("Bananenmuß", "50g Banane 50g quark", 100, false, 110, 1, 12, 1));
            Gericht.gerichteListe.add(new Gericht("Nuss", "steinhart", 5, true, 80, 1, 18, 0));
        }

        //Inputfilter
        InputFilter[] ifd = new InputFilter[]{new InputFilterDecimal(5, 1)};
        InputFilter[] ifd2 = new InputFilter[]{new InputFilterDecimal(4, 1)};
        InputFilter[] ifd3 = new InputFilter[]{new InputFilterDecimal(4, 2)};

        ((EditText) findViewById(R.id.curKcal)).setFilters(ifd);
        ((EditText) findViewById(R.id.curProt)).setFilters(ifd);
        ((EditText) findViewById(R.id.curKh)).setFilters(ifd);
        ((EditText) findViewById(R.id.curFett)).setFilters(ifd);

        ((EditText) findViewById(R.id.goalKcal)).setFilters(ifd);
        ((EditText) findViewById(R.id.goalProt)).setFilters(ifd);
        ((EditText) findViewById(R.id.goalKh)).setFilters(ifd);
        ((EditText) findViewById(R.id.goalFett)).setFilters(ifd);

        final EditText toAddKcalEditText = findViewById(R.id.toAddKcal);
        final EditText toAddProtEditText = findViewById(R.id.toAddProt);
        final EditText toAddKhEditText = findViewById(R.id.toAddKh);
        final EditText toAddFettEditText = findViewById(R.id.toAddFett);

        toAddKcalEditText.setFilters(ifd2);
        toAddProtEditText.setFilters(ifd2);
        toAddKhEditText.setFilters(ifd2);
        toAddFettEditText.setFilters(ifd2);

        ((EditText) findViewById(R.id.toAddAmount)).setFilters(ifd3);


        //portionen live updater
        final EditText portionenGrammEditText = findViewById(R.id.toAddAmount);

        portionenGrammEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                if (!portionenLiveUpdaterActive) return;

                Gericht cur = Gericht.currentGericht;
                double curPortionenGramm = cur.getPortionenGramm();
                double displayedPortionenGramm;
                if (portionenGrammEditText.getText().toString().equals("")) {
                    if (!cur.isInPortionen()) {
                        displayedPortionenGramm = 100.0;
                    } else {
                        displayedPortionenGramm = 1.0;
                    }
                } else {
                    displayedPortionenGramm = Double.parseDouble(portionenGrammEditText.getText().toString());
                }

                double nextKcal = ((double) (Math.round(cur.getKcal() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextProt = ((double) (Math.round(cur.getProt() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextKh = ((double) (Math.round(cur.getKh() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextFett = ((double) (Math.round(cur.getFett() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);

                if (nextKcal > 9999.9) nextKcal = 9999.9;
                if (nextProt > 9999.9) nextProt = 9999.9;
                if (nextKh > 9999.9) nextKh = 9999.9;
                if (nextFett > 9999.9) nextFett = 9999.9;

                toAddKcalEditText.setText(doubleBeautifulizer(nextKcal));
                toAddProtEditText.setText(doubleBeautifulizer(nextProt));
                toAddKhEditText.setText(doubleBeautifulizer(nextKh));
                toAddFettEditText.setText(doubleBeautifulizer(nextFett));
            }
        });


        //set curGericht to unknownGericht when manually entering values
        TextWatcher changeToUnknownCurGerichtWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                if (toAddKcalEditText.isFocused() || toAddProtEditText.isFocused() || toAddKhEditText.isFocused() || toAddFettEditText.isFocused()) {

                    portionenLiveUpdaterActive = false;
                    ((Button) findViewById(R.id.gerichtAuswählenButton)).setText("Gericht auswählen...");
                    portionenGrammEditText.setText("");
                    portionenGrammEditText.setHint("1");
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText("Portionen");

                    String kcalString = toAddKcalEditText.getText().toString();
                    String protString = toAddProtEditText.getText().toString();
                    String khString   = toAddKhEditText.getText().toString();
                    String fettString = toAddFettEditText.getText().toString();

                    double kcalDouble = 0.0;
                    double protDouble = 0.0;
                    double khDouble = 0.0;
                    double fettDouble = 0.0;

                    if (!kcalString.equals("")) kcalDouble = Double.parseDouble(toAddKcalEditText.getText().toString());
                    if (!protString.equals("")) protDouble = Double.parseDouble(toAddProtEditText.getText().toString());
                    if (!khString.equals(""))   khDouble   = Double.parseDouble(toAddKhEditText.getText().toString());
                    if (!fettString.equals("")) fettDouble = Double.parseDouble(toAddFettEditText.getText().toString());

                    if (Gericht.currentGericht == null) {
                        Gericht.currentGericht = new Gericht(kcalDouble, protDouble, khDouble, fettDouble);
                    } else if (Gericht.currentGericht.getName().equals("unbekanntes Gericht")) {
                        Gericht.currentGericht.setKcal(kcalDouble);
                        Gericht.currentGericht.setProt(protDouble);
                        Gericht.currentGericht.setKh(khDouble);
                        Gericht.currentGericht.setFett(fettDouble);
                    } else {
                        Gericht.currentGericht = new Gericht(kcalDouble, protDouble, khDouble, fettDouble);
                    }
                    portionenLiveUpdaterActive = true;
                }
            }
        };

        toAddKcalEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddProtEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddKhEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddFettEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
    }



    public void gerichtAuswählenButtonPressed(View view) {
        Intent intent = new Intent(this, GerichtAuswaehlenRecyclerViewActivity.class);
        startActivityForResult(intent, 1234);
        findViewById(R.id.toAddKcal).clearFocus();
        findViewById(R.id.toAddProt).clearFocus();
        findViewById(R.id.toAddKh).clearFocus();
        findViewById(R.id.toAddFett).clearFocus();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent returnGericht) {

        super.onActivityResult(requestCode, resultCode, returnGericht);

        if (requestCode == 1234) {
            if (resultCode == RESULT_OK) {

                portionenLiveUpdaterActive = false;
                ((Button) findViewById(R.id.gerichtAuswählenButton)).setText(Gericht.currentGericht.getName());
                ((EditText) findViewById(R.id.toAddKcal)).setText(doubleBeautifulizer(Gericht.currentGericht.getKcal()));
                ((EditText) findViewById(R.id.toAddProt)).setText(doubleBeautifulizer(Gericht.currentGericht.getProt()));
                ((EditText) findViewById(R.id.toAddKh)).setText(doubleBeautifulizer(Gericht.currentGericht.getKh()));
                ((EditText) findViewById(R.id.toAddFett)).setText(doubleBeautifulizer(Gericht.currentGericht.getFett()));

                if (Gericht.currentGericht.isInPortionen()) {
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText("Portionen");
                    ((TextView) findViewById(R.id.toAddAmount)).setHint("1");
                    if (Gericht.currentGericht.getPortionenGramm() != 1) {
                        ((EditText) findViewById(R.id.toAddAmount)).setText(doubleBeautifulizer(Gericht.currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.toAddAmount)).setText("");
                    }
                } else {
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText("Gramm");
                    ((TextView) findViewById(R.id.toAddAmount)).setHint("100");
                    if (Gericht.currentGericht.getPortionenGramm() != 100) {
                        ((EditText) findViewById(R.id.toAddAmount)).setText(doubleBeautifulizer(Gericht.currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.toAddAmount)).setText("");
                    }
                }
                portionenLiveUpdaterActive = true;
            }
        }
    }



    public void mampfButtonPressed(View view) {
        //abbruch wenn (warum auch immer) currentGericht == null
        if (Gericht.currentGericht == null) return;

        //abbruch wenn alles 0
        double kcal = 0;
        double prot = 0;
        double kh   = 0;
        double fett = 0;
        String toAddKcal = ((EditText) findViewById(R.id.toAddKcal)).getText().toString();
        String toAddProt = ((EditText) findViewById(R.id.toAddProt)).getText().toString();
        String toAddKh   = ((EditText) findViewById(R.id.toAddKh)).getText().toString();
        String toAddFett = ((EditText) findViewById(R.id.toAddFett)).getText().toString();
        if (!toAddKcal.equals("")) kcal = Double.parseDouble(toAddKcal);
        if (!toAddProt.equals("")) prot = Double.parseDouble(toAddProt);
        if (!toAddKh.equals(""))   kh   = Double.parseDouble(toAddKh);
        if (!toAddFett.equals("")) fett = Double.parseDouble(toAddFett);
        if (kcal + prot + kh + fett == 0) return;

        //speichern
        Gericht cur = Gericht.currentGericht;
        Gericht curCopy = new Gericht(cur.getName(), cur.getDescription(), cur.getPortionenGramm(), cur.isInPortionen(), cur.getKcal(), cur.getProt(), cur.getKh(), cur.getFett());
        double curPortionenGramm = curCopy.getPortionenGramm();

        EditText portionenGrammEditText = findViewById(R.id.toAddAmount);
        double displayedPortionenGramm;
        if (portionenGrammEditText.getText().toString().equals("")) {
            if (!curCopy.isInPortionen()) {
                displayedPortionenGramm = 100.0;
            } else {
                displayedPortionenGramm = 1.0;
            }
        } else {
            displayedPortionenGramm = Double.parseDouble(portionenGrammEditText.getText().toString());
        }

        curCopy.setKcal((double) (Math.round(curCopy.getKcal() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
        curCopy.setProt((double) (Math.round(curCopy.getProt() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
        curCopy.setKh(  (double) (Math.round(curCopy.getKh()   / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
        curCopy.setFett((double) (Math.round(curCopy.getFett() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
        curCopy.setPortionenGramm(displayedPortionenGramm);

        heuteSpeicher.gerichtEssen(curCopy);
        //TODO
        Toast.makeText(MainActivity.this, "added: " + Gericht.currentGericht.getName(), Toast.LENGTH_SHORT).show();


        //update upper editViews
        ((EditText) findViewById(R.id.curKcal)).setText(doubleBeautifulizer(heuteSpeicher.getKcalHeute()));
        ((EditText) findViewById(R.id.curProt)).setText(doubleBeautifulizer(heuteSpeicher.getProtHeute()));
        ((EditText) findViewById(R.id.curKh)).setText(doubleBeautifulizer(heuteSpeicher.getKhHeute()));
        ((EditText) findViewById(R.id.curFett)).setText(doubleBeautifulizer(heuteSpeicher.getFettHeute()));
    }


    public String doubleBeautifulizer(double enemy) {
        if (Math.abs(enemy) < 0.001) return "";
        if (((int) (enemy * 10)) % 10 == 0) return "" + ((int) enemy);
        return "" + enemy;
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
