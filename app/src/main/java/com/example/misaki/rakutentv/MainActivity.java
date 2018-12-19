package com.example.misaki.rakutentv;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misaki.rakutentv.adaptadores.AdaptadorPeliculasRV;
import com.example.misaki.rakutentv.beans.Pelicula;
import com.example.misaki.rakutentv.dataGlobal.RakutenTvData;
import com.example.misaki.rakutentv.fragments.FragmentoInfoPelicula;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculas;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasMisCompras;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasMisFavoritos;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasPopulares;
import com.example.misaki.rakutentv.fragments.FragmentoListaPeliculasRanking;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentoInfoPelicula.OnFragmentInteractionListener,
        FragmentoListaPeliculas.OnFragmentInteractionListener,
        FragmentoListaPeliculasMisCompras.OnFragmentInteractionListener,
        FragmentoListaPeliculasMisFavoritos.OnFragmentInteractionListener,
        FragmentoListaPeliculasPopulares.OnFragmentInteractionListener,
        FragmentoListaPeliculasRanking.OnFragmentInteractionListener,
        SearchView.OnQueryTextListener {

    private ArrayList<Pelicula> m_peliculas = new ArrayList<Pelicula>();
    private AdaptadorPeliculasRV adaptadorPeliculas;
    private ListView lv;
    private TextView txtNick;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtNick = (TextView) findViewById(R.id.generalNick);
        txtEmail = (TextView) findViewById(R.id.generaEmail);

        if (RakutenTvData.getCliente() != null) {
//            txtNick.setText("Hola, " + RakutenTvData.getCliente().getNick());
//            txtEmail.setText(RakutenTvData.getCliente().getEmail());
        }

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
        getMenuInflater().inflate(R.menu.toolbar_buscador, menu);

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
            if (RakutenTvData.getCliente() != null) {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasMisFavoritos()).commit();
            } else {
                Toast.makeText(this, "Primero Logeate!", Toast.LENGTH_LONG);
            }
        } else if (id == R.id.nav_compras) {
            if (RakutenTvData.getCliente() != null) {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentoListaPeliculasMisCompras()).commit();
            } else {
                Toast.makeText(getBaseContext(), "Primero Logeate!", Toast.LENGTH_LONG);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("titulo", query );
        FragmentoListaPeliculas fragmentoListaPeliculas = new FragmentoListaPeliculas();
        fragmentoListaPeliculas.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoListaPeliculas).commit();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("titulo", newText );
        FragmentoListaPeliculas fragmentoListaPeliculas = new FragmentoListaPeliculas();
        fragmentoListaPeliculas.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoListaPeliculas).commit();

        return false;
    }


}
