package com.notifytec.service;

import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private final String _LOGIN_URL_ = "Login/Login";

    public Resultado<Token> login(String login, String senha) {
        RestService<Token> rest = new RestService<Token>(Token.class);

        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("userName", login);
        parametros.put("password", senha);

        Resultado<Token> resultado = rest.post(_LOGIN_URL_, parametros);
        if (resultado.isSucess()) {
            if (resultado.getResult().getToken() != null && !resultado.getResult().getToken().isEmpty()) {
                // SALVAR TOKEN DO USUARIO NO BANCO OU QUALQUER OUTRO LUGAR

            }
        }

        return resultado;
    }
}
