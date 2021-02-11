package com.annequinpoulain.biblioygg;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_livres")
public class Livre {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String nom;
    private double prix;
    private String auteur;
    private String cat;
    private String description;
    private char stat;
    private String email;
    private String imgpath;


    public Livre(String nom, double prix, String auteur, String cat, String description, char stat, String imgpath){
        this.nom = nom;
        this.prix = prix;
        this.auteur = auteur;
        this.cat = cat;
        this.description = description;
        this.stat = stat;
        this.email = SingletonSession.Instance().getUser().getEmail();
        this.imgpath = imgpath;
    }

    public int getId() {
        return id;
    }
    public String getNom(){
        return nom;
    }
    public double getPrix(){
        return prix;
    }
    public String getEmail() {
        return email;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCat() {
        return cat;
    }

    public String getDescription() {
        return description;
    }

    public char getStat() {
        return stat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setStat(char stat) {
        this.stat = stat;
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
