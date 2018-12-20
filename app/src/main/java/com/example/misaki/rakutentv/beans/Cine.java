package com.example.misaki.rakutentv.beans;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cine {
    private int id_cine;
    private String nombre;
    private String descripcion;
    private String foto;
    private int telefono;

    private final static String ID_CINE= "id_cine";
    private final static String NOMBRE= "nombre";
    private final static String DESCRIPCION= "descripcion";
    private final static String FOTO= "foto";
    private final static String TELEFONO= "telefono";


    public int getId_cine() {
        return id_cine;
    }

    public void setId_cine(int id_cine) {
        this.id_cine = id_cine;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }



    public static ArrayList<Cine> getArrayListFromJSon(JSONArray datos){
        ArrayList<Cine> lista = null;
        Cine cine = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Cine>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                cine = new Cine();
                cine.setId_cine(json_data.getInt(ID_CINE));
                cine.setNombre(json_data.getString(NOMBRE));
                cine.setDescripcion(json_data.getString(DESCRIPCION));
                cine.setFoto(json_data.getString(FOTO));
                cine.setTelefono(json_data.getInt(TELEFONO));
                lista.add(cine);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }

}
