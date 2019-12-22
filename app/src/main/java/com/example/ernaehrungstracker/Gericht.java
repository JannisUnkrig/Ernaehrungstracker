package com.example.ernaehrungstracker;

import java.util.ArrayList;

public class Gericht {

    private static int identificationNumber = 0;
    private static ArrayList<Gericht> gerichteListe;

    private String name;
    private long id;

    private int kcal;
    private int prot;
    private int kh;
    private int fett;

    private Note kcalNote = Note.NEUTRAL;
    private Note protNote = Note.NEUTRAL;
    private Note khNote = Note.NEUTRAL;
    private Note fettNote = Note.NEUTRAL;

    public Gericht(int kcal, int prot, int kh, int fett) {
        this.name = "unbekanntes Gericht";
        this.id = identificationNumber++;
        this.kcal = kcal;
        this.prot = prot;
        this.kh = kh;
        this.fett = fett;
    }

    public Gericht(String name, int kcal, int prot, int kh, int fett, boolean save) {
        new Gericht(kcal, prot, kh, fett);
        this.name = name;
        //TODO
        if (save) gerichteListe.add(this);
    }

    public Gericht(String name, int kcal, int prot, int kh, int fett, Note kcalNote, Note protNote, Note khNote, Note fettNote, boolean save) {
        new Gericht(name, kcal, prot, kh, fett, save);
        this.kcalNote = kcalNote;
        this.protNote = protNote;
        this.khNote = khNote;
        this.fettNote = fettNote;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getKcal() {
        return kcal;
    }

    public int getProt() {
        return prot;
    }

    public int getKh() {
        return kh;
    }

    public int getFett() {
        return fett;
    }

    public Note getKcalNote() {
        return kcalNote;
    }

    public Note getProtNote() {
        return protNote;
    }

    public Note getKhNote() {
        return khNote;
    }

    public Note getFettNote() {
        return fettNote;
    }
}
