/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "EMPLEADO_OPCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadoOpcion.findAll", query = "SELECT e FROM EmpleadoOpcion e"),
    @NamedQuery(name = "EmpleadoOpcion.findById", query = "SELECT e FROM EmpleadoOpcion e WHERE e.id = :id")})
public class EmpleadoOpcion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "ID_OPCION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Opcion idOpcion;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleadoOpcion")
    private List<EstadoEmpleadoOpcion> estadoEmpleadoOpcionList;

    public EmpleadoOpcion() {
    }

    public EmpleadoOpcion(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Opcion getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Opcion idOpcion) {
        this.idOpcion = idOpcion;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @XmlTransient
    public List<EstadoEmpleadoOpcion> getEstadoEmpleadoOpcionList() {
        return estadoEmpleadoOpcionList;
    }

    public void setEstadoEmpleadoOpcionList(List<EstadoEmpleadoOpcion> estadoEmpleadoOpcionList) {
        this.estadoEmpleadoOpcionList = estadoEmpleadoOpcionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoOpcion)) {
            return false;
        }
        EmpleadoOpcion other = (EmpleadoOpcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.EmpleadoOpcion[ id=" + id + " ]";
    }
    
}
