package com.notifytec.taks;

import android.content.Context;
import android.os.AsyncTask;

import com.notifytec.MenuPrincipal;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.NotificacaoService;

import java.util.List;

public class LerNotificacoesPendentesTask extends AsyncTask<Void, Void, Resultado<List<NotificacaoCompletaModel>>> {

    private MenuPrincipal activity;
    private Boolean aviso;

    public LerNotificacoesPendentesTask(MenuPrincipal activity, Boolean aviso){
        this.activity = activity;
        this.aviso = aviso;
    }

    @Override
    protected void onPreExecute() {
        activity.setCarregandoNotificacao(true);
    }

    @Override
    protected void onPostExecute(Resultado<List<NotificacaoCompletaModel>> notificacaoCompletaModelResultado) {
        activity.showPendentes(notificacaoCompletaModelResultado);
        activity.setCarregandoNotificacao(false);
    }

    @Override
    protected Resultado<List<NotificacaoCompletaModel>> doInBackground(Void... params) {
        return new NotificacaoService(activity.getBaseContext()).get(aviso);
    }
}
