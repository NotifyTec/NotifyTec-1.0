package com.notifytec.service;

import com.google.gson.Gson;
import com.notifytec.contratos.Resultado;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestService<T> {

    private String _DOMINIO_ = "http://notifytec.azurewebsites.net/";
    private Class<T> type;

    public RestService(Class<T> type) {
        this.type = type;
    }

    public String formatarUrl(String url) {
        if (url.charAt(0) == '/')
            url = url.substring(1);

        return _DOMINIO_ + url;
    }

    private String getAuthorizationToken(){
        return "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbiI6ImFuZHJvaWQiLCJlbWFpbCI6InRlc3RlQGRvbWFpbi5jb20iLCJhbHRlcm91U2VuaGEiOnRydWUsInBvZGVFbnZpYXIiOnRydWUsImp0aSI6ImMyYmIxYmEyLWYyYjMtNGM2My05ODIwLTFjNmI3NTJiOGVkYyJ9.H8Ze9GRvwp7KO9m4ZL_oIj3HjtjRw1n23bgfAqFuATpBWx336T5iNtAbcLNf4xQzSMQmsiPwmdxuGn5lUPWhRA";
    }

    private Resultado execute(String url, HttpMethod method, Object obj, Map<?, ?> params){
        try {
            url = formatarUrl(url);
            RestTemplate t = new RestTemplate();
            t.getMessageConverters().add(new GsonHttpMessageConverter());

            HttpHeaders header = new HttpHeaders();
            header.set("Authorization",getAuthorizationToken());

            HttpEntity entity;
            if(obj == null){
                entity = new HttpEntity(header);
            }else{
                entity = new HttpEntity(obj, header);
            }

            ResponseEntity<? extends Resultado> resultado = null;
            resultado = t.exchange(url, method, entity, Resultado.class, params);

            if(resultado.getBody().getResult() instanceof ArrayList){
                Resultado<List<T>> rl = new Resultado<>();
                rl.merge(resultado.getBody());

                List<T> list = new ArrayList<T>();
                for (Object o : (ArrayList) resultado.getBody().getResult()) {
                    list.add(new Gson().fromJson(new Gson().toJson(o), type));
                }

                rl.setResult(list);

                return rl;
            }else{
                Resultado<T> r = new Resultado<>();
                r.merge(resultado.getBody());

                T res = new Gson().fromJson(resultado.getBody().getResult().toString(), type);
                r.setResult(res);

                return r;
            }
        } catch (Exception ex) {
            return getExceptionReturn(ex);
        }
    }

    public Resultado<T> get(String url, Map<?, ?> urlParametros) {
        return execute(url, HttpMethod.GET, null, urlParametros);
    }

    public Resultado<List<T>> getList(String url, Map<?, ?> urlParametros) {
        return execute(url, HttpMethod.GET, null, urlParametros);
    }

    public Resultado<T> post(String url, Object obj) {
        return execute(url, HttpMethod.POST, obj, null);
    }

    public Resultado<List<T>> postList(String url, Object obj) {
        return execute(url, HttpMethod.POST, obj, null);
    }

    private Resultado getExceptionReturn(Exception ex){
        if(ex.getMessage().contains("401")){
            return getNaoAutorizado();
        }else{
            return getErroGenerico();
        }
    }

    private Resultado getErroGenerico() {
        Resultado r = new Resultado();
        r.addError("Verifique a sua conexão com a internet.");
        return r;
    }

    private Resultado getNaoAutorizado() {
        Resultado r = new Resultado();
        r.addError("Usuário não autorizado.");
        return r;
    }
}