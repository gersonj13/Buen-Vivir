/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "RESULTADO_FACTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResultadoFactor.findAll", query = "SELECT r FROM ResultadoFactor r"),
    @NamedQuery(name = "ResultadoFactor.findById", query = "SELECT r FROM ResultadoFactor r WHERE r.id = :id"),
    @NamedQuery(name = "ResultadoFactor.findByPuntos", query = "SELECT r FROM ResultadoFactor r WHERE r.puntos = :puntos")})
public class ResultadoFactor implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESULTADO_FACTOR_id_seq")
    @SequenceGenerator(name = "RESULTADO_FACTOR_id_seq", sequenceName = "RESULTADO_FACTOR_id_seq", catalog = "", schema = "EVA", allocationSize = 1)
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTOS")
    private BigDecimal puntos;
    @JoinColumn(name = "ID_PLANTEAMIENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FormatoEvalPlanteamiento idPlanteamiento;
    @JoinColumn(name = "ID_EVALUACION_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EvaluacionEmpleado idEvaluacionEmpleado;

    public ResultadoFactor() {
    }

    public ResultadoFactor(BigDecimal id) {
        this.id = id;
    }

    public ResultadoFactor(BigDecimal id, BigDecimal puntos) {
        this.id = id;
        this.puntos = puntos;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getPuntos() {
        return puntos;
    }

    public void setPuntos(BigDecimal puntos) {
        this.puntos = puntos;
    }

    public FormatoEvalPlanteamiento getIdPlanteamiento() {
        return idPlanteamiento;
    }

    public void setIdPlanteamiento(FormatoEvalPlanteamiento idPlanteamiento) {
        this.idPlanteamiento = idPlanteamiento;
    }

    public EvaluacionEmpleado getIdEvaluacionEmpleado() {
        return idEvaluacionEmpleado;
    }

    public void setIdEvaluacionEmpleado(EvaluacionEmpleado idEvaluacionEmpleado) {
        this.idEvaluacionEmpleado = idEvaluacionEmpleado;
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
        if (!(object instanceof ResultadoFactor)) {
            return false;
        }
        ResultadoFactor other = (ResultadoFactor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.ResultadoFactor[ id=" + id + " ]";
    }
}
