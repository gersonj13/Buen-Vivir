/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.evap.controller.util;

import com.pangea.evap.services.Planteamiento;
import java.util.ArrayList;
import java.util.List;


public class NodoP {
    
    private String idFactor;
    private String descFactor;
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    private List<Planteamiento> listaPlan;

    public NodoP(){
        listaPlan = new ArrayList<Planteamiento>();
    }
    
    public List<Planteamiento> getListaPlan() {
        return listaPlan;
    }

    public void setListaPlan(List<Planteamiento> listaPlan) {
        this.listaPlan = listaPlan;
    }

    public String getDescFactor() {
        return descFactor;
    }

    public void setDescFactor(String descFactor) {
        this.descFactor = descFactor;
    }
    
    public String getIdFactor() {
        return idFactor;
    }

    public void setIdFactor(String idFactor) {
        this.idFactor = idFactor;
    }
}
