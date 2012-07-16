/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.Periodo;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

/**
 *
 * @author pangea
 */
@Stateless
public class PeriodoFacade extends AbstractFacade<Periodo> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeriodoFacade() {
        super(Periodo.class);
    }

    public Periodo buscar(BigDecimal id) {

        Periodo f;
        f = (Periodo) em.createNamedQuery("Periodo.findById").setParameter("id", id).getSingleResult();

        return f;
    }

    public Periodo periodoActivo() {

        Periodo f;
        f = (Periodo) em.createNamedQuery("Periodo.findByFechas").setParameter("fecha", new Date(), TemporalType.DATE).getSingleResult();

        return f;

    }
}
