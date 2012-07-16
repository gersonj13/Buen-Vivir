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
@Table(name = "EMPLEADO_SUPERVISOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadoSupervisor.findAll", query = "SELECT e FROM EmpleadoSupervisor e"),
    @NamedQuery(name = "EmpleadoSupervisor.findByIdEmpleado", query = "SELECT e FROM EmpleadoSupervisor e WHERE e.idEmpleado.id = :id"),
    @NamedQuery(name = "EmpleadoSupervisor.findById", query = "SELECT e FROM EmpleadoSupervisor e WHERE e.id = :id")})
public class EmpleadoSupervisor implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "ID_SUPERVISOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idSupervisor;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleadoSupervisor",orphanRemoval=true)
    private List<EstadoEmpleadoSupervisor> estadoEmpleadoSupervisorList;

    public EmpleadoSupervisor() {
    }

    public EmpleadoSupervisor(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Empleado getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(Empleado idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @XmlTransient
    public List<EstadoEmpleadoSupervisor> getEstadoEmpleadoSupervisorList() {
        return estadoEmpleadoSupervisorList;
    }

    public void setEstadoEmpleadoSupervisorList(List<EstadoEmpleadoSupervisor> estadoEmpleadoSupervisorList) {
        this.estadoEmpleadoSupervisorList = estadoEmpleadoSupervisorList;
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
        if (!(object instanceof EmpleadoSupervisor)) {
            return false;
        }
        EmpleadoSupervisor other = (EmpleadoSupervisor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.EmpleadoSupervisor[ id=" + id + " ]";
    }
    
}
