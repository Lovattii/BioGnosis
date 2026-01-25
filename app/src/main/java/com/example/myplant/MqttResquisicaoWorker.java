package com.example.myplant;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttResquisicaoWorker extends Worker {
    private static final String url = "ws://broker.emqx.io:8083/mqtt";
    private static final String topico = "bioGnosis/sensores/listen";
    private Context context;

    public MqttResquisicaoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Log.d("WORK_KA", "Iniciando requisição... ");
        
        AppDatabase db = AppDatabase.getDatabase(context);
        String payload = String.valueOf(db.plantDAO().CountRegistrations() + 1);

        boolean []sucess = {false};
        MqttClient mqtt = null;
        try{
            mqtt = new MqttClient(url, "labubu" + System.currentTimeMillis(), new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(25);

            mqtt.connect(options);
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setRetained(true);

            mqtt.publish(topico, message);
            Log.d("WORK_KA", payload);

            sucess[0] = true;

        } catch (Exception e) {
            Log.e("WORK_KA", e.getMessage(), e);
        } finally {
            // BLOCO DE LIMPEZA OBRIGATÓRIA
            // Isso roda independente de ter dado erro ou sucesso
            if (mqtt != null) {
                try {
                    if (mqtt.isConnected()) {
                        mqtt.disconnect();
                    }
                    mqtt.close(); // Libera a memória e threads
                } catch (MqttException e) {
                    Log.e("WORK_KA", "Erro ao fechar cliente", e);
                }
            }
        }

        if (sucess[0])
            return Result.success();
        return Result.retry();
    }
}
