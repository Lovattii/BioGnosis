package com.example.myplant;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ProcessFragment extends Fragment implements HomeFragment.onPlantMain{
    private View view;

    public ProcessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_process, container, false);

        LineChart chart = view.findViewById(R.id.chartPlantProcess);

        AppDatabase db = AppDatabase.getDatabase(getContext());
        List<RegistrationPlant> registros = db.plantDAO().GetAllRegistrations();

        if (registros != null)
            initChart(chart, registros);

        return view;
    }

    private void initChart(LineChart chart, List<RegistrationPlant> registros)
    {
        List<Entry> umidade = new ArrayList<>();
        List<Entry> luminosidade = new ArrayList<>();
        List<Entry> temperatura = new ArrayList<>();
        int i = 0;

        for (RegistrationPlant medida : registros)
        {
            umidade.add(new Entry(i, medida.getUmidade()));
            luminosidade.add(new Entry(i, medida.getLuminosidade()));
            temperatura.add(new Entry(i, medida.getTemperatura()));
        }

        LineDataSet lineUmidade = new LineDataSet(umidade, "UMIDADE DO SOLO");
        LineDataSet lineLuminosidade = new LineDataSet(luminosidade, "LUMINOSIDADE");
        LineDataSet lineTemperatura = new LineDataSet(temperatura, "TEMPERATURA");

        initLine(lineUmidade, R.color.azul_macio, R.color.azul_escuro, 2, 10);
        initLine(lineLuminosidade, com.example.pincel.R.color.laranja_claro, R.color.laranja_escuro, 2, 10);
        initLine(lineTemperatura, R.color.amarelo_claro, Color.YELLOW, 2, 10);

        LineData dataUmidade = new LineData(lineUmidade);
        LineData dataLuminosidade = new LineData(lineLuminosidade);
        LineData dataTemperatura = new LineData(lineTemperatura);

        chart.setData(dataUmidade);
        chart.setData(dataLuminosidade);
        chart.setData(dataTemperatura);

        Description descricao = new Description();
        descricao.setText("HISTÓRICO DA PLANTA");

        chart.setDescription(descricao);
        chart.animateX(1000);
        chart.invalidate();
    }

    private void initLine(LineDataSet line, int color_line, int color_ball, int espessura, int tam_txt)
    {
        line.setColor(color_line);
        line.setCircleColor(color_ball);
        line.setLineWidth(espessura);
        line.getValueTextColor(tam_txt);
        line.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        line.setDrawFilled(true);
        line.setFillColor(Color.CYAN);
    }


    @Override
    public void ThisIsTheMainPlant(Planta p)
    {
        TextView num_dias = view.findViewById(R.id.txt1);
        TextView num_dias_irrigadas = view.findViewById(R.id.txt2);
        TextView num_quase_morte = view.findViewById(R.id.txt3);
        TextView pontos_vida = view.findViewById(R.id.txt4);

        TextView plant_name = view.findViewById(R.id.txtNameProcess);
        ImageView planta_image = view.findViewById(R.id.imagePlantProcess);

        plant_name.setText(p.getNome());
        planta_image.setImageResource(p.getIdImagePlant());
        num_dias.setText(String.valueOf(p.getPlantDays()));
    }
}