/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "EVALUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluacion.findAll", query = "SELECT e FROM Evaluacion e"),
    @NamedQuery(name = "Evaluacion.findById", query = "SELECT e FROM Evaluacion e WHERE e.id = :id"),
    @NamedQuery(name = "Evaluacion.findByFechaInicio", query = "SELECT e FROM Evaluacion e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Evaluacion.findByFechaFin", query = "SELECT e FROM Evaluacion e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "Evaluacion.findByFechaentre", query = "SELECT e FROM Evaluacion e WHERE e.fechaInicio between :fi and :ff"),
    @NamedQuery(name = "Evaluacion.findByNombreEvaluacion", query = "SELECT e FROM Evaluacion e WHERE e.nombreEvaluacion = :nombreEvaluacion"),
    @NamedQuery(name = "Evaluacion.findByPeso", query = "SELECT e FROM Evaluacion e WHERE e.peso = :peso"),
    @NamedQuery(name = "Evaluacion.findByNivelRevision", query = "SELECT e FROM Evaluacion e WHERE e.nivelRevision = :nivelRevision")})
public class Evaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_EVALUACION")
    private String nombreEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO")
    private BigInteger peso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NIVEL_REVISION")
    private BigInteger nivelRevision;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvaluacion",orphanRemoval=true)
    private List<EvaluacionEmpleado> evaluacionEmpleadoList;
    @JoinColumn(name = "ID_CREADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idCreador;
    @JoinColumn(name = "ID_FORMATO_EVALUACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FormatoEvaluacion idFormatoEvaluacion;
    @JoinColumn(name = "ID_DEPENDENCIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Dependencia idDependencia;

    public Evaluacion() {
    }

    public Evaluacion(BigDecimal id) {
        this.id = id;
    }

    public Evaluacion(BigDecimal id, Date fechaInicio, Date fechaFin, String nombreEvaluacion, BigInteger peso, BigInteger nivelRevision) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreEvaluacion = nombreEvaluacion;
        this.peso = peso;
        this.nivelRevision = nivelRevision;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
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

    public String getNombreEvaluacion() {
        return nombreEvaluacion;
    }

    public void setNombreEvaluacion(String nombreEvaluacion) {
        this.nombreEvaluacion = nombreEvaluacion;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public BigInteger getNivelRevision() {
        return nivelRevision;
    }

    public void setNivelRevision(BigInteger nivelRevision) {
        this.nivelRevision = nivelRevision;
    }

    @XmlTransient
    public List<EvaluacionEmpleado> getEvaluacionEmpleadoList() {
        return evaluacionEmpleadoList;
    }

    public void setEvaluacionEmpleadoList(List<EvaluacionEmpleado> evaluacionEmpleadoList) {
        this.evaluacionEmpleadoList = evaluacionEmpleadoList;
    }

    public Usuario getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Usuario idCreador) {
        this.idCreador = idCreador;
    }

    public FormatoEvaluacion getIdFormatoEvaluacion() {
        return idFormatoEvaluacion;
    }

    public void setIdFormatoEvaluacion(FormatoEvaluacion idFormatoEvaluacion) {
        this.idFormatoEvaluacion = idFormatoEvaluacion;
    }

    public Dependencia getIdDependencia() {
        return idDependencia;
    }

    public void setIdDependencia(Dependencia idDependencia) {
        this.idDependencia = idDependencia;
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
        if (!(object instanceof Evaluacion)) {
            return false;
        }
        Evaluacion other = (Evaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Evaluacion[ id=" + id + " ]";
    }
    
}
