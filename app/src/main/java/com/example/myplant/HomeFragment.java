package com.example.myplant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ImageView termometro;
    private ImageView solzinho;
    private ImageView gotinha;
    private Ring ringBar;

    private AppDatabase db;
    private Ring ringLife;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = AppDatabase.getDatabase(getContext());

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

        ImageButton lixeira = view.findViewById(R.id.ic_bin);
        lixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.plantDAO().DeletePlantById(plantMain.getIdPlant());
                setPlantInMain(db.plantDAO().GetFirstPlant());
            }
        });

        db.plantDAO().ListenerLastRegistration(0).observe(getViewLifecycleOwner(), new Observer<RegistrationPlant>() {
            @Override
            public void onChanged(RegistrationPlant registrationPlant) {
                Log.d("WORK_KA", "Houve mudanca");
                updateProgress(registrationPlant);
            }
        });

        return view;
    }

    private void initPlantMain()
    {
        Planta firstPlant = db.plantDAO().GetFirstPlant();
        setPlantInMain(firstPlant);
    }

    public void updateProgress(RegistrationPlant registro)
    {
        ringLife.setProgressAnimation((float) registro.calculateLife(
                25f, 4f, (int)registro.getTemperatura(),
                3750, 250, (int)registro.getLuminosidade(),
                2445, 820, (int)registro.getUmidade()
        ));

        progressBar1.setProgressAnimation((float)registro.calculateLuminosidade());
        progressBar2.setProgressAnimation((float)registro.calculateUmidade());
        trickDrawable(termometro, R.drawable.ic_termometro, ringBar.getColorBackground());
        ringBar.setProgressAnimation((float)registro.calculateTemperatura());

        criaAlerta(progressBar1.getProgressNow(), solzinho);
        criaAlerta(progressBar2.getProgressNow(), gotinha);
        criaAlerta(ringBar.getProgress(), (View) termometro.getParent());

    }

    private void criaAlerta(float progress, View imagem)
    {
        if (progress <= 30)
        {
            Animation pop = AnimationUtils.loadAnimation(getContext(), R.anim.grow_up_object);
            imagem.startAnimation(pop);
        }
    }

    public void setPlantInMain(Planta p)
    {
        plantMain = p;

        if (listenerPlantCentral != null)
            listenerPlantCentral.ThisIsTheMainPlant(p);

        RegistrationPlant last_registro = db.plantDAO().GetLastRegistration(0);

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

        progressBar1.setBackIsNotAlpha(true);
        progressBar1.setColorsBackground(ContextCompat.getColor(getContext(), R.color.amarelo_claro));
        progressBar1.setColorsProgress(ContextCompat.getColor(getContext(), R.color.laranja_claro), ContextCompat.getColor(getContext(), R.color.red));

        solzinho = barraSol.findViewById(R.id.icone);
        trickDrawable(solzinho, R.drawable.ic_sun, ContextCompat.getColor(getContext(), R.color.laranja_escuro));

        termometro = nivelBar.findViewById(R.id.termometro);

        gotinha = barraGota.findViewById(R.id.icone);
        gotinha.setImageResource(R.drawable.gota);


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
    }

    private void trickDrawable(ImageView imagem, int id_drawable, int color)
    {
        Drawable drawable = ContextCompat.getDrawable(getContext(), id_drawable);
        if (drawable != null)
            drawable.setTint(color);
        imagem.setImageDrawable(drawable);
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

    public static void makeAllNotClipChildrens(View v)
    {
        v.setElevation(50f);
        v.bringToFront();
        ViewParent parent = v.getParent();
        while(parent != null && parent instanceof ViewGroup)
        {
            Log.d("CUT_C", "Cortando");
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            parent = viewGroup.getParent();
        }

        Log.d("CUT_C", "     .");
    }


}