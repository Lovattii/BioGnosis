package com.example.myplant;

import android.util.Log;

import java.io.PrintWriter;
import java.net.Socket;

public class EspConection {
    private static String ip = "192.168.4.1";
    private static int porta = 8080;

    public static void sendCommands(String command)
    {
        new Thread(()->
        {
            try {
                Log.d("DEBUG_CLICK", "Conectando ao Esp");

                Socket socket = new Socket(ip, porta);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                writer.println("PISCA");
                socket.close();
            }

            catch (Exception e)
            {
                Log.e("DEBUG_CLICK", "DEU MERDA");
            }
        }).start();
    }

}
