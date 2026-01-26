package com.example.myplant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlantaViewModel extends AndroidViewModel {
    MutableLiveData<Planta> plantaMain = new MutableLiveData<>();

    public PlantaViewModel(@NonNull Application application)
    {
        super(application);
    }
    public void setPlant(Planta p)
    {
        plantaMain.setValue(p);
    }

    public LiveData<Planta> getPlant()
    {
        if (plantaMain.getValue() == null)
        {
            AppDatabase db = AppDatabase.getDatabase(getApplication());
            plantaMain.setValue(db.plantDAO().GetFirstPlant());
        }

        return plantaMain;
    }

}
