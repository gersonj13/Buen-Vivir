/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.Usuario;
import com.seguroshorizonte.evaservices.jpa.entidades.UsuarioRol;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario buscar(BigInteger nroi) {

        Usuario us;
        try {
            us = (Usuario) em.createNamedQuery("Usuario.findByNroIdentificacion").setParameter("nroIdentificacion", nroi).getSingleResult();

        } catch (Exception e) {
            return null;
        }


        return us;
    }
}
