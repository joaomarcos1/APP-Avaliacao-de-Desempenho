package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.apache.commons.math3.stat.inference.TTest;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.umich.PowerTutor.R;


public class TesteT extends Activity {


    private EditText edt_APP01, edt_APP02;
    private Button voltar;
    private Button graficos_resultados;

    ArrayList<Double> somaAPP01 = new ArrayList<Double>();
    ArrayList<Double> somaAPP02 = new ArrayList<Double>();


    //Variáveis - Estatística

    float mediaaA = 0, mediaaB = 0, somaA = 0, somaB = 0;
    int countA = 0, countB = 0;

    ArrayList<Float>  mediaA = new ArrayList<Float>();
    ArrayList<Float>  mediaB = new ArrayList<Float>();
    ArrayList<Float> valoresA = new ArrayList<Float>();
    ArrayList<Float> valoresB = new ArrayList<Float>();
    ArrayList<Float> varianciaA = new ArrayList<Float>();
    ArrayList<Float> varianciaB = new ArrayList<Float>();
    ArrayList<Float> varianciaAux = new ArrayList<Float>();
    double variancA = 0;
    double variancB = 0;
    double desvioPadrao01 = 0;
    double desvioPadrao02 = 0;
    double mediaAplicativo01 = 0;
    double mediaAplicativo02 = 0;




    private TextView mediaAPP01;
    private TextView varianciaAPP01;
    private TextView desvioPadraoAPP01;

    private TextView mediaAPP02;
    private TextView varianciaAPP02;
    private TextView desvioPadraoAPP02;

    private TextView resultado_TesteT;
    ArrayList<String> nomes_APPS = new ArrayList<String>();

