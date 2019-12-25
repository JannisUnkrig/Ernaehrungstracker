package com.example.ernaehrungstracker;

import java.util.ArrayList;

public class Gericht {

    private static int identificationNumber = 0;
    public static ArrayList<Gericht> gerichteListe = null;

    public static Gericht currentGericht = null;


    private String name = "unbekanntes Gericht";
    private long id;
    private String description = "";

    private double portionenGramm = 1;
    private boolean inPortionen = true;

    private double kcal;
    private double prot;
    private double kh;
    private double fett;

    private Note kcalNote = Note.NEUTRAL;
    private Note protNote = Note.NEUTRAL;
    private Note khNote = Note.NEUTRAL;
    private Note fettNote = Note.NEUTRAL;


    public Gericht(double kcal, double prot, double kh, double fett) {
        this.id = identificationNumber++;
        this.kcal = kcal;
        this.prot = prot;
        this.kh = kh;
        this.fett = fett;
    }

    //ab diesem konstruktor werden gerichte gespeichert
    public Gericht(String name, String description, double portionGramm, boolean inPortionen, double kcal, double prot, double kh, double fett) {
        this(kcal, prot, kh, fett);
        this.name = name;
        this.description = description;
        this.portionenGramm = portionGramm;
        this.inPortionen = inPortionen;

        //TODO
        gerichteListe.add(this);
    }

    public Gericht(String name, String description, double portionGramm, boolean inPortionen, double kcal, double prot, double kh, double fett, Note kcalNote, Note protNote, Note khNote, Note fettNote) {
        this(name, description, portionGramm, inPortionen, kcal, prot, kh, fett);
        this.kcalNote = kcalNote;
        this.protNote = protNote;
        this.khNote = khNote;
        this.fettNote = fettNote;
    }

    public String getName() {
        return name;
    }

    public long getId() { return id; }

    public String getDescription() {
        return description;
    }

    public double getPortionenGramm() { return portionenGramm; }

    public boolean isInPortionen() { return inPortionen; }

    public double getKcal() {
        return kcal;
    }

    public double getProt() {
        return prot;
    }

    public double getKh() {
        return kh;
    }

    public double getFett() { return fett; }

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



    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPortionenGramm(double portionenGramm) { this.portionenGramm = portionenGramm; }

    public void setInPortionen(boolean inPortionen) { this.inPortionen = inPortionen; }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void setProt(double prot) {
        this.prot = prot;
    }

    public void setKh(double kh) {
        this.kh = kh;
    }

    public void setFett(double fett) {
        this.fett = fett;
    }

    public void setKcalNote(Note kcalNote) {
        this.kcalNote = kcalNote;
    }

    public void setProtNote(Note protNote) {
        this.protNote = protNote;
    }

    public void setKhNote(Note khNote) {
        this.khNote = khNote;
    }

    public void setFettNote(Note fettNote) {
        this.fettNote = fettNote;
    }
}
