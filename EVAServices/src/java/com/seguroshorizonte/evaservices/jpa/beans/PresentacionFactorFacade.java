/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvaluacionFactor;
import com.seguroshorizonte.evaservices.jpa.entidades.PresentacionFactor;
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
public class PresentacionFactorFacade extends AbstractFacade<PresentacionFactor> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PresentacionFactorFacade() {
        super(PresentacionFactor.class);
    }

    public List<PresentacionFactor> buscarfactor(Factor f) {

        List<PresentacionFactor> p;
        p = em.createNamedQuery("PresentacionFactor.findByIdFactor").setParameter("id", f.getId()).getResultList();
        return p;
    }

    public PresentacionFactor buscarp(PresentacionFactor pre) {

        PresentacionFactor p;
        p = (PresentacionFactor) em.createNamedQuery("PresentacionFactor.findById").setParameter("id", pre.getId()).getSingleResult();
        return p;

    }

    
}
