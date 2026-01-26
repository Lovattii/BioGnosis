package com.example.myplant;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ProcessFragment extends Fragment{
    private View view;
    private PlantaViewModel viewModel;
    private AppDatabase db;

    public ProcessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_process, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PlantaViewModel.class);
        db = AppDatabase.getDatabase(getContext());

        LineChart chartUmidade = view.findViewById(R.id.chartUmidade);
        LineChart chartLuminosidade = view.findViewById(R.id.chartLuminosidade);
        LineChart chartTemperatura = view.findViewById(R.id.chartTemperatura);

        Callback returnUmidade = new Callback() {
            @Override
            public float returnDado(RegistrationPlant registro) {
                if (registro.getUmidade() > 100)
                    return registro.getUmidade();
                return 0;
            }
        };

        Callback returnLuminosidade = new Callback() {
            @Override
            public float returnDado(RegistrationPlant registro) {
                if (registro.getLuminosidade() > 100)
                    return registro.getLuminosidade();
                return 0;
            }
        };

        Callback returnTemperatura = new Callback() {
            @Override
            public float returnDado(RegistrationPlant registro) {
                if (registro.getTemperatura() > 50)
                    return registro.getTemperatura();
                return 0;
            }
        };

        AppDatabase db = AppDatabase.getDatabase(getContext());
        List<RegistrationPlant> registros = db.plantDAO().GetAllRegistrationsById(0);

        if (registros != null)
        {
            initChart(chartUmidade, registros, returnUmidade, "UMIDADE", R.color.azul_escuro, R.color.azul_macio);
            initChart(chartLuminosidade, registros, returnLuminosidade, "LUMINOSIDADE", R.color.laranja_escuro, R.color.laranja_claro);
            initChart(chartTemperatura, registros, returnTemperatura, "TEMPERATURA", R.color.red, R.color.vermelho_trans);
        }

        initDados();

        return view;
    }

    private void initDados()
    {
        Planta p = viewModel.getPlant().getValue();

        Log.d("PLANTA_MAIN", "Passou");
        TextView num_dias = view.findViewById(R.id.txt1);
        TextView num_dias_irrigadas = view.findViewById(R.id.txt2);
        TextView num_quase_morte = view.findViewById(R.id.txt3);
        TextView pontos_vida = view.findViewById(R.id.txt4);

        TextView plant_name = view.findViewById(R.id.txtNameProcess);
        ImageView planta_image = view.findViewById(R.id.imagePlantProcess);

        plant_name.setText(p.getNome());
        int count = db.plantDAO().CountVezesIrrigadas(0);
        num_dias_irrigadas.setText(String.valueOf(count));
        num_quase_morte.setText(String.valueOf(db.plantDAO().CountKillsPlantById(0)));
        pontos_vida.setText(String.valueOf((int)db.plantDAO().GetLastRegistration(0).getVida()));
        planta_image.setImageResource(p.getIdImagePlant());
        num_dias.setText(String.valueOf(p.getPlantDays()));
    }

    private interface Callback{
        float returnDado(RegistrationPlant registro);
    }

    private void initChart(LineChart chart, List<RegistrationPlant> registros, Callback call, String titulo, int id_color_line, int id_color_ball)
    {
        List<Entry> listDados = new ArrayList<>();

        int i = 0;
        for (RegistrationPlant medida : registros)
        {
            if (call.returnDado(medida) != 0)
            {
                listDados.add(new Entry(i, call.returnDado(medida)));
                i++;
            }
        }

        XAxis axisX = chart.getXAxis();
        axisX.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisX.setGranularity(1f);
        axisX.setLabelCount(5, false);

        LineDataSet lineDado = new LineDataSet(listDados, titulo);
        initLine(lineDado, id_color_line, id_color_ball, 2, 10);

        LineData dataDado = new LineData(lineDado);
        chart.setData(dataDado);

        Description descricao = new Description();
        descricao.setText(titulo);

        YAxis axisY = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);
        axisY.setAxisMinimum(0f);
        axisY.setSpaceBottom(0f);

        chart.setDescription(descricao);
        chart.animateX(1000);
        chart.invalidate();
    }

    private void initLine(LineDataSet line, int color_line, int color_ball, int espessura, int tam_txt)
    {
        line.setColor(ContextCompat.getColor(getContext(), color_line));
        line.setCircleColor(ContextCompat.getColor(getContext(), color_ball));
        line.setLineWidth(espessura);
        line.setValueTextColor(tam_txt);
        line.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        line.setDrawFilled(true);
        line.setFillColor(ContextCompat.getColor(getContext(), color_ball));
        line.setDrawCircles(false);
        line.setDrawValues(false);

    }
}