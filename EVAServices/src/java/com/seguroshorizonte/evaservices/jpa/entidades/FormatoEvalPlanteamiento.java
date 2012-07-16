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
@Table(name = "FORMATO_EVAL_PLANTEAMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoEvalPlanteamiento.findAll", query = "SELECT f FROM FormatoEvalPlanteamiento f"),
    @NamedQuery(name = "FormatoEvalPlanteamiento.findById", query = "SELECT f FROM FormatoEvalPlanteamiento f WHERE f.id = :id"),
    @NamedQuery(name = "FormatoEvalPlanteamiento.findById2", query = "SELECT f FROM FormatoEvalPlanteamiento f WHERE f.idPlanteamiento.id = :id and f.idFormatoEvaluacionFactor.id = :id2"),
    @NamedQuery(name = "FormatoEvalPlanteamiento.findByPesoPlanteamientoEscala", query = "SELECT f FROM FormatoEvalPlanteamiento f WHERE f.pesoPlanteamientoEscala = :pesoPlanteamientoEscala")})
public class FormatoEvalPlanteamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO_PLANTEAMIENTO_ESCALA")
    private BigDecimal pesoPlanteamientoEscala;
    @JoinColumn(name = "ID_PLANTEAMIENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Planteamiento idPlanteamiento;
    @JoinColumn(name = "ID_FORMATO_EVALUACION_FACTOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FormatoEvaluacionFactor idFormatoEvaluacionFactor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanteamiento")
    private List<ResultadoFactor> resultadoFactorList;

    public FormatoEvalPlanteamiento() {
    }

    public FormatoEvalPlanteamiento(BigDecimal id) {
        this.id = id;
    }

    public FormatoEvalPlanteamiento(BigDecimal id, BigDecimal pesoPlanteamientoEscala) {
        this.id = id;
        this.pesoPlanteamientoEscala = pesoPlanteamientoEscala;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getPesoPlanteamientoEscala() {
        return pesoPlanteamientoEscala;
    }

    public void setPesoPlanteamientoEscala(BigDecimal pesoPlanteamientoEscala) {
        this.pesoPlanteamientoEscala = pesoPlanteamientoEscala;
    }

    public Planteamiento getIdPlanteamiento() {
        return idPlanteamiento;
    }

    public void setIdPlanteamiento(Planteamiento idPlanteamiento) {
        this.idPlanteamiento = idPlanteamiento;
    }

    public FormatoEvaluacionFactor getIdFormatoEvaluacionFactor() {
        return idFormatoEvaluacionFactor;
    }

    public void setIdFormatoEvaluacionFactor(FormatoEvaluacionFactor idFormatoEvaluacionFactor) {
        this.idFormatoEvaluacionFactor = idFormatoEvaluacionFactor;
    }

    @XmlTransient
    public List<ResultadoFactor> getResultadoFactorList() {
        return resultadoFactorList;
    }

    public void setResultadoFactorList(List<ResultadoFactor> resultadoFactorList) {
        this.resultadoFactorList = resultadoFactorList;
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
        if (!(object instanceof FormatoEvalPlanteamiento)) {
            return false;
        }
        FormatoEvalPlanteamiento other = (FormatoEvalPlanteamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvalPlanteamiento[ id=" + id + " ]";
    }
    
}
