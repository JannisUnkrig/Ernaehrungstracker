package com.example.ernaehrungstracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class DeleteDialog extends AppCompatDialogFragment {

    private int posDay;
    private DeleteDialogListener listener;

    public DeleteDialog(int posDay) {
        this.posDay = posDay;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.aufzeichnungen_loeschen))
                .setMessage(getResources().getString(R.string.aufzeichnungen_loeschen_message))
                .setPositiveButton(getString(R.string.zurueck_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                })
                .setNegativeButton(getString(R.string.loeschen_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HSL.remove(posDay);
                        Speicher.saveHeuteSpeicherListe(getActivity(), HSL);
                        listener.applyDeleteClicked(HSL);
                        Toast.makeText(getContext(), getString(R.string.tag_geloescht), Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    public interface DeleteDialogListener {
        void applyDeleteClicked(ArrayList<HeuteSpeicher> HSL);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement HHClickDialogListener");
        }
    }
}