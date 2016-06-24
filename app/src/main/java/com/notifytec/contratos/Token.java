package com.notifytec.contratos;

import java.util.UUID;

public class Token {

    private String token;
    private String nome;
    private String gcmToken;
    private boolean podeEnviar;
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isPodeEnviar() {
        return podeEnviar;
    }

    public void setPodeEnviar(boolean podeEnviar) {
        this.podeEnviar = podeEnviar;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
