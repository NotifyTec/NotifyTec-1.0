package com.notifytec.service;

import android.content.Context;
import android.util.Log;

import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import java.util.HashMap;
import java.util.Map;

public class UsuarioService {
    private String _LINK_UPDATE_GCM_ = "Login/UpdateGcm";
    private String _LINK_REDEFINIR_SENHA = "Login/RedefinirSenha";
    private String _LINK_ESQUECEU_SENHA = "Login/EsqueceuSenha";
    private String _LINK_CONFIRMAR_ESQUECEU_SENHA = "Login/ConfirmarEsqueceuSenha";

    private UsuarioDao dao;
    private Context context;

    public UsuarioService(Context context){
        this.context = context;
        this.dao = new UsuarioDao(context);
    }

    public Resultado<UsuarioModel> redefinirSenha(String novaSenha){
        RestService<UsuarioModel> rest = new RestService<UsuarioModel>(UsuarioModel.class, context);
        Map<String, String> map = new HashMap<>();
        map.put("novaSenha", novaSenha);
        map.put("usuarioID", dao.get().getId().toString());

        Resultado<UsuarioModel> resultado = rest.post(_LINK_REDEFINIR_SENHA, map);
        if(!resultado.isSucess())
            return resultado;

        if(!dao.update(resultado.getResult())){
            resultado.addError("Não foi possível salvar a nova senha.");
        }
        return resultado;
    }

    public Resultado<UsuarioModel> esqueceuSenha(String login){
        RestService<UsuarioModel> rest = new RestService<UsuarioModel>(UsuarioModel.class, context);
        Map<String, String> map = new HashMap<>();
        map.put("login", login);

        Resultado<UsuarioModel> r = rest.post(_LINK_ESQUECEU_SENHA, map);
        if(!r.isSucess())
            return r;

        return r;
    }

    public Resultado<UsuarioModel> confirmarEsqueceuSenha(String token, String novaSenha){
        RestService<UsuarioModel> rest = new RestService<UsuarioModel>(UsuarioModel.class, context);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("novaSenha", novaSenha);

        Resultado<UsuarioModel> r = rest.post(_LINK_CONFIRMAR_ESQUECEU_SENHA, map);
        if(!r.isSucess())
            return r;

        return r;
    }

    public Resultado setGcmToken(String token){
        Resultado<UsuarioModel> r = new Resultado();

        UsuarioModel usuario = dao.get();

        if(usuario == null) {
            usuario = new UsuarioModel();
            usuario.setGcmToken(token);
            dao.inserir(usuario);
        }else{
            usuario.setGcmToken(token);
            dao.update(usuario);
        }

        Log.d(String.valueOf(Log.VERBOSE), "SALVO TOKEN NO CELULAR: " + token);

        sendGcmTokenToBack();
        return r;
    }

    public void sendGcmTokenToBack(){
        UsuarioModel u = dao.get();
        if(u == null || u.getId() == null || u.getGcmToken() == null || u.getGcmToken().isEmpty())
            return;

        RestService rest = new RestService<UsuarioModel>(UsuarioModel.class, context);

        Map<String, String> map = new HashMap<String, String>();
        map.put("gcm", u.getGcmToken());
        map.put("usuarioID", u.getId().toString());
        rest.post(_LINK_UPDATE_GCM_, map);

        Log.d(String.valueOf(Log.VERBOSE), "ENVIADO TOKEN PARA BACK-END: " + u.getGcmToken());
    }
}
