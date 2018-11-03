package vi_semestre.app_alumnos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }*/
    Button btnIngresar;
    EditText txtUsu,txtPas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsu=(EditText)findViewById(R.id.txtusuario);
        txtPas=(EditText)findViewById(R.id.txtpas);
        btnIngresar=(Button)findViewById(R.id.btningresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String resultado=enviarDatosGET(txtUsu.getText().toString(), txtPas.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r = obtDatosJSON(resultado);
                                //Toast.makeText(getApplicationContext(), r+"", Toast.LENGTH_LONG).show();

                                if (r > 0) {
                                    Intent i = new Intent(getApplicationContext(), Menu_Principal.class);
                                    i.putExtra("cod", txtUsu.getText().toString());
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Usuario o Pas Incorrectos", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                };
                tr.start();
            }
        });

    }


    public String enviarDatosGET(String usu, String pas){

        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;

        try{
            url=new URL("https://franconmvp.000webhostapp.com/services/login.php?usu="+usu+"&pas="+pas);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();

            resul=new StringBuilder();

            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }

        }catch (Exception e){}

        return resul.toString();

    }

    public int obtDatosJSON(String response){
        int res=0;
        try{
            JSONArray json=new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }catch(Exception e){}
        return res;
    }


}
