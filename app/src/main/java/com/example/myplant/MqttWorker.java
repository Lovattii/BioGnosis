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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MqttWorker extends Worker {
    private final Context contexto;
    private static final String url = "ws://broker.hivemq.com:8000/mqtt";
    private static final String topico = "bioGnosis/sensores/dados";

    public MqttWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contexto = context;
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Log.d("WORK_KA", "Começando a pedir dados do ESP");

        CountDownLatch latch = new CountDownLatch(1);
        final boolean [] sucesso = {false};
        MqttClient mqtt = null;

        try{
            mqtt = new MqttClient(url, "BioGnosis" + System.currentTimeMillis(), new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(25);

            mqtt.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String newMessage = new String(message.getPayload());
                    Log.d("WORK_KA", "O QUE EU RECEBI :" + newMessage);

                    Gson json = new Gson();

                    ListRegistration lista = json.fromJson(newMessage, ListRegistration.class);
                    List<RegistrationPlant> registros;

                    if (lista.registros != null)
                    {
                        registros = lista.registros;
                        AppDatabase db = AppDatabase.getDatabase(contexto);
                        db.plantDAO().InsertAllListRegistration(registros);

                        Log.d("WORK_KA", "Quantidade: " + db.plantDAO().CountRegistrations());
                    }

                    sucesso[0] = true;
                    latch.countDown();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            });

            mqtt.connect(options);
            mqtt.subscribe(topico, 1);

            if(latch.await(30, TimeUnit.SECONDS))
                Log.d("WORK_KA", "jasdf");

        }

        catch (Exception e) {
            Log.e("WORK_KA", "========= ERRO MQTT =========");
            Log.e("WORK_KA", "Mensagem: " + e.getMessage());
            Log.e("WORK_KA", "Causa raiz: " + e.getCause());

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

        if (sucesso[0])
        {
            Log.d("WORK_KA", "Concluido com sucesso.. ----");
            Tasks.agendaMqtt(getApplicationContext());
            return Result.success();
        }
        else
            return Result.retry();
    }

}
