package com.example.ernaehrungstracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class HistorieActivity extends AppCompatActivity implements HeuteHistorieAdapter.ItemClickListener, HistorieAdapter.ItemClickListener, HHClickDialog.HHClickDialogListener, UmbenennenDialog.UmbenennenDialogListener, DatePickerDialog.OnDateSetListener {

    Boolean displayDetails;

    Date graphFrom;
    Date graphTo;
    boolean graphKcal = true;
    boolean graphFett = true;
    boolean graphKh   = true;
    boolean graphProt = true;
    boolean graphGewicht = true;
    boolean fromGetsEdited;

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

        //graph
        updateGraph();
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

    public void kcalButtonClicked(View view) {
        Button b = findViewById(R.id.toggleButton1);
        TextView t = findViewById(R.id.kcalLabelText);
        if (graphKcal) {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.ausgegraut));
            b.setTextColor(getResources().getColor(R.color.mittelDarkText));
            graphKcal = false;
        } else {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.rosa));
            b.setTextColor(0xFFFFFFFF);
            graphKcal = true;
        }
        updateGraph();
    }

    public void fettButtonClicked(View view) {
        Button b = findViewById(R.id.toggleButton2);
        if (graphFett) {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.ausgegraut));
            b.setTextColor(getResources().getColor(R.color.mittelDarkText));
            graphFett = false;
        } else {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.gelb));
            b.setTextColor(0xFFFFFFFF);
            graphFett = true;
        }
        updateGraph();
    }

    public void khButtonClicked(View view) {
        Button b = findViewById(R.id.toggleButton3);
        if (graphKh) {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.ausgegraut));
            b.setTextColor(getResources().getColor(R.color.mittelDarkText));
            graphKh = false;
        } else {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.gruen));
            b.setTextColor(0xFFFFFFFF);
            graphKh = true;
        }
        updateGraph();
    }

    public void protButtonClicked(View view) {
        Button b = findViewById(R.id.toggleButton4);
        if (graphProt) {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.ausgegraut));
            b.setTextColor(getResources().getColor(R.color.mittelDarkText));
            graphProt = false;
        } else {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.rot));
            b.setTextColor(0xFFFFFFFF);
            graphProt = true;
        }
        updateGraph();
    }

    public void gewichtButtonClicked(View view) {
        Button b = findViewById(R.id.toggleButton5);
        if (graphGewicht) {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.ausgegraut));
            b.setTextColor(getResources().getColor(R.color.mittelDarkText));
            graphGewicht = false;
        } else {
            b.setBackgroundTintList(getResources().getColorStateList(R.color.dunkelgrau));
            b.setTextColor(0xFFFFFFFF);
            graphGewicht = true;
        }
        updateGraph();
    }

    public void fromButtonClicked(View view) {
        fromGetsEdited = true;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "REPLACE");
    }

    public void toButtonClicked(View view) {
        fromGetsEdited = false;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "REPLACE");
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        String curDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());

        if (fromGetsEdited) {
            ((Button) findViewById(R.id.fromButton)).setText(curDateString);
            graphFrom = c.getTime();
        } else {
            ((Button) findViewById(R.id.toButton)).setText(curDateString);
            graphTo = c.getTime();
        }

        class asyncUpdateGraphTask extends AsyncTask<MainActivity, Void, Void> {
            @Override
            protected Void doInBackground(MainActivity... params) {
                updateGraph();
                return null;
            }
        }
        new asyncUpdateGraphTask().execute();
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

    private void updateGraph() {
        ArrayList<HeuteSpeicher> HSL = Speicher.loadHeuteSpeicherListe(this);
        GraphView myGraphView = findViewById(R.id.myGraphView);
        myGraphView.removeAllSeries();
        myGraphView.clearSecondScale();

        if (graphFrom == null || graphTo == null) {
            graphFrom = HSL.get(HSL.size() - 1).getDate();
            graphTo = HSL.get(0).getDate();
            ((Button) findViewById(R.id.fromButton)).setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(graphFrom));
            ((Button) findViewById(R.id.toButton)).setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(graphTo));
        }

        PointsGraphSeries.CustomShape smallPoint = new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(15);
                canvas.drawPoint(x, y, paint);
            }
        };

        PointsGraphSeries.CustomShape smallCross = new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(5);
                canvas.drawLine(x-8, y-8, x+8, y+8, paint);
                canvas.drawLine(x+8, y-8, x-8, y+8, paint);
            }
        };

        //outside of if because its also used to determine first and last actually tracked day later
        PointsGraphSeries<DataPoint> kcalSeries = getDataPoints(HSL, 1);
        if (graphKcal) {
            kcalSeries.setColor(0xFFEA80FC);
            kcalSeries.setCustomShape(smallPoint);
            myGraphView.getSecondScale().addSeries(kcalSeries);
        }

        /// the y bounds are always manual for second scale
        myGraphView.getSecondScale().setMinY(0);
        myGraphView.getSecondScale().setMaxY(4000);     //TODO

        if (graphFett) {
            PointsGraphSeries<DataPoint> fettSeries = getDataPoints(HSL, 2);
            fettSeries.setColor(0xFFccbf08);
            fettSeries.setCustomShape(smallPoint);
            myGraphView.addSeries(fettSeries);
        }

        if (graphKh) {
            PointsGraphSeries<DataPoint> khSeries   = getDataPoints(HSL, 3);
            khSeries.setColor(0xFF82c959);
            khSeries.setCustomShape(smallPoint);
            myGraphView.addSeries(khSeries);
        }

        if (graphProt) {
            PointsGraphSeries<DataPoint> protSeries = getDataPoints(HSL, 4);
            protSeries.setColor(0xFFd36234);
            protSeries.setCustomShape(smallPoint);
            myGraphView.addSeries(protSeries);
        }

        if (graphGewicht) {
            LineGraphSeries<DataPoint> gewichtSeriesLine = getGewichtDataPoints(HSL);
            gewichtSeriesLine.setColor(0xFF303030);
            myGraphView.addSeries(gewichtSeriesLine);

            PointsGraphSeries<DataPoint> gewichtSeriesPoints = getDataPoints(HSL, 5);
            gewichtSeriesPoints.setColor(0xFF303030);
            gewichtSeriesPoints.setCustomShape(smallCross);
            myGraphView.addSeries(gewichtSeriesPoints);
        }

        // label y axis
        //myGraphView.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.kilo_gram));

        // title overlaps with y-labels for some reason if title set and colored. Used because only than y-labels get completely printed
        myGraphView.getSecondScale().setVerticalAxisTitle(" ");
        //myGraphView.getSecondScale().setVerticalAxisTitleColor(0xFF000000);

        // set date label formatter
        myGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        // TODO nicht hsl size sondern aktuelle auswahl
        myGraphView.getGridLabelRenderer().setNumHorizontalLabels(Math.min(HSL.size(), 4)); // only 4 because of the space

        // set manual x bounds to have nice steps
        myGraphView.getViewport().setMinX(kcalSeries.getLowestValueX());
        myGraphView.getViewport().setMaxX(kcalSeries.getHighestValueX()+1000*60*60);
        myGraphView.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        myGraphView.getGridLabelRenderer().setHumanRounding(false, true);
    }

    private PointsGraphSeries<DataPoint> getDataPoints(ArrayList<HeuteSpeicher> HSL, int mode) {
        LinkedList<DataPoint> dataPoints = new LinkedList<>();
        for (int i = HSL.size()-1; i >= 0; i--) {
            HeuteSpeicher hs = HSL.get(i);
            Date d = hs.getDate();
            if ( (d.after(graphFrom) || (d.equals(graphFrom))) && ((d.before(graphTo)) || (d.equals(graphTo))) ) {
                if      (mode == 1 && hs.isTrackKcal()) dataPoints.add(new DataPoint(d.getTime(), hs.getKcalHeute()));
                else if (mode == 2 && hs.isTrackFett()) dataPoints.add(new DataPoint(d.getTime(), hs.getFettHeute()));
                else if (mode == 3 && hs.isTrackKh())   dataPoints.add(new DataPoint(d.getTime(), hs.getKhHeute()));
                else if (mode == 4 && hs.isTrackProt()) dataPoints.add(new DataPoint(d.getTime(), hs.getProtHeute()));
                else if (mode == 5 && hs.getGewicht() >= 0) dataPoints.add(new DataPoint(d.getTime(), hs.getGewicht()));
            }
        }
        //useless wenn man nicht an der zeit vom handy rumspielt
        dataPoints.sort(new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint o1, DataPoint o2) {
                return (int) (o1.getX() - o2.getX());
            }
        });
        DataPoint[] asArray = dataPoints.toArray(new DataPoint[0]);
        return new PointsGraphSeries<>(asArray);
    }

    private LineGraphSeries<DataPoint> getGewichtDataPoints(ArrayList<HeuteSpeicher> HSL) {
        LinkedList<DataPoint> dataPoints = new LinkedList<>();
        for (int i = HSL.size()-1; i >= 0; i--) {
            HeuteSpeicher hs = HSL.get(i);
            Date d = hs.getDate();
            if ( (d.after(graphFrom) || (d.equals(graphFrom))) && ((d.before(graphTo)) || (d.equals(graphTo))) ) {
                if (hs.getGewicht() >= 0) dataPoints.add(new DataPoint(d.getTime(), hs.getGewicht()));
            }
        }
        //useless wenn man nicht an der zeit vom handy rumspielt
        dataPoints.sort(new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint o1, DataPoint o2) {
                return (int) (o1.getX() - o2.getX());
            }
        });
        DataPoint[] asArray = dataPoints.toArray(new DataPoint[0]);
        return new LineGraphSeries<>(asArray);
    }
}
