package com.example.myplant;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pincel.LinearProgressBar;
import com.example.pincel.Ring;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class HomeFragment extends Fragment {


    public HomeFragment() {
    }
    private long last_click_plantSHEET = 0;
    private View view;
    private View nivelBar;
    private Planta plantMain;

    public interface onPlantMain{
        void ThisIsTheMainPlant(Planta p);
    }
    private onPlantMain listenerPlantCentral;

    private LinearProgressBar progressBar1;
    private LinearProgressBar progressBar2;
    private Ring ringBar;

    private Ring ringLife;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        nivelBar = view.findViewById(R.id.nivelBar);

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

        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.plantDAO().ListenerLastRegistration(plantMain.getIdPlant()).observe(getViewLifecycleOwner(), new Observer<RegistrationPlant>() {
            @Override
            public void onChanged(RegistrationPlant registrationPlant) {
                updateProgress(registrationPlant);
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

    public void updateProgress(RegistrationPlant registro)
    {
        ringLife.setProgressAnimation((float) registro.calculateLife(
                23f, 5f, 20,
                3500, 200, 50,
                3800, 100, 30
        ));

        progressBar1.setProgressAnimation((float)registro.calculateLuminosidade());
        progressBar2.setProgressAnimation((float)registro.calculateUmidade());
        ringBar.setProgressAnimation((float)registro.calculateTemperatura());

    }
    public void setPlantInMain(Planta p)
    {
        plantMain = p;

        if (listenerPlantCentral != null)
            listenerPlantCentral.ThisIsTheMainPlant(p);

        AppDatabase db = AppDatabase.getDatabase(getContext());
        RegistrationPlant last_registro = db.plantDAO().GetLastRegistration(p.getIdPlant());

        updateProgress(last_registro);

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

        ringLife = view.findViewById(R.id.ringProgress);
        ringBar = view.findViewById(R.id.ring_temperature);
        progressBar1 = barraSol.findViewById(R.id.progressBar);
        progressBar2 = barraGota.findViewById(R.id.progressBar);

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

        ringBar.setBackIsNotAlpha(true);
        ringBar.setColorsBackground(ContextCompat.getColor(getContext(), R.color.amarelo_claro), ContextCompat.getColor(getContext(), com.example.pincel.R.color.laranja_escuro) , ContextCompat.getColor(getContext(), R.color.red));
        ringBar.setColorsProgress(android.R.color.transparent);

        ringLife.setColorsProgress(
            ContextCompat.getColor(getContext(),R.color.red),
            ContextCompat.getColor(getContext(),R.color.laranja_escuro),
            ContextCompat.getColor(getContext(),R.color.verde_escuro),
            ContextCompat.getColor(getContext(),R.color.verde_urbano),
            ContextCompat.getColor(getContext(),R.color.verde_esc_dif));

        ImageView icone2 = barraGota.findViewById(R.id.icone);
        icone2.setImageResource(R.drawable.gota);
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

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        if (context instanceof onPlantMain)
            listenerPlantCentral = (onPlantMain) context;
    }

}