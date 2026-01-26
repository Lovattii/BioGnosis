package com.example.myplant;

import static java.security.AccessController.getContext;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;
import android.util.Log;

@Database(entities = {RegistrationPlant.class, Planta.class, BancoPlantsCadrastro.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
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
                        Log.d("DATABASE_D", "INICO ....");
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
        Log.d("DATABASE_D", "Até ....");
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("TOMATE-CEREJA", R.drawable.tomato, 25, 3750, 2445));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("CEBOLINHA", R.drawable.cebolinha, 23, 3550, 2000));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("COUVE-FLOR", R.drawable.couve_flor, 30, 3500, 2800));
    }

    private static void InsertPlants(AppDatabase db)
    {
        if (db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA") != null)
        {
            Log.d("DATABASE_D", "Vish");
            Log.d("DATABASE_D", "..." + db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA").getSpecie());
        }

        Planta p = new Planta("Alface", 0, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA")));

            Log.d("DATABASE_D", p.getSpecie());



        Log.d("DATABASE_D", "Aqui ....");
        db.plantDAO().InsertNewPlant(new Planta("Alface", 0, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfacea",  1, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("CEBOLINHA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfa", 2, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("CEBOLINHA"))));
        db.plantDAO().InsertNewPlant(new Planta("kasdfj", 3, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfaakds",4, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfacelans", 5, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        Log.d("DATABASE_D", "Sera ....");

    }

    private static void InsertRegistration(AppDatabase db)
    {
        Log.d("DATABASE_D", "Criou ....");
        RegistrationPlant r = new RegistrationPlant(0, 300f, 3500, 3200, System.currentTimeMillis());
        r.configLife(db);
        db.plantDAO().InsertRegistration(r);
    }
}
