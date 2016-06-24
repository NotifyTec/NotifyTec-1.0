package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.EsqueciSenha;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.UsuarioService;

import java.util.List;

public class EsqueceuSenhaTask extends AsyncTask<Void, Void, Resultado<UsuarioModel>> {
    private EsqueciSenha activity;
    private String login;

    public EsqueceuSenhaTask(EsqueciSenha activity, String login){
        this.activity = activity;
        this.login = login;
    }

    @Override
    protected void onPreExecute() {
        activity.antesEsqueceuSenha();
    }

    @Override
    protected void onPostExecute(Resultado<UsuarioModel> usuarioModelResultado) {
        activity.resultadoEsqueceuSenha(usuarioModelResultado);
    }

    @Override
    protected Resultado<UsuarioModel> doInBackground(Void... params) {
        return new UsuarioService(activity.getApplicationContext()).esqueceuSenha(login);
    }
}

