package com.example.ernaehrungstracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class GewichtSpeichernDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gewicht_einspeichern, null);

        final EditText gewichtInput = view.findViewById(R.id.toSaveGewichtEditText);
        final ArrayList<HeuteSpeicher> tempHSL = Speicher.loadHeuteSpeicherListe(getActivity());

        gewichtInput.setFilters(new InputFilter[]{new InputFilterDecimal(3, 1)});
        if (tempHSL.get(0).getGewicht() != -1) gewichtInput.setHint("" + MainActivity.doubleBeautifulizerNull(tempHSL.get(0).getGewicht()));

        builder.setView(view)
                .setNegativeButton(getActivity().getResources().getString(R.string.abbrechen_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                })
                .setPositiveButton(getActivity().getResources().getString(R.string.speichern_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            String input = gewichtInput.getText().toString();
                            if (input.equals("")) {
                                //TODO offen lassen?
                                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.kein_gewicht_angegeben), Toast.LENGTH_SHORT).show();
                            } else {
                                double inputD = ((double) (Math.round(Double.parseDouble(input) * 10)) / 10);
                                tempHSL.get(0).setGewicht(inputD);
                                Speicher.saveHeuteSpeicherListe(getActivity(), tempHSL);
                                Toast.makeText(getContext(), "" + getActivity().getResources().getString(R.string.gewicht) + " (" + inputD + getActivity().getResources().getString(R.string.kg) + ") " + getActivity().getResources().getString(R.string.gespeichert), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }

}
