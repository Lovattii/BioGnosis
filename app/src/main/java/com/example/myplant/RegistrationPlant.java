package com.example.myplant;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "Table_Registration")
public class RegistrationPlant {

    public int id;
    public int id_plant;
    public long dataMedicao;
    public float temperatura;
    public float luminosidade;
    public float umidade;

    public RegistrationPlant() {
        dataMedicao = System.currentTimeMillis();
    }

    public float getUmidade() {
        return umidade;
    }

    public int getId() {
        return id;
    }

    public float getLuminosidade() {
        return luminosidade;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public int getId_plant() {
        return id_plant;
    }

    public long getDataMedicao() {
        return dataMedicao;
    }
}
