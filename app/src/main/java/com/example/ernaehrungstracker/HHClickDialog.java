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
            if (gericht.getPortionenGramm() != 1) {
                title += "  (" + MainActivity.doubleBeautifulizerNull(gericht.getPortionenGramm()) + " " + getString(R.string.portionen) + ")";
            }
        } else {
            if (gericht.getPortionenGramm() == 1) {
                title += "  (" + getString(R.string.ein_gramm) + ")";
            } else if (gericht.getPortionenGramm() == 100) {
                //nix
            } else {
                title += "  (" + MainActivity.doubleBeautifulizerNull(gericht.getPortionenGramm()) + " " + getString(R.string.gramm) + ")";
            }
        }

        String message = "";
        if (!gericht.getDescription().equals("")) message += gericht.getDescription() + "\n\n";


        if (HS.isTrackKcal()) message += MainActivity.doubleBeautifulizerNull(gericht.getKcal()) + " " + getString(R.string.kcal);

        if (HS.isTrackProt() && HS.isTrackKcal()) message += "  |  ";
        if (HS.isTrackProt()) message += MainActivity.doubleBeautifulizerNull(gericht.getProt()) + getString(R.string.g_prot);

        if (HS.isTrackKh() && (HS.isTrackProt() || HS.isTrackKcal())) message += "  |  ";
        if (HS.isTrackKh())   message += MainActivity.doubleBeautifulizerNull(gericht.getKh())   + getString(R.string.g_kh);

        if (HS.isTrackFett() && (HS.isTrackProt() || HS.isTrackKcal() || HS.isTrackKh())) message += "  |  ";
        if (HS.isTrackFett()) message += MainActivity.doubleBeautifulizerNull(gericht.getFett()) + getString(R.string.g_fett);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.loeschen_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HS.addKcalHeute(gericht.getKcal() * -1);
                        HS.addProtHeute(gericht.getProt() * -1);
                        HS.addKhHeute(  gericht.getKh()   * -1);
                        HS.addFettHeute(gericht.getFett() * -1);
                        gegesseneGerichte.remove(pos);
                        Speicher.saveHeuteSpeicherListe(getActivity(), HSL);
                        listener.applyHHChange(HSL);
                        Toast.makeText(getContext(), gericht.getName() + " " + getString(R.string.geloescht), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(getString(R.string.zurueck_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                });

        if (gericht.getName().equals(getString(R.string.unbekanntes_gericht)) || gericht.getName().equals(getString(R.string.manuelle_aenderung)) || gericht.getName().equals(getString(R.string.nachtraegliche_aenderung))) {
            builder.setNeutralButton(getString(R.string.benennen), new DialogInterface.OnClickListener() {
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