package com.example.misaki.rakutentv.adaptadores;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.misaki.rakutentv.R;
import com.example.misaki.rakutentv.beans.Pelicula;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class AdaptadorPeliculasRV extends RecyclerView.Adapter<AdaptadorPeliculasRV.PeliculaViewHolder> {

    private ArrayList<Pelicula> listaPeliculas;

    public AdaptadorPeliculasRV(ArrayList<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }


    @Override
    public PeliculaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.cardview_item_film, parent, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeliculaViewHolder holder, final int position) {

        Pelicula pelicula = listaPeliculas.get(position);
        holder.txtNombre.setText(listaPeliculas.get(position).getTitulo());
        if (holder.foto != null) {
            new AdaptadorPeliculasRV.BitmapWorkerTask(holder.foto).execute(pelicula.getFoto());
        }
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, Book_Activity.class);
//
//                // passing data to the book activity
//                intent.putExtra("Title", mData.get(position).getTitle());
//                intent.putExtra("Description", mData.get(position).getDescription());
//                intent.putExtra("Thumbnail", mData.get(position).getThumbnail());
//                // start the activity
//                mContext.startActivity(intent);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return listaPeliculas.size();
    }

    public class PeliculaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtNombre;
        ImageView foto;

        public PeliculaViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            txtNombre = (TextView) itemView.findViewById(R.id.film_title_id);
            foto = (ImageView) itemView.findViewById(R.id.film_img_id);
        }
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

