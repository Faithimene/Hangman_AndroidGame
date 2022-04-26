package com.example.tp2;

public class Utilisateur {
    private int _id;
    private String _username;
    private String _password;
    private  String nom;
    private  String prenom;
    private int _bestscore;

    public Utilisateur(String _username, String _password, String nom, String prenom) {

        this._username = _username;
        this._password = _password;
        this.nom = nom;
        this.prenom = prenom;

    }

    public Utilisateur(int _id, String _username, String _password, String nom, String prenom, int _bestscore) {
        this._id = _id;
        this._username = _username;
        this._password = _password;
        this.nom = nom;
        this.prenom = prenom;
        this._bestscore = _bestscore;
    }

    public int get_id() {
        return _id;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public int get_bestscore() {
        return _bestscore;
    }

    public void set_bestscore(int _bestscore) {
        this._bestscore = _bestscore;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
