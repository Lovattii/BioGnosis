package com.example.myplant;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;
@Database(entities = {RegistrationPlant.class, Planta.class, BancoPlantsCadrastro.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PlantDAO plantDAO();
    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "myDatabaseOfPlants")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
