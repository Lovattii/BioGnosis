package com.example.myplant;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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
    @Insert
    void InsertAllListRegistration(List<RegistrationPlant> registros);


    @Query("DELETE FROM Table_Planta WHERE idPlant = :id_plant")
    void DeletePlantById (int id_plant);
    @Query("DELETE FROM Table_Planta WHERE nome = :name")
    void DeleteNewPlant(String name);


    @Query("SELECT * FROM Table_Banco_Plants")
    List<BancoPlantsCadrastro> GetBancoOfPlants();
    @Query("SELECT * FROM Table_Planta WHERE idPlant = :id_plant ORDER BY dataCadrastro LIMIT 1")
    Planta GetPlantaById(int id_plant);

    @Query("SELECT * FROM Table_Planta")
    List<Planta> GetAllPlantas();

    @Query("SELECT * FROM Table_Registration")
    List<RegistrationPlant> GetAllRegistrations();

    @Query("SELECT * FROM Table_Registration WHERE id_plant = :id_plant ORDER BY dataMedicao ASC")
    List<RegistrationPlant> GetAllRegistrationsById(int id_plant);

    @Query("SELECT * FROM Table_Planta ORDER BY dataCadrastro ASC LIMIT 1")
    Planta GetFirstPlant();

    @Query("SELECT * FROM Table_Planta ORDER BY dataCadrastro DESC LIMIT 1")
    Planta GetLastPlant();

    @Query("SELECT * FROM Table_Banco_Plants WHERE specie = :specie LIMIT 1")
    BancoPlantsCadrastro GetCadrastroBySpecie(String specie);

    @Query("SELECT * FROM Table_Registration WHERE id_plant = :id_plant ORDER BY dataMedicao DESC")
    List<RegistrationPlant> GetHistoryOfPlant(int id_plant);

    @Query("SELECT * FROM Table_Registration WHERE id_plant = :id_plant ORDER BY dataMedicao DESC LIMIT 1")
    RegistrationPlant GetLastRegistration(int id_plant);

    @Query("SELECT * FROM table_registration WHERE id_plant = :id_plant ORDER BY dataMedicao DESC LIMIT 1")
    LiveData<RegistrationPlant> ListenerLastRegistration(int id_plant);

    @Query("SELECT COUNT (*) FROM table_planta WHERE dataCadrastro < (SELECT dataCadrastro FROM table_planta WHERE idPlant = :id_plant)")
    int ReturnPosByIdPlanta(int id_plant);

//    @Query("")
//    int ReturnPosToCadrastro();

    @Query("SELECT COUNT (*) FROM table_planta WHERE idPlant = 0")
    int hasIdZero();
//
//    @Query("SELECT ")

    @Query("SELECT COUNT (*) FROM Table_planta")
    int CountPlants();

    @Query("SELECT COUNT (*) FROM table_registration")
    int CountRegistrations();

    @Query("SELECT COUNT (*) FROM Table_Banco_Plants")
    int CountBancoOfPlants();

    @Query("SELECT COUNT (*) FROM table_registration WHERE id_plant = :id_plant AND vida < 30")
    int CountKillsPlantById(int id_plant);

    @Query("SELECT COUNT (*) FROM table_registration AS atual " +
    "WHERE atual.id_plant = :id_plant AND " +
    "atual.umidade - (SELECT anterior.umidade FROM table_registration " +
    "AS anterior WHERE anterior.id_plant = :id_plant AND anterior.dataMedicao < atual.dataMedicao " +
    "ORDER BY anterior.dataMedicao DESC LIMIT 1) < -1000")
    int CountVezesIrrigadas(int id_plant);

    @Query("SELECT COUNT (*) FROM Table_Registration WHERE id_plant = :id_plant AND vida > 70")
    int CountPlantSaudavelById(int id_plant);
}
