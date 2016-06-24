package com.notifytec.taks;

import android.content.Context;
import android.os.AsyncTask;

import com.notifytec.MenuPrincipal;
import com.notifytec.adapters.NotificacoesAdapter;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.NotificacaoService;

import java.util.List;
import java.util.UUID;

public class ResponderNotificacaoTask extends AsyncTask<Void, Void, Resultado<NotificacaoCompletaModel>> {

    private MenuPrincipal activity;
    private UUID notificacaoID;
    private UUID notificacaoOpcaoID;
    private  NotificacoesAdapter adapter;
    private int position;
    private NotificacoesAdapter.MyViewHolder myViewHolder;

    public ResponderNotificacaoTask(NotificacoesAdapter a, int position, MenuPrincipal activity, UUID notificacaoID, UUID notificacaoOpcaoID, NotificacoesAdapter.MyViewHolder myViewHolder){
        this.activity = activity;
        this.notificacaoID = notificacaoID;
        this.notificacaoOpcaoID = notificacaoOpcaoID;
        this.adapter = a;
        this.position = position;
        this.myViewHolder = myViewHolder;
    }

    @Override
    protected void onPreExecute() {
        activity.setCarregando(true, "Salvando resposta...");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Resultado<NotificacaoCompletaModel> listResultado) {
        activity.setCarregando(false, null);
        adapter.resultadoResposta(listResultado, position, this.myViewHolder);
    }

    @Override
    protected Resultado<NotificacaoCompletaModel> doInBackground(Void... params) {
        return new NotificacaoService(activity.getBaseContext()).responder(notificacaoID, notificacaoOpcaoID);
    }
}
