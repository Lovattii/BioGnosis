package com.example.myplant;

public class PlantaToCadrastro {
    public String specie;
    public int idResImage;
    public float idealTemperatura;
    public int idealLuminosidade;
    public int idealUmidade;

    public PlantaToCadrastro(BancoPlantsCadrastro cadrastro)//nao pode erar
    {
        this.specie = cadrastro.specie;
        this.idealLuminosidade = cadrastro.idealLuminosidade;
        this.idealTemperatura = cadrastro.idealTemperatura;
        this.idealUmidade = cadrastro.idealUmidade;
        this.idResImage = cadrastro.idResImage;
    }

    public PlantaToCadrastro()//nao pode erar
    {}

    public float getIdealTemperatura() {
        return idealTemperatura;
    }

    public int getIdealLuminosidade() {
        return idealLuminosidade;
    }

    public int getIdealUmidade() {
        return idealUmidade;
    }

    public String getSpecie() {
        return specie;
    }

    public int getIdResImage() {
        return idResImage;
    }
}
