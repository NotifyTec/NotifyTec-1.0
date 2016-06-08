package com.notifytec.contratos;

import java.util.ArrayList;
import java.util.List;

public class Resultado<T> {

    private List<String> messages;
    private boolean success;
    private T result;

    public Resultado() {
        this.messages = new ArrayList<>();
        this.success = true;
    }

    public Resultado(T result) {
        this();
        setResult(result);
    }

    public void merge(Resultado r){            
        this.messages.addAll(r.getMessages());
        if(!r.isSucess())
            this.success = false;
    }
    
    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

    public void addWarning(String warning) {
        this.messages.add(warning);
    }

    public void addError(String error) {
        this.addWarning(error);
        this.success = false;
    }
    
    public List<String> getMessages(){
        return this.messages;
    }
    
    public boolean isSucess(){
        return this.success;
    }

}
