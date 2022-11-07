package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demirmu_projet_tabata.db.DatabaseClient;
import com.example.demirmu_projet_tabata.db.Entrainement;
import com.example.demirmu_projet_tabata.db.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class ListEntrainementActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "user_id_KEY";

    private DatabaseClient mDb;
    private EntrainementAdapter entrainementAdapter;
    private Long idUser;
    private TextView txtName;
    // VIEW
    private Button buttonAdd,btnAjouterEntrainement,btnSeConnecter;
    private ListView listEntrainement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entraienement_list);
        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Récupérer les vues
        listEntrainement = findViewById(R.id.listEntrainement);
        txtName = findViewById(R.id.txtPersonneName);

        // on récupère la préférence ou l'on a écrit à l'intérieur
        SharedPreferences sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        // Puis on récupère la valeur grâce à la clé
        idUser = sharedPref.getLong(getString(R.string.idUser), 0);

        // bouton Ouverture de l'interface d'ajout d'interface
        btnSeConnecter = findViewById(R.id.btnSeConnecter);

        btnAjouterEntrainement = findViewById(R.id.btnAjouterEntrainement);
        btnAjouterEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListEntrainementActivity.this,AddEntrainementActivity.class));
            }
        });



        // Lier l'adapter au listView
        entrainementAdapter = new EntrainementAdapter(this, new ArrayList<Entrainement>());
        listEntrainement.setAdapter(entrainementAdapter);

        // EXEMPLE : Ajouter un événement click à la listView
        listEntrainement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Récupération de l'entrainement cliquée à l'aide de l'adapter
                com.example.demirmu_projet_tabata.db.Entrainement entrainement = entrainementAdapter.getItem(position);

                Intent intent = new Intent(ListEntrainementActivity.this, ExerciceActivity.class);
                intent.putExtra(ExerciceActivity.EXERCICE_KEY, entrainement);
                startActivity(intent);
            }
        });

        // un longue click pour supprimer l'entrainement
        listEntrainement.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de l'entrainement cliquée à l'aide de l'adapter
                com.example.demirmu_projet_tabata.db.Entrainement entrainement = entrainementAdapter.getItem(position);
                // longue click va faire appaître une fenêtre de dialogue
                // pour demander à l'utilisateur la validation de la suppression
                if(idUser != 0 && entrainement.getIdUser() !=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListEntrainementActivity.this);
                    builder.setMessage("Voulez-supprimer cette entrainement ?")
                            .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // si il click OUI
                                    // on fait appel à la méthode de suppression

                                    deleteEntrainementEnCour(entrainement);
                                    findEntrainementUser(idUser);
                                }
                            })
                            .setNegativeButton("NON", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    if(idUser == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListEntrainementActivity.this);
                        builder.setMessage("Suppresion Impossible pour un invité")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else if(entrainement.getIdUser() == 0){
                        // la suppresion des entrainement par default est impossible
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListEntrainementActivity.this);
                        builder.setMessage("Impossible de supprimer les entrainements par défaut")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }

                return false;
            }
        });

    }
    // retour en arrière
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // finie cette activité pour accéder à l'interface de la connexion
    public void seConnecter(View v){super.finish();}
    private void findEntrainementUser(Long idUser) {
        ///////////////////////
        // Classe asynchrone permettant de récupérer les entrainement de l'utilisateur
        class FindEntrainementUser extends AsyncTask<Void, Void, List<com.example.demirmu_projet_tabata.db.Entrainement>> {

            @Override
            protected List<com.example.demirmu_projet_tabata.db.Entrainement> doInBackground(Void... voids) {
                List<com.example.demirmu_projet_tabata.db.Entrainement> entrainListe = mDb.getAppDatabase()
                        .entrainementDao()
                        .findEntrainementIdUser(idUser);
                return entrainListe;
            }

            @Override
            protected void onPostExecute(List<com.example.demirmu_projet_tabata.db.Entrainement> entrainements) {
                super.onPostExecute(entrainements);

                // Mettre à jour l'adapter avec la liste de taches
                entrainementAdapter.clear();
                entrainementAdapter.addAll(entrainements);

                // Now, notify the adapter of the change in source
                entrainementAdapter.notifyDataSetChanged();
            }
        }

        FindEntrainementUser ge = new FindEntrainementUser();
        ge.execute();
    }
    public void findUser(long id){
        /// Accès à la base de données pour trouver la personne selon son id
        class FindUser extends AsyncTask<Void, Void, Utilisateur> {

            @Override
            protected Utilisateur doInBackground(Void... voids) {
                Utilisateur user = mDb.getAppDatabase()
                        .utilisateurDao()
                        .findUserById(id);
                return user;
            }

            @Override
            protected void onPostExecute(Utilisateur user) {
                super.onPostExecute(user);
                if(user != null){
                    txtName.setText("Bonjour " + user.getPrenom()+" "+user.getNom().toUpperCase());
                }else{
                    txtName.setText("Bonjour l'Invité");
                    btnAjouterEntrainement.setVisibility(View.INVISIBLE);
                    btnSeConnecter.setVisibility(View.VISIBLE);
                }

            }
        }

        FindUser fu = new FindUser();
        fu.execute();
    }
    private void deleteEntrainementEnCour(Entrainement entrainement) {
        ///////////////////////
        // Classe asynchrone permettant de supprimer l'entrainement
        class DeleteEntrainementEnCour extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                mDb.getAppDatabase()
                        .entrainementDao()
                        .delete(entrainement);

                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                Toast.makeText(ListEntrainementActivity.this, "Supprimer" , Toast.LENGTH_SHORT).show();

            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        DeleteEntrainementEnCour de = new DeleteEntrainementEnCour();
        de.execute();
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Mise à jour des taches
        findEntrainementUser(idUser);
        findUser(idUser);

    }

}