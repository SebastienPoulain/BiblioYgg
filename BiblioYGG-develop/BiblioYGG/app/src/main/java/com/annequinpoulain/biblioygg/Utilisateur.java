package com.annequinpoulain.biblioygg;

import com.google.gson.annotations.SerializedName;

public class Utilisateur {
    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("nom")
    String nom;

    @SerializedName("prenom")
    String prenom;

    @SerializedName("tel")
    String tel;

    public Utilisateur(String email, String password, String nom, String prenom, String tel) {
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setTel(tel);
        setPassword(password);
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTel() {
        return tel;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = SingletonSession.getSha256Hash(password);
    }

    public void setNom(String nom) {
        if (nom.trim().length() > 2)
            this.nom = nom.trim().substring(0,1).toUpperCase() + nom.trim().substring(1).toLowerCase();
        else
            this.nom = nom;
    }

    public void setPrenom(String prenom) {
        if (prenom.trim().length() > 2)
            this.prenom = prenom.trim().substring(0,1).toUpperCase() + prenom.trim().substring(1).toLowerCase();
        else
            this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
