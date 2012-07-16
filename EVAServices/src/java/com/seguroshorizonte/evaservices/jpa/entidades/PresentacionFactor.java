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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "PRESENTACION_FACTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PresentacionFactor.findAll", query = "SELECT p FROM PresentacionFactor p"),
    @NamedQuery(name = "PresentacionFactor.findById", query = "SELECT p FROM PresentacionFactor p WHERE p.id = :id"),
    @NamedQuery(name = "PresentacionFactor.findByIdFactor", query = "SELECT p FROM PresentacionFactor p WHERE p.idFactor.id = :id"),
    @NamedQuery(name = "PresentacionFactor.findByDescripcionPresentacion", query = "SELECT p FROM PresentacionFactor p WHERE p.descripcionPresentacion = :descripcionPresentacion")})
public class PresentacionFactor implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DESCRIPCION_PRESENTACION")
    private String descripcionPresentacion;
    @JoinColumn(name = "ID_FACTOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Factor idFactor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPresentacionFactor",orphanRemoval=true)
    private List<Planteamiento> planteamientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPresentacionFactor")
    private List<FormatoEvaluacionFactor> formatoEvaluacionFactorList;

    public PresentacionFactor() {
    }

    public PresentacionFactor(BigDecimal id) {
        this.id = id;
    }

    public PresentacionFactor(BigDecimal id, String descripcionPresentacion) {
        this.id = id;
        this.descripcionPresentacion = descripcionPresentacion;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDescripcionPresentacion() {
        return descripcionPresentacion;
    }

    public void setDescripcionPresentacion(String descripcionPresentacion) {
        this.descripcionPresentacion = descripcionPresentacion;
    }

    public Factor getIdFactor() {
        return idFactor;
    }

    public void setIdFactor(Factor idFactor) {
        this.idFactor = idFactor;
    }

    @XmlTransient
    public List<Planteamiento> getPlanteamientoList() {
        return planteamientoList;
    }

    public void setPlanteamientoList(List<Planteamiento> planteamientoList) {
        this.planteamientoList = planteamientoList;
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
        if (!(object instanceof PresentacionFactor)) {
            return false;
        }
        PresentacionFactor other = (PresentacionFactor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.PresentacionFactor[ id=" + id + " ]";
    }
}
