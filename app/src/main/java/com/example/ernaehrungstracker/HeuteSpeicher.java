package com.example.ernaehrungstracker;


import java.util.ArrayList;

public class HeuteSpeicher {

    private ArrayList<Gericht> gegesseneGerichte = new ArrayList<Gericht>();
    private int Gewicht = -1;

    private int kcalHeute = 0;
    private int protHeute = 0;
    private int khHeute   = 0;
    private int fettHeute = 0;

    private int kcalZielHeute = -1;
    private int protZielHeute = -1;
    private int khZielHeute   = -1;
    private int fettZielHeute = -1;



    public void gerichtEssen(Gericht gericht) {

        gegesseneGerichte.add(gericht);

        kcalHeute += gericht.getKcal();
        protHeute += gericht.getProt();
        khHeute   += gericht.getKh();
        fettHeute += gericht.getFett();
    }


    public void setGewicht(int gewicht) {
        Gewicht = gewicht;
    }

    public void setKcalHeute(int kcalHeute) {
        this.kcalHeute = kcalHeute;
    }

    public void setProtHeute(int protHeute) {
        this.protHeute = protHeute;
    }

    public void setKhHeute(int khHeute) {
        this.khHeute = khHeute;
    }

    public void setFettHeute(int fettHeute) {
        this.fettHeute = fettHeute;
    }

    public void setKcalZielHeute(int kcalZielHeute) {
        this.kcalZielHeute = kcalZielHeute;
    }

    public void setProtZielHeute(int protZielHeute) {
        this.protZielHeute = protZielHeute;
    }

    public void setKhZielHeute(int khZielHeute) {
        this.khZielHeute = khZielHeute;
    }

    public void setFettZielHeute(int fettZielHeute) {
        this.fettZielHeute = fettZielHeute;
    }

    public ArrayList<Gericht> getGegesseneGerichte() {
        return gegesseneGerichte;
    }

    public int getGewicht() {
        return Gewicht;
    }

    public int getKcalHeute() {
        return kcalHeute;
    }

    public int getProtHeute() {
        return protHeute;
    }

    public int getKhHeute() {
        return khHeute;
    }

    public int getFettHeute() {
        return fettHeute;
    }

    public int getKcalZielHeute() {
        return kcalZielHeute;
    }

    public int getProtZielHeute() {
        return protZielHeute;
    }

    public int getKhZielHeute() {
        return khZielHeute;
    }

    public int getFettZielHeute() {
        return fettZielHeute;
    }
}
