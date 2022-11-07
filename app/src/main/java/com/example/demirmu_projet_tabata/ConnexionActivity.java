package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demirmu_projet_tabata.db.DatabaseClient;
import com.example.demirmu_projet_tabata.db.Utilisateur;

public class ConnexionActivity extends AppCompatActivity {

    private DatabaseClient mDb;
    private EditText editLoginConnex,editMdpConnex;
    private Button btnAjouterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        editLoginConnex = findViewById(R.id.editLoginConnex);
        editMdpConnex = findViewById(R.id.editMdpConnex);

        // bouton Ouverture de l'interface d'ajout d'un utilisateur
        btnAjouterUser = findViewById(R.id.btnAjouterUser);
        btnAjouterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnexionActivity.this,InscriptionUserActivity.class));
            }
        });


    }

    // méthode appelé après le click sur le bouton de connexion
    public void onValiderConnexion(View v){
        // on vérifie si toutes les cases sont remplis
        if(TextUtils.isEmpty(editLoginConnex.getText()) ||
                TextUtils.isEmpty(editMdpConnex.getText())){

            Toast.makeText(ConnexionActivity.this, "Toutes les case doivent être remplis " , Toast.LENGTH_SHORT).show();

        }else{
            // si toutes les cases sont remplis on vérifie si la personne existe
            VerifUser vu = new VerifUser();
            vu.execute();
        }


    }

    // méthode appelé après le clcik sur le bouton invité
    public void onInviteConnexion(View v){
        // on crée la preférence
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        // on crée par la suite l'editor pour pouvoir écrire à l'intérieur de la clé
        SharedPreferences.Editor editor = sharedPref.edit();
        // on stock la valeur dans la clé
        editor.putLong(getString(R.string.idUser), 0);
        // enregistrer les modifications
        editor.apply();
        editMdpConnex.setText("");
        editLoginConnex.setText("");


        startActivity(new Intent(ConnexionActivity.this, ListEntrainementActivity.class));
    }
    /**
     * Création d'une classe asynchrone pour verifier l'utilisateur
     */
    class VerifUser extends AsyncTask<Void, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Void... voids) {

            // Verification de l"utilisateur
            // on regarde si le login et le mot de passe correspond
            Utilisateur user = mDb.getAppDatabase()
                    .utilisateurDao()
                    .UserExist(String.valueOf(editLoginConnex.getText()).toLowerCase(),String.valueOf(editMdpConnex.getText()));

            // On retourne le user correspondant au mdp et login saisie
            return user;
        }

        @Override
        protected void onPostExecute(Utilisateur user) {
            super.onPostExecute(user);

            // si user != null cela veut dire qu'il existe une personne avec ce login correspondant à ce mot de passe
            // on enregistre son Id
            if(user != null){

                // on crée la preférence
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                // on crée par la suite l'editor pour pouvoir écrire à l'intérieur de la clé
                SharedPreferences.Editor editor = sharedPref.edit();
               // on stock la valeur dans la clé
                editor.putLong(getString(R.string.idUser), user.getId());
                // enregistrer les modifications
                editor.apply();
                editMdpConnex.setText("");
                editLoginConnex.setText("");
                startActivity(new Intent(ConnexionActivity.this, ListEntrainementActivity.class));
            }else{
                Toast.makeText(ConnexionActivity.this, "Mot de passe ou login INCORRECT "  , Toast.LENGTH_SHORT).show();

            }

        }
    }
}