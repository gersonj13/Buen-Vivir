/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.EmpleadoSupervisor;
import com.seguroshorizonte.evaservices.jpa.entidades.EstadoEmpleadoSupervisor;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class EstadoEmpleadoSupervisorFacade extends AbstractFacade<EstadoEmpleadoSupervisor> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoEmpleadoSupervisorFacade() {
        super(EstadoEmpleadoSupervisor.class);
    }

    public EstadoEmpleadoSupervisor buscar(EmpleadoSupervisor id) {

        EstadoEmpleadoSupervisor ees;
        try {
            ees = (EstadoEmpleadoSupervisor) em.createNamedQuery("EstadoEmpleadoSupervisor.findByIdestado").setParameter("id", id.getId()).getSingleResult();

        } catch (Exception e) {
            return null;
        }


        return ees;
    }
}
