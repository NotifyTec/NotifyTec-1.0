package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.MenuPrincipal;
import com.notifytec.adapters.NotificacoesAdapter;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.NotificacaoService;

import java.util.UUID;

public class ConfirmarVisualizadoTask  extends AsyncTask<Void, Void, Resultado<NotificacaoCompletaModel>> {

    private MenuPrincipal activity;
    private UUID notificacaoID;
    private NotificacoesAdapter adapter;
    private int position;
    private NotificacoesAdapter.MyViewHolder myViewHolder;

    public ConfirmarVisualizadoTask(NotificacoesAdapter a, int position,
                                    UUID notificacaoID, MenuPrincipal activity, NotificacoesAdapter.MyViewHolder myViewHolder){
        this.notificacaoID = notificacaoID;
        this.adapter = a;
        this.position = position;
        this.activity = activity;
        this.myViewHolder = myViewHolder;
    }

    @Override
    protected void onPreExecute() {
        activity.setCarregando(true, "Marcando como lida...");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Resultado<NotificacaoCompletaModel> listResultado) {
        activity.setCarregando(false, null);
        adapter.resultadoVisualizar(listResultado, position, this.myViewHolder);
    }

    @Override
    protected Resultado<NotificacaoCompletaModel> doInBackground(Void... params) {
        return new NotificacaoService(activity.getBaseContext()).visualizar(notificacaoID);
    }
}
