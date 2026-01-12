package com.example.myplant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pincel.LinearProgressBar;

import java.util.List;
public class RobotPlant extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static int BUTTON_PLUS = 0;
    public static int PLANT = 1;

    public interface PlantListener{
        void onPlantSelected(Planta planta);
        void onPlusSelected();
    }
    public PlantListener listener;

    public void setListener(PlantListener listener)
    {
        this.listener = listener;
    }

    private List<Planta> listPlants;

    public RobotPlant(Context context)
    {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.listPlants = db.plantDAO().GetAllPlantas();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == PLANT)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_plants, parent, false);
            return new RobotViewHolder(view, listPlants, listener);
        }

        else
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_plus, parent, false);
            return new PlusViewHolder(view, listener);
        }


    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
            return BUTTON_PLUS;
        return PLANT;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (getItemViewType(position) == PLANT)
        {
            Planta plantNow = listPlants.get(position);

            RobotViewHolder robot = (RobotViewHolder)(holder);
            robot.nome.setText(plantNow.getNome());
            robot.lifeProgress.setProgressAnimation(50);
            robot.imagem.setImageResource(plantNow.getIdImagePlant());
            robot.idade.setText(String.valueOf(plantNow.getPlantDays()));
        }
    }

    public void updateList(List<Planta> novasPlantas)
    {
        this.listPlants.addAll(novasPlantas);
        notifyDataSetChanged();
    }

    public List<Planta> getListPlants() {
        return listPlants;
    }

    @Override
    public int getItemCount()
    {
        return listPlants.size();
    }

    public static class RobotViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome;
        LinearProgressBar lifeProgress;
        ImageView imagem;
        TextView idade;

        public RobotViewHolder(@NonNull View view, List<Planta> plantas, PlantListener listener)
        {
            super(view);
            nome = view.findViewById(R.id.plantName);
            lifeProgress = view.findViewById(R.id.progressLifeBar);
            imagem = view.findViewById(R.id.card_image);
            idade = view.findViewById(R.id.txtIdadePlant);

            Context context = lifeProgress.getContext();
            lifeProgress.setColorsProgress(
                    ContextCompat.getColor(context, com.example.pincel.R.color.red),
                    ContextCompat.getColor(context, com.example.pincel.R.color.laranja_escuro),
                    ContextCompat.getColor(context, R.color.verde_clarin),
                    ContextCompat.getColor(context, R.color.verde_alert),
                    ContextCompat.getColor(context, com.example.pincel.R.color.verde_escuro)
                    );

            view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (plantas.get(position) != null && listener != null)
                    listener.onPlantSelected(plantas.get(position));
            });
        }
    }

    public static class PlusViewHolder extends RecyclerView.ViewHolder
    {
        public PlusViewHolder(@NonNull View view, PlantListener listener) {
            super(view);

            view.setOnClickListener(v ->{

                if (listener != null)
                    listener.onPlusSelected();
            });
        }
    }

}
