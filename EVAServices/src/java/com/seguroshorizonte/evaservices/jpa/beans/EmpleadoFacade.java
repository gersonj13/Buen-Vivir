/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Dependencia;
import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class EmpleadoFacade extends AbstractFacade<Empleado> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadoFacade() {
        super(Empleado.class);
    }

    public Empleado buscarMail(String mail) {

        Empleado emp;
        try {
            emp = (Empleado) em.createNamedQuery("Empleado.findByCorreo").setParameter("correo", mail.toLowerCase()).getSingleResult();

        } catch (Exception e) {
            return null;
        }

        return emp;
    }

    public List<Empleado> buscarDependencia(String dependencia, String filtron, String filtroa, String filtroc) {

        List<Empleado> emp;
        emp = (List<Empleado>) em.createNamedQuery("Empleado.findByDep").setParameter("dep", new BigDecimal(dependencia)).setParameter("nom", "%" + filtron.toUpperCase() + "%").setParameter("ape", "%" + filtroa.toUpperCase() + "%").setParameter("ced", "%" + filtroc.toUpperCase() + "%").getResultList();
        return emp;
    }

    public List<Empleado> buscarTodos(BigDecimal idv) {

        List<Empleado> emp;


        emp = (List<Empleado>) em.createNamedQuery("Empleado.findByIdc").setParameter("idvx", idv).setParameter("idv", idv).getResultList();
        return emp;

    }
    public List<Empleado> buscarTodosGerentes(BigDecimal idv,BigDecimal idg) {

        List<Empleado> emp;


        emp = (List<Empleado>) em.createNamedQuery("Empleado.findByIdcG").setParameter("idvx", idv).setParameter("idv", idv).setParameter("idd",idg).setParameter("idg",idg).getResultList();
        return emp;

    }

    public List<Empleado> buscarTodosHistorico() {

        List<Empleado> emp;


        emp = (List<Empleado>) em.createNamedQuery("Empleado.findByHistorico").getResultList();
        return emp;

    }

    public List<Empleado> buscarGrupoDependenciaG(List<Dependencia> dep) {
        List<Empleado> res = null;

        BigDecimal[] cadena = new BigDecimal[dep.size()];

        for (int i = 0; i < dep.size(); i++) {

            cadena[i] = dep.get(i).getId();

        }
        try {
            res = em.createNamedQuery("Empleado.findByGrupo").setParameter("c1", Arrays.asList(cadena)).setParameter("c2", Arrays.asList(cadena)).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;


    }

    public List<Empleado> buscarGrupoDependenciaD(List<Dependencia> dep) {
        List<Empleado> res = null;
         BigDecimal[] cadena = new BigDecimal[dep.size()];

        for (int i = 0; i < dep.size(); i++) {

            cadena[i] = dep.get(i).getId();

        }
        try {
            res = em.createNamedQuery("Empleado.findByGrupoE").setParameter("c1", Arrays.asList(cadena)).getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;


    }
    public List<Empleado> buscarEliminados()
    {
        List<Empleado> res;
        
       try {
            res = em.createNamedQuery("Empleado.findByEliminado").getResultList();

        } catch (Exception e) {
            return null;
        }

        return res;
        
    }
}
