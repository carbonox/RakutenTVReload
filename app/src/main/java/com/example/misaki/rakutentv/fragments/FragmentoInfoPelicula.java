package com.example.misaki.rakutentv.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misaki.rakutentv.LoginActivity;
import com.example.misaki.rakutentv.MainActivity;
import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Cliente;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.tools.JsonHelper;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentoInfoPelicula extends Fragment {


    private static FragmentoInfoPelicula fragmentoInfoPelicula;

    public static FragmentoInfoPelicula getInstance() {
        return fragmentoInfoPelicula;
    }

    private OnFragmentInteractionListener mListener;

    ImageView imageViewFoto;
    TextView txtTitulo, txtAnio, txtGenero, txtDirector, txtEstudio, txtScore, txtSinopsis;
    Switch switchFav;
    Button btnComprar, btnFavorito;
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
//            pelicula.setDirector(savedInstanceState.getSerializable("PELI").toString());

        pelicula = RakutenTvData.getPeliculaSeleccionado();

        View vista = inflater.inflate(R.layout.fragment_fragmento_info_pelicula, container, false);

        imageViewFoto = (ImageView) vista.findViewById(R.id.idImagen);
        btnComprar = (Button) vista.findViewById(R.id.idBtnComprar);
        btnFavorito = (Button) vista.findViewById(R.id.idBtnFavorito);
//        switchFav = (Switch) vista.findViewById(R.id.infoFav);

        txtAnio = (TextView) vista.findViewById(R.id.infoAnio);
        txtDirector = (TextView) vista.findViewById(R.id.infoDirector);
        txtEstudio = (TextView) vista.findViewById(R.id.infoEstudio);
        txtGenero = (TextView) vista.findViewById(R.id.infoGeneros);
        txtScore = (TextView) vista.findViewById(R.id.infoScore);
        txtTitulo = (TextView) vista.findViewById(R.id.infoTitulo);
        txtSinopsis = (TextView) vista.findViewById(R.id.infoSinopsis);

        imageViewFoto.setImageURI(Uri.parse(pelicula.getFoto()));
        new FragmentoInfoPelicula.BitmapWorkerTask(imageViewFoto).execute(pelicula.getFoto());

        txtTitulo.setText(pelicula.getTitulo());
        txtScore.setText(String.valueOf(pelicula.getPuntuacion()));
        txtGenero.setText(pelicula.getGenero().replace(" ", "\n"));
        txtEstudio.setText(pelicula.getEstudio());
        txtDirector.setText(pelicula.getDirector());
        txtSinopsis.setText(pelicula.getSinopsis());
        txtAnio.setText(String.valueOf(pelicula.getAnio()));

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RakutenTvData.getCliente() != null) {
                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put("ACTION", "PELICULA.COMPRAR");
                    parametros.put("inputComprarUsuario", String.valueOf(RakutenTvData.getCliente().getId_usuario()));
                    parametros.put("inputComprarPelicula", String.valueOf(pelicula.getId_pelicula()));

                    TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                    tarea.execute("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/Controller");
                } else {
                    Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Logeate primero", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RakutenTvData.getCliente() != null) {
                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put("ACTION", "PELICULA.DELETE_ADD_FAVORITOS");
                    parametros.put("inputFavoritoUsuario", String.valueOf(RakutenTvData.getCliente().getId_usuario()));
                    parametros.put("inputFavoritoPelicula", String.valueOf(pelicula.getId_pelicula()));

                    TareaSegundoPlano2 tarea = new TareaSegundoPlano2(parametros);
                    tarea.execute("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/Controller");
                } else {
                    Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Logeate primero", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String imageUrl;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            return loadImage(imageUrl);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        private Bitmap loadImage(String URL) {
            Bitmap bitmap = null;
            InputStream in = null;
            try {
                in = openHttpConnection(URL);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
            }
            return bitmap;
        }

        private InputStream openHttpConnection(String strURL) throws IOException {
            InputStream inputStream = null;
            URL url = new URL(strURL);
            URLConnection conn = url.openConnection();

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }
            } catch (Exception ex) {
            }
            return inputStream;
        }
    }

    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {

        //        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        private HashMap<String, String> parametros = null;
        private int aniadido = -1;


        public TareaSegundoPlano(HashMap<String, String> parametros) {
            this.parametros = parametros;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            String url_select = params[0];
            try {
                Post post = new Post();

                JsonHelper jsonHelper = new JsonHelper();

                JSONArray result = post.getServerDataPost(parametros, url_select);

                List list = jsonHelper.toList(result);

                Map map = (Map) list.get(0);

                try {
                    aniadido = (Integer) map.get("success");
                } catch (Exception e) {
                    aniadido = -2000;
                }

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                //messageUser = "Error al conectar con el servidor. ";
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
//            progressDialog.setProgress(progreso);
        }

        /*
         * onPostExecute().
         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
         * tras la finalización del método doInBackground().
         * */
        @Override
        protected void onPostExecute(Boolean resp) {
            try {
                if (aniadido != -2000) {
                    if (aniadido != 1) {
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Esta pelicula ya la tienes comprada. ", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Exito! Has comprado la pelicula. ", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Logeate primero", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    class TareaSegundoPlano2 extends AsyncTask<String, Integer, Boolean> {

        //        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        private HashMap<String, String> parametros = null;
        private int aniadido = -1;


        public TareaSegundoPlano2(HashMap<String, String> parametros) {
            this.parametros = parametros;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            String url_select = params[0];
            try {
                Post post = new Post();

                JsonHelper jsonHelper = new JsonHelper();

                JSONArray result = post.getServerDataPost(parametros, url_select);

                List list = jsonHelper.toList(result);

                Map map = (Map) list.get(0);

                try {
                    aniadido = (Integer) map.get("success");
                } catch (Exception e) {
                    aniadido = -2000;
                }

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                //messageUser = "Error al conectar con el servidor. ";
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
//            progressDialog.setProgress(progreso);
        }

        /*
         * onPostExecute().
         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
         * tras la finalización del método doInBackground().
         * */
        @Override
        protected void onPostExecute(Boolean resp) {
            try {
                if (aniadido != -2000) {
                    if (aniadido != 1) {
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Exito! Eliminado de favorito", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Exito! Añadido a favorito", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(LoginActivity.getInstance().getBaseContext(), "Logeate primero", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }


}