package com.annequinpoulain.biblioygg;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String password;

    public User(String nom, String prenom, String email, String tel, String password)
    {
        this.id = 0;
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setTel(tel);
        setPassword(password);
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPassword(String password) {
        this.password = SingletonSession.getSha256Hash(password);
    }

    public void setPassNoHash(String password){
        this.password = password;
    }
}
