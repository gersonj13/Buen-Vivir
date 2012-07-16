package com.seguroshorizonte.evaservices;

import com.novell.ldap.*;
import com.seguroshorizonte.evaservices.jpa.beans.*;
import com.seguroshorizonte.evaservices.jpa.entidades.*;
import com.seguroshorizonte.evaservices.to.EmpleadoTO;
import com.seguroshorizonte.evaservices.to.ResultadoEvaluacionTO;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author gerson ramirez
 */
@WebService(serviceName = "GestionEvaluacion")
public class GestionEvaluacion {

    @EJB
    FactorFacade ejb;
    @EJB
    EmpleadoFacade empleadoFacade;
    @EJB
    PresentacionFactorFacade presentacionFactorFacade;
    @EJB
    EstadoEmpleadoSupervisorFacade estadoempleadosupervisorFactorFacade;
    @EJB
    EvaluacionFacade evaluacionFacade;
    @EJB
    ResultadoFactorFacade resultadoFactorFacade;
    @EJB
    EvaluacionEmpleadoFacade evaluacionEmpleadoFacade;
    @EJB
    RevisionFacade revisionFacade;
    @EJB
    DependenciaFacade dependenciaFacade;
    @EJB
    TipoEvaluacionFacade tipoevaluacionFacade;
    @EJB
    UsuarioRolFacade UsuarioRolFacade;
    @EJB
    UsuarioFacade usuarioFacade;
    @EJB
    EstadoEmpleadoFacade estadoempleadoFacade;
    @EJB
    EmpleadoSupervisorFacade empleadosupervisorFacade;
    @EJB
    CargosFacade cargoFacade;
    @EJB
    FactorFacade factoresFacade;
    @EJB
    PlanteamientoFacade planteamientosFacade;
    @EJB
    FormatoEvaluacionFacade formatoevaluacionFacade;
    @EJB
    FormatoEvaluacionFactorFacade formatoevaluacionfactorFacade;
    @EJB
    FormatoEvalPlanteamientoFacade formatoevalplanteamientoFacade;
    @EJB
    PeriodoFacade periodoFacade;
    @EJB
    CorteFacade corteFacade;
    @EJB
    SeleccionadoFacade seleccionadoFacade;
    @EJB
    CanjeFacade canjeFacade;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "registrarEvaluacion")
    public String registrarEvaluacion(@WebParam(name = "resultadox") ResultadoEvaluacionTO resultado) {

        Empleado empleado = empleadoFacade.find(resultado.getEmpleadox().getId());
        Empleado supervisor = empleadoFacade.find(resultado.getSupervisor().getId());
        Evaluacion evaluacion = evaluacionFacade.find(resultado.getEvaluacionx().getId());
        TipoEvaluacion tipoEvaluacion = tipoevaluacionFacade.find(resultado.getTipoEvaluacionx().getId());
        ResultadoEvaluacionTO re = new ResultadoEvaluacionTO();
        re.setPlanteamientos(null);

        if (evaluacionEmpleadoFacade.existe(empleado, evaluacion)) {

            List<ResultadoFactor> listaFactores = new ArrayList<ResultadoFactor>();




            List<FormatoEvaluacionFactor> listaFortEval = evaluacion.getIdFormatoEvaluacion().getFormatoEvaluacionFactorList();
            Iterator it1 = listaFortEval.iterator();
            while (it1.hasNext()) {
                FormatoEvaluacionFactor auxFEF = (FormatoEvaluacionFactor) it1.next();

                Iterator it2 = auxFEF.getFormatoEvalPlanteamientoList().iterator();
                while (it2.hasNext()) {
                    FormatoEvalPlanteamiento auxFEP = (FormatoEvalPlanteamiento) it2.next();

                    Iterator it3 = resultado.getPlanteamientos().iterator();
                    while (it3.hasNext()) {
                        Planteamiento auxp = (Planteamiento) it3.next();

                        if (auxp.getId().compareTo(auxFEP.getIdPlanteamiento().getId()) == 0) {

                            ResultadoFactor resultadoFactor = new ResultadoFactor();

                            resultadoFactor.setIdEvaluacionEmpleado(null);
                            resultadoFactor.setIdPlanteamiento(auxFEP);

                            resultadoFactor.setPuntos(auxFEP.getPesoPlanteamientoEscala());
                            listaFactores.add(resultadoFactor);
                        }

                    }
                }
            }



            EvaluacionEmpleado evalEmpleado = new EvaluacionEmpleado();

            evalEmpleado.setIdEmpleado(empleado);
            evalEmpleado.setIdEvaluacion(evaluacion);
            evalEmpleado.setIdEvaluador(supervisor);
            evalEmpleado.setIdTipoEvaluacion(tipoEvaluacion);
            evalEmpleado.setNombreCargo(empleado.getIdCargo().getNombre());
            evalEmpleado.setNombreDependencia(empleado.getIdDependencia().getNombre());

            Iterator itl = listaFactores.iterator();
            BigDecimal pts = new BigDecimal(0);
            double puntosT = 0;
            while (itl.hasNext()) {
                ResultadoFactor n = (ResultadoFactor) itl.next();
                puntosT = puntosT + n.getPuntos().doubleValue();
            }

            int i = (int) puntosT;
            evalEmpleado.setPuntos(new BigInteger(String.valueOf(i)).multiply(evaluacion.getPeso()));
            evalEmpleado.setCerrada(BigInteger.ZERO);


            //persistencia Evaluacion empleado
            evaluacionEmpleadoFacade.create(evalEmpleado);
            evaluacionEmpleadoFacade.sincronizar();



            //setear id
            Iterator itlf = listaFactores.iterator();

            int t = evalEmpleado.getId().intValue() + 1;
            evalEmpleado.setId(new BigDecimal(String.valueOf(t)));

            while (itlf.hasNext()) {
                ResultadoFactor n = (ResultadoFactor) itlf.next();
                n.setIdEvaluacionEmpleado(evalEmpleado);

                //persistencia Resultado factor

                resultadoFactorFacade.create(n);
            }
            resultadoFactorFacade.sincronizar();
            Revision revision = new Revision();
            revision.setCargoRevisor(empleado.getIdCargo().getNombre());
            revision.setIdEvaluacionEmpleado(evalEmpleado);
            revision.setIdRevisor(supervisor);

            //persistencia revision
            revisionFacade.sincronizar();
            revisionFacade.create(revision);
            revisionFacade.sincronizar();

            return "procesado";
        }
        return "error";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "registrarEvaluacionVacaciones")
    public String registrarEvaluacionVacaciones(@WebParam(name = "resultadox") ResultadoEvaluacionTO resultado) {


        Empleado empleado = empleadoFacade.find(resultado.getEmpleadox().getId());
        Empleado supervisor = empleadoFacade.find(resultado.getSupervisor().getId());
        Evaluacion evaluacion = evaluacionFacade.find(resultado.getEvaluacionx().getId());
        TipoEvaluacion tipoEvaluacion = tipoevaluacionFacade.find(resultado.getTipoEvaluacionx().getId());
        int pes = 0;

        if (evaluacionEmpleadoFacade.existe(empleado, evaluacion)) {


            EvaluacionEmpleado evalEmpleado = new EvaluacionEmpleado();
            evalEmpleado.setIdEmpleado(empleado);
            evalEmpleado.setIdEvaluacion(evaluacion);
            evalEmpleado.setIdEvaluador(supervisor);
            evalEmpleado.setIdTipoEvaluacion(tipoEvaluacion);
            evalEmpleado.setNombreCargo(empleado.getIdCargo().getNombre());
            evalEmpleado.setNombreDependencia(empleado.getIdDependencia().getNombre());


            List<EvaluacionEmpleado> historico = evaluacionEmpleadoFacade.buscarempleado(empleado.getId());
            int puntos = 0;


            String pun, peso;
            for (int i = 0; i < historico.size(); i++) {

                if (historico.get(i).getIdEvaluacion().getIdFormatoEvaluacion().getEscala().toString().compareTo("1000") == 0) {
                    pun = historico.get(i).getPuntos().toString();
                    peso = historico.get(i).getIdEvaluacion().getPeso().toString();
                    if (Integer.parseInt(peso) > 1) {
                        pes += (Integer.parseInt(peso) - 1);
                    }
                    int div = Integer.parseInt(pun);
                    puntos += div;
                }

            }
            int numero = historico.size();
            for (int j = 0; j < historico.size(); j++) {
                if (historico.get(j).getIdEvaluacion().getIdFormatoEvaluacion().getEscala().toString().compareTo("1000") != 0) {
                    numero--;
                }
            }
            if (numero > 0) {
                puntos /= (historico.size() + pes);
            } else {
                puntos = 0;
            }

            evalEmpleado.setPuntos(new BigInteger(String.valueOf(puntos)).multiply(evaluacion.getPeso()));
            evalEmpleado.setCerrada(BigInteger.ZERO);


            //persistencia Evaluacion empleado
            evaluacionEmpleadoFacade.create(evalEmpleado);
            evaluacionEmpleadoFacade.sincronizar();

            int t = evalEmpleado.getId().intValue() + 1;
            evalEmpleado.setId(new BigDecimal(String.valueOf(t)));


            Revision revision = new Revision();
            revision.setCargoRevisor(empleado.getIdCargo().getNombre());
            revision.setIdEvaluacionEmpleado(evalEmpleado);
            revision.setIdRevisor(supervisor);

            //persistencia revision
            revisionFacade.sincronizar();
            revisionFacade.create(revision);
            revisionFacade.sincronizar();
        } else {
            return "error";
        }

        return "";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "registrarEvaluacionReposo")
    public String registrarEvaluacionReposo(@WebParam(name = "resultadox") ResultadoEvaluacionTO resultado) {
        //TODO write your implementation code here:


        Empleado empleado = empleadoFacade.find(resultado.getEmpleadox().getId());
        Empleado supervisor = empleadoFacade.find(resultado.getSupervisor().getId());
        Evaluacion evaluacion = evaluacionFacade.find(resultado.getEvaluacionx().getId());
        TipoEvaluacion tipoEvaluacion = tipoevaluacionFacade.find(resultado.getTipoEvaluacionx().getId());

        if (evaluacionEmpleadoFacade.existe(empleado, evaluacion)) {


            EvaluacionEmpleado evalEmpleado = new EvaluacionEmpleado();
            evalEmpleado.setIdEmpleado(empleado);
            evalEmpleado.setIdEvaluacion(evaluacion);
            evalEmpleado.setIdEvaluador(supervisor);
            evalEmpleado.setIdTipoEvaluacion(tipoEvaluacion);
            evalEmpleado.setNombreCargo(empleado.getIdCargo().getNombre());
            evalEmpleado.setNombreDependencia(empleado.getIdDependencia().getNombre());


            int puntos = 0;


            evalEmpleado.setPuntos(new BigInteger(String.valueOf(puntos)).multiply(evaluacion.getPeso()));
            evalEmpleado.setCerrada(BigInteger.ZERO);


            //persistencia Evaluacion empleado
            evaluacionEmpleadoFacade.create(evalEmpleado);
            evaluacionEmpleadoFacade.sincronizar();

            int t = evalEmpleado.getId().intValue() + 1;
            evalEmpleado.setId(new BigDecimal(String.valueOf(t)));


            Revision revision = new Revision();
            revision.setCargoRevisor(empleado.getIdCargo().getNombre());
            revision.setIdEvaluacionEmpleado(evalEmpleado);
            revision.setIdRevisor(supervisor);

            //persistencia revision
            revisionFacade.sincronizar();
            revisionFacade.create(revision);
            revisionFacade.sincronizar();

        } else {
            return "error";
        }
        return "";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaTipoEvaluacion")
    public TipoEvaluacion retornaTipoEvaluacion(@WebParam(name = "nombretipox") String nombreTipo) {

        TipoEvaluacion tipo;
        tipo = tipoevaluacionFacade.buscarTipo(nombreTipo);

        return tipo;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isSupervisor")
    public boolean isSupervisor(@WebParam(name = "idEmpleado") String idEmpleado) {

        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));

        List<EmpleadoSupervisor> empleadosSupervisor = empleado.getEmpleadoSupervisorList();

        for (Iterator<EmpleadoSupervisor> it = empleadosSupervisor.iterator(); it.hasNext();) {
            EmpleadoSupervisor empleadoSupervisor = it.next();


            for (Iterator<EstadoEmpleadoSupervisor> it1 = empleadoSupervisor.getEstadoEmpleadoSupervisorList().iterator(); it1.hasNext();) {
                EstadoEmpleadoSupervisor estadoEmpleadoSupervisor = it1.next();
                if (estadoEmpleadoSupervisor.getUltimoEstado().compareTo(new BigInteger("1")) == 0 && estadoEmpleadoSupervisor.getActivo().compareTo(new BigInteger("1")) == 0) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEvaluacionesSuper")
    public List<Evaluacion> getEvaluacionesSuper(@WebParam(name = "idEmpleadox") String idEmpleado) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();

        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        Dependencia dependencia = empleado.getIdDependencia();
        do {

            for (Iterator<Evaluacion> it = dependencia.getEvaluacionList().iterator(); it.hasNext();) {
                Evaluacion evaluacion = it.next();

                if (evaluacion.getFechaInicio().compareTo(new Date()) <= 0 && evaluacion.getFechaFin().compareTo(new Date()) >= 0) {

                    evaluacion.setEvaluacionEmpleadoList(null);
                    evaluacion.setIdFormatoEvaluacion(null);
                    evaluacion.setIdCreador(null);
                    evaluaciones.add(evaluacion);
                }
            }
            dependencia = dependencia.getIdDependenciaPadre();

        } while (dependencia != null);


        return evaluaciones;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEvaluacionesAbiertas")
    public boolean getEvaluacionesAbiertas(@WebParam(name = "idEmpleadox") String idEmpleado) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        boolean ban = true;
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        Dependencia dependencia = empleado.getIdDependencia();
        do {

            for (Iterator<Evaluacion> it = dependencia.getEvaluacionList().iterator(); it.hasNext();) {
                Evaluacion evaluacion = it.next();

                if (evaluacion.getFechaInicio().compareTo(new Date()) <= 0 && evaluacion.getFechaFin().compareTo(new Date()) >= 0) {

                    ban = false;
                }
            }
            dependencia = dependencia.getIdDependenciaPadre();

        } while (dependencia != null);


        return ban;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEmpleadosEvaluacion")
    public List<EmpleadoTO> getEmpleadosEvaluacion(@WebParam(name = "idEmpleadox") String idEmpleado,
            @WebParam(name = "idEvaluacion") String idEvaluacion) {
        //TODO write your implementation code here:



        List<EmpleadoTO> empleados = new ArrayList<EmpleadoTO>();



        empleadoFacade.sincronizar();
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        empleadoFacade.sincronizar();

        for (Iterator<EmpleadoSupervisor> it = empleado.getEmpleadoSupervisorList().iterator(); it.hasNext();) {
            EmpleadoSupervisor empleadoSupervisor = it.next();


            EmpleadoTO empleadoTO = new EmpleadoTO();


            empleadoTO.copyEmpleado(empleadoSupervisor.getIdEmpleado());

            Empleado auxiliar = empleadoFacade.find(empleadoSupervisor.getIdEmpleado().getId());
            empleadoTO.setCondicion("pendiente");

            for (Iterator<EvaluacionEmpleado> it2 = empleadoSupervisor.getIdEmpleado().getEvaluacionEmpleadoList().iterator(); it2.hasNext();) {
                EvaluacionEmpleado evaluacionEmpleado = it2.next();
                if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0) {
                    empleadoTO.setCondicion("evaluadoR0");
                }
                if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Vacaciones") == 0) {
                    empleadoTO.setCondicion("vacaciones");
                }
                if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Reposo") == 0) {
                    empleadoTO.setCondicion("reposo");
                }
            }
            if (auxiliar.getEstadoEmpleadoList().get(0).getActivo().toString().compareTo("1") == 0) {
                empleados.add(empleadoTO);
            }



        }

        Collections.sort(empleados, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                EmpleadoTO e1 = (EmpleadoTO) o1;
                EmpleadoTO e2 = (EmpleadoTO) o2;
                char codigo1 = e1.getCondicion().charAt(0);
                char codigo2 = e2.getCondicion().charAt(0);
                if ((codigo1 == 'p' || codigo1 == 'e') && (codigo2 == 'p' || codigo2 == 'e')) {
                    char a;
                    a = codigo1;
                    codigo1 = codigo2;
                    codigo2 = a;
                }

                if (codigo1 > codigo2) {
                    return 1;
                } else if (codigo1 < codigo2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return empleados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEmpleadosEvaluaciong")
    public List<EmpleadoTO> getEmpleadosEvaluaciong(@WebParam(name = "idEmpleadox") String idEmpleado,
            @WebParam(name = "idEvaluacion") String idEvaluacion) {
        //TODO write your implementation code here:



        List<EmpleadoTO> empleados = new ArrayList<EmpleadoTO>();



        empleadoFacade.sincronizar();
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        empleadoFacade.sincronizar();

        Dependencia aux = empleado.getIdDependencia();

        Dependencia dependencia = dependenciaFacade.find(aux.getId());



        if (!dependencia.getDependenciaList().isEmpty()) {



            for (int i = 0; i < dependencia.getDependenciaList().size(); i++) {


                Dependencia n1 = dependencia.getDependenciaList().get(i);



                if (!n1.getDependenciaList().isEmpty()) {



                    for (int j = 0; j < n1.getDependenciaList().size(); j++) {


                        Dependencia n2 = n1.getDependenciaList().get(j);

                        if (!n2.getDependenciaList().isEmpty()) {





                            for (int k = 0; k < n2.getDependenciaList().size(); k++) {


                                Dependencia n3 = n2.getDependenciaList().get(k);



                                if (!n3.getEmpleadoList().isEmpty()) {


                                    for (int l = 0; l < n3.getEmpleadoList().size(); l++) {

                                        Empleado auxe = n3.getEmpleadoList().get(l);


                                        EmpleadoTO empleadoTO = new EmpleadoTO();
                                        empleadoTO.copyEmpleado(auxe);

                                        Empleado auxiliar = empleadoFacade.find(auxe.getId());
                                        empleadoTO.setCondicion("pendiente");

                                        for (Iterator<EvaluacionEmpleado> it2 = auxiliar.getEvaluacionEmpleadoList().iterator(); it2.hasNext();) {

                                            EvaluacionEmpleado evaluacionEmpleado = it2.next();
                                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0) {
                                                empleadoTO.setCondicion("evaluadoR0");
                                            }
                                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Vacaciones") == 0) {
                                                empleadoTO.setCondicion("vacaciones");
                                            }
                                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Reposo") == 0) {
                                                empleadoTO.setCondicion("reposo");
                                            }
                                        }
                                        if (auxiliar.getEstadoEmpleadoList().get(0).getActivo().toString().compareTo("1") == 0) {
                                            empleados.add(empleadoTO);
                                        }

                                    }

                                }


                            }





                        }
                        if (!n2.getEmpleadoList().isEmpty()) {


                            for (int l = 0; l < n2.getEmpleadoList().size(); l++) {

                                Empleado auxe = n2.getEmpleadoList().get(l);


                                EmpleadoTO empleadoTO = new EmpleadoTO();
                                empleadoTO.copyEmpleado(auxe);

                                Empleado auxiliar = empleadoFacade.find(auxe.getId());
                                empleadoTO.setCondicion("pendiente");

                                for (Iterator<EvaluacionEmpleado> it2 = auxiliar.getEvaluacionEmpleadoList().iterator(); it2.hasNext();) {

                                    EvaluacionEmpleado evaluacionEmpleado = it2.next();
                                    if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0) {
                                        empleadoTO.setCondicion("evaluadoR0");
                                    }
                                    if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Vacaciones") == 0) {
                                        empleadoTO.setCondicion("vacaciones");
                                    }
                                    if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Reposo") == 0) {
                                        empleadoTO.setCondicion("reposo");
                                    }
                                }
                                if (auxiliar.getEstadoEmpleadoList().get(0).getActivo().toString().compareTo("1") == 0) {
                                    empleados.add(empleadoTO);
                                }

                            }

                        }


                    }



                }
                if (!n1.getEmpleadoList().isEmpty()) {


                    for (int l = 0; l < n1.getEmpleadoList().size(); l++) {

                        Empleado auxe = n1.getEmpleadoList().get(l);


                        EmpleadoTO empleadoTO = new EmpleadoTO();
                        empleadoTO.copyEmpleado(auxe);

                        Empleado auxiliar = empleadoFacade.find(auxe.getId());
                        empleadoTO.setCondicion("pendiente");

                        for (Iterator<EvaluacionEmpleado> it2 = auxiliar.getEvaluacionEmpleadoList().iterator(); it2.hasNext();) {

                            EvaluacionEmpleado evaluacionEmpleado = it2.next();
                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0) {
                                empleadoTO.setCondicion("evaluadoR0");
                            }
                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Vacaciones") == 0) {
                                empleadoTO.setCondicion("vacaciones");
                            }
                            if (evaluacionEmpleado.getIdEvaluacion().getId().compareTo(new BigDecimal(idEvaluacion)) == 0 && evaluacionEmpleado.getIdTipoEvaluacion().getNombre().compareTo("Reposo") == 0) {
                                empleadoTO.setCondicion("reposo");
                            }
                        }
                        if (auxiliar.getEstadoEmpleadoList().get(0).getActivo().toString().compareTo("1") == 0) {
                            empleados.add(empleadoTO);
                        }

                    }

                }


            }

        }


        Collections.sort(empleados, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                EmpleadoTO e1 = (EmpleadoTO) o1;
                EmpleadoTO e2 = (EmpleadoTO) o2;
                char codigo1 = e1.getCondicion().charAt(0);
                char codigo2 = e2.getCondicion().charAt(0);
                if ((codigo1 == 'p' || codigo1 == 'e') && (codigo2 == 'p' || codigo2 == 'e')) {
                    char a;
                    a = codigo1;
                    codigo1 = codigo2;
                    codigo2 = a;
                }

                if (codigo1 > codigo2) {
                    return 1;
                } else if (codigo1 < codigo2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return empleados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getFormatoEvaluacion")
    public List<Planteamiento> getFormatoEvaluacion(@WebParam(name = "idEvaluacionx") String idEvaluacion) {


        List<Planteamiento> planteamientos = new ArrayList<Planteamiento>();



        Evaluacion evaluacion = evaluacionFacade.find(new BigDecimal(idEvaluacion));
        Collection factoresformato = evaluacion.getIdFormatoEvaluacion().getFormatoEvaluacionFactorList();


        for (Iterator it = factoresformato.iterator(); it.hasNext();) {

            FormatoEvaluacionFactor formatoFactor = (FormatoEvaluacionFactor) it.next();
            Collections.sort(formatoFactor.getIdPresentacionFactor().getPlanteamientoList(), new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    Planteamiento e1 = (Planteamiento) o1;
                    Planteamiento e2 = (Planteamiento) o2;
                    BigDecimal codigo1 = e1.getId();
                    BigDecimal codigo2 = e2.getId();

                    if (codigo1.compareTo(codigo2) > 0) {
                        return 1;
                    } else if (codigo1.compareTo(codigo2) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            planteamientos.addAll(formatoFactor.getIdPresentacionFactor().getPlanteamientoList());

        }



        return planteamientos;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deduccionesEmpleado")
    public String deduccionesEmpleado(@WebParam(name = "idEmpleadox") String idEmpleado) {
        double deducciones = 0;

        Periodo p = periodoFacade.periodoActivo();
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));

        List<Seleccionado> seleccionado = empleado.getSeleccionadoList();

        if (seleccionado != null) {
            for (int i = 0; i < seleccionado.size(); i++) {

                if (seleccionado.get(i).getIdCorte().getIdPeriodo().getId().compareTo(p.getId()) == 0) {
                    if (seleccionado.get(i).getCanjeList() != null) {
                        List<Canje> canjes = seleccionado.get(i).getCanjeList();

                        for (int j = 0; j < canjes.size(); j++) {

                            deducciones += Float.parseFloat(canjes.get(j).getPuntos().toString());

                        }

                    }


                }
            }

        }


        return String.valueOf(deducciones);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "puntajeEmpleado")
    public String puntajeEmpleado(@WebParam(name = "idEmpleadox") String idEmpleado) {


        double puntuacion = 0;
        List<EvaluacionEmpleado> listax, listae;

        Periodo p = periodoFacade.periodoActivo();
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        listax = empleado.getEvaluacionEmpleadoList();

        if (listax != null) {

            listae = new ArrayList<EvaluacionEmpleado>();
            for (int i = 0; i < listax.size(); i++) {

                if (listax.get(i).getIdEvaluacion().getFechaInicio().compareTo(p.getFechaInicioEvaluacion()) >= 0 && listax.get(i).getIdEvaluacion().getFechaInicio().compareTo(p.getFechaFinEvaluacion()) <= 0) {
                    listae.add(listax.get(i));
                }
            }
            Collection<EvaluacionEmpleado> evaluacionesEmpleado = listae;
            for (Iterator<EvaluacionEmpleado> it = evaluacionesEmpleado.iterator(); it.hasNext();) {

                EvaluacionEmpleado evaluacionEmpleado = it.next();
                if (evaluacionEmpleado.getCerrada().compareTo(new BigInteger("0")) == 0) {
                    puntuacion += evaluacionEmpleado.getPuntos().doubleValue();
                }

            }
        } else {
            puntuacion = 0;
        }

        return String.valueOf(puntuacion);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isSeleccionado")
    public Seleccionado isSeleccionado(@WebParam(name = "idEmpleadox") String idEmpleado) {

        Periodo p = periodoFacade.periodoActivo();
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));
        Corte corte = null, cortex;
        Seleccionado sel = null;
        if (!p.getCorteList().isEmpty()) {

            corte = p.getCorteList().get(0);

            for (int i = 1; i < p.getCorteList().size(); i++) {

                if (Integer.parseInt(p.getCorteList().get(i).getId().toString()) > Integer.parseInt(corte.getId().toString())) {
                    corte = p.getCorteList().get(i);
                }
            }


            cortex = corteFacade.find(corte.getId());
            if (!cortex.getSeleccionadoList().isEmpty()) {

                for (int i = 0; i < cortex.getSeleccionadoList().size(); i++) {


                    if (cortex.getSeleccionadoList().get(i).getIdEmpleado().getId().compareTo(empleado.getId()) == 0) {
                        sel = cortex.getSeleccionadoList().get(i);
                    }

                }
            }
            if (cortex.getSaldoCorte().compareTo(BigDecimal.ZERO) == 0) {
                sel = null;
            }
        }
        return sel;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "user") String user,
            @WebParam(name = "password") String password) {


        int ldapPort;
        int ldapVersion;
        String base = "@seguroshorizonte.com";
        String ldapHost = "172.19.4.6";
        String dn = "";
        String[] ATTRS = {"mail", "sAMAccountName"};
        LDAPConnection conn;
        String[] values;
        String[] vect = new String[2];
        boolean find = false;

        try {
            ldapVersion = LDAPConnection.LDAP_V3;
            //Puerto por Defecto 389
            ldapPort = LDAPConnection.DEFAULT_PORT;
            //ldapPort = LDAPConnection.DEFAULT_SSL_PORT; //Puerto SSL 636
            conn = new LDAPConnection();
            dn = user + base;
            conn.connect(ldapHost, ldapPort);
            conn.bind(ldapVersion, dn, password.getBytes("UTF8"));

            if (conn.isBound()) {

                LDAPAttribute attr = null;
                LDAPEntry nextEntry = null;

                // nextEntry = conn.read("CN=Prueba II,OU=Oficinas Con Servidor,OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com");

                LDAPSearchConstraints cons = new LDAPSearchConstraints();
                cons.setTimeLimit(20000);
                cons.setMaxResults(5000);

                //   LDAPSearchResults res = conn.search("OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(objectclass=user)", ATTRS, false);
                LDAPSearchResults res = conn.search("OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(&(objectclass=user)(sAMAccountName=" + user + "))", ATTRS, false, cons);

                // Loop on results until complete
                while (res.hasMore() && !find) {
                    try {
                        vect[0] = "";
                        vect[1] = "";
                        // The next entry
                        LDAPEntry entry = res.next();
                        int i = 0;

                        LDAPAttributeSet attributeSet = entry.getAttributeSet();
                        Iterator allAttributes = attributeSet.iterator();
                        //Recore los atributos del usuario
                        while (allAttributes.hasNext()) {
                            LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                            String attributeName = attribute.getName();
                            Enumeration allValues = attribute.getStringValues();

                            if (allValues != null) {


                                while (allValues.hasMoreElements()) {
                                    String value = (String) allValues.nextElement();
                                    vect[i] = value;

                                }

                            }
                            i++;
                        }

                        if (vect[1].toUpperCase().equals(user.toUpperCase())) {
                            find = true;
                        }
                    } catch (LDAPReferralException e) {
                        // Ignore any referrals
                        continue;
                    } catch (LDAPException e) {
                        continue;
                    }
                }
                if (find) {
                    return vect[0];
                }

                return "rechazado";

            } else {
                return "rechazado";
            }

        } //catch(LDAPException ex){
        catch (UnsupportedEncodingException ex) {
            return "rechazado";
        } catch (LDAPException ex) {
            if (ex.getLDAPErrorMessage().split(",")[2].trim().split(" ")[1].trim().compareTo("773") == 0) {
                return "pass";
            }
            return "rechazado";
        } finally {
            //conn.disconnect();
        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "loginDatos")
    public String[] loginDatos(@WebParam(name = "user") String user,
            @WebParam(name = "password") String password) {


        int ldapPort;
        int ldapVersion;
        String base = "@seguroshorizonte.com";
        String ldapHost = "172.19.4.6";
        String dn = "";
        String[] ATTRS = {"mail", "sAMAccountName", "name", "distinguishedName"};
        LDAPConnection conn;
        String[] values;
        String[] rechazado = {"Rechazado"};
        String[] vect = new String[4];
        boolean find = false;

        try {
            ldapVersion = LDAPConnection.LDAP_V3;
            //Puerto por Defecto 389
            ldapPort = LDAPConnection.DEFAULT_PORT;
            //ldapPort = LDAPConnection.DEFAULT_SSL_PORT; //Puerto SSL 636
            conn = new LDAPConnection();
            dn = user + base;
            conn.connect(ldapHost, ldapPort);
            conn.bind(ldapVersion, dn, password.getBytes("UTF8"));

            if (conn.isBound()) {

                LDAPAttribute attr = null;
                LDAPEntry nextEntry = null;

                // nextEntry = conn.read("CN=Prueba II,OU=Oficinas Con Servidor,OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com");

                LDAPSearchConstraints cons = new LDAPSearchConstraints();
                cons.setTimeLimit(20000);
                cons.setMaxResults(5000);

                //   LDAPSearchResults res = conn.search("OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(objectclass=user)", ATTRS, false);
                LDAPSearchResults res = conn.search("OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(&(objectclass=user)(sAMAccountName=" + user + "))", ATTRS, false, cons);

                // Loop on results until complete
                while (res.hasMore() && !find) {
                    try {
                        vect[0] = "";
                        vect[1] = "";
                        // The next entry
                        LDAPEntry entry = res.next();
                        int i = 0;

                        LDAPAttributeSet attributeSet = entry.getAttributeSet();
                        Iterator allAttributes = attributeSet.iterator();
                        //Recore los atributos del usuario
                        while (allAttributes.hasNext()) {
                            LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                            String attributeName = attribute.getName();
                            Enumeration allValues = attribute.getStringValues();

                            if (allValues != null) {


                                while (allValues.hasMoreElements()) {
                                    String value = (String) allValues.nextElement();
                                    vect[i] = value;

                                }

                            }
                            i++;
                        }


                        find = true;

                    } catch (LDAPReferralException e) {
                        // Ignore any referrals
                        continue;
                    } catch (LDAPException e) {
                        continue;
                    }
                }
                if (find) {
                    return vect;
                }

                return rechazado;

            } else {
                return rechazado;
            }

        } //catch(LDAPException ex){
        catch (UnsupportedEncodingException ex) {
            return rechazado;
        } catch (LDAPException ex) {
            if (ex.getLDAPErrorMessage().split(",")[2].trim().split(" ")[1].trim().compareTo("773") == 0) {
                return rechazado;
            }
            return rechazado;
        } finally {
            //conn.disconnect();
        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "comprobarCorreo")
    public String comprobarCorreo(@WebParam(name = "correox") String correo) {


        int ldapPort;
        int ldapVersion;
        String base = "@seguroshorizonte.com";
        String ldapHost = "172.19.4.6";
        String dn = "";
        String[] ATTRS = {"mail", "sAMAccountName"};
        LDAPConnection conn;
        String[] values;
        String[] vect = new String[2];
        boolean find = false;
        String user = "uConsBuenVivir";
        String password = "uConsBuenVivir";
        try {
            ldapVersion = LDAPConnection.LDAP_V3;
            //Puerto por Defecto 389
            ldapPort = LDAPConnection.DEFAULT_PORT;
            //ldapPort = LDAPConnection.DEFAULT_SSL_PORT; //Puerto SSL 636
            conn = new LDAPConnection();
            dn = user + base;
            conn.connect(ldapHost, ldapPort);
            conn.bind(ldapVersion, dn, password.getBytes("UTF8"));

            if (conn.isBound()) {

                LDAPAttribute attr = null;
                LDAPEntry nextEntry = null;

                // nextEntry = conn.read("CN=Prueba II,OU=Oficinas Con Servidor,OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com");

                LDAPSearchConstraints cons = new LDAPSearchConstraints();
                cons.setTimeLimit(20000);
                cons.setMaxResults(5000);

                //   LDAPSearchResults res = conn.search("OU=Zona Central,OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(objectclass=user)", ATTRS, false);
                LDAPSearchResults res = conn.search("OU=Zonas Del Pais,DC=seguroshorizonte,DC=com", conn.SCOPE_SUB, "(&(objectclass=user)(mail=" + correo + "))", ATTRS, false, cons);

                // Loop on results until complete
                while (res.hasMore() && !find) {
                    find = true;
                }
                if (find) {
                    return "aceptado";
                }

                return "rechazado";

            } else {
                return "rechazado";
            }

        } //catch(LDAPException ex){
        catch (UnsupportedEncodingException ex) {
            return "rechazado";
        } catch (LDAPException ex) {
            return "rechazado";
        } finally {
            //conn.disconnect();
        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "agregarCargo")
    public void agregarCargo(@WebParam(name = "empleadox") Empleado emp) {


        Cargo c = cargoFacade.buscarNombre(emp.getIdCargo().getNombre().trim().toUpperCase());
        if (c == null) {
            c = new Cargo();
            c.setId(BigDecimal.ZERO);
            c.setNombre(emp.getIdCargo().getNombre().trim().toUpperCase());
            c.setEmpleadoList(null);
            cargoFacade.create(c);

        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaCargos")
    public List<Cargo> retornaCargos(@WebParam(name = "filtrox") String filtro) {


        List<Cargo> car = cargoFacade.listado(filtro);
        Collections.sort(car, new Comparator() {

            public int compare(Object o1, Object o2) {
                Cargo e1 = (Cargo) o1;
                Cargo e2 = (Cargo) o2;
                String codigo1 = e1.getNombre().trim();
                String codigo2 = e2.getNombre().trim();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return car;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarCargo")
    public void editarCargo(@WebParam(name = "cargox") Cargo car) {


        Cargo f = cargoFacade.find(car.getId());
        car.setEmpleadoList(f.getEmpleadoList());

        cargoFacade.edit(car);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarCargo")
    public boolean eliminarCargo(@WebParam(name = "cargox") Cargo car) {

        Cargo fc = cargoFacade.find(car.getId());

        try {
            cargoFacade.remove(fc);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaTiposEvaluacion")
    public List<TipoEvaluacion> retornaTiposEvaluacion() {


        List<TipoEvaluacion> lista = tipoevaluacionFacade.findAll();
        Collections.sort(lista, new Comparator() {

            public int compare(Object o1, Object o2) {
                TipoEvaluacion e1 = (TipoEvaluacion) o1;
                TipoEvaluacion e2 = (TipoEvaluacion) o2;
                String codigo1 = e1.getNombre();
                String codigo2 = e2.getNombre();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return lista;
    }

    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////ORGANIZACION//////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////EMPLEADOS//////////////////////////////////////////////////////
    /**
     * Web service operation
     */
    @WebMethod(operationName = "agregarEmpleado")
    public Empleado agregarEmpleado(@WebParam(name = "empleadox") Empleado emp) {


        Cargo c = cargoFacade.buscarNombre(emp.getIdCargo().getNombre().trim().toUpperCase());
        Usuario us = usuarioFacade.buscar(emp.getNroIdentificacion());

        emp.setIdCargo(c);
        emp.setIdUsuario(us);

        emp.setEmpleadoOpcionList(null);
        emp.setEmpleadoSupervisorList(null);
        emp.setEmpleadoSupervisorList1(null);
        emp.setEstadoEmpleadoList(null);
        emp.setEvaluacionEmpleadoList(null);
        emp.setEvaluacionEmpleadoList1(null);
        emp.setRevisionList(null);
        emp.setSeleccionadoList(null);
        emp.setNombres(emp.getNombres().trim().toUpperCase());
        emp.setApellidos(emp.getApellidos().trim().toUpperCase());
        emp.setCorreo(emp.getCorreo().trim().toLowerCase());
        emp.setCodigo(emp.getCodigo().trim().toUpperCase());

        empleadoFacade.create(emp);
        emp.setIdUsuario(null);
        return emp;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "agregarEstadoEmpleado")
    public void agregarEstadoEmpleado(@WebParam(name = "empleadox") Empleado emp) {


        Empleado em = empleadoFacade.buscarMail(emp.getCorreo());
        EstadoEmpleado es = new EstadoEmpleado();

        es.setActivo(BigInteger.ONE);
        es.setFechaFin(new Date());
        es.setFechaInicio(new Date());
        es.setId(BigDecimal.ZERO);
        es.setIdEmpleado(em);
        es.setUltimoEstado(BigInteger.ONE);
        estadoempleadoFacade.create(es);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarEmpleado")
    public void eliminarEmpleado(@WebParam(name = "empleadox") Empleado emp) {


        Empleado e = empleadoFacade.find(emp.getId());
        EstadoEmpleado em = estadoempleadoFacade.buscarEmpleado(emp);
        em.setActivo(BigInteger.ZERO);
        em.setUltimoEstado(BigInteger.ONE);
        estadoempleadoFacade.edit(em);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reingresarEmpleado")
    public void reingresarEmpleado(@WebParam(name = "empleadox") Empleado emp) {


        Empleado e = empleadoFacade.find(emp.getId());
        EstadoEmpleado em = estadoempleadoFacade.buscarEmpleado(emp);
        em.setActivo(BigInteger.ONE);
        em.setUltimoEstado(BigInteger.ZERO);
        estadoempleadoFacade.edit(em);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarEmpleadoEvaluacion")
    public void eliminarEmpleadoEvaluacion(@WebParam(name = "empleadox") Empleado emp) {


        Empleado e = empleadoFacade.find(emp.getId());

        Iterator it = e.getEvaluacionEmpleadoList1().iterator();
        while (it.hasNext()) {

            EvaluacionEmpleado eva = (EvaluacionEmpleado) it.next();
            EvaluacionEmpleado auxe = evaluacionEmpleadoFacade.find(eva.getId());
            Empleado empleado = empleadoFacade.find(auxe.getIdEmpleado().getId());
            auxe.setIdEvaluador(empleado);
            evaluacionEmpleadoFacade.edit(auxe);


        }




    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarEmpleadoRevision")
    public void eliminarEmpleadoRevision(@WebParam(name = "empleadox") Empleado emp) {


        Empleado e = empleadoFacade.find(emp.getId());

        Iterator it = e.getRevisionList().iterator();
        while (it.hasNext()) {

            Revision rev = (Revision) it.next();
            Revision auxe = revisionFacade.find(rev.getId());
            Empleado empleado = empleadoFacade.find(rev.getIdEvaluacionEmpleado().getIdEmpleado().getId());
            auxe.setIdRevisor(empleado);
            revisionFacade.edit(auxe);


        }




    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarUsuario")
    public void eliminarUsuario(@WebParam(name = "Identificacion") BigInteger nro) {



        Usuario us = usuarioFacade.buscar(nro);
        usuarioFacade.remove(us);


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarEmpleado")
    public void editarEmpleado(@WebParam(name = "empleadox") Empleado sup,
            @WebParam(name = "supervisor") Empleado emp) {

        emp.setIdCargo(cargoFacade.buscarNombre(emp.getIdCargo().getNombre()));


        Empleado supervisorc = this.retornaSupervisor(emp.getId().toString());
        if (sup != null) {

            if (supervisorc == null) {
                this.actualizaSupervisor(emp, sup);

            } else {

                if (supervisorc.getCodigo().compareTo(sup.getCodigo()) != 0) {
                    this.actualizaSupervisor(emp, sup);

                }
            }

        }

        Empleado edita = empleadoFacade.find(emp.getId());
        emp.setEmpleadoOpcionList(edita.getEmpleadoOpcionList());
        emp.setEmpleadoSupervisorList(edita.getEmpleadoSupervisorList());
        emp.setEmpleadoSupervisorList1(edita.getEmpleadoSupervisorList1());
        emp.setEstadoEmpleadoList(edita.getEstadoEmpleadoList());
        emp.setEvaluacionEmpleadoList(edita.getEvaluacionEmpleadoList());
        emp.setEvaluacionEmpleadoList1(edita.getEvaluacionEmpleadoList1());
        emp.setSeleccionadoList(edita.getSeleccionadoList());
        emp.setRevisionList(edita.getRevisionList());


        Usuario us;
        us = usuarioFacade.buscar(emp.getNroIdentificacion());
        emp.setIdUsuario(us);
        empleadoFacade.sincronizar();
        us.setCorreo(emp.getCorreo());
        usuarioFacade.edit(us);
        empleadoFacade.edit(emp);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "creaestadoSupervisor")
    public void creaestadoSupervisor(@WebParam(name = "empleadox") Empleado ide) {



        EmpleadoSupervisor emps = empleadosupervisorFacade.buscarEmpleado(ide.getId().toString());
        EstadoEmpleadoSupervisor e = estadoempleadosupervisorFactorFacade.buscar(emps);

        if (e == null) {
            EstadoEmpleadoSupervisor est = new EstadoEmpleadoSupervisor();
            est.setActivo(BigInteger.ONE);
            est.setFechaFin(new Date());
            est.setFechaInicio(new Date());
            est.setId(BigDecimal.ZERO);
            est.setIdEmpleadoSupervisor(emps);
            est.setUltimoEstado(BigInteger.ONE);
            estadoempleadosupervisorFactorFacade.create(est);
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "actualizaSupervisor")
    public void actualizaSupervisor(@WebParam(name = "idEmpleado") Empleado id,
            @WebParam(name = "supervisor") Empleado ids) {



        Empleado empl = empleadoFacade.find(id.getId());

        if (empl.getEmpleadoSupervisorList1().size() > 0) {
            EmpleadoSupervisor emps = empl.getEmpleadoSupervisorList1().get(0);
            emps.setIdSupervisor(ids);
            empleadosupervisorFacade.edit(emps);

        } else {
            EmpleadoSupervisor emps = new EmpleadoSupervisor();
            emps.setEstadoEmpleadoSupervisorList(null);
            emps.setId(BigDecimal.ZERO);
            emps.setIdSupervisor(ids);
            emps.setIdEmpleado(empl);
            empleadosupervisorFacade.create(emps);

        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarSupervisor")
    public void eliminarSupervisor(@WebParam(name = "empleadox") Empleado emp) {
        Empleado empl = empleadoFacade.find(emp.getId());

        EmpleadoSupervisor emps = empleadosupervisorFacade.buscarEmpleado(empl.getId().toString());

        empleadosupervisorFacade.remove(emps);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "agregarUsuario")
    public boolean agregarUsuario(@WebParam(name = "empleadox") Empleado emp) {

        Usuario us = new Usuario();
        Usuario usBuscar;
        us.setClave("123");
        us.setCorreo(emp.getCorreo());
        us.setEstadoUsuarioList(null);
        us.setEvaluacionList(null);
        us.setId(BigDecimal.ZERO);
        us.setIdentificador(emp.getNombres().split(" ")[0] + " " + emp.getApellidos().charAt(0));
        if (us.getIdentificador().length() > 8) {
            us.setIdentificador(us.getIdentificador().substring(0, 8));
        }
        us.setNroIdentificacion(emp.getNroIdentificacion());
        us.setUsuarioRolList(null);


        usBuscar = usuarioFacade.buscar(emp.getNroIdentificacion());
        if (usBuscar != null) {
            return false;
        }
        try {

            usuarioFacade.create(us);

        } catch (ConstraintViolationException e) {

            return false;
        }

        return true;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaSupervisor")
    public Empleado retornaSupervisor(@WebParam(name = "idEmpleado") String id) {



        EmpleadoSupervisor empleadosupervisor = empleadosupervisorFacade.buscarEmpleado(id);
        if (empleadosupervisor != null) {
            empleadosupervisor.getIdEmpleado().setIdUsuario(null);
            empleadosupervisor.getIdSupervisor().setIdUsuario(null);
        } else {
            return null;
        }




        return empleadosupervisor.getIdSupervisor();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isRecursos")
    public boolean isRecursos(@WebParam(name = "mailempleado") String mail) {


//        empleadoFacade.sincronizar();
        Empleado empleado = empleadoFacade.buscarMail(mail);

        List<UsuarioRol> roles;
        roles = UsuarioRolFacade.buscarRol(empleado.getIdUsuario());

        Iterator it = roles.iterator();
        while (it.hasNext()) {
            UsuarioRol rol = (UsuarioRol) it.next();
            if (rol.getIdRol().getNombre().compareTo("AnalistaRH") == 0) {
                return true;
            }

        }

        return false;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isGerente")
    public Dependencia isGerente(@WebParam(name = "mailgerente") String mail) {


//        empleadoFacade.sincronizar();
        Empleado empleado = empleadoFacade.buscarMail(mail);

        if (empleado.getEmpleadoSupervisorList1().isEmpty()) {
            return empleado.getIdDependencia();
        }

        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEmpleado")
    public Empleado retornaEmpleado(@WebParam(name = "mailempleado") String mail) {


        empleadoFacade.sincronizar();
        Empleado empleado = empleadoFacade.buscarMail(mail);



        return empleado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEmpleadosd")
    public List<Empleado> retornaEmpleadosd(@WebParam(name = "idDependencia") String id, @WebParam(name = "filtronx") String filtron, @WebParam(name = "filtroanx") String filtroa, @WebParam(name = "filtrocx") String filtroc) {


        Dependencia d = dependenciaFacade.buscarId(new BigDecimal(id));
        List<Empleado> emp = empleadoFacade.buscarDependencia(id, filtron, filtroa, filtroc);
        if (d.getIdDependenciaPadre() != null && d.getIdDependenciaPadre().getNivel().toString().compareTo("1") != 0) {
            List<Empleado> emp2 = empleadoFacade.buscarDependencia(d.getIdDependenciaPadre().getId().toString(), filtron, filtroa, filtroc);
            emp.addAll(emp2);
        }
        List<Empleado> emp2 = new ArrayList<Empleado>();

        Iterator it = emp.iterator();
        while (it.hasNext()) {
            Empleado empleado = (Empleado) it.next();
            empleado.setIdUsuario(null);
            if (empleado.getEstadoEmpleadoList().get(0).getActivo().toString().compareTo("1") == 0) {
                emp2.add(empleado);
            }

        }



        return emp2;



    }
//////////////////////////////////////////////////////////////////////
//////////////////////////////DEPENDENCIAS      //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "consultarDependencia")
    public String consultarDependencia(@WebParam(name = "idEmpleadox") String idEmpleado) {
        String nombreCompletoDependencia = "";
        Empleado empleado = empleadoFacade.find(new BigDecimal(idEmpleado));

        Dependencia dependencia = empleado.getIdDependencia();
        while (dependencia != null) {
            if (nombreCompletoDependencia.length() > 1) {
                nombreCompletoDependencia = dependencia.getNombre() + "/" + nombreCompletoDependencia;
            } else {
                nombreCompletoDependencia = dependencia.getNombre();
            }

            dependencia = dependencia.getIdDependenciaPadre();
        }

        return nombreCompletoDependencia;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaDependencias")
    public List<Dependencia> retornaDependencias() {


        List<Dependencia> dep = dependenciaFacade.findAll();
        Collections.sort(dep, new Comparator() {

            public int compare(Object o1, Object o2) {
                Dependencia e1 = (Dependencia) o1;
                Dependencia e2 = (Dependencia) o2;
                BigDecimal codigo1 = e1.getId();
                BigDecimal codigo2 = e2.getId();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return dep;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "agregarDependencia")
    public boolean agregarDependencia(@WebParam(name = "dependenciax") Dependencia dep) {



        try {

            dep.setId(BigDecimal.ZERO);
            dep.setEmpleadoList(null);
            dep.setEvaluacionList(null);
            dep.setDependenciaList(null);
            dependenciaFacade.create(dep);
        } catch (Exception e) {
            return false;

        }

        return true;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarDependencia")
    public void editarDependencia(@WebParam(name = "dependenciax") Dependencia dep) {

        Dependencia d = dependenciaFacade.buscarId(dep.getId());


        dep.setDependenciaList(d.getDependenciaList());
        dep.setEmpleadoList(d.getEmpleadoList());
        dep.setEvaluacionList(d.getEvaluacionList());
        dep.setIdDependenciaPadre(d.getIdDependenciaPadre());

        dependenciaFacade.sincronizar();
        dependenciaFacade.edit(dep);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarDependencia")
    public void eliminarDependencia(@WebParam(name = "dependenciax") Dependencia dep) {

        Dependencia eliminada = dependenciaFacade.buscarId(dep.getId());

        Dependencia principal = dependenciaFacade.buscarNombre("Seguros Horizonte, S.A.");

        Iterator it = eliminada.getEmpleadoList().iterator();
        while (it.hasNext()) {
            Empleado emp = (Empleado) it.next();
            Empleado e = empleadoFacade.find(emp.getId());
            e.setIdDependencia(principal);
            Usuario us;
            us = usuarioFacade.buscar(e.getNroIdentificacion());
            e.setIdUsuario(us);
            empleadoFacade.edit(e);


        }


        dependenciaFacade.remove(eliminada);




    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "cambiarDependencia")
    public void cambiarDependencia(@WebParam(name = "dependenciax") Dependencia dep,
            @WebParam(name = "empleadox") Empleado emp) {


        Empleado em = empleadoFacade.find(emp.getId());
        Dependencia d = dependenciaFacade.find(dep.getId());

        em.setIdDependencia(d);

        Usuario us;
        us = usuarioFacade.buscar(emp.getNroIdentificacion());
        em.setIdUsuario(us);
        empleadoFacade.edit(em);



    }

    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////EVALUACION ADMINISTRACION//////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////EVALUACION//////////////////////////////////////////////////////
    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEvaluaciones")
    public List<Evaluacion> retornaEvaluaciones() {


        List<Evaluacion> eval;


        eval = evaluacionFacade.findAll();

        for (int i = 0; i < eval.size(); i++) {
            eval.get(i).setIdCreador(null);
        }

        Collections.sort(eval, new Comparator() {

            public int compare(Object o1, Object o2) {
                Evaluacion e1 = (Evaluacion) o1;
                Evaluacion e2 = (Evaluacion) o2;
                Date codigo1 = e1.getFechaInicio();
                Date codigo2 = e2.getFechaInicio();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return eval;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearEvaluacion")
    public void crearEvaluacion(@WebParam(name = "evaluacionx") Evaluacion eva) {


        Empleado emp = empleadoFacade.find(eva.getIdCreador().getId());
        Usuario us = new Usuario();
        us.setId(emp.getIdUsuario().getId());
        eva.setIdCreador(us);
        eva.setId(BigDecimal.ZERO);
        eva.setNivelRevision(BigInteger.ONE);
        eva.setEvaluacionEmpleadoList(null);
        try {
            evaluacionFacade.create(eva);
        } catch (ConstraintViolationException e) {
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarEvaluacion")
    public void editarEvaluacion(@WebParam(name = "evaluacionx") Evaluacion eva) {


        Evaluacion eval = evaluacionFacade.find(eva.getId());


        Empleado emp = empleadoFacade.find(eva.getIdCreador().getId());

        Usuario us = new Usuario();
        us.setId(emp.getIdUsuario().getId());
        eval.setIdCreador(us);
        eval.setFechaInicio(eva.getFechaInicio());
        eval.setFechaFin(eva.getFechaFin());


        evaluacionFacade.edit(eval);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarEvaluacion")
    public void eliminarEvaluacion(@WebParam(name = "evaluacionx") Evaluacion eva) {


        Evaluacion eval = evaluacionFacade.find(eva.getId());
        eva.setIdCreador(eval.getIdCreador());
        evaluacionFacade.remove(eva);

    }
    ////////////////////////FACTORES//////////////////////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaFactores")
    public List<Factor> retornaFactores() {


        List<Factor> fac = factoresFacade.findAll();
        Collections.sort(fac, new Comparator() {

            public int compare(Object o1, Object o2) {
                Factor e1 = (Factor) o1;
                Factor e2 = (Factor) o2;
                String codigo1 = e1.getNombre();
                String codigo2 = e2.getNombre();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return fac;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearFactor")
    public void crearFactor(@WebParam(name = "factorx") Factor fac) {

        fac.setId(BigDecimal.ZERO);
        fac.setPresentacionFactorList(null);
        factoresFacade.create(fac);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarFactor")
    public void editarFactor(@WebParam(name = "factorx") Factor fac) {


        Factor f = factoresFacade.find(fac.getId());
        fac.setPresentacionFactorList(f.getPresentacionFactorList());

        factoresFacade.edit(fac);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarFactor")
    public void eliminarFactor(@WebParam(name = "factorx") Factor fac) {

        Factor fc = factoresFacade.buscar(fac.getId());
        factoresFacade.remove(fc);



    }
    ////////////////////////PRESENTACIONES//////////////////////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaPresentaciones")
    public List<PresentacionFactor> retornaPresentaciones(@WebParam(name = "factorx") Factor fac) {


        List<PresentacionFactor> pres = presentacionFactorFacade.buscarfactor(fac);


        return pres;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearPresentacion")
    public void crearPresentacion(@WebParam(name = "factorx") Factor fac,
            @WebParam(name = "presentacion") PresentacionFactor pre) {


        pre.setId(BigDecimal.ZERO);
        pre.setIdFactor(fac);
        pre.setPlanteamientoList(null);
        pre.setFormatoEvaluacionFactorList(null);
        presentacionFactorFacade.create(pre);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarPresentacion")
    public void editarPresentacion(@WebParam(name = "presentacion") PresentacionFactor pres) {


        PresentacionFactor p = presentacionFactorFacade.find(pres.getId());
        pres.setFormatoEvaluacionFactorList(p.getFormatoEvaluacionFactorList());
        pres.setPlanteamientoList(p.getPlanteamientoList());

        presentacionFactorFacade.edit(pres);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarPresentacion")
    public void eliminarPresentacion(@WebParam(name = "presentacion") PresentacionFactor pre) {

        PresentacionFactor pr = presentacionFactorFacade.buscarp(pre);
        presentacionFactorFacade.remove(pr);


    }
    ////////////////////////////////////////PLANTEAMIENTOS////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaPlanteamientos")
    public List<Planteamiento> retornaPlanteamientos(@WebParam(name = "presfactor") PresentacionFactor pfac) {


        List<Planteamiento> plan = planteamientosFacade.lista(pfac.getId());
        Collections.sort(plan, new Comparator() {

            public int compare(Object o1, Object o2) {
                Planteamiento e1 = (Planteamiento) o1;
                Planteamiento e2 = (Planteamiento) o2;
                String codigo1 = e1.getPlanteamiento();;
                String codigo2 = e2.getPlanteamiento();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return plan;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearPlanteamiento")
    public void crearPlanteamiento(@WebParam(name = "planteamientox") Planteamiento plan,
            @WebParam(name = "presfactor") PresentacionFactor pfac) {

        plan.setId(BigDecimal.ZERO);
        plan.setFormatoEvalPlanteamientoList(null);
        plan.setIdPresentacionFactor(pfac);
        planteamientosFacade.create(plan);


        PresentacionFactor pre = presentacionFactorFacade.buscarp(pfac);
        Iterator it = pre.getFormatoEvaluacionFactorList().iterator();
        while (it.hasNext()) {
            FormatoEvaluacionFactor fef = (FormatoEvaluacionFactor) it.next();
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "actualizarPlanteamientoEval")
    public void actualizarPlanteamientoEval(@WebParam(name = "presfactor") PresentacionFactor pfac) {


        PresentacionFactor pre = presentacionFactorFacade.buscarp(pfac);
        Iterator it = pre.getFormatoEvaluacionFactorList().iterator();
        while (it.hasNext()) {
            FormatoEvaluacionFactor fef = (FormatoEvaluacionFactor) it.next();

            Iterator it2 = pre.getPlanteamientoList().iterator();
            while (it2.hasNext()) {
                Planteamiento plan = (Planteamiento) it2.next();

                FormatoEvalPlanteamiento fep;
                fep = formatoevalplanteamientoFacade.buscarb(plan.getId(), fef.getId());
                if (fep == null) {
                    fep = new FormatoEvalPlanteamiento();
                    fep.setId(BigDecimal.ZERO);
                    fep.setIdFormatoEvaluacionFactor(fef);
                    fep.setIdPlanteamiento(plan);
                    BigInteger x = plan.getPesoPlanteamiento().multiply(fef.getPesoFactorEscala()).divide(new BigInteger(String.valueOf(100)));
                    fep.setPesoPlanteamientoEscala(new BigDecimal(x.toString()));
                    formatoevalplanteamientoFacade.create(fep);



                }

            }




        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarPlanteamiento")
    public void editarPlanteamiento(@WebParam(name = "planteamientox") Planteamiento plan) {


        Planteamiento pl = planteamientosFacade.buscar(plan.getId());
        pl.setPesoPlanteamiento(plan.getPesoPlanteamiento());
        pl.setPlanteamiento(plan.getPlanteamiento());
        planteamientosFacade.edit(pl);
        Iterator it = pl.getFormatoEvalPlanteamientoList().iterator();
        while (it.hasNext()) {


            FormatoEvalPlanteamiento fep = (FormatoEvalPlanteamiento) it.next();
            BigInteger x = plan.getPesoPlanteamiento().multiply(fep.getIdFormatoEvaluacionFactor().getPesoFactorEscala()).divide(new BigInteger(String.valueOf(100)));
            fep.setPesoPlanteamientoEscala(new BigDecimal(x.toString()));
            formatoevalplanteamientoFacade.edit(fep);
        }





    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarPlanteamiento")
    public void eliminarPlanteamiento(@WebParam(name = "planteamiento") Planteamiento plan) {

        Planteamiento fc = planteamientosFacade.buscar(plan.getId());
        planteamientosFacade.remove(fc);

    }

    ////////////////////////////////////////FORMATO EVALUACION////////////////////////////////////
    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaFormatos")
    public List<FormatoEvaluacion> retornaFormatos() {


        List<FormatoEvaluacion> formatos = formatoevaluacionFacade.findAll();

        Collections.sort(formatos, new Comparator() {

            public int compare(Object o1, Object o2) {
                FormatoEvaluacion e1 = (FormatoEvaluacion) o1;
                FormatoEvaluacion e2 = (FormatoEvaluacion) o2;
                String codigo1 = e1.getNombre();
                String codigo2 = e2.getNombre();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return formatos;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearformatoEvaluacion")
    public void crearformatoEvaluacion(@WebParam(name = "FormatoEvaluacionx") FormatoEvaluacion form) {

        form.setId(BigDecimal.ZERO);
        form.setEvaluacionList(null);
        formatoevaluacionFacade.create(form);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarformatoEvaluacion")
    public void editarformatoEvaluacion(@WebParam(name = "FormatoEvaluacionx") FormatoEvaluacion form) {


        FormatoEvaluacion f = formatoevaluacionFacade.find(form.getId());
        form.setEvaluacionList(f.getEvaluacionList());
        form.setFormatoEvaluacionFactorList(f.getFormatoEvaluacionFactorList());
        formatoevaluacionFacade.edit(form);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarformatoEvaluacion")
    public void eliminarformatoEvaluacion(@WebParam(name = "FormatoEvaluacionx") FormatoEvaluacion form) {


        FormatoEvaluacion fc = formatoevaluacionFacade.buscar(form.getId());
        formatoevaluacionFacade.remove(fc);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaPresentacionesformatos")
    public List<PresentacionFactor> retornaPresentacionesformatos(@WebParam(name = "formato") BigDecimal form) {

        List<PresentacionFactor> presentaciones = presentacionFactorFacade.findAll();
        List<PresentacionFactor> pres = formatoevaluacionfactorFacade.lista2(form);



        List<PresentacionFactor> regresar = new ArrayList<PresentacionFactor>();

        Iterator it = presentaciones.iterator();
        while (it.hasNext()) {
            PresentacionFactor p = (PresentacionFactor) it.next();

            if (!pres.contains(p)) {
                regresar.add(p);
            }

        }
        Collections.sort(regresar, new Comparator() {

            public int compare(Object o1, Object o2) {
                PresentacionFactor e1 = (PresentacionFactor) o1;
                PresentacionFactor e2 = (PresentacionFactor) o2;
                String codigo1 = e1.getIdFactor().getNombre();
                String codigo2 = e2.getIdFactor().getNombre();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return regresar;
    }

    ////////////////////////////////////////FormatoEvaluacionFactor////////////////////////////////////
    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaformatofactores")
    public List<FormatoEvaluacionFactor> retornaformatofactores(@WebParam(name = "formato") FormatoEvaluacion form) {


        List<FormatoEvaluacionFactor> formatosf = formatoevaluacionfactorFacade.lista(form.getId());
        Collections.sort(formatosf, new Comparator() {

            public int compare(Object o1, Object o2) {
                FormatoEvaluacionFactor e1 = (FormatoEvaluacionFactor) o1;
                FormatoEvaluacionFactor e2 = (FormatoEvaluacionFactor) o2;
                String codigo1 = e1.getIdPresentacionFactor().getIdFactor().getNombre();
                String codigo2 = e2.getIdPresentacionFactor().getIdFactor().getNombre();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return formatosf;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearformatofactor")
    public void crearformatofactor(@WebParam(name = "formato") FormatoEvaluacion form,
            @WebParam(name = "presentacion") PresentacionFactor pres) {


        FormatoEvaluacionFactor formato = new FormatoEvaluacionFactor();
        formato.setId(BigDecimal.ZERO);
        formato.setPesoFactorEscala(BigInteger.ZERO);
        formato.setIdFormatoEvaluacion(form);

        PresentacionFactor pres2 = presentacionFactorFacade.buscarp(pres);

        formato.setIdPresentacionFactor(pres2);
        formato.setFormatoEvalPlanteamientoList(null);
        formatoevaluacionfactorFacade.create(formato);








    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearformatoevalplanteamiento")
    public void crearformatoevalplanteamiento(@WebParam(name = "formato") FormatoEvaluacion form,
            @WebParam(name = "presentacion") PresentacionFactor pres) {




        FormatoEvaluacionFactor formato = formatoevaluacionfactorFacade.buscarFormatoPresentacion(form, pres);

        PresentacionFactor pres2 = presentacionFactorFacade.buscarp(pres);

        Iterator it = pres2.getPlanteamientoList().iterator();
        while (it.hasNext()) {
            Planteamiento pl = (Planteamiento) it.next();
            FormatoEvalPlanteamiento fep = new FormatoEvalPlanteamiento();
            fep.setId(BigDecimal.ZERO);
            fep.setIdFormatoEvaluacionFactor(formato);
            fep.setIdPlanteamiento(pl);
            BigInteger x = pl.getPesoPlanteamiento().multiply(formato.getPesoFactorEscala()).divide(new BigInteger(String.valueOf(100)));
            fep.setPesoPlanteamientoEscala(new BigDecimal(x.toString()));
            formatoevalplanteamientoFacade.create(fep);
        }



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarformatofactor")
    public void editarformatofactor(@WebParam(name = "formatoevaluacionfactorx") FormatoEvaluacionFactor formato) {




        FormatoEvaluacionFactor fr = formatoevaluacionfactorFacade.buscar(formato.getId());
        fr.setPesoFactorEscala(formato.getPesoFactorEscala());
        formatoevaluacionfactorFacade.edit(fr);

        Iterator it = fr.getIdPresentacionFactor().getPlanteamientoList().iterator();
        while (it.hasNext()) {
            Planteamiento pl = (Planteamiento) it.next();
            FormatoEvalPlanteamiento fep = formatoevalplanteamientoFacade.buscar(pl.getId(), fr.getId());
            BigInteger x = pl.getPesoPlanteamiento().multiply(fr.getPesoFactorEscala()).divide(new BigInteger(String.valueOf(100)));
            fep.setPesoPlanteamientoEscala(new BigDecimal(x.toString()));
            formatoevalplanteamientoFacade.edit(fep);

        }


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarformatofactor")
    public void eliminarformatofactor(@WebParam(name = "formatoevaluacionfactorx") FormatoEvaluacionFactor formato) {


        FormatoEvaluacionFactor fc = formatoevaluacionfactorFacade.buscar(formato.getId());
        formatoevaluacionfactorFacade.remove(fc);

    }

    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////REPORTES//////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////EMPLEADOS NO EVALUADOS//////////////////////////////////////////////////////
    /**
     * Web service operation
     */
    @WebMethod(operationName = "reporteEmpleadosno")
    public List<Empleado[]> reporteEmpleadosno(@WebParam(name = "identificadorEvaluacion") String id) {


        List<Empleado> noevaluados;
        List<Dependencia> dependencias;
        List<Empleado[]> emps = new ArrayList<Empleado[]>();

        noevaluados = empleadoFacade.buscarTodos(new BigDecimal(id));
        dependencias = dependenciaFacade.buscarTodos(new BigDecimal(id));

        for (int i = 0; i < noevaluados.size(); i++) {
            noevaluados.get(i).getIdDependencia().setIdDependenciaPadre(dependencias.get(i).getIdDependenciaPadre());

        }


        for (int i = 0; i < noevaluados.size(); i++) {


            Empleado emp = noevaluados.get(i);
            if (emp.getEmpleadoSupervisorList1().size() > 0 && emp.getEstadoEmpleadoList().get(0).getActivo().compareTo(BigInteger.ONE) == 0) {
                Empleado es = noevaluados.get(i).getEmpleadoSupervisorList1().get(0).getIdSupervisor();
                emp.setIdUsuario(null);
                es.setIdUsuario(null);
                Empleado[] par = new Empleado[2];
                par[0] = emp;
                par[1] = es;
                emps.add(par);
            }
        }

        return emps;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reporteEmpleadosnoGerentes")
    public List<Empleado[]> reporteEmpleadosnoGerentes(@WebParam(name = "identificadorEvaluacion") String id, @WebParam(name = "identificadorGerencia") String idg) {


        List<Empleado> noevaluados;
        List<Dependencia> dependencias;
        List<Empleado[]> emps = new ArrayList<Empleado[]>();

        noevaluados = empleadoFacade.buscarTodosGerentes(new BigDecimal(id), new BigDecimal(idg));
        dependencias = dependenciaFacade.buscarTodosGerentes(new BigDecimal(id), new BigDecimal(idg));

        for (int i = 0; i < noevaluados.size(); i++) {
            noevaluados.get(i).getIdDependencia().setIdDependenciaPadre(dependencias.get(i).getIdDependenciaPadre());

        }


        for (int i = 0; i < noevaluados.size(); i++) {


            Empleado emp = noevaluados.get(i);
            if (emp.getEmpleadoSupervisorList1().size() > 0 && emp.getEstadoEmpleadoList().get(0).getActivo().compareTo(BigInteger.ONE) == 0) {
                Empleado es = noevaluados.get(i).getEmpleadoSupervisorList1().get(0).getIdSupervisor();
                emp.setIdUsuario(null);
                es.setIdUsuario(null);
                Empleado[] par = new Empleado[2];
                par[0] = emp;
                par[1] = es;
                emps.add(par);
            }
        }

        return emps;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reporteInformacionEvaluacion")
    public String[][] reporteInformacionEvaluacion(@WebParam(name = "identificadorEvaluacion") String id) {

        return evaluacionEmpleadoFacade.informacionEvaluacion(new BigDecimal(id));


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "RetornaEvaluacionesReporte")
    public List<Evaluacion> RetornaEvaluacionesReporte() {


        List<Evaluacion> eval, eval2;
        eval2 = new ArrayList<Evaluacion>();

        eval = evaluacionFacade.findAll();

        for (int i = 0; i < eval.size(); i++) {
            eval.get(i).setIdCreador(null);
            if (!eval.get(i).getEvaluacionEmpleadoList().isEmpty()) {
                eval2.add(eval.get(i));
            }
        }

        Collections.sort(eval2, new Comparator() {

            public int compare(Object o1, Object o2) {
                Evaluacion e1 = (Evaluacion) o1;
                Evaluacion e2 = (Evaluacion) o2;
                Date codigo1 = e1.getFechaInicio();
                Date codigo2 = e2.getFechaInicio();

                if (codigo1.after(codigo2)) {
                    return 1;
                } else if (codigo1.before(codigo2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return eval2;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "EvaluacionFecha")
    public List<Evaluacion> EvaluacionFecha(@WebParam(name = "fix") Date fi, @WebParam(name = "ffx") Date ff) throws ParseException {

        List<Evaluacion> eval = evaluacionFacade.findAll();
        List<Evaluacion> eva2 = new ArrayList<Evaluacion>();
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < eval.size(); i++) {
            eval.get(i).setIdCreador(null);
            Date d1, d2, d3, dx;
            String f;
            d1 = eval.get(i).getFechaInicio();
            f = formatoDeFecha.format(d1);
            String an = f.split("/")[2];
            if (Integer.parseInt(f.split("/")[2]) < 2000) {
                an = String.valueOf(Integer.parseInt(f.split("/")[2]) + 2000);
            }
            dx = formatoDeFecha.parse(f.split("/")[0] + "/" + f.split("/")[1] + "/" + an);
            d2 = fi;
            d3 = ff;
            if (dx.compareTo(d2) >= 0 && dx.compareTo(d3) <= 0) {
                eva2.add(eval.get(i));
            }
        }

        Collections.sort(eva2, new Comparator() {

            public int compare(Object o1, Object o2) {
                Evaluacion e1 = (Evaluacion) o1;
                Evaluacion e2 = (Evaluacion) o2;
                Date codigo1 = e1.getFechaInicio();
                Date codigo2 = e2.getFechaInicio();

                if (codigo1.after(codigo2)) {
                    return 1;
                } else if (codigo1.before(codigo2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return eva2;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "RetornaDependenciasNivel")
    public List<Dependencia> RetornaDependenciasNivel(@WebParam(name = "nivelx") String nivel) {

        return dependenciaFacade.buscarNivel(nivel);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "puntosEvaluacionAcumulados")
    public String[][] puntosEvaluacionAcumulados(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Dependenciax") List<String> dep) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Dependencia> dependencias = new ArrayList<Dependencia>();
        String[][] resultados;
        List<Object[]> lista;
        resultados = new String[dep.size()][];
        for (int i = 0; i < dep.size(); i++) {

            dependencias.add(dependenciaFacade.buscarDescripcion(dep.get(i)));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < dep.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0";

            }

        }



        for (int j = 0; j < dependencias.size(); j++) {

            if (nivel.compareTo("1") == 0 || nivel.compareTo("2") == 0) {

                lista = evaluacionFacade.acumuladoPuntos(evaluaciones, dependencias.get(j));
            } else {
                lista = evaluacionFacade.acumuladoPuntossucursal(evaluaciones, dependencias.get(j));

            }
            for (int i = 0; i < evaluaciones.size(); i++) {

                for (int k = 0; k < lista.size(); k++) {

                    Object o = lista.get(k)[1];
                    BigDecimal n = (BigDecimal) o;
                    if (n.toString().compareTo(evaluaciones.get(i).getId().toString()) == 0) {
                        Object p = lista.get(k)[0];
                        BigDecimal x = (BigDecimal) p;
                        resultados[j][i] = x.toString();
                    }
                }




            }

        }


        return resultados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "puntosEvaluacionPromedios")
    public String[][] puntosEvaluacionPromedios(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Dependenciax") List<String> dep) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Dependencia> dependencias = new ArrayList<Dependencia>();
        String[][] resultados;
        List<Object[]> lista;
        resultados = new String[dep.size()][];
        for (int i = 0; i < dep.size(); i++) {

            dependencias.add(dependenciaFacade.buscarDescripcion(dep.get(i)));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < dep.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0";

            }

        }



        for (int j = 0; j < dependencias.size(); j++) {

            if (nivel.compareTo("1") == 0 || nivel.compareTo("2") == 0) {

                lista = evaluacionFacade.promedioPuntos(evaluaciones, dependencias.get(j));
            } else {
                lista = evaluacionFacade.promedioPuntossucursal(evaluaciones, dependencias.get(j));
            }
            for (int i = 0; i < evaluaciones.size(); i++) {

                for (int k = 0; k < lista.size(); k++) {

                    Object o = lista.get(k)[1];
                    BigDecimal n = (BigDecimal) o;
                    if (n.toString().compareTo(evaluaciones.get(i).getId().toString()) == 0) {
                        Object p = lista.get(k)[0];
                        BigDecimal x = (BigDecimal) p;
                        resultados[j][i] = x.toString();
                    }
                }





            }

        }


        return resultados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "puntosEvaluacionCantidad")
    public String[][] puntosEvaluacionCantidad(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Dependenciax") List<String> dep) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Dependencia> dependencias = new ArrayList<Dependencia>();
        List<Object[]> lista = null;
        Object cantidadEmpleados = null;
        String[][] resultados;
        String cepp;
        resultados = new String[dep.size()][];
        for (int i = 0; i < dep.size(); i++) {

            dependencias.add(dependenciaFacade.buscarDescripcion(dep.get(i)));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < dep.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0-1";

            }

        }




        for (int j = 0; j < dependencias.size(); j++) {

            if (nivel.compareTo("1") == 0 || nivel.compareTo("2") == 0) {
                lista = evaluacionFacade.cantidadEmpleados(evaluaciones, dependencias.get(j));
                cantidadEmpleados = evaluacionFacade.cantidadEmpleadosdependencia(dependencias.get(j));
            } else {
                lista = evaluacionFacade.cantidadEmpleadossucursal(evaluaciones, dependencias.get(j));
                cantidadEmpleados = evaluacionFacade.cantidadEmpleadosdependenciasucursal(dependencias.get(j));

            }
            cepp = cantidadEmpleados.toString();

            for (int i = 0; i < evaluaciones.size(); i++) {


                for (int k = 0; k < lista.size(); k++) {

                    Object o = lista.get(k)[1];
                    BigDecimal n = (BigDecimal) o;
                    if (n.toString().compareTo(evaluaciones.get(i).getId().toString()) == 0) {
                        Object p = lista.get(k)[0];
                        BigDecimal x = (BigDecimal) p;

                        resultados[j][i] = x.toString() + "-" + cepp;
                    }
                }


            }





        }


        return resultados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEmpleadosGrupodependencias")
    public List<Empleado> retornaEmpleadosGrupodependencias(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Dependenciax") List<String> dep) {

        List<Empleado> listae = null;
        List<Empleado> lista2 = new ArrayList<Empleado>();
        List<Dependencia> dependencias = new ArrayList<Dependencia>();
        for (int i = 0; i < dep.size(); i++) {

            dependencias.add(dependenciaFacade.buscarDescripcion(dep.get(i)));

        }

        if (nivel.compareTo("1") == 0 || nivel.compareTo("2") == 0) {

            listae = empleadoFacade.buscarGrupoDependenciaG(dependencias);
        } else {
            listae = empleadoFacade.buscarGrupoDependenciaD(dependencias);

        }

        for (int i = 0; i < listae.size(); i++) {
            listae.get(i).setIdUsuario(null);
            if (listae.get(i).getEstadoEmpleadoList().get(0).getActivo().compareTo(BigInteger.ONE) == 0) {
                lista2.add(listae.get(i));
            }

        }
        return lista2;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reporteHistoricoEmpleados")
    public String[][] reporteHistoricoEmpleados() {

        List<Evaluacion> evaluaciones = evaluacionFacade.findAll();

        List<Empleado> auxempleados = empleadoFacade.buscarTodosHistorico();
        List<Empleado> empleados = new ArrayList<Empleado>();
        List<Dependencia> auxdependencias = dependenciaFacade.buscarTodosHistorico();
        List<Dependencia> dependencias = new ArrayList<Dependencia>();
        String[][] historico;
        Object[] objeto = new Object[2];

        for (int i = 0; i < auxempleados.size(); i++) {

            if (auxempleados.get(i).getEvaluacionEmpleadoList().size() > 0 && auxempleados.get(i).getEstadoEmpleadoList().get(0).getActivo().compareTo(BigInteger.ONE) == 0) {
                empleados.add(auxempleados.get(i));
                dependencias.add(auxdependencias.get(i));
            }

        }

        Collections.sort(evaluaciones, new Comparator() {

            public int compare(Object o1, Object o2) {
                Evaluacion e1 = (Evaluacion) o1;
                Evaluacion e2 = (Evaluacion) o2;
                int codigo1 = Integer.parseInt(e1.getId().toString());
                int codigo2 = Integer.parseInt(e2.getId().toString());

                if (codigo1 > codigo2) {
                    return 1;
                } else if (codigo1 < codigo2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });


        historico = new String[empleados.size()][];
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).getIdDependencia().setIdDependenciaPadre(dependencias.get(i).getIdDependenciaPadre());

            historico[i] = new String[evaluaciones.size() + 5];
        }

        for (int i = 0; i < empleados.size(); i++) {

            for (int j = 0; j < evaluaciones.size(); j++) {

                historico[i][j] = "0";

            }
        }

        for (int i = 0; i < empleados.size(); i++) {

            Empleado emp = empleados.get(i);
            if (emp.getEvaluacionEmpleadoList().size() > 0) {
                for (int k = 0; k < emp.getEvaluacionEmpleadoList().size(); k++) {

                    for (int j = 0; j < evaluaciones.size(); j++) {


                        if (emp.getEvaluacionEmpleadoList().get(k).getIdEvaluacion().getId().toString().toString().compareTo(evaluaciones.get(j).getId().toString()) == 0) {
                            historico[i][j] = emp.getEvaluacionEmpleadoList().get(k).getPuntos().toString();
                            break;
                        }
                    }


                }



            }




        }
        for (int i = 0; i < empleados.size(); i++) {
            double prom = 0;
            int resta = 0;
            int doble = 0;

            for (int j = 0; j < evaluaciones.size(); j++) {

                prom += Double.parseDouble(historico[i][j]);
                if (empleados.get(i).getFechaIngreso().compareTo(evaluaciones.get(j).getFechaInicio()) > 0) {
                    resta += Integer.parseInt(evaluaciones.get(j).getPeso().toString());
                }
                if (Integer.parseInt(evaluaciones.get(j).getPeso().toString()) > 1) {
                    doble += Integer.parseInt(evaluaciones.get(j).getPeso().toString()) - 1;
                }

            }
            prom /= (evaluaciones.size() - resta + doble);
            historico[i][evaluaciones.size()] = String.valueOf(prom);
            if (empleados.get(i).getIdDependencia().getIdDependenciaPadre() != null) {
                historico[i][evaluaciones.size() + 1] = empleados.get(i).getIdDependencia().getIdDependenciaPadre().getNombre();
            } else {
                historico[i][evaluaciones.size() + 1] = "";
            }
            historico[i][evaluaciones.size() + 2] = empleados.get(i).getIdDependencia().getNombre();
            historico[i][evaluaciones.size() + 3] = empleados.get(i).getNombres();
            historico[i][evaluaciones.size() + 4] = empleados.get(i).getApellidos();
        }


        return historico;

    }

    @WebMethod(operationName = "pEAEmpleados")
    public String[][] pEAEmpleados(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Empleadosx") List<String> emp) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Empleado> empleados = new ArrayList<Empleado>();
        String[][] resultados;
        List<Object[]> lista;
        resultados = new String[emp.size()][];
        for (int i = 0; i < emp.size(); i++) {

            empleados.add(empleadoFacade.find(new BigDecimal(emp.get(i))));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < emp.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0";

            }

        }



        for (int j = 0; j < empleados.size(); j++) {


            lista = evaluacionFacade.acumuladoPuntosEmpleado(evaluaciones, empleados.get(j));


            for (int i = 0; i < evaluaciones.size(); i++) {

                for (int k = 0; k < lista.size(); k++) {

                    Object o = lista.get(k)[1];
                    BigDecimal n = (BigDecimal) o;
                    if (n.toString().compareTo(evaluaciones.get(i).getId().toString()) == 0) {
                        Object p = lista.get(k)[0];
                        BigDecimal x = (BigDecimal) p;
                        resultados[j][i] = x.toString();
                    }
                }
            }





        }
        return resultados;
    }

    @WebMethod(operationName = "pEAEmpleadosP")
    public String[][] pEAEmpleadosP(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Empleadosx") List<String> emp) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Empleado> empleados = new ArrayList<Empleado>();
        String[][] resultados;
        List<Object[]> lista;
        resultados = new String[emp.size()][];
        for (int i = 0; i < emp.size(); i++) {

            empleados.add(empleadoFacade.find(new BigDecimal(emp.get(i))));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < emp.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0";

            }

        }



        for (int j = 0; j < empleados.size(); j++) {


            lista = evaluacionFacade.acumuladoPuntosEmpleadoPeso(evaluaciones, empleados.get(j));


            for (int i = 0; i < evaluaciones.size(); i++) {

                for (int k = 0; k < lista.size(); k++) {

                    Object o = lista.get(k)[1];
                    BigDecimal n = (BigDecimal) o;
                    if (n.toString().compareTo(evaluaciones.get(i).getId().toString()) == 0) {
                        Object p = lista.get(k)[0];
                        BigDecimal x = (BigDecimal) p;
                        resultados[j][i] = x.setScale(2, RoundingMode.CEILING).toString();
                    }
                }
            }





        }
        return resultados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "pEPE")
    public String[][] pEPE(@WebParam(name = "nivelx") String nivel, @WebParam(name = "Evaluacionx") List<String> Eval, @WebParam(name = "Empleadosx") List<String> emp) {

        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        List<Empleado> empleados = new ArrayList<Empleado>();
        String[][] resultados;
        List<Object[]> lista;
        resultados = new String[emp.size()][];
        for (int i = 0; i < emp.size(); i++) {

            empleados.add(empleadoFacade.find(new BigDecimal(emp.get(i))));
            resultados[i] = new String[Eval.size()];
        }
        for (int i = 0; i < Eval.size(); i++) {

            evaluaciones.add(evaluacionFacade.buscarNombre(Eval.get(i)));

        }
        for (int i = 0; i < emp.size(); i++) {

            for (int j = 0; j < Eval.size(); j++) {

                resultados[i][j] = "0";

            }

        }



        for (int j = 0; j < empleados.size(); j++) {



            lista = evaluacionFacade.promedioPuntosEmpleado(evaluaciones, empleados.get(j));


            Object p = lista.get(0);
            if (p != null) {
                BigDecimal x = (BigDecimal) p;
                resultados[j][0] = x.toString();
            } else {
                resultados[j][0] = "0";
            }

        }
        return resultados;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "empleadosEliminados")
    public List<Empleado> empleadosEliminados() {



        List<Empleado> emp = empleadoFacade.buscarEliminados();

        for (int i = 0; i < emp.size(); i++) {
            emp.get(i).setIdUsuario(null);

        }

        Collections.sort(emp, new Comparator() {

            public int compare(Object o1, Object o2) {
                Empleado e1 = (Empleado) o1;
                Empleado e2 = (Empleado) o2;
                String codigo1 = e1.getNombres().trim();
                String codigo2 = e2.getNombres().trim();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return emp;
    }
    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////CANJE//////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////PERIODO//////////////////////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaPeriodos")
    public List<Periodo> retornaPeriodos() {


        List<Periodo> per = periodoFacade.findAll();
        Collections.sort(per, new Comparator() {

            public int compare(Object o1, Object o2) {
                Periodo e1 = (Periodo) o1;
                Periodo e2 = (Periodo) o2;
                Date codigo1 = e1.getFechaInicioEvaluacion();
                Date codigo2 = e2.getFechaInicioEvaluacion();

                if (codigo1.compareTo(codigo2) > 0) {
                    return 1;
                } else if (codigo1.compareTo(codigo2) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return per;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearPeriodo")
    public void crearPeriodo(@WebParam(name = "periodox") Periodo per) {

        per.setId(BigDecimal.ZERO);
        per.setCorteList(null);
        periodoFacade.create(per);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarPeriodo")
    public void editarPeriodo(@WebParam(name = "periodox") Periodo per) {


        Periodo p = periodoFacade.find(per.getId());
        per.setCorteList(p.getCorteList());

        periodoFacade.edit(per);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarPeriodo")
    public void eliminarPeriodo(@WebParam(name = "periodox") Periodo per) {

        Periodo fc = periodoFacade.find(per.getId());
        periodoFacade.remove(fc);



    }
    ////////////////////////CORTE//////////////////////////////////////////////////////

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaCorte")
    public List<Corte> retornaCorte() {


        List<Corte> cor = corteFacade.findAll();

        Collections.sort(cor, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                Canje e1 = (Canje) o1;
                Canje e2 = (Canje) o2;
                int codigo1 = Integer.parseInt(e1.getId().toString());
                int codigo2 = Integer.parseInt(e2.getId().toString());
                if (codigo1 > codigo2) {
                    return 1;
                } else if (codigo1 < codigo2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });







        return cor;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "crearCorte")
    public void crearCorte(@WebParam(name = "periodox") Periodo per, @WebParam(name = "cortex") Corte cor, @WebParam(name = "montox") BigDecimal monto) {


        Periodo p = periodoFacade.find(per.getId());
        String[] caracteristicas;

        caracteristicas = evaluacionEmpleadoFacade.datosEvaluaciones(p, cor.getMinimoPromedioPuntos());
        BigDecimal tasa_general = null, tasa_personal;
        BigDecimal tasag, tasap;
        float acumuladosel = 0;



        ////////////////////////////////////////////
        List<Object[]> emp = evaluacionEmpleadoFacade.empleadosSeleccionados(cor);

        List<Object[]> emp2 = evaluacionEmpleadoFacade.empleadosSeleccionadosC(cor);


        for (int i = 0; i < emp.size(); i++) {

            Empleado e = (Empleado) emp.get(i)[0];
            Empleado aux = empleadoFacade.find(e.getId());
            Seleccionado sel = new Seleccionado();
            sel.setId(BigDecimal.ZERO);
            sel.setCanjeList(null);
            sel.setIdEmpleado(e);
            Object p1 = emp.get(i)[1];
            Object q = emp2.get(i)[1];
            float a = Float.parseFloat(String.valueOf(p1));
            float b = Float.parseFloat(String.valueOf(q));
            float c = 0;
            if (b != 0) {
                c = a / b;
            }
            if (c >= Float.parseFloat(cor.getMinimoPromedioPuntos().toString())) {


                BigDecimal x = new BigDecimal(String.valueOf(c)).setScale(2, RoundingMode.CEILING);



                sel.setPromedioPuntos(x);
                sel.setPuntosAcumulados(new BigInteger("0"));
                float puntos = 0;
                for (int j = 0; j < e.getEvaluacionEmpleadoList().size(); j++) {

                    EvaluacionEmpleado eva = e.getEvaluacionEmpleadoList().get(j);
                    if (eva.getIdEvaluacion().getFechaInicio().compareTo(cor.getIdPeriodo().getFechaInicioEvaluacion()) > 0 && eva.getIdEvaluacion().getFechaInicio().compareTo(cor.getIdPeriodo().getFechaFinEvaluacion()) < 0) {
                        puntos += Integer.parseInt(eva.getPuntos().toString());
                    }

                }

                float deduccion = 0;
                if (!e.getSeleccionadoList().isEmpty()) {

                    for (int j = 0; j < e.getSeleccionadoList().size(); j++) {

                        Seleccionado s = e.getSeleccionadoList().get(j);


                        if (s.getIdCorte().getIdPeriodo().getId().compareTo(cor.getIdPeriodo().getId()) == 0) {


                            if (!s.getCanjeList().isEmpty()) {

                                for (int k = 0; k < s.getCanjeList().size(); k++) {

                                    deduccion += Integer.parseInt(s.getCanjeList().get(k).getPuntos().toString());
                                }



                            }



                        }




                    }


                }

                float totalp = puntos - deduccion;

                acumuladosel += totalp;

            }
        }
////////////////////////////////////////////////////////////////        



        if (p.getCorteList().isEmpty()) {


            List<Empleado> e = empleadoFacade.findAll();

            tasa_general = new BigDecimal(caracteristicas[0]).add(new BigDecimal(e.size() * 1000 * 5)).add(new BigDecimal(caracteristicas[2]));

            tasag = monto.divide(tasa_general, 2, RoundingMode.CEILING).setScale(2, RoundingMode.CEILING);

            tasap = new BigDecimal(String.valueOf(Float.parseFloat(caracteristicas[1]) - acumuladosel)).multiply(tasag).divide(new BigDecimal(String.valueOf(acumuladosel)), 2, RoundingMode.CEILING).setScale(2, RoundingMode.CEILING);


            cor.setTasaCambio(tasag.add(tasap));

        } else {



            tasa_general = new BigDecimal(caracteristicas[0]).add(new BigDecimal(caracteristicas[2]));

            tasag = monto.divide(tasa_general, 2, RoundingMode.CEILING).setScale(2, RoundingMode.CEILING);

            tasap = new BigDecimal(String.valueOf(Float.parseFloat(caracteristicas[1]) - acumuladosel)).multiply(tasag).divide(new BigDecimal(String.valueOf(acumuladosel)), 2, RoundingMode.CEILING).setScale(2, RoundingMode.CEILING);


            cor.setTasaCambio(tasag.add(tasap));



        }


        cor.setId(BigDecimal.ZERO);
        cor.setIdPeriodo(p);
        cor.setSeleccionadoList(null);
        cor.setSaldoInicial(tasa_general);
        cor.setSaldoIngreso(BigDecimal.ZERO);
        cor.setSaldoCorte(new BigDecimal(BigInteger.ONE));
        corteFacade.create(cor);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarCorte")
    public void editarCorte(@WebParam(name = "cortex") Corte cor) {


        Corte c = corteFacade.find(cor.getId());
        cor.setSeleccionadoList(c.getSeleccionadoList());

        corteFacade.edit(cor);

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "corteMaximo")
    public Corte corteMaximo() {


        return corteFacade.buscarMaximo();


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "datosEvaluaciones")
    public String[] datosEvaluaciones(@WebParam(name = "periodox") Periodo pe, @WebParam(name = "minimox") BigDecimal minimo) {

        String[] caracteristicas;
        Periodo p = periodoFacade.find(pe.getId());



        caracteristicas = evaluacionEmpleadoFacade.datosEvaluaciones(p, minimo);



        return caracteristicas;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "seleccionarEmpleados")
    public void seleccionarEmpleados(@WebParam(name = "cortex") Corte cor) {


        List<Object[]> emp = evaluacionEmpleadoFacade.empleadosSeleccionados(cor);

        List<Object[]> emp2 = evaluacionEmpleadoFacade.empleadosSeleccionadosC(cor);


        for (int i = 0; i < emp.size(); i++) {

            Empleado e = (Empleado) emp.get(i)[0];
            Empleado aux = empleadoFacade.find(e.getId());
            Seleccionado sel = new Seleccionado();
            sel.setId(BigDecimal.ZERO);
            sel.setCanjeList(null);
            sel.setIdEmpleado(e);
            Object p = emp.get(i)[1];
            Object q = emp2.get(i)[1];
            float a = Float.parseFloat(String.valueOf(p));
            float b = Float.parseFloat(String.valueOf(q));
            float c = 0;
            if (b != 0) {
                c = a / b;
            }
            if (c >= Float.parseFloat(cor.getMinimoPromedioPuntos().toString())) {


                BigDecimal x = new BigDecimal(String.valueOf(c)).setScale(2, RoundingMode.CEILING);



                sel.setPromedioPuntos(x);
                sel.setPuntosAcumulados(new BigInteger("0"));
                int puntos = 0;
                for (int j = 0; j < e.getEvaluacionEmpleadoList().size(); j++) {

                    EvaluacionEmpleado eva = e.getEvaluacionEmpleadoList().get(j);
                    if (eva.getIdEvaluacion().getFechaInicio().compareTo(cor.getIdPeriodo().getFechaInicioEvaluacion()) > 0 && eva.getIdEvaluacion().getFechaInicio().compareTo(cor.getIdPeriodo().getFechaFinEvaluacion()) < 0) {
                        puntos += Integer.parseInt(eva.getPuntos().toString());
                    }

                }
                sel.setPuntosAcumulados(new BigInteger(String.valueOf(puntos)));

                int deduccion = 0;
                if (!e.getSeleccionadoList().isEmpty()) {

                    for (int j = 0; j < e.getSeleccionadoList().size(); j++) {

                        Seleccionado s = e.getSeleccionadoList().get(j);


                        if (s.getIdCorte().getIdPeriodo().getId().compareTo(cor.getIdPeriodo().getId()) == 0) {


                            if (!s.getCanjeList().isEmpty()) {

                                for (int k = 0; k < s.getCanjeList().size(); k++) {

                                    deduccion += Integer.parseInt(s.getCanjeList().get(k).getPuntos().toString());
                                }



                            }



                        }




                    }


                }

                int totalp = puntos - deduccion;

                sel.setPuntosDisponibles(new BigInteger(String.valueOf(totalp)));


                sel.setSaldoInicialCorte(BigInteger.ZERO);
                sel.setSaldoFinCorte(BigInteger.ZERO);
                sel.setTotalCanjesCorte(BigInteger.ZERO);
                sel.setIdCorte(cor);
                seleccionadoFacade.create(sel);

            }
        }



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEmpleadosSeleccionados")
    public List<Seleccionado> retornaEmpleadosSeleccionados(@WebParam(name = "cortex") String cor, @WebParam(name = "filtronx") String filtron, @WebParam(name = "filtroanx") String filtroa, @WebParam(name = "filtrocx") String filtroc) {


        List<Seleccionado> sel;
        Corte c = corteFacade.find(new BigDecimal(cor));

        sel = seleccionadoFacade.buscarCorte(c, filtron, filtroa, filtroc);
        return sel;


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaCorteSeleccionado")
    public List<Canje> retornaCorteSeleccionado(@WebParam(name = "eseleccionado") Seleccionado sel) {




        return canjeFacade.buscar(sel.getId());

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "procesarCanje")
    public boolean procesarCanje(@WebParam(name = "eseleccionado") Seleccionado sel, @WebParam(name = "puntosx") String puntos) {


        Canje c = new Canje();
        c.setId(BigDecimal.ZERO);
        c.setIdComercio(BigInteger.ZERO);
        c.setIdSeleccionado(seleccionadoFacade.find(sel.getId()));
        c.setMontoTasa(sel.getIdCorte().getTasaCambio());

        c.setPuntos(new BigInteger(puntos));

        try {
            canjeFacade.create(c);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaultimoCanje")
    public Canje retornaultimoCanje(@WebParam(name = "eseleccionado") Seleccionado sel) {


        Seleccionado selec = seleccionadoFacade.find(sel.getId());
        Canje aux = null;


        if (!selec.getCanjeList().isEmpty()) {
            aux = selec.getCanjeList().get(0);

            for (int i = 1; i < selec.getCanjeList().size(); i++) {
                if (Integer.parseInt(selec.getCanjeList().get(i).getId().toString()) > Integer.parseInt(aux.getId().toString())) {
                    aux = selec.getCanjeList().get(i);
                }

            }
            aux = canjeFacade.find(aux.getId());


        }

        return aux;


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornatodosCanjes")
    public List<Canje> retornatodosCanjes(@WebParam(name = "eseleccionado") Seleccionado sel) {


        Seleccionado selec = seleccionadoFacade.find(sel.getId());

        List<Canje> lista = selec.getCanjeList();
        Collections.sort(lista, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                Canje e1 = (Canje) o1;
                Canje e2 = (Canje) o2;
                int codigo1 = Integer.parseInt(e1.getId().toString());
                int codigo2 = Integer.parseInt(e2.getId().toString());
                if (codigo1 > codigo2) {
                    return 1;
                } else if (codigo1 < codigo2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });


        return lista;


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "actualizarSeleccionado")
    public boolean actualizarSeleccionado(@WebParam(name = "eseleccionado") Seleccionado sel, @WebParam(name = "puntosx") String puntos) {


        Seleccionado selec = seleccionadoFacade.find(sel.getId());

        float disponibles = Float.parseFloat(selec.getPuntosDisponibles().toString()) - Float.parseFloat(puntos);

        int puntosx = (int) disponibles;
        selec.setPuntosDisponibles(new BigInteger(String.valueOf(puntosx)));

        selec.setTotalCanjesCorte(selec.getTotalCanjesCorte().add(BigInteger.ONE));

        try {
            seleccionadoFacade.edit(selec);
        } catch (Exception e) {
            return false;
        }

        return true;


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "actualizarCorte")
    public void actualizarCorte(@WebParam(name = "eseleccionado") Seleccionado sel, @WebParam(name = "puntosx") String puntos) {


        Seleccionado selec = seleccionadoFacade.find(sel.getId());

        Corte cx = selec.getIdCorte();

        Corte cor = corteFacade.find(cx.getId());

        cor.setSaldoIngreso(cor.getSaldoIngreso().add(new BigDecimal(puntos)));

        corteFacade.edit(cor);


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "editarCanje")
    public void editarCanje(@WebParam(name = "Canjex") Canje can) {


        Canje act = canjeFacade.find(can.getId());
        act.setIdComercio(can.getIdComercio());
        canjeFacade.edit(act);



    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "planteamientosS")
    public List<String[]> planteamientosS(@WebParam(name = "Evaluacionx") Evaluacion eva, @WebParam(name = "Empleadox") Empleado emp) {



        List<String[]> plant = new ArrayList<String[]>();

        Empleado empleado = empleadoFacade.find(emp.getId());
        Evaluacion eval = evaluacionFacade.find(eva.getId());

        if (!empleado.getEvaluacionEmpleadoList().isEmpty()) {

            for (int i = 0; i < empleado.getEvaluacionEmpleadoList().size(); i++) {

                EvaluacionEmpleado evae = empleado.getEvaluacionEmpleadoList().get(i);

                if (evae.getIdEvaluacion().getId().compareTo(eval.getId()) == 0) {


                    if (!evae.getResultadoFactorList().isEmpty()) {

                        for (int j = 0; j < evae.getResultadoFactorList().size(); j++) {

                            ResultadoFactor rs = evae.getResultadoFactorList().get(j);




                            String[] values = new String[3];


                            values[0] = rs.getIdPlanteamiento().getIdPlanteamiento().getIdPresentacionFactor().getIdFactor().getNombre();
                            values[1] = rs.getIdPlanteamiento().getIdPlanteamiento().getIdPresentacionFactor().getIdFactor().getDescripcion();
                            values[2] = rs.getIdPlanteamiento().getIdPlanteamiento().getPlanteamiento();
                            plant.add(values);

                        }


                    }


                }


            }


        }

        return plant;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "retornaEvaluacionesEmpleado")
    public List<EvaluacionEmpleado> retornaEvaluacionesEmpleado(@WebParam(name = "empleadox") Empleado emp) {

        List<EvaluacionEmpleado> evaluaciones = null;
        Empleado e;
        e = empleadoFacade.find(emp.getId());
        if (e != null) {

            evaluaciones = evaluacionEmpleadoFacade.buscarempleado(e.getId());
            Collections.sort(evaluaciones, new Comparator() {

                public int compare(Object o1, Object o2) {
                    EvaluacionEmpleado e1 = (EvaluacionEmpleado) o1;
                    EvaluacionEmpleado e2 = (EvaluacionEmpleado) o2;
                    int codigo1 = Integer.parseInt(e1.getIdEvaluacion().getId().toString());
                    int codigo2 = Integer.parseInt(e2.getIdEvaluacion().getId().toString());

                    if (codigo1 > codigo2) {
                        return 1;
                    } else if (codigo1 < codigo2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        }

        return evaluaciones;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminaEvaluacionEmpleado")
    public void eliminaEvaluacionEmpleado(@WebParam(name = "evaluacionx") EvaluacionEmpleado eve) {


        EvaluacionEmpleado eva;
        eva = evaluacionEmpleadoFacade.find(eve.getId());
        evaluacionEmpleadoFacade.remove(eva);

    }
}
