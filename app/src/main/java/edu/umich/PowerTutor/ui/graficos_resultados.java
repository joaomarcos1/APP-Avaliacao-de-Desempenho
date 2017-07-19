package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



/*
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPojint;(*/

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.credentials.internal.SaveRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import edu.umich.PowerTutor.R;

public class graficos_resultados extends Activity {

    private Button voltar;
    double varianciaAPP01, varianciaAPP02, desvioPadraoAPP01, desvioPadraoAPP02, mediaAPP01, mediaAPP02;
    ArrayList<Double> somasAPP01 = new ArrayList<Double>();
    ArrayList<Double> somasAPP02 = new ArrayList<Double>();

    CharSequence opcoesInformacoes[] = {"Consumo Energético", "Desvios Padrão", "Variancia", "Média", "Moda", "Mediana"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos_resultados);


        voltar = (Button) findViewById(R.id.btn_voltar_tela_graficos);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bnd = getIntent().getExtras();

        if (bnd.containsKey("varianciaAPP01")) {
            varianciaAPP01 = bnd.getDouble("varianciaAPP01");
        }

        if (bnd.containsKey("varianciaAPP02")) {
            varianciaAPP02 = bnd.getDouble("varianciaAPP02");
        }

        if (bnd.containsKey("desvioPadraoAPP01")) {
            desvioPadraoAPP01 = bnd.getDouble("desvioPadraoAPP01");
        }

        if (bnd.containsKey("desvioPadraoAPP02")) {
            desvioPadraoAPP02 = bnd.getDouble("desvioPadraoAPP02");
        }

        if (bnd.containsKey("mediaAPP01")) {
            mediaAPP01 = bnd.getDouble("mediaAPP01");
        }

        if (bnd.containsKey("mediaAPP02")) {
            mediaAPP02 = bnd.getDouble("mediaAPP02");
        }

        if (bnd.containsKey("somasAPP01")) {
            somasAPP01 = (ArrayList<Double>) getIntent().getExtras().getSerializable("somasAPP01");
        }

        if (bnd.containsKey("somasAPP02")) {
            somasAPP02 = (ArrayList<Double>) getIntent().getExtras().getSerializable("somasAPP02");
        }


        CandleStickChart candleStickChart = (CandleStickChart) findViewById(R.id.chart);


        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();
        entries.add(new CandleEntry(0, (float) varianciaAPP01, (float) (varianciaAPP01 - (0.90 * varianciaAPP01)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.70)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.30))));
        entries.add(new CandleEntry(1, (float) varianciaAPP02, (float) (varianciaAPP02 - (0.90 * varianciaAPP02)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.70)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.30))));


        CandleDataSet dataset = new CandleDataSet(entries, "Aplicativos (1 e 2, respectivamente)");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Aplicativo 01");
        labels.add("Aplicativo 02");

        CandleData data = new CandleData(labels, dataset);
        candleStickChart.setData(data);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        candleStickChart.setDescription("Variâncias");
        candleStickChart.animateY(3000);
        candleStickChart.setBackgroundColor(Color.WHITE);



        //Gráfico de Barras

        //1º PAsso
        BarChart barChart = (BarChart) findViewById(R.id.chart2);
        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        for (int i = 0; i < somasAPP01.size(); i++){
            entrada.add(new BarEntry(somasAPP01.get(i).floatValue(), i));
        }


        //2º PAsso
        BarDataSet dataSet = new BarDataSet(entrada, "Legenda TEste");

        ArrayList<String> legends = new ArrayList<String>();
        for (int i = 0; i < somasAPP01.size(); i++){
            legends.add("obs"+i);
        }

        BarData dados = new BarData(legends, dataSet);

        //3º Passo

        barChart.setData(dados);
        barChart.setDescription("Teste 01");
        barChart.animateY(2000);
        barChart.invalidate();



    }



    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 01, 0, "Escolher Gráficos");
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
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 2", Toast.LENGTH_LONG).show();
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
                builder.setTitle("Escolha as Informações:");
                //builder.setItems(TEMPOS,
                builder.setItems(opcoesInformacoes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //tempo_escolhido = tempo_valores[item];
                        //.makeText(escolha_apps.this, "Tempo escolhido: "+tempo_escolhido, Toast.LENGTH_SHORT).show();
                        showDialog(01);

                    }
                });
                return builder.create();


        }
        return null;
    }


}

