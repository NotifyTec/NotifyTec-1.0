package com.notifytec.service;

import android.content.Context;

import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private final String _LOGIN_URL_ = "Login/LoginApp";

    private Context context;

    private UsuarioService usuarioService;

    public LoginService(Context context){
        this.context = context;
        this.usuarioService = new UsuarioService(this.context);
    }

    public Resultado<UsuarioModel> login(String login, String senha) {
        RestService<UsuarioModel> rest = new RestService<UsuarioModel>(UsuarioModel.class, context);

        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("userName", login);
        parametros.put("password", senha);

        Resultado<UsuarioModel> resultado = rest.post(_LOGIN_URL_, parametros);
        if (resultado.isSucess()) {
            if (resultado.getResult().getToken() != null && !resultado.getResult().getToken().isEmpty()) {

                if(!new UsuarioDao(context).inserir(resultado.getResult())){
                    Resultado<UsuarioModel> r = new Resultado<>();
                    r.merge(resultado);
                    r.addError("Erro ao realizar o login. Salvar usuário no banco.");
                    return r;
                }

                this.usuarioService.sendGcmTokenToBack();
            }
        }

        return resultado;
    }
}
