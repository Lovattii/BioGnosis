package com.example.myplant;

import android.media.Image;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplant.databinding.ActivityMainBinding;
import com.example.pincel.LinearProgressBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import meow.bottomnavigation.MeowBottomNavigation;

public class HomeFragment extends Fragment {


    public HomeFragment() {
    }
    private long last_click_plantSHEET = 0;
    private View view;
    private View nivelBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        nivelBar = view.findViewById(R.id.nivelBar);

        InsertBancoPlants();
        InsertPlants();

        initColorAndButtons();
        initPlantMain();

        ImageButton button_plant = view.findViewById(R.id.button_plus);
        Log.d("DEBUG_CLICK", "humm");

        button_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = "MochilaOfPlants";
                boolean haveShow = getParentFragmentManager().findFragmentByTag(tag) == null;

                if (haveShow && canClick(last_click_plantSHEET))
                {
                    PlantSHEET sheet = new PlantSHEET(new PlantSHEET.ListenerOfPlantMain() {
                        @Override
                        public void plantForMain(BottomSheetDialogFragment sheets, Planta p) {
                            setPlantInMain(p);
                            sheets.dismiss();
                        }
                    });

                    Log.d("DEBUG_CLICK", "OXI");
                    sheet.show(getParentFragmentManager(), tag);
                }
            }
        });

        ImageButton notification = view.findViewById(R.id.button_menu);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View barraSol = view.findViewById(R.id.barra_sol);
                LinearProgressBar progressBar = barraSol.findViewById(R.id.progressBar);
                progressBar.setProgressAnimation(progressBar.getProgressNow() - 10f);
            }
        });

        return view;
    }

    private void initPlantMain()
    {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        Planta firstPlant = db.plantDAO().GetFirstPlant();
        setPlantInMain(firstPlant);
    }

    public void setPlantInMain(Planta p)
    {
        TextView plantDays = nivelBar.findViewById(R.id.plant_days);
        TextView plantName = view.findViewById(R.id.plant_name);
        ImageView plantImage = view.findViewById(R.id.plant_image);

        plantDays.setText(String.valueOf(p.getPlantDays()));
        plantName.setText(p.getNome());
        plantImage.setImageResource(p.getIdImagePlant());
    }

    private void initColorAndButtons()
    {
        View barraSol = nivelBar.findViewById(R.id.barra_sol);
        View barraGota = nivelBar.findViewById(R.id.barra_gota);


        LinearProgressBar progressBar1 = barraSol.findViewById(R.id.progressBar);
        LinearProgressBar progressBar2 = barraGota.findViewById(R.id.progressBar);

        if (progressBar1.getProgressNow() <= 99)
        {
            PopUpAlert pop = new PopUpAlert();
            pop.setAlerta(PopUpAlert.Mensagem.LOW_WATER, getContext());
        }

        progressBar1.setBackIsNotAlpha(true);
        progressBar1.setColorsBackground(ContextCompat.getColor(getContext(), R.color.amarelo_claro));
        progressBar1.setColorsProgress(ContextCompat.getColor(getContext(), R.color.laranja_claro), ContextCompat.getColor(getContext(), R.color.red));

        ImageView icone1 = barraSol.findViewById(R.id.icone);
        icone1.setImageResource(R.drawable.sol);

        progressBar2.setBackIsNotAlpha(true);
        progressBar2.setColorsBackground(ContextCompat.getColor(getContext(), R.color.ciano));
        progressBar2.setColorsProgress(ContextCompat.getColor(getContext(), R.color.azul_claro), ContextCompat.getColor(getContext(), R.color.azul_escuro));

        ImageView icone2 = barraGota.findViewById(R.id.icone);
        icone2.setImageResource(R.drawable.gota);

    }

    private void InsertBancoPlants()
    {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("TOMATE-CEREJA", R.drawable.tomato));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("CEBOLINHA", R.drawable.cebolinha));
        db.plantDAO().InsertBancoPlantsCadrastro(new BancoPlantsCadrastro("COUVE-FLOR", R.drawable.couve_flor));
    }

    private void InsertPlants()
    {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.plantDAO().InsertNewPlant(new Planta("Alface", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfacea", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfa", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfaakds", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfacelans", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alface", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfacea", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfa", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfaakds", "ALFACE", R.drawable.central_alface));
        db.plantDAO().InsertNewPlant(new Planta("Alfacelans", "ALFACE", R.drawable.central_alface));
    }

    public boolean canClick(long last_click)
    {
        long agora = System.currentTimeMillis();

        if (agora - last_click > 1000)
        {
            last_click = agora;
            return true;
        }

        return false;
    }


}