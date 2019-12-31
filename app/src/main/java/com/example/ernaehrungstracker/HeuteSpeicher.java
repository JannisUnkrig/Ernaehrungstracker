package com.example.ernaehrungstracker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings({"WeakerAccess","unused"})
public class HeuteSpeicher {

    private String date;

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


    public HeuteSpeicher() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd. MMM yyyy");
        Date date = new Date();
        this.date = formatter.format(date);
    }


    public void gerichtEssen(Gericht gericht) {

        gegesseneGerichte.add(gericht);

        kcalHeute += gericht.getKcal();
        protHeute += gericht.getProt();
        khHeute   += gericht.getKh();
        fettHeute += gericht.getFett();

        //auf eine nachkommastelle runden
        kcalHeute = ((double) Math.round(kcalHeute * 10)) / 10;
        protHeute = ((double) Math.round(protHeute * 10)) / 10;
        khHeute   = ((double) Math.round(khHeute * 10)) / 10;
        fettHeute = ((double) Math.round(fettHeute * 10)) / 10;

        //nix darf über 99999.9 sein
        if (kcalHeute > 99999.9) kcalHeute = 99999.9;
        if (protHeute > 99999.9) protHeute = 99999.9;
        if (khHeute > 99999.9) khHeute = 99999.9;
        if (fettHeute > 99999.9) fettHeute = 99999.9;

        if (kcalZielHeute > 99999.9) kcalZielHeute = 99999.9;
        if (protZielHeute > 99999.9) protZielHeute = 99999.9;
        if (khZielHeute > 99999.9) khZielHeute = 99999.9;
        if (fettZielHeute > 99999.9) fettZielHeute = 99999.9;
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
        this.kcalHeute += kcalHeute;
    }

    public void addProtHeute(double protHeute) {
        this.protHeute += protHeute;
    }

    public void addKhHeute(double khHeute) {
        this.khHeute += khHeute;
    }

    public void addFettHeute(double fettHeute) {
        this.fettHeute += fettHeute;
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

    public String getDate() { return date; }

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
}
