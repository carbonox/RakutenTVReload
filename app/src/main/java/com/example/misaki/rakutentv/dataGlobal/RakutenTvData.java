package com.example.misaki.rakutentv.dataGlobal;






import com.example.misaki.rakutentv.beans.Cine;
import com.example.misaki.rakutentv.beans.Cliente;
import com.example.misaki.rakutentv.beans.Pelicula;

import java.util.ArrayList;


public class RakutenTvData {
	private static Cliente cliente;
	private static ArrayList<Pelicula> lstPelicula;
	private static ArrayList<Cine> lstCine;
	private static Pelicula peliculaSeleccionado;
	private static Cine cineSelecionado;
	private static String miIP = "192.168.20.109";
//	private static String miIP = "192.168.2.132";

	public static Cliente getCliente() {
		return cliente;
	}

	public static void setCliente(Cliente cliente) {
		RakutenTvData.cliente = cliente;
	}

	public static ArrayList<Pelicula> getLstPelicula() {
		return lstPelicula;
	}

	public static void setLstPelicula(ArrayList<Pelicula> lstPelicula) {
		RakutenTvData.lstPelicula = lstPelicula;
	}

	public static Pelicula getPeliculaSeleccionado() {
		return peliculaSeleccionado;
	}

	public static void setPeliculaSeleccionado(Pelicula peliculaSeleccionado) {
		RakutenTvData.peliculaSeleccionado = peliculaSeleccionado;
	}

	public static ArrayList<Cine> getLstCine() {
		return lstCine;
	}

	public static void setLstCine(ArrayList<Cine> lstCine) {
		RakutenTvData.lstCine = lstCine;
	}

	public static Cine getCineSelecionado() {
		return cineSelecionado;
	}

	public static void setCineSelecionado(Cine cineSelecionado) {
		RakutenTvData.cineSelecionado = cineSelecionado;
	}

	public static String getMiIP() { return miIP;}

	public static void setMiIP(String miIP) {
		RakutenTvData.miIP = miIP;
	}
}
