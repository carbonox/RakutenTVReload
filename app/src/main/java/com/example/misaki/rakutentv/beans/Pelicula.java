package com.example.misaki.rakutentv.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pelicula {

    private int id_pelicula;
    private String titulo;
    private String genero;
    private String director;
    private String foto;
    private int duracion;
    private int puntuacion;
    private String estudio;
    private int precio;
    private String sinopsis;
    private int anio;

    private final static String ID_PELICULA = "id_pelicula";
    private final static String TITULO = "titulo";
    private final static String GENERO = "genero";
    private final static String DIRECTOR = "director";
    private final static String FOTO = "foto";
    private final static String DURACION = "duracion";
    private final static String PUNTUACION = "puntuacion";
    private final static String ESTUDIO = "estudio";
    private final static String PRECIO = "precio";
    private final static String SINOPSIS = "sinopsis";
    private final static String ANIO = "anio";

    public int getId_pelicula() { return id_pelicula;}

    public void setId_pelicula(int id_pelicula) {this.id_pelicula = id_pelicula;}

    public String getTitulo() {return titulo; }

    public void setTitulo(String titulo) {this.titulo = titulo; }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getDirector() { return director; }

    public void setDirector(String director) {this.director = director;}

    public String getFoto() { return foto; }

    public void setFoto(String foto) {this.foto = foto;}

    public int getDuracion() { return duracion; }

    public void setDuracion(int duracion) { this.duracion = duracion; }

    public int getPuntuacion() {return puntuacion;}

    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion;}

    public String getEstudio() {return estudio; }

    public void setEstudio(String estudio) { this.estudio = estudio; }

    public int getPrecio() {  return precio; }

    public void setPrecio(int precio) {  this.precio = precio; }

    public String getSinopsis() { return sinopsis; }

    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis;}

    public int getAnio() { return anio; }

    public void setAnio(int anio) { this.anio = anio; }


    public static ArrayList<Pelicula> getArrayListFromJSon(JSONArray datos) {
        ArrayList<Pelicula> lista = null;
        Pelicula pelicula = null;
        try {
            if (datos != null && datos.length() > 0) {
                lista = new ArrayList<Pelicula>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                pelicula = new Pelicula();
                pelicula.setId_pelicula(json_data.getInt(ID_PELICULA));
                pelicula.setTitulo(json_data.getString(TITULO));
                pelicula.setGenero(json_data.getString(GENERO));
                pelicula.setDirector(json_data.getString(DIRECTOR));
                pelicula.setFoto(json_data.getString(FOTO));
                pelicula.setDuracion(json_data.getInt(DURACION));
                pelicula.setPuntuacion(json_data.getInt(PUNTUACION));
                pelicula.setEstudio(json_data.getString(ESTUDIO));
                pelicula.setPrecio(json_data.getInt(PRECIO));
                pelicula.setSinopsis(json_data.getString(SINOPSIS));
                pelicula.setAnio(json_data.getInt(ANIO));
                lista.add(pelicula);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }

}
