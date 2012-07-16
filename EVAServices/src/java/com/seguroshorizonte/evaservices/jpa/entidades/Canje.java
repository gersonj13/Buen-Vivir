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
@Table(name = "CANJE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Canje.findAll", query = "SELECT c FROM Canje c"),
    @NamedQuery(name = "Canje.findById", query = "SELECT c FROM Canje c WHERE c.id = :id"),
    @NamedQuery(name = "Canje.findBySeleccionado", query = "SELECT c FROM Canje c WHERE c.idSeleccionado.id = :id"),
    @NamedQuery(name = "Canje.findByPuntos", query = "SELECT c FROM Canje c WHERE c.puntos = :puntos"),
    @NamedQuery(name = "Canje.findByMontoTasa", query = "SELECT c FROM Canje c WHERE c.montoTasa = :montoTasa"),
    @NamedQuery(name = "Canje.findByIdComercio", query = "SELECT c FROM Canje c WHERE c.idComercio = :idComercio")})
public class Canje implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTOS")
    private BigInteger puntos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_TASA")
    private BigDecimal montoTasa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_COMERCIO")
    private BigInteger idComercio;
    @JoinColumn(name = "ID_SELECCIONADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Seleccionado idSeleccionado;

    public Canje() {
    }

    public Canje(BigDecimal id) {
        this.id = id;
    }

    public Canje(BigDecimal id, BigInteger puntos, BigDecimal montoTasa, BigInteger idComercio) {
        this.id = id;
        this.puntos = puntos;
        this.montoTasa = montoTasa;
        this.idComercio = idComercio;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getPuntos() {
        return puntos;
    }

    public void setPuntos(BigInteger puntos) {
        this.puntos = puntos;
    }

    public BigDecimal getMontoTasa() {
        return montoTasa;
    }

    public void setMontoTasa(BigDecimal montoTasa) {
        this.montoTasa = montoTasa;
    }

    public BigInteger getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(BigInteger idComercio) {
        this.idComercio = idComercio;
    }

    public Seleccionado getIdSeleccionado() {
        return idSeleccionado;
    }

    public void setIdSeleccionado(Seleccionado idSeleccionado) {
        this.idSeleccionado = idSeleccionado;
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
        if (!(object instanceof Canje)) {
            return false;
        }
        Canje other = (Canje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Canje[ id=" + id + " ]";
    }
    
}
