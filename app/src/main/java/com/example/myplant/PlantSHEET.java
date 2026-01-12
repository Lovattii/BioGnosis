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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class PlantSHEET extends BottomSheetDialogFragment {

    private ProgressBar loading;
    private RecyclerView recycler;
    private RobotPlant adapter = new RobotPlant(getContext());


    public interface ListenerOfPlantMain{
        void plantForMain(BottomSheetDialogFragment sheets, Planta p);
    }

    private ListenerOfPlantMain listenerPlant;


    private Button myButton;
    private MaterialCardView myCard;
    private BancoPlantsCadrastro plantaParaCadrastro;
    private String nomeParaCadrastro;
    private aSheet cadrastration;

    private long last_click = 0;


    public PlantSHEET(ListenerOfPlantMain listenerPlant)
    {
        this.listenerPlant = listenerPlant;
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
        BottomSheetDialogFragment sheets = this;

        adapter.setListener(new RobotPlant.PlantListener() {
            @Override
            public void onPlantSelected(Planta planta) {
                Log.d("DEBUG_CLICK", "entrouPlant");
                listenerPlant.plantForMain(sheets, planta);
            }

            @Override
            public void onPlusSelected() {
                String tag = "cadrastro";
                boolean haveShow = getParentFragmentManager().findFragmentByTag(tag) == null;
                long agora = System.currentTimeMillis();

                if (agora - last_click > 1000 && haveShow)
                {
                    last_click = agora;
                    plantaParaCadrastro = null;
                    nomeParaCadrastro = null;

                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    List<BancoPlantsCadrastro> plantsCadrastroList = db.plantDAO().GetBancoOfPlants();

                    cadrastration = new aSheet(R.layout.recycler_cadrastation, R.id.recyclerCad, 1, R.id.shimmerCad, new aSheet.ListenerBtnClicado() {
                        @Override
                        public void ButtonClicable(View v) {
                            myButton = v.findViewById(R.id.btnProsseguir);
                            myCard = v.findViewById(R.id.cardBtnProsseguir);
                            myButton.setOnClickListener(vi -> {
                                NameCadrastationDialog dialog = new NameCadrastationDialog(view.getContext(), name ->{

                                    if (name == null)
                                        Log.d("DEBUG_CLICK", "NAME");

                                    if (plantaParaCadrastro == null)
                                        Log.d("DEBUG_CLICK", "PLANTA");

                                    if (name != null && plantaParaCadrastro != null) {
                                        Log.d("DEBUG_CLICK", "FINALMENTE CHEGOU ATÉ AQUI");
                                        name = name.replaceAll("\\s", "");
                                        nomeParaCadrastro = name;
                                        AppDatabase db = AppDatabase.getDatabase(getContext());
                                        List<Planta> list = new ArrayList<>();
                                        list.add(new Planta(nomeParaCadrastro, plantaParaCadrastro.getSpecie(), plantaParaCadrastro.getIdResImage()));
                                        db.plantDAO().InsertNewPlant(list.get(0));
                                        adapter.updateList(list);
                                    }

                                    else
                                    {
                                        nomeParaCadrastro = null;
                                        plantaParaCadrastro = null;
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
                                public void ThisIsThePlantSelected(BancoPlantsCadrastro planta) {
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
}
