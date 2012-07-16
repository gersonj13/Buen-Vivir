/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Corte;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class CorteFacade extends AbstractFacade<Corte> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CorteFacade() {
        super(Corte.class);
    }

    public Corte buscar(BigDecimal id) {

        Corte f;
        f = (Corte) em.createNamedQuery("Corte.findById").setParameter("id", id).getSingleResult();

        return f;
    }
     public Corte buscarMaximo() {

        Corte f;
        f = (Corte) em.createNamedQuery("Corte.findMaximo").getSingleResult();

        return f;
    }
}

