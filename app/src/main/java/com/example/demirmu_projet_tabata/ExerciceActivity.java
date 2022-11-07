package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demirmu_projet_tabata.db.DatabaseClient;
import com.example.demirmu_projet_tabata.db.Utilisateur;

public class ExerciceActivity extends AppCompatActivity {

    public static final String EXERCICE_KEY = "exercice_key";
    //création de mes constantes pour les récupérer lors de la destruction
    public static final String NB_SEANCE_KEY = "nombre_seance_key";
    public static final String NB_CYCLE_KEY = "nombre_cylce_key";
    public static final String TIMER_REPOS_SESSION_KEY = "timer_repos_session_key";
    public static final String TIMER_REPOS_CYCLE_KEY = "timer_repos_cycle_key";
    public static final String TIMER_TRAVAIL_CYCLE_KEY = "timer_travail_cycle_key";
    public static final String ETAT_BTN_DEMARRER_KEY = "etat_bouton_demarrer_key";
    public static final String ETAT_BTN_REPOS_CYCLE_KEY = "etat_btn_cycle_repos_key";
    public static final String ETAT_BTN_REPOS_SESSION_KEY ="etat_btn_repos_session_key";
    public static final String ETAT_BTN_CONTINUER_CYCLE = "etat_btn_continuer_cycle";
    public static final String ETAT_BTN_CONTINUER_SESSION = "etat_btn_continuer_session";
    public static final String ETAT_IMG_SRC = "etat_image_src";
    public static final String ETAT_IMG_VISIBILITY = "etat_image_visibility";
    public static final String ETAT_MEDIA_PLAYER = "etat_media_player";
    public static final String ETAT_BACKGROUND_COLOR = "etat_background_color";
    //CONSTANTE
    private final static long INITIAL_TIME_TRAVAIL = 10000;
    private final static long INITIAL_TIME_REPOS_CYCLE = 10000;
    private final static long INITIAL_TIME_REPOS_SEANCE = 10000;
    private int valCycle,valSession,saveNbCycle,saveNbSession; // attribut pour savoir le nombre de session et de cycle
    //VIEW
    private Button btnDemarer,btnContinuerCycle,btnReposCycle,btnReposSeance,btnContinuerSeance,btnReset,btnPhoto;
    private TextView txtCycleTravail,txtCycleRepos,txtSessionRepos,txtCycle,txtSession;
    private CountDownTimer timerTravail,timerReposCycle,timerReposSeance;
    private com.example.demirmu_projet_tabata.db.Entrainement entrainement;
    //DATA
    private long updateTimeWork,saveTimeWork ; // update pour le timer, et Time pour enregistrer la valeur
    private long updateTimeReposCycle,saveTimeReposCycle ;
    private long updateTimeReposSession,saveTimeReposSession ;
    private Boolean etatBtnDemarrer,etatBtnReposCycle,etatBtnReposSession,etatBtnContinuerCycle,etatBtnContinuerSession; // lors du destroy voir si le bouton est activé ou pas
    private String etatImgSrc = "null";  // etatImgSrc pour savoir la source de l'image
    private String etatBackColor; // enregistrer la couleur du fond
    private Integer visibilituImg = 0;// visibilityImg pour savoir si elle est visible ou pas
    private Long idUser;
    private DatabaseClient mDb;
    private Boolean pause = false; // elle va nous permettre de savoir quelle timer a été mis en pause
    private Boolean mediaPlayeractive = false; // va nous permettre de savoir si un media a déjà été activé ou pas
    private ImageView imgAffiche;
    private LinearLayout linearGlobal;
    private MediaPlayer mediaPlayerLong,mediaPlayerUnCoup,mediaPause,mediaPlayerApplau,mediaPlayerPauseLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // on récupuère l'objet entrainement
        entrainement = getIntent().getParcelableExtra(EXERCICE_KEY);
        // Récupération les valeurs de l'entrainement selectionné
        saveTimeWork = entrainement.getTrTravail();
        saveTimeReposCycle = entrainement.getTpReposCycle();
        saveTimeReposSession = entrainement.getTpReposSeance();
        saveNbCycle = entrainement.getNbCycle();
        saveNbSession = entrainement.getNbSeance();

