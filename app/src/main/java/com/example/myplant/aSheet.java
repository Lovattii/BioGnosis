package com.example.myplant;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeListener;

public class aSheet extends BottomSheetDialogFragment {

    private int id_layout;
    private int id_recycler;
    private int num_col;

    private int id_shimmer;

    public interface ListenerBtnClicado{
        void ButtonClicable(View v);
    }
    private ListenerBtnClicado listenerBtnClicado;

    private ShimmerFrameLayout shimmer;

    public interface setAdapterObject
    {
        void setAdapter(BottomSheetDialogFragment sheet, RecyclerView recyclerView);
    }
    private setAdapterObject setter;
    public void setAdapterSetter(setAdapterObject setter)
    {
        this.setter = setter;
    }
    private RecyclerView recycler;

    public aSheet(int id_layout, int id_recyler, int num_col, int id_shimmer_effect, ListenerBtnClicado listenerBtnClicado)
    {
        this.id_layout = id_layout;
        this.id_recycler = id_recyler;
        this.num_col = num_col;
        this.id_shimmer = id_shimmer_effect;
        this.listenerBtnClicado = listenerBtnClicado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(id_layout, container, false);

        recycler = view.findViewById(id_recycler);
        GridLayoutManager grid = new GridLayoutManager(getContext(), num_col);
        recycler.setLayoutManager(grid);
        shimmer = view.findViewById(id_shimmer);

        if (listenerBtnClicado != null)
            listenerBtnClicado.ButtonClicable(view);

        initDate();

        return view;
    }

    public void initDate()
    {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            recycler.setVisibility(View.VISIBLE);
            shimmer.setVisibility(View.GONE);

            if (setter != null)
            {
                setter.setAdapter(this, this.recycler);
                Log.d("DEBUG_CLICK", "entrouPlus");
            }

        }, 1500);

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

                bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
    }
}
