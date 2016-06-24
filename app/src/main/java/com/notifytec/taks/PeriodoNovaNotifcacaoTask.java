package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.NovaNotificacao;
import com.notifytec.contratos.CursoModel;
import com.notifytec.contratos.PeriodoModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.service.CursoService;

import java.util.List;
import java.util.UUID;

public class PeriodoNovaNotifcacaoTask extends AsyncTask<Void, Void, Resultado<List<PeriodoModel>>> {
    private NovaNotificacao activity;
    private UUID cursoID;

    public PeriodoNovaNotifcacaoTask(NovaNotificacao activity, UUID cursoID){
        this.activity = activity;
        this.cursoID = cursoID;
    }

    @Override
    protected void onPostExecute(Resultado<List<PeriodoModel>> listResultado) {
        activity.setPeriodos(listResultado);
    }

    @Override
    protected void onPreExecute() {
        activity.antesCarregarPeriodo();
    }

    @Override
    protected Resultado<List<PeriodoModel>> doInBackground(Void... params) {
        return new CursoService(activity.getApplicationContext()).getPeriodos(this.cursoID);
    }
}
