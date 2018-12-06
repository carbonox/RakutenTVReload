package com.example.misaki.rakutentv.threads;

import android.os.Handler;
import android.widget.Toast;

import com.rakutentv.LoginActivity;
import com.rakutentv.beans.Cliente;
import com.rakutentv.dataGlobal.RakutenTvData;
import com.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceLogin {
	private final static Handler manejador = new Handler();
	private static String messageUser = null;
	private static ArrayList<Cliente> listaClientes = null;
	private static Thread threadLogin = null;
	
	public static void accionLogin(final String user, final String pass){
		threadLogin = new Thread(){
			public void run(){
				JSONArray datos = null;
				HashMap<String, String> parametros = new HashMap<String, String>();
				parametros.put("formLogNick",user );
				parametros.put("formLogPass",pass );
				// Llamada a Servidor Web PHP
				try {
					Post post = new Post();
					datos = post.getServerDataPost(parametros,"http://192.168.2.187:8080/RakutenTV/Controller");
					listaClientes = Cliente.getArrayListFromJSon(datos);
				} catch (Exception e) {
					messageUser = "Error al conectar con el servidor. ";
				}
				manejador.post(proceso);
			}
		};
		threadLogin.start();
	}
	
	private final static Runnable proceso = new Runnable(){
		public void run() {
			try{
				if (listaClientes != null && listaClientes.size() > 0) {
					Cliente cliente = listaClientes.get(0);
					if (cliente.getId_usuario() > 0) {
						RakutenTvData.setCliente(cliente);
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),"" +
								"Usuario correcto. ", Toast.LENGTH_SHORT).show();
					}
				} else {
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),"" +
								"Usuario incorrecto. ", Toast.LENGTH_SHORT).show();
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),
								messageUser, Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){}
		}
	};
}