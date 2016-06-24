package com.notifytec.contratos;

import android.text.method.DateTimeKeyListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificacaoCompletaModel extends NotificacaoModel {

    private int totalAlunosEnviados;
    private int totalAlunosVisualizados;
    private int totalRespondidos;
    private AlunoNotificacaoModel resposta;
    private String nomePeriodo;
    private String nomeCurso;
    private String nomeUsuario;
    private Date visualizouEm;

    public Date getVisualizouEm() {
        return visualizouEm;
    }

    public void setVisualizouEm(Date visualizouEm) {
        this.visualizouEm = visualizouEm;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomePeriodo() {
        return nomePeriodo;
    }

    public void setNomePeriodo(String nomePeriodo) {
        this.nomePeriodo = nomePeriodo;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

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

    public String getValidade(){
        if(this.getExpiraEm() != null){
                    return "";
        }else{
            return null;
        }
    }
}
