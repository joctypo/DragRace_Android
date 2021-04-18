package com.example.primercontrol;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPSingelton extends Thread {


    private static TCPSingelton unicainstancia;
    private BufferedWriter bwriter;

    public static TCPSingelton  getInstance(){
        if(unicainstancia==null){

            unicainstancia= new TCPSingelton();
            unicainstancia.start();
        }

        return unicainstancia;
    }


    private TCPSingelton(){


    }

    private Socket socket;

    @Override
    public void run(){

        try {
            //En el host va la ip del pc
            Socket socket = new Socket("192.168.20.22",5000);
            OutputStream os= socket.getOutputStream();
            OutputStreamWriter osw= new OutputStreamWriter(os);
            bwriter= new BufferedWriter(osw);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void accion(String hizo) {

        new Thread(
                () -> {

                    Gson gson = new Gson();
                    Acciones acc = new Acciones(hizo);
                    //Serialización
                    String tipocoord = gson.toJson(acc);

                    try {

                        bwriter.write(tipocoord+"\n");
                        bwriter.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        ).start();

    }
}
