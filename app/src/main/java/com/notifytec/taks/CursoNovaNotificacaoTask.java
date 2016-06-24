package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.NovaNotificacao;
import com.notifytec.contratos.CursoModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.CursoService;

import java.util.List;

public class CursoNovaNotificacaoTask extends AsyncTask<Void, Void, Resultado<List<CursoModel>>>{
    private NovaNotificacao activity;

    public CursoNovaNotificacaoTask(NovaNotificacao activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Resultado<List<CursoModel>> listResultado) {
        activity.setCursos(listResultado);
    }

    @Override
    protected void onPreExecute() {
        activity.antesCarregarCurso();
    }

    @Override
    protected Resultado<List<CursoModel>> doInBackground(Void... params) {
        return new CursoService(activity.getApplicationContext()).getList();
    }
}
