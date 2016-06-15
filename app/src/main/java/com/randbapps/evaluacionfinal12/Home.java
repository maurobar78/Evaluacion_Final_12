package com.randbapps.evaluacionfinal12;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Home extends AppCompatActivity {

    //se delclaran objetospara tomar informacion de FB y luego almacenarla en SP
    private FacebookSdk fb;
    private SharedPreferences sp;
    private static final String FILECONTROL = "myAppData";//nombre del fichero que almacena los elementos de control

    //objeto para el control de ads
    private AdView adView;

    //objetos para la interface
    private TextView nombre, apellido, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //se inicializa el SDK de facebook....
        fb.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_home);

        //se habilita el manejo del actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        nombre = (TextView)findViewById(R.id.userName);
        apellido = (TextView)findViewById(R.id.userLastName);
        correo = (TextView)findViewById(R.id.userMail);

        //se le indica al objeto el archivo que debe leer SP
        sp = getSharedPreferences(FILECONTROL, Context.MODE_PRIVATE);

        //se traen los datos del archivo
        nombre.setText(sp.getString("Nombre", ""));
        apellido.setText(sp.getString("Apellido", ""));
        correo.setText(sp.getString("Email", ""));
        adView = (AdView)findViewById(R.id.bannerAd);


        //objeto para solicitar el envio de publicidad
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);
    }

    //metodos pàra los diferentes estados en los modulos de publicidad
    @Override
    protected void onPause() {

        if (adView != null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (adView != null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            //se llama el menu de preferencias para el usuario
            case R.id.close:
                //cierra sesion de FB y elimna el access token
                LoginManager.getInstance().logOut();

                //se le indica al objeto el archivo que debe leer
                sp = getSharedPreferences(FILECONTROL, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                //se eliminan los datos del fichero, lo cual cerrara sesion
                editor.clear();
                //se ejecuta la instruccion
                editor.commit();

                //se redirige a la pantalla de inicio de sesion
                Intent intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
