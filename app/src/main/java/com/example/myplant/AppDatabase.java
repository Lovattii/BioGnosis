package com.example.myplant;

import static java.security.AccessController.getContext;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;
@Database(entities = {RegistrationPlant.class, Planta.class, BancoPlantsCadrastro.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PlantDAO plantDAO();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "myDatabaseOfPlants")
                            .allowMainThreadQueries()
                            .build();

                    if (INSTANCE.plantDAO().CountPlants() == 0)
                    {
                        AppDatabase.InsertBancoPlants(INSTANCE);
                        AppDatabase.InsertPlants(INSTANCE);
                        AppDatabase.InsertRegistration(INSTANCE);
                    }
                }
            }
        }
        return INSTANCE;
    }

    private static void InsertBancoPlants(AppDatabase db)
    {
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("TOMATE-CEREJA", R.drawable.tomato));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("CEBOLINHA", R.drawable.cebolinha));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("COUVE-FLOR", R.drawable.couve_flor));
    }

    private static void InsertPlants(AppDatabase db)
    {
        db.plantDAO().InsertNewPlant(new Planta("kasdfj", "ALFACE", R.drawable.central_alface, 0));
        db.plantDAO().InsertNewPlant(new Planta("Alface", "ALFACE", R.drawable.central_alface, 1));
        db.plantDAO().InsertNewPlant(new Planta("Alfacea", "ALFACE", R.drawable.central_alface, 2));
        db.plantDAO().InsertNewPlant(new Planta("Alfa", "ALFACE", R.drawable.central_alface, 3));
        db.plantDAO().InsertNewPlant(new Planta("Alfaakds", "ALFACE", R.drawable.central_alface, 4));
        db.plantDAO().InsertNewPlant(new Planta("Alfacelans", "ALFACE", R.drawable.central_alface, 5));
    }

    private static void InsertRegistration(AppDatabase db)
    {
        db.plantDAO().InsertRegistration(new RegistrationPlant(0, 40, 34, 90, System.currentTimeMillis()));
    }
}
