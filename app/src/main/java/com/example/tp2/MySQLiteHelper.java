package com.example.tp2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tp2.Utilisateur;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOM = "User.db";
    private static final String TABLE_User = "User";
    private static final String COLONNE_ID = "_id";
    private static final String COLONNE_USERNAME = "_username";
    private static final String COLONNE_PASSWORD = "_password";
    private static final String COLONNE_NOM = "_nom";
    private static final String COLONNE_PRENOM = "_prenom";
    private static final String COLONNE_BESTSCORE = "_bestscore";

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NOM, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_User + "(" +
                COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLONNE_USERNAME + " TEXT , " +
                COLONNE_PASSWORD + " TEXT , " +
                COLONNE_NOM + " TEXT , " +
                COLONNE_PRENOM + " TEXT , " +
                COLONNE_BESTSCORE + " INTEGER );";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NOM);
        onCreate(db);
    }

    public void ajouterUtilisateur(Utilisateur user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valeur = new ContentValues();
        valeur.put(COLONNE_USERNAME, user.get_username());
        valeur.put(COLONNE_PASSWORD, user.get_password());
        valeur.put(COLONNE_NOM, user.getNom());
        valeur.put(COLONNE_PRENOM, user.getPrenom());
        valeur.put(COLONNE_BESTSCORE, 0);
        db.insert(TABLE_User, null, valeur);
        db.close();
    }

    public Utilisateur Connexion(String username, String password){
        Utilisateur user = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + "User" + " WHERE " +
                COLONNE_USERNAME + "= \"" + username + "\" and " +
                COLONNE_PASSWORD + "= \"" + password + "\"";
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        if (c.getCount()>0){
            //Prend les donnes du user pour le jeu
            String COLONNE_ID = c.getString(0);
            String COLONNE_USERNAME = c.getString(1);
            String COLONNE_PASSWORD = c.getString(2);
            String COLONNE_NOM = c.getString(3);
            String COLONNE_PRENOM = c.getString(4);
            String COLONNE_BESTSCORE = c.getString(5);
            user = new Utilisateur(Integer.valueOf(c.getString(0)),c.getString(1), c.getString(2),c.getString(3),c.getString(4),Integer.valueOf(c.getString(5)));
        }
        return user;
    }

    //update la colone bestscore
    public void updateUser(Utilisateur user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLONNE_BESTSCORE, user.get_bestscore());
        db.update(TABLE_User, values, COLONNE_ID + " = ?",
                new String[]{String.valueOf(user.get_id())});
        db.close();
    }

}
