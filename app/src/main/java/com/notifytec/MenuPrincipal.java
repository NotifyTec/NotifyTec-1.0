package com.notifytec;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import outros.VarConst;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB
        if (VarConst.podeEnviar){
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            //Colorindo o fab
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_principal)));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tela = new Intent(getBaseContext(), novo_notificacao.class);
                    startActivity(tela);
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            });
        }


        //DrawerMenu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Itens DrawerMenu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Atualizando TextView com o nome do usuario logado
        View headerLayout = navigationView.getHeaderView(0);
        TextView user = (TextView) headerLayout.findViewById(R.id.edUsuarioLogado);
        user.setText(VarConst.nome_usu_logado);

        //Exibindo emptyState
        if (!VarConst.novasnotificacoes){
            ImageView emptyState = (ImageView) findViewById(R.id.img_emptyState);
            emptyState.setVisibility(View.VISIBLE);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            setTitle(getString(R.string.title_activity_menu_principal));
        } else if (id == R.id.nav_aviso) {
            setTitle(getString(R.string.title_activity_menu_principal_aviso));
        } else if (id == R.id.nav_enquete) {
            setTitle(getString(R.string.title_activity_menu_principal_enquete));
        } else if (id == R.id.nav_logout) {
            Intent tela = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(tela);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
