package com.example.myplant;

import static androidx.core.content.ContextCompat.getColor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.pincel.Drawables;
import com.example.pincel.GroupColors;
import com.example.pincel.ImageAnimation;
import com.example.pincel.LinearProgressBar;
import com.example.pincel.Ring;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class HomeFragment extends Fragment {


    public HomeFragment() {
    }
    private long last_click_plantSHEET = 0;
    private View view;
    private View nivelBar;
    private Planta plantMain;
    private LinearProgressBar progressBar1;
    private LinearProgressBar progressBar2;
    private Ring ringBar;
    private Ring ringLife;

    private ImageAnimation temperatura;
    private ImageView termometro;
    private ImageView solzinho;
    private ImageView gotinha;

    private AppDatabase db;
    private PlantaViewModel viewModel;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false); // view do fragmento
        context = requireContext();

        db = AppDatabase.getDatabase(context);
        viewModel = new ViewModelProvider(requireActivity()).get(PlantaViewModel.class); //nuvem que compartilha a planta com outros fragmentos

        initColorAndButtons();
        initPlantMain();

        ImageButton button_plant = view.findViewById(R.id.button_plus);
        button_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = "MochilaOfPlants";
                boolean haveShow = getParentFragmentManager().findFragmentByTag(tag) == null;

                if (haveShow && Drawables.canClick(last_click_plantSHEET))
                {
                    PlantSHEET sheet = new PlantSHEET(viewModel, db, new PlantSHEET.ListenerOfPlantMain() {
                        @Override
                        public void plantForMain(BottomSheetDialogFragment sheets, Planta p) {
                            Log.d("DATABASE_D", "Chegou a planta");
                            setPlantInMain(p);
                            sheets.dismiss();
                        }
                    });

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

        db.plantDAO().ListenerLastRegistration(plantMain.getIdPlant()).observe(getViewLifecycleOwner(), new Observer<RegistrationPlant>() {
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
        Planta firstPlant = viewModel.getPlant().getValue();
        setPlantInMain(firstPlant);
    }

    public void updateProgress(RegistrationPlant registro)
    {
        Log.d("DATABASE_D", "quantidade de registros = "+ db.plantDAO().CountRegistrations());
        ringLife.setProgressAnimation(registro.getVida());

        progressBar1.setProgressAnimation((float)registro.calculateLuminosidade(db));
        progressBar2.setProgressAnimation((float)registro.calculateUmidade(db));

        temperatura.setProgressAnimation((float)registro.calculateTemperatura(db));
        ringBar.setProgressAnimation((float)registro.calculateTemperatura(db));

        criaAlerta(progressBar1.getProgressNow(), solzinho);
        criaAlerta(progressBar2.getProgressNow(), gotinha);
        //criaAlerta(ringBar.getProgress(), (View) termometro.getParent());

    }

    private void criaAlerta(float progress, View imagem)
    {
        if (progress <= 30)
        {
            Animation pop = AnimationUtils.loadAnimation(context, R.anim.grow_up_object);
            imagem.startAnimation(pop);
        }
    }

    public void setPlantInMain(Planta p)
    {
        plantMain = p;
        viewModel.setPlant(p);

        Log.d("DATABASE_D", "id da planta = " + p.getIdPlant());

        RegistrationPlant last_registro = db.plantDAO().GetLastRegistration(p.getIdPlant());
        Log.d("DATABASE_D", "questões");
        Log.d("DATABASE_D", String.valueOf(last_registro.getTemperatura()));
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
        initProgressBar();
        initImagesAndViews();
    }

    private void initImagesAndViews()
    {
        View barraSol = nivelBar.findViewById(R.id.barra_sol);
        View barraGota = nivelBar.findViewById(R.id.barra_gota);

        solzinho = barraSol.findViewById(R.id.icone);
        Drawables.trickDrawable(solzinho, R.drawable.ic_sun, getColor(context, R.color.laranja_escuro), context);

        termometro = nivelBar.findViewById(R.id.termometro);
        temperatura = new ImageAnimation(termometro, R.drawable.ic_termometro, context, GroupColors.getColorsTemperatura(context));

        gotinha = barraGota.findViewById(R.id.icone);
        gotinha.setImageResource(R.drawable.gota);
    }

    private void initProgressBar()
    {
        nivelBar = view.findViewById(R.id.nivelBar);
        View barraSol = nivelBar.findViewById(R.id.barra_sol);
        View barraGota = nivelBar.findViewById(R.id.barra_gota);

        //barra de sol
        progressBar1 = barraSol.findViewById(R.id.progressBar);
        progressBar1.setBackIsNotAlpha(true);
        progressBar1.setColorsBackground(GroupColors.getColorsBackLuminosidade(context));
        progressBar1.setColorsProgress(GroupColors.getColorsProgressLuminosidade(context));

        //barra de umidade
        progressBar2 = barraGota.findViewById(R.id.progressBar);
        progressBar2.setBackIsNotAlpha(true);
        progressBar2.setColorsBackground(GroupColors.getColorsBackUmidade(context));
        progressBar2.setColorsProgress(GroupColors.getColorsProgressUmidade(context));

        //ring de temperatura
        ringBar = view.findViewById(R.id.ring_temperature);
        ringBar.setBackIsNotAlpha(true);
        ringBar.setColorsBackground(GroupColors.getColorsTemperatura(context));
        ringBar.setColorsProgress(android.R.color.transparent);

        ringLife = view.findViewById(R.id.ringProgress);
        ringLife.setColorsProgress(GroupColors.getColorsRing(context));
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