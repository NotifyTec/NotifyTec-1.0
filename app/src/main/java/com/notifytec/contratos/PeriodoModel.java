package com.notifytec.contratos;

import java.util.UUID;

public class PeriodoModel {
    private UUID id;
    private int numero;
    private UUID cursoid;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public UUID getCursoid() {
        return cursoid;
    }

    public void setCursoid(UUID cursoid) {
        this.cursoid = cursoid;
    }
}
