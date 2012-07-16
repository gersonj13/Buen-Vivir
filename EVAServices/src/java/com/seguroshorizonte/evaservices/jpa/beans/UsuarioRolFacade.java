/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Empleado;
import com.seguroshorizonte.evaservices.jpa.entidades.Factor;
import com.seguroshorizonte.evaservices.jpa.entidades.Usuario;
import com.seguroshorizonte.evaservices.jpa.entidades.UsuarioRol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pangea
 */
@Stateless
public class UsuarioRolFacade extends AbstractFacade<UsuarioRol> {
    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioRolFacade() {
        super(UsuarioRol.class);
    }
   
    public List<UsuarioRol> buscarRol(Usuario codusu)
    {
        
        List<UsuarioRol> roles;
        roles=(List<UsuarioRol>)em.createNamedQuery("UsuarioRol.findByUsuario").setParameter("id", codusu).getResultList();
        
        return roles;
    }
    
}
