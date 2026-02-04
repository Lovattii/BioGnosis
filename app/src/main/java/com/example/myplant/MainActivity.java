package com.example.myplant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import com.example.myplant.Planta;
import com.example.myplant.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.qamar.curvedbottomnaviagtion.BottomNavigationItem;
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation;
import androidx.core.graphics.ColorUtils;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.splashscreen.SplashScreen; // Importante importar o correto
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meow.bottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppDatabase db;


    private int ultimo_id = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //what_fuck
        db = AppDatabase.getDatabase(this);
        //don't clean this line :)

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toast.makeText(this, "Nova planta detectada!", Toast.LENGTH_SHORT).show();
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_process));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_account));

        binding.bottomNavigation.show(2, true);
        binding.bottomNavigation.setOnShowListener(item -> {
            int id_novo = item.getId();
            Fragment fragment_selecionado = null;

            switch (id_novo)
            {
                case 1:
                    fragment_selecionado = new ProcessFragment();
                    break;

                case 2:
                    fragment_selecionado = new HomeFragment();
                    break;

                case 3:
                    fragment_selecionado = new BonusFragment();
                    break;

                default:
                    break;
            }

            if (fragment_selecionado != null ) {
                if (id_novo > ultimo_id)
                    trocar_framentos(fragment_selecionado, R.anim.slide_in_right, R.anim.slide_out_left);

                else if (id_novo < ultimo_id)
                    trocar_framentos(fragment_selecionado, R.anim.slide_in_left, R.anim.slide_out_right);

                ultimo_id = id_novo;
            }

            Tasks.agendaMqtt(this);

            return null;
        });

        if(db.plantDAO().CountPlants() == 0)
        {
            Log.d("DATABASE_D", "ACHEI");
            AppDatabase.cadrastraPrimeiraPlanta(db, this, getSupportFragmentManager(), new AppDatabase.CallbackMain() {
                @Override
                public void initMain() {
                    inicializaMain();
                }
            });

            return;
        }

        else
            inicializaMain();
    }

    public void inicializaMain()
    {
        trocar_framentos(new HomeFragment(), 0, 0);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void trocar_framentos(Fragment fragment, int anim_entrada, int anim_saida)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(anim_entrada, anim_saida);
        transaction.replace(R.id.container_fragments, fragment);
        transaction.commit();
    }

}