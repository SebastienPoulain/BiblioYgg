package com.annequinpoulain.biblioygg;

import com.google.gson.annotations.SerializedName;

public class LivreRetrofit {

    @SerializedName("nom")
    String nom;

    @SerializedName("prix")
    double prix;

    @SerializedName("auteur")
    String auteur;

    @SerializedName("cat")
    String cat;

    @SerializedName("description")
    String description;

    @SerializedName("stat")
    char stat;

    @SerializedName("email")
    String email;

    @SerializedName("imgpath")
    String imgpath;

    public LivreRetrofit(String nom, double prix, String auteur, String cat, String description, char stat, String email, String imgpath) {
        this.nom = nom;
        this.prix = prix;
        this.auteur = auteur;
        this.cat = cat;
        this.description = description;
        this.stat = stat;
        this.email = email;
        this.imgpath = imgpath;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getStat() {
        return stat;
    }

    public void setStat(char stat) {
        this.stat = stat;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
