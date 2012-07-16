/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@Entity
@Table(name = "REVISION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Revision.findAll", query = "SELECT r FROM Revision r"),
    @NamedQuery(name = "Revision.findById", query = "SELECT r FROM Revision r WHERE r.id = :id"),
    @NamedQuery(name = "Revision.findByCargoRevisor", query = "SELECT r FROM Revision r WHERE r.cargoRevisor = :cargoRevisor")})
public class Revision implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVISION_id_seq")
    @SequenceGenerator(name = "REVISION_id_seq", sequenceName = "REVISION_id_seq", catalog = "", schema = "EVA", allocationSize = 1)
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CARGO_REVISOR")
    private String cargoRevisor;
    @JoinColumn(name = "ID_EVALUACION_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EvaluacionEmpleado idEvaluacionEmpleado;
    @JoinColumn(name = "ID_REVISOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idRevisor;

    public Revision() {
    }

    public Revision(BigDecimal id) {
        this.id = id;
    }

    public Revision(BigDecimal id, String cargoRevisor) {
        this.id = id;
        this.cargoRevisor = cargoRevisor;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCargoRevisor() {
        return cargoRevisor;
    }

    public void setCargoRevisor(String cargoRevisor) {
        this.cargoRevisor = cargoRevisor;
    }

    public EvaluacionEmpleado getIdEvaluacionEmpleado() {
        return idEvaluacionEmpleado;
    }

    public void setIdEvaluacionEmpleado(EvaluacionEmpleado idEvaluacionEmpleado) {
        this.idEvaluacionEmpleado = idEvaluacionEmpleado;
    }

    public Empleado getIdRevisor() {
        return idRevisor;
    }

    public void setIdRevisor(Empleado idRevisor) {
        this.idRevisor = idRevisor;
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
        if (!(object instanceof Revision)) {
            return false;
        }
        Revision other = (Revision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Revision[ id=" + id + " ]";
    }
}
