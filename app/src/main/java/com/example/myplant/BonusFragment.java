package com.example.myplant;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class BonusFragment extends Fragment {
    public BonusFragment() {
        // Required empty public constructor
    }

    private View v;
    private AppDatabase db;
    private PlantaViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bonus, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PlantaViewModel.class);

        Planta p = viewModel.getPlant().getValue();

        MaterialCardView image1 = v.findViewById(R.id.bonus50);
        MaterialCardView image2 = v.findViewById(R.id.bonus100);
        MaterialCardView image3 = v.findViewById(R.id.bonus365);
        MaterialCardView image4 = v.findViewById(R.id.bonusKill);
        MaterialCardView image5 = v.findViewById(R.id.bonusAgua);
        MaterialCardView image6 = v.findViewById(R.id.bonusLuz);
        MaterialCardView image7 = v.findViewById(R.id.bonusTemperatura);
        MaterialCardView image8 = v.findViewById(R.id.bonusVida);

        db = AppDatabase.getDatabase(getContext());

        if (p.getPlantDays() > 50)
            image1.setVisibility(VISIBLE);

        if (p.getPlantDays() > 100)
            image2.setVisibility(VISIBLE);

        if (p.getPlantDays() > 365)
            image3.setVisibility(VISIBLE);

        if (db.plantDAO().CountKillsPlantById(p.getIdPlant()) > 1000)
            image4.setVisibility(VISIBLE);

        if (db.plantDAO().CountVezesIrrigadas(p.getIdPlant()) > 1000)
            image5.setVisibility(VISIBLE);

        if (db.plantDAO().CountPlantSaudavelById(p.getIdPlant()) > 100)
            image8.setVisibility(VISIBLE);

        TextView descricao = v.findViewById(R.id.txtDescription);
        descricao.setText(ContextCompat.getString(getContext(), p.getIdDescriptionPlant()));

        return v;
    }
}