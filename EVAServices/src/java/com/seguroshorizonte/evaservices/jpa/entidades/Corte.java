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
@Table(name = "CORTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Corte.findAll", query = "SELECT c FROM Corte c"),
    @NamedQuery(name = "Corte.findMaximo", query = "SELECT c FROM Corte c where c.id=(select Max(d.id) from Corte d)"),
    @NamedQuery(name = "Corte.findById", query = "SELECT c FROM Corte c WHERE c.id = :id"),
    @NamedQuery(name = "Corte.findBySaldoInicial", query = "SELECT c FROM Corte c WHERE c.saldoInicial = :saldoInicial"),
    @NamedQuery(name = "Corte.findBySaldoIngreso", query = "SELECT c FROM Corte c WHERE c.saldoIngreso = :saldoIngreso"),
    @NamedQuery(name = "Corte.findBySaldoCorte", query = "SELECT c FROM Corte c WHERE c.saldoCorte = :saldoCorte"),
    @NamedQuery(name = "Corte.findByMinimoPromedioPuntos", query = "SELECT c FROM Corte c WHERE c.minimoPromedioPuntos = :minimoPromedioPuntos"),
    @NamedQuery(name = "Corte.findByTasaCambio", query = "SELECT c FROM Corte c WHERE c.tasaCambio = :tasaCambio")})
public class Corte implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_INICIAL")
    private BigDecimal saldoInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_INGRESO")
    private BigDecimal saldoIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_CORTE")
    private BigDecimal saldoCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINIMO_PROMEDIO_PUNTOS")
    private BigDecimal minimoPromedioPuntos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TASA_CAMBIO")
    private BigDecimal tasaCambio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCorte")
    private List<Seleccionado> seleccionadoList;
    @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Periodo idPeriodo;

    public Corte() {
    }

    public Corte(BigDecimal id) {
        this.id = id;
    }

    public Corte(BigDecimal id, BigDecimal saldoInicial, BigDecimal saldoIngreso, BigDecimal saldoCorte, BigDecimal minimoPromedioPuntos, BigDecimal tasaCambio) {
        this.id = id;
        this.saldoInicial = saldoInicial;
        this.saldoIngreso = saldoIngreso;
        this.saldoCorte = saldoCorte;
        this.minimoPromedioPuntos = minimoPromedioPuntos;
        this.tasaCambio = tasaCambio;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoIngreso() {
        return saldoIngreso;
    }

    public void setSaldoIngreso(BigDecimal saldoIngreso) {
        this.saldoIngreso = saldoIngreso;
    }

    public BigDecimal getSaldoCorte() {
        return saldoCorte;
    }

    public void setSaldoCorte(BigDecimal saldoCorte) {
        this.saldoCorte = saldoCorte;
    }

    public BigDecimal getMinimoPromedioPuntos() {
        return minimoPromedioPuntos;
    }

    public void setMinimoPromedioPuntos(BigDecimal minimoPromedioPuntos) {
        this.minimoPromedioPuntos = minimoPromedioPuntos;
    }

    public BigDecimal getTasaCambio() {
        return tasaCambio;
    }

    public void setTasaCambio(BigDecimal tasaCambio) {
        this.tasaCambio = tasaCambio;
    }

    @XmlTransient
    public List<Seleccionado> getSeleccionadoList() {
        return seleccionadoList;
    }

    public void setSeleccionadoList(List<Seleccionado> seleccionadoList) {
        this.seleccionadoList = seleccionadoList;
    }

    public Periodo getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Periodo idPeriodo) {
        this.idPeriodo = idPeriodo;
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
        if (!(object instanceof Corte)) {
            return false;
        }
        Corte other = (Corte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Corte[ id=" + id + " ]";
    }
}
