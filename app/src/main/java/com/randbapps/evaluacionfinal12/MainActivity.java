package com.randbapps.evaluacionfinal12;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //objeto que controlara los eventos para facebook
    public CallbackManager callbackManager;
    //Objeto que controlara el boton de inicio de facebook
    private LoginButton loginButton;

    //strings que almacenaran las cadenas correspondiente al perfil del usuario
    private String fbUserName = "";
    private  String fbUserLastName = "";
    private String fbUserEmail = "";
    private String fbUserId = "";
    private SharedPreferences sp;
    private static final String FILECONTROL = "myAppData";

    //objeto para el control de ads
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se inicializa el sdk de facebook
        facebookSDKInitialize();

        setContentView(R.layout.activity_main);


        //se asocian los obejtos a la vista
        loginButton = (LoginButton) findViewById(R.id.fbLogin);
        //incluye los permisos inicales de lo que podra leer en esta conexion
        loginButton.setReadPermissions("public_profile, email");
        getLoginDetails(loginButton);

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

    //inicializa el uso del SDK
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }

    //metodo que traera informacion del usuario desde FB
    protected void getLoginDetails(LoginButton login_button) {

        //se registra el callback para el boton de fb
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {

                //esta funcion permitira recuperar informacion del perfil de usuario en fb para luego ser alamcenada en
                //SP del app
                GraphRequest request = GraphRequest.newMeRequest(login_result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());
                        setProfileToView(object);//metodo que leera el JSONObject
                    }
                });
                Bundle parameters = new Bundle();
                //se especifica la informacion a extraer del perfil de usuario en FB
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //se envia mensaje de cancelacion
                Toast.makeText(getApplicationContext(), getString(R.string.fb_cancel), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //se envia mensaje de error
                Toast.makeText(getApplicationContext(), getString(R.string.fb_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //metodo invocado en getLoginDetails...
    //es donde se corre el proceso de extraer y almacenar la informacion de FB
    private void setProfileToView(JSONObject jsonObject) {

        //se inicializa objeto para almacenar informacion en los SP
        sp = getApplicationContext().getSharedPreferences(FILECONTROL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        try {
            //se lee la informacion de JSONObject
            fbUserName = jsonObject.getString("first_name");
            fbUserLastName = jsonObject.getString("last_name");
            fbUserEmail = jsonObject.getString("email");
            fbUserId = jsonObject.getString("id");

            //se asignan los valores a SharedPreferences
            editor.putString("Fb_ID", fbUserId);
            editor.putString("Nombre", fbUserName);
            editor.putString("Apellido", fbUserLastName);
            editor.putString("Email", fbUserEmail);
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //se lanza la actividad correspondiete
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();

    }

    //llamado a actividad nativa de FB
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //recibe la informacion de callback
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //se almacena informacion de log
        Log.e("data",data.toString());
    }




}
