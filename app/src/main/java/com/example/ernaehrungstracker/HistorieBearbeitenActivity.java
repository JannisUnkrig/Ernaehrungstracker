package com.example.ernaehrungstracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistorieBearbeitenActivity extends AppCompatActivity implements HistorischeGerichteAdapter.ItemClickListener, HHClickDialog.HHClickDialogListener, UmbenennenDialog.UmbenennenDialogListener {

    ArrayList<HeuteSpeicher> HSL;
    
    RecyclerView recyView;
    LinearLayout nixGetracktLayout;
    EditText curKcal;
    EditText curProt;
    EditText curKh;
    EditText curFett;
    EditText goalKcal;
    EditText goalProt;
    EditText goalKh;
    EditText goalFett;

    int posDay;
    private boolean curWatcherActive = true;
    private boolean goalWatcherActive = true;

    private boolean displayDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie_bearbeiten);

        displayDetails = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("naehrwerteInListe", true);

        //receive posDay
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            posDay = extras.getInt("posDay");
        }

        HSL = Speicher.loadHeuteSpeicherListe(this);

        ((TextView) findViewById(R.id.HeadlineDateHistorisch)).setText(HSL.get(posDay).getDate());
        
        recyView = findViewById(R.id.RecyViewHistorisch);
        nixGetracktLayout = findViewById(R.id.nichtsGetracktLayoutHistorisch);
        curKcal = findViewById(R.id.curKcalHistorisch);
        curProt = findViewById(R.id.curProtHistorisch);
        curKh   = findViewById(R.id.curKhHistorisch);
        curFett = findViewById(R.id.curFettHistorisch);
        goalKcal = findViewById(R.id.goalKcalHistorisch);
        goalProt = findViewById(R.id.goalProtHistorisch);
        goalKh   = findViewById(R.id.goalKhHistorisch);
        goalFett = findViewById(R.id.goalFettHistorisch);

        //Inputfilter
        InputFilter[] ifd = new InputFilter[]{new InputFilterDecimal(5, 1)};

        curKcal.setFilters(ifd);
        curProt.setFilters(ifd);
        curKh.setFilters(ifd);
        curFett.setFilters(ifd);
        goalKcal.setFilters(ifd);
        goalProt.setFilters(ifd);
        goalKh.setFilters(ifd);
        goalFett.setFilters(ifd);
        
        //upper initialisieren
        updateTrackerDisplayed(HSL.get(posDay));
        updateUpperEditTexts(HSL);

        //recy View initialisieren
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        updateLowerRecyView(HSL);


        //set curListener
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
                if (!curWatcherActive) return;
                ArrayList<HeuteSpeicher> curHSL = Speicher.loadHeuteSpeicherListe(getApplicationContext());
                HeuteSpeicher curHS = curHSL.get(posDay);

                String kcalString = curKcal.getText().toString();
                String protString = curProt.getText().toString();
                String khString = curKh.getText().toString();
                String fettString = curFett.getText().toString();

                double kcalDouble = 0;
                double protDouble = 0;
                double khDouble = 0;
                double fettDouble = 0;

                if (!kcalString.equals("")) kcalDouble = Double.parseDouble(kcalString);
                if (!protString.equals("")) protDouble = Double.parseDouble(protString);
                if (!khString.equals("")) khDouble = Double.parseDouble(khString);
                if (!fettString.equals("")) fettDouble = Double.parseDouble(fettString);

                double kcalDelta = (double) (Math.round((kcalDouble - curHS.getKcalHeute()) * 10)) / 10;
                double protDelta = (double) (Math.round((protDouble - curHS.getProtHeute()) * 10)) / 10;
                double khDelta = (double) (Math.round((khDouble - curHS.getKhHeute()) * 10)) / 10;
                double fettDelta = (double) (Math.round((fettDouble - curHS.getFettHeute()) * 10)) / 10;

                //Toast.makeText(MainActivity.curMainAct, "" + kcalDelta + "  " + protDelta + "  " + khDelta + "  " + fettDelta, Toast.LENGTH_SHORT).show();

                if (!curHS.getGegesseneGerichte().isEmpty()) {
                    Gericht letztes = curHS.getGegesseneGerichte().get(curHS.getGegesseneGerichte().size() - 1);

                    if (letztes.getName().equals("nachträgliche Änderung")) {
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
                            Gericht nachträglAenderung = new Gericht("nachträgliche Änderung", "", 1, true, kcalDelta, protDelta, khDelta, fettDelta);
                            curHS.gerichtEssen(nachträglAenderung);
                        }
                    }
                } else {
                    if (kcalDelta != 0 || protDelta != 0 || khDelta != 0 || fettDelta != 0) {
                        Gericht manuelleAenderung = new Gericht("nachträgliche Änderung", "", 1, true, kcalDelta, protDelta, khDelta, fettDelta);
                        curHS.gerichtEssen(manuelleAenderung);
                    }
                }

                Speicher.saveHeuteSpeicherListe(getApplicationContext(), curHSL);
                updateLowerRecyView(curHSL);
            }
        };

        curKcal.addTextChangedListener(curWatcher);
        curProt.addTextChangedListener(curWatcher);
        curKh.addTextChangedListener(curWatcher);
        curFett.addTextChangedListener(curWatcher);

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
                HeuteSpeicher curHS = curHSL.get(posDay);

                String kcalString = goalKcal.getText().toString();
                String protString = goalProt.getText().toString();
                String khString   = goalKh.getText().toString();
                String fettString = goalFett.getText().toString();

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

        goalKcal.addTextChangedListener(goalWatcher);
        goalProt.addTextChangedListener(goalWatcher);
        goalKh.addTextChangedListener(goalWatcher);
        goalFett.addTextChangedListener(goalWatcher);
    }

    @Override
    public void onItemClickHG(View view, int position) {
        HHClickDialog hhClickDialog = new HHClickDialog(position, posDay);
        hhClickDialog.show(getSupportFragmentManager(), "hh click dialog");
    }


    @Override
    public void applyHHChange(ArrayList<HeuteSpeicher> HSL) {
        updateUpperEditTexts(HSL);
        updateLowerRecyView(HSL);
    }

    @Override
    public void applyUmbenennen(ArrayList<HeuteSpeicher> HSL) {
        updateUpperEditTexts(HSL);
        updateLowerRecyView(HSL);
    }



    private void updateUpperEditTexts(ArrayList<HeuteSpeicher> HSL) {
        curWatcherActive = false;
        HeuteSpeicher HS = HSL.get(posDay);

        curKcal.setText(MainActivity.doubleBeautifulizerNull(HS.getKcalHeute()));
        curProt.setText(MainActivity.doubleBeautifulizerNull(HS.getProtHeute()));
        curKh.setText(MainActivity.doubleBeautifulizerNull(HS.getKhHeute()));
        curFett.setText(MainActivity.doubleBeautifulizerNull(HS.getFettHeute()));
        if (HS.getKcalZielHeute() != -1) goalKcal.setText(MainActivity.doubleBeautifulizerNull(HS.getKcalZielHeute()));
        if (HS.getProtZielHeute() != -1) goalProt.setText(MainActivity.doubleBeautifulizerNull(HS.getProtZielHeute()));
        if (HS.getKhZielHeute() != -1)     goalKh.setText(MainActivity.doubleBeautifulizerNull(HS.getKhZielHeute()));
        if (HS.getFettZielHeute() != -1) goalFett.setText(MainActivity.doubleBeautifulizerNull(HS.getFettZielHeute()));

        curWatcherActive = true;
    }

    private void updateLowerRecyView(ArrayList<HeuteSpeicher> HSL) {
        HistorischeGerichteAdapter HGA = new HistorischeGerichteAdapter(this, HSL.get(posDay), displayDetails);
        HGA.setmClickListener(this);
        recyView.setAdapter(HGA);

        if (HSL.get(posDay).getGegesseneGerichte().size() != 0) {
            recyView.setVisibility(View.VISIBLE);
            nixGetracktLayout.setVisibility(View.GONE);
        } else {
            recyView.setVisibility(View.GONE);
            nixGetracktLayout.setVisibility(View.VISIBLE);
        }
    }


    private void updateTrackerDisplayed(HeuteSpeicher HS) {
        View upperKcal = findViewById(R.id.kcalTrackerLayoutHistorisch);
        View upperProt = findViewById(R.id.protTrackerLayoutHistorisch);
        View upperKh   = findViewById(R.id.khTrackerLayoutHistorisch);
        View upperFett = findViewById(R.id.fettTrackerLayoutHistorisch);


        if (HS.isTrackKcal()) {
            upperKcal.setVisibility(View.VISIBLE);
        } else {
            upperKcal.setVisibility(View.GONE);
        }

        if (HS.isTrackProt()) {
            upperProt.setVisibility(View.VISIBLE);
        } else {
            upperProt.setVisibility(View.GONE);
        }

        if (HS.isTrackKh()) {
            upperKh.setVisibility(View.VISIBLE);
        } else {
            upperKh.setVisibility(View.GONE);
        }

        if (HS.isTrackFett()) {
            upperFett.setVisibility(View.VISIBLE);
        } else {
            upperFett.setVisibility(View.GONE);
        }
    }
    
}
