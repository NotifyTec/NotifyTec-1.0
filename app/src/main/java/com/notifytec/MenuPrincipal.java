package com.notifytec;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.notifytec.Dao.UsuarioDao;
import com.notifytec.adapters.NotificacoesAdapter;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.taks.LerNotificacoesPendentesTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import funcoes.Funcoes;
import outros.VarConst;

public class MenuPrincipal extends BaseActivity {

    public static String _MENU_ = "MENU";

    private RecyclerView mRecyclerView;
    private List<NotificacaoCompletaModel> mList = new ArrayList<NotificacaoCompletaModel>();
    private NotificacoesAdapter adapter;
    private NavigationView mNavigationView;
    private Boolean aviso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        super.setActivityReference(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.setActionBarDrawerToggle(toolbar);

        //Mapeano a RecyclerView, Img de EmptyState e Text de explicaçõa
        mRecyclerView = (RecyclerView) findViewById(R.id.RV_Notificacoes);

        checkUsuario();

        checkGooglePlayServices();

        Intent i = getIntent();
        if(i.hasExtra(_MENU_)){
            String menu = i.getStringExtra(_MENU_);
            if(menu.equals("AVISO")){
                // NOTE: AVISO
                aviso = true;
                setTitle("Avisos");
                setItemChecked(1);
            }else{
                // NOTE: ENQUETE
                aviso = false;
                setTitle("Enquetes");
                setItemChecked(2);
            }
        }else{
            // NOTE: MENU PENDENDE
            aviso = null;
            setItemChecked(0);
            setTitle("Início");
        }

        new LerNotificacoesPendentesTask(this, aviso).execute();
    }

    protected void onResume() {
        super.onResume();
    }

