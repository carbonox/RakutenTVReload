package com.example.misaki.rakutentv.dataGlobal;






import com.example.misaki.rakutentv.beans.Cliente;
import com.example.misaki.rakutentv.beans.Pelicula;

import java.util.ArrayList;


public class RakutenTvData {
	private static Cliente cliente;
	private static ArrayList<Pelicula> lstPelicula;
	private static Pelicula peliculaSeleccionado;
	private static String miIP = "192.168.2.132";

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

	public static String getMiIP() { return miIP;}

	public static void setMiIP(String miIP) {
		RakutenTvData.miIP = miIP;
	}
}
