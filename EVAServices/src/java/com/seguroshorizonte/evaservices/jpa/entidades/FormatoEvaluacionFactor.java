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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "FORMATO_EVALUACION_FACTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoEvaluacionFactor.findAll", query = "SELECT f FROM FormatoEvaluacionFactor f"),
    @NamedQuery(name = "FormatoEvaluacionFactor.findById", query = "SELECT f FROM FormatoEvaluacionFactor f WHERE f.id = :id"),
    @NamedQuery(name = "FormatoEvaluacionFactor.findByIdFormato", query = "SELECT f FROM FormatoEvaluacionFactor f WHERE f.idFormatoEvaluacion.id = :id"),
    @NamedQuery(name = "FormatoEvaluacionFactor.findByIdFormatoPre", query = "SELECT f.idPresentacionFactor FROM FormatoEvaluacionFactor f WHERE f.idFormatoEvaluacion.id = :id"),
    @NamedQuery(name = "FormatoEvaluacionFactor.findByIdFP", query = "SELECT f FROM FormatoEvaluacionFactor f WHERE f.idFormatoEvaluacion.id = :id1 and f.idPresentacionFactor.id = :id2"),
    @NamedQuery(name = "FormatoEvaluacionFactor.findByPesoFactorEscala", query = "SELECT f FROM FormatoEvaluacionFactor f WHERE f.pesoFactorEscala = :pesoFactorEscala")})
public class FormatoEvaluacionFactor implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO_FACTOR_ESCALA")
    private BigInteger pesoFactorEscala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormatoEvaluacionFactor", orphanRemoval = true)
    private List<FormatoEvalPlanteamiento> formatoEvalPlanteamientoList;
    @JoinColumn(name = "ID_PRESENTACION_FACTOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PresentacionFactor idPresentacionFactor;
    @JoinColumn(name = "ID_FORMATO_EVALUACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FormatoEvaluacion idFormatoEvaluacion;

    public FormatoEvaluacionFactor() {
    }

    public FormatoEvaluacionFactor(BigDecimal id) {
        this.id = id;
    }

    public FormatoEvaluacionFactor(BigDecimal id, BigInteger pesoFactorEscala) {
        this.id = id;
        this.pesoFactorEscala = pesoFactorEscala;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getPesoFactorEscala() {
        return pesoFactorEscala;
    }

    public void setPesoFactorEscala(BigInteger pesoFactorEscala) {
        this.pesoFactorEscala = pesoFactorEscala;
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

    public FormatoEvaluacion getIdFormatoEvaluacion() {
        return idFormatoEvaluacion;
    }

    public void setIdFormatoEvaluacion(FormatoEvaluacion idFormatoEvaluacion) {
        this.idFormatoEvaluacion = idFormatoEvaluacion;
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
        if (!(object instanceof FormatoEvaluacionFactor)) {
            return false;
        }
        FormatoEvaluacionFactor other = (FormatoEvaluacionFactor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvaluacionFactor[ id=" + id + " ]";
    }
}
