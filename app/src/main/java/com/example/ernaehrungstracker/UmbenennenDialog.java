package com.example.ernaehrungstracker;

import android.app.Dialog;
import android.content.Context;
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

public class UmbenennenDialog extends AppCompatDialogFragment {

    ArrayList<HeuteSpeicher> HSL;
    int pos;
    int posDay;

    private UmbenennenDialogListener listener;

    UmbenennenDialog(ArrayList<HeuteSpeicher> HSL, int pos, int posDay) {
        this.HSL = HSL;
        this.pos = pos;
        this.posDay = posDay;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.umbenennen, null);

        final EditText nameInput = view.findViewById(R.id.toSaveNameEditText);

        builder.setView(view)
                .setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                })
                .setPositiveButton("speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String input = nameInput.getText().toString();
                        if (input.equals("")) {
                            //TODO offen lassen?
                            Toast.makeText(getContext(), "keinen Namen angegeben", Toast.LENGTH_SHORT).show();
                        } else {
                            HSL.get(posDay).getGegesseneGerichte().get(pos).setName(input);
                            Speicher.saveHeuteSpeicherListe(getActivity(), HSL);
                            listener.applyUmbenennen(HSL);
                            Toast.makeText(getContext(), "zu " + input + " umbenannt", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }

    public interface UmbenennenDialogListener {
        void applyUmbenennen(ArrayList<HeuteSpeicher> HSL);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (UmbenennenDialog.UmbenennenDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement UmbenennenDialogListener");
        }
    }

}
