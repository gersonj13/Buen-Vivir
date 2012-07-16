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
@Table(name = "SELECCIONADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seleccionado.findAll", query = "SELECT s FROM Seleccionado s"),
    @NamedQuery(name = "Seleccionado.findById", query = "SELECT s FROM Seleccionado s WHERE s.id = :id"),
    @NamedQuery(name = "Seleccionado.findByIdCorte", query = "SELECT s FROM Seleccionado s WHERE s.idCorte.id = :id and s.idEmpleado.nombres like :nom and s.idEmpleado.apellidos like :ape and s.idEmpleado.nroIdentificacion like :ced order by trim(s.idEmpleado.nombres) asc"),
    @NamedQuery(name = "Seleccionado.findByPromedioPuntos", query = "SELECT s FROM Seleccionado s WHERE s.promedioPuntos = :promedioPuntos"),
    @NamedQuery(name = "Seleccionado.findByPuntosAcumulados", query = "SELECT s FROM Seleccionado s WHERE s.puntosAcumulados = :puntosAcumulados"),
    @NamedQuery(name = "Seleccionado.findByPuntosDisponibles", query = "SELECT s FROM Seleccionado s WHERE s.puntosDisponibles = :puntosDisponibles"),
    @NamedQuery(name = "Seleccionado.findBySaldoInicialCorte", query = "SELECT s FROM Seleccionado s WHERE s.saldoInicialCorte = :saldoInicialCorte"),
    @NamedQuery(name = "Seleccionado.findBySaldoFinCorte", query = "SELECT s FROM Seleccionado s WHERE s.saldoFinCorte = :saldoFinCorte"),
    @NamedQuery(name = "Seleccionado.findByTotalCanjesCorte", query = "SELECT s FROM Seleccionado s WHERE s.totalCanjesCorte = :totalCanjesCorte")})
public class Seleccionado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROMEDIO_PUNTOS")
    private BigDecimal promedioPuntos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTOS_ACUMULADOS")
    private BigInteger puntosAcumulados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTOS_DISPONIBLES")
    private BigInteger puntosDisponibles;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_INICIAL_CORTE")
    private BigInteger saldoInicialCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_FIN_CORTE")
    private BigInteger saldoFinCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_CANJES_CORTE")
    private BigInteger totalCanjesCorte;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSeleccionado")
    private List<Canje> canjeList;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @JoinColumn(name = "ID_CORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Corte idCorte;

    public Seleccionado() {
    }

    public Seleccionado(BigDecimal id) {
        this.id = id;
    }

    public Seleccionado(BigDecimal id, BigDecimal promedioPuntos, BigInteger puntosAcumulados, BigInteger puntosDisponibles, BigInteger saldoInicialCorte, BigInteger saldoFinCorte, BigInteger totalCanjesCorte) {
        this.id = id;
        this.promedioPuntos = promedioPuntos;
        this.puntosAcumulados = puntosAcumulados;
        this.puntosDisponibles = puntosDisponibles;
        this.saldoInicialCorte = saldoInicialCorte;
        this.saldoFinCorte = saldoFinCorte;
        this.totalCanjesCorte = totalCanjesCorte;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getPromedioPuntos() {
        return promedioPuntos;
    }

    public void setPromedioPuntos(BigDecimal promedioPuntos) {
        this.promedioPuntos = promedioPuntos;
    }

    public BigInteger getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(BigInteger puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public BigInteger getPuntosDisponibles() {
        return puntosDisponibles;
    }

    public void setPuntosDisponibles(BigInteger puntosDisponibles) {
        this.puntosDisponibles = puntosDisponibles;
    }

    public BigInteger getSaldoInicialCorte() {
        return saldoInicialCorte;
    }

    public void setSaldoInicialCorte(BigInteger saldoInicialCorte) {
        this.saldoInicialCorte = saldoInicialCorte;
    }

    public BigInteger getSaldoFinCorte() {
        return saldoFinCorte;
    }

    public void setSaldoFinCorte(BigInteger saldoFinCorte) {
        this.saldoFinCorte = saldoFinCorte;
    }

    public BigInteger getTotalCanjesCorte() {
        return totalCanjesCorte;
    }

    public void setTotalCanjesCorte(BigInteger totalCanjesCorte) {
        this.totalCanjesCorte = totalCanjesCorte;
    }

    @XmlTransient
    public List<Canje> getCanjeList() {
        return canjeList;
    }

    public void setCanjeList(List<Canje> canjeList) {
        this.canjeList = canjeList;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Corte getIdCorte() {
        return idCorte;
    }

    public void setIdCorte(Corte idCorte) {
        this.idCorte = idCorte;
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
        if (!(object instanceof Seleccionado)) {
            return false;
        }
        Seleccionado other = (Seleccionado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Seleccionado[ id=" + id + " ]";
    }
    
}
