package com.example.ernaehrungstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class HistorieBearbeitenActivity extends AppCompatActivity implements HistorischeGerichteAdapter.ItemClickListener, HHClickDialog.HHClickDialogListener, UmbenennenDialog.UmbenennenDialogListener {

    ArrayList<HeuteSpeicher> HSL;
    
    RecyclerView recyView;
    LinearLayout nixGetracktLayout;
    EditText curKcal;
    EditText curProt;
    EditText curKh;
    EditText curFett;
    EditText goalKcal;
    EditText goalProt;
    EditText goalKh;
    EditText goalFett;

    int posDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie_bearbeiten);

        //receive posDay
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            posDay = extras.getInt("posDay");
        }

        HSL = Speicher.loadHeuteSpeicherListe(this);
        
        recyView = findViewById(R.id.RecyViewHistorisch);
        nixGetracktLayout = findViewById(R.id.nichtsGetracktLayoutHistorisch);
        curKcal = findViewById(R.id.curKcalHistorisch);
        curProt = findViewById(R.id.curProtHistorisch);
        curKh   = findViewById(R.id.curKhHistorisch);
        curFett = findViewById(R.id.curFettHistorisch);
        goalKcal = findViewById(R.id.goalKcalHistorisch);
        goalProt = findViewById(R.id.goalProtHistorisch);
        goalKh   = findViewById(R.id.goalKhHistorisch);
        goalFett = findViewById(R.id.goalFettHistorisch);
        
        //upper initialisieren
        updateUpperEditTexts(HSL);

        //recy View initialisieren
        updateLowerRecyView(HSL);

        //TODO add listeners (nachträgliche Änderungen)
    }

    @Override
    public void onItemClickHG(View view, int position) {
        HHClickDialog hhClickDialog = new HHClickDialog(position, posDay);
        hhClickDialog.show(getSupportFragmentManager(), "hh click dialog");
    }


    @Override
    public void applyHHChange(ArrayList<HeuteSpeicher> HSL) {
        updateUpperEditTexts(HSL);
        updateLowerRecyView(HSL);
    }

    @Override
    public void applyUmbenennen(ArrayList<HeuteSpeicher> HSL) {
        updateUpperEditTexts(HSL);
        updateLowerRecyView(HSL);
    }



    private void updateUpperEditTexts(ArrayList<HeuteSpeicher> HSL) {
        HeuteSpeicher HS = HSL.get(posDay);

        curKcal.setText("" + HS.getKcalHeute());
        curProt.setText("" + HS.getProtHeute());
        curKh.setText(  "" + HS.getKhHeute());
        curFett.setText("" + HS.getFettHeute());
        goalKcal.setText("" + HS.getKcalZielHeute());
        goalProt.setText("" + HS.getProtZielHeute());
        goalKh  .setText("" + HS.getKhZielHeute());
        goalFett.setText("" + HS.getFettZielHeute());

    }

    private void updateLowerRecyView(ArrayList<HeuteSpeicher> HSL) {
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        HistorischeGerichteAdapter HGA = new HistorischeGerichteAdapter(this, HSL.get(posDay).getGegesseneGerichte());
        HGA.setmClickListener(this);
        recyView.setAdapter(HGA);

        if (HSL.get(posDay).getGegesseneGerichte().size() != 0) {
            recyView.setVisibility(View.VISIBLE);
            nixGetracktLayout.setVisibility(View.GONE);
        } else {
            recyView.setVisibility(View.GONE);
            nixGetracktLayout.setVisibility(View.VISIBLE);
        }
    }
    
}
