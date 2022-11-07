package com.example.demirmu_projet_tabata.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntrainementDao {

    @Query("SELECT * FROM entrainement")
    List<Entrainement> getAll();

    @Query("SELECT * FROM entrainement WHERE idUser = :idUser or idUser =0")
    List<Entrainement> findEntrainementIdUser(Long idUser);

    @Query("SELECT * FROM entrainement where libelle = :libEntrainement ")
    Entrainement findByLibelle(String libEntrainement);

    @Insert
    long insert(Entrainement entrainement);

    @Insert
    long[] insertAll(Entrainement... entrainements);

    @Delete
    void delete(Entrainement entrainement);

    @Update
    void update(Entrainement entrainement);
}
