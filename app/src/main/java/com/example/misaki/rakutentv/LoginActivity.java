package com.example.misaki.rakutentv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.misaki.rakutentv.beans.Cliente;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends Activity{
    private TextInputLayout layoutEdtEmail;
    private EditText edtEmail;
    private TextInputLayout layoutEdtPass;
    private EditText edtPass;
    private Button btnLogin;
    private Button btnRegistro;
    private Button btnInvitado;

    /*Singleton*/
    private static LoginActivity loginActivity;

    public static LoginActivity getInstance() {
        return loginActivity;
    }

    /*Fin Singleton*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);


        btnLogin = (Button) findViewById(R.id.btnEnviar);
        btnInvitado = (Button) findViewById(R.id.btnEnviarInvitado);
        btnRegistro = (Button) findViewById(R.id.btnEnviarRegistro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*ServiceLogin.accionLogin(edtEmail.getText().toString(), edtPass.getText().toString());*/
                HashMap<String, String> parametros = new HashMap<String, String>();
                parametros.put("ACTION", "USUARIO.LOGIN");
                parametros.put("formLogNick", edtEmail.getText().toString());
                parametros.put("formLogPass", edtPass.getText().toString());

                TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                tarea.execute("http://"+RakutenTvData.getMiIP()+":8080/RakutenTV/Controller");
            }
        });
        btnInvitado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RakutenTvData.setCliente(null);
                Intent intent = new Intent(btnInvitado.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onPause(){
        super.onPause();
        SharedPreferences datosLogin = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor she = datosLogin.edit();

        she.putString("NICK", edtEmail.getText().toString());
        she.putString("PASS", edtPass.getText().toString());
        she.apply();
    }

    public void onResume(){
        super.onResume();
        SharedPreferences datosLogin = PreferenceManager.getDefaultSharedPreferences(this);

        edtEmail.setText(datosLogin.getString("NICK", ""));
        edtPass.setText(datosLogin.getString("PASS", ""));
    }

    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {

//        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        private HashMap<String, String> parametros = null;
        private ArrayList<Cliente> listaClientes = null;


        public TareaSegundoPlano(HashMap<String, String> parametros) {
            this.parametros = parametros;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            String url_select = params[0];
            try {
                Post post = new Post();

                JSONArray result = post.getServerDataPost(parametros, url_select);
                listaClientes = Cliente.getArrayListFromJSon(result);
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                //messageUser = "Error al conectar con el servidor. ";
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
//            progressDialog.setProgress(progreso);
        }

        /*
         * onPostExecute().
         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
         * tras la finalización del método doInBackground().
         * */
        @Override
        protected void onPostExecute(Boolean resp) {
            try {
                if (listaClientes != null && listaClientes.size() > 0) {
                    Cliente cliente = listaClientes.get(0);
                    if (cliente.getId_usuario() > 0 && cliente.getNick().equals("") && cliente.getEmail().equals("")) {
                        RakutenTvData.setCliente(cliente);
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Usuario correcto. ",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(btnLogin.getContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(),"Rellena los campos. ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.getInstance().getBaseContext(),"Usuario incorrecto. ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }


}