    public void showPendentes(Resultado<List<NotificacaoCompletaModel>> resultado){
        if(resultado.isSucess()){
            if(resultado.getResult().size() == 0){
                setEmptyState(true, mRecyclerView);
            }else {
                mRecyclerView.setHasFixedSize(true);//dizendo que o tamanho o RecyclerView ñ vai alterar
                LinearLayoutManager ll = new LinearLayoutManager(getBaseContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                mRecyclerView.setLayoutManager(ll);

                //Criando Adapter
                adapter = new NotificacoesAdapter(this, resultado.getResult(), usuario);
//            adapter.setRecyclerViewOnClickListenerHack(this);
                mRecyclerView.setClickable(false);
                mRecyclerView.setAdapter(adapter);
                setEmptyState(false, mRecyclerView);
            }
        }else{
            mostrarErro(resultado.getMessage());
        }
    }

    public void cargaNotificacaoesEnviados(boolean isEnquete) {
        /*Notificacao n = new Notificacao(this);

        if (n.contaNotificacoesEnviadas(VarConst.idBinaryUsuLogado, isEnquete) > 0) {
            mList = n.getNotificacoesEnviadas(VarConst.idBinaryUsuLogado, isEnquete);

            mRecyclerView.setHasFixedSize(true);//dizendo que o tamanho o RecyclerView ñ vai alterar
            LinearLayoutManager ll = new LinearLayoutManager(getBaseContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            mRecyclerView.setLayoutManager(ll);

            //Criando Adapter
            adapter = new NotificacoesAdapter(getBaseContext(), mList);
//            adapter.setRecyclerViewOnClickListenerHack(this);
            mRecyclerView.setClickable(false);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            //NÃO Possiu novas notificações. Exibir emptyState
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }*/
    }

    public void cargaNotificacaoesRecebidas(boolean isEnquete) {
        /*Notificacao n = new Notificacao(this);

        if (n.contaNotificacoesRecebidas(isEnquete) > 0) {
            mList = n.getNotificacoesRecebidas(isEnquete);

            mRecyclerView.setHasFixedSize(true);//dizendo que o tamanho o RecyclerView ñ vai alterar
            LinearLayoutManager ll = new LinearLayoutManager(getBaseContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            mRecyclerView.setLayoutManager(ll);

            //Criando Adapter
            adapter = new NotificacoesAdapter(getBaseContext(), mList);
//            adapter.setRecyclerViewOnClickListenerHack(this);
            mRecyclerView.setClickable(false);
            mRecyclerView.setAdapter(adapter);
            tvNovasNotifExplica.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            //NÃO Possiu novas notificações. Exibir emptyState
            tvNovasNotifExplica.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }*/
    }

    public void cargaNovasNotificacaoes() {
        //Carregando Novas Notificações
        /*Notificacao n = new Notificacao(this);
        if (n.contaNewNotificacoes() > 0) {
            //Possiu novas notificações
            mList = n.getNewNotificacaoes();

            mRecyclerView.setHasFixedSize(true);//dizendo que o tamanho o RecyclerView ñ vai alterar
            LinearLayoutManager ll = new LinearLayoutManager(getBaseContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            mRecyclerView.setLayoutManager(ll);

            //Criando Adapter
            adapter = new NotificacoesAdapter(getBaseContext(), mList);
            adapter.setRecyclerViewOnClickListenerHack(this);
            mRecyclerView.setAdapter(adapter);
            tvNovasNotifExplica.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            //NÃO Possiu novas notificações. Exibir emptyState
            tvNovasNotifExplica.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }*/
    }
/*
    //Deixar o metodo pq é obrigatorio. Tirar não funfa
    @Override
    public void onClickListener(View view, int position) {
//        Toast.makeText(getBaseContext(), "onClickListener(): " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        if (mNavigationView.getMenu().findItem(R.id.nav_inicio).isChecked()) {
            Notificacao n = new Notificacao(getBaseContext());
            AlunoNotificacao an = new AlunoNotificacao(n.getBd());
            if (mList.get(position).getEnquete() == 1) {
                //Enquete
                //Verifique se ExpiraEm é menor que data atual
                Date dExpira = Funcoes.StringToDate2(mList.get(position).getExpiraEm());
                Date dDtHrAtual = Funcoes.StringToDate(Funcoes.getDateTime());
                if (dExpira != null){
                    if (dDtHrAtual.after(dExpira)){
                        Funcoes.msgSimples(
                                view.getContext(),
                                "Atenção...",
                                "Período de resposta expirado\nSua resposta foi descartada!"
                        );
                        n.setNotificacaoLida(mList.get(position).getId_binary(), "null");
                        adapter.removeListItem(position);
                    } else {
                        //Não expirou periodo de repsosta
                        //Verificar se escolheu alguma opção
                        int index = mList.get(position).getIndexOpcaoEscolhida();
                        if (index == -1) {
                            Snackbar.make(view, "Escolha uma Opção!", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        } else {
//                    String idBinaryNotificacaoOpcao = String.valueOf(opcao.getTag(R.string.testTagIdBinaryNotificacaoOpcao));
                            String idBinaryNotificacaoOpcao = mList.get(position).getListOpcoes().get(index).getId_binary();
                            n.setNotificacaoLida(mList.get(position).getId_binary(), idBinaryNotificacaoOpcao);
                            adapter.removeListItem(position);
                            Snackbar.make(view, "Notificação marcada como lida!", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }
                } else {
                    //Não tem dt de Expira
                    //Verificar se escolheu alguma opção
                    int index = mList.get(position).getIndexOpcaoEscolhida();
                    if (index == -1) {
                        Snackbar.make(view, "Escolha uma Opção!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
//                    String idBinaryNotificacaoOpcao = String.valueOf(opcao.getTag(R.string.testTagIdBinaryNotificacaoOpcao));
                        String idBinaryNotificacaoOpcao = mList.get(position).getListOpcoes().get(index).getId_binary();
                        n.setNotificacaoLida(mList.get(position).getId_binary(), idBinaryNotificacaoOpcao);
                        adapter.removeListItem(position);
                        Snackbar.make(view, "Notificação marcada como lida!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            } else {
                //Aviso
                n.setNotificacaoLida(mList.get(position).getId_binary(), "null");
                adapter.removeListItem(position);
                Snackbar.make(view, "Notificação marcada como lida!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }*/
    }