package com.example.myplant;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PlantDAO {

    @Insert
    void InsertRegistration(RegistrationPlant registro);

    @Insert
    void InsertBancoPlantsCadrastro(BancoPlantsCadrastro bancoPlants);
    @Insert
    void InsertNewPlant(Planta plant);

    @Query("SELECT * FROM Table_Banco_Plants")
    List<BancoPlantsCadrastro> GetBancoOfPlants();

    @Query("DELETE FROM Table_Planta WHERE nome = :name")
    void DeleteNewPlant(String name);
    @Query("SELECT * FROM Table_Planta WHERE nome = :name ORDER BY dataCadrastro LIMIT 1")
    Planta GetDadosPlanta(String name);

    @Query("SELECT * FROM Table_Planta")
    List<Planta> GetAllPlantas();

    @Query("SELECT * FROM Table_Planta LIMIT 1")
    Planta GetFirstPlant();

    @Query("SELECT * FROM Table_Registration WHERE idPlant = :id_plant ORDER BY dataMedicao DESC")
    List<RegistrationPlant> GetHistoryOfPlant(int id_plant);

    @Query("SELECT * FROM Table_Registration WHERE idPlant = :id_plant ORDER BY dataMedicao DESC LIMIT 1")
    RegistrationPlant GetLastRegistration(int id_plant);
}
