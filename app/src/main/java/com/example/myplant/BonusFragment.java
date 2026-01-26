package com.example.myplant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BonusFragment extends Fragment {
    public BonusFragment() {
        // Required empty public constructor
    }

    private View v;
    private PlantaViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bonus, container, false);



        return v;
    }
}