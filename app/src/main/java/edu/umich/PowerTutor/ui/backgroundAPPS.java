package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by João Marcos on 18/05/2017.
 */
public class backgroundAPPS extends Activity implements Runnable {

    private ArrayList<String> itens;
    /*
    private Activity getActivity;

    Execucao execucao = new Execucao();
    Thread th = new Thread(execucao);

    long inicial = System.currentTimeMillis();
    long atual, total;*/


    public backgroundAPPS(){

    }


    public backgroundAPPS (ArrayList<String> its){
        itens = its;
    }


    @Override
    public void run() {
        Toast.makeText(backgroundAPPS.this, "Thread Executou!", Toast.LENGTH_SHORT).show();


        //Intent it = getPackageManager().getLaunchIntentForPackage(itens.get(0));
        //startActivity(it);
        /*while (true){
            atual = System.currentTimeMillis();
            total = atual - inicial;
            if (total >= 10000){
                Toast.makeText(backgroundAPPS.this, "Finalizado!", Toast.LENGTH_SHORT).show();
                break;
            }

        }*/
    }
}
