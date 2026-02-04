package com.example.myplant;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplant.databinding.ActivityMainBinding;
import com.example.pincel.Drawables;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class PlantSHEET extends BottomSheetDialogFragment {

    private ProgressBar loading;
    private RecyclerView recycler;
    private RobotPlant adapter;
    private AppDatabase db;


    public interface ListenerOfPlantMain{
        void plantForMain(BottomSheetDialogFragment sheets, Planta p);
    }

    private ListenerOfPlantMain listenerPlant;


    private Button myButton;
    private MaterialCardView myCard;
    private BancoPlantsCadrastro plantaParaCadrastro;
    private aSheet cadrastration;
    private long last_click = 0;
    BottomSheetDialogFragment sheets;

    private Planta plantaCadrastrada;

    public PlantSHEET(PlantaViewModel viewModel, AppDatabase db, ListenerOfPlantMain listenerPlant)
    {
        this.listenerPlant = listenerPlant;
        this.db = db;
        this.adapter = new RobotPlant(getContext(), viewModel);
        this.sheets = this;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent);
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                behavior.setSkipCollapsed(true);

                bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.recycler_plants, container, false);

        adapter.setListener(new RobotPlant.PlantListener() {
            @Override
            public void onPlantSelected(Planta planta) {
                Log.d("DEBUG_CLICK", "entrouPlant");
                listenerPlant.plantForMain(sheets, planta); //foi clicado em uma planta
            }

            @Override
            public void onPlusSelected() { //foi clicado no botao de adicionar uma nova planta
                String tag = "cadrastro";
                boolean haveShow = getParentFragmentManager().findFragmentByTag(tag) == null;

                if (Drawables.canClick(last_click) && haveShow)
                {
                    cadrastraPlanta(view, tag);
                }
            }
        });

        recycler = view.findViewById(R.id.recycler);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(grid);
        recycler.setAdapter(adapter);
        loading = view.findViewById(R.id.loading);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        initDados();

        return view;
    }

    public void initDados()
    {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loading.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }, 1500);

    }

    private void cadrastraPlanta(View view, String tag)
    {
        plantaParaCadrastro = null;

        List<BancoPlantsCadrastro> plantsCadrastroList = db.plantDAO().GetBancoOfPlants();

        cadrastration = new aSheet(R.layout.recycler_cadrastation, R.id.recyclerCad, 1, R.id.shimmerCad, false, new aSheet.ListenerBtnClicado() {
            @Override
            public void ButtonClicable(View v) {
                myButton = v.findViewById(R.id.btnProsseguir);
                myCard = v.findViewById(R.id.cardBtnProsseguir);
                myButton.setOnClickListener(vi -> {
                    NameCadrastationDialog dialog = new NameCadrastationDialog(view.getContext(), name ->{

                        if (name != null && plantaParaCadrastro != null) {
                            name = name.replaceAll("\\s", "");

                            int countPlants = db.plantDAO().CountPlants();

                            List<Planta> list = new ArrayList<>();
                            list.add(new Planta(name, countPlants, new PlantaToCadrastro(plantaParaCadrastro)));
                            db.plantDAO().InsertNewPlant(list.get(0));
                            adapter.updateList(list);

                            RegistrationPlant r1 = new RegistrationPlant(list.get(0).getIdPlant(), 300f, 3500, 3200, System.currentTimeMillis());
                            r1.configLife(db);
                            db.plantDAO().InsertRegistration(r1);
                            listenerPlant.plantForMain(sheets, list.get(0)); //foi clicado em uma planta
                        }

                        if (cadrastration != null)
                            cadrastration.dismiss();
                    });
                });
            }
        });


        cadrastration.setAdapterSetter(new aSheet.setAdapterObject() {
            @Override
            public void setAdapter(BottomSheetDialogFragment sheet, RecyclerView recyclerView) {

                recyclerView.setAdapter(new AdapterCadrastation(plantsCadrastroList, recyclerView, new AdapterCadrastation.aListener() {
                    @Override
                    public void ThisIsThePlantSelected(BancoPlantsCadrastro planta) { //devolve qual planta para cadrastro foi clicado
                        if (myCard.getVisibility() == View.INVISIBLE)
                            myCard.setVisibility(View.VISIBLE);

                        plantaParaCadrastro = planta;
                        Log.d("DEBUG_CLICK", "Botao apertado");
                    }
                }));
            }
        });

        cadrastration.show(getParentFragmentManager(), tag);
    }
}
