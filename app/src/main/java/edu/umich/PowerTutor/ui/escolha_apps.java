package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.umich.PowerTutor.*;
import edu.umich.PowerTutor.util.Counter;
/*
public class escolha_apps extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_apps);
    }
}*/


public class escolha_apps extends Activity{

    int MENU1 = 0, MENU2 = 1, MENU3 = 2;

    public static final CharSequence[] TEMPOS = { "Testes","1 MINUTO", "2 MINUTOS", "3 MINUTOS", "5 MINUTOS", "8 MINUTOS", "15 MINUTOS"};

    public static final CharSequence[] INTERVALOS = {"Testes", "1", "3", "5", "7", "8", "10", "15", "30", "40", "50", "60"};


    int tempo_valores[] = {10000, 60000, 120000, 180000, 300000, 480000, 900000};
    int obsevacoes_valores[] = {2, 1, 3, 5, 7, 8, 10, 15, 30, 40, 50, 60};

    int tempo_escolhido;
    int observacao_escolhida;

    private ListView lista_aplicativos;
    private Button voltar;
    private Button confirmar_apps;
    private CheckBox salvarDados;

    int contador_apps = 0;

    boolean condicaoSalvar = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_apps);
        //addPreferencesFromResource(R.xml.);

        confirmar_apps = (Button) findViewById(R.id.btn_confirmar);
        voltar = (Button) findViewById(R.id.btn_voltar_logs_apps);
        salvarDados = (CheckBox) findViewById(R.id.chk_SalvarDadosEmLOG_TelaSelecaoDeAPPS);

        salvarDados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    condicaoSalvar = true;
                }else{
                    condicaoSalvar = false;
                }
            }
        });




        final ArrayList<String> opcoes = new ArrayList<String>();
        final ArrayList<String> salvaPacotes = new ArrayList<String>();
        PackageManager packageManager = getPackageManager();
        //LinearLayout linear = (LinearLayout) findViewById(android.R.layout.linear);
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo ap:list){
            String nome = ap.packageName;
            try {
                ApplicationInfo app = getPackageManager().getApplicationInfo(nome, 0);
                CharSequence nome1 = getPackageManager().getApplicationLabel(app);
                opcoes.add((String) nome1);
                salvaPacotes.add(nome);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        

        //ApplicationInfo app = getPackageManager().getApplicationInfo(itens.get(0), 0);
        //CharSequence nome1 = getPackageManager().getApplicationLabel(app);
        

        //alterar para multipla escolha
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opcoes);

        lista_aplicativos = (ListView) findViewById(R.id.listView_Aplicativos_Instalados);

        lista_aplicativos.setAdapter(adapter);

        final ArrayList<String> itens = new ArrayList<String>();
        final int[] cont = {0};
        final ArrayList<String> pacotes = new ArrayList<String>();
        lista_aplicativos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                contador_apps++;

                itens.add (opcoes.get(position));
                pacotes.add(salvaPacotes.get(position));
                cont[0]++;
                Toast.makeText(escolha_apps.this, "Voce selecionou: " + opcoes.get(position) + " ---Contador: " + cont[0], Toast.LENGTH_SHORT).show();


                if(cont[0] == 2){//CONDIÇÃO PARA LIMITAR ATÉ DOIS APLICATIVOSSELECIONADOS, NO MÁXIMO
                    //Toast.makeText(escolha_apps.this, "Nao é possivel selecionar mais itens. Somente 2!", Toast.LENGTH_SHORT).show();
                    lista_aplicativos.setClickable(false);
                    lista_aplicativos.setEnabled(false);
                }
            }
        });

        confirmar_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String aplicativo = null;
                    Intent it = new Intent(escolha_apps.this, PowerTop.class);
                    //Toast.makeText(escolha_apps.this, "Pressionou o botão!", Toast.LENGTH_LONG).show();

                    it.putExtra("QUANTIDADE_APPS", contador_apps);
                    it.putExtra("VALOR", itens);
                    it.putExtra("VALOR2", pacotes);
                    it.putExtra("OBSERVACAO", observacao_escolhida);
                    it.putExtra("TEMPO", tempo_escolhido);
                    it.putExtra("CONDICAO_SALVAR", condicaoSalvar);
                    finish();
                    startActivity(it);


            }

        });
            /*
        lista_aplicativos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aplicativo = null;
                Intent it = new Intent(escolha_apps.this, PowerTop.class);

                it.putExtra("VALOR", opcoes.get(position));
                startActivity(it);

            }
        });*/


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU1, 0, "Definir Execução");
        menu.add(0, MENU2, 0, "Preferências Adicionais");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 1!", Toast.LENGTH_LONG).show();
                showDialog(0);
                return true;
            case 1:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 2", Toast.LENGTH_LONG).show();
                showDialog(2);
                return true;

            case 2:
                Toast.makeText(escolha_apps.this, "Selecionado Opção 3", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case 0:
                builder.setTitle("Tempo Total de Execução para Cada APP");
                //builder.setItems(TEMPOS,
                builder.setItems(TEMPOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        tempo_escolhido = tempo_valores[item];
                        Toast.makeText(escolha_apps.this, "Tempo escolhido: "+tempo_escolhido, Toast.LENGTH_SHORT).show();
                        showDialog(01);

                    }
                });
                return builder.create();

            case 1:
                builder.setTitle("Quantidade de Observações");
                builder.setItems(INTERVALOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //prefs.edit().putInt("topWindowType", item).commit();
                        observacao_escolhida = obsevacoes_valores[item];
                        Toast.makeText(escolha_apps.this, "Intervalo Escolhido: "+observacao_escolhida, Toast.LENGTH_SHORT).show();
                    }
                });
                return builder.create();
            case 2:
                Intent it = new Intent (escolha_apps.this, preferencias_execucao_teste.class);
                startActivity(it);
        }
        return null;
    }
}
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
*/
