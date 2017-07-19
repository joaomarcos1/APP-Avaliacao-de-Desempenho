package edu.umich.PowerTutor.ui;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by João Marcos on 06/06/2017.
 */
public class Estatistica {

    float media = 0, soma = 0;
    double total = 0;

    final ArrayList<Float> mediaA = new ArrayList<Float>();
    final ArrayList<Float> mediaB = new ArrayList<Float>();
    final ArrayList<Float> valoresA = new ArrayList<Float>();
    final ArrayList<Float> valoresB = new ArrayList<Float>();
    final ArrayList<Float> varianciaA = new ArrayList<Float>();
    final ArrayList<Float> varianciaB = new ArrayList<Float>();
    final ArrayList<Float> varianciaAux = new ArrayList<Float>();
    double variancA = 0;
    double variancB = 0;


    public Estatistica(){

    }


    public Estatistica(ArrayList soma01, ArrayList soma2){

    }

    float mediaaA = 0, mediaaB = 0, somaA = 0, somaB = 0;
    int countA = 0, countB = 0;


    public double getSomaDosElementosAoQuadrado(ArrayList<Float> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += Math.pow(valoresl.get(counter), 2);

        return total;

    }



    public double getMediaAritmetica(ArrayList<Float> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += valoresl.get(counter);

        return total / cont;

    }

    // Desvio Padrão Amostral

    public double getDesvioPadrao(ArrayList<Float> valoresl, int cont, float soma) throws IOException {

        return Math.sqrt(getVariancia(valoresl, cont, soma));

    }

    //Variancia

    public double getVariancia(ArrayList<Float> valoresl, int cont, float soma) throws IOException {


        double p1 = 1 / (double) (cont - 1);

        double p2 = getSomaDosElementosAoQuadrado(valoresl, cont) - (Math.pow(soma, 2) / (double) cont);

        return (p1 * p2);

    }
}