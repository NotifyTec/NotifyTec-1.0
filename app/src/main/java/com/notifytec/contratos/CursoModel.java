package com.notifytec.contratos;

import java.util.UUID;

public class CursoModel {
        private UUID id;
        private String nome;
        private String apelido;
        private boolean ativo;
        private String ativoTraduzido;
        private int periodo;

        public int getPeriodo() {
            return periodo;
        }

        public void setPeriodo(int periodo) {
            this.periodo = periodo;
        }

        public String getAtivoTraduzido() {
            return ativoTraduzido;
        }

        public void setAtivoTraduzido(String ativoTraduzido) {
            this.ativoTraduzido = ativoTraduzido;
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

        public String getApelido() {
            return apelido;
        }

        public void setApelido(String apelido) {
            this.apelido = apelido;
        }

        public boolean isAtivo() {
            return ativo;
        }

        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }
    }
