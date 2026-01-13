package com.example.myplant;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Table_Planta")
public class Planta {
    @PrimaryKey (autoGenerate = true)
    public int id;

    public int idPlant;
    public String nome;
    public String specie;
    public int idImagePlant;
    public long dataCadrastro;

    public Planta (String nome, String specie, int idImagePlant, int idPlant)
    {
        this.idPlant = idPlant;
        this.nome = nome;
        this.idImagePlant = idImagePlant;
        this.specie = specie;

        this.dataCadrastro = System.currentTimeMillis();
    }

    public String getNome()
    {
        return nome;
    }

    public String getSpecie() {
        return specie;
    }

    public int getIdImagePlant() {
        return idImagePlant;
    }

    public long getDataCadrastro() {
        return dataCadrastro;
    }

    public int getIdPlant() {
        return idPlant;
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
