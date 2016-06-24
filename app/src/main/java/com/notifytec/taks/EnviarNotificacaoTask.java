package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.NovaNotificacao;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.NotificacaoService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EnviarNotificacaoTask extends AsyncTask<Void, Void, Resultado<NotificacaoCompletaModel>> {
    private NovaNotificacao activity;
    private UUID periodoID;
    private String titulo;
    private String conteudo;
    private Date expiraEm;
    private List<String> opcoes;

    public EnviarNotificacaoTask(NovaNotificacao activity,
                                 UUID periodoID,
                                 String titulo,
                                 String conteudo,
                                 Date expiraEm,
                                 List<String> opcoes){
        this.activity = activity;
        this.periodoID = periodoID;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.expiraEm = expiraEm;
        this.opcoes = opcoes;
    }

    @Override
    protected void onPreExecute() {
        activity.antesEnviar();
    }

    @Override
    protected Resultado<NotificacaoCompletaModel> doInBackground(Void... params) {
        return new NotificacaoService(activity.getApplicationContext())
                .enviar(periodoID, titulo, conteudo, expiraEm, opcoes);
    }

    @Override
    protected void onPostExecute(Resultado<NotificacaoCompletaModel> notificacaoCompletaModelResultado) {
        this.activity.resultadoEnviar(notificacaoCompletaModelResultado);
    }
}
