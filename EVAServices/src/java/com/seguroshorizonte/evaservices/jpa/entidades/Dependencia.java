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
@Table(name = "DEPENDENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dependencia.findAll", query = "SELECT d FROM Dependencia d"),
    @NamedQuery(name = "Dependencia.findById", query = "SELECT d FROM Dependencia d WHERE d.id = :id"),
    @NamedQuery(name = "Dependencia.findByIdc", query = "SELECT d FROM Dependencia d,Empleado e,EmpleadoSupervisor s  WHERE e.idDependencia.id=d.id and e.id=s.idEmpleado.id and e.id not in (select p.idEmpleado.id from EvaluacionEmpleado p where p.idEvaluacion.id = :idv) order by d.descripcion  asc, trim(s.idSupervisor.nombres) asc"),
    @NamedQuery(name = "Dependencia.findByIdcG", query = "SELECT d FROM Dependencia d,Empleado e,EmpleadoSupervisor s  WHERE e.idDependencia.id=d.id and e.id=s.idEmpleado.id and e.id not in (select p.idEmpleado.id from EvaluacionEmpleado p where p.idEvaluacion.id = :idv) and (e.idDependencia.id=:idd or e.idDependencia.idDependenciaPadre.id=:idg) order by d.descripcion  asc, trim(s.idSupervisor.nombres) asc"),
    @NamedQuery(name = "Dependencia.findByHistorico", query = "SELECT d FROM Dependencia d,Empleado e WHERE e.idDependencia.id=d.id order by d.descripcion  asc,trim(e.nombres) asc,trim(e.apellidos) asc"),
    @NamedQuery(name = "Dependencia.findByNombre", query = "SELECT d FROM Dependencia d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "Dependencia.findByDescripcion", query = "SELECT d FROM Dependencia d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Dependencia.findByNivel", query = "SELECT d FROM Dependencia d WHERE d.nivel = :nivel")})
public class Dependencia implements Serializable {
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
    @Column(name = "NIVEL")
    private BigInteger nivel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDependencia")
    private List<Empleado> empleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDependencia")
    private List<Evaluacion> evaluacionList;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "idDependenciaPadre",orphanRemoval=true)
    private List<Dependencia> dependenciaList;
    @JoinColumn(name = "ID_DEPENDENCIA_PADRE", referencedColumnName = "ID")
    @ManyToOne
    private Dependencia idDependenciaPadre;

    public Dependencia() {
    }

    public Dependencia(BigDecimal id) {
        this.id = id;
    }

    public Dependencia(BigDecimal id, String nombre, String descripcion, BigInteger nivel) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivel = nivel;
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

    public BigInteger getNivel() {
        return nivel;
    }

    public void setNivel(BigInteger nivel) {
        this.nivel = nivel;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @XmlTransient
    public List<Evaluacion> getEvaluacionList() {
        return evaluacionList;
    }

    public void setEvaluacionList(List<Evaluacion> evaluacionList) {
        this.evaluacionList = evaluacionList;
    }

    @XmlTransient
    public List<Dependencia> getDependenciaList() {
        return dependenciaList;
    }

    public void setDependenciaList(List<Dependencia> dependenciaList) {
        this.dependenciaList = dependenciaList;
    }

    public Dependencia getIdDependenciaPadre() {
        return idDependenciaPadre;
    }

    public void setIdDependenciaPadre(Dependencia idDependenciaPadre) {
        this.idDependenciaPadre = idDependenciaPadre;
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
        if (!(object instanceof Dependencia)) {
            return false;
        }
        Dependencia other = (Dependencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Dependencia[ id=" + id + " ]";
    }
    
}
