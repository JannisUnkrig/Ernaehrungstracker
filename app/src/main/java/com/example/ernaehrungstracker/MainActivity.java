package com.example.ernaehrungstracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    boolean portionenLiveUpdaterActive = true;
    boolean curWatcherActive = true;
    boolean goalWatcherActive = true;
    boolean changeToUnknownCurGerichtWatcherActive = true;
    public Gericht currentGericht = null;

    public static Context curMainAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curMainAct = this;

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
                if (!portionenLiveUpdaterActive || currentGericht == null) return;
                changeToUnknownCurGerichtWatcherActive = false;

                double curPortionenGramm = currentGericht.getPortionenGramm();
                double displayedPortionenGramm;
                if (portionenGrammEditText.getText().toString().equals("")) {
                    if (!currentGericht.isInPortionen()) {
                        displayedPortionenGramm = 100.0;
                    } else {
                        displayedPortionenGramm = 1.0;
                    }
                } else {
                    displayedPortionenGramm = Double.parseDouble(portionenGrammEditText.getText().toString());
                }

                double nextKcal = ((double) (Math.round(currentGericht.getKcal() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextProt = ((double) (Math.round(currentGericht.getProt() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextKh = ((double) (Math.round(currentGericht.getKh() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);
                double nextFett = ((double) (Math.round(currentGericht.getFett() / curPortionenGramm * displayedPortionenGramm * 10)) / 10);

                if (nextKcal > 9999.9) nextKcal = 9999.9;
                if (nextProt > 9999.9) nextProt = 9999.9;
                if (nextKh > 9999.9) nextKh = 9999.9;
                if (nextFett > 9999.9) nextFett = 9999.9;

                toAddKcalEditText.setText(doubleBeautifulizer(nextKcal));
                toAddProtEditText.setText(doubleBeautifulizer(nextProt));
                toAddKhEditText.setText(doubleBeautifulizer(nextKh));
                toAddFettEditText.setText(doubleBeautifulizer(nextFett));

                changeToUnknownCurGerichtWatcherActive = true;
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
                if (!changeToUnknownCurGerichtWatcherActive) return;
                //if (toAddKcalEditText.isFocused() || toAddProtEditText.isFocused() || toAddKhEditText.isFocused() || toAddFettEditText.isFocused()) {

                    portionenLiveUpdaterActive = false;

                    String kcalString = toAddKcalEditText.getText().toString();
                    String protString = toAddProtEditText.getText().toString();
                    String khString   = toAddKhEditText.getText().toString();
                    String fettString = toAddFettEditText.getText().toString();

                    portionenGrammEditText.setText("");
                    portionenGrammEditText.setHint("100");
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText(getString(R.string.gramm));

                    if (toAddKcalEditText.getText().toString().equals("") && toAddProtEditText.getText().toString().equals("") && toAddKhEditText.getText().toString().equals("") && toAddFettEditText.getText().toString().equals("")) {
                        ((Button) findViewById(R.id.gerichtAuswählenButton)).setText(getString(R.string.gericht_auswaehlen_button));
                        findViewById(R.id.entfernenButton).setVisibility(View.GONE);
                        currentGericht = null;
                    } else {
                        ((Button) findViewById(R.id.gerichtAuswählenButton)).setText(getString(R.string.unbekanntes_gericht));
                        findViewById(R.id.entfernenButton).setVisibility(View.VISIBLE);

                        double kcalDouble = 0.0;
                        double protDouble = 0.0;
                        double khDouble = 0.0;
                        double fettDouble = 0.0;

                        if (!kcalString.equals("")) kcalDouble = Double.parseDouble(kcalString);
                        if (!protString.equals("")) protDouble = Double.parseDouble(protString);
                        if (!khString.equals("")) khDouble = Double.parseDouble(khString);
                        if (!fettString.equals("")) fettDouble = Double.parseDouble(fettString);

                        if (currentGericht == null) {
                            currentGericht = new Gericht(kcalDouble, protDouble, khDouble, fettDouble);
                        } else if (currentGericht.getName().equals(getString(R.string.unbekanntes_gericht))) {
                            currentGericht.setKcal(kcalDouble);
                            currentGericht.setProt(protDouble);
                            currentGericht.setKh(khDouble);
                            currentGericht.setFett(fettDouble);
                        } else {
                            currentGericht = new Gericht(kcalDouble, protDouble, khDouble, fettDouble);
                        }
                        portionenLiveUpdaterActive = true;
                    }
                //}
            }
        };

        toAddKcalEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddProtEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddKhEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);
        toAddFettEditText.addTextChangedListener(changeToUnknownCurGerichtWatcher);


        //manuelle änderungen Ziel
        TextWatcher goalWatcher = new TextWatcher() {
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
                if (!goalWatcherActive) return;
                ArrayList<HeuteSpeicher> curHSL = Speicher.loadHeuteSpeicherListe(getApplicationContext());
                HeuteSpeicher curHS = curHSL.get(0);

                String kcalString = ((EditText) findViewById(R.id.goalKcal)).getText().toString();
                String protString = ((EditText) findViewById(R.id.goalProt)).getText().toString();
                String khString   = ((EditText) findViewById(R.id.goalKh)).getText().toString();
                String fettString = ((EditText) findViewById(R.id.goalFett)).getText().toString();

                double kcalDouble = -1;
                double protDouble = -1;
                double khDouble   = -1;
                double fettDouble = -1;

                if (!kcalString.equals("")) kcalDouble = Double.parseDouble(kcalString);
                if (!protString.equals("")) protDouble = Double.parseDouble(protString);
                if (!khString.equals(""))   khDouble   = Double.parseDouble(khString);
                if (!fettString.equals("")) fettDouble = Double.parseDouble(fettString);

                curHS.setKcalZielHeute(kcalDouble);
                curHS.setProtZielHeute(protDouble);
                curHS.setKhZielHeute(khDouble);
                curHS.setFettZielHeute(fettDouble);

                Speicher.saveHeuteSpeicherListe(getApplicationContext(), curHSL);
            }
        };

        ((EditText) findViewById(R.id.goalKcal)).addTextChangedListener(goalWatcher);
        ((EditText) findViewById(R.id.goalProt)).addTextChangedListener(goalWatcher);
        ((EditText) findViewById(R.id.goalKh)).addTextChangedListener(goalWatcher);
        ((EditText) findViewById(R.id.goalFett)).addTextChangedListener(goalWatcher);


        //manuelle änderungen cur
        TextWatcher curWatcher = new TextWatcher() {
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
                if (!curWatcherActive) return;
                ArrayList<HeuteSpeicher> curHSL = Speicher.loadHeuteSpeicherListe(getApplicationContext());
                HeuteSpeicher curHS = curHSL.get(0);

                String kcalString = ((EditText) findViewById(R.id.curKcal)).getText().toString();
                String protString = ((EditText) findViewById(R.id.curProt)).getText().toString();
                String khString   = ((EditText) findViewById(R.id.curKh)).getText().toString();
                String fettString = ((EditText) findViewById(R.id.curFett)).getText().toString();

                double kcalDouble = 0;
                double protDouble = 0;
                double khDouble   = 0;
                double fettDouble = 0;

                if (!kcalString.equals("")) kcalDouble = Double.parseDouble(kcalString);
                if (!protString.equals("")) protDouble = Double.parseDouble(protString);
                if (!khString.equals(""))   khDouble   = Double.parseDouble(khString);
                if (!fettString.equals("")) fettDouble = Double.parseDouble(fettString);

                double kcalDelta = (double) (Math.round((kcalDouble - curHS.getKcalHeute()) * 10)) / 10;
                double protDelta = (double) (Math.round((protDouble - curHS.getProtHeute()) * 10)) / 10;
                double khDelta   = (double) (Math.round((khDouble   - curHS.getKhHeute())   * 10)) / 10;
                double fettDelta = (double) (Math.round((fettDouble - curHS.getFettHeute()) * 10)) / 10;

                //Toast.makeText(MainActivity.curMainAct, "" + kcalDelta + "  " + protDelta + "  " + khDelta + "  " + fettDelta, Toast.LENGTH_SHORT).show();

                if (!curHS.getGegesseneGerichte().isEmpty()) {
                    Gericht letztes = curHS.getGegesseneGerichte().get(curHS.getGegesseneGerichte().size() - 1);

                    if (letztes.getName().equals(getString(R.string.manuelle_aenderung))) {
                        curHS.addKcalHeute(kcalDelta);
                        curHS.addProtHeute(protDelta);
                        curHS.addKhHeute(khDelta);
                        curHS.addFettHeute(fettDelta);
                        letztes.addKcal(kcalDelta);
                        letztes.addProt(protDelta);
                        letztes.addKh(khDelta);
                        letztes.addFett(fettDelta);
                        if (letztes.getKcal() == 0 && letztes.getProt() == 0 && letztes.getKh() == 0 && letztes.getFett() == 0) {
                            curHS.getGegesseneGerichte().remove(curHS.getGegesseneGerichte().size() - 1);
                        }
                    } else {
                        if (kcalDelta != 0 || protDelta != 0 || khDelta != 0 || fettDelta != 0) {
                            Gericht manuelleAenderung = new Gericht(getString(R.string.manuelle_aenderung), "", 1, true, kcalDelta, protDelta, khDelta, fettDelta);
                            curHS.gerichtEssen(manuelleAenderung);
                        }
                    }
                } else {
                    if (kcalDelta != 0 || protDelta != 0 || khDelta != 0 || fettDelta != 0) {
                        Gericht manuelleAenderung = new Gericht(getString(R.string.manuelle_aenderung), "", 1, true, kcalDelta, protDelta, khDelta, fettDelta);
                        curHS.gerichtEssen(manuelleAenderung);
                    }
                }

                Speicher.saveHeuteSpeicherListe(getApplicationContext(), curHSL);
            }
        };

        ((EditText) findViewById(R.id.curKcal)).addTextChangedListener(curWatcher);
        ((EditText) findViewById(R.id.curProt)).addTextChangedListener(curWatcher);
        ((EditText) findViewById(R.id.curKh)).addTextChangedListener(curWatcher);
        ((EditText) findViewById(R.id.curFett)).addTextChangedListener(curWatcher);

    }


    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(this);

        updateTrackerDisplayed(HSL);

        curWatcherActive = false;
        ((EditText) findViewById(R.id.curKcal)).setText("");
        ((EditText) findViewById(R.id.curProt)).setText("");
        ((EditText) findViewById(R.id.curKh)).setText("");
        ((EditText) findViewById(R.id.curFett)).setText("");
        curWatcherActive = true;
        dailyReset(HSL);
        updateUpperEditTexts(HSL.get(0));
    }

    public void entfernenButtonPressed(View view) {
        changeToUnknownCurGerichtWatcherActive = false;
        ((EditText) findViewById(R.id.toAddKcal)).setText("");
        ((EditText) findViewById(R.id.toAddProt)).setText("");
        ((EditText) findViewById(R.id.toAddKh)).setText("");
        ((EditText) findViewById(R.id.toAddFett)).setText("1");
        changeToUnknownCurGerichtWatcherActive = true;
        ((EditText) findViewById(R.id.toAddFett)).setText("");
    }


    public void gerichtAuswaehlenButtonPressed(View view) {
        Intent intent = new Intent(this, GerichtAuswaehlenRecyclerViewActivity.class);
        startActivityForResult(intent, 1);
        findViewById(R.id.toAddKcal).clearFocus();
        findViewById(R.id.toAddProt).clearFocus();
        findViewById(R.id.toAddKh).clearFocus();
        findViewById(R.id.toAddFett).clearFocus();
    }



    public void mampfButtonPressed(View view) {
        //abbruch wenn currentGericht == null
        if (currentGericht == null) return;
        curWatcherActive = false;

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
        if (kcal == 0 && prot == 0 && kh == 0 && fett == 0) return;

        ((Vibrator) this.getSystemService(VIBRATOR_SERVICE)).vibrate(50);

        //speicherbares Gericht erzeugen
        Gericht curCopy = new Gericht(  currentGericht.getName(), currentGericht.getDescription(), currentGericht.getPortionenGramm(), currentGericht.isInPortionen(),
                                        currentGericht.getKcal(), currentGericht.getProt(), currentGericht.getKh(), currentGericht.getFett());
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
        if (curCopy.getKcal() > 9999.9) curCopy.setKcal(9999.9);
        if (curCopy.getProt() > 9999.9) curCopy.setProt(9999.9);
        if (curCopy.getKh() > 9999.9) curCopy.setKh(9999.9);
        if (curCopy.getFett() > 9999.9) curCopy.setFett(9999.9);
        curCopy.setPortionenGramm(displayedPortionenGramm);

        //curCopy einspeichern
        ArrayList<HeuteSpeicher> curHeuteSpeicherListe = Speicher.loadHeuteSpeicherListe(this);
        HeuteSpeicher curHeuteSpeicher = curHeuteSpeicherListe.get(0);
        ArrayList<Gericht> curGegesseneGerichte = curHeuteSpeicher.getGegesseneGerichte();

        if (!curHeuteSpeicher.isTrackKcal()) curCopy.setKcal(0);
        if (!curHeuteSpeicher.isTrackProt()) curCopy.setProt(0);
        if (!curHeuteSpeicher.isTrackKh()  ) curCopy.setKh(0);
        if (!curHeuteSpeicher.isTrackFett()) curCopy.setFett(0);

        //gericht hinzufügen
        if (curGegesseneGerichte.size() > 0) {
            Gericht letztes = curGegesseneGerichte.get(curGegesseneGerichte.size() - 1);

            if (letztes.getName().equals(curCopy.getName()) && letztes.getDescription().equals(curCopy.getDescription()) && !curCopy.getName().equals(getString(R.string.unbekanntes_gericht))) {
                letztes.addPortionenGramm(curCopy.getPortionenGramm());
                letztes.addKcal(curCopy.getKcal());
                letztes.addProt(curCopy.getProt());
                letztes.addKh(curCopy.getKh());
                letztes.addFett(curCopy.getFett());
                curHeuteSpeicher.addKcalHeute(curCopy.getKcal());
                curHeuteSpeicher.addProtHeute(curCopy.getProt());
                curHeuteSpeicher.addKhHeute(curCopy.getKh());
                curHeuteSpeicher.addFettHeute(curCopy.getFett());
            } else {
                curHeuteSpeicher.gerichtEssen(curCopy);
            }
        } else {
            curHeuteSpeicher.gerichtEssen(curCopy);
        }
        //update upper editViews
        updateUpperEditTextsCur(curHeuteSpeicherListe.get(0));

        Speicher.saveHeuteSpeicherListe(this, curHeuteSpeicherListe);

        //Toast ausgeben
        String builtString;
        if (curCopy.isInPortionen()) {
            if (curCopy.getPortionenGramm() == 1) {
                builtString = getString(R.string.eine_portion) + " ";
            } else {
                builtString = doubleBeautifulizerNull(curCopy.getPortionenGramm()) + " " + getString(R.string.portionen) + " ";
            }
        } else {
            if (curCopy.getPortionenGramm() == 1) {
                builtString = getString(R.string.ein_gramm) + " ";
            } else {
                builtString = doubleBeautifulizerNull(curCopy.getPortionenGramm()) + " " + getString(R.string.gramm) + " ";
            }
        }
        Toast.makeText(MainActivity.this, builtString + currentGericht.getName() + " " + getString(R.string.hinzugefügt), Toast.LENGTH_SHORT).show();

        curWatcherActive = true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnGericht) {

        super.onActivityResult(requestCode, resultCode, returnGericht);

        //after gericht auswählen
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                portionenLiveUpdaterActive = false;
                changeToUnknownCurGerichtWatcherActive = false;
                currentGericht = GerichtAuswaehlenRecyclerViewActivity.uebergabeGericht;
                HeuteSpeicher HS = Speicher.loadHeuteSpeicherListe(this).get(0);

                findViewById(R.id.entfernenButton).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.gerichtAuswählenButton)).setText(currentGericht.getName());
                if (HS.isTrackKcal()) ((EditText) findViewById(R.id.toAddKcal)).setText(doubleBeautifulizer(currentGericht.getKcal()));
                if (HS.isTrackProt()) ((EditText) findViewById(R.id.toAddProt)).setText(doubleBeautifulizer(currentGericht.getProt()));
                if (HS.isTrackKh())   ((EditText) findViewById(R.id.toAddKh)).setText(doubleBeautifulizer(currentGericht.getKh()));
                if (HS.isTrackFett()) ((EditText) findViewById(R.id.toAddFett)).setText(doubleBeautifulizer(currentGericht.getFett()));

                if (currentGericht.isInPortionen()) {
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText(getString(R.string.portionen));
                    ((TextView) findViewById(R.id.toAddAmount)).setHint("1");
                    if (currentGericht.getPortionenGramm() != 1) {
                        ((EditText) findViewById(R.id.toAddAmount)).setText(doubleBeautifulizer(currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.toAddAmount)).setText("");
                    }
                } else {
                    ((TextView) findViewById(R.id.toAddPortionenText)).setText(getString(R.string.gramm));
                    ((TextView) findViewById(R.id.toAddAmount)).setHint("100");
                    if (currentGericht.getPortionenGramm() != 100) {
                        ((EditText) findViewById(R.id.toAddAmount)).setText(doubleBeautifulizer(currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.toAddAmount)).setText("");
                    }
                }
                changeToUnknownCurGerichtWatcherActive = true;
                portionenLiveUpdaterActive = true;
            }
        }

        //after gerichte bearbeiten
        if (requestCode == 3) {
            updateUpperEditTextsCur(Speicher.loadHeuteSpeicherListe(this).get(0));
        }

        //after settings
        if (requestCode == 5) {
            changeToUnknownCurGerichtWatcherActive = false;
            ((EditText) findViewById(R.id.toAddKcal)).setText("");
            ((EditText) findViewById(R.id.toAddProt)).setText("");
            ((EditText) findViewById(R.id.toAddKh)).setText("");
            ((EditText) findViewById(R.id.toAddFett)).setText("1");
            changeToUnknownCurGerichtWatcherActive = true;
            ((EditText) findViewById(R.id.toAddFett)).setText("");

            ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(this);

            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("limitStorage", false)) {
                int limitTo = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("limitStorageTo", "0x7fffffff"));
                while (HSL.size() > limitTo + 1) {
                    HSL.remove(HSL.size() - 1);
                }
                Speicher.saveHeuteSpeicherListe(this, HSL);
            }
        }
    }


    public static String doubleBeautifulizer(double enemy) {
        if (Math.abs(enemy) < 0.001) return "";
        if (((int) (enemy * 10)) % 10 == 0) return "" + ((int) enemy);
        return "" + enemy;
    }

    public static String doubleBeautifulizerNull(double enemy) {
        if (((int) (enemy * 10)) % 10 == 0) return "" + ((int) enemy);
        return "" + enemy;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.historie:
                Intent intent2 = new Intent(this, HistorieActivity.class);
                startActivityForResult(intent2, 3);
                break;


            case R.id.gewichtSpeichern:
                GewichtSpeichernDialog gewichtSpeichernDialog = new GewichtSpeichernDialog();
                gewichtSpeichernDialog.show(getSupportFragmentManager(), "gewicht speichern dialog");
                break;


            case R.id.gerichteBearbeiten:
                Intent intent = new Intent(this, GerichteBearbeitenActivity.class);
                startActivity(intent);
                break;


            case R.id.einstellungen:
                Intent intent4 = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent4, 5);
                break;
        }

        return false;
    }

    private void updateUpperEditTexts(HeuteSpeicher curHeuteSpeicher) {
        curWatcherActive = false;
        goalWatcherActive = false;
        ((EditText) findViewById(R.id.curKcal)).setText(doubleBeautifulizer(curHeuteSpeicher.getKcalHeute()));
        ((EditText) findViewById(R.id.curProt)).setText(doubleBeautifulizer(curHeuteSpeicher.getProtHeute()));
        ((EditText) findViewById(R.id.curKh)).setText(doubleBeautifulizer(curHeuteSpeicher.getKhHeute()));
        ((EditText) findViewById(R.id.curFett)).setText(doubleBeautifulizer(curHeuteSpeicher.getFettHeute()));

        if (curHeuteSpeicher.getKcalZielHeute() == -1) {
            ((EditText) findViewById(R.id.goalKcal)).setText("");
        } else {
            ((EditText) findViewById(R.id.goalKcal)).setText(doubleBeautifulizerNull(curHeuteSpeicher.getKcalZielHeute()));
        }
        if (curHeuteSpeicher.getProtZielHeute() == -1) {
            ((EditText) findViewById(R.id.goalProt)).setText("");
        } else {
            ((EditText) findViewById(R.id.goalProt)).setText(doubleBeautifulizerNull(curHeuteSpeicher.getProtZielHeute()));
        }
        if (curHeuteSpeicher.getKhZielHeute() == -1) {
            ((EditText) findViewById(R.id.goalKh)).setText("");
        } else {
            ((EditText) findViewById(R.id.goalKh)).setText(doubleBeautifulizerNull(curHeuteSpeicher.getKhZielHeute()));
        }
        if (curHeuteSpeicher.getFettZielHeute() == -1) {
            ((EditText) findViewById(R.id.goalFett)).setText("");
        } else {
            ((EditText) findViewById(R.id.goalFett)).setText(doubleBeautifulizerNull(curHeuteSpeicher.getFettZielHeute()));
        }
        curWatcherActive = true;
        goalWatcherActive = true;
    }


    private void updateUpperEditTextsCur(HeuteSpeicher curHeuteSpeicher) {
        curWatcherActive = false;
        ((EditText) findViewById(R.id.curKcal)).setText(doubleBeautifulizer(curHeuteSpeicher.getKcalHeute()));
        ((EditText) findViewById(R.id.curProt)).setText(doubleBeautifulizer(curHeuteSpeicher.getProtHeute()));
        ((EditText) findViewById(R.id.curKh)).setText(doubleBeautifulizer(curHeuteSpeicher.getKhHeute()));
        ((EditText) findViewById(R.id.curFett)).setText(doubleBeautifulizer(curHeuteSpeicher.getFettHeute()));
        curWatcherActive = true;
    }

    private void updateTrackerDisplayed(ArrayList<HeuteSpeicher> HSL) {
        boolean kcalTrackerDisplayed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("displayTrackerKcal", true);
        boolean protTrackerDisplayed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("displayTrackerProt", true);
        boolean khTrackerDisplayed   = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("displayTrackerKh",   true);
        boolean fettTrackerDisplayed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("displayTrackerFett", true);

        HeuteSpeicher HS = HSL.get(0);

        HS.setTrackKcal(kcalTrackerDisplayed);
        HS.setTrackProt(protTrackerDisplayed);
        HS.setTrackKh(khTrackerDisplayed);
        HS.setTrackFett(fettTrackerDisplayed);

        Speicher.saveHeuteSpeicherListe(this, HSL);

        View upperKcal = findViewById(R.id.kcalTrackerLayout);
        View upperProt = findViewById(R.id.protTrackerLayout);
        View upperKh   = findViewById(R.id.khTrackerLayout);
        View upperFett = findViewById(R.id.fettTrackerLayout);
        View lowerKcal = findViewById(R.id.kcalToAddLayout);
        View lowerProt = findViewById(R.id.protToAddLayout);
        View lowerKh   = findViewById(R.id.khToAddLayout);
        View lowerFett = findViewById(R.id.fettToAddLayout);

        if (kcalTrackerDisplayed) {
            upperKcal.setVisibility(View.VISIBLE);
            lowerKcal.setVisibility(View.VISIBLE);
        } else {
            upperKcal.setVisibility(View.GONE);
            lowerKcal.setVisibility(View.GONE);
        }
        if (protTrackerDisplayed) {
            upperProt.setVisibility(View.VISIBLE);
            lowerProt.setVisibility(View.VISIBLE);
        } else {
            upperProt.setVisibility(View.GONE);
            lowerProt.setVisibility(View.GONE);
        }
        if (khTrackerDisplayed) {
            upperKh.setVisibility(View.VISIBLE);
            lowerKh.setVisibility(View.VISIBLE);
        } else {
            upperKh.setVisibility(View.GONE);
            lowerKh.setVisibility(View.GONE);
        }
        if (fettTrackerDisplayed) {
            upperFett.setVisibility(View.VISIBLE);
            lowerFett.setVisibility(View.VISIBLE);
        } else {
            upperFett.setVisibility(View.GONE);
            lowerFett.setVisibility(View.GONE);
        }
    }


    private void dailyReset(ArrayList<HeuteSpeicher> curHSL) {
        HeuteSpeicher curHS = curHSL.get(0);

        SimpleDateFormat formatter = new SimpleDateFormat("dd. MMM yyyy");
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        today = calendar.getTime();


        if (!formatter.format(curHS.getDate()).equals(formatter.format(today))) {

            curWatcherActive = false;
            ((EditText) findViewById(R.id.curKcal)).setText("");
            ((EditText) findViewById(R.id.curProt)).setText("");
            ((EditText) findViewById(R.id.curKh)).setText("");
            ((EditText) findViewById(R.id.curFett)).setText("");
            curWatcherActive = true;

            changeToUnknownCurGerichtWatcherActive = false;
            ((EditText) findViewById(R.id.toAddKcal)).setText("");
            ((EditText) findViewById(R.id.toAddProt)).setText("");
            ((EditText) findViewById(R.id.toAddKh)).setText("");
            ((EditText) findViewById(R.id.toAddFett)).setText("1");
            changeToUnknownCurGerichtWatcherActive = true;
            ((EditText) findViewById(R.id.toAddFett)).setText("");

            RueckblickDialog rueckblickDialog = new RueckblickDialog(curHS);
            rueckblickDialog.show(getSupportFragmentManager(), "rueckblick dialog");
            //Toast.makeText(this, "new day", Toast.LENGTH_SHORT).show();

            HeuteSpeicher newHS = new HeuteSpeicher();
            newHS.setKcalZielHeute(curHS.getKcalZielHeute());
            newHS.setProtZielHeute(curHS.getProtZielHeute());
            newHS.setKhZielHeute(curHS.getKhZielHeute());
            newHS.setFettZielHeute(curHS.getFettZielHeute());
            curHSL.add(0, newHS);

            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("limitStorage", false)) {
                int limitTo = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("limitStorageTo", "0x7fffffff"));
                while (curHSL.size() > limitTo + 1) {
                    curHSL.remove(curHSL.size() - 1);
                }
            }
            Speicher.saveHeuteSpeicherListe(this, curHSL);
        }
    }
}
