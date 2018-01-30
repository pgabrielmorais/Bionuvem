/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbuz.prediction;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;

/**
 * @author guilherme
 */
public class Instance {
    
    public String id;
    public String programa;
    public int memoria;
    public int cpu;
    public int preco;
    public String provider;
    public DateTime creationTimer;
    public int delay;
    public DateTime timetocreate;
    public String isnow; 

    public Instance(){}

    public Instance(String id, String programa, int memoria, int cpu, int preco, String provider, DateTime creationTimer, int delay, DateTime timetocreate, String isnow) {
        this.id = id;
        this.programa = programa;
        this.memoria = memoria;
        this.cpu = cpu;
        this.preco = preco;
        this.provider = provider;
        this.creationTimer = creationTimer;
        this.delay = delay;
        this.timetocreate = timetocreate;
        this.isnow = isnow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public int getMemoria() {
        return memoria;
    }

    public void setMemoria(int memoria) {
        this.memoria = memoria;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public DateTime getCreationTimer() {
        return creationTimer;
    }

    public void setCreationTimer(DateTime creationTimer) {
        this.creationTimer = creationTimer;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public DateTime getTimetocreate() {
        return timetocreate;
    }

    public void setTimetocreate(DateTime timetocreate) {
        this.timetocreate = timetocreate;
    }

    public String getIsnow() {
        return isnow;
    }

    public void setIsnow(String isnow) {
        this.isnow = isnow;
    }
    
    

       
}
