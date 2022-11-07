package com.example.demirmu_projet_tabata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Entrainement.class,Utilisateur.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EntrainementDao entrainementDao();
    public abstract UtilisateurDao utilisateurDao();
}
