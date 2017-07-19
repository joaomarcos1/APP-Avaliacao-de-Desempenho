package edu.umich.PowerTutor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.umich.PowerTutor.R;

public class preferencias_execucao_teste extends Activity {


    private Button voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_execucao_teste);

        voltar = (Button) findViewById(R.id.btn_voltar_preferencias_execucao_Teste);


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
