package com.example.myplant;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Table_Banco_Plants")
public class BancoPlantsCadrastro {
    @PrimaryKey(autoGenerate = true)
    public int id_d;
    public String specie;
    public int idResImage;
    public int idResDescription;

    public float idealTemperatura;
    public int idealLuminosidade;
    public int idealUmidade;
    public BancoPlantsCadrastro(String specie, int idResImage, int idResDescription, float idealTemperatura, int idealLuminosidade, int idealUmidade)
    {
        this.specie = specie;
        this.idResImage = idResImage;
        this.idealLuminosidade = idealLuminosidade;
        this.idealTemperatura = idealTemperatura;
        this.idealUmidade = idealUmidade;
        this.idResDescription = idResDescription;
    }

    public String getSpecie() {
        return specie;
    }
    public int getIdResImage() {
        return idResImage;
    }

    public float getIdealTemperatura() {
        return idealTemperatura;
    }

    public int getIdealLuminosidade() {
        return idealLuminosidade;
    }

    public int getIdealUmidade() {
        return idealUmidade;
    }
}
