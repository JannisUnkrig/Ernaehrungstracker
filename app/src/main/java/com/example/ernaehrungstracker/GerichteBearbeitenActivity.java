package com.example.ernaehrungstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        //inPortionen?
        if (position == 0) inPortionen = true;
        if (position == 0) inPortionen = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void neuesGerichtButtonClicked(View view) {
        neuesGerichtModus = true;
    }


    public void gerichteBearbeitenButtonClicked(View view) {
        neuesGerichtModus = false;
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

            //gericht speichern
            //String name, String description, double portionGramm, boolean inPortionen, double kcal, double prot, double kh, double fett, Note kcalNote, Note protNote, Note khNote, Note fettNote
            new Gericht(userInputName, userInputDescription, ) //TODO)
        }

        //TODO änderung speichern
    }
}
