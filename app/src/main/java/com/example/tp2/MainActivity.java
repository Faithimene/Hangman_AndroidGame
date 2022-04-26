package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    TextView Score,motChercher , username, password , prenom ,nom , name,bestscore;
    ImageView bonhome;
    Button lettreBtn;
    String lettre;
    StringBuilder mot,temp;
    ArrayList<String> motDispo = new ArrayList<String>();
    boolean erreur = true ,victoire = false;
    int random,point,nbErreur=0;
    MySQLiteHelper gestionnaire;
    Utilisateur user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        builder = new AlertDialog.Builder(this);
        //ajout des mot pour la liste
        motDispo.add("Soleil");
        motDispo.add("Ordinateur");
        motDispo.add("Serveur");
        motDispo.add("Eau");
        motDispo.add("Restaurant");
        motDispo.add("Soulier");
        motDispo.add("Article");
        motDispo.add("Voiture");
        motDispo.add("Football");
        motDispo.add("Examen");
        gestionnaire = new MySQLiteHelper(this, null, null, 1);
    }



    public void lettreBtn(View view) {
        //verifie quel button a etais selectionner
        lettreBtn = (Button) findViewById(view.getId());
        //Compare la lettre au mot
        for (int i = 0; i < mot.length(); i++) {
            if(lettreBtn.getText().charAt(0)==mot.charAt(i)){
                temp.setCharAt(i, lettreBtn.getText().charAt(0));
                erreur =false;
            }
        }
        //change l'image si il a eu une erreur
        if (erreur){
            nbErreur +=1;
            if (nbErreur==1){
                bonhome.setImageResource(R.drawable.err01);
            }else if(nbErreur==2){
                bonhome.setImageResource(R.drawable.err02);
            }else if(nbErreur==3){
                bonhome.setImageResource(R.drawable.err03);
            }else if(nbErreur==4){
                bonhome.setImageResource(R.drawable.err04);
            }else if(nbErreur==5){
                bonhome.setImageResource(R.drawable.err05);
            }else if(nbErreur==6){
                bonhome.setImageResource(R.drawable.err06);
                Toast.makeText(MainActivity.this,"Vous avez perdu :( ",Toast.LENGTH_LONG).show();
                if (Integer.valueOf(Score.getText().toString()) > user.get_bestscore() ){
                    user.set_bestscore(Integer.valueOf(Score.getText().toString()));
                    gestionnaire.updateUser(user);
                }
                finJeu();
            }
        }else {
            point +=1;
        }
        erreur=true;
        Score.setText(String.valueOf(point));
        motChercher.setText(temp);
        lettreBtn.setEnabled(false);
        //verifie si le mot et decouvert
        if (motChercher.getText().toString().equals(mot.toString())){
            victoire=true;
            finJeu();
        }
    }

    //Logout et ramene page principale
    public void Logoutbtn(View view) {
        if (Integer.valueOf(Score.getText().toString()) > user.get_bestscore() ){
            user.set_bestscore(Integer.valueOf(Score.getText().toString()));
            gestionnaire.updateUser(user);
        }
        point =0;
        user = null;
        setContentView(R.layout.login);
    }


    public void connexion(View view) {
        //prend les donnees rentrer
        username = findViewById(R.id.editTextUser);
        password = findViewById(R.id.editTextPswd);
        String usertxt = String.valueOf(username.getText());
        String passwd =String.valueOf(password.getText());
        user = gestionnaire.Connexion(usertxt,passwd);
        //verifie si le user existe sinon affiche une message d'erreur
        if (user != null){
            setContentView(R.layout.activity_main);
            startGame();


        }else{
            Toast.makeText(MainActivity.this," Information incorrect \n Creer un compte si necesaire ",Toast.LENGTH_LONG).show();
        }
    }

    //Reset les textView
    public void CancelLogin(View view) {
        password.setText("");
        username.setText("");
    }

    //Amene a la page insciption
    public void usercreate(View view) {
        setContentView(R.layout.inscription);
    }

    //Inscription du user dans la DB
    public void createUsers(View view) {
        username = findViewById(R.id.editTextInsUsername);
        password = findViewById(R.id.editTextInsPassword);
        prenom = findViewById(R.id.editTextInsPrenom);
        nom = findViewById(R.id.editTextInsNom);
        Utilisateur create = null;

        //verifier que aucune donnes n'est laisser vide
        if(username != null && !String.valueOf(username.getText()).isEmpty() ) {
            if(password != null && !String.valueOf(password.getText()).isEmpty() ) {
                if(prenom != null && !String.valueOf(prenom.getText()).isEmpty() ) {
                    if(nom != null && !String.valueOf(nom.getText()).isEmpty() ) {
                        //ajout de l'utilisateur
                        create = new Utilisateur(String.valueOf(username.getText()),String.valueOf(password.getText()),String.valueOf(nom.getText()),String.valueOf(prenom.getText()));
                        gestionnaire.ajouterUtilisateur(create);
                        Toast.makeText(MainActivity.this,"Utilisateur creer",Toast.LENGTH_LONG).show();
                        setContentView(R.layout.login);
                    }
                }
            }
        }else{
            Toast.makeText(MainActivity.this,"Il manque des information",Toast.LENGTH_LONG).show();
        }

    }

    public void finJeu() {
        //message de fin de partie victoire ou defaite
        builder.setMessage("Voulez recommencer une partie ou quitter ?")
                .setCancelable(false)
                .setNegativeButton("Recommencer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //reload la page et continue les points du joueur
                        setContentView(R.layout.activity_main);

                        startGame();
                        Score.setText(String.valueOf(point));

                    }
                })
                .setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Update la db pour le meilleur score
                        if (Integer.valueOf(Score.getText().toString()) > user.get_bestscore() ){
                            user.set_bestscore(Integer.valueOf(Score.getText().toString()));
                            gestionnaire.updateUser(user);
                        }

                        //enleve le user et renvoie a la page principal
                        user = null;
                        point =0;
                        setContentView(R.layout.login);
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        if (victoire){
            alert.setTitle("Victoire");
        }else{
            alert.setTitle("Perdu :( ");
        }

        alert.show();
    }

    //Fonction qui repart les donnees neccaise pour lancer ou relancer le jeu
    public void startGame(){
        nbErreur=0;
        random =(int)(Math.random() * 10);
        mot = new StringBuilder(motDispo.get(random).toUpperCase().toString());
        temp = new StringBuilder("");
        Score=findViewById(R.id.textViewScore);
        name=findViewById(R.id.textViewFullname);
        bestscore=findViewById(R.id.textViewBestScore);
        motChercher=findViewById(R.id.textViewMot);
        bonhome = findViewById(R.id.imageView);
        for (int i = 0; i < mot.length(); i++) {
            temp.append("*");
        }
        motChercher.setText(temp);
        name.setText(user.getPrenom()+" " + user.getNom());
        bestscore.setText(String.valueOf(user.get_bestscore()));

    }


}