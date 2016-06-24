package com.notifytec.taks;

import android.content.Context;
import android.os.AsyncTask;

import com.notifytec.PrimeiroAcesso;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.UsuarioService;

public class RedefinirSenhaTask extends AsyncTask<Void, Void, Resultado<UsuarioModel>> {

    private String novaSenha;
    private PrimeiroAcesso activity;

    public RedefinirSenhaTask(PrimeiroAcesso activity, String novaSenha){
        this.activity = activity;
        this.novaSenha = novaSenha;
    }

    @Override
    protected void onPreExecute() {
        activity.anteRedefinirSenha();
    }

    @Override
    protected void onPostExecute(Resultado<UsuarioModel> usuarioModelResultado) {
        activity.resultadoRedefinirSenha(usuarioModelResultado);
    }

    @Override
    protected Resultado<UsuarioModel> doInBackground(Void... params) {
        return new UsuarioService(activity.getApplicationContext()).redefinirSenha(novaSenha);
    }
}
