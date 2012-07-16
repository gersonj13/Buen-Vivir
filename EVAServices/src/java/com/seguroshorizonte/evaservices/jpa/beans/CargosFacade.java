/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.Cargo;
import com.seguroshorizonte.evaservices.jpa.entidades.Dependencia;
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
public class CargosFacade extends AbstractFacade<Cargo> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CargosFacade() {
        super(Cargo.class);
    }

    public Cargo buscarNombre(String nombre) {
        Cargo d;
        try {
            d = (Cargo) em.createNamedQuery("Cargo.findByNombre").setParameter("nombre", nombre.trim().toUpperCase()).getSingleResult();
           
        } catch (Exception e) {
            return null;
        }
         return d;
    }
    public List<Cargo> listado(String filtro)
    {
        List<Cargo> lista;
        
        lista=em.createNamedQuery("Cargo.findByFiltro").setParameter("filtro","%"+filtro.toUpperCase()+"%").getResultList();
        
        
       return lista; 
    }
}
