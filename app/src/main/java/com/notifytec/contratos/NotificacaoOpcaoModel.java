package com.notifytec.contratos;

import java.util.UUID;

public class NotificacaoOpcaoModel {

    private UUID id;
    private String nome;
    private UUID notificacaoID;
    private int totalRespondidos;

    public int getTotalRespondidos() {
        return totalRespondidos;
    }

    public void setTotalRespondidos(int totalRespondidos) {
        this.totalRespondidos = totalRespondidos;
    }

    public UUID getNotificacaoID() {
        return notificacaoID;
    }

    public void setNotificacaoID(UUID notificacaoID) {
        this.notificacaoID = notificacaoID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
