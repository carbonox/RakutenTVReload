package com.example.misaki.rakutentv.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Compra{

    private int id_usuario;
    private int id_pelicula;

    private final static String ID_USUARIO= "id_usuario";
    private final static String ID_PELICULA = "id_pelicula";


    public int getId_usuario() {return id_usuario; }

    public void setId_usuario(int id_usuario) {this.id_usuario = id_usuario; }

    public int getId_pelicula() { return id_pelicula; }

    public void setId_pelicula(int id_pelicula) { this.id_pelicula = id_pelicula; }

    public static ArrayList<Compra> getArrayListFromJSon(JSONArray datos) {
        ArrayList<Compra> lista = null;
        Compra compra = null;
        try {
            if (datos != null && datos.length() > 0) {
                lista = new ArrayList<Compra>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                compra= new Compra();
                compra.setId_usuario(json_data.getInt(ID_USUARIO));
                compra.setId_pelicula(json_data.getInt(ID_PELICULA));
                lista.add(compra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }

}
