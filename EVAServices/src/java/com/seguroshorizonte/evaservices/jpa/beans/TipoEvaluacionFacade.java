/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.TipoEvaluacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class TipoEvaluacionFacade extends AbstractFacade<TipoEvaluacion> {
    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoEvaluacionFacade() {
        super(TipoEvaluacion.class);
    }
    
    public TipoEvaluacion buscarTipo(String tipo)
    {
        TipoEvaluacion t;
        t=(TipoEvaluacion) em.createNamedQuery("TipoEvaluacion.findByNombre").setParameter("nombre",tipo).getSingleResult();
        return t;
    }
    
}
