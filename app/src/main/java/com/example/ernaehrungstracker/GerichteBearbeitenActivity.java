package com.example.ernaehrungstracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GerichteBearbeitenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean inPortionen = true;
    private boolean neuesGerichtModus = true;

    private Note kcalNote = Note.NEUTRAL;
    private Note protNote = Note.NEUTRAL;
    private Note khNote   = Note.NEUTRAL;
    private Note fettNote = Note.NEUTRAL;

    Gericht currentGericht = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerichte_bearbeiten);

        Spinner spinner = findViewById(R.id.portionenGrammSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.portionenGramm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Inputfilter
        InputFilter[] ifd = new InputFilter[]{new InputFilterDecimal(4, 1)};
        InputFilter[] ifd2 = new InputFilter[]{new InputFilterDecimal(4, 2)};

        ((EditText) findViewById(R.id.gerichteBearbeitenKcalEditText)).setFilters(ifd);
        ((EditText) findViewById(R.id.gerichteBearbeitenProtEditText)).setFilters(ifd);
        ((EditText) findViewById(R.id.gerichteBearbeitenKhEditText)).setFilters(ifd);
        ((EditText) findViewById(R.id.gerichteBearbeitenFettEditText)).setFilters(ifd);

        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setFilters(ifd2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //farbe und größe von portionen/gramm
        //((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
        ((TextView) parent.getChildAt(0)).setTextSize(14);


        //inPortionen?
        if (position == 0) {
            inPortionen = true;
            ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("1");
            //Toast.makeText(this, "portionen", Toast.LENGTH_SHORT).show();
        }
        if (position == 1) {
            inPortionen = false;
            ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("100");
            //Toast.makeText(this, "Gramm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void neuesGerichtButtonClicked(View view) {
        activateNeuesGerichtModus();
    }

    private void activateNeuesGerichtModus() {
        neuesGerichtModus = true;
        findViewById(R.id.neuesGerichtButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.buttonDunkel));
        findViewById(R.id.gerichteBearbeitenButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.buttonHell));
        findViewById(R.id.gerichteBearbeitenLöschenButton).setVisibility(View.GONE);

        currentGericht = null;
        ((EditText) findViewById(R.id.nameEditText)).setText("");
        ((EditText) findViewById(R.id.gerichteBearbeitenBeschreibungEditText)).setText("");
        ((EditText) findViewById(R.id.gerichteBearbeitenKcalEditText)).setText("");
        ((EditText) findViewById(R.id.gerichteBearbeitenProtEditText)).setText("");
        ((EditText) findViewById(R.id.gerichteBearbeitenKhEditText)).setText("");
        ((EditText) findViewById(R.id.gerichteBearbeitenFettEditText)).setText("");

        kcalNote = Note.NEUTRAL;
        protNote = Note.NEUTRAL;
        khNote   = Note.NEUTRAL;
        fettNote = Note.NEUTRAL;

        ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.ic_input_add);

        ((Spinner) findViewById(R.id.portionenGrammSpinner)).setSelection(0);
        ((TextView) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("1");
        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setText("");
    }



    public void gerichteBearbeitenButtonClicked(View view) {
        Intent intent = new Intent(this, GerichtAuswaehlenRecyclerViewActivity.class);
        startActivityForResult(intent, 2);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent returnGericht) {

        super.onActivityResult(requestCode, resultCode, returnGericht);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                neuesGerichtModus = false;
                findViewById(R.id.neuesGerichtButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.buttonHell));
                findViewById(R.id.gerichteBearbeitenButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.buttonDunkel));
                findViewById(R.id.gerichteBearbeitenLöschenButton).setVisibility(View.VISIBLE);

                currentGericht = GerichtAuswaehlenRecyclerViewActivity.uebergabeGericht;
                ((EditText) findViewById(R.id.nameEditText)).setText(currentGericht.getName());
                ((EditText) findViewById(R.id.gerichteBearbeitenBeschreibungEditText)).setText(currentGericht.getDescription());
                ((EditText) findViewById(R.id.gerichteBearbeitenKcalEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getKcal()));
                ((EditText) findViewById(R.id.gerichteBearbeitenProtEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getProt()));
                ((EditText) findViewById(R.id.gerichteBearbeitenKhEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getKh()));
                ((EditText) findViewById(R.id.gerichteBearbeitenFettEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getFett()));

                kcalNote = currentGericht.getKcalNote();
                protNote = currentGericht.getProtNote();
                khNote   = currentGericht.getKhNote();
                fettNote = currentGericht.getFettNote();

                if (kcalNote == Note.NEUTRAL) {
                    ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.ic_input_add);
                } else if (kcalNote == Note.HIGH) {
                    ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
                } else {
                    ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
                }

                if (protNote == Note.NEUTRAL) {
                    ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.ic_input_add);
                } else if (protNote == Note.HIGH) {
                    ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
                } else {
                    ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
                }

                if (khNote == Note.NEUTRAL) {
                    ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.ic_input_add);
                } else if (khNote == Note.HIGH) {
                    ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
                } else {
                    ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
                }

                if (fettNote == Note.NEUTRAL) {
                    ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.ic_input_add);
                } else if (fettNote == Note.HIGH) {
                    ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
                } else {
                    ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
                }

                if (currentGericht.isInPortionen()) {
                    ((Spinner) findViewById(R.id.portionenGrammSpinner)).setSelection(0);
                    ((TextView) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("1");
                    if (currentGericht.getPortionenGramm() != 1) {
                        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setText("");
                    }
                } else {
                    ((Spinner) findViewById(R.id.portionenGrammSpinner)).setSelection(1);
                    ((TextView) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("100");
                    if (currentGericht.getPortionenGramm() != 100) {
                        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setText(MainActivity.doubleBeautifulizer(currentGericht.getPortionenGramm()));
                    } else {
                        ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setText("");
                    }
                }

            }
        }
    }


    public void kcalNoteButtonClicked(View view) {
        if (kcalNote == Note.NEUTRAL) {
            kcalNote = Note.HIGH;
            ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
            return;
        }
        if (kcalNote == Note.HIGH){
            kcalNote = Note.LOW;
            ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
            return;
        }
        if (kcalNote == Note.LOW) {
            kcalNote = Note.NEUTRAL;
            ((ImageButton) findViewById(R.id.kcalNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        }
    }


    public void protNoteButtonClicked(View view) {
        if (protNote == Note.NEUTRAL) {
            protNote = Note.HIGH;
            ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
            return;
        }
        if (protNote == Note.HIGH){
            protNote = Note.LOW;
            ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
            return;
        }
        if (protNote == Note.LOW) {
            protNote = Note.NEUTRAL;
            ((ImageButton) findViewById(R.id.protNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        }
    }


    public void khNoteButtonClicked(View view) {
        if (khNote == Note.NEUTRAL) {
            khNote = Note.HIGH;
            ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
            return;
        }
        if (khNote == Note.HIGH){
            khNote = Note.LOW;
            ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
            return;
        }
        if (khNote == Note.LOW) {
            khNote = Note.NEUTRAL;
            ((ImageButton) findViewById(R.id.khNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        }
    }


    public void fettNoteButtonClicked(View view) {
        if (fettNote == Note.NEUTRAL) {
            fettNote = Note.HIGH;
            ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.arrow_up_float);
            return;
        }
        if (fettNote == Note.HIGH){
            fettNote = Note.LOW;
            ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.arrow_down_float);
            return;
        }
        if (fettNote == Note.LOW) {
            fettNote = Note.NEUTRAL;
            ((ImageButton) findViewById(R.id.fettNoteButton)).setImageResource(android.R.drawable.ic_input_add);
        }
    }


    public void loeschenButtonClicked(View view) {
        if (neuesGerichtModus) return;
        ArrayList<Gericht> curGerichteListe = Speicher.loadGerichteListe(this);
        Toast.makeText(this, "" + curGerichteListe.get(0).getName() + " " + this.getResources().getString(R.string.geloescht), Toast.LENGTH_SHORT).show();
        curGerichteListe.remove(0);
        Speicher.saveGerichteListe(this, curGerichteListe);
        activateNeuesGerichtModus();
    }


    public void speichernButtonClicked(View view) {
        EditText nameEditText = findViewById(R.id.nameEditText);
        String userInputName = nameEditText.getText().toString();

        EditText descriptionEditText = findViewById(R.id.gerichteBearbeitenBeschreibungEditText);
        String userInputDescription =  descriptionEditText.getText().toString();

        String userInputKcal = ((EditText) findViewById(R.id.gerichteBearbeitenKcalEditText)).getText().toString();
        String userInputProt = ((EditText) findViewById(R.id.gerichteBearbeitenProtEditText)).getText().toString();
        String userInputKh   = ((EditText) findViewById(R.id.gerichteBearbeitenKhEditText)).getText().toString();
        String userInputFett = ((EditText) findViewById(R.id.gerichteBearbeitenFettEditText)).getText().toString();
        double userInputKcalD = 0;
        double userInputProtD = 0;
        double userInputKhD   = 0;
        double userInputFettD = 0;
        if (!userInputKcal.equals("")) userInputKcalD = Double.parseDouble(userInputKcal);
        if (!userInputProt.equals("")) userInputProtD = Double.parseDouble(userInputProt);
        if (!userInputKh.equals(""))   userInputKhD   = Double.parseDouble(userInputKh);
        if (!userInputFett.equals("")) userInputFettD = Double.parseDouble(userInputFett);

        EditText portionenGrammEditText = findViewById(R.id.gerichteBearbeitenPortionenGrammEditText);
        double displayedPortionenGramm;
        if (portionenGrammEditText.getText().toString().equals("")) {
            if (inPortionen) {
                displayedPortionenGramm = 1.0;
            } else {
                displayedPortionenGramm = 100.0;
            }
        } else {
            displayedPortionenGramm = Double.parseDouble(portionenGrammEditText.getText().toString());
        }


            //fehler: name leer
        if (userInputName.trim().equals("")) {
            Toast.makeText(this, this.getResources().getString(R.string.name_angeben), Toast.LENGTH_SHORT).show();
            return;
        }

            //fehler: name verboten
        if (userInputName.toLowerCase().equals(this.getResources().getString(R.string.unbekanntes_gericht)) || userInputName.toLowerCase().equals(this.getResources().getString(R.string.manuelle_aenderung))) {
            Toast.makeText(this, this.getResources().getString(R.string.anderen_namen_angeben), Toast.LENGTH_SHORT).show();
            return;
        }

            //fehler: alle nährwerte == 0
        if (userInputKcalD == 0 && userInputProtD == 0 && userInputKhD == 0 && userInputFettD == 0) {
            Toast.makeText(this, this.getResources().getString(R.string.mindestens_einen_naehrwert_angeben), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Gericht> curGerichteListe = Speicher.loadGerichteListe(this);

        if (neuesGerichtModus) {

            //fehler: name & beschreibung bereits vorhanden
            for(Gericht i : curGerichteListe) {
                if (i.getName().toLowerCase().equals(userInputName.toLowerCase().trim())) {
                    if (i.getDescription().toLowerCase().equals(userInputDescription.toLowerCase().trim())) {
                        Toast.makeText(this, this.getResources().getString(R.string.gericht_bereits_gespeichert), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, this.getResources().getString(R.string.namen_oder_beschreibung_aendern), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            //gericht speichern
            curGerichteListe.add(0, new Gericht(   userInputName, userInputDescription, displayedPortionenGramm, inPortionen,
                                                userInputKcalD, userInputProtD, userInputKhD, userInputFettD, kcalNote, protNote, khNote, fettNote));
            Speicher.saveGerichteListe(this, curGerichteListe);
            Toast.makeText(this, "" + userInputName + " " + this.getResources().getString(R.string.gespeichert), Toast.LENGTH_SHORT).show();
            activateNeuesGerichtModus();

        } else {

            //fehler: name & beschreibung bereits vorhanden
            for(int i = 1; i < curGerichteListe.size(); i++) {
                if (curGerichteListe.get(i).getName().toLowerCase().equals(userInputName.toLowerCase().trim())) {
                    if (curGerichteListe.get(i).getDescription().toLowerCase().equals(userInputDescription.toLowerCase().trim())) {
                        Toast.makeText(this, this.getResources().getString(R.string.gericht_bereits_gespeichert), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, this.getResources().getString(R.string.namen_oder_beschreibung_aendern), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            //gericht speichern
            Gericht toChange = curGerichteListe.get(0);
            toChange.setName(userInputName);
            toChange.setDescription(userInputDescription);
            toChange.setPortionenGramm(displayedPortionenGramm);
            toChange.setInPortionen(inPortionen);
            toChange.setKcal(userInputKcalD);
            toChange.setProt(userInputProtD);
            toChange.setKh(userInputKhD);
            toChange.setFett(userInputFettD);
            toChange.setKcalNote(kcalNote);
            toChange.setProtNote(protNote);
            toChange.setKhNote(khNote);
            toChange.setFettNote(fettNote);


            Speicher.saveGerichteListe(this, curGerichteListe);
            Toast.makeText(this, this.getResources().getString(R.string.aenderungen_an) + " " + userInputName + " " + this.getResources().getString(R.string.gespeichert), Toast.LENGTH_SHORT).show();
        }

    }
}
