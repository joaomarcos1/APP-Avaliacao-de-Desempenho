/*
Copyright (C) 2011 The University of Michigan

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Please send inquiries to powertutor@umich.edu
*/

package edu.umich.PowerTutor.ui;

import edu.umich.PowerTutor.R;
import edu.umich.PowerTutor.components.Threeg;
import edu.umich.PowerTutor.service.ICounterService;
import edu.umich.PowerTutor.service.PowerEstimator;
import edu.umich.PowerTutor.service.UMLoggerService;
import edu.umich.PowerTutor.service.UidInfo;
import edu.umich.PowerTutor.util.Counter;
import edu.umich.PowerTutor.util.Recycler;
import edu.umich.PowerTutor.util.SystemInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowerTop extends Activity implements Runnable {
  private static final String TAG = "PowerTop";
  private static final double HIDE_UID_THRESHOLD = 0.1;

  public static final int KEY_CURRENT_POWER = 0;
  public static final int KEY_AVERAGE_POWER = 1;
  public static final int KEY_TOTAL_ENERGY = 2;
  private static final CharSequence[] KEY_NAMES = { "Current power",
      "Average power", "Energy usage"};

  private SharedPreferences prefs;
  private int noUidMask;
  private String[] componentNames;

  private Intent serviceIntent;
  private CounterServiceConnection conn;
  private ICounterService counterService;
  private Handler handler;

  private LinearLayout topGroup;
  private LinearLayout filterGroup;
  private LinearLayout mainView;


  private TextView txtValores;

  ArrayList<String> valores = new ArrayList<String>();


    ArrayList<String> itens = new ArrayList<String>();
    ArrayList<String> pacotes = new ArrayList<String>();

    int tempo_escolhido, observacao_escolhida;

  Long tempo;
  int cont1 = 0, cont2 = 0, cont3 = 0, cont4 = 0, cont5 = 0;


  static double temp;
  static ArrayList<Double> ini = new ArrayList<Double>();

  static int ig;

  int Sequenciaplicativo = 0;

  long inicial1 = System.currentTimeMillis();
  long atual1 = 0, atual2, inicial2, atual0 = 0;
  long tempoTotal;

  Intent it1, it2;

  static  ArrayList<Double> SomaAplicativo01 = new ArrayList<Double>();
  static ArrayList<Double> SomaAplicativo02 = new ArrayList<Double>();

    int app = 01;
  Intent it;


    boolean condicaoSalvar;

    boolean salvamento = false;

    long tempoObservacao;

    int quantidade_apps = 0;


    int aux;

    int contador_observacoes_APP01 = 0, contador_observacoes_APP02 = 0;
    boolean finalizado01 = false;

    static int contador_observacoes = 0;



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        contador_observacoes = 0;

      //Limpando ArrayList Para não entrar com lixo de testes anteriores
      itens.clear();
      pacotes.clear();
      SomaAplicativo01.clear();
      SomaAplicativo02.clear();


    //Teste para criaçao de notificação

    final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    final Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
    PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
    //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
    n.setLatestEventInfo(PowerTop.this, "PowerTop", "60segs  - T1 /- Cont: "+cont1, pend);


    final NotificationManager nm1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    final Notification n1 = new Notification(R.drawable.background, "thread2 -SECUNDÁRIA!- 10segundos completos", System.currentTimeMillis());
    PendingIntent pend1 = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
    //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
    n1.setLatestEventInfo(PowerTop.this, "PowerTop", "05 segs - T2 /- Cont: ", pend1);


    txtValores = (TextView) findViewById(R.id.txt_PowerTabs);


    //Receber Aplicativos selecionados
    Bundle bnd = getIntent().getExtras();

    String nomeServico = null;


    if (bnd.containsKey("QUANTIDADE_APPS")){
        quantidade_apps = bnd.getInt("QUANTIDADE_APPS");
    }


      if (bnd.containsKey("CONDICAO_SALVAR")){
          condicaoSalvar = bnd.getBoolean("CONDICAO_SALVAR");
      }

    if (bnd.containsKey("VALOR")) {
      itens = bnd.getStringArrayList("VALOR");

    }

    if (bnd.containsKey("VALOR2")){
      pacotes = bnd.getStringArrayList("VALOR2");
    }

    if (bnd.containsKey("OBSERVACAO")){
      observacao_escolhida = bnd.getInt("OBSERVACAO");
    }

    if (bnd.containsKey("TEMPO")){
      tempo_escolhido = bnd.getInt("TEMPO");
    }



    tempoTotal = tempo_escolhido;

    if (quantidade_apps == 01) {
        Toast.makeText(PowerTop.this, "Quantidade de observações escolhidas: " + observacao_escolhida, Toast.LENGTH_SHORT).show();
        Toast.makeText(PowerTop.this, "Pacote 01: " + pacotes.get(0), Toast.LENGTH_SHORT).show();
    }else if (quantidade_apps == 02) {
        Toast.makeText(PowerTop.this, "Tempo escolhido (tela PowerTop): " + tempoTotal, Toast.LENGTH_SHORT).show();
        Toast.makeText(PowerTop.this, "Pacote 01: " + pacotes.get(0), Toast.LENGTH_SHORT).show();
        Toast.makeText(PowerTop.this, "Pacote 02: " + pacotes.get(1), Toast.LENGTH_SHORT).show();
    }


    it =  new Intent(PowerTop.this, TesteT.class);


    final int[] contador = {0};

    if (quantidade_apps == 01){
        it1 = getPackageManager().getLaunchIntentForPackage(pacotes.get(0));
    }else if (quantidade_apps == 02){
        it1 = getPackageManager().getLaunchIntentForPackage(pacotes.get(0));
        it2 = getPackageManager().getLaunchIntentForPackage(pacotes.get(1));
    }



    String pacote = null;

    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    serviceIntent = new Intent(this, UMLoggerService.class);
    conn = new CounterServiceConnection();


      aux = tempo_escolhido/observacao_escolhida;

    if(savedInstanceState != null) {
      componentNames = savedInstanceState.getStringArray("componentNames");
      noUidMask = savedInstanceState.getInt("noUidMask");
    }

    topGroup = new LinearLayout(this);
    topGroup.setOrientation(LinearLayout.VERTICAL);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(topGroup);
    filterGroup = new LinearLayout(this);
    filterGroup.setOrientation(LinearLayout.HORIZONTAL);
    filterGroup.setMinimumHeight(50);
    mainView = new LinearLayout(this);
    mainView.setOrientation(LinearLayout.VERTICAL);
    mainView.addView(filterGroup);
    mainView.addView(scrollView);

  }

  @Override
  protected void onResume() {
    super.onResume();
    handler = new Handler();
    handler.postDelayed(this, 100);
    getApplicationContext().bindService(serviceIntent, conn, 0);

    refreshView();
  }

  /*
  @Override
  protected void onPause() {
    super.onPause();
    getApplicationContext().unbindService(conn);
    handler.removeCallbacks(this);
    handler = null;
  }*/

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putStringArray("componentNames", componentNames);
    outState.putInt("noUidMask", noUidMask);
  }

  private static final int MENU_KEY = 0;
  private static final int MENU_WINDOW = 1;
  private static final int DIALOG_KEY = 0;
  private static final int DIALOG_WINDOW = 1;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_KEY, 0, "Display Type");
    menu.add(0, MENU_WINDOW, 0, "Time Span");
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    /* We need to make sure that the user can't cause any of the dialogs to be
     * created before we have contacted the Power Tutor service to get the
     * component names and such.
     */
    for(int i = 0; i < menu.size(); i++) {
      menu.getItem(i).setEnabled(counterService != null);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case MENU_KEY:
        showDialog(DIALOG_KEY);
        return true;
      case MENU_WINDOW:
        showDialog(DIALOG_WINDOW);
        return true;
    }
    return false;
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    switch(id) {
      case DIALOG_KEY:
        builder.setTitle("Select sort key");
        builder.setItems(KEY_NAMES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
              prefs.edit().putInt("topKeyId", item).commit();
            }
        });
        return builder.create();
      case DIALOG_WINDOW:
        builder.setTitle("Select window type");
        builder.setItems(Counter.WINDOW_NAMES,
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
              prefs.edit().putInt("topWindowType", item).commit();
            }
        });
        return builder.create();
    }
    return null;
  }

  private void refreshView() {
    if(counterService == null) {
      TextView loadingText = new TextView(this);
      loadingText.setText("Waiting for profiler service...");
      loadingText.setGravity(Gravity.CENTER);
      setContentView(loadingText);
      return;
    }

    int keyId = prefs.getInt("topKeyId", KEY_TOTAL_ENERGY);
    try {
      byte[] rawUidInfo = counterService.getUidInfo(
          prefs.getInt("topWindowType", Counter.WINDOW_TOTAL),
          noUidMask | prefs.getInt("topIgnoreMask", 0));
      if(rawUidInfo != null) {
        UidInfo[] uidInfos = (UidInfo[]) new ObjectInputStream(
                new ByteArrayInputStream(rawUidInfo)).readObject();
        double total = 0;
        for (UidInfo uidInfo : uidInfos) {
          if (uidInfo.uid == SystemInfo.AID_ALL) continue;
          switch (keyId) {
            case KEY_CURRENT_POWER:
              uidInfo.key = uidInfo.currentPower;
              uidInfo.unit = "W";
              break;
            case KEY_AVERAGE_POWER:
              uidInfo.key = uidInfo.totalEnergy /
                      (uidInfo.runtime == 0 ? 1 : uidInfo.runtime);
              uidInfo.unit = "W";
              break;
            case KEY_TOTAL_ENERGY:
              uidInfo.key = uidInfo.totalEnergy;
              uidInfo.unit = "J";
              //txtValores.setText(Double.toString(uidInfo.key));

              break;
            default:
              uidInfo.key = uidInfo.currentPower;
              uidInfo.unit = "W";
          }
          total += uidInfo.key;
        }
        if (total == 0) total = 1;
        for (UidInfo uidInfo : uidInfos) {
          uidInfo.percentage = 100.0 * uidInfo.key / total;
        }
        Arrays.sort(uidInfos);

        int sz = 0;
        //Onde são inseridos os elementos que são exibidos na lista
        for (int i = 0; i < uidInfos.length; i++) {
          if (uidInfos[i].uid == SystemInfo.AID_ALL ||
                  uidInfos[i].percentage < HIDE_UID_THRESHOLD) {
            continue;
          }

          UidPowerView powerView;
          if (sz < topGroup.getChildCount()) {
            powerView = (UidPowerView) topGroup.getChildAt(sz);
          } else {
            powerView = UidPowerView.obtain(this, getIntent());
            topGroup.addView(powerView);
          }
          //alteração aqui 0xF0000000
          powerView.setBackgroundDrawable(null);
          powerView.setBackgroundColor((sz & 1) == 0 ? 0xF5785555 :
                  0xFF222222);
          //uidInfos[i] = vetor com os aplicativos em execução
          // keyID chave com o numero dos aplicativos em execução
          //alteração para enviar o nome do pacote selecionado - possibilidade futura para mandar mais de um pacot, talvez em um vetor


            //Definição de quantidade de aplicações que serão utilizadas
            // CASO 01 - Apenas verificação de valores, sem nenhuma comparação
            // CASO 02 - Verificação de valores e comparação. Utilização do TESTE T

          if (quantidade_apps == 01){



              //DELAY PARA DAR TEMPO  O APLICATIVO SER ABERTO, ASSIM O TESTE PODERA OCORRER NO SEU TEMPO NORMAL
/*
              if (cont1 < 1) {
                  Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                  startActivity(it1);
                  cont1++;
              }


              if (atual0 <= 5000){
                  atual0 = System.currentTimeMillis() - inicial1;
              }else {
                  if (cont4 < 1) {
                      inicial1 = System.currentTimeMillis();
                      cont4++;
                  }


                  if (contador_observacoes <= observacao_escolhida) {

                      //contador_aplicativos++;

                     //atual1  = System.currentTimeMillis() - inicial1;

                      //if (atual1 <= 2000) {//Dentro do que o usuário quer/SELECIONOU COMO PARÂMETROS DA AVALIAÇÃO . O QUE ESTÁ ABAIXO É O QUE DEVE ACONTECER DURANTE A EXECUÇÃO DO TESTE
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                          PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                          //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                          n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Observações: " + contador_observacoes+" - Tempo: "+atual1, pend);

                          nm.notify(R.drawable.icon, n);


                      //} else {
                          //CASO O TEMPODA OBSERVAÇÃO SEJA ATINGIDO


                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                              Toast.makeText(PowerTop.this, "Condição Salvar - TRUE!", Toast.LENGTH_SHORT).show();
                          } else {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                              Toast.makeText(PowerTop.this, "Condição Salvar - FALSE!", Toast.LENGTH_SHORT).show();
                          }




                          //cont4 = 0;
                          contador_observacoes++;
                          sz++;
                     //}
                  }else {//Se quantidade de observações até agora é menor ou igual que a escolhida
                      cont4 = 0;
                      finishActivity(1);//finalizar o único aplicativo aberto
                      stopService(serviceIntent);//Finalizar a service que faz a verificação de consumo de energi, que está rodando em segundo plano
                      counterService = null;//Finalizar serviço rodando em segundo plano
                      handler = null;
                      //Toast.makeText(PowerTop.this, "Teste Finalizado! Exibindo Resultados", Toast.LENGTH_SHORT).show();
                      /// /startActivity(it);//abrir tela
                      Intent it0 = new Intent(PowerTop.this, resultados_teste_01_app.class);
                      it0.putExtra("APLICATIVO01", SomaAplicativo01);//MANDANDO SOMA DOS CONSUMOS ENERGETICOS CALCULADAS
                      //it0.putExtra("APLICATIVO01", arr);
                      //MANDA R NOME E PACOTE DOS APLICATIVOS
                      it0.putExtra("NOME_APP", itens.get(0));
                      startActivity(it0);

                  }

              }*/





              /*
              //DELAY PARA DAR TEMPO  O APLICATIVO SER ABERTO, ASSIM O TESTE PODERA OCORRER NO SEU TEMPO NORMAL

              if (cont1 < 1) {
                  Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                  startActivity(it1);
                  cont1++;
              }
              if (atual0 <= 5000){
                  atual0 = System.currentTimeMillis() - inicial1;
              }else {
                  if (cont4 < 1) {
                      inicial1 = System.currentTimeMillis();
                      cont4++;
                  }

                  //contador_aplicativos++;
                  atual1 = System.currentTimeMillis() - inicial1;

                  //long aux = tempo_escolhido/observacao_escolhida;
                  //aux = contador_aplicativos * 1000;
                  //tempoObservacao += 1000;
                  tempoObservacao += 900;

                  //if (atual1 <= tempo_escolhido) {
                  if (atual1 <= tempo_escolhido) {
                      NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                      Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                      PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                      //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                      n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Tempo: " + atual1, pend);

                      nm.notify(R.drawable.icon, n);


                      if (tempoObservacao >= (tempo_escolhido / observacao_escolhida)) {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                          tempoObservacao = 0;
                      } else {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                          powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                      }
                      sz++;
                      //} else if (atual1 > tempo_escolhido) {
                  } else if (atual1 > tempo_escolhido) {
                      cont4 = 0;
                      finishActivity(1);//finalizar o único aplicativo aberto
                      stopService(serviceIntent);//Finalizar a service que faz a verificação de consumo de energi, que está rodando em segundo plano
                      counterService = null;//Finalizar serviço rodando em segundo plano
                      handler = null;
                      Toast.makeText(PowerTop.this, "Teste Finalizado! Exibindo Resultados", Toast.LENGTH_SHORT).show();
                      /// /startActivity(it);//abrir tela
                      Intent it0 = new Intent(PowerTop.this, resultados_teste_01_app.class);
                      it0.putExtra("APLICATIVO01", SomaAplicativo01);//MANDANDO SOMA DOS CONSUMOS ENERGETICOS CALCULADAS
                      //MANDA R NOME E PACOTE DOS APLICATIVOS
                      it0.putExtra("NOME_APP", itens.get(0));
                      startActivity(it0);
                  }
              }*/




        /*
              //DELAY PARA DAR TEMPO  O APLICATIVO SER ABERTO, ASSIM O TESTE PODERA OCORRER NO SEU TEMPO NORMAL
              if (cont1 < 1) {
                  Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                  startActivity(it1);
                  inicial1 = System.currentTimeMillis();
                  cont1++;
              }
              if (atual0 <= 5000){
                  atual0 = System.currentTimeMillis() - inicial1;
              }else {
                  if (cont4 < 1) {
                      inicial1 = System.currentTimeMillis();
                      cont4++;
                  } else {
                      atual1 = System.currentTimeMillis() - inicial1;
                      if (atual1 >= 2000 && atual1 <= 2500){
                          if (contador_observacoes <= observacao_escolhida) {
                              finalizado01 = false;

                          }else{
                              finalizado01 = true;
                          }

                          if (atual1 >2500){
                              //cont4 = 0;
                              Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                              PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                              //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                              n.setLatestEventInfo(PowerTop.this, "Finalizado!", "Limite de observações atingido!", pend);
                          }
                      }else {
                          //atual1 = System.currentTimeMillis() - inicial1;
                          finalizado01 = true;
                      }



                    if (finalizado01 == true) {
                            //(abaixo) O QUE FAZER QUANDO TODAS AS OBSERVAÇÕES FORAM FEITAS
                            cont4 = 0;
                            finishActivity(1);//finalizar o único aplicativo aberto
                            stopService(serviceIntent);//Finalizar a service que faz a verificação de consumo de energi, que está rodando em segundo plano
                            counterService = null;//Finalizar serviço rodando em segundo plano
                            handler = null;
                            Toast.makeText(PowerTop.this, "Teste Finalizado! Exibindo Resultados", Toast.LENGTH_SHORT).show();
                            /// /startActivity(it);//abrir tela
                            Intent it0 = new Intent(PowerTop.this, resultados_teste_01_app.class);
                            it0.putExtra("APLICATIVO01", SomaAplicativo01);//MANDANDO SOMA DOS CONSUMOS ENERGETICOS CALCULADAS
                            //MANDA R NOME E PACOTE DOS APLICATIVOS
                            it0.putExtra("NOME_APP", itens.get(0));
                            startActivity(it0);

                    }else{
                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                        PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                        //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                        n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Observação: " + contador_observacoes, pend);

                        nm.notify(R.drawable.icon, n);

                        //atual1 = System.currentTimeMillis() - inicial1;

                        //Tempo TOTAL para a observação, caso atingido, será feita outra
                        if (condicaoSalvar == true) {
                            powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                        } else
                            powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                        sz++;
                        contador_observacoes++;
                    }


                  }

                  //contador_aplicativos++;


                  //long aux = tempo_escolhido/observacao_escolhida;
                  //aux = contador_aplicativos * 1000;
                  //tempoObservacao += 1000;
                  /*tempoObservacao += 900;

                  //if (atual1 <= tempo_escolhido) {
                  if (atual1 <= tempo_escolhido) {
                      NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                      Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                      PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                      //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                      n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Tempo: " + atual1, pend);

                      nm.notify(R.drawable.icon, n);


                      if (tempoObservacao >= (tempo_escolhido / observacao_escolhida)) {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                          tempoObservacao = 0;
                      } else {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                          powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                      }
                      sz++;
                      //} else if (atual1 > tempo_escolhido) {
                  } else if (atual1 > tempo_escolhido) {
                      cont4 = 0;
                      finishActivity(1);//finalizar o único aplicativo aberto
                      stopService(serviceIntent);//Finalizar a service que faz a verificação de consumo de energi, que está rodando em segundo plano
                      counterService = null;//Finalizar serviço rodando em segundo plano
                      handler = null;
                      Toast.makeText(PowerTop.this, "Teste Finalizado! Exibindo Resultados", Toast.LENGTH_SHORT).show();
                      /// /startActivity(it);//abrir tela
                      Intent it0 = new Intent(PowerTop.this, resultados_teste_01_app.class);
                      it0.putExtra("APLICATIVO01", SomaAplicativo01);//MANDANDO SOMA DOS CONSUMOS ENERGETICOS CALCULADAS
                      //MANDA R NOME E PACOTE DOS APLICATIVOS
                      it0.putExtra("NOME_APP", itens.get(0));
                      startActivity(it0);
                  }
              }
              */





              //DELAY PARA DAR TEMPO  O APLICATIVO SER ABERTO, ASSIM O TESTE PODERA OCORRER NO SEU TEMPO NORMAL
              if (cont1 < 1) {
                  Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                  startActivity(it1);
                  cont1++;
              }
              if (atual0 <= 5000){
                  atual0 = System.currentTimeMillis() - inicial1;
              }else {
                  if (cont4 < 1){
                      inicial1 = System.currentTimeMillis();
                      cont4++;
                  }

                  atual1 = System.currentTimeMillis() - inicial1;
                  //atual1 += 1000
                  //tempoObservacao += 1000;
                  tempoObservacao += 1000;

                  if ((atual1 <= (tempo_escolhido*2)) && (contador_observacoes < observacao_escolhida)) {
                      NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                      Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                      PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                      //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                      n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Observacões: "+contador_observacoes+"/"+observacao_escolhida, pend);

                      nm.notify(R.drawable.icon, n);


                      if (tempoObservacao >= (tempo_escolhido / observacao_escolhida)) {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                          tempoObservacao = 0;
                      } else {
                          if (condicaoSalvar == true) {
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                          } else
                              powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                      }
                      sz++;
                      //} else if (atual1 > (tempo_escolhido*2)) {
                  }else{
                      cont4 = 0;
                      finishActivity(1);//finalizar o único aplicativo aberto
                      stopService(serviceIntent);//Finalizar a service que faz a verificação de consumo de energi, que está rodando em segundo plano
                      counterService = null;//Finalizar serviço rodando em segundo plano
                      handler = null;
                      Toast.makeText(PowerTop.this, "Teste Finalizado! Exibindo Resultados", Toast.LENGTH_SHORT).show();
                      /// /startActivity(it);//abrir tela
                      Intent it0 = new Intent(PowerTop.this, resultados_teste_01_app.class);
                      it0.putExtra("APLICATIVO01", SomaAplicativo01);//MANDANDO SOMA DOS CONSUMOS ENERGETICOS CALCULADAS
                      //MANDA R NOME E PACOTE DOS APLICATIVOS
                      it0.putExtra("NOME_APP", itens.get(0));
                      startActivity(it0);
                  }
              }





              }else if (quantidade_apps == 02) {




              if (app == 01) {
                  if (cont1 < 1) {
                      Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                      startActivity(it1);
                      cont1++;
                  }

                  if (atual0 <= 5000) {
                      atual0 = System.currentTimeMillis() - inicial1;
                  } else {
                      if (cont4 < 1){
                          inicial1 = System.currentTimeMillis();
                          cont4++;
                          contador_observacoes = 0;
                      }
                      atual1 = System.currentTimeMillis() - inicial1;
                      tempoObservacao += 1000;
                      //if (atual1 < tempo_escolhido) {
                      if ((atual1 <= (tempo_escolhido*2)) && (contador_observacoes < observacao_escolhida)){
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                          PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                          //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                          n.setLatestEventInfo(PowerTop.this, "Avaliando "+itens.get(0),  "Observacões: "+contador_observacoes+"/"+observacao_escolhida, pend);//Notificação mostrada enquanto estiver avaliando o APP

                          nm.notify(R.drawable.icon, n);



                          if (tempoObservacao >= (tempo_escolhido / observacao_escolhida)) {
                              if (condicaoSalvar == true) {
                                  powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                              tempoObservacao = 0;
                          } else {
                              if (condicaoSalvar == true) {
                                  powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);
                          }
                          sz++;
                      } else{
                          app = 02;
                          cont4 = 0;
                          finishActivity(1);
                      }
                  }
              } else {
                  if (cont2 < 1) {//Abrir o aplicativo antes de pergar o momento inicial
                      Toast.makeText(PowerTop.this, "Iniciando "+ itens.get(1), Toast.LENGTH_SHORT).show();//Aviso Iniciando Aplicativo 02!
                      startActivity(it2);
                      cont2++;
                  }
                  if (cont5 < 1) {
                      atual0 = 0;
                      inicial2 = System.currentTimeMillis();
                      cont5++;
                  }

                  if (atual0 <= 5000) {
                      atual0 = System.currentTimeMillis() - inicial2;
                  } else {
                      if (cont4 < 1) {
                          inicial2 = System.currentTimeMillis();
                          cont4++;
                          contador_observacoes = 0;
                      }

                      atual2 = System.currentTimeMillis() - inicial2;
                      tempoObservacao += 1000;

                      //if (atual2 < (tempo_escolhido + 500)) {
                          //if (atual >= (tempoTotal/2) && (atual <= tempoTotal)){
                     if ((atual2 <= (tempo_escolhido*2)) && (contador_observacoes < observacao_escolhida)){
                          NotificationManager nm1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          Notification n1 = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                          PendingIntent pend1 = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                          //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                          n1.setLatestEventInfo(PowerTop.this, "Avaliando "+itens.get(1),  "Observacões: "+contador_observacoes+"/"+observacao_escolhida, pend1);
                          nm1.notify(R.drawable.icon, n1);




                          if (tempoObservacao >= ((tempo_escolhido) / observacao_escolhida)) {
                              if (condicaoSalvar == true){
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, false);

                              tempoObservacao = 0;

                          } else {
                              if (condicaoSalvar == true){
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, false);


                          }
                          //powerView.init(uidInfos[i], keyId, itens.get(1), 02);
                          sz++;


                      } else {
                          finishActivity(1);
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                          PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                          //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                          n.setLatestEventInfo(PowerTop.this, " Avaliação Finalizada!", "Tempo Limite Atingido!", pend);
                          nm.notify(R.drawable.icon, n);
                          finishActivity(1);


                          it.putExtra("app01", SomaAplicativo01);
                          it.putExtra("app02", SomaAplicativo02);
                          it.putExtra("NOMES_APPS", itens);
                          if (cont3 < 1) {
                              stopService(serviceIntent);
                              counterService = null;
                                  /*super.onPause();
                                  getApplicationContext().unbindService(conn);
                                  handler.removeCallbacks(this);
                                  handler = null;*/
                              startActivity(it);
                              Toast.makeText(PowerTop.this, "Condição. Abrir Tela Teste T", Toast.LENGTH_SHORT).show();
                              cont3++;
                          }


                          //powerView.init(uidInfos[i], keyId, itens.get(1), 02);
                          //sz++;
                      }

                  }
              }





















/*
              if (app == 01) {
                  if (cont1 < 1) {
                      Toast.makeText(PowerTop.this, "Iniciando "+itens.get(0), Toast.LENGTH_SHORT).show();
                      startActivity(it1);
                      cont1++;
                  }

                  if (atual0 <= 5000) {
                      atual0 = System.currentTimeMillis() - inicial1;
                  } else {
                      if (cont4 < 1) {
                          inicial1 = System.currentTimeMillis();
                          cont4++;
                      }

                      if (contador_observacoes_APP01 <= observacao_escolhida) {

                          atual1 = System.currentTimeMillis() - inicial1;

                          if (atual1 <= 2500) {
                              NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                              Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                              PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                              //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                              n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(0), "Observações: " + contador_observacoes_APP01+" - Tempo: "+atual1, pend);//Notificação mostrada enquanto estiver avaliando o APP

                              nm.notify(R.drawable.icon, n);


                          } else if (atual1 > 2500) {
                              if (condicaoSalvar == true) {
                                  //powerView.init(uidInfos[i], keyId, itens.get(0), 01, true);
                              } else
                                  //powerView.init(uidInfos[i], keyId, itens.get(0), 01, false);

                              sz++;
                              cont4 = 0;
                              contador_observacoes_APP01++;
                          }
                      }else{
                          app = 02;
                          cont4 = 0;
                          finishActivity(1);
                      }


                  }


              } else {
                  if (cont2 < 1) {//Abrir o aplicativo antes de pergar o momento inicial
                      Toast.makeText(PowerTop.this, "Iniciando "+ itens.get(1), Toast.LENGTH_SHORT).show();//Aviso Iniciando Aplicativo 02!
                      startActivity(it2);
                      cont2++;
                  }
                  if (cont5 < 1) {
                      atual0 = 0;
                      inicial2 = System.currentTimeMillis();
                      cont5++;
                  }

                  if (atual0 <= 5000) {
                      atual0 = System.currentTimeMillis() - inicial2;
                  } else {
                      if (cont4 < 1) {
                          inicial2 = System.currentTimeMillis();
                          cont4++;
                      }


                      if (contador_observacoes_APP02 <= observacao_escolhida) {

                          atual2 = System.currentTimeMillis() - inicial2;

                          if (atual2 <= 2500) {
                              NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                              Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                              PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                              //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                              n.setLatestEventInfo(PowerTop.this, "Avaliando " + itens.get(1), "Observações: " + contador_observacoes_APP02+" - Tempo: "+atual2, pend);//Notificação mostrada enquanto estiver avaliando o APP

                              nm.notify(R.drawable.icon, n);


                          } else if (atual2 > 2500) {
                              if (condicaoSalvar == true) {
                                  //powerView.init(uidInfos[i], keyId, itens.get(1), 02, true);
                              } else
                                 // powerView.init(uidInfos[i], keyId, itens.get(1), 02, false);

                              sz++;
                              cont4 = 0;
                              contador_observacoes_APP02++;

                          if (tempoObservacao >= ((tempo_escolhido+500) / observacao_escolhida)) {
                              if (condicaoSalvar == true){
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, false);

                              tempoObservacao = 0;

                          } else {
                              if (condicaoSalvar == true){
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, true);
                              }else
                                  powerView.init(uidInfos[i], keyId, itens.get(1), 02, false);


                          }
                          //powerView.init(uidInfos[i], keyId, itens.get(1), 02);
                          sz++;


                          }

                      }else{
                          finishActivity(1);
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                          PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                          //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                          n.setLatestEventInfo(PowerTop.this, " Avaliação Finalizada!", "Tempo Limite Atingido!", pend);
                          nm.notify(R.drawable.icon, n);
                          finishActivity(1);


                          it.putExtra("app01", SomaAplicativo01);
                          it.putExtra("app02", SomaAplicativo02);
                          it.putExtra("NOMES_APPS", itens);
                          if (cont3 < 1) {
                              stopService(serviceIntent);
                              counterService = null;

                              startActivity(it);
                              Toast.makeText(PowerTop.this, "Condição. Abrir Tela Teste T", Toast.LENGTH_SHORT).show();
                              cont3++;
                          }


                          //powerView.init(uidInfos[i], keyId, itens.get(1), 02);
                          //sz++;
                      }

                  }
              }*/

          }


            //Depois da leitura dos dois aplicativos
              /*if (atual > tempoTotal) {

                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification n = new Notification(R.drawable.icon, "thread1 -GERAL!- 60 segundos completos", System.currentTimeMillis());
                PendingIntent pend = PendingIntent.getActivity(this, 0, new Intent(this, UMLogger.class), 0);
                //n.setLatestEventInfo(this, titulo, mensagem, PendingIntent);
                n.setLatestEventInfo(PowerTop.this, "Finalizado!", "Tempo Limite Atingido!", pend);
                nm.notify(R.drawable.icon, n);
                finishActivity(1);


                it.putExtra("app01", SomaAplicativo01);
                it.putExtra("app02", SomaAplicativo02);
                if (cont3 < 1) {
                  //startActivity(it);
                    Toast.makeText(PowerTop.this, "Condição. Abrir Tela Teste T", Toast.LENGTH_SHORT).show();
                  cont3++;
                }


                powerView.init(uidInfos[i], keyId, itens.get(1), 02);
                sz++;

              }*/
        }
        for(int i = sz; i < topGroup.getChildCount(); i++) {
          UidPowerView powerView = (UidPowerView)topGroup.getChildAt(i);
          powerView.recycle();
        }
        topGroup.removeViews(sz, topGroup.getChildCount() - sz);
      }
    } catch(IOException e) {
    } catch(RemoteException e) {
    } catch(ClassNotFoundException e) {
    } catch(ClassCastException e) {
    }
    setContentView(mainView);
    if(keyId == KEY_CURRENT_POWER) {
      setTitle(KEY_NAMES[keyId]);
    } else {
      setTitle(KEY_NAMES[keyId] + " over " +
               Counter.WINDOW_DESCS[prefs.getInt("topWindowType",
                                                 Counter.WINDOW_TOTAL)]);
    }
  }




    //Execução da Thread Geral
  public void run() {
    refreshView();
    if(handler != null) {
      //Alteração Aqui - 2 anteriormente) // ALterar para salvar

      handler.postDelayed(this, 1 * PowerEstimator.ITERATION_INTERVAL);
    }
  }

  public boolean conferePacote(String pack){
    PackageManager packageManager=getPackageManager();
    String retorno = null;
    //LinearLayout linear = (LinearLayout) findViewById(android.R.layout.linear);
    List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    for(ApplicationInfo ap:list){
      String nome = ap.packageName;
      if ((nome.equalsIgnoreCase(pack))){
        retorno = nome;
        return true;
      }
    }
    return false;
  }



  private static class UidPowerView extends LinearLayout {
    private static Recycler<UidPowerView> recycler =
        new Recycler<UidPowerView>();
    private static DecimalFormat formatter = new DecimalFormat("0.0");


      public static UidPowerView obtain(Activity activity, Intent startIntent) {
      UidPowerView result = recycler.obtain();
      if(result == null) return new UidPowerView(activity, startIntent);
      return result;
    }

    public void recycle() {
      recycler.recycle(this);
    }

    private UidInfo uidInfo;
    private String name;
    private Drawable icon;

    private ImageView imageView;
    private TextView textView;


    private UidPowerView(final Activity activity, final Intent startIntent) {
      super(activity);
      setMinimumHeight(50);
      setOrientation(LinearLayout.HORIZONTAL);
      imageView = new ImageView(activity);
      imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      imageView.setAdjustViewBounds(true);
      imageView.setMaxHeight(40);
      imageView.setMaxWidth(40);
      imageView.setMinimumWidth(50);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.FILL_PARENT));
      textView = new TextView(activity);
      textView.setGravity(Gravity.CENTER_VERTICAL);
      textView.setLayoutParams(new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.FILL_PARENT,
          ViewGroup.LayoutParams.FILL_PARENT));
      addView(imageView);
      addView(textView);
      setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Intent viewIntent = new Intent(v.getContext(), PowerTabs.class);
          viewIntent.putExtras(startIntent);
          viewIntent.putExtra("uid", uidInfo.uid);
          activity.startActivityForResult(viewIntent, 0);
        }
      });
      setFocusable(true);
    }

    //método usado para obter as informações dos aplicativos

    public void init(UidInfo uidInfo, int keyType, String nomeAPP, int condicao, boolean condicaoSalvar){
        SystemInfo sysInfo = SystemInfo.getInstance();
        this.uidInfo = uidInfo;
        PackageManager pm = getContext().getPackageManager();

        //descobrir nome do pacote
        name = sysInfo.getUidName(uidInfo.uid, pm);
        String nome1 = null;
        /*try {
          ApplicationInfo app = getContext().getPackageManager().getApplicationInfo(itens.get(0), 0);
          nome1 = (String) getContext().getPackageManager().getApplicationLabel(app);


          //fecha decobrimento de pacote do aplicativo
        } catch (PackageManager.NameNotFoundException e) {
          e.printStackTrace();
        }*/


        icon = sysInfo.getUidIcon(uidInfo.uid, pm);
        imageView.setImageDrawable(icon);
        String prefix;
        if(uidInfo.key > 1e12) {
          prefix = "G";
          uidInfo.key /= 1e12;
        } else if(uidInfo.key > 1e9) {
          prefix = "M";
          uidInfo.key /= 1e9;
        } else if(uidInfo.key > 1e6) {
          prefix = "k";
          uidInfo.key /= 1e6;
        } else if(uidInfo.key > 1e3) {
          prefix = "";
          uidInfo.key /= 1e3;
        } else {
          prefix = "m";
        }
        long secs = (long)Math.round(uidInfo.runtime);


        //Conteúdo que e exibido nas listas
        textView.setText(String.format("%1$.1f%% [%3$d:%4$02d:%5$02d] %2$s\n" +
            "%6$.1f %7$s%8$s",
            uidInfo.percentage, name, secs / 60 / 60, (secs / 60) % 60,
            secs % 60, uidInfo.key, prefix, uidInfo.unit));



        StringBuilder numeros = null;
      /*
        if (name.equalsIgnoreCase(itens.get(0))){
          textView.setText(name + " + ENCONTRADO EM SEGUNDO PLANO! -- Consumo:" +uidInfo.key+ prefix + uidInfo.unit);
          File f = new File ("/storage/emulated/0/log_"+name+".txt");
          if (!f.exists()){
            try {
              f.createNewFile();
            }catch (IOException e){
              e.printStackTrace();
            }
          }else{
            try{
              FileWriter arq = new FileWriter(f);
              PrintWriter gravarArq = new PrintWriter(arq);
              StringBuilder recebeValores = new StringBuilder();
              BufferedReader br = new BufferedReader(new FileReader(f));
              String linha = null;
            while ((linha = br.readLine()) != null) {
              //b.append(Float.toString(media)).append(System.lineSeparator());
              recebeValores.append(linha).append("\n");
              //recebeValores.add(linha);
              //recebeValores.add("\n");
            }
            recebeValores.append(uidInfo.key+ prefix + uidInfo.unit);
            gravarArq.printf(recebeValores.toString());

              arq.close();
            //gravarArq.printf(uidInfo.key+ prefix + uidInfo.unit);
          } catch (IOException e) {
              e.printStackTrace();
          }
          }
        }else
          textView.setText("Nome: "+name+" /// Consumo: " +uidInfo.key+ prefix + uidInfo.unit);*/

      String vals, unidade;
      double valor = 0;
      double vfim;

          if (name.equalsIgnoreCase(nomeAPP)) {
              textView.setText(name + " + ENCONTRADO EM SEGUNDO PLANO! -- Consumo:" + uidInfo.key + prefix + uidInfo.unit);
              try {


                      StringBuilder recebeValores = new StringBuilder();

                      vals = recebeValores.append(uidInfo.key).toString();
                      unidade = recebeValores.append(uidInfo.key + prefix + uidInfo.unit).toString();


                      if (unidade.contains("m")) {
                          valor = Double.valueOf(vals).doubleValue();
                          valor = valor / 1000;
                          if (ini.size() == 0) {
                              vfim = valor;
                          } else {
                              vfim = valor - temp;
                              temp = valor;
                          }
                          //vfim = valor - temp;
                          //temp = valor;
                          ini.add(vfim);
                          //MiliJoule
                      } else {
                          valor = Double.valueOf(vals).doubleValue();
                          if (ini.size() == 0) {
                              vfim = valor;
                          } else {
                              vfim = valor - temp;
                              temp = valor;
                          }
                          ini.add(vfim);
                          //Joule
                      }

                      double somaConsumoTotal = 0;


                      if (condicaoSalvar == true) {
                          //if(ini.size() == (tempoTotal/ observacao_escolhida)) {
                          //int tam = ini.size();
                          //Toast.makeText(getContext(), "Salvando valores...", Toast.LENGTH_SHORT).show();
                          for (int i = 0; i < ini.size(); i++) {
                              somaConsumoTotal += ini.get(i);
                          }
                          //gravarArq.printf(Double.toString(ini.get(i))+"J\n");


                          //CASO A CONDIÇÃO DE SALVAR SEJA SELECIONADA, VAI CRIAR O ARQUI E SALVAR NELE, CASO ELE JÁ NÃO EXISTA.
                          FileWriter arq = new FileWriter("/storage/emulated/0/log_" + name + ".txt", true);
                          PrintWriter gravarArq = new PrintWriter(arq, true);
                          gravarArq.printf(Double.toString(somaConsumoTotal) + "J\n");


                          //SomaAplicativo.add(somaConsumoTotal);
                          if (condicao == 01) {
                              SomaAplicativo01.add(somaConsumoTotal);
                          } else
                              SomaAplicativo02.add(somaConsumoTotal);

                          contador_observacoes++;
                          //Toast.makeText(getContext(), "Feito!", Toast.LENGTH_SHORT).show();
                          ini.clear();
                      } else {
                          for (int i = 0; i < ini.size(); i++) {
                              somaConsumoTotal += ini.get(i);
                          }

                          if (condicao == 01) {
                              SomaAplicativo01.add(somaConsumoTotal);
                          } else {
                              SomaAplicativo02.add(somaConsumoTotal);
                          }

                          contador_observacoes++;
                      }


                      //gravarArq.printf(Double.toString(vfim)+"J");
                      //Toast.makeText(getContext(), "Valor: " + vfim, Toast.LENGTH_SHORT).show();
                      //gravarArq.printf(Double.toString(ini.get(ig))+"&");
                      //ig++;
                      //gravarArq.printf(vals);

                  }catch(IOException e){
                      e.printStackTrace();
                  }
              }else{
                  textView.setText("Nome: " + name + " /// Consumo: " + uidInfo.key + prefix + uidInfo.unit);
              }
    }
  }

  private class CounterServiceConnection implements ServiceConnection {
    public void onServiceConnected(ComponentName className,
                                   IBinder boundService ) {
      counterService = ICounterService.Stub.asInterface((IBinder)boundService);
      try {
        componentNames = counterService.getComponents();
        noUidMask = counterService.getNoUidMask();
        filterGroup.removeAllViews();
        for(int i = 0; i < componentNames.length; i++) {
            int ignMask = prefs.getInt("topIgnoreMask", 0);
            if ((noUidMask & 1 << i) != 0) continue;
            final TextView filterToggle = new TextView(PowerTop.this);
            final int index = i;
            filterToggle.setText(componentNames[i]);
            filterToggle.setGravity(Gravity.CENTER);
            filterToggle.setTextColor((ignMask & 1 << index) == 0 ?
                    0xFFFFFFFF : 0xFF888888);
            filterToggle.setBackgroundColor(
                    filterGroup.getChildCount() % 2 == 0 ? 0xFF444444 : 0xFF555555);
            filterToggle.setFocusable(true);
            filterToggle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int ignMask = prefs.getInt("topIgnoreMask", 0);
                    if ((ignMask & 1 << index) == 0) {
                        prefs.edit().putInt("topIgnoreMask", ignMask | 1 << index)
                                .commit();
                        filterToggle.setTextColor(0xFF888888);
                    } else {
                        prefs.edit().putInt("topIgnoreMask", ignMask & ~(1 << index))
                                .commit();
                        filterToggle.setTextColor(0xFFFFFFFF);
                    }
                }
            });
            filterGroup.addView(filterToggle,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.FILL_PARENT, 1f));

        }

      } catch(RemoteException e) {
        counterService = null;
      }
    }

    public void onServiceDisconnected(ComponentName className) {
      counterService = null;
      getApplicationContext().unbindService(conn);
      getApplicationContext().bindService(serviceIntent, conn, 0);
      Log.w(TAG, "Unexpectedly lost connection to service");
    }
  }
}

