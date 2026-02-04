package com.example.myplant;

import static java.security.AccessController.getContext;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.RoomDatabase;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {RegistrationPlant.class, Planta.class, BancoPlantsCadrastro.class}, version = 1, exportSchema = false) //entidades sao tabelas
public abstract class AppDatabase extends RoomDatabase{
    public abstract PlantDAO plantDAO(); //gerente do banco de dados (classe que possui todas as funcoes para manipula-lo
    private static volatile AppDatabase INSTANCE; //banco de dados em si
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) //garantir que as instancias sejam a mesma em contextos diferentes
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), //cria o banco de dados
                                    AppDatabase.class, "myDatabaseOfPlants")
                            .allowMainThreadQueries()
                            .build();

                    if (INSTANCE.plantDAO().CountBancoOfPlants() == 0)
                    {
                        Log.d("DATABASE_D", "INICO ...."); //cria umas plantas fakes pra teste
                        AppDatabase.InsertBancoPlants(INSTANCE);
                        Log.d("DATABASE_D", "CRIOU BANCO ...."); //cria umas plantas fakes pra teste
                    }
                }
            }
        }
        return INSTANCE;
    }

    private static void InsertBancoPlants(AppDatabase db)
    {
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("TOMATE-CEREJA", R.drawable.tomato, R.string.descrition_tomate_cereja, 25, 3750, 2445));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("CEBOLINHA", R.drawable.cebolinha, R.string.descrition_cebolinha, 23, 3550, 2000));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("COUVE-FLOR", R.drawable.couve_flor, R.string.descrition_couve_flor, 30, 3500, 2800));
    }

    private static void InsertPlants(AppDatabase db)
    {
        db.plantDAO().InsertNewPlant(new Planta("Alface", 0, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfacea",  1, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("CEBOLINHA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfa", 2, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("CEBOLINHA"))));
        db.plantDAO().InsertNewPlant(new Planta("kasdfj", 3, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfaakds",4, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
        db.plantDAO().InsertNewPlant(new Planta("Alfacelans", 5, new PlantaToCadrastro(db.plantDAO().GetCadrastroBySpecie("TOMATE-CEREJA"))));
    }

    private static void InsertRegistration(AppDatabase db)
    {
        RegistrationPlant r1 = new RegistrationPlant(0, 300f, 3500, 3200, System.currentTimeMillis());
        RegistrationPlant r2 = new RegistrationPlant(1, 300f, 3500, 3200, System.currentTimeMillis());
        RegistrationPlant r3 = new RegistrationPlant(2, 300f, 3500, 3200, System.currentTimeMillis());
        Log.d("DATABASE_D", "INteressante");
        r1.configLife(db);
        r2.configLife(db);
        r3.configLife(db);

        db.plantDAO().InsertRegistration(r1);
        Log.d("DATABASE_D", "Consegui cadrastrar o 0" + db.plantDAO().GetLastRegistration(0));
        Log.d("DATABASE_D", "COunt de registro  " + db.plantDAO().CountRegistrations());
        db.plantDAO().InsertRegistration(r2);
        db.plantDAO().InsertRegistration(r3);
    }

    public static void cadrastraPrimeiraPlanta(AppDatabase db, Context context, FragmentManager fragmentManager, CallbackMain callbackMain)
    {
        final List<BancoPlantsCadrastro> plantsCadrastroList = db.plantDAO().GetBancoOfPlants();
        class ButtonsLocal
        {
            BancoPlantsCadrastro plantaParaCadrastro;
            Button myButton;
            MaterialCardView myCard;
            aSheet cadrastration;
        }

        final ButtonsLocal buttonsLocal = new ButtonsLocal();
        buttonsLocal.plantaParaCadrastro = null;

        buttonsLocal.cadrastration = new aSheet(R.layout.recycler_cadrastation, R.id.recyclerCad, 1, R.id.shimmerCad, true, new aSheet.ListenerBtnClicado() {
            @Override
            public void ButtonClicable(View v) {
                buttonsLocal.myCard = v.findViewById(R.id.cardBtnProsseguir);
                buttonsLocal.myButton = v.findViewById(R.id.btnProsseguir);
                buttonsLocal.myButton.setOnClickListener(vi -> {
                    NameCadrastationDialog dialog = new NameCadrastationDialog(context, name ->{

                        if (name != null && buttonsLocal.plantaParaCadrastro != null) {
                            name = name.replaceAll("\\s", "");

                            int countPlants = db.plantDAO().CountPlants();
                            Log.d("DATABASE_D", "qtd de plantas = " + String.valueOf(countPlants));

                            db.plantDAO().InsertNewPlant(new Planta(name, countPlants, new PlantaToCadrastro(buttonsLocal.plantaParaCadrastro)));

                            if (buttonsLocal.cadrastration != null)
                            {
                                buttonsLocal.cadrastration.dismiss();
                                RegistrationPlant r1 = new RegistrationPlant(countPlants, 300f, 3500, 3200, System.currentTimeMillis());
                                r1.configLife(db);
                                db.plantDAO().InsertRegistration(r1);

                                if (callbackMain != null)
                                {
                                    Log.d("DATABASE_D", "consegui");
                                    callbackMain.initMain();
                                }
                            }
                        }


                    });
                });
            }
        });

        buttonsLocal.cadrastration.setAdapterSetter(new aSheet.setAdapterObject() {
            @Override
            public void setAdapter(BottomSheetDialogFragment sheet, RecyclerView recyclerView) {

                recyclerView.setAdapter(new AdapterCadrastation(plantsCadrastroList, recyclerView, new AdapterCadrastation.aListener() {
                    @Override
                    public void ThisIsThePlantSelected(BancoPlantsCadrastro planta) { //devolve qual planta para cadrastro foi clicado
                        if (buttonsLocal.myCard.getVisibility() == View.INVISIBLE)
                            buttonsLocal.myCard.setVisibility(View.VISIBLE);

                        buttonsLocal.plantaParaCadrastro = planta;
                        Log.d("DEBUG_CLICK", "Botao apertado");
                    }
                }));
            }
        });

        buttonsLocal.cadrastration.show(fragmentManager, "Cadrastro");


    }

    public interface CallbackMain{
        void initMain();
    }

}
