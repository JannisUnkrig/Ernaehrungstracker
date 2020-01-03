package com.example.ernaehrungstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistorieActivity extends AppCompatActivity implements HeuteHistorieAdapter.ItemClickListener, HistorieAdapter.ItemClickListener, HHClickDialog.HHClickDialogListener, UmbenennenDialog.UmbenennenDialogListener {

    Boolean displayDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie);

        ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(this);

        displayDetails = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("naehrwerteInListe", true);

        //upper recyclerView
        updateUpperRecyView(HSL);

        //lower recyclerView
        updateLowerRecyView(HSL);

    }

    @Override
    public void onItemClickHH(View view, int position) {
        HHClickDialog hhClickDialog = new HHClickDialog(position, 0);
        hhClickDialog.show(getSupportFragmentManager(), "hh click dialog");
    }

    @Override
    public void onItemClickH(View view, int position) {
        if (position == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.nur_vergangene_tage_nachbearbeitbar), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent3 = new Intent(this, HistorieBearbeitenActivity.class);
        intent3.putExtra("posDay", position);
        startActivityForResult(intent3, 4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnGericht) {

        super.onActivityResult(requestCode, resultCode, returnGericht);

        //after HistorieBearbeitenActivity
        if (requestCode == 4) {
            updateLowerRecyView(Speicher.loadHeuteSpeicherListe(this));
        }
    }


    @Override
    public void applyHHChange(ArrayList<HeuteSpeicher> HSL) {
        updateUpperRecyView(HSL);
        updateLowerRecyView(HSL);
    }

    @Override
    public void applyUmbenennen(ArrayList<HeuteSpeicher> HSL) {
        updateUpperRecyView(HSL);
        updateLowerRecyView(HSL);
    }



    private void updateUpperRecyView(ArrayList<HeuteSpeicher> HSL) {
        RecyclerView hhRecyView = findViewById(R.id.heuteHistorieRecyclerView);
        hhRecyView.setHasFixedSize(true);
        hhRecyView.setLayoutManager(new LinearLayoutManager(this));
        HeuteHistorieAdapter HHA = new HeuteHistorieAdapter(this, HSL.get(0), displayDetails);
        HHA.setmClickListener(this);
        hhRecyView.setAdapter(HHA);

        if (HSL.get(0).getGegesseneGerichte().size() != 0) {
            findViewById(R.id.heuteHistorieRecyclerView).setVisibility(View.VISIBLE);
            findViewById(R.id.nichtsGetracktLayout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.heuteHistorieRecyclerView).setVisibility(View.GONE);
            findViewById(R.id.nichtsGetracktLayout).setVisibility(View.VISIBLE);
        }
    }

    private void updateLowerRecyView(ArrayList<HeuteSpeicher> HSL) {
        RecyclerView hRecyView = findViewById(R.id.historieRecyclerView);
        hRecyView.setHasFixedSize(true);
        hRecyView.setLayoutManager(new LinearLayoutManager(this));
        HistorieAdapter HA = new HistorieAdapter(this, HSL);
        HA.setmClickListener(this);
        hRecyView.setAdapter(HA);
    }
}
