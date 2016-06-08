package com.notifytec.service;

import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NotificacaoService {
    private final String _LINK_GET_RECEBIDAS_ = "/Notificacao/getRecebidas";
    private final String _LINK_GET_ENVIADAS_ = "/Notificacao/getEnviadas";

    public Resultado<List<NotificacaoCompletaModel>> getRecebidas(UUID alunoID){
        RestService<NotificacaoCompletaModel> r = new RestService<NotificacaoCompletaModel>(NotificacaoCompletaModel.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("alunoID", alunoID.toString());

        Resultado<List<NotificacaoCompletaModel>> resultado =  r.getList(_LINK_GET_RECEBIDAS_, map);
        return resultado;
    }

    public Resultado<List<NotificacaoCompletaModel>> getEnviadas(UUID professorID){
        RestService<NotificacaoCompletaModel> r = new RestService<NotificacaoCompletaModel>(NotificacaoCompletaModel.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("professorID", professorID.toString());

        Resultado<List<NotificacaoCompletaModel>> resultado =  r.getList(_LINK_GET_ENVIADAS_, map);
        return resultado;
    }
}