    private TextView nomeAPP01;
    private TextView nomeAPP02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_t);

        //edt_APP01 = (EditText) findViewById(R.id.edt_Valores_APP_01);

        //edt_APP02 = (EditText) findViewById(R.id.edt_Valores_APP_02);

        voltar = (Button) findViewById(R.id.btn_Voltar_Tela_TesteT);

        mediaAPP01 = (TextView) findViewById(R.id.txt_Media_APP01);
        mediaAPP02 = (TextView) findViewById(R.id.txt_Media_APP02);

        varianciaAPP01 = (TextView) findViewById(R.id.txt_Variancia_APP01);
        varianciaAPP02 = (TextView) findViewById(R.id.txt_Variancia_APP02);

        desvioPadraoAPP01 = (TextView) findViewById(R.id.txt_DesvioPadrao_APP01);
        desvioPadraoAPP02 = (TextView) findViewById(R.id.txt_DesvioPadrao_APP02);

        graficos_resultados = (Button) findViewById(R.id.btn_graficos_resultados);

        resultado_TesteT = (TextView) findViewById(R.id.txt_resultadoTesteT);

        nomeAPP01 = (TextView) findViewById(R.id.txt_Nome_APP_01);

        nomeAPP02 = (TextView) findViewById(R.id.txt_Nome_APP_02);


        Bundle bnd = getIntent().getExtras();

        if (bnd.containsKey("app01")){
            //somaAPP01 = bnd.getL("app01");
            somaAPP01 = (ArrayList<Double>)getIntent().getExtras().getSerializable("app01");
        }

        if (bnd.containsKey("app02")){
            somaAPP02 = (ArrayList<Double>)getIntent().getExtras().getSerializable("app02");
        }

        if (bnd.containsKey("NOMES_APPS")){
            nomes_APPS = bnd.getStringArrayList("NOMES_APPS");
        }

        nomeAPP01.setText(nomes_APPS.get(0));

        nomeAPP02.setText(nomes_APPS.get(1));

        StringBuilder stb1 = new StringBuilder();
        StringBuilder stb2 = new StringBuilder();


        double SomasAplicativo01 = 0;
        for (int i = 0; i < somaAPP01.size(); i++){
            stb1.append(Double.toString(somaAPP01.get(i))+"  ");
            SomasAplicativo01 += somaAPP01.get(i);
        }


        double SomasAplicativo02 = 0;
        for (int i = 0; i < somaAPP02.size(); i++){
            stb2.append(Double.toString(somaAPP02.get(i))+"  ");
            SomasAplicativo02 += somaAPP02.get(i);
        }


        try {
            variancA = getVariancia(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            variancB = getVariancia(somaAPP02, somaAPP02.size(), SomasAplicativo02);
            desvioPadrao01 = getDesvioPadrao(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            desvioPadrao02 = getDesvioPadrao(somaAPP02, somaAPP02.size(), SomasAplicativo02);
            mediaAplicativo01 = getMediaAritmetica(somaAPP01, somaAPP01.size());
            mediaAplicativo02 = getMediaAritmetica(somaAPP02, somaAPP02.size());


        } catch (IOException e) {
            e.printStackTrace();
        }



        //edt_APP01.setText(stb1.toString());

        //edt_APP02.setText(stb2.toString());


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity(1);
                Intent it = new Intent (TesteT.this, UMLogger.class);
                startActivity(it);
            }
        });


        graficos_resultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent (TesteT.this, graficos_resultados.class);
                it.putExtra("varianciaAPP01", variancA);
                it.putExtra("varianciaAPP02", variancB);
                it.putExtra("desvioPadraoAPP01", desvioPadrao01);
                it.putExtra("desvioPadraoAPP02", desvioPadrao02);
                it.putExtra("mediaAPP01", mediaAplicativo01);
                it.putExtra("mediaAPP02", mediaAplicativo02);
                it.putExtra("somasAPP01", somaAPP01);
                it.putExtra("somasAPP02", somaAPP02);
                it.putExtra("NOMES_APPS", nomes_APPS);
                startActivity(it);

            }
        });

        double vetorSomasAPP01[] = new double[somaAPP01.size()];
        double vetorSomasAPP02[] = new double[somaAPP02.size()];


        for (int i = 9; i < somaAPP01.size(); i++){
            vetorSomasAPP01[i] = somaAPP01.get(i);
        }

        for (int i = 9; i < somaAPP02.size(); i++){
            vetorSomasAPP02[i] = somaAPP02.get(i);
        }

        TTest testT = new TTest();

        boolean resutadoTesteT = testT.homoscedasticTTest(vetorSomasAPP01, vetorSomasAPP02, 0.05);
        boolean testeT = testT.homoscedasticTTest(vetorSomasAPP01, vetorSomasAPP02, 0.05);


        mediaAPP01.setText(Double.toString(mediaAplicativo01));
        mediaAPP02.setText(Double.toString(mediaAplicativo02));

        varianciaAPP01.setText(Double.toString(variancA));
        varianciaAPP02.setText(Double.toString(variancB));

        desvioPadraoAPP01.setText(Double.toString(desvioPadrao01));
        desvioPadraoAPP02.setText(Double.toString(desvioPadrao02));

        resultado_TesteT.setText(Boolean.toString(testeT));










    /*
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dados);
        graph.setTitle("Médias Obtidas");
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(10);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
//series.setValuesOnTopSize(50);*/



    }



    public double getSomaDosElementosAoQuadrado(ArrayList<Double> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += Math.pow(valoresl.get(counter), 2);

        return total;

    }



    public double getMediaAritmetica(ArrayList<Double> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += valoresl.get(counter);

        return total / cont;

    }

    // Desvio Padrão Amostral

    public double getDesvioPadrao(ArrayList<Double> valoresl, int cont, double soma) throws
            IOException {

        return Math.sqrt(getVariancia(valoresl, cont, soma));

    }

    //Variancia

    public double getVariancia(ArrayList<Double> valoresl, int cont, double soma) throws IOException {


        double p1 = 1 / (double) (cont - 1);

        double p2 = getSomaDosElementosAoQuadrado(valoresl, cont) - (Math.pow(soma, 2) / (double) cont);

        return (p1 * p2);

    }
}

