package com.example.myplant;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Table_Planta")
public class Planta {
    @PrimaryKey (autoGenerate = true)
    public int idD;
    public int idPlant;
    public String nome;
    public long dataCadrastro;
    @Embedded
    public PlantaToCadrastro cadrastro;

    public Planta (String nome, int idPlant, PlantaToCadrastro cadrastro)
    {
        this.idPlant = idPlant;
        this.nome = nome;
        this.dataCadrastro = System.currentTimeMillis();
        this.cadrastro = cadrastro;
    }

    public String getNome()
    {
        return nome;
    }

    public String getSpecie() {
        return cadrastro.getSpecie();
    }

    public int getIdImagePlant() {
        return cadrastro.getIdResImage();
    }

    public long getDataCadrastro() {
        return dataCadrastro;
    }

    public int getIdPlant() {
        return idPlant;
    }

    public float getIdealTemperatura()
    {
        return cadrastro.getIdealTemperatura();
    }
    public int getIdealUmidade()
    {
        return cadrastro.getIdealUmidade();
    }

    public int getIdealLuminosidade()
    {
        return cadrastro.getIdealLuminosidade();
    }

    public int calculaDiasComMilli(long time)
    {
        return (int)(time / (24 * 60 * 60 * 1000));
    }

    public int getPlantDays()
    {
        long agora = System.currentTimeMillis();
        return Math.abs(calculaDiasComMilli(dataCadrastro) - calculaDiasComMilli(agora));
    }

}
