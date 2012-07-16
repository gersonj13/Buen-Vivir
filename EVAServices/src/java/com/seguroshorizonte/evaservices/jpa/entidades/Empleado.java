/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
@Table(name = "EMPLEADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e order by e.nombres asc"),
    @NamedQuery(name = "Empleado.findById", query = "SELECT e FROM Empleado e WHERE e.id = :id"),
    @NamedQuery(name = "Empleado.findByIdc", query = "SELECT e FROM Empleado e,EmpleadoSupervisor s  WHERE  e.id=s.idEmpleado.id and e.fechaIngreso<(select v.fechaInicio from Evaluacion v where v.id = :idvx) and e.id not in (select p.idEmpleado.id from EvaluacionEmpleado p where p.idEvaluacion.id = :idv) order by  e.idDependencia.descripcion asc, trim(s.idSupervisor.nombres) asc"),
    @NamedQuery(name = "Empleado.findByIdcG", query = "SELECT e FROM Empleado e,EmpleadoSupervisor s,Dependencia d  WHERE  e.id=s.idEmpleado.id and e.idDependencia.id=d.id and e.fechaIngreso<(select v.fechaInicio from Evaluacion v where v.id = :idvx) and e.id not in (select p.idEmpleado.id from EvaluacionEmpleado p where p.idEvaluacion.id = :idv) and (d.id=:idd or d.idDependenciaPadre.id=:idg) order by  e.idDependencia.descripcion asc, trim(s.idSupervisor.nombres) asc"),
    @NamedQuery(name = "Empleado.findByHistorico", query = "SELECT e FROM Empleado e  order by  e.idDependencia.descripcion asc,trim(e.nombres) asc,trim(e.apellidos) asc"),
    @NamedQuery(name = "Empleado.findByNroIdentificacion", query = "SELECT e FROM Empleado e WHERE e.nroIdentificacion = :nroIdentificacion"),
    @NamedQuery(name = "Empleado.findByGrupo", query = "SELECT e FROM Empleado e where (e.idDependencia.id in :c1  or e.idDependencia.id in (select d.id from Dependencia d where d.idDependenciaPadre.id in :c2) ) and e.id in(select p.idEmpleado.id from EvaluacionEmpleado p) order by trim(e.nombres) asc,trim(e.apellidos) asc "),
    @NamedQuery(name = "Empleado.findByGrupoE", query = "SELECT e FROM Empleado e where e.idDependencia.id in :c1 and e.id in(select p.idEmpleado.id from EvaluacionEmpleado p) order by trim(e.nombres) asc,trim(e.apellidos) asc "),
    @NamedQuery(name = "Empleado.findByTipoIdentificacion", query = "SELECT e FROM Empleado e WHERE e.tipoIdentificacion = :tipoIdentificacion"),
    @NamedQuery(name = "Empleado.findByNombres", query = "SELECT e FROM Empleado e WHERE e.nombres = :nombres"),
    @NamedQuery(name = "Empleado.findByApellidos", query = "SELECT e FROM Empleado e WHERE e.apellidos = :apellidos"),
    @NamedQuery(name = "Empleado.findByCodigo", query = "SELECT e FROM Empleado e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "Empleado.findByFechaIngreso", query = "SELECT e FROM Empleado e WHERE e.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Empleado.findByFechaNacimiento", query = "SELECT e FROM Empleado e WHERE e.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Empleado.findByDep", query = "SELECT e FROM Empleado e WHERE e.idDependencia.id = :dep and e.nombres like :nom and e.apellidos like :ape and e.nroIdentificacion like :ced order by trim(e.nombres) asc"),
    @NamedQuery(name = "Empleado.findByCorreo", query = "SELECT e FROM Empleado e WHERE LOWER(e.correo) = :correo"),
    @NamedQuery(name = "Empleado.findByEliminado", query = "SELECT e FROM Empleado e,EstadoEmpleado s WHERE e.id=s.idEmpleado.id and s.activo=0"),
    @NamedQuery(name = "Empleado.findByObservation", query = "SELECT e FROM Empleado e WHERE e.observation = :observation")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_IDENTIFICACION")
    private BigInteger nroIdentificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_IDENTIFICACION")
    private char tipoIdentificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRES")
    private String nombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Size(max = 100)
    @Column(name = "CORREO")
    private String correo;
    @Size(max = 200)
    @Column(name = "OBSERVATION")
    private String observation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado", orphanRemoval = true)
    private List<EvaluacionEmpleado> evaluacionEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvaluador", orphanRemoval = true)
    private List<EvaluacionEmpleado> evaluacionEmpleadoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado", orphanRemoval = true)
    private List<EmpleadoOpcion> empleadoOpcionList;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @OneToOne
    private Usuario idUsuario;
    @JoinColumn(name = "ID_DEPENDENCIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Dependencia idDependencia;
    @JoinColumn(name = "ID_CARGO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cargo idCargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSupervisor", orphanRemoval = true)
    private List<EmpleadoSupervisor> empleadoSupervisorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado", orphanRemoval = true)
    private List<EmpleadoSupervisor> empleadoSupervisorList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRevisor", orphanRemoval = true)
    private List<Revision> revisionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado", orphanRemoval = true)
    private List<Seleccionado> seleccionadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado", orphanRemoval = true)
    private List<EstadoEmpleado> estadoEmpleadoList;

    public Empleado() {
    }

    public Empleado(BigDecimal id) {
        this.id = id;
    }

    public Empleado(BigDecimal id, BigInteger nroIdentificacion, char tipoIdentificacion, String nombres, String apellidos, String codigo, Date fechaIngreso, Date fechaNacimiento) {
        this.id = id;
        this.nroIdentificacion = nroIdentificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.codigo = codigo;
        this.fechaIngreso = fechaIngreso;
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getNroIdentificacion() {
        return nroIdentificacion;
    }

    public void setNroIdentificacion(BigInteger nroIdentificacion) {
        this.nroIdentificacion = nroIdentificacion;
    }

    public char getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(char tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @XmlTransient
    public List<EvaluacionEmpleado> getEvaluacionEmpleadoList() {
        return evaluacionEmpleadoList;
    }

    public void setEvaluacionEmpleadoList(List<EvaluacionEmpleado> evaluacionEmpleadoList) {
        this.evaluacionEmpleadoList = evaluacionEmpleadoList;
    }

    @XmlTransient
    public List<EvaluacionEmpleado> getEvaluacionEmpleadoList1() {
        return evaluacionEmpleadoList1;
    }

    public void setEvaluacionEmpleadoList1(List<EvaluacionEmpleado> evaluacionEmpleadoList1) {
        this.evaluacionEmpleadoList1 = evaluacionEmpleadoList1;
    }

    @XmlTransient
    public List<EmpleadoOpcion> getEmpleadoOpcionList() {
        return empleadoOpcionList;
    }

    public void setEmpleadoOpcionList(List<EmpleadoOpcion> empleadoOpcionList) {
        this.empleadoOpcionList = empleadoOpcionList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Dependencia getIdDependencia() {
        return idDependencia;
    }

    public void setIdDependencia(Dependencia idDependencia) {
        this.idDependencia = idDependencia;
    }

    public Cargo getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Cargo idCargo) {
        this.idCargo = idCargo;
    }

    @XmlTransient
    public List<EmpleadoSupervisor> getEmpleadoSupervisorList() {
        return empleadoSupervisorList;
    }

    public void setEmpleadoSupervisorList(List<EmpleadoSupervisor> empleadoSupervisorList) {
        this.empleadoSupervisorList = empleadoSupervisorList;
    }

    @XmlTransient
    public List<EmpleadoSupervisor> getEmpleadoSupervisorList1() {
        return empleadoSupervisorList1;
    }

    public void setEmpleadoSupervisorList1(List<EmpleadoSupervisor> empleadoSupervisorList1) {
        this.empleadoSupervisorList1 = empleadoSupervisorList1;
    }

    @XmlTransient
    public List<Revision> getRevisionList() {
        return revisionList;
    }

    public void setRevisionList(List<Revision> revisionList) {
        this.revisionList = revisionList;
    }

    @XmlTransient
    public List<Seleccionado> getSeleccionadoList() {
        return seleccionadoList;
    }

    public void setSeleccionadoList(List<Seleccionado> seleccionadoList) {
        this.seleccionadoList = seleccionadoList;
    }

    @XmlTransient
    public List<EstadoEmpleado> getEstadoEmpleadoList() {
        return estadoEmpleadoList;
    }

    public void setEstadoEmpleadoList(List<EstadoEmpleado> estadoEmpleadoList) {
        this.estadoEmpleadoList = estadoEmpleadoList;
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
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seguroshorizonte.evaservices.jpa.entidades.Empleado[ id=" + id + " ]";
    }
}
