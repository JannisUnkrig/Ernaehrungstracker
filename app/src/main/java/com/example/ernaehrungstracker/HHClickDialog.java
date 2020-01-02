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

public class HHClickDialog extends AppCompatDialogFragment {

    private int pos;
    private int posDay;
    private HHClickDialogListener listener;

    public HHClickDialog(int pos, int posDay) {
        this.pos = pos;
        this.posDay = posDay;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(getActivity());
        final HeuteSpeicher HS = HSL.get(posDay);
        final ArrayList<Gericht> gegesseneGerichte = HS.getGegesseneGerichte();
        final Gericht gericht = gegesseneGerichte.get(pos);

        String title = gericht.getName();
        if (gericht.isInPortionen()) {
            if (gericht.getPortionenGramm() == 1) {
                //mach nix
            } else {
                title += "  (" + MainActivity.doubleBeautifulizerNull(gericht.getPortionenGramm()) + " Portionen)";
            }
        } else {
            if (gericht.getPortionenGramm() == 1) {
                title += "  (ein Gramm)";
            } else {
                title += "  (" + MainActivity.doubleBeautifulizerNull(gericht.getPortionenGramm()) + " Gramm)";
            }
        }

        String message = "";
        if (!gericht.getDescription().equals("")) message += gericht.getDescription() + "\n\n";


        if (HS.isTrackKcal()) message += MainActivity.doubleBeautifulizerNull(gericht.getKcal()) + " kcal";

        if (HS.isTrackProt() && HS.isTrackKcal()) message += "  |  ";
        if (HS.isTrackProt()) message += MainActivity.doubleBeautifulizerNull(gericht.getProt()) + "g Prot";

        if (HS.isTrackKh() && (HS.isTrackProt() || HS.isTrackKcal())) message += "  |  ";
        if (HS.isTrackKh())   message += MainActivity.doubleBeautifulizerNull(gericht.getKh())   + "g KH";

        if (HS.isTrackFett() && (HS.isTrackProt() || HS.isTrackKcal() || HS.isTrackKh())) message += "  |  ";
        if (HS.isTrackFett()) message += MainActivity.doubleBeautifulizerNull(gericht.getFett()) + "g Fett";



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("entfernen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HS.addKcalHeute(gericht.getKcal() * -1);
                        HS.addProtHeute(gericht.getProt() * -1);
                        HS.addKhHeute(  gericht.getKh()   * -1);
                        HS.addFettHeute(gericht.getFett() * -1);
                        gegesseneGerichte.remove(pos);
                        Speicher.saveHeuteSpeicherListe(getActivity(), HSL);
                        listener.applyHHChange(HSL);
                        Toast.makeText(getContext(), gericht.getName() + " entfernt", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                });

        if (gericht.getName().equals("unbekanntes Gericht") || gericht.getName().equals("manuelle Änderung") || gericht.getName().equals("nachträgliche Änderung")) {
            builder.setNeutralButton("umbenennen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UmbenennenDialog umbenennenDialog = new UmbenennenDialog(HSL, pos, posDay);
                    umbenennenDialog.show(getActivity().getSupportFragmentManager(), "umbenennen dialog");
                }
            });
        }

        return builder.create();
    }

    public interface HHClickDialogListener {
        void applyHHChange(ArrayList<HeuteSpeicher> HSL);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (HHClickDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement HHClickDialogListener");
        }
    }
}