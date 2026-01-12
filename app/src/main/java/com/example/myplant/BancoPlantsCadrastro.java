package com.example.myplant;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Table_Banco_Plants")
public class BancoPlantsCadrastro {
    @PrimaryKey(autoGenerate = true)
    public int Idd;
    public String specie;
    public int idResImage;
    public BancoPlantsCadrastro(String specie, int idResImage)
    {
        this.specie = specie;
        this.idResImage = idResImage;
    }

    public String getSpecie() {
        return specie;
    }

    public int getIdd() {
        return Idd;
    }

    public int getIdResImage() {
        return idResImage;
    }


}
