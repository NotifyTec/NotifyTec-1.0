package com.notifytec.contratos;

import java.util.Date;
import java.util.UUID;

public class AlunoNotificacaoModel {

    private UUID id;
    private UUID alunoID;
    private UUID notificacaoID;
    private UUID notificacaoOpcao;
    private Date visualizouEm;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAlunoID() {
        return alunoID;
    }

    public void setAlunoID(UUID alunoID) {
        this.alunoID = alunoID;
    }

    public UUID getNotificacaoID() {
        return notificacaoID;
    }

    public void setNotificacaoID(UUID notificacaoID) {
        this.notificacaoID = notificacaoID;
    }

    public UUID getNotificacaoOpcao() {
        return notificacaoOpcao;
    }

    public void setNotificacaoOpcao(UUID notificacaoOpcao) {
        this.notificacaoOpcao = notificacaoOpcao;
    }

    public Date getVisualizouEm() {
        return visualizouEm;
    }

    public void setVisualizouEm(Date visualizouEm) {
        this.visualizouEm = visualizouEm;
    }
    
    
}
