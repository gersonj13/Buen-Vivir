/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.Planteamiento;
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
public class PlanteamientoFacade extends AbstractFacade<Planteamiento> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanteamientoFacade() {
        super(Planteamiento.class);
    }

    public Planteamiento buscar(BigDecimal id) {

        Planteamiento f;
        f = (Planteamiento) em.createNamedQuery("Planteamiento.findById").setParameter("id", id).getSingleResult();

        return f;
    }
     public List<Planteamiento> lista(BigDecimal id) {

        List<Planteamiento> f;
        f =em.createNamedQuery("Planteamiento.findByIdPresentacion").setParameter("id", id).getResultList();
        return f;
    }
}
