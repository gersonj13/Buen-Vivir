/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class EstadoEmpleadoFacade extends AbstractFacade<EstadoEmpleado> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoEmpleadoFacade() {
        super(EstadoEmpleado.class);
    }
    public EstadoEmpleado buscarEmpleado(Empleado emp)
    {
        
        EstadoEmpleado es=(EstadoEmpleado) em.createNamedQuery("EstadoEmpleado.findByEmpleado").setParameter("id", emp.getId()).getSingleResult();
        
        return es;
    }
}
