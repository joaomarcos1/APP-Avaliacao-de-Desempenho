package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import edu.umich.PowerTutor.R;

public class resultados_teste_01_app extends Activity {


    private Button voltar;

    CharSequence opcoesInformacoes[] = {"Consumo Energético", "Desvios Padrão", "Variancia", "Média", "Moda", "Mediana"};

    ArrayList<Double> somaAPP01 = new ArrayList<Double>();

    private TextView mediaAPP01;
    private TextView varianciaAPP01;
    private TextView desvioPadraoAPP01;

    String nome_APP;


    double variancia01 = 0;
    double desvioPadrao01 = 0;
    double mediaAplicativo01 = 0;


    private LineChart mChart;

    private TextView txt_Nome_APP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_resultados_teste_01_app);


        mediaAPP01 = (TextView) findViewById(R.id.txt_media_teste_01_APP);
        varianciaAPP01 = (TextView) findViewById(R.id.variancia_teste_01_APP);
        desvioPadraoAPP01 = (TextView) findViewById(R.id.txt_Desvio_Padrao_Teste_01_APP);
        txt_Nome_APP = (TextView) findViewById(R.id.txt_Nome_APP_TESTE_01_APP);



        Bundle bnd = getIntent().getExtras();
        if (bnd.containsKey("APLICATIVO01")){
            somaAPP01 = (ArrayList<Double>)getIntent().getExtras().getSerializable("APLICATIVO01");
        }

        if (bnd.containsKey("NOME_APP")){
            nome_APP = bnd.getString("NOME_APP");
        }


        txt_Nome_APP.setText(nome_APP);


        voltar = (Button) findViewById(R.id.btn_voltar_teste_01_app);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        double SomasAplicativo01 = 0;
        for (int i = 0; i < somaAPP01.size(); i++){
            SomasAplicativo01 += somaAPP01.get(i);
        }

        try {
            variancia01 = getVariancia(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            desvioPadrao01 = getDesvioPadrao(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            mediaAplicativo01 = getMediaAritmetica(somaAPP01, somaAPP01.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*NumberFormat formatt = NumberFormat.getInstance();
        formatt.setMinimumFractionDigits(3);
        formatt.setMaximumFractionDigits(6);
        formatt.setMaximumIntegerDigits(3);
        formatt.setRoundingMode(RoundingMode.HALF_UP);*/



        mediaAPP01.setText(Double.toString(mediaAplicativo01));
        varianciaAPP01.setText(Double.toString(variancia01));
        desvioPadraoAPP01.setText(Double.toString(desvioPadrao01));


        //GRÁFICO de BARRA - MÉTODO OnCreate


        BarChart barChart= (BarChart) findViewById(R.id.chart_teste_01_app);

        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        //String aux = null;
        for (int i = 0; i < somaAPP01.size(); i++){
            //aux = Double.toString(somaAPP01.get(i));
            //entrada.add(new Entry(Float.parseFloat(aux), i));
            entrada.add(new BarEntry(somaAPP01.get(i).floatValue(), i));

        }

        //2º PAsso
        BarDataSet dataSet = new BarDataSet(entrada, "Consumo Aplicativo");

        ArrayList<String> legends = new ArrayList<String>();
        for (int i = 0; i < somaAPP01.size()+1; i++){
            legends.add("obs"+i);
        }

        BarData dados = new BarData(legends, dataSet);

        //3º Passo


        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(dados);
        barChart.setDescription("Consumo Energético (Em Joules)");
        barChart.animateY(2000);
        barChart.invalidate();


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 01, 0, "Escolher Informações");
        menu.add(0, 02, 0, "Salvar Material de Análise");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 1!", Toast.LENGTH_LONG).show();
                showDialog(0);
                return true;
            case 1:
                showDialog(01);
                Toast.makeText(resultados_teste_01_app.this, "Selecionado Opção 2", Toast.LENGTH_LONG).show();
                //showDialog(2);
                //Colocar aqui o método de salvaros dados
                return true;

        }
        return false;
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 0:
                builder.setTitle("Escolha a Informação para ser exibida:");
                //builder.setItems(TEMPOS,
                builder.setItems(opcoesInformacoes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(resultados_teste_01_app.this, "Opção esoclhida: "+opcoesInformacoes[item], Toast.LENGTH_SHORT).show();
                        //CRIAR CONDIÇÕES PARA QUANDO CADA QUE QUANDO CADA UMA DAS OPÇOÕES SEJA SELECIONADA O GRÁFICO SERÁ REESCRITO

                    }
                });
                return builder.create();

        }
        return null;
    }



    //SOMA DOS ELEMENTOS AO QUADRADO
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





    //  A PARTIR DE AGORA , OGRÁFICOS DE LINHAS!!!


    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "DataSet 1 - TESTE hahaha");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }


    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }





}
