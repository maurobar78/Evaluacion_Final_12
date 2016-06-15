package com.randbapps.evaluacionfinal12;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    //se establece el nombre del fichero que almacenara elementos de control
    private static final String FILECONTROL = "myAppData";
    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    //con este metodo se determinara la disponibilidad de conexion wifi y/o mobile para el funcionmiento del app
    protected  void onStart(){
        super.onStart();

        //se declaran las instancias para comprobar conexion por red celular
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //servicio de datos celular
        NetworkInfo networkInfoMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //conexion via wifi
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //se captura el valor cuando esta conectado
        boolean isMobileConnected = networkInfoMobile.isConnected();
        boolean isWifiConnected = networkInfoWifi.isConnected();

        //primero se valida si esta conectado via wifi
        if(!isWifiConnected){

            //si no esta conectado wifi, se valida conexion a datos celular
            if(!isMobileConnected){
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);

                //se crea AlertDialog con instrucciones pra que se conecte
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setView(dialogView);//se asocia la vista personalizada

                //se declara objeto para accion...
                TextView ir = (TextView)dialogView.findViewById(R.id.ir);
                ir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //se lanza panel de conexion wifi
                        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(i);
                    }
                });

                TextView cancel = (TextView)dialogView.findViewById(R.id.cerrar);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //sale del app
                        finish();

                    }
                });

                AlertDialog d = adb.create();//se crea el dialogo
                d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                d.show();//se muestra


            }else{

                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        validaIn();
                    };
                }, DURACION_SPLASH);

            }
        }else{
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    validaIn();
                };
            }, DURACION_SPLASH);
        }
    }

    //este metodo valida si el usuario ya se registro y/o inicio sesion
    public void validaIn(){

        //se crea el objeto para manipular la clase SharedPreferences
        SharedPreferences sp = getSharedPreferences(FILECONTROL, Context.MODE_PRIVATE);
        //se valida si el archivo de SharedPreferences esta vacio o no para lanzar actividad especifica
        if(sp.contains("Nombre")){
            //si el fichero existe y contiene informacion, se lanza Home
            Intent i = new Intent(this,Home.class);
            startActivity(i);
            //se carga animacion de transicion
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            finish();
        }else{
            //si el fichero no existe o no tiene informacion, se lanza Loggin
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            //se carga animacion de transicion
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            finish();
        }
    }
}
