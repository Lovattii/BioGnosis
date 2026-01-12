package com.example.myplant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

public class NameCadrastationDialog {

    public interface ListenerName
    {
        void ThisIsTheNameOfPlant(String nome);
    }

    public NameCadrastationDialog(Context context, ListenerName listenerName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.name_cadastration, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText text = view.findViewById(R.id.inputName);
        Button btnCancel = view.findViewById(R.id.btnCancelName);
        Button btnConfirm = view.findViewById(R.id.btnConfirmName);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutName);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null)
                {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.requestFocus();
                }

            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (listenerName != null && text.getText() != null)
                listenerName.ThisIsTheNameOfPlant(text.getText().toString());
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v ->{
            if (listenerName != null)
                listenerName.ThisIsTheNameOfPlant(null);
            dialog.dismiss();
        });

        dialog.show();
    }
}
