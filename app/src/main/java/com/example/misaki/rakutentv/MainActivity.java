package com.example.misaki.rakutentv;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;



import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculas;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasMisCompras;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasMisFavoritos;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasPopulares;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasRanking;
import com.example.misaki.rakutentv.tools.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentoListaPeliculas.OnFragmentInteractionListener {

    private ArrayList<Pelicula> m_peliculas = new ArrayList<Pelicula>();
    private AdaptadorPeliculasRV adaptadorPeliculas;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();


        if (id == R.id.nav_inicio) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculas()).commit();
        } else if (id == R.id.nav_polulares) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasPopulares()).commit();
        } else if (id == R.id.nav_ranking) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasRanking()).commit();
        } else if (id == R.id.nav_ranking) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasRanking()).commit();
        } else if (id == R.id.nav_fabs) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasMisFavoritos()).commit();
        } else if (id == R.id.nav_compras) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasMisCompras()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {
//        private ArrayList<Pelicula> listaPeliculas = null;
//        private HashMap<String, String> parametros = null;
//
//
//        public TareaSegundoPlano(HashMap<String, String> parametros) {
//            this.parametros = parametros;
//        }
//
//        /*
//         * doInBackground().
//         * Contendrá el código principal de nuestra tarea.
//         * */
//        @Override
//        protected Boolean doInBackground(String... params) {
//            // URL
//            String url_select = params[0];
//            try {
//                Post post = new Post();
//
//                JSONArray result = post.getServerDataPost(parametros, url_select);
//                listaPeliculas = Pelicula.getArrayListFromJSon(result);
//            } catch (Exception e) {
//                Log.e("log_tag", "Error in http connection " + e.toString());
//                //messageUser = "Error al conectar con el servidor. ";
//            }
//
//            return true;
//        }
//
//        /*
//         * onPostExecute().
//         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
//         * tras la finalización del método doInBackground().
//         * */
//        @Override
//        protected void onPostExecute(Boolean resp) {
//            try {
//                if (resp && listaPeliculas != null && listaPeliculas.size() > 0) {
//                    for (Pelicula pelicula : listaPeliculas) {
//                        pelicula.setFoto("http://" + RakutenTvData.getMiIP() + ":8080/RakutenTV/" + pelicula.getFoto());
//                    }
//                    adaptadorPeliculas = new AdaptadorPeliculasRV(getBaseContext(), listaPeliculas);
//                    lv.setAdapter(adaptadorPeliculas);
//                } else {
////                    Toast.makeText(ListaPeliculasActivity.getInstance().getBaseContext(), "" +
////                            "Lista incorrecta. ", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                Log.e("log_tag", "Error parsing data " + e.toString());
//            }
//        }
//    }

}
