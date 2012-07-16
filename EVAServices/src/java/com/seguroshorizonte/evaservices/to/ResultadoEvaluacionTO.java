/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.to;

import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.Evaluacion;
import com.seguroshorizonte.evaservices.jpa.entidades.Planteamiento;
import com.seguroshorizonte.evaservices.jpa.entidades.TipoEvaluacion;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author gerson ramirez
 */
public class ResultadoEvaluacionTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Planteamiento> planteamientos;
    private Empleado empleadox;
    private Empleado supervisor;
    private Evaluacion evaluacionx;
    private TipoEvaluacion tipoEvaluacionx;
    private String observacion;

    public Empleado getEmpleadox() {
        return empleadox;
    }

    public void setEmpleadox(Empleado empleadox) {
        this.empleadox = empleadox;
    }

    public Evaluacion getEvaluacionx() {
        return evaluacionx;
    }

    public void setEvaluacionx(Evaluacion evaluacionx) {
        this.evaluacionx = evaluacionx;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Planteamiento> getPlanteamientos() {
        return planteamientos;
    }

    public void setPlanteamientos(List<Planteamiento> planteamientos) {
        this.planteamientos = planteamientos;
    }

    public Empleado getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Empleado supervisor) {
        this.supervisor = supervisor;
    }

    public TipoEvaluacion getTipoEvaluacionx() {
        return tipoEvaluacionx;
    }

    public void setTipoEvaluacionx(TipoEvaluacion tipoEvaluacionx) {
        this.tipoEvaluacionx = tipoEvaluacionx;
    }
}
