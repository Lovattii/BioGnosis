package com.example.myplant;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListRegistration {
    private List<RegistrationPlant> dados;
    private static void configCalculatorsForDado(AppDatabase db, List<RegistrationPlant> dados)
    {
        for (RegistrationPlant registro : dados)
        {
            registro.configLife(db);
        }
    }
    public static List<RegistrationPlant> insereJsonInList(String newMessage, AppDatabase db)
    {
        Gson json = new Gson();

        ListRegistration lista = json.fromJson(newMessage, ListRegistration.class);
        List<RegistrationPlant> dados = lista.dados;

        if (dados != null)
            configCalculatorsForDado(db, dados);

        return dados;
    }
}
