package com.notifytec;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import org.w3c.dom.Text;

import outros.VarConst;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected UsuarioModel usuario;

    private LinearLayout mLinearLayoutBase;
    private DrawerLayout mDrawer;
    private FrameLayout mFrame;
    private ProgressBar mLoadingProgressbar;
    private Toolbar mToolbar;
    private ImageView mEmptyState;
    protected NavigationView mNavigationView;
    private int activityAtual = -1;
    private Activity activityReference;
    private Snackbar mSnackLoading;
    private Snackbar mSnackErro;

    @Override
    public void setContentView(int layoutResID) {
        mDrawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mFrame = (FrameLayout) mDrawer.findViewById(R.id.base_frame);
        // add layout of BaseActivities inside framelayout.i.e. frame_container
        getLayoutInflater().inflate(layoutResID, mFrame, true);
        // set the drawer layout as main content view of Activity.
        super.setContentView(mDrawer);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    protected void setItemChecked(int index){
        if(mNavigationView == null)
            mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavigationView.getMenu().getItem(index).setChecked(true);
    }

    protected void checkUsuario(){
        checkFab();
        checkMenu();
    }

    private void checkMenu() {
        View headerLayout = mNavigationView.getHeaderView(0);
        if(usuario.isPodeEnviar()) {
            mNavigationView.getMenu().findItem(R.id.nav_inicio).setVisible(false);
        }

        TextView user = (TextView) headerLayout.findViewById(R.id.edUsuarioLogado);
        user.setText(usuario.getEmail());
    }

    private void checkFab(){
        if (usuario.isPodeEnviar()) {
            FloatingActionButton fab = (FloatingActionButton) activityReference.findViewById(R.id.fab_nova_notificacao);
            fab.setVisibility(View.VISIBLE);
            //Colorindo o fab
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_principal)));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tela = new Intent(getBaseContext(), NovaNotificacao.class);
                    startActivity(tela);
                }
            });
       }
    }

    public void setEmptyState(boolean visible, View viewToChange){
        if(mEmptyState == null) {
            mEmptyState = (ImageView) findViewById(R.id.img_emptyState);
        }

        if(visible){
            mEmptyState.setVisibility(View.VISIBLE);
            viewToChange.setVisibility(View.GONE);
        }else{
            mEmptyState.setVisibility(View.GONE);
            viewToChange.setVisibility(View.VISIBLE);
        }
    }

    public void mostrarErro(String erro){
        mSnackErro = Snackbar.make(findViewById(R.id.base_frame), erro, Snackbar.LENGTH_INDEFINITE)
        .setAction("FECHAR", new View.OnClickListener() {
            public void onClick(View v) {
                if(mSnackErro != null) mSnackErro.dismiss();
            }
        });

        if(mSnackLoading != null && mSnackLoading.isShown())
            mSnackLoading.dismiss();

        mSnackErro.show();
    }

    public void setCarregando(boolean mostrar, String mensagem){
        if(!mostrar && mSnackLoading == null)
            return;

        if(!mostrar && mSnackLoading != null) {
            mSnackLoading.dismiss();
            mSnackLoading = null;
        }

        if(mostrar) {
            mSnackLoading = Snackbar.make(findViewById(R.id.base_frame), mensagem, Snackbar.LENGTH_INDEFINITE);
            mSnackLoading.show();
        }
    }

    public void setCarregandoNotificacao(boolean mostrar){
        setCarregando(mostrar,  "Carregando notificações...");
    }

    public void setActionBarDrawerToggle(Toolbar toolbar){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    protected void setActivityReference(Activity a){
        this.activityReference = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = new UsuarioDao(getApplicationContext()).get();
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
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_search) {
            if(isSearchEnabled){
                setSearchVisible(true);
            }
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void abrirPendente(){
        Intent i = new Intent(getBaseContext(), MenuPrincipal.class);
        startActivity(i);
    }

    public void abrirAvisos(){
        Intent i = new Intent(getBaseContext(), MenuPrincipal.class);
        i.putExtra(MenuPrincipal._MENU_, "AVISO");
        startActivity(i);
    }

    public void abrirEnquetes(){
        Intent i = new Intent(getBaseContext(), MenuPrincipal.class);
        i.putExtra(MenuPrincipal._MENU_, "ENQUETE");
        startActivity(i);
    }

    public void logout(){
        if(!new UsuarioDao(getApplicationContext()).logout())
        {
            mostrarErro("Não foi possível realizar o logout");
        }else {
            Intent i  =new Intent(getBaseContext(), LoginActivity.class);
            i.putExtra(LoginActivity.LOG_OUT, "1");

            startActivity(i);
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == activityAtual)
            return true;

        activityAtual = id;

        switch (id){
            case R.id.nav_inicio:{
                abrirPendente();
                break;
            }
            case R.id.nav_enquete:{
                abrirEnquetes();
                break;
            }
            case R.id.nav_aviso:{
                abrirAvisos();
                break;
            }
            case R.id.nav_logout:{
                logout();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkGooglePlayServices(){
        int r = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(r != ConnectionResult.SUCCESS){
            GooglePlayServicesUtil.getErrorDialog(r, this, r, null);
        }
    }
}
