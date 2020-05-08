package com.example.ernaehrungstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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



        GraphView graph = findViewById(R.id.myGraphView);
        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d8 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d9 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d10 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d11 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d12 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d13 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d14 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d15 = calendar.getTime();

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 1),
                new DataPoint(d5, 5),
                new DataPoint(d6, 3),
                new DataPoint(d7, 1),
                new DataPoint(d8, 5),
                new DataPoint(d9, 3),
                new DataPoint(d10, 1),
                new DataPoint(d11, 1),
                new DataPoint(d12, 5),
                new DataPoint(d13, 3),
                new DataPoint(d14, 1),
                new DataPoint(d15, 5)
        });

        graph.addSeries(series);

        new DateAsXAxisLabelFormatter(MainActivity.curMainAct);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(MainActivity.curMainAct));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d15.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
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
