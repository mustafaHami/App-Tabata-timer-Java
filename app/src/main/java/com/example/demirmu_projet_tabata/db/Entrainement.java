package com.example.demirmu_projet_tabata.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entrainement")
public class Entrainement implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String libelle;
    private String description;
    private int nbSeance;
    private int nbCycle;
    private int trTravail;
    private int tpReposCycle;
    private int tpReposSeance;
    private long idUser;
    private String entrainementFini;

    public Entrainement() {

    }

    protected Entrainement(Parcel in) {
        id = in.readLong();
        libelle = in.readString();
        description = in.readString();
        nbSeance = in.readInt();
        nbCycle = in.readInt();
        trTravail = in.readInt();
        tpReposCycle = in.readInt();
        tpReposSeance = in.readInt();
        idUser = in.readLong();
        entrainementFini = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(libelle);
        dest.writeString(description);
        dest.writeInt(nbSeance);
        dest.writeInt(nbCycle);
        dest.writeInt(trTravail);
        dest.writeInt(tpReposCycle);
        dest.writeInt(tpReposSeance);
        dest.writeLong(idUser);
        dest.writeString(entrainementFini);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Entrainement> CREATOR = new Creator<Entrainement>() {
        @Override
        public Entrainement createFromParcel(Parcel in) {
            return new Entrainement(in);
        }

        @Override
        public Entrainement[] newArray(int size) {
            return new Entrainement[size];
        }
    };

    /*
     * Getters and Setters
     * */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbSeance(){
        return nbSeance;
    }
    public void setNbSeance(int nbSeance){
        this.nbSeance = nbSeance;
    }

    public int getNbCycle(){
        return nbCycle;
    }
    public void setNbCycle(int nbCycle){
        this.nbCycle = nbCycle;
    }

    public int getTrTravail() {
        return trTravail;
    }

    public void setTrTravail(int trTravail) {
        this.trTravail = trTravail;
    }

    public int getTpReposCycle() {
        return tpReposCycle;
    }

    public void setTpReposCycle(int tpReposCycle) {
        this.tpReposCycle = tpReposCycle;
    }

    public int getTpReposSeance() {
        return tpReposSeance;
    }

    public void setTpReposSeance(int tpReposeSeance) {
        this.tpReposSeance = tpReposeSeance;
    }

    public long getIdUser() { return idUser;}

    public void setIdUser(long idUser){ this.idUser = idUser;}

    public String getEntrainementFini() {
        return entrainementFini;
    }

    public void setEntrainementFini(String entrainementFini) {
        this.entrainementFini = entrainementFini;
    }
}