/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.FormatoEvalPlanteamiento;
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
public class FormatoEvalPlanteamientoFacade extends AbstractFacade<FormatoEvalPlanteamiento> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormatoEvalPlanteamientoFacade() {
        super(FormatoEvalPlanteamiento.class);
    }

    public FormatoEvalPlanteamiento buscar(BigDecimal id, BigDecimal id2) {

        FormatoEvalPlanteamiento f;
        f = (FormatoEvalPlanteamiento) em.createNamedQuery("FormatoEvalPlanteamiento.findById2").setParameter("id", id).setParameter("id2", id2).getSingleResult();

        return f;
    }

    public FormatoEvalPlanteamiento buscarb(BigDecimal id, BigDecimal id2) {

        FormatoEvalPlanteamiento f;
        try {
            f = (FormatoEvalPlanteamiento) em.createNamedQuery("FormatoEvalPlanteamiento.findById2").setParameter("id", id).setParameter("id2", id2).getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return f;
    }
}
