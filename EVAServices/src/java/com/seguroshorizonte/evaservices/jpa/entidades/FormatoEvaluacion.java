/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "FORMATO_EVALUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoEvaluacion.findAll", query = "SELECT f FROM FormatoEvaluacion f"),
    @NamedQuery(name = "FormatoEvaluacion.findById", query = "SELECT f FROM FormatoEvaluacion f WHERE f.id = :id"),
    @NamedQuery(name = "FormatoEvaluacion.findByNombre", query = "SELECT f FROM FormatoEvaluacion f WHERE f.nombre = :nombre"),
    @NamedQuery(name = "FormatoEvaluacion.findByDescripcion", query = "SELECT f FROM FormatoEvaluacion f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FormatoEvaluacion.findByEscala", query = "SELECT f FROM FormatoEvaluacion f WHERE f.escala = :escala")})
public class FormatoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESCALA")
    private BigInteger escala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormatoEvaluacion")
    private List<Evaluacion> evaluacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormatoEvaluacion")
    private List<FormatoEvaluacionFactor> formatoEvaluacionFactorList;

    public FormatoEvaluacion() {
    }

    public FormatoEvaluacion(BigDecimal id) {
        this.id = id;
    }

    public FormatoEvaluacion(BigDecimal id, String nombre, String descripcion, BigInteger escala) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.escala = escala;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getEscala() {
        return escala;
    }

    public void setEscala(BigInteger escala) {
        this.escala = escala;
    }

    @XmlTransient
    public List<Evaluacion> getEvaluacionList() {
        return evaluacionList;
    }

    public void setEvaluacionList(List<Evaluacion> evaluacionList) {
        this.evaluacionList = evaluacionList;
    }

    @XmlTransient
    public List<FormatoEvaluacionFactor> getFormatoEvaluacionFactorList() {
        return formatoEvaluacionFactorList;
    }

    public void setFormatoEvaluacionFactorList(List<FormatoEvaluacionFactor> formatoEvaluacionFactorList) {
        this.formatoEvaluacionFactorList = formatoEvaluacionFactorList;
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
        if (!(object instanceof FormatoEvaluacion)) {
            return false;
        }
        FormatoEvaluacion other = (FormatoEvaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvaluacion[ id=" + id + " ]";
    }
    
}
