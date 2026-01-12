package com.example.myplant;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCadrastation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView recycler;
    private int posicaoSelecionada = RecyclerView.NO_POSITION;
    private List<BancoPlantsCadrastro> list;

    public interface aListener
    {
        void ThisIsThePlantSelected(BancoPlantsCadrastro planta);
    }

    private aListener listener;
    public AdapterCadrastation(List<BancoPlantsCadrastro> list, RecyclerView recycler, aListener listener)
    {
        this.list = list;
        this.listener = listener;
        this.recycler = recycler;
    }

    private BancoPlantsCadrastro plantaClicada;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cadrastation, parent, false);

        return new CadrastationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        BancoPlantsCadrastro planta = list.get(position);
        CadrastationViewHolder viewHolder = (CadrastationViewHolder)holder;
        viewHolder.plantImage.setImageResource(planta.getIdResImage());
        viewHolder.plantName.setText(planta.getSpecie());

        if (position == posicaoSelecionada) {
            ligaButton(viewHolder);
        }
        else {
            desligaButton(viewHolder);
        }

        viewHolder.plantButton.setOnClickListener(v -> {
            int posicao = viewHolder.getBindingAdapterPosition();

            if (posicao == posicaoSelecionada || posicao == RecyclerView.NO_POSITION)
                return;

            int oldPosition = posicaoSelecionada;
            posicaoSelecionada = posicao;

            notifyItemChanged(posicaoSelecionada);
            if (oldPosition != RecyclerView.NO_POSITION)
                notifyItemChanged(oldPosition);

            Log.d("DEBUG_CLICK", "HOHOHOH");
            plantaClicada = list.get(posicao);

            if (listener != null && plantaClicada != null)
                listener.ThisIsThePlantSelected(plantaClicada);
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class CadrastationViewHolder extends RecyclerView.ViewHolder
    {
        TextView plantName;
        ImageView plantImage;
        Button plantButton;

        View plantBtnLigado;
        View plantBtnDesligado;

        public CadrastationViewHolder(@NonNull View view){
            super(view);

            plantName = view.findViewById(R.id.txtCad);
            plantImage = view.findViewById(R.id.imageCad);
            plantButton = view.findViewById(R.id.buttonCad);
            plantBtnDesligado = view.findViewById(R.id.btnDesligado);
            plantBtnLigado = view.findViewById(R.id.btnLigado);
        }
    }
    private void desligaButton(CadrastationViewHolder viewHolder)
    {
        if (viewHolder.plantBtnLigado.getVisibility() != View.GONE)
        {
            viewHolder.plantBtnLigado.setVisibility(View.GONE);
            viewHolder.plantBtnDesligado.setVisibility(View.VISIBLE);

        }
    }

    private void ligaButton(CadrastationViewHolder viewHolder)
    {
        if (viewHolder.plantBtnLigado.getVisibility() != View.VISIBLE)
        {
            viewHolder.plantBtnLigado.setVisibility(View.VISIBLE);
            viewHolder.plantBtnDesligado.setVisibility(View.GONE);
        }
    }
}
