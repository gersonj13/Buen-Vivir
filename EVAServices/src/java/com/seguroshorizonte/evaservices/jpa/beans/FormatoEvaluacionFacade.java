/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvaluacion;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class FormatoEvaluacionFacade extends AbstractFacade<FormatoEvaluacion> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormatoEvaluacionFacade() {
        super(FormatoEvaluacion.class);
    }

    public FormatoEvaluacion buscar(BigDecimal id) {

        FormatoEvaluacion f;
        f = (FormatoEvaluacion) em.createNamedQuery("FormatoEvaluacion.findById").setParameter("id", id).getSingleResult();

        return f;
    }
}
