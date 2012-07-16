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
@Table(name = "ESTADO_EMPLEADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoEmpleado.findAll", query = "SELECT e FROM EstadoEmpleado e"),
    @NamedQuery(name = "EstadoEmpleado.findById", query = "SELECT e FROM EstadoEmpleado e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoEmpleado.findByActivo", query = "SELECT e FROM EstadoEmpleado e WHERE e.activo = :activo"),
    @NamedQuery(name = "EstadoEmpleado.findByEmpleado", query = "SELECT e FROM EstadoEmpleado e WHERE e.idEmpleado.id = :id"),
    @NamedQuery(name = "EstadoEmpleado.findByFechaInicio", query = "SELECT e FROM EstadoEmpleado e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "EstadoEmpleado.findByFechaFin", query = "SELECT e FROM EstadoEmpleado e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EstadoEmpleado.findByUltimoEstado", query = "SELECT e FROM EstadoEmpleado e WHERE e.ultimoEstado = :ultimoEstado")})
public class EstadoEmpleado implements Serializable {
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
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;

    public EstadoEmpleado() {
    }

    public EstadoEmpleado(BigDecimal id) {
        this.id = id;
    }

    public EstadoEmpleado(BigDecimal id, BigInteger activo, Date fechaInicio, BigInteger ultimoEstado) {
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

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
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
        if (!(object instanceof EstadoEmpleado)) {
            return false;
        }
        EstadoEmpleado other = (EstadoEmpleado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.EstadoEmpleado[ id=" + id + " ]";
    }
    
}
