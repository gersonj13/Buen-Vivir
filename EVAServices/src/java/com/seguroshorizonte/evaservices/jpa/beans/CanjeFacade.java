/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Canje;
import com.seguroshorizonte.evaservices.jpa.entidades.Corte;
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
public class CanjeFacade extends AbstractFacade<Canje> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CanjeFacade() {
        super(Canje.class);
    }

    public List<Canje> buscar(BigDecimal id) {

        List<Canje> f;
        try {
           f = (List<Canje>) em.createNamedQuery("Canje.findBySeleccionado").setParameter("id", id).getResultList();
  
        } catch (Exception e) {
            return null;
        }
  
       
        return f;
    }
   
}

