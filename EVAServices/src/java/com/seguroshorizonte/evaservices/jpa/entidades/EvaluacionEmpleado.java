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
@Table(name = "EVALUACION_EMPLEADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionEmpleado.findAll", query = "SELECT e FROM EvaluacionEmpleado e"),
    @NamedQuery(name = "EvaluacionEmpleado.findById", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.id = :id"),
    @NamedQuery(name = "EvaluacionEmpleado.findByIdEVA", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.idEmpleado.id = :ide and e.idEvaluacion.id = :idv "),
    @NamedQuery(name = "EvaluacionEmpleado.resultadoE", query = "SELECT max(e.puntos),min(e.puntos),avg(e.puntos),sum(e.puntos),count(e.puntos) FROM EvaluacionEmpleado e,Empleado m,EstadoEmpleado s  WHERE e.idEmpleado.id=m.id and m.id=s.idEmpleado.id and s.activo=1  and e.idEvaluacion.id = :idv  "),
    @NamedQuery(name = "EvaluacionEmpleado.resultadoT", query = "SELECT count(e.id),e.idTipoEvaluacion.nombre FROM EvaluacionEmpleado e,Empleado m,EstadoEmpleado s  WHERE e.idEmpleado.id=m.id and m.id=s.idEmpleado.id and s.activo=1 and e.idEvaluacion.id = :idv group by e.idTipoEvaluacion.nombre "),
    @NamedQuery(name = "EvaluacionEmpleado.findByIdEvaluacion", query = "SELECT e.idEmpleado.id FROM EvaluacionEmpleado e WHERE e.idEvaluacion.id = :idv "),
    @NamedQuery(name = "EvaluacionEmpleado.findByFechas", query = "SELECT v FROM EvaluacionEmpleado v WHERE v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff"),
    @NamedQuery(name = "EvaluacionEmpleado.findByMaximos", query = "SELECT count(v.id)*1000*v.idEvaluacion.peso FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=1 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff group by v.idEvaluacion.peso"),
    @NamedQuery(name = "EvaluacionEmpleado.findByMaximosPuntos", query = "SELECT sum(v.puntos) FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=1 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff"),
    @NamedQuery(name = "EvaluacionEmpleado.findByMaximosDesincorporados", query = "SELECT count(v.id)*1000*v.idEvaluacion.peso FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=0 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff group by v.idEvaluacion.peso"),
    @NamedQuery(name = "EvaluacionEmpleado.findBySeleccionados", query = "SELECT count(avg(v.puntos/v.idEvaluacion.peso)) FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=1 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff group by v.idEmpleado.id having avg(v.puntos/v.idEvaluacion.peso)>:minimo"),
    @NamedQuery(name = "EvaluacionEmpleado.findBySeleccionadosCorte", query = "SELECT e,sum(v.puntos) FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=1 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff group by e order by e.id"),
    @NamedQuery(name = "EvaluacionEmpleado.findBySeleccionadosCorteC", query = "SELECT e,sum(v.idEvaluacion.peso) FROM EvaluacionEmpleado v, Empleado e, EstadoEmpleado s WHERE v.idEmpleado.id=e.id and e.id=s.idEmpleado.id and s.activo=1 and v.idEvaluacion.fechaInicio>:fi and v.idEvaluacion.fechaInicio<:ff group by e order by e.id"),
    @NamedQuery(name = "EvaluacionEmpleado.findByIdEmpleado", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.idEmpleado.id = :id"),
    @NamedQuery(name = "EvaluacionEmpleado.findByNombreCargo", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.nombreCargo = :nombreCargo"),
    @NamedQuery(name = "EvaluacionEmpleado.findByNombreDependencia", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.nombreDependencia = :nombreDependencia"),
    @NamedQuery(name = "EvaluacionEmpleado.findByPuntos", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.puntos = :puntos"),
    @NamedQuery(name = "EvaluacionEmpleado.findByCerrada", query = "SELECT e FROM EvaluacionEmpleado e WHERE e.cerrada = :cerrada")})
public class EvaluacionEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVALUACION_EMPLEADO_id_seq")
    @SequenceGenerator(name = "EVALUACION_EMPLEADO_id_seq", sequenceName = "EVALUACION_EMPLEADO_id_seq", catalog = "", schema = "EVA", allocationSize = 1)
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_CARGO")
    private String nombreCargo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_DEPENDENCIA")
    private String nombreDependencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTOS")
    private BigInteger puntos;
    @Column(name = "CERRADA")
    private BigInteger cerrada;
    @JoinColumn(name = "ID_TIPO_EVALUACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoEvaluacion idTipoEvaluacion;
    @JoinColumn(name = "ID_EVALUACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Evaluacion idEvaluacion;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @JoinColumn(name = "ID_EVALUADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empleado idEvaluador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvaluacionEmpleado", orphanRemoval = true)
    private List<Revision> revisionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvaluacionEmpleado", orphanRemoval = true)
    private List<ResultadoFactor> resultadoFactorList;

    public EvaluacionEmpleado() {
    }

    public EvaluacionEmpleado(BigDecimal id) {
        this.id = id;
    }

    public EvaluacionEmpleado(BigDecimal id, String nombreCargo, String nombreDependencia, BigInteger puntos) {
        this.id = id;
        this.nombreCargo = nombreCargo;
        this.nombreDependencia = nombreDependencia;
        this.puntos = puntos;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    public BigInteger getPuntos() {
        return puntos;
    }

    public void setPuntos(BigInteger puntos) {
        this.puntos = puntos;
    }

    public BigInteger getCerrada() {
        return cerrada;
    }

    public void setCerrada(BigInteger cerrada) {
        this.cerrada = cerrada;
    }

    public TipoEvaluacion getIdTipoEvaluacion() {
        return idTipoEvaluacion;
    }

    public void setIdTipoEvaluacion(TipoEvaluacion idTipoEvaluacion) {
        this.idTipoEvaluacion = idTipoEvaluacion;
    }

    public Evaluacion getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Evaluacion idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Empleado getIdEvaluador() {
        return idEvaluador;
    }

    public void setIdEvaluador(Empleado idEvaluador) {
        this.idEvaluador = idEvaluador;
    }

    @XmlTransient
    public List<Revision> getRevisionList() {
        return revisionList;
    }

    public void setRevisionList(List<Revision> revisionList) {
        this.revisionList = revisionList;
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
        if (!(object instanceof EvaluacionEmpleado)) {
            return false;
        }
        EvaluacionEmpleado other = (EvaluacionEmpleado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.EvaluacionEmpleado[ id=" + id + " ]";
    }
}
