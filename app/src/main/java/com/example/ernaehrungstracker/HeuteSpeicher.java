package com.example.ernaehrungstracker;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class HeuteSpeicher implements Serializable {

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

    // Serializes an object and saves it to a file
    public void saveToFile(Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("heuteSpeicher.ser", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Creates an object by reading it from a file
    public static HeuteSpeicher readFromFile(Context context) {
        HeuteSpeicher createResumeForm = null;
        try {
            FileInputStream fileInputStream = context.openFileInput("heuteSpeicher.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            createResumeForm = (HeuteSpeicher) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            /*HeuteSpeicher myHeuteSpeicher  = new HeuteSpeicher();
            myHeuteSpeicher.saveToFile(context);
            readFromFile(context);*/
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return createResumeForm;
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
