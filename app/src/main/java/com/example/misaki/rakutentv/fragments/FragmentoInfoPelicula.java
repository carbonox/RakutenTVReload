package com.example.misaki.rakutentv.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FragmentoInfoPelicula extends Fragment{


    private static FragmentoInfoPelicula fragmentoInfoPelicula;

    public static FragmentoInfoPelicula getInstance() {
        return fragmentoInfoPelicula;
    }

    private OnFragmentInteractionListener mListener;

    ImageView imageViewFoto;
    TextView txtTitulo, txtAnio, txtGenero, txtDirector, txtEstudio, txtScore, txtSinopsis;
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
//            pelicula.setDirector(savedInstanceState.getSerializable("PELI").toString());

        pelicula = RakutenTvData.getPeliculaSeleccionado();

        View vista=inflater.inflate(R.layout.fragment_fragmento_info_pelicula, container, false);

        imageViewFoto = (ImageView) vista.findViewById(R.id.idImagen);
        btnComprar = (Button) vista.findViewById(R.id.idBtnComprar);
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

}