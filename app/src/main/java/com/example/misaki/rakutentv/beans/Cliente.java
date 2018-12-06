package com.example.misaki.rakutentv.beans;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cliente {
	private int id_usuario;
	private String nick;
	private String pass;
	private String email;
	private int rol;

	private final static String ID_USUARIO= "id_usuario";
	private final static String NICK= "nick";
	private final static String PASS= "pass";
	private final static String EMAIL= "email";
	private final static String ROL= "rol";


	public int getId_usuario() {return id_usuario;}

	public void setId_usuario(int id_usuario) {this.id_usuario = id_usuario; }

	public String getNick() { return nick; }

	public void setNick(String nick) { this.nick = nick;}

	public String getPass() { return pass; }

	public void setPass(String pass) { this.pass = pass; }

	public String getEmail() {  return email; }

	public void setEmail(String email) {  this.email = email; }

	public int getRol() { return rol; }

	public void setRol(int rol) { this.rol = rol; }

	public static ArrayList<Cliente> getArrayListFromJSon(JSONArray datos){
		ArrayList<Cliente> lista = null;
		Cliente cliente = null;
		try {
			if(datos!=null && datos.length() > 0 ){
				lista = new ArrayList<Cliente>();
			}
			for (int i = 0; i < datos.length(); i++) {
				JSONObject json_data = datos.getJSONObject(i);
				cliente = new Cliente();
					cliente.setId_usuario(json_data.getInt(ID_USUARIO));
					cliente.setNick(json_data.getString(NICK));
					cliente.setPass(json_data.getString(PASS));
					cliente.setEmail(json_data.getString(EMAIL));
					cliente.setRol(json_data.getInt(ROL));
				lista.add(cliente);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lista;

	}
	
}