        //Récupération des boutons
        setContentView(R.layout.activity_exercice);
        // methode de récupération des données
        recuparationDesObjet();

        mediaPlayerLong = MediaPlayer.create(getApplicationContext(),R.raw.siffletdeb);
        mediaPlayerUnCoup = MediaPlayer.create(getApplicationContext(),R.raw.siffletuncoup);
        mediaPause = MediaPlayer.create(getApplicationContext(),R.raw.siffletpause);
        mediaPlayerApplau = MediaPlayer.create(getApplicationContext(),R.raw.soundapplau);
        mediaPlayerPauseLong = MediaPlayer.create(getApplicationContext(),R.raw.siffletpauselong);
        // récupération de l'id grâce à la clé string.idUser
        SharedPreferences sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        idUser = sharedPref.getLong(getString(R.string.idUser), 0);

        // si saveInstanceState != null cela veut dire que cette fonction a appelé juste avec la destruction
        if(savedInstanceState != null){
            // recuperation des valeurs enregistrés avant la destruction
            valSession = savedInstanceState.getInt(NB_SEANCE_KEY);
            valCycle = savedInstanceState.getInt(NB_CYCLE_KEY);
            updateTimeReposCycle = savedInstanceState.getLong(TIMER_REPOS_CYCLE_KEY);;
            updateTimeReposSession = savedInstanceState.getLong(TIMER_REPOS_SESSION_KEY);;
            updateTimeWork = savedInstanceState.getLong(TIMER_TRAVAIL_CYCLE_KEY);
            etatBtnContinuerCycle = savedInstanceState.getBoolean(ETAT_BTN_CONTINUER_CYCLE);
            etatBtnContinuerSession = savedInstanceState.getBoolean(ETAT_BTN_CONTINUER_SESSION);
            etatBtnDemarrer = savedInstanceState.getBoolean(ETAT_BTN_DEMARRER_KEY);
            etatBtnReposSession= savedInstanceState.getBoolean(ETAT_BTN_REPOS_SESSION_KEY);
            etatBtnReposCycle = savedInstanceState.getBoolean(ETAT_BTN_REPOS_CYCLE_KEY);
            etatImgSrc = savedInstanceState.getString(ETAT_IMG_SRC);
            visibilituImg = savedInstanceState.getInt(ETAT_IMG_VISIBILITY);
            etatBackColor = savedInstanceState.getString(ETAT_BACKGROUND_COLOR);

            mediaPlayeractive = savedInstanceState.getBoolean(ETAT_MEDIA_PLAYER);
            if(visibilituImg != 0){
                imgAffiche.setVisibility(visibilituImg);
            }
            // couleur du background
            if(etatBackColor =="vert"){
                linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFine));
            }else if(etatBackColor =="verfonce"){
                linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFonce));
            }else if(etatBackColor =="rouge"){
                linearGlobal.setBackgroundColor(getResources().getColor(R.color.redFine));
            }else{
                linearGlobal.setBackgroundColor(getResources().getColor(R.color.grey));
            }
            // récupération des données pour l'affichage des données
            if(etatImgSrc.equals("pause")){
                imgAffiche.setVisibility(View.VISIBLE);
                imgAffiche.setBackgroundResource(R.drawable.pause);
            }else if(etatImgSrc.equals("pouce")){
                imgAffiche.setVisibility(View.VISIBLE);
                imgAffiche.setBackgroundResource(R.drawable.pouce);

            }else if(etatImgSrc.equals("run")){
                imgAffiche.setVisibility(View.VISIBLE);
                imgAffiche.setBackgroundResource(R.drawable.run);

            }else if(etatImgSrc.equals("repos")) {
                imgAffiche.setVisibility(View.VISIBLE);
                imgAffiche.setBackgroundResource(R.drawable.pause);
            }else{
                imgAffiche.setVisibility(View.INVISIBLE);
            }
            //activer/désactiver les boutons en fonction des valeurs récupérer
            if(etatBtnDemarrer == false){
                btnDemarer.setEnabled(false);
                btnReset.setVisibility(View.VISIBLE);
            }else{
                btnDemarer.setEnabled(true);
                btnReset.setVisibility(View.INVISIBLE);
            }
            if(etatBtnReposSession == false){
                btnReposSeance.setEnabled(false);
            }else{
                btnReposSeance.setEnabled(true);
                // on lance le timerSession car si le bouton repos session est activé
                // cela veut dire que l'utilisateur peut appuyer sur pause
                // donc le compteur tourne
                timerSession();
            }
            if(etatBtnContinuerSession == false){
                btnContinuerSeance.setEnabled(false);
            }else{
                // le bouton continuer est activé donc le compteur session ne troune pas
                // car il doit appuyer sur ce bouton pour l'activer
                btnContinuerSeance.setEnabled(true);
            }
            if(etatBtnContinuerCycle == false){
                btnContinuerCycle.setEnabled(false);
            }else{
                // le bouton continuer est activé donc le compteur cycle ne troune pas
                // car il doit appuyer sur ce bouton pour l'activer
                if (updateTimeWork == 0){
                    pause = true; // on met pause à true, si la valeur du timer Travail est égale à 0
                                  // car cela voudrais dire que l'activation du bouton continuer va activer le timer repos cycle
                                  // cette attribue permet d'activer directement le timer repos
                                  // car si on ne fait pas cela le timer Travail va ce lancer en arrière plan puisque sa valeur est à 0
                                  // il va aller dans le onFinish et créer un deuxième timer repos en arrière plan
                                  // ce qui va du coup créer deux timer et on ne pourra plus stopper le timer repos
                }

                btnContinuerCycle.setEnabled(true);
            }
            if(etatBtnReposCycle == false){
                btnReposCycle.setEnabled(false);
            }else{
                // on lance le timerCycle car si le bouton repos cycle est activé
                // cela veut dire que l'utilisateur peut appuyer sur pause
                // donc le compteur tourne

                btnReposCycle.setEnabled(true);
                timerCycle();
            }


        }else{
            // sinon on redonne les valeurs de base
            updateTimeReposCycle = saveTimeReposCycle;
            updateTimeReposSession = saveTimeReposSession;
            updateTimeWork = saveTimeWork;
            valCycle = 1;
            valSession = 1;
            etatBtnDemarrer = true;
            etatBtnReposCycle = false;
            etatBtnReposSession = false;
            etatBtnContinuerSession = false;
            etatBtnContinuerCycle = false;
        }
        // on met à jour les textes
        miseAJour();

    }

    private void recuparationDesObjet() {
        btnDemarer = findViewById(R.id.btnDemarer);
        btnReposCycle = findViewById(R.id.btnCyclePause);
        btnContinuerCycle = findViewById(R.id.btnCycleContinuer);
        btnReposSeance =findViewById(R.id.btnSeanceRepos);
        btnContinuerSeance = findViewById(R.id.btnSeanceContinuer);
        btnReset = findViewById(R.id.btnReset);
        btnPhoto = findViewById(R.id.btnPhoto);

        txtCycleTravail = findViewById(R.id.txtMinCycleTravail);
        txtCycleRepos = findViewById(R.id.txtMinCycleRepos);
        txtSessionRepos = findViewById(R.id.txtMinSeanceRepos);
        txtCycle = findViewById(R.id.txtCycle);
        txtSession = findViewById(R.id.txtSession);

        imgAffiche = findViewById(R.id.imgAffichageAction);
        // récupération du linearCycle pour changer la couleur
        linearGlobal= findViewById(R.id.linearGlobal);
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
                // user != null, user existe
                if(user != null){

                }

            }
        }

        FindUser fu = new FindUser();
        fu.execute();
    }
    @Override
    protected void onStart() {
        super.onStart();

        findUser(idUser);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(timerTravail != null){
            timerTravail.cancel();
        }
        if(timerReposSeance != null){
            timerReposSeance.cancel();
        }
        if(timerReposCycle != null){
            timerReposCycle.cancel();
        }
        // Save the user's current game state
        outState.putInt(NB_SEANCE_KEY, valSession);
        outState.putInt(NB_CYCLE_KEY, valCycle);
        outState.putLong(TIMER_REPOS_CYCLE_KEY, updateTimeReposCycle);
        outState.putLong(TIMER_REPOS_SESSION_KEY, updateTimeReposSession);
        outState.putLong(TIMER_TRAVAIL_CYCLE_KEY, updateTimeWork);
        outState.putBoolean(ETAT_BTN_DEMARRER_KEY,etatBtnDemarrer);
        outState.putBoolean(ETAT_BTN_REPOS_CYCLE_KEY,etatBtnReposCycle);
        outState.putBoolean(ETAT_BTN_REPOS_SESSION_KEY,etatBtnReposSession);
        outState.putBoolean(ETAT_BTN_CONTINUER_SESSION,etatBtnContinuerSession);
        outState.putBoolean(ETAT_BTN_CONTINUER_CYCLE,etatBtnContinuerCycle);
        outState.putString(ETAT_IMG_SRC, etatImgSrc);
        outState.putInt(ETAT_IMG_VISIBILITY,visibilituImg);
        outState.putBoolean(ETAT_MEDIA_PLAYER,mediaPlayeractive);
        outState.putString(ETAT_BACKGROUND_COLOR,etatBackColor);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    // Mise à jour graphique
    private void miseAJour() {
        // Décompositions en secondes et minutes
        int secs = (int) (updateTimeWork / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (updateTimeWork % 1000);

        // Affichage du "timer"
        txtCycleTravail.setText("" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds));

        // Décompositions en secondes et minutes
         secs = (int) (updateTimeReposCycle / 1000);
         mins = secs / 60;
        secs = secs % 60;
         milliseconds = (int) (updateTimeReposCycle % 1000);

        // Affichage du "timer"
        txtCycleRepos.setText("" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds));

        // Décompositions en secondes et minutes
        secs = (int) (updateTimeReposSession / 1000);
        mins = secs / 60;
        secs = secs % 60;
        milliseconds = (int) (updateTimeReposSession % 1000);

        // Affichage du "timer"
        txtSessionRepos.setText("" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds));


        if(valSession == saveNbSession && secs == 0 && milliseconds == 0){
            mediaPlayerApplau.start();
        }

        // à chaque passage il va nous afficher le text
        // en cas d'incrémentation d'un des deux valeurs, il va afficher le text avec le nombre incrémenté
        // le if est pour éviter d'afficher le nombre qui sera suppéieur au nombre de cycle
        if(valCycle <= saveNbCycle){
            txtCycle.setText("Cycle "+valCycle +"/"+saveNbCycle);
        }
        if(valSession <= saveNbSession){
            txtSession.setText("Seance " + valSession+"/"+saveNbSession);


        }


    }

    public void onDemarer(View view) {
        mediaPlayerLong.start();
        btnReset.setVisibility(View.VISIBLE);
        btnDemarer.setEnabled(false);
        btnReposCycle.setEnabled(true);
        imgAffiche.setVisibility(View.VISIBLE);
        visibilituImg = View.VISIBLE;
        imgAffiche.setBackgroundResource(R.drawable.run);
        etatImgSrc = "run";
        linearGlobal.setBackgroundColor(getResources().getColor(R.color.redFine));
        // Dès qu'on appuie sur démarrer le bouton reposCycle s'active
        etatBtnDemarrer = false;
        etatBtnReposCycle = true;
        etatBtnReposSession = false;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        if(entrainement.getId() != 1 && entrainement.getId() != 2){
            changeEntrainementEnCour("DÉMARRER");
        }

        miseAJour();
        // je fait appel au timer pour le cycle
        timerCycle();

    }
    public void onResetExercice(View v){
        // lors du reset on remet les boutons dans leurs formes de bases
        btnReset.setVisibility(View.INVISIBLE);
        btnDemarer.setEnabled(true);
        btnContinuerSeance.setEnabled(false);
        btnContinuerCycle.setEnabled(false);
        btnReposCycle.setEnabled(false);
        btnReposSeance.setEnabled(false);
        etatBtnDemarrer = true;
        etatBtnReposCycle = false;
        etatBtnReposSession = false;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        pause = false;
        linearGlobal.setBackgroundColor(getResources().getColor(R.color.grey));

        imgAffiche.setVisibility(View.INVISIBLE);
        visibilituImg = View.INVISIBLE;
        // on met les timers à null que si elles sont différents de null.
        if(timerTravail != null){
            timerTravail.cancel();
        }
        if(timerReposSeance != null){
            timerReposSeance.cancel();
        }
        if(timerReposCycle != null){
            timerReposCycle.cancel();
        }
        updateTimeReposCycle = saveTimeReposCycle;
        updateTimeReposSession = saveTimeReposSession;
        updateTimeWork = saveTimeWork;
        valCycle = 1;
        valSession = 1;
        miseAJour();
    }
    public void timerCycle() {
        // on met le bouton repos à vrai car le timer est actif
        etatBtnReposCycle = true;
        etatBtnReposSession = false;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        linearGlobal.setBackgroundColor(getResources().getColor(R.color.redFine));

        // verifier si le bouton pause a déjà été appuyé ou pas.
        // pour éviter de créer deux fois le timer
        // donc quand l'utilisateur va appuyer sur le bouton pause pour arrêter le timerReposCycle
        // puis quand il va continuer on va pas crée un autre timerRepos dans le timerTravail
        // mais on va directement relancer le timerReposCycle

        if (pause != true) {
            imgAffiche.setBackgroundResource(R.drawable.run);
            etatImgSrc = "run";
        timerTravail = new CountDownTimer(updateTimeWork, 10) {

            public void onTick(long millisUntilFinished) {
                updateTimeWork = millisUntilFinished;
                miseAJour();
                etatBackColor = "rouge";
            }

            public void onFinish() {
                updateTimeWork = 0;
                timerTravail = null;
                miseAJour();


                // cette comparaison compare le nombre de Cycle réaliser valCycle
                // et le nombre de cycle à réaliser saveNbCycle
                if (valCycle == saveNbCycle) {
                    // si on rentre à l'intérieur cela veut dire
                    // qu'on a atteint le nombre de de cycle prévu
                    updateTimeReposSession = saveTimeReposSession;
                    updateTimeWork = saveTimeWork;
                    updateTimeReposCycle = saveTimeReposCycle;
                    btnContinuerCycle.setEnabled(false);
                    btnReposCycle.setEnabled(false);
                    btnReposSeance.setEnabled(true);
                    // puis on lance le timer session
                    if(mediaPlayeractive == false){
                        mediaPlayerPauseLong.start();
                        mediaPlayeractive = true;
                    }
                    timerSession();
                } else {
                    imgAffiche.setBackgroundResource(R.drawable.repos);
                    etatImgSrc = "repos";
                    linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFine));
                    if(mediaPlayeractive == false){
                        mediaPause.start();
                        mediaPlayeractive = true;
                    }

                    // Travail fini, active chrono repos cycle
                    timerReposCycle = new CountDownTimer(updateTimeReposCycle, 10) {

                        public void onTick(long millisUntilFinished) {
                            updateTimeReposCycle = millisUntilFinished;
                            etatBackColor = "vert";
                            miseAJour();
                        }

                        public void onFinish() {
                            updateTimeReposCycle = 0;
                            miseAJour();
                            mediaPlayeractive = false;
                            timerReposCycle = null;
                            valCycle++;
                            mediaPlayerUnCoup.start();
                            if (valCycle < saveNbCycle) {
                                // on met à jour que si le nombre de cycle n'est pas atteint
                                updateTimeReposCycle = saveTimeReposCycle;
                                updateTimeWork = saveTimeWork;
                            } else {
                                updateTimeWork = saveTimeWork;
                            }

                            timerCycle();


                        }
                    }.start();
                }
            }
        }.start();
    }else{
            //
            pause = false;
            imgAffiche.setBackgroundResource(R.drawable.repos);
            etatImgSrc = "repos";
            linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFine));

            // Travail fini, active chrono repos cycle
            timerReposCycle = new CountDownTimer(updateTimeReposCycle, 10) {

                public void onTick(long millisUntilFinished) {
                    updateTimeReposCycle = millisUntilFinished;
                    etatBackColor = "vert";
                    miseAJour();
                }

                public void onFinish() {
                    updateTimeReposCycle = 0;
                    miseAJour();
                    timerReposCycle = null;
                    valCycle++;
                    mediaPlayeractive = false;
                    mediaPlayerUnCoup.start();
                    if (valCycle < saveNbCycle) {
                        updateTimeReposCycle = saveTimeReposCycle;
                        updateTimeWork = saveTimeWork;
                    } else {
                        updateTimeWork = saveTimeWork;
                    }

                    timerCycle();


                }
            }.start();
        }
    }
    public void timerSession(){
        etatBtnReposCycle = false;
        etatBtnReposSession = true;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        mediaPlayeractive = false;
        etatBackColor = "vert";
        imgAffiche.setBackgroundResource(R.drawable.repos);
        etatImgSrc = "repos";
        linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFine));

        // lancement du timer repos de session
            timerReposSeance = new CountDownTimer(updateTimeReposSession, 10) {

                public void onTick(long millisUntilFinished) {
                    updateTimeReposSession = millisUntilFinished;
                    miseAJour();
                }

                public void onFinish() {
                    updateTimeReposSession = 0;
                    timerReposSeance = null;
                    miseAJour();
                    valSession++;

                    // si le nombre de session prévus n'est pas obtenu
                    // on relance le timer cycle
                    if (valSession <= saveNbSession) {
                        valCycle = 1;
                        updateTimeWork = saveTimeWork;
                        updateTimeReposCycle = saveTimeReposCycle;
                        updateTimeReposSession = saveTimeReposSession;
                        btnReposCycle.setEnabled(true);
                        btnReposSeance.setEnabled(false);
                        mediaPlayerUnCoup.start();
                        timerCycle();
                    }else{
                        Toast.makeText(ExerciceActivity.this, "Bravo : " , Toast.LENGTH_SHORT).show();
                        imgAffiche.setBackgroundResource(R.drawable.pouce);
                        linearGlobal.setBackgroundColor(getResources().getColor(R.color.vertFonce));
                        updateTimeReposCycle = saveTimeReposCycle;
                        updateTimeReposSession = saveTimeReposSession;
                        updateTimeWork = saveTimeWork;
                        valCycle = 1;
                        valSession = 1;
                        miseAJour();
                        etatBackColor = "verfonce";
                        etatImgSrc = "pouce";
                        btnDemarer.setEnabled(true);
                        btnReposSeance.setEnabled(false);
                        btnContinuerCycle.setEnabled(false);
                        btnReposCycle.setEnabled(false);
                        etatBtnReposCycle = false;
                        etatBtnReposSession = false;
                        etatBtnContinuerSession = false;
                        etatBtnContinuerCycle = false;
                        etatBtnDemarrer = true;
                        mediaPlayeractive = false;
                        btnReset.setVisibility(View.INVISIBLE);
                        if(entrainement.getId() != 1 && entrainement.getId() != 2){
                            changeEntrainementEnCour("FINIE");
                        }
                    }
                }

            }.start();
    }
    @Override
    public void onBackPressed() {
        if(timerTravail != null){
            timerTravail.cancel();
        }
        if(timerReposSeance != null){
            timerReposSeance.cancel();
        }
        if(timerReposCycle != null){
            timerReposCycle.cancel();
        }

        super.finish();
    }
    public void onPauseCycle(View view){
        // dès qu'on active le bouton pause du cycle le bouton continuer du cycle s'active
        etatBtnDemarrer = false;
        etatBtnReposCycle = false;
        etatBtnReposSession = false;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = true;
        imgAffiche.setBackgroundResource(R.drawable.pause);
        etatImgSrc = "pause";
        btnReposCycle.setEnabled(false);
        if (timerTravail != null) {
            timerTravail.cancel(); // Arrete le CountDownTimer
            btnContinuerCycle.setEnabled(true);
        }else{
            if(timerReposCycle != null){
                timerReposCycle.cancel();
                btnContinuerCycle.setEnabled(true);
                pause = true; // on récupère le timerReposCyle lorsque celui-ci et mis en pause car sinon
                              // dans le timerCycle il crée deux fois le timerRepos et quand on réappuie sur pause il met
                              // en pause le timer d'avant et non le nouveau timer créée
            }else{
                btnContinuerCycle.setEnabled(false);
                btnContinuerSeance.setEnabled(true);
            }
        }
    }

    public void onContinuerCycle(View view){
        // dès qu'on appuie sur le bouton continuer du cycle le bouton repos du cycle s'active
        etatBtnDemarrer = false;
        etatBtnReposCycle = true;
        etatBtnReposSession = false;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        timerReposCycle = null;
        imgAffiche.setBackgroundResource(R.drawable.run);
        etatImgSrc = "run";
        btnContinuerCycle.setEnabled(false);
        btnReposCycle.setEnabled(true);
        // dés qu'il appuie sur le bouton continuer
        // on lance le timer pour cycle car si il peut appuyer cela veut dire que le cycle n'est pas fini
        timerCycle();


    }
    public void onReposSeance(View v){
        // dès qu'on appuie sur le bouton repos de la séance le bouton continuer de la séance s'active
        etatBtnDemarrer = false;
        etatBtnReposCycle = false;
        etatBtnReposSession = false;
        etatBtnContinuerSession = true;
        etatBtnContinuerCycle = false;
        imgAffiche.setBackgroundResource(R.drawable.repos);
        etatImgSrc = "repos";
        if(timerReposSeance != null){
            timerReposSeance.cancel();
        }
        btnContinuerSeance.setEnabled(true);
        btnReposSeance.setEnabled(false);

    }

    public void onContinuerSeance(View v){
        //dès qu'on appuie sur le bouton continuer de la séance le bouton repos de la séance s'active
        etatBtnDemarrer = false;
        etatBtnReposCycle = false;
        etatBtnReposSession = true;
        etatBtnContinuerSession = false;
        etatBtnContinuerCycle = false;
        imgAffiche.setBackgroundResource(R.drawable.repos);
        etatImgSrc = "repos";
        btnContinuerSeance.setEnabled(false);
        btnReposSeance.setEnabled(true);
        // dès qu'il appuie sur le bouton continuer d'une session
        // on appel la methode timerSession()
        // on ne verifie pas le timer car si il peut appuyer cela veut dire que le repos session n'est pas fini
        timerSession();
    }
    public void onActivityPicture(View v){
        startActivity(new Intent(ExerciceActivity.this, takePictureActivity.class));
    }
    private void changeEntrainementEnCour(String valUpdate) {
        ///////////////////////
        // Classe asynchrone permettant de récupérer les entrainement  créée par l'utilisateur
        class ChangeEntrainementEnCour extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                entrainement.setEntrainementFini(valUpdate);
                // on met à jour l'entrainement
                 mDb.getAppDatabase()
                        .entrainementDao()
                        .update(entrainement);

                 return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                Toast.makeText(ExerciceActivity.this, "Update" , Toast.LENGTH_SHORT).show();

            }
        }

        ChangeEntrainementEnCour ce = new ChangeEntrainementEnCour();
        ce.execute();
    }

}