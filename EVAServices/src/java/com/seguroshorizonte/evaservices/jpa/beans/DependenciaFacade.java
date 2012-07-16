/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Dependencia;
import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class DependenciaFacade extends AbstractFacade<Dependencia> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DependenciaFacade() {
        super(Dependencia.class);
    }

    public Dependencia buscarId(BigDecimal id) {
        Dependencia d;
        d = (Dependencia) em.createNamedQuery("Dependencia.findById").setParameter("id", id).getSingleResult();
        return d;
    }

    public Dependencia buscarDescripcion(String nombre) {
        Dependencia d;
        d = (Dependencia) em.createNamedQuery("Dependencia.findByDescripcion").setParameter("descripcion", nombre).getSingleResult();
        return d;
    }

    public Dependencia buscarNombre(String nombre) {
        Dependencia d;
        d = (Dependencia) em.createNamedQuery("Dependencia.findByNombre").setParameter("nombre", nombre).getSingleResult();
        return d;
    }

    public List<Dependencia> buscarTodos(BigDecimal idv) {

        List<Dependencia> dep;


        dep = (List<Dependencia>) em.createNamedQuery("Dependencia.findByIdc").setParameter("idv", idv).getResultList();
        return dep;

    }

    public List<Dependencia> buscarTodosGerentes(BigDecimal idv, BigDecimal idg) {

        List<Dependencia> dep;


        dep = (List<Dependencia>) em.createNamedQuery("Dependencia.findByIdcG").setParameter("idv", idv).setParameter("idd", idg).setParameter("idg", idg).getResultList();
        return dep;

    }

    public List<Dependencia> buscarTodosHistorico() {

        List<Dependencia> dep;


        dep = (List<Dependencia>) em.createNamedQuery("Dependencia.findByHistorico").getResultList();
        return dep;

    }

    public List<Dependencia> buscarNivel(String nivel) {

        List<Dependencia> dep;

        try {
            dep = (List<Dependencia>) em.createNamedQuery("Dependencia.findByNivel").setParameter("nivel", new BigInteger(nivel)).getResultList();

        } catch (Exception e) {
            return null;
        }
        return dep;

    }
}
