package edu.umich.PowerTutor.ui;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.umich.PowerTutor.R;



public class Aplicacoes extends Activity {

    int MENU1, MENU2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacoes);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU1, 0, "Alterar Atributos do Teste");
        menu.add(0, MENU2, 0, "Menu2");
        return true;
    }

/*
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
    }*/

}
