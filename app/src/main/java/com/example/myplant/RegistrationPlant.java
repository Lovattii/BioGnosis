package com.example.myplant;

import android.content.Context;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
@Entity(tableName = "Table_Registration",
indices = {@Index(value = {"id_plant", "dataMedicao"})})
public class RegistrationPlant {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int id_plant;
    public long dataMedicao;
    public float temperatura;
    public int luminosidade;
    public int umidade;
    public float vida;
    @Ignore
    private BioGnosisLifeCalculator calculator;

    public RegistrationPlant(int id_plant, float temperatura, int luminosidade, int umidade, long dataMedicao)
    {
        this.id_plant = id_plant;
        this.temperatura = temperatura;
        this.luminosidade = luminosidade;
        this.umidade = umidade;
        this.dataMedicao = dataMedicao;
    }

    public float getUmidade() {
        return umidade;
    }

    public int getIdRegistration() {
        return id;
    }

    public float getLuminosidade() {
        return luminosidade;
    }

    public float getTemperatura() {
        return temperatura;
    }

    private void initCalculator(AppDatabase db)
    {
        Planta p = db.plantDAO().GetPlantaById(id_plant);

        calculator = new BioGnosisLifeCalculator(p.getIdealTemperatura(), 5, temperatura,
                p.getIdealLuminosidade(), 250, luminosidade,
                p.getIdealUmidade(), 250, umidade);
    }
    public void configLife(AppDatabase db)
    {
        initCalculator(db);
        this.vida = calculator.getLife();
        Log.d("DATABASE_D", "kAMI");
    }

    public float getVida() {

        return vida;
    }

    public int getId_plant() {
        return id_plant;
    }

    public long getDataMedicao() {
        return dataMedicao;
    }

    public double calculateLuminosidade(AppDatabase db)
    {
        initCalculator(db);
        return calculator.getLumScore() * 100;
    }

    public double calculateUmidade(AppDatabase db)
    {
        initCalculator(db);
        return calculator.getHumScore() * 100;
    }

    public double calculateTemperatura(AppDatabase db)
    {
        initCalculator(db);
        return calculator.getTempScore() * 100;
    }
}
