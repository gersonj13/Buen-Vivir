/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.*;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class FormatoEvaluacionFactorFacade extends AbstractFacade<FormatoEvaluacionFactor> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormatoEvaluacionFactorFacade() {
        super(FormatoEvaluacionFactor.class);
    }

    public FormatoEvaluacionFactor buscar(BigDecimal id) {

        FormatoEvaluacionFactor f;
        f = (FormatoEvaluacionFactor) em.createNamedQuery("FormatoEvaluacionFactor.findById").setParameter("id", id).getSingleResult();

        return f;
    }

    public List<FormatoEvaluacionFactor> lista(BigDecimal id) {

        List<FormatoEvaluacionFactor> f;
        f = em.createNamedQuery("FormatoEvaluacionFactor.findByIdFormato").setParameter("id", id).getResultList();
        return f;
    }

    public List<PresentacionFactor> lista2(BigDecimal id) {

        List<PresentacionFactor> f;
        f = em.createNamedQuery("FormatoEvaluacionFactor.findByIdFormatoPre").setParameter("id", id).getResultList();

        return f;
    }

    public void limpiar() {
        em.flush();

    }

    public FormatoEvaluacionFactor buscarFormatoPresentacion(FormatoEvaluacion fe, PresentacionFactor pf) {

        FormatoEvaluacionFactor fef = (FormatoEvaluacionFactor) em.createNamedQuery("FormatoEvaluacionFactor.findByIdFP").setParameter("id1", fe.getId()).setParameter("id2", pf.getId()).getSingleResult();

        return fef;

    }
}
