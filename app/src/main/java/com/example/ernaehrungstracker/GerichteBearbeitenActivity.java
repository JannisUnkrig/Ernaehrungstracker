package com.example.ernaehrungstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GerichteBearbeitenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean inPortionen = true;
    private boolean neuesGerichtModus = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerichte_bearbeiten);

        Spinner spinner = findViewById(R.id.portionenGrammSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.portionenGramm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //farbe und größe von portionen/gramm
        ((TextView) parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
        ((TextView) parent.getChildAt(0)).setTextSize(14);


        //inPortionen?
        if (position == 0) {
            inPortionen = true;
            ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("1");
            Toast.makeText(this, "portionen", Toast.LENGTH_SHORT).show();
        }
        if (position == 1) {
            inPortionen = false;
            ((EditText) findViewById(R.id.gerichteBearbeitenPortionenGrammEditText)).setHint("100");
            Toast.makeText(this, "Gramm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void neuesGerichtButtonClicked(View view) {
        neuesGerichtModus = true;
        findViewById(R.id.neuesGerichtButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.dunkellila));
        findViewById(R.id.gerichteBearbeitenButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.helllila));
        findViewById(R.id.gerichteBearbeitenLöschenButton).setVisibility(View.GONE);
        //Toast.makeText(this, "neuesGericht = true", Toast.LENGTH_SHORT).show();
    }


    public void gerichteBearbeitenButtonClicked(View view) {
        neuesGerichtModus = false;
        findViewById(R.id.neuesGerichtButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.helllila));
        findViewById(R.id.gerichteBearbeitenButton).setBackgroundTintList(this.getResources().getColorStateList(R.color.dunkellila));
        findViewById(R.id.gerichteBearbeitenLöschenButton).setVisibility(View.VISIBLE);
        //Toast.makeText(this, "neuesGericht = false", Toast.LENGTH_SHORT).show();
        //TODO recy öffnen
    }



    public void löschenButtonClicked(View view) {
        if (neuesGerichtModus) return;
        //TODO löschen
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

        if (neuesGerichtModus) {
            //fehler: name leer
            if (userInputName.equals("")) {
                Toast.makeText(this, "Namen angeben", Toast.LENGTH_SHORT).show();
                return;
            }

            //fehler: name & beschreibung bereits vorhanden
            for(Gericht i : Gericht.gerichteListe) {
                if (i.getName().equals(userInputName)) {
                    if (i.getDescription().equals(userInputDescription)) {
                        Toast.makeText(this, "Gericht bereits gespeichert", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Namen oder Beschreibung ändern", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            //fehler: alle nährwerte == 0
            if (userInputKcalD == 0 && userInputProtD == 0 && userInputKhD == 0 && userInputFettD == 0) {
                Toast.makeText(this, "Mindestens einen Nährwert angeben", Toast.LENGTH_SHORT).show();
                return;
            }

            //gericht speichern
            Gericht.gerichteListe.add(new Gericht(userInputName, userInputDescription, displayedPortionenGramm, inPortionen, userInputKcalD, userInputProtD, userInputKhD, userInputFettD));
            Toast.makeText(this, "" + userInputName + " gespeichert", Toast.LENGTH_SHORT).show();
        }

        //TODO änderung speichern
    }
}
