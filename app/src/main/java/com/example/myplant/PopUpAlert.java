package com.example.myplant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.AlertDialogLayout;
import androidx.core.content.ContextCompat;

import com.example.myplant.databinding.ActivityMainBinding;

public class PopUpAlert {
    public enum Mensagem
    {
        VERY_WATER,
        LOW_WATER,
        VERY_SUN,
        LOW_SUN;
    }
    private Mensagem Alerta = Mensagem.LOW_SUN;

    private void upPopUp(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.pop_alert, null);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnConfirmar = dialogView.findViewById(R.id.button_ok);
        btnConfirmar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        TextView textView = dialogView.findViewById(R.id.text_main);

        if (Alerta == Mensagem.LOW_SUN)
        {
            trickImage(dialogView, context, R.id.propriety_of_plat, R.drawable.ic_sun);
            trickColor(dialogView, context, R.id.propriety_of_plat, R.color.laranja_escuro);
            textView.setText(ContextCompat.getString(context, R.string.alert_sun_lower));
        }

        else if (Alerta == Mensagem.VERY_SUN)
        {
            trickImage(dialogView, context, R.id.propriety_of_plat, R.drawable.ic_sun);
            trickColor(dialogView, context, R.id.propriety_of_plat, R.color.laranja_escuro);
            trickColor(dialogView, context, R.id.seta, R.color.red);
            rotationImage(dialogView, context, R.id.seta, 180);
            textView.setText(ContextCompat.getString(context, R.string.alert_sun_upper));
        }

        else if (Alerta == Mensagem.VERY_WATER)
        {
            rotationImage(dialogView, context, R.id.seta, 180);
            textView.setText(ContextCompat.getString(context, R.string.alert_water_upper));
            trickColor(dialogView, context, R.id.seta, R.color.red);
        }

        dialog.show();
    }

    public void setAlerta(Mensagem Alerta, Context context)
    {
        this.Alerta = Alerta;
        upPopUp(context);
    }

    private void trickImage(View dialogView, Context context, int id_Image_old, int id_Image_new)
    {
        ImageView imageOld = dialogView.findViewById(id_Image_old);
        imageOld.setImageResource(id_Image_new);
    }

    private void trickColor(View dialogView, Context context, int id_Image, int id_Color)
    {
        ImageView imageView = dialogView.findViewById(id_Image);
        Drawable drawable = imageView.getDrawable();
        drawable.mutate().setTint(ContextCompat.getColor(context, id_Color));
        imageView.setImageDrawable(drawable);
    }

    private void rotationImage(View dialogView, Context context, int id_Image, float rotation)
    {
        ImageView imageView = dialogView.findViewById(id_Image);
        imageView.setRotation(rotation);
    }


}


