package com.example.myplant;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "Table_Registration")
public class RegistrationPlant {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int id_plant;
    public long dataMedicao;
    public float temperatura;
    public int luminosidade;
    public int umidade;

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

    public int getId_plant() {
        return id_plant;
    }

    public long getDataMedicao() {
        return dataMedicao;
    }

    public double calculateLife(double idealTemperature, double toleranceTemperature, int weightTemperature,
                                int idealLuminosity, int toleranceLuminosity, int weightLuminosity,
                                int idealHumidity, int toleranceHumidity, int weightHumidity){
        calculator = new BioGnosisLifeCalculator(idealTemperature, toleranceTemperature,
                weightTemperature, idealLuminosity, toleranceLuminosity, weightLuminosity,
                idealHumidity, toleranceHumidity, weightHumidity);

        return calculator.calculateLife(temperatura, luminosidade, umidade);
    }

    public double calculateLuminosidade()
    {
        return calculator.getLumScore() * 100;
    }

    public double calculateUmidade()
    {
        return calculator.getHumScore() * 100;
    }

    public double calculateTemperatura()
    {
        return calculator.getTempScore() * 100;
    }
}
