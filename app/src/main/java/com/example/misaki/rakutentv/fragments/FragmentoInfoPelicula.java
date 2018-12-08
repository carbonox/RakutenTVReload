package com.example.misaki.rakutentv.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentoInfoPelicula extends Fragment{


    private static FragmentoInfoPelicula fragmentoInfoPelicula;

    public static FragmentoInfoPelicula getInstance() {
        return fragmentoInfoPelicula;
    }

    private OnFragmentInteractionListener mListener;

    ImageView imageViewFoto;
    TextView txtTitulo, txtAnio, txtGenero, txtDirector, txtEstudio, txtScore;
    ScrollView scrollViewSinopsis;
    Switch switchFav;
    Button btnComprar;
    Pelicula pelicula;



    public FragmentoInfoPelicula() {
        // Required empty public constructor
    }


    public static FragmentoInfoPelicula newInstance() {
        FragmentoInfoPelicula fragment = new FragmentoInfoPelicula();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            pelicula.setDirector(savedInstanceState.getSerializable("PELI").toString());


        View vista=inflater.inflate(R.layout.fragment_fragmento_info_pelicula, container, false);

        imageViewFoto = (ImageView) vista.findViewById(R.id.idImagen);
        scrollViewSinopsis = (ScrollView) vista.findViewById(R.id.infoSinopsis);
        btnComprar = (Button) vista.findViewById(R.id.idBtnComprar);
        switchFav = (Switch) vista.findViewById(R.id.infoFav);

        txtAnio = (TextView) vista.findViewById(R.id.infoAnio);
        txtDirector = (TextView) vista.findViewById(R.id.infoDirector);
        txtEstudio = (TextView) vista.findViewById(R.id.infoEstudio);
        txtGenero = (TextView) vista.findViewById(R.id.infoGeneros);
        txtScore = (TextView) vista.findViewById(R.id.infoScore);
        txtTitulo = (TextView) vista.findViewById(R.id.infoTitulo);

        imageViewFoto.setImageURI(Uri.parse(pelicula.getFoto()));
        txtTitulo.setText(pelicula.getTitulo());
        txtScore.setText(pelicula.getPuntuacion());
        txtGenero.setText(pelicula.getGenero());
        txtEstudio.setText(pelicula.getEstudio());
        txtDirector.setText(pelicula.getDirector());
        txtAnio.setText(pelicula.getAnio());
        return vista;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " añadir OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}