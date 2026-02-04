package com.example.myplant;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlantaViewModel extends AndroidViewModel {
    MutableLiveData<Planta> plantaMain = new MutableLiveData<>();

     //nuvem que compartilha a planta com outros fragmentos
    // como usar :: viewModel = new ViewModelProvider(requireActivity()).get(PlantaViewModel.class);

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
            Log.d("DATABASE_D", "tentou setar planta");
            plantaMain.setValue(db.plantDAO().GetFirstPlant());

            Log.d("DATABASE_D", String.valueOf(db.plantDAO().CountPlants()));
        }

        return plantaMain;
    }

}
