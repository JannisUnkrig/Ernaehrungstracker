package com.example.ernaehrungstracker;


import androidx.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({"WeakerAccess","unused"})
public class HeuteSpeicher {

    private long dateMillis;

    private ArrayList<Gericht> gegesseneGerichte = new ArrayList<>();
    private double Gewicht = -1;

    private double kcalHeute = 0;
    private double protHeute = 0;
    private double khHeute   = 0;
    private double fettHeute = 0;

    private double kcalZielHeute = -1;
    private double protZielHeute = -1;
    private double khZielHeute   = -1;
    private double fettZielHeute = -1;

    private boolean trackKcal;
    private boolean trackProt;
    private boolean trackKh;
    private boolean trackFett;


    public HeuteSpeicher() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.dateMillis = calendar.getTimeInMillis();

        trackKcal = PreferenceManager.getDefaultSharedPreferences(MainActivity.curMainAct).getBoolean("displayTrackerKcal", true);
        trackProt = PreferenceManager.getDefaultSharedPreferences(MainActivity.curMainAct).getBoolean("displayTrackerProt", true);
        trackKh   = PreferenceManager.getDefaultSharedPreferences(MainActivity.curMainAct).getBoolean("displayTrackerKh",   true);
        trackFett = PreferenceManager.getDefaultSharedPreferences(MainActivity.curMainAct).getBoolean("displayTrackerFett", true);
    }


    public void gerichtEssen(Gericht gericht) {

        gegesseneGerichte.add(gericht);

        addKcalHeute(gericht.getKcal());
        addProtHeute(gericht.getProt());
        addKhHeute(gericht.getKh());
        addFettHeute(gericht.getFett());
    }


    public void setGewicht(double gewicht) {
        Gewicht = gewicht;
    }

    public void setKcalHeute(double kcalHeute) {
        this.kcalHeute = kcalHeute;
    }

    public void setProtHeute(double protHeute) {
        this.protHeute = protHeute;
    }

    public void setKhHeute(double khHeute) {
        this.khHeute = khHeute;
    }

    public void setFettHeute(double fettHeute) {
        this.fettHeute = fettHeute;
    }

    public void addKcalHeute(double kcalHeute) {
        if (!trackKcal) return;
        this.kcalHeute += kcalHeute;

        this.kcalHeute = ((double) Math.round(this.kcalHeute * 10)) / 10;
        if (this.kcalHeute > 99999.9) this.kcalHeute = 99999.9;
    }

    public void addProtHeute(double protHeute) {
        if (!trackProt) return;
        this.protHeute += protHeute;

        this.protHeute = ((double) Math.round(this.protHeute * 10)) / 10;
        if (this.protHeute > 99999.9) this.protHeute = 99999.9;
    }

    public void addKhHeute(double khHeute) {
        if (!trackKh) return;
        this.khHeute += khHeute;

        this.khHeute = ((double) Math.round(this.khHeute * 10)) / 10;
        if (this.khHeute > 99999.9) this.khHeute = 99999.9;
    }

    public void addFettHeute(double fettHeute) {
        if (!trackFett) return;
        this.fettHeute += fettHeute;

        this.fettHeute = ((double) Math.round(this.fettHeute * 10)) / 10;
        if (this.fettHeute > 99999.9) this.fettHeute = 99999.9;
    }

    public void setKcalZielHeute(double kcalZielHeute) {
        this.kcalZielHeute = kcalZielHeute;
    }

    public void setProtZielHeute(double protZielHeute) {
        this.protZielHeute = protZielHeute;
    }

    public void setKhZielHeute(double khZielHeute) {
        this.khZielHeute = khZielHeute;
    }

    public void setFettZielHeute(double fettZielHeute) {
        this.fettZielHeute = fettZielHeute;
    }


    public long getDateMillis() {
        return dateMillis;
    }

    public String getDateForPrint() {
        return DateFormat.getDateInstance(DateFormat.DEFAULT).format(dateMillis);
    }

    public ArrayList<Gericht> getGegesseneGerichte() {
        return gegesseneGerichte;
    }

    public double getGewicht() {
        return Gewicht;
    }

    public double getKcalHeute() {
        return kcalHeute;
    }

    public double getProtHeute() {
        return protHeute;
    }

    public double getKhHeute() {
        return khHeute;
    }

    public double getFettHeute() {
        return fettHeute;
    }

    public double getKcalZielHeute() {
        return kcalZielHeute;
    }

    public double getProtZielHeute() {
        return protZielHeute;
    }

    public double getKhZielHeute() {
        return khZielHeute;
    }

    public double getFettZielHeute() {
        return fettZielHeute;
    }



    public boolean isTrackKcal() { return trackKcal; }

    public void setTrackKcal(boolean trackKcal) { this.trackKcal = trackKcal; }

    public boolean isTrackProt() { return trackProt; }

    public void setTrackProt(boolean trackProt) { this.trackProt = trackProt; }

    public boolean isTrackKh() { return trackKh; }

    public void setTrackKh(boolean trackKh) { this.trackKh = trackKh; }

    public boolean isTrackFett() { return trackFett; }

    public void setTrackFett(boolean trackFett) { this.trackFett = trackFett; }
}
