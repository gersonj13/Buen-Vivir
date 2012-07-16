/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Dependencia;
import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.Evaluacion;
import com.seguroshorizonte.evaservices.jpa.entidades.EvaluacionEmpleado;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class EvaluacionFacade extends AbstractFacade<Evaluacion> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluacionFacade() {
        super(Evaluacion.class);
    }

    public Evaluacion buscarNombre(String nombre) {
        Evaluacion e;
        return e = (Evaluacion) em.createNamedQuery("Evaluacion.findByNombreEvaluacion").setParameter("nombreEvaluacion", nombre).getSingleResult();


    }

    public List<Object[]> acumuladoPuntos(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select sum(puntos/c.PESO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID) join estado_empleado s on(e.ID=s.ID_EMPLEADO)").append(" where id_evaluacion in(").append(cadena).append(") and s.activo=1  and (d.id=").append(dep.getId().toString()).append(" or d.ID in(select id from dependencia where ID_DEPENDENCIA_PADRE=").append(dep.getId().toString()).append(")) group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> acumuladoPuntossucursal(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select sum(puntos/c.PESO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID) join estado_empleado s on(e.ID=s.ID_EMPLEADO) ").append(" where id_evaluacion in(").append(cadena).append(") and s.activo=1  and d.id=").append(dep.getId().toString()).append(" group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> acumuladoPuntosEmpleado(List<Evaluacion> eval, Empleado emp) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select puntos,v.ID_EVALUACION from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) join evaluacion c on (v.ID_EVALUACION=c.ID) ").append(" where id_evaluacion in(").append(cadena).append(")  and e.id=").append(emp.getId().toString()).append(" order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> acumuladoPuntosEmpleadoPeso(List<Evaluacion> eval, Empleado emp) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select (puntos/c.peso),v.ID_EVALUACION from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) join evaluacion c on (v.ID_EVALUACION=c.ID) ").append(" where id_evaluacion in(").append(cadena).append(")  and e.id=").append(emp.getId().toString()).append(" order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> promedioPuntos(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select AVG(puntos/c.PESO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID) join estado_empleado s on(e.ID=s.ID_EMPLEADO)").append(" where id_evaluacion in(").append(cadena).append(") and s.activo=1   and (d.id=").append(dep.getId().toString()).append(" or d.ID in(select id from dependencia where ID_DEPENDENCIA_PADRE=").append(dep.getId().toString()).append(")) group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> promedioPuntosEmpleado(List<Evaluacion> eval, Empleado emp) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        int cont = 0;
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }
        for (int i = 0; i < eval.size(); i++) {

            if (Integer.parseInt(eval.get(i).getPeso().toString()) > 1) {
                cont += Integer.parseInt(eval.get(i).getPeso().toString());
            } else {
                cont++;
            }

        }


        StringBuffer s = new StringBuffer("select sum(puntos/(select sum(h.peso) from evaluacion h join evaluacion_empleado i on (h.ID=i.ID_EVALUACION) where i.ID_EMPLEADO=").append(emp.getId().toString()).append(" and h.id in (" )
                .append(cadena).append(" )) )").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").append("join evaluacion c on (v.ID_EVALUACION=c.ID)").append(" where id_evaluacion in(").append(cadena).append(") and v.id_empleado=").append(emp.getId().toString()).append(" order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> promedioPuntossucursal(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select AVG(puntos/c.PESO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID) join estado_empleado s on(e.ID=s.ID_EMPLEADO)").append(" where id_evaluacion in(").append(cadena).append(") and s.activo=1   and d.id=").append(dep.getId().toString()).append(" group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> cantidadEmpleados(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select count(v.ID_EMPLEADO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").
                append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID)").append(" where id_evaluacion in(").append(cadena).append(")  and (d.id=").append(dep.getId().toString()).append(" or d.ID in(select id from dependencia where ID_DEPENDENCIA_PADRE=").append(dep.getId().toString()).append(")) group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public List<Object[]> cantidadEmpleadossucursal(List<Evaluacion> eval, Dependencia dep) {
        List<Object[]> res = null;
        String cadena = eval.get(0).getId().toString();
        for (int i = 1; i < eval.size(); i++) {

            cadena += "," + eval.get(i).getId().toString();

        }

        StringBuffer s = new StringBuffer("select count(v.ID_EMPLEADO),v.ID_EVALUACION").append(" from EVA.EVALUACION_EMPLEADO v join empleado e on(v.ID_EMPLEADO=e.ID) ").
                append(" join dependencia d on(d.ID=e.ID_DEPENDENCIA) join evaluacion c on (v.ID_EVALUACION=c.ID) join estado_empleado s on(e.ID=s.ID_EMPLEADO) ").append(" where id_evaluacion in(").append(cadena).append(") and s.activo=1  and d.id=").append(dep.getId().toString()).append(" group by v.ID_EVALUACION order by v.ID_EVALUACION");
        try {
            res = em.createNativeQuery(s.toString()).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public Object cantidadEmpleadosdependencia(Dependencia dep) {
        Object res = null;

        StringBuffer s = new StringBuffer("select  count(e.id) from empleado e  join dependencia d on(d.ID=e.ID_DEPENDENCIA) ").append(" where (d.id=").append(dep.getId().toString()).append(" or d.ID in(select id from dependencia where ID_DEPENDENCIA_PADRE=").append(dep.getId().toString()).append("))");
        try {
            res = em.createNativeQuery(s.toString()).getSingleResult();

        } catch (Exception e) {
            return null;
        }

        return res;
    }

    public Object cantidadEmpleadosdependenciasucursal(Dependencia dep) {
        Object res = null;

        StringBuffer s = new StringBuffer("select count(e.id) from empleado e  join dependencia d on(d.ID=e.ID_DEPENDENCIA) join estado_empleado s on(e.ID=s.ID_EMPLEADO)").append(" where s.activo=1 and d.id=").append(dep.getId().toString()).append(" ");
        try {
            res = em.createNativeQuery(s.toString()).getSingleResult();

        } catch (Exception e) {
            return null;
        }

        return res;
    }
}
