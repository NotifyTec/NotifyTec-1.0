package com.notifytec.contratos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotificacaoModel {

    private UUID id;
    private String titulo;
    private String conteudo;
    private UUID usuarioID;
    private Date dataHoraEnvio;
    private Date expiraEm;
    private List<NotificacaoOpcaoModel> opcoes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public UUID getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(UUID usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Date getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(Date dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Date getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Date expiraEm) {
        this.expiraEm = expiraEm;
    }

    public List<NotificacaoOpcaoModel> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<NotificacaoOpcaoModel> opcoes) {
        this.opcoes = opcoes;
    }
}
