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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPojint;(*/

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.umich.PowerTutor.R;

public class graficos_resultados extends Activity {

    private Button voltar;
    double varianciaAPP01, varianciaAPP02, desvioPadraoAPP01, desvioPadraoAPP02, mediaAPP01, mediaAPP02;
    ArrayList<Double> somasAPP01 = new ArrayList<Double>();
    ArrayList<Double> somasAPP02 = new ArrayList<Double>();

    CharSequence opcoesInformacoes[] = {"Consumo Energético", "Desvios Padrão", "Variancia", "Média", "Moda", "Mediana"};

    private Button SalvarRelatorio;
    private Button SalvarGraficos;

    CandleStickChart candleStickChart;
    BarChart barChart;
    BarChart barChar2;

    String nomeImagem01, nomeImagem02, nomeImagem03;

    ArrayList<String> nomesAPPS = new ArrayList<String>();



    //DATABASE


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Message> messagesList = new ArrayList<Message>();
    private ListView main_listview;
    private MainAdapter mainAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String test_string;
    private String username;
    private graficos_resultados mContext;
    private TextView textView_is_typing;

    String app1, app2;



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


        SalvarGraficos = (Button) findViewById(R.id.btn_Salvar_Graficos_TesteT);

        SalvarGraficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeImagem01 = "grap1"+ System.currentTimeMillis();
                nomeImagem02 = "grap2"+ System.currentTimeMillis();
                nomeImagem03 = "grap3" + System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem01, 150) && candleStickChart.saveToGallery(nomeImagem02, 150) && barChar2.saveToGallery(nomeImagem03, 150)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
            }
        });

        SalvarRelatorio = (Button) findViewById(R.id.btn_Salvar_Relatorios__TesteT);


        SalvarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(graficos_resultados.this, "Gerando Relatório Completo!", Toast.LENGTH_SHORT).show();

                Document document = new Document();


                try {

                    Toast.makeText(getApplicationContext(), "Pdf OK1!",
                            Toast.LENGTH_SHORT).show();
                    PdfWriter.getInstance(document,
                            new FileOutputStream("/storage/emulated/0/" + System.currentTimeMillis() + ".pdf"));
                    document.open();
                    Toast.makeText(getApplicationContext(), "Pdf OK 1.1!",Toast.LENGTH_SHORT).show();
                    Paragraph paragraph = new Paragraph();
                    Paragraph paragraph2 = new Paragraph();
                    Paragraph paragraph3 = new Paragraph();
                    paragraph.setAlignment(Element.DOCUMENT_POSITION_CONTAINS);
                    paragraph.add(new Phrase("Relatório"));
                    document.add(paragraph);

                    Toast.makeText(getApplicationContext(), "Pdf OK2!",
                            Toast.LENGTH_SHORT).show();
                    Image image2 = Image.getInstance("/storage/emulated/0/DCIM/"+nomeImagem02+".jpg");
                    image2.scaleAbsolute(200f, 200f);
                    document.add(image2);

                    ///storage/emulated/0/
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();


                    /*String imageUrl = "http://jenkov.com/images/" +
                            "20081123-20081123-3E1W7902-small-portrait.jpg";
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();

                    com.itextpdf.text.Image image22 = com.itextpdf.text.Image.getInstance(new URL(imageUrl));
                    document.add(image22);*/



                    document.add(new Chunk("Este Relatório apresenta os dados da avaliação do Aplicativo " +
                            "e apresenta algumas informações estatíticas sobre o mesmo."));

                    //pega hora

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();






                    String media1 =  Double.toString(mediaAPP01);;
                    String variancia1 = Double.toString(varianciaAPP01);
                    String desvioP1 = Double.toString(desvioPadraoAPP01);
                    paragraph2.add(new Phrase("Tabela APP1"));
                    document.add(paragraph2);
                    Image image1 = Image.getInstance("/storage/emulated/0/DCIM/"+nomeImagem01+".jpg");
                    image1.scaleAbsolute(200f, 200f);
                    document.add(image1);

                    PdfPTable table1 = new PdfPTable(2); // 2 columns.


                    PdfPCell cell1 = new PdfPCell(new Paragraph("Media"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(media1));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Variancia"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph(variancia1));
                    PdfPCell cell5 = new PdfPCell(new Paragraph("Desvio Padrao"));
                    PdfPCell cell6 = new PdfPCell(new Paragraph(desvioP1));


                    table1.addCell(cell1);
                    table1.addCell(cell2);
                    table1.addCell(cell3);
                    table1.addCell(cell4);
                    table1.addCell(cell5);
                    table1.addCell(cell6);

                    document.add(table1);

                    String media2 =  Double.toString(mediaAPP02);;
                    String variancia2 = Double.toString(varianciaAPP02);
                    String desvioP2 = Double.toString(desvioPadraoAPP02);
                    paragraph3.add(new Phrase("Tabela APP2"));
                    document.add(paragraph3);

                    Image image3 = Image.getInstance("/storage/emulated/0/DCIM/"+nomeImagem03+".jpg");
                    image3.scaleAbsolute(200f, 200f);
                    document.add(image3);

                    PdfPTable table2 = new PdfPTable(2); // 2 columns.


                    PdfPCell cell7 = new PdfPCell(new Paragraph("Media"));
                    PdfPCell cell8 = new PdfPCell(new Paragraph(media2));
                    PdfPCell cell9 = new PdfPCell(new Paragraph("Variancia"));
                    PdfPCell cell10 = new PdfPCell(new Paragraph(variancia2));
                    PdfPCell cell11 = new PdfPCell(new Paragraph("Desvio Padrao"));
                    PdfPCell cell12 = new PdfPCell(new Paragraph(desvioP2));


                    table2.addCell(cell7);
                    table2.addCell(cell8);
                    table2.addCell(cell9);
                    table2.addCell(cell10);
                    table2.addCell(cell11);
                    table2.addCell(cell12);

                    document.add(table2);

                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        Bundle bnd = getIntent().getExtras();


        if(bnd.containsKey("NOMES_APPS")){
            nomesAPPS = bnd.getStringArrayList("NOMES_APPS");
        }

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


        candleStickChart = (CandleStickChart) findViewById(R.id.chart);


        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();
        entries.add(new CandleEntry(0, (float) varianciaAPP01, (float) (varianciaAPP01 - (0.90 * varianciaAPP01)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.70)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.30))));
        entries.add(new CandleEntry(1, (float) varianciaAPP02, (float) (varianciaAPP02 - (0.90 * varianciaAPP02)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.70)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.30))));


        CandleDataSet dataset = new CandleDataSet(entries, nomesAPPS.get(0)+" e "+nomesAPPS.get(1)+ ", respectivamente");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add(nomesAPPS.get(0));
        labels.add(nomesAPPS.get(1));

        CandleData data = new CandleData(labels, dataset);
        candleStickChart.setData(data);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        candleStickChart.setDescription("Variâncias");
        candleStickChart.animateY(2000);
        candleStickChart.invalidate();



        //Gráfico de Barras

        //1º Passo
        barChart = (BarChart) findViewById(R.id.chart2);
        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        for (int i = 0; i < somasAPP01.size(); i++){
            entrada.add(new BarEntry(somasAPP01.get(i).floatValue(), i));
        }


        //2º Passo
        BarDataSet dataSet = new BarDataSet(entrada, nomesAPPS.get(0));

        ArrayList<String> legends1 = new ArrayList<String>();
        for (int i = 0; i < somasAPP01.size(); i++){
            legends1.add("obs"+ (i+1));
        }

        BarData dados = new BarData(legends1, dataSet);

        //3º Passo

        barChart.setData(dados);
        barChart.setDescription("Consumo Energético "+ nomesAPPS.get(0));
        barChart.animateY(2000);
        barChart.invalidate();


        barChar2 = (BarChart) findViewById(R.id.chart3);

        ArrayList<BarEntry> entrada2 = new ArrayList<BarEntry>();
        for (int i = 0; i < somasAPP02.size(); i++){
            entrada2.add(new BarEntry(somasAPP02.get(i).floatValue(), i));
        }

        ArrayList<String> legends2 = new ArrayList<String>();
        for (int i = 0; i < somasAPP02.size(); i++){
            legends2.add("obs"+ (i+1));
        }


        BarDataSet dataSet2 = new BarDataSet(entrada2, nomesAPPS.get(1));

        BarData dados2 = new BarData(legends2, dataSet2);

        barChar2.setData(dados2);
        barChar2.setDescription("Consumo Energético "+nomesAPPS.get(1));
        barChar2.animateY(2000);
        barChar2.invalidate();




        // Botão Enviar para NUVEM


        mContext = graficos_resultados.this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.setUserProperty("user_type", "author");

        Button button_send = (Button) findViewById(R.id.button_send);
        //editText_message = (EditText) findViewById(R.id.editText_message);
        //textView_is_typing = (TextView) findViewById(R.id.textView_is_typing);
        //main_listview = (ListView) findViewById(R.id.main_listview);
        username = getSharedPreferences("PREFS", 0).getString("username", "Anonymous");
        //textView_is_typing.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        mainAdapter = new MainAdapter(mContext, messagesList);
        //main_listview.setAdapter(mainAdapter);

        test_string = null;

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                databaseReference.child("room-typing").child("irc").child(username).setValue(false);


                process_message();
                //editText_message.setText("Mensagem Fixa");
            }
        });

        databaseReference.child("users").child(MyUtils.generateUniqueUserId(mContext)).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                if (username == null) {
                    username = "Anonymous";
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

        //------------------------------------------------------------
        databaseReference.child("db_messages").limitToLast(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messagesList.add(message);
                mainAdapter.notifyDataSetChanged();
                Log.d("message", message.toString());
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildChanged", dataSnapshot.toString());
            }


            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("onChildRemoved", dataSnapshot.toString());
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildMoved", dataSnapshot.toString());
            }

        });











    }


    private void process_message() {

        String message2 = "";
        String message3 = "";
        //String ap1 = app1;
        //String ap2 = app1;

        for(int i = 0; i < somasAPP01.size(); i++){
            message2 = message2 + somasAPP01.get(i) + "&";
        }
        for(int i = 0; i < somasAPP02.size(); i++){
            message3 = message3 + somasAPP02.get(i) + "&";
        }

        //sends the db to the server.

        String username = "Anonimo";
        String key = databaseReference.child("db_messages").push().getKey();
        Message post = new Message(MyUtils.generateUniqueUserId(mContext), username, message2, message3, app1, app2, System.currentTimeMillis() / 1000L);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/db_messages/" + key, postValues);
        databaseReference.updateChildren(childUpdates);
    }







/*

    public void enviarDados(View view){


        new Thread(){
            public void run(){
                EditText nEt = (EditText) findViewById(R.id.nome);
                EditText sEt = (EditText) findViewById(R.id.sobrenome);
                EditText eEt = (EditText) findViewById(R.id.email);

                postHttp(nEt.getText().toString(), sEt.getText().toString(), eEt.getText().toString());
            }
        }.start();

    }

    public void postHttp(String nome, String sobrenome, String email){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.villopim.com.br/android/teste/server.php");

        try{
            ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
            valores.add(new BasicNameValuePair("nome", nome));
            valores.add(new BasicNameValuePair("sobrenome", sobrenome));
            valores.add(new BasicNameValuePair("email", email));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));
            final HttpResponse resposta = httpClient.execute(httpPost);

            runOnUiThread(new Runnable(){
                public void run(){
                    try {
                        Toast.makeText(getBaseContext(), EntityUtils.toString(resposta.getEntity()), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        catch(ClientProtocolException e){}
        catch(IOException e){}
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
    }*/


}

