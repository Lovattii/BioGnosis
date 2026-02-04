package com.example.pincel;

import android.content.Context;

import static androidx.core.content.ContextCompat.getColor;

public class GroupColors {

    public static int[] getColorsRing(Context context) { return new int[] {
        getColor(context,R.color.red),
        getColor(context,R.color.laranja_escuro),
        getColor(context,R.color.verde_escuro),
        getColor(context,R.color.verde_urbano),
        getColor(context,R.color.verde_esc_dif) };
    }

    public static int[] getColorsProgressLuminosidade(Context context)
    {
        return new int[] {
                getColor(context, R.color.laranja_claro),
                getColor(context, R.color.red)
        };
    }

    public static int[] getColorsBackLuminosidade(Context context)
    {
        return new int []{
                getColor(context, R.color.amarelo_claro)
        };
    }

    public static int[] getColorsProgressUmidade(Context context)
    {
        return new int []{
                getColor(context, R.color.azul_claro),
                getColor(context, R.color.azul_escuro)
        };
    }

    public static int[] getColorsBackUmidade(Context context)
    {
        return new int []{
                getColor(context, R.color.ciano)
        };
    }

    public static int[] getColorsTemperatura(Context context)
    {
        return new int []{
                getColor(context, R.color.amarelo_claro),
                getColor(context, com.example.pincel.R.color.laranja_escuro),
                getColor(context, R.color.red)
        };
    }

}
