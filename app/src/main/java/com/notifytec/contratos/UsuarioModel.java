package com.notifytec.contratos;

import java.util.Date;
import java.util.UUID;

public class UsuarioModel {

    private UUID id;
    private String login;
    private String senha;
    private Boolean podeEnviar;
    private Boolean alterouSenha;
    private String email;

    public String getTokenRecuperarSenha() {
        return tokenRecuperarSenha;
    }

    public Boolean getAlterouSenha() {
        return alterouSenha;
    }

    public Boolean getPodeEnviar() {
        return podeEnviar;
    }

    private String tokenRecuperarSenha;
    private Date dataValidadeToken;
    private String gcmToken;
    private String token;

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean isPodeEnviar() {
        return podeEnviar;
    }

    public void setPodeEnviar(Boolean podeEnviar) {
        this.podeEnviar = podeEnviar;
    }

    public Boolean isAlterouSenha() {
        return alterouSenha;
    }

    public void setAlterouSenha(Boolean alterouSenha) {
        this.alterouSenha = alterouSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String isTokenRecuperarSenha() {
        return tokenRecuperarSenha;
    }

    public void setTokenRecuperarSenha(String tokenRecuperarSenha) {
        this.tokenRecuperarSenha = tokenRecuperarSenha;
    }

    public Date getDataValidadeToken() {
        return dataValidadeToken;
    }

    public void setDataValidadeToken(Date dataValidadeToken) {
        this.dataValidadeToken = dataValidadeToken;
    }
}