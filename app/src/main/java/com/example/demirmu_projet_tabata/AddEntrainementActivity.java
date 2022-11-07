package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demirmu_projet_tabata.db.DatabaseClient;
import com.example.demirmu_projet_tabata.db.Entrainement;

public class AddEntrainementActivity extends AppCompatActivity {
    private DatabaseClient mDb;
    private Button btnAjouter;
    private EditText editLibelle,editDescription,editNbSeance,editNbCycle,editTpReposCycle,editTpReposSeance,editTpTravail;
    private Long idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entrainement);
        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // récupération des données
        btnAjouter = findViewById(R.id.btnAjouterEntrainement);
        editDescription = findViewById(R.id.editDescription);
        editLibelle = findViewById(R.id.editLibelle);
        editNbSeance = findViewById(R.id.editNbSeance);
        editNbCycle = findViewById(R.id.editNbCycle);
        editTpReposCycle = findViewById(R.id.editTpReposCycle);
        editTpReposSeance = findViewById(R.id.editTpReposSeance);
        editTpTravail = findViewById(R.id.editTpTravail);

        // on récupère la préférence ou l'on a écrit à l'intérieur
        SharedPreferences sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        // Puis on récupère la valeur grâce à la clé
        idUser = sharedPref.getLong(getString(R.string.idUser), 0);


        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // verifier si il y a des champs vides
             if(TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editDescription.getText()) ||
                TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editDescription.getText())
                        || TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editDescription.getText()) ){

                    Toast.makeText(AddEntrainementActivity.this, "AJOUTER IMPOSSIBLE : il y a des champs vides" , Toast.LENGTH_SHORT).show();
                    int val = Integer.parseInt(editTpReposSeance.getText() + "000");
                    Toast.makeText(AddEntrainementActivity.this, editTpReposSeance.getText() + "000" , Toast.LENGTH_SHORT).show();

                }else{
                 // si tout est remplis appel de la fonction AddEntrainement()
                    AddEntrainement();
                }
            }
        });
    }

    public void AddEntrainement(){
        /**
         * Création d'une classe asynchrone pour sauvegarder l'entrainement donnée par l'utilisateur
         */
        class SaveEntrainement extends AsyncTask<Void, Void, Entrainement> {

            @Override
            protected Entrainement doInBackground(Void... voids) {

                // creating a entrainement
                Entrainement entrainement = new Entrainement();
                entrainement.setDescription(String.valueOf(editDescription.getText()));
                entrainement.setLibelle(String.valueOf(editLibelle.getText()));
                entrainement.setNbCycle(Integer.valueOf(String.valueOf(editNbCycle.getText())));
                entrainement.setNbSeance(Integer.valueOf(String.valueOf(editNbSeance.getText())));
                entrainement.setTpReposCycle(Integer.valueOf(String.valueOf(editTpReposCycle.getText()+"000")));
                entrainement.setTpReposSeance(Integer.valueOf(String.valueOf(editTpReposSeance.getText()+"000")));
                entrainement.setTrTravail(Integer.valueOf(String.valueOf(editTpTravail.getText()+"000")));
                entrainement.setEntrainementFini("COMMENCER");
                entrainement.setIdUser(idUser);

                // adding to database
                long id = mDb.getAppDatabase()
                        .entrainementDao()
                        .insert(entrainement);

                // mettre à jour l'id de la tache
                // Nécessaire si on souhaite avoir accès à l'id plus tard dans l'activité
                entrainement.setId(id);


                return entrainement;
            }

            @Override
            protected void onPostExecute(Entrainement entrainement) {
                super.onPostExecute(entrainement);
                // apprès la création de l'entrainement
                // on finie l'activity pour passer à la précedente.
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveEntrainement se = new SaveEntrainement();
        se.execute();
    }
}