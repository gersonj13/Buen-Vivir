/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.evaservices.jpa.beans;

import com.seguroshorizonte.evaservices.jpa.entidades.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

/**
 *
 * @author pangea
 */
@Stateless
public class EvaluacionEmpleadoFacade extends AbstractFacade<EvaluacionEmpleado> {

    @PersistenceContext(unitName = "EVAServicesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluacionEmpleadoFacade() {
        super(EvaluacionEmpleado.class);
    }

    public void getEntityManagerFlush() {
        em.flush();
    }

    public List<EvaluacionEmpleado> buscarempleado(BigDecimal id) {
        List<EvaluacionEmpleado> eva;

        eva = em.createNamedQuery("EvaluacionEmpleado.findByIdEmpleado").setParameter("id", id).getResultList();

        return eva;
    }

    public boolean existe(Empleado emp, Evaluacion ev) {

        EvaluacionEmpleado evp;


        try {
            evp = (EvaluacionEmpleado) em.createNamedQuery("EvaluacionEmpleado.findByIdEVA").setParameter("ide", emp.getId()).setParameter("idv", ev.getId()).getSingleResult();

        } catch (Exception e) {
            return true;
        }

        return false;

    }

    public List<BigDecimal> buscarEval(BigDecimal Eval) {

        List<BigDecimal> codigos;
        try {
            codigos = (List<BigDecimal>) em.createNamedQuery("EvaluacionEmpleado.findByIdEvaluacion").setParameter("idv", Eval).getResultList();

        } catch (Exception e) {
            return null;
        }
        return codigos;


    }

    public String[][] informacionEvaluacion(BigDecimal id) {

        Object[] resultados;
        List<Object[]> resultadosT;
        String[][] res;


        resultados = (Object[]) em.createNamedQuery("EvaluacionEmpleado.resultadoE").setParameter("idv", id).getSingleResult();
        resultadosT = em.createNamedQuery("EvaluacionEmpleado.resultadoT").setParameter("idv", id).getResultList();

        res = new String[8][];
        int i = 0;
        for (i = 0; i < resultados.length; i++) {
            res[i] = new String[1];
            if (resultados[i] == null) {
                resultados[i] = "0";
            }
            res[i][0] = String.valueOf(resultados[i]);
        }

        for (int x = 0; x < resultadosT.size(); x++) {

            res[x + i] = new String[2];
            res[x + i][0] = String.valueOf(resultadosT.get(x)[0]);
            res[x + i][1] = String.valueOf(resultadosT.get(x)[1]);
        }

        return res;

    }

    public List<Object[]> empleadosSeleccionados(Corte c) {


        List<Object[]> emp;
        


        try {

                emp = em.createNamedQuery("EvaluacionEmpleado.findBySeleccionadosCorte").setParameter("fi", c.getIdPeriodo().getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", c.getIdPeriodo().getFechaFinEvaluacion(), TemporalType.DATE).getResultList();

        } catch (Exception e) {
            return null;
        }




        return emp;
    }

    public List<Object[]> empleadosSeleccionadosC(Corte c) {


         List<Object[]> emp;
        


        try {
           emp = em.createNamedQuery("EvaluacionEmpleado.findBySeleccionadosCorteC").setParameter("fi", c.getIdPeriodo().getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", c.getIdPeriodo().getFechaFinEvaluacion(), TemporalType.DATE).getResultList();

        } catch (Exception e) {
            return null;
        }




        return emp;
    }

    public String[] datosEvaluaciones(Periodo p, BigDecimal minimo) {


        List<Object[]> c1, c3;
        Object c2, c4;
        List<EvaluacionEmpleado> ee;
        float puntosMaximos = 0, puntosMaximosd = 0;
        float canjeados = 0;
        String[] caracteristicas = new String[4];

        if (!p.getCorteList().isEmpty()) {

            for (int i = 0; i < p.getCorteList().size(); i++) {
                canjeados += p.getCorteList().get(i).getSaldoIngreso().floatValue();

            }


        }

        try {


            c1 = (List<Object[]>) em.createNamedQuery("EvaluacionEmpleado.findByMaximos").setParameter("fi", p.getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", p.getFechaFinEvaluacion(), TemporalType.DATE).getResultList();

            c2 = em.createNamedQuery("EvaluacionEmpleado.findByMaximosPuntos").setParameter("fi", p.getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", p.getFechaFinEvaluacion(), TemporalType.DATE).getSingleResult();

            c3 = (List<Object[]>) em.createNamedQuery("EvaluacionEmpleado.findByMaximosDesincorporados").setParameter("fi", p.getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", p.getFechaFinEvaluacion(), TemporalType.DATE).getResultList();

            c4 = em.createNamedQuery("EvaluacionEmpleado.findBySeleccionados").setParameter("fi", p.getFechaInicioEvaluacion(), TemporalType.DATE).setParameter("ff", p.getFechaFinEvaluacion(), TemporalType.DATE).setParameter("minimo", minimo).getSingleResult();


            for (int i = 0; i < c1.size(); i++) {
                Object px = c1.get(i);
                BigInteger y = (BigInteger) px;
                String x = y.toString();
                float z = Float.parseFloat(x);
                puntosMaximos += z;

            }
            caracteristicas[0] = String.valueOf(puntosMaximos);
            caracteristicas[1] = String.valueOf(Float.parseFloat(c2.toString()) - canjeados);

            for (int i = 0; i < c3.size(); i++) {
                Object px = c1.get(i);
                BigInteger y = (BigInteger) px;
                String x = y.toString();
                float z = Float.parseFloat(x);
                puntosMaximosd += z;

            }

            caracteristicas[2] = String.valueOf(puntosMaximosd);
            caracteristicas[3] = c4.toString();

        } catch (Exception e) {
            caracteristicas = null;
        }

        return caracteristicas;




    }
}
