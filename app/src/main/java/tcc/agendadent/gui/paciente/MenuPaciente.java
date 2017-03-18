package tcc.agendadent.gui.paciente;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import tcc.agendadent.R;
import tcc.agendadent.gui.dentista.ConfigAgenda;

public class MenuPaciente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paciente);
//        configuraMenu();
    }

    private void configuraMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.AgendaDiaria);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.Aguarde, R.string.Aguarde);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.config_agenda) {
            startActivity(new Intent(getApplicationContext(), ConfigAgenda.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.AgendaDiaria);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
