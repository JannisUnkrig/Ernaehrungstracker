package com.example.ernaehrungstracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;

public class RueckblickDialog extends AppCompatDialogFragment {

    private HeuteSpeicher HS;

    public RueckblickDialog(HeuteSpeicher HS) {
        this.HS = HS;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd. MMM yyyy");
        String title = getString(R.string.tagesbilanz_vom) + " " + formatter.format(HS.getDate());

        String message = "";
        if (HS.isTrackKcal()) {
            message += MainActivity.doubleBeautifulizerNull(HS.getKcalHeute());
            if (HS.getKcalZielHeute() != -1) message += " / " + MainActivity.doubleBeautifulizerNull(HS.getKcalZielHeute());
            message += " " + getString(R.string.kcal) + "\n";
        }

        if (HS.isTrackProt()) {
            message += MainActivity.doubleBeautifulizerNull(HS.getProtHeute()) ;
            if (HS.getProtZielHeute() != -1) message += " / " + MainActivity.doubleBeautifulizerNull(HS.getProtZielHeute());
            message += " " + getString(R.string.g_prot) + "\n";
        }

        if (HS.isTrackKh()) {
            message += MainActivity.doubleBeautifulizerNull(HS.getKhHeute());
            if (HS.getKhZielHeute() != -1) message += " / " + MainActivity.doubleBeautifulizerNull(HS.getKhZielHeute());
            message += " " + getString(R.string.g_kh) + "\n";
        }

        if (HS.isTrackFett()) {
            message += MainActivity.doubleBeautifulizerNull(HS.getFettHeute());
            if (HS.getFettZielHeute() != -1) message += " / " + MainActivity.doubleBeautifulizerNull(HS.getFettZielHeute());
            message += " " + getString(R.string.g_fett) + "\n";
        }

        if (message.equals("")) message += getString(R.string.keine_naehrwerte_getrackt);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nix machen
                    }
                });

        return builder.create();
    }
}