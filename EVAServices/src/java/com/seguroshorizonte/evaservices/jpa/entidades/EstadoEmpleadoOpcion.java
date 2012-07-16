/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "ESTADO_EMPLEADO_OPCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoEmpleadoOpcion.findAll", query = "SELECT e FROM EstadoEmpleadoOpcion e"),
    @NamedQuery(name = "EstadoEmpleadoOpcion.findById", query = "SELECT e FROM EstadoEmpleadoOpcion e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoEmpleadoOpcion.findByActivo", query = "SELECT e FROM EstadoEmpleadoOpcion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EstadoEmpleadoOpcion.findByFechaInicio", query = "SELECT e FROM EstadoEmpleadoOpcion e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "EstadoEmpleadoOpcion.findByFechaFin", query = "SELECT e FROM EstadoEmpleadoOpcion e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EstadoEmpleadoOpcion.findByUltimoEstado", query = "SELECT e FROM EstadoEmpleadoOpcion e WHERE e.ultimoEstado = :ultimoEstado")})
public class EstadoEmpleadoOpcion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private BigInteger activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ULTIMO_ESTADO")
    private BigInteger ultimoEstado;
    @JoinColumn(name = "ID_EMPLEADO_OPCION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EmpleadoOpcion idEmpleadoOpcion;

    public EstadoEmpleadoOpcion() {
    }

    public EstadoEmpleadoOpcion(BigDecimal id) {
        this.id = id;
    }

    public EstadoEmpleadoOpcion(BigDecimal id, BigInteger activo, Date fechaInicio, BigInteger ultimoEstado) {
        this.id = id;
        this.activo = activo;
        this.fechaInicio = fechaInicio;
        this.ultimoEstado = ultimoEstado;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getActivo() {
        return activo;
    }

    public void setActivo(BigInteger activo) {
        this.activo = activo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigInteger getUltimoEstado() {
        return ultimoEstado;
    }

    public void setUltimoEstado(BigInteger ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

    public EmpleadoOpcion getIdEmpleadoOpcion() {
        return idEmpleadoOpcion;
    }

    public void setIdEmpleadoOpcion(EmpleadoOpcion idEmpleadoOpcion) {
        this.idEmpleadoOpcion = idEmpleadoOpcion;
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
        if (!(object instanceof EstadoEmpleadoOpcion)) {
            return false;
        }
        EstadoEmpleadoOpcion other = (EstadoEmpleadoOpcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.EstadoEmpleadoOpcion[ id=" + id + " ]";
    }
    
}
