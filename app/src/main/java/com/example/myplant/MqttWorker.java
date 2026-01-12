package com.example.myplant;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MqttWorker extends Worker {
    private Context contexto;
    private static final String url = "tcp://broker.hivemq.com:1883";
    private static final String topico = "bioGnosis/sensores/dados";

    public MqttWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contexto = context;
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Log.d("WORK_KA", "Conectando ao worker");

        OneTimeWorkRequest proximoTrabalho = new OneTimeWorkRequest.Builder(MqttWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS) // Espera 20s
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork(
                "MONITORAMENTO",
                ExistingWorkPolicy.REPLACE,
                proximoTrabalho
        );

        CountDownLatch latch = new CountDownLatch(1);
        final boolean [] sucesso = {false};

        try{
            MqttClient mqtt = new MqttClient(url, MqttClient.generateClientId(), new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setConnectionTimeout(10);

            mqtt.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String newMessage = new String(message.getPayload());
                    Log.d("WORK_KA", "O QUE EU RECEBI :" + newMessage);

                    Gson json = new Gson();
                    DadosEspPlant dados = json.fromJson(newMessage, DadosEspPlant.class);
                    AppDatabase db = AppDatabase.getDatabase(contexto);
                    db.plantDAO().InsertRegistration(new RegistrationPlant(
                            dados.getIdDado(),
                            dados.getId_planta()));

                    sucesso[0] = true;
                    latch.countDown();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            mqtt.connect(options);
            mqtt.subscribe(topico);

            latch.await(10, TimeUnit.SECONDS);
            mqtt.disconnect();
        }

        catch (Exception e)
        {
            Log.e("WORK_KA", "NÃO CONECTOU NO MQTT" + e.getMessage(), e);
            Result.retry();
        }

        if (sucesso[0])
            return Result.success();
        else
            return Result.retry();
    }

}
