package com.example.misaki.rakutentv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Pattern;


public class RegistroActivity extends Activity{
    private TextInputLayout layoutEdtEmail;
    private EditText edtNickReg;
    private EditText edtEmailReg;
    private TextInputLayout layoutEdtPass;
    private EditText edtPassReg, edtPass2Reg;
    private Button btnEnviarLogin;
    private Button btnRegistro;

    /*Singleton*/
    private static RegistroActivity  registroActivity;

    public static RegistroActivity getInstance() {
        return registroActivity;
    }

    /*Fin Singleton*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registroActivity = this;

        edtNickReg = (EditText) findViewById(R.id.edtNickReg);
        edtEmailReg = (EditText) findViewById(R.id.edtEmailReg);
        edtPassReg = (EditText) findViewById(R.id.edtPassReg);
        edtPass2Reg = (EditText) findViewById(R.id.edtPassReg2);


        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnEnviarLogin= (Button) findViewById(R.id.btnEnviarLogin);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*ServiceLogin.accionLogin(edtEmail.getText().toString(), edtPass.getText().toString());*/

                if(!edtPassReg.getText().toString().equals("")
                        || !edtPassReg.getText().toString().equals("")
                        || !edtEmailReg.getText().toString().equals("")
                        || !edtNickReg.getText().toString().equals("") ){

                    if(edtPassReg.getText().toString().equals(edtPass2Reg.getText().toString())){
                        if(validarEmail(edtEmailReg.getText().toString())){
                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("ACTION", "USUARIO.REGISTER");
                            parametros.put("formRegNick", edtNickReg.getText().toString());
                            parametros.put("formRegPass", edtPassReg.getText().toString());
                            parametros.put("formRegEmail", edtEmailReg.getText().toString());

                            TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                            tarea.execute("http://"+RakutenTvData.getMiIP()+":8080/RakutenTV/Controller");
                        }else{
                            Toast.makeText(RegistroActivity.getInstance().getBaseContext(),"El email no es valido", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(RegistroActivity.getInstance().getBaseContext(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistroActivity.getInstance().getBaseContext(),"Algún campo esta vacio", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnEnviarLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RakutenTvData.setCliente(null);
                Intent intent = new Intent(btnEnviarLogin.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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
                    if (cliente.getId_usuario() > 0 && !cliente.getNick().equals("") && !cliente.getEmail().equals("")) {
                        RakutenTvData.setCliente(cliente);
                        Toast.makeText(RegistroActivity.getInstance().getBaseContext(), "Usuario correcto. ",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(btnRegistro.getContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(RegistroActivity.getInstance().getBaseContext(),"Rellena los campos. ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.getInstance().getBaseContext(),"Usuario incorrecto. ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }



}

