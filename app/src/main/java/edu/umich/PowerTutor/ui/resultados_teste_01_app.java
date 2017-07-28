package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    //private Button Salvar_Grafico_Imagem;
    //private Button SalvarRelatorio;

    private BarChart barChart;

    String nomeImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_resultados_teste_01_app);


        mediaAPP01 = (TextView) findViewById(R.id.txt_media_teste_01_APP);
        varianciaAPP01 = (TextView) findViewById(R.id.variancia_teste_01_APP);
        desvioPadraoAPP01 = (TextView) findViewById(R.id.txt_Desvio_Padrao_Teste_01_APP);
        txt_Nome_APP = (TextView) findViewById(R.id.txt_Nome_APP_TESTE_01_APP);

        //Salvar_Grafico_Imagem = (Button) findViewById(R.id.btn_Salvamento_Grafico);
        //SalvarRelatorio = (Button) findViewById(R.id.btn_Salvamento_Relatorio);


        /*Salvar_Grafico_Imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeImagem = "grap"+ System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem, 50)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
            }
        });

        SalvarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        Bundle bnd = getIntent().getExtras();
        if (bnd.containsKey("APLICATIVO01")) {
            somaAPP01 = (ArrayList<Double>) getIntent().getExtras().getSerializable("APLICATIVO01");
        }

        if (bnd.containsKey("NOME_APP")) {
            nome_APP = bnd.getString("NOME_APP");
        }


        txt_Nome_APP.setText(nome_APP);


        voltar = (Button) findViewById(R.id.btn_voltar_teste_01_app);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent it = new Intent(resultados_teste_01_app.this, UMLogger.class);
                startActivity(it);
            }
        });

        double SomasAplicativo01 = 0;
        for (int i = 0; i < somaAPP01.size(); i++) {
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


        barChart = (BarChart) findViewById(R.id.chart_teste_01_app);

        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        //String aux = null;
        for (int i = 0; i < somaAPP01.size(); i++) {
            //aux = Double.toString(somaAPP01.get(i));
            //entrada.add(new Entry(Float.parseFloat(aux), i));
            entrada.add(new BarEntry(somaAPP01.get(i).floatValue(), i));

        }

        //2º PAsso
        BarDataSet dataSet = new BarDataSet(entrada, "Consumo Aplicativo");

        ArrayList<String> legends = new ArrayList<String>();
        for (int i = 0; i < somaAPP01.size() + 1; i++) {
            legends.add("obs" + i);
        }

        BarData dados = new BarData(legends, dataSet);

        //3º Passo


        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(dados);
        barChart.setDescription("Consumo Energético (Em Joules)");
        barChart.animateY(2000);
        barChart.invalidate();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }






/*
            case R.id.button_create2:

                break;*/

    //case R.id.bSair:
    //  finish();


    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 0, 0, "Salvar Gráfico");
        menu.add(0, 01, 0, "Salvar Relatório");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 1!", Toast.LENGTH_LONG).show();
                //showDialog(0);
                nomeImagem = "grap"+ System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem, 50)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                return true;
            case 1:
                Toast.makeText(resultados_teste_01_app.this, "Criar Relatório Completo!", Toast.LENGTH_SHORT).show();


                Document document = null;


                try {

                    Toast.makeText(getApplicationContext(), "Pdf OK1!",
                            Toast.LENGTH_SHORT).show();
                    PdfWriter.getInstance(document,
                            new FileOutputStream("/storage/emulated/0/" + System.currentTimeMillis() + ".pdf"));
                    document.open();

                    Paragraph paragraph = new Paragraph();
                    paragraph.setAlignment(Element.DOCUMENT_POSITION_CONTAINS);
                    paragraph.add(new Phrase("Relatório"));
                    document.add(paragraph);

                    Toast.makeText(getApplicationContext(), "Pdf OK2!",
                            Toast.LENGTH_SHORT).show();
                    Image image1 = Image.getInstance("/storage/emulated/0/DCIM/"+nomeImagem+".jpg");
                    image1.scaleAbsolute(200f, 200f);
                    document.add(image1);
                    ///storage/emulated/0/
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();


                    /*String imageUrl = "http://jenkov.com/images/" +
                            "20081123-20081123-3E1W7902-small-portrait.jpg";
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();

                    com.itextpdf.text.Image image22 = com.itextpdf.text.Image.getInstance(new URL(imageUrl));
                    document.add(image22);*/



                    document.add(new Chunk("Este Relatório apresenta os dados da avaliação do Aplicativo "+nome_APP+" e apresenta algumas informações estatíticas sobre o mesmo."));

                    //pega hora

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String hora_atual = dateFormat_hora.format(data_atual);


                    PdfPTable table = new PdfPTable(3); // 3 columns.

                    PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph(hora_atual));
                    table.addCell(new Paragraph("Text Mode"));
                    table.addCell(new Paragraph("Text Mode"));

                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                    table.addCell(cell4);

                    document.add(table);

                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        Toast.makeText(resultados_teste_01_app.this, "Opção esoclhida: " + opcoesInformacoes[item], Toast.LENGTH_SHORT).show();
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


    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

/*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "resultados_teste_01_app Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.umich.PowerTutor.ui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "resultados_teste_01_app Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.umich.PowerTutor.ui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
