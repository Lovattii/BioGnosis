package com.example.myplant;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class Tasks {
    public static void agendaMqtt(Context context){
        WorkManager.getInstance(context).cancelAllWork();
        WorkManager.getInstance(context).pruneWork();

        OneTimeWorkRequest requisitaMqtt = new OneTimeWorkRequest.Builder(MqttResquisicaoWorker.class)
                .build();

        OneTimeWorkRequest getDadosMqtt = new OneTimeWorkRequest.Builder(MqttWorker.class)
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(context).beginUniqueWork(
                "MONITORAMENTO",
                ExistingWorkPolicy.REPLACE,
                requisitaMqtt)
                .then(getDadosMqtt)
                .enqueue();
    }

}
