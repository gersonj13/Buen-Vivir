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
@Table(name = "TIPO_EVALUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEvaluacion.findAll", query = "SELECT t FROM TipoEvaluacion t"),
    @NamedQuery(name = "TipoEvaluacion.findById", query = "SELECT t FROM TipoEvaluacion t WHERE t.id = :id"),
    @NamedQuery(name = "TipoEvaluacion.findByIdMetodo", query = "SELECT t FROM TipoEvaluacion t WHERE t.idMetodo = :idMetodo"),
    @NamedQuery(name = "TipoEvaluacion.findByNombre", query = "SELECT t FROM TipoEvaluacion t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoEvaluacion.findByDescripcion", query = "SELECT t FROM TipoEvaluacion t WHERE t.descripcion = :descripcion")})
public class TipoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_METODO")
    private BigInteger idMetodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoEvaluacion")
    private List<EvaluacionEmpleado> evaluacionEmpleadoList;

    public TipoEvaluacion() {
    }

    public TipoEvaluacion(BigDecimal id) {
        this.id = id;
    }

    public TipoEvaluacion(BigDecimal id, BigInteger idMetodo, String nombre, String descripcion) {
        this.id = id;
        this.idMetodo = idMetodo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(BigInteger idMetodo) {
        this.idMetodo = idMetodo;
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

    @XmlTransient
    public List<EvaluacionEmpleado> getEvaluacionEmpleadoList() {
        return evaluacionEmpleadoList;
    }

    public void setEvaluacionEmpleadoList(List<EvaluacionEmpleado> evaluacionEmpleadoList) {
        this.evaluacionEmpleadoList = evaluacionEmpleadoList;
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
        if (!(object instanceof TipoEvaluacion)) {
            return false;
        }
        TipoEvaluacion other = (TipoEvaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.TipoEvaluacion[ id=" + id + " ]";
    }
    
}
