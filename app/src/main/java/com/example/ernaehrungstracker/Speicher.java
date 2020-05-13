package com.example.ernaehrungstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("WeakerAccess")
public abstract class Speicher {

    private static ArrayList<HeuteSpeicher> currentHSL = null;
    private static ArrayList<Gericht> currentGerichteListe = null;

    public static void saveHeuteSpeicherListe(final Context context, final ArrayList<HeuteSpeicher> heuteSpeicherListe ) {

        currentHSL = heuteSpeicherListe;

        class asyncStoreHSLTask extends AsyncTask<MainActivity, Void, Void> {
            @Override
            protected Void doInBackground(MainActivity... params) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(heuteSpeicherListe);
                editor.putString("heuteSpeicherListeKey", json);
                editor.apply();
                return null;
            }
        }
        new asyncStoreHSLTask().execute();
    }

    public static ArrayList<HeuteSpeicher> loadHeuteSpeicherListe(Context context) {

        if (currentHSL != null) return currentHSL;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("heuteSpeicherListeKey", null);
        Type type = new TypeToken<ArrayList<HeuteSpeicher>>() {}.getType();
        ArrayList<HeuteSpeicher> heuteSpeicherListe = gson.fromJson(json, type);

        if (heuteSpeicherListe == null) {
            heuteSpeicherListe = new ArrayList<>();
            heuteSpeicherListe.add(new HeuteSpeicher());
        }
        return heuteSpeicherListe;
    }


    public static void saveGerichteListe(final Context context, final ArrayList<Gericht> gerichteListe ) {

        currentGerichteListe = gerichteListe;

        class asyncStoreGLTask extends AsyncTask<MainActivity, Void, Void> {
            @Override
            protected Void doInBackground(MainActivity... params) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(gerichteListe);
                editor.putString("gerichteListeKey", json);
                editor.apply();
                return null;
            }
        }
        new asyncStoreGLTask().execute();
    }

    public static ArrayList<Gericht> loadGerichteListe(Context context) {

        if (currentGerichteListe != null) return currentGerichteListe;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("gerichteListeKey", null);
        Type type = new TypeToken<ArrayList<Gericht>>() {}.getType();
        ArrayList<Gericht> gerichteListe = gson.fromJson(json, type);

        if (gerichteListe == null) {
            gerichteListe = new ArrayList<>();
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.apfel),                    MainActivity.curMainAct.getString(R.string.apfel_desc), 1, true, 104, 0.7, 22.9, 0.8, Note.LOW, Note.LOW, Note.HIGH, Note.LOW));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.banane),                   MainActivity.curMainAct.getString(R.string.banane_desc), 1, true, 104, 1.2, 23.5, 0.2, Note.LOW, Note.LOW, Note.HIGH, Note.LOW));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.skyr_mit_geschmack_desira),MainActivity.curMainAct.getString(R.string.skyr_mit_geschmack_desira_desc), 250, false, 184, 22.9, 21.4, 0.2, Note.LOW, Note.HIGH, Note.NEUTRAL, Note.LOW));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.lachs),                    MainActivity.curMainAct.getString(R.string.lachs_desc), 125, false, 273, 33.3, 0, 15.5, Note.NEUTRAL, Note.HIGH, Note.LOW, Note.NEUTRAL));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.sojaflocken),              MainActivity.curMainAct.getString(R.string.sojaflocken_desc), 100, false, 403, 41, 3.1, 21, Note.NEUTRAL, Note.HIGH, Note.LOW, Note.HIGH));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.skyr_mit_sojaflocken),     MainActivity.curMainAct.getString(R.string.skyr_mit_sojaflocken_desc), 1, true, 486, 54, 24, 16, Note.NEUTRAL, Note.HIGH, Note.LOW, Note.NEUTRAL));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.milch),                    MainActivity.curMainAct.getString(R.string.milch_desc), 100, false, 47, 3.4, 4.9, 1.5, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.schuessel_muesli),         MainActivity.curMainAct.getString(R.string.schuessel_muesli_desc), 1, true, 550, 22, 84, 12, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht("Karotten Eiweiß Brot", "50g", 1, true, 114, 10.9, 5.7, 4.1, Note.NEUTRAL, Note.HIGH, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht("Düsis", "", 30, false, 113, 5.1, 18.5, 0.9, Note.LOW, Note.HIGH, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht("Kokos Müsli", "50g Düsis mit 333ml Kokosmilch", 1, true, 282, 9.5, 35, 4.8, Note.LOW, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht("Löffel Proteinpulver", "5g Seitenbacher Erdbeer-Proteinpulver", 1, true, 18.2, 4.1, 0.3, 0.1, Note.NEUTRAL, Note.HIGH, Note.LOW, Note.LOW));
            gerichteListe.add(new Gericht("Kokosmilch", "", 100, false, 28, 0.3, 4.3, 1, Note.LOW, Note.NEUTRAL, Note.NEUTRAL, Note.NEUTRAL));
            gerichteListe.add(new Gericht(MainActivity.curMainAct.getString(R.string.thunfisch), MainActivity.curMainAct.getString(R.string.thunfisch_desc), 100, false, 115, 25, 0.3, 1.3, Note.LOW, Note.HIGH, Note.LOW, Note.LOW));

        }
        return gerichteListe;
    }

}
