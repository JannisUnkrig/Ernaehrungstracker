package com.example.ernaehrungstracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("WeakerAccess")
public abstract class Speicher {

    public static void saveHeuteSpeicherListe(Context context, ArrayList<HeuteSpeicher> heuteSpeicherListe ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(heuteSpeicherListe);
        editor.putString("heuteSpeicherListeKey", json);
        editor.apply();
    }

    public static ArrayList<HeuteSpeicher> loadHeuteSpeicherListe(Context context) {
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


    public static void saveGerichteListe(Context context, ArrayList<Gericht> gerichteListe ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gerichteListe);
        editor.putString("gerichteListeKey", json);
        editor.apply();
    }

    public static ArrayList<Gericht> loadGerichteListe(Context context) {
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

        }
        return gerichteListe;
    }

}
