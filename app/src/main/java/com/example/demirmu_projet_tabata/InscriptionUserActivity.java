package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demirmu_projet_tabata.db.DatabaseClient;
import com.example.demirmu_projet_tabata.db.Utilisateur;

public class InscriptionUserActivity extends AppCompatActivity /*implements View.OnFocusChangeListener*/{
    private EditText editNom,editPrenom,editMail,editLogin,editMdp;
    private DatabaseClient mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_user);

        editLogin = findViewById(R.id.editLoginUser);
        editNom = findViewById(R.id.editNomUser);
        editPrenom = findViewById(R.id.editPrenomUser);
        editMail = findViewById(R.id.editMailUser);
        editMdp = findViewById(R.id.editMdpUser);

        mDb = DatabaseClient.getInstance(getApplicationContext());


    }

    public void onAnnuler(View v){
        super.finish();
    }

    public void onInscription(View v){
        // verification des input
        if(TextUtils.isEmpty(editLogin.getText()) ||
                TextUtils.isEmpty(editNom.getText()) ||
                        TextUtils.isEmpty(editPrenom.getText()) ||
                                TextUtils.isEmpty(editMail.getText()) ||
                                        TextUtils.isEmpty(editMdp.getText())){
            Toast.makeText(InscriptionUserActivity.this, "AJOUTER IMPOSSIBLE : il y a des champs vides" , Toast.LENGTH_SHORT).show();

        }else {
            // si tout les inputs a bien été remplis on vérifie le User
            VerifUserLogin vul = new VerifUserLogin();
            vul.execute();// Verification de l'éxistance de la personne en vérifiant son mail
        }
    }

    /**
     * Création d'une classe asynchrone pour sauvegarder l'utilisateur
     */
    class VerifUserLogin extends AsyncTask<Void, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Void... voids) {

            // vérification de l'existance de la personne via son libelle
            // car le libelle doit être unique
            Utilisateur user = mDb.getAppDatabase()
                    .utilisateurDao()
                    .findByLogin(String.valueOf(editLogin.getText()).toLowerCase());

            // On retourne le user correspondant au mail saisie
            return user;
        }

        @Override
        protected void onPostExecute(Utilisateur user) {
            super.onPostExecute(user);

            // si le user retouné est différent de null cela veut dire que le login existe déjà
            if(user != null){
                Toast.makeText(InscriptionUserActivity.this, "Ce Login correspond déjà à une personne" , Toast.LENGTH_SHORT).show();

            }else{
                // si le login n'existe pas on vérifie le mail
                VerifUserMail vum = new VerifUserMail();
                vum.execute();
            }

        }
    }
    /**
     * Création d'une classe asynchrone pour sauvegarder l'utilisateur
     */
    class VerifUserMail extends AsyncTask<Void, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Void... voids) {

            // verification via le mail
            Utilisateur user = mDb.getAppDatabase()
                    .utilisateurDao()
                    .findByMail(String.valueOf(editMail.getText()).toLowerCase());

            // On retourne le user correspondant au mail saisie
            return user;
        }

        @Override
        protected void onPostExecute(Utilisateur user) {
            super.onPostExecute(user);

            // si le mail ne correspond à aucun utilisateur on l'insert
            if(user != null){
                Toast.makeText(InscriptionUserActivity.this, "Ce Mail est déjà associer à un utilisateur" , Toast.LENGTH_SHORT).show();

            }else{
                // on l'insert
                InsertUser us = new InsertUser();
                us.execute();
            }

        }
    }
    /**
     * Verification de la personne
     */
    class InsertUser extends AsyncTask<Void, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Void... voids) {

            // creating user
            Utilisateur user = new Utilisateur();
            user.setLogin(String.valueOf(editLogin.getText()).toLowerCase());
            user.setMail(String.valueOf(editMail.getText()).toLowerCase());
            user.setNom(String.valueOf(editNom.getText()).toLowerCase());
            user.setPrenom(String.valueOf(editPrenom.getText()).toLowerCase());
            user.setMotDePasse(String.valueOf(editMdp.getText()));

            // adding to database
            long id = mDb.getAppDatabase()
                    .utilisateurDao()
                    .insert(user);

            // mettre à jour l'id de la utilisateur
            // Nécessaire si on souhaite avoir accès à l'id plus tard dans l'activité
            user.setId(id);


            return user;
        }

        @Override
        protected void onPostExecute(Utilisateur user) {
            super.onPostExecute(user);

            // Quand la tache est créée, on arrête l'activité AddTaskActivity (on l'enleve de la pile d'activités)
            setResult(RESULT_OK);
            finish();
            Toast.makeText(getApplicationContext(), "Inscrit", Toast.LENGTH_LONG).show();

        }
    }
}
