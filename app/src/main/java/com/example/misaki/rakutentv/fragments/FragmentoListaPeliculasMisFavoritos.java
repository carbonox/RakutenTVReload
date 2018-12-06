package com.example.misaki.rakutentv.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoListaPeliculasMisFavoritos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoListaPeliculasMisFavoritos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoListaPeliculasMisFavoritos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerPeliculas;
    ArrayList<Pelicula> listaPeliculas;
    AdaptadorPeliculasRV adaptadorPeliculasRV;

    public FragmentoListaPeliculasMisFavoritos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaPersonajesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoListaPeliculasMisFavoritos newInstance(String param1, String param2) {
        FragmentoListaPeliculasMisFavoritos fragment = new FragmentoListaPeliculasMisFavoritos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_fragmento_lista_peliculas, container, false);

        listaPeliculas=new ArrayList<>();
        recyclerPeliculas= (RecyclerView) vista.findViewById(R.id.recyclerview_id);
        recyclerPeliculas.setLayoutManager(new LinearLayoutManager(getContext()));

//        llenarLista();

        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put("ACTION", "PELICULA.MISFAVORITOS");
        parametros.put("idUsuario", String.valueOf(RakutenTvData.getCliente().getId_usuario()));
        TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
        tarea.execute("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/Controller?");

        AdaptadorPeliculasRV adapter=new AdaptadorPeliculasRV(listaPeliculas);
        recyclerPeliculas.setAdapter(adapter);

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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {
        private ArrayList<Pelicula> listaPeliculas = null;
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlano(HashMap<String, String> parametros) {
            this.parametros = parametros;
        }

        /*
         * doInBackground().
         * Contendrá el código principal de nuestra tarea.
         * */
        @Override
        protected Boolean doInBackground(String... params) {
            // URL
            String url_select = params[0];
            try {
                Post post = new Post();

                JSONArray result = post.getServerDataPost(parametros, url_select);
                listaPeliculas = Pelicula.getArrayListFromJSon(result);
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                //messageUser = "Error al conectar con el servidor. ";
            }

            return true;
        }

        /*
         * onPostExecute().
         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
         * tras la finalización del método doInBackground().
         * */
        @Override
        protected void onPostExecute(Boolean resp) {
            try {
                if (resp && listaPeliculas != null && listaPeliculas.size() > 0) {
                    for (Pelicula pelicula : listaPeliculas) {
                        pelicula.setFoto("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/" + pelicula.getFoto());
                    }

                    adaptadorPeliculasRV =new AdaptadorPeliculasRV(listaPeliculas);
                    recyclerPeliculas.setAdapter(adaptadorPeliculasRV);

                } else {
//                    Toast.makeText(ListaPeliculasActivity.getInstance().getBaseContext(), "" +
//                            "Lista incorrecta. ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

}