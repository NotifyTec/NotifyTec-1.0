package com.notifytec.taks;

import android.os.AsyncTask;

import com.notifytec.TrocarSenha;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.UsuarioService;

public class ConfirmarEsqueceuSenhaTask  extends AsyncTask<Void, Void, Resultado<UsuarioModel>> {
    private TrocarSenha activity;
    private String token;
    private String novaSenha;

    public ConfirmarEsqueceuSenhaTask(TrocarSenha activity, String token, String novaSenha){
        this.activity = activity;
        this.token = token;
        this.novaSenha = novaSenha;
    }

    @Override
    protected void onPreExecute() {
        activity.antesTrocarSenha();
    }

    @Override
    protected void onPostExecute(Resultado<UsuarioModel> usuarioModelResultado) {
        activity.resultadoConfirmarEsqueceuSenha(usuarioModelResultado);
    }

    @Override
    protected Resultado<UsuarioModel> doInBackground(Void... params) {
        return new UsuarioService(activity.getApplicationContext()).confirmarEsqueceuSenha(token, novaSenha);
    }
}
