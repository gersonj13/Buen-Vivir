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
@Table(name = "PLANTEAMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planteamiento.findAll", query = "SELECT p FROM Planteamiento p"),
    @NamedQuery(name = "Planteamiento.findById", query = "SELECT p FROM Planteamiento p WHERE p.id = :id"),
    @NamedQuery(name = "Planteamiento.findByIdPresentacion", query = "SELECT p FROM Planteamiento p WHERE p.idPresentacionFactor.id = :id"),
    @NamedQuery(name = "Planteamiento.findByPlanteamiento", query = "SELECT p FROM Planteamiento p WHERE p.planteamiento = :planteamiento"),
    @NamedQuery(name = "Planteamiento.findByPesoPlanteamiento", query = "SELECT p FROM Planteamiento p WHERE p.pesoPlanteamiento = :pesoPlanteamiento")})
public class Planteamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "PLANTEAMIENTO")
    private String planteamiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO_PLANTEAMIENTO")
    private BigInteger pesoPlanteamiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanteamiento",orphanRemoval=true)
    private List<FormatoEvalPlanteamiento> formatoEvalPlanteamientoList;
    @JoinColumn(name = "ID_PRESENTACION_FACTOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PresentacionFactor idPresentacionFactor;

    public Planteamiento() {
    }

    public Planteamiento(BigDecimal id) {
        this.id = id;
    }

    public Planteamiento(BigDecimal id, String planteamiento, BigInteger pesoPlanteamiento) {
        this.id = id;
        this.planteamiento = planteamiento;
        this.pesoPlanteamiento = pesoPlanteamiento;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getPlanteamiento() {
        return planteamiento;
    }

    public void setPlanteamiento(String planteamiento) {
        this.planteamiento = planteamiento;
    }

    public BigInteger getPesoPlanteamiento() {
        return pesoPlanteamiento;
    }

    public void setPesoPlanteamiento(BigInteger pesoPlanteamiento) {
        this.pesoPlanteamiento = pesoPlanteamiento;
    }

    @XmlTransient
    public List<FormatoEvalPlanteamiento> getFormatoEvalPlanteamientoList() {
        return formatoEvalPlanteamientoList;
    }

    public void setFormatoEvalPlanteamientoList(List<FormatoEvalPlanteamiento> formatoEvalPlanteamientoList) {
        this.formatoEvalPlanteamientoList = formatoEvalPlanteamientoList;
    }

    public PresentacionFactor getIdPresentacionFactor() {
        return idPresentacionFactor;
    }

    public void setIdPresentacionFactor(PresentacionFactor idPresentacionFactor) {
        this.idPresentacionFactor = idPresentacionFactor;
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
        if (!(object instanceof Planteamiento)) {
            return false;
        }
        Planteamiento other = (Planteamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Planteamiento[ id=" + id + " ]";
    }
    
}
