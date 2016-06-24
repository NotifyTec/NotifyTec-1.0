package com.notifytec.service;

import android.content.Context;
import android.webkit.HttpAuthHandler;

import com.notifytec.contratos.CursoModel;
import com.notifytec.contratos.PeriodoModel;
import com.notifytec.contratos.Resultado;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CursoService {
    private Context context;

    private final String _LINK_GET_LIST_ = "Curso/getCursos";
    private final String _LINK_GET_PERIODOS_LIST_ = "Curso/getListPeriodo";

    public CursoService(Context context){
        this.context = context;
    }

    public Resultado<List<CursoModel>> getList(){
        RestService<CursoModel> rest = new RestService<CursoModel>(CursoModel.class, context);
        return rest.getList(_LINK_GET_LIST_, null);
    }

    public Resultado<List<PeriodoModel>> getPeriodos(UUID cursoID){
        RestService<PeriodoModel> rest = new RestService<PeriodoModel>(PeriodoModel.class, context);
        Map<String, String> map = new HashMap<String, String>();
        map.put("cursoID", cursoID.toString());

        return rest.postList(_LINK_GET_PERIODOS_LIST_, map);
    }
}
