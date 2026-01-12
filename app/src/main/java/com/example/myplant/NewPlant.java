package com.example.myplant;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Table_New")
public class NewPlant {
    @PrimaryKey (autoGenerate = true)
    public int id;
    public String nome;
    public long dataCadrastro;

    public NewPlant(String nome, long dataCadrastro)
    {
        this.nome = nome;
        this.dataCadrastro = dataCadrastro;
    }
}
