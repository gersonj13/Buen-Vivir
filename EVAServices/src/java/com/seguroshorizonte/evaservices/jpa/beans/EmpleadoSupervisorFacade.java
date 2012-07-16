/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.EmpleadoSupervisor;
import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
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
public class EmpleadoSupervisorFacade extends AbstractFacade<EmpleadoSupervisor> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadoSupervisorFacade() {
        super(EmpleadoSupervisor.class);
    }

    public EmpleadoSupervisor buscarEmpleado(String id) {

        EmpleadoSupervisor emps;

        try {
            emps = (EmpleadoSupervisor) em.createNamedQuery("EmpleadoSupervisor.findByIdEmpleado").setParameter("id", new BigDecimal(id)).getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return emps;
    }
    public void refrescar(EmpleadoSupervisor e)
    {
        em.refresh(e);
    }
}
