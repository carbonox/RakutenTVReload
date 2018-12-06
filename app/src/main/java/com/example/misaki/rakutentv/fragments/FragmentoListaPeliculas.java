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
import android.widget.Toast;


import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentoListaPeliculas extends Fragment{


    private static FragmentoListaPeliculas fragmentoListaPeliculas;

    public static FragmentoListaPeliculas getInstance() {
        return fragmentoListaPeliculas;
    }

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerPeliculas;
    ArrayList<Pelicula> listaPeliculas;
    AdaptadorPeliculasRV adaptadorPeliculasRV;



    public FragmentoListaPeliculas() {
        // Required empty public constructor
    }


    public static FragmentoListaPeliculas newInstance(String param1, String param2) {
        FragmentoListaPeliculas fragment = new FragmentoListaPeliculas();
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
        View vista=inflater.inflate(R.layout.fragment_fragmento_lista_peliculas, container, false);

        listaPeliculas=new ArrayList<>();
        recyclerPeliculas= (RecyclerView) vista.findViewById(R.id.recyclerview_id);
        recyclerPeliculas.setLayoutManager(new GridLayoutManager(getContext(),3));


        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put("ACTION", "PELICULA.FIND_ALL");
        TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
        tarea.execute("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/Controller?");



        AdaptadorPeliculasRV adapter=new AdaptadorPeliculasRV(listaPeliculas);
        recyclerPeliculas.setAdapter(adapter);

//        adapter.setOnItemClickListener(new AdaptadorPeliculasRV.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                final TextView tvHelp = (TextView) findViewById(R.id.infoAnio);
//                Toast.makeText(FragmentoListaPeliculasMisCompras.getInstance().getContext(), "Card View "+ listaPeliculas.get(position) ,Toast.LENGTH_SHORT).show();
//
//            }
//        });
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
                    Toast.makeText(getActivity(), "Lista incorrecta. ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

}