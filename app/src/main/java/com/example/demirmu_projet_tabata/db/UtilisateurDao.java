package com.example.demirmu_projet_tabata.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UtilisateurDao {

    @Query("SELECT * FROM utilisateur")
    List<Utilisateur> getAll();

    // Verification du login, car login unique
    @Query("SELECT * FROM utilisateur WHERE login = :login")
    Utilisateur findByLogin(String login);

    // Verifier si le mail correspond déjà à un compte
    @Query("SELECT * FROM utilisateur WHERE mail = :mail")
    Utilisateur findByMail(String mail);

    @Query("SELECT * FROM utilisateur WHERE login = :login and motDePasse = :mdp")
    Utilisateur UserExist(String login,String mdp);

    @Query("SELECT * FROM utilisateur WHERE id = :id")
    Utilisateur findUserById(long id);

    @Insert
    long insert(Utilisateur utilisateur);

    @Delete
    void delete(Utilisateur utilisateur);

    @Update
    void update(Utilisateur utilisateur);


}
