/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Corte;
import com.seguroshorizonte.evaservices.jpa.entidades.Seleccionado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class SeleccionadoFacade extends AbstractFacade<Seleccionado> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SeleccionadoFacade() {
        super(Seleccionado.class);
    }

    

    public List<Seleccionado> buscarCorte(Corte c, String filtron, String filtroa, String filtroc) {

        List<Seleccionado> sel;
       
        try {
           sel = em.createNamedQuery("Seleccionado.findByIdCorte").setParameter("id",c.getId()).setParameter("nom", "%" + filtron.toUpperCase() + "%").setParameter("ape", "%" + filtroa.toUpperCase() + "%").setParameter("ced", "%" + filtroc.toUpperCase() + "%").getResultList();
        
        } catch (Exception e) {
            return null;
        }
        
        
         return sel;
    }
}
