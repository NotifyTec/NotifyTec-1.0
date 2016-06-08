package com.notifytec.contratos;

import java.util.List;

public class NotificacaoCompletaModel extends NotificacaoModel {

    private int totalAlunosEnviados;
    private int totalAlunosVisualizados;
    private int totalRespondidos;
    private AlunoNotificacaoModel resposta;

    public AlunoNotificacaoModel getResposta() {
        return resposta;
    }

    public void setResposta(AlunoNotificacaoModel resposta) {
        this.resposta = resposta;
    }

    public int getTotalAlunosEnviados() {
        return totalAlunosEnviados;
    }

    public void setTotalAlunosEnviados(int totalAlunosEnviados) {
        this.totalAlunosEnviados = totalAlunosEnviados;
    }

    public int getTotalAlunosVisualizados() {
        return totalAlunosVisualizados;
    }

    public void setTotalAlunosVisualizados(int totalAlunosVisualizados) {
        this.totalAlunosVisualizados = totalAlunosVisualizados;
    }

    public int getTotalRespondidos() {
        return totalRespondidos;
    }

    public void setTotalRespondidos(int totalRespondidos) {
        this.totalRespondidos = totalRespondidos;
    }
}
