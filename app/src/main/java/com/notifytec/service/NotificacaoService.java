package com.notifytec.service;

import android.content.Context;

import com.notifytec.Dao.DatabaseManager;
import com.notifytec.Dao.NotificacaoDao;
import com.notifytec.Dao.UsuarioDao;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import funcoes.Funcoes;

public class NotificacaoService {
    private final String _LINK_GET_NOTIFICACOES_ = "/Notificacao/get";
    private final String _LINK_RESPONDER_NOTIFICACOES_ = "/Notificacao/responder";
    private final String _LINK_ENVIAR_NOTIFICACAO_ = "/Notificacao/enviar";
    private final String _LINK_VISUALIZAR_NOTIFICACAO_ = "/Notificacao/visualizar";

    private NotificacaoDao dao;
    private UsuarioDao usuarioDao;

    private Context context;

    public NotificacaoService(Context context){
        this.context = context;
        this.dao = new NotificacaoDao(context);
        this.usuarioDao = new UsuarioDao(context);
    }

    public Resultado<NotificacaoCompletaModel> responder(UUID notificacaoID, UUID notificacaoOpcaoID){
        Resultado<NotificacaoCompletaModel> r = new Resultado<>();

        RestService<NotificacaoCompletaModel> rest = new RestService<>(NotificacaoCompletaModel.class, context);
        Map<String, String> map = new HashMap<>();
        map.put("notificacaoID", notificacaoID.toString());
        map.put("notificacaoOpcaoID", notificacaoOpcaoID.toString());
        map.put("usuarioID", usuarioDao.get().getId().toString());

        r = rest.post(_LINK_RESPONDER_NOTIFICACOES_, map);

        if(!r.isSucess()){
            return r;
        }

        dao.update(r.getResult());

        return r;
    }

    public Resultado<List<NotificacaoCompletaModel>> get(Boolean aviso){
        Resultado<List<NotificacaoCompletaModel>> resultado = new Resultado<>();
        UsuarioModel usuario = usuarioDao.get();

        if(Funcoes.conectadoAInternet(context)){
            resultado = get(usuario.getId(), aviso);

            if(!resultado.isSucess()){
                return resultado;
            }
        }

        resultado.setResult(this.dao.get(aviso));
        return resultado;
    }

    private Resultado<List<NotificacaoCompletaModel>> get(UUID usuarioID, Boolean aviso){
        Resultado<List<NotificacaoCompletaModel>> resultado = new Resultado<>();

        RestService<NotificacaoCompletaModel> r = new RestService<NotificacaoCompletaModel>(NotificacaoCompletaModel.class, context);
        Map<String, String> map = new HashMap<String, String>();
        map.put("usuarioID", usuarioID.toString());

         resultado =  r.postList(_LINK_GET_NOTIFICACOES_, map);
        if(!resultado.isSucess())
            return resultado;

        if(!dao.add(resultado.getResult())){
            resultado.addError("Erro ao salvar as notificações.");
        }
        return resultado;
    }

    public Resultado<NotificacaoCompletaModel> enviar(UUID periodoID,
                                                      String titulo,
                                                      String conteudo,
                                                      Date expiraEm,
                                                      List<String> opcoes) {
        RestService<NotificacaoCompletaModel> rest = new RestService<NotificacaoCompletaModel>(NotificacaoCompletaModel.class, context);
        Map<String, Object> map = new HashMap<>();
        map.put("periodoID", periodoID.toString());
        map.put("titulo", titulo);
        map.put("conteudo", conteudo);

        String ops = "";
        if (opcoes.size() > 0){
            for (String o : opcoes) {
                ops += o + "¬";
            }
            ops = ops.substring(0, ops.length() - 1);
        }

        map.put("opcoes", ops);
        map.put("usuarioID", new UsuarioDao(context).get().getId());
        if(expiraEm != null)
            map.put("expiraEm", Funcoes.dateTimeToSQL(expiraEm, true));

        Resultado<NotificacaoCompletaModel> r = rest.post(_LINK_ENVIAR_NOTIFICACAO_, map);

        if(!r.isSucess())
            return r;

        List<NotificacaoCompletaModel> list = new ArrayList<>();
        list.add(r.getResult());
        dao.add(list);

        return r;
    }

    public Resultado<NotificacaoCompletaModel> visualizar(UUID notificacaoID){
        RestService<NotificacaoCompletaModel> rest = new RestService<NotificacaoCompletaModel>(NotificacaoCompletaModel.class, context);
        Map<String, String> map = new HashMap<>();
        map.put("notificacaoID", notificacaoID.toString());
        map.put("usuarioID", usuarioDao.get().getId().toString());

        Resultado<NotificacaoCompletaModel> r = rest.post(_LINK_VISUALIZAR_NOTIFICACAO_, map);
        if(!r.isSucess())
            return r;

        dao.update(r.getResult());

        return r;
    }
}
