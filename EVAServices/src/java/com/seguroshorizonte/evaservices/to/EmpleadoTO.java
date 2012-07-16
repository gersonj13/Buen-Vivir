/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.to;

import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import java.io.Serializable;

public class EmpleadoTO extends Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    private String Condicion;

    public String getCondicion() {
        return Condicion;
    }

    public void setCondicion(String Condicion) {
        this.Condicion = Condicion;
    }

    public void copyEmpleado(Empleado empleadox) {
        this.setApellidos(empleadox.getApellidos());
        this.setCodigo(empleadox.getCodigo());
        this.setFechaIngreso(empleadox.getFechaIngreso());
        this.setFechaNacimiento(empleadox.getFechaNacimiento());
        this.setId(empleadox.getId());
        this.setIdCargo(empleadox.getIdCargo());
        this.setIdDependencia(empleadox.getIdDependencia());
        this.setTipoIdentificacion(empleadox.getTipoIdentificacion());
        //  this.setIdUsuario(empleado.getIdUsuario());
        this.setNombres(empleadox.getNombres());
        this.setNroIdentificacion(empleadox.getNroIdentificacion());
    }
}
