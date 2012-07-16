/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "PERIODO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodo.findAll", query = "SELECT p FROM Periodo p"),
    @NamedQuery(name = "Periodo.findById", query = "SELECT p FROM Periodo p WHERE p.id = :id"),
    @NamedQuery(name = "Periodo.findByFechas", query = "SELECT p FROM Periodo p WHERE :fecha between p.fechaInicioEvaluacion and p.fechaFinEvaluacion"),
    @NamedQuery(name = "Periodo.findByFechaInicioEvaluacion", query = "SELECT p FROM Periodo p WHERE p.fechaInicioEvaluacion = :fechaInicioEvaluacion"),
    @NamedQuery(name = "Periodo.findByFechaFinEvaluacion", query = "SELECT p FROM Periodo p WHERE p.fechaFinEvaluacion = :fechaFinEvaluacion"),
    @NamedQuery(name = "Periodo.findByDescripcion", query = "SELECT p FROM Periodo p WHERE p.descripcion = :descripcion")})
public class Periodo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO_EVALUACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FIN_EVALUACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPeriodo")
    private List<Corte> corteList;

    public Periodo() {
    }

    public Periodo(BigDecimal id) {
        this.id = id;
    }

    public Periodo(BigDecimal id, Date fechaInicioEvaluacion, Date fechaFinEvaluacion, String descripcion) {
        this.id = id;
        this.fechaInicioEvaluacion = fechaInicioEvaluacion;
        this.fechaFinEvaluacion = fechaFinEvaluacion;
        this.descripcion = descripcion;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getFechaInicioEvaluacion() {
        return fechaInicioEvaluacion;
    }

    public void setFechaInicioEvaluacion(Date fechaInicioEvaluacion) {
        this.fechaInicioEvaluacion = fechaInicioEvaluacion;
    }

    public Date getFechaFinEvaluacion() {
        return fechaFinEvaluacion;
    }

    public void setFechaFinEvaluacion(Date fechaFinEvaluacion) {
        this.fechaFinEvaluacion = fechaFinEvaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Corte> getCorteList() {
        return corteList;
    }

    public void setCorteList(List<Corte> corteList) {
        this.corteList = corteList;
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
        if (!(object instanceof Periodo)) {
            return false;
        }
        Periodo other = (Periodo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Periodo[ id=" + id + " ]";
    }
}
