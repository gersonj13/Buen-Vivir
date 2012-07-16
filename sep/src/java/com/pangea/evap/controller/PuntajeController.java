package com.pangea.evap.controller;

import com.fenix.util.jbVarios;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.pangea.evap.services.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.xml.sax.SAXException;

// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "puntajeBean")
@RequestScoped
public class PuntajeController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private String puntaje, deducciones, saldo;
    private Seleccionado seleccionado;
    private String cantidad;
    private String calculo;
    private String puntosCanje;
    private Canje ultimoCanje;
    private Empleado current;
    private String Saldoc;
    private List<Canje> canjes;
    private Canje currentcanje;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
    private static Font catFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
    private static Font tFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.UNDERLINE);
    private static Font celda = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    private static Font cont = new Font(Font.FontFamily.TIMES_ROMAN, 12);

    public Canje getCurrentcanje() {
        return currentcanje;
    }

    public String getSaldoc() {
        return Saldoc;
    }

    public void setSaldoc(String Saldoc) {
        this.Saldoc = Saldoc;
    }

    public void setCurrentcanje(Canje currentcanje) {
        this.currentcanje = currentcanje;
    }

    public List<Canje> getCanjes() {
        return canjes;
    }

    public void setCanjes(List<Canje> canjes) {
        this.canjes = canjes;
    }

    public Empleado getCurrent() {
        return current;
    }

    public void setCurrent(Empleado current) {
        this.current = current;
    }

    public Canje getUltimoCanje() {
        return ultimoCanje;
    }

    public void setUltimoCanje(Canje ultimoCanje) {
        this.ultimoCanje = ultimoCanje;
    }

    public String getPuntosCanje() {
        return puntosCanje;
    }

    public void setPuntosCanje(String puntosCanje) {
        this.puntosCanje = puntosCanje;
    }

    public String getCalculo() {
        return calculo;
    }

    public void setCalculo(String calculo) {
        this.calculo = calculo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public Seleccionado getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Seleccionado seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getDeducciones() {
        return deducciones;
    }

    public void setDeducciones(String deducciones) {
        this.deducciones = deducciones;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void init() {
    }

    public PuntajeController() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("listacanjes") != null) {
            if (sesion.getAttribute("listacanjes").toString().compareTo("nada") != 0) {
                canjes = (List<Canje>) sesion.getAttribute("listacanjes");
            }
        }



    }

    public void canjearPuntos() throws SAXException, IOException {
        this.ConsultarPuntaje();


        if (puntosCanje.compareTo("") != 0 && Float.parseFloat(puntosCanje) != 0) {

            if (Float.parseFloat(puntosCanje) <= Float.parseFloat(seleccionado.getPuntosDisponibles().toString())) {


                this.procesarCanje(seleccionado, puntosCanje);
                this.actualizarCorte(seleccionado, puntosCanje);
                this.actualizarSeleccionado(seleccionado, puntosCanje);
                this.ConsultarPuntaje();
                puntosCanje = "";


                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();





                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/canje/comprobante.xhtml");

                } catch (IOException e) {
                    //  LOG.error("Redirect failed");
                    throw new FacesException(e);
                }


            } else {
                mostrarMensaje(3, "Error", "Cantidad Introducida supera su saldo disponible");
            }


        } else {
            mostrarMensaje(2, "Error", "Debe introducir una Cantidad de Puntos");

        }


    }

    public String estilo(String codigo) {

        if (codigo.equals("1")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
    }

    public void iniciarComprobante() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        seleccionado = (Seleccionado) sesion.getAttribute("selec");
        this.ultimoCanje = this.retornaultimoCanje(seleccionado);
        sesion.setAttribute("imprimeC", ultimoCanje);

        current = (Empleado) sesion.getAttribute("Empleado");

    }

    public void iniciarComprobanteI() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        seleccionado = (Seleccionado) sesion.getAttribute("selec");
        this.ultimoCanje = (Canje) sesion.getAttribute("imprimeC");

        current = (Empleado) sesion.getAttribute("Empleado");

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void pdf() throws MalformedURLException, DocumentException {
        OutputStream os = null;
        try {
            OutputStream browserStream = null;
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            ExternalContext ec = context.getExternalContext();
            HttpSession sesion = (HttpSession) ec.getSession(true);

            seleccionado = (Seleccionado) sesion.getAttribute("selec");
            this.ultimoCanje = (Canje) sesion.getAttribute("imprimeC");

            current = (Empleado) sesion.getAttribute("Empleado");
            jbVarios jvar = new jbVarios();

            String fecha = jvar.Hoy("dd-MM-yyyy hh:mm");



            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + current.getNroIdentificacion() + "_" + fecha + ".pdf");

            Document document = new Document(PageSize.A4, 86, 86, 72, 72);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();



            Image imagex = Image.getInstance(context.getExternalContext().getRealPath("/resources/images/img/logo.png"));
            imagex.scaleAbsolute(document.getPageSize().getWidth() - 150f, 40f);
            document.add(imagex);
            Paragraph prefacex = new Paragraph();
            addEmptyLine(prefacex, 1);
            document.add(prefacex);




            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 1);
            Paragraph titulo = new Paragraph("Señores", catFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);

            preface = new Paragraph();
            titulo = new Paragraph("DAKA,", catFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);

            preface = new Paragraph();
            titulo = new Paragraph("Presente.-", catFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 1);
            document.add(preface);


            preface = new Paragraph();
            addEmptyLine(preface, 1);
            document.add(preface);


            preface = new Paragraph();
            titulo = new Paragraph("AUTORIZACION PARA CANJE", tFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 2);
            document.add(preface);


            ////////////////////////CUERPO

            String calculo2;
            float monto_calculado=Float.parseFloat(ultimoCanje.getPuntos().toString()) * seleccionado.getIdCorte().getTasaCambio().floatValue();

            BigDecimal decimales=new BigDecimal(String.valueOf(monto_calculado));
            calculo2 = decimales.setScale(2, RoundingMode.CEILING).toString().replace(".",",");
            StringBuilder st = new StringBuilder("");

            int ced = current.getTipoIdentificacion();
            String cedu = "V";
            if (ced != 86) {
                cedu = "E";
            }
            st.append("Quienes suscribimos, Abogado Omar José Yánez Villanueva y Capitán Amancio José").append(" Aldariz Adrianza, titulares de la cédula de identidad Nº V- 6.077.038 y 13.224.880").append(" respectivamente, actuando en nuestra condición de Gerente Corporativo de Gestión").append(" Humana y Jefe de Departamento de Servicios Generales de Seguros Horizonte, S.A., ").append("autorizamos al EMPLEADO ").append(current.getNombres()).append(" ").append(current.getApellidos()).append(", titular de la cédula de identidad Nº ").append(cedu).append("- ").append(current.getNroIdentificacion()).append(", ").append("a canjear la cantidad de Bs. ").append(calculo2).append("; ").append("equivalentes a ").append(ultimoCanje.getPuntos().toString()).append(" puntos obtenidos en forma parcial como resultado de su participación en ").append("el Plan de Reconocimiento \"El Buen Vivir\".");




            preface = new Paragraph();
            titulo = new Paragraph(st.toString(), cont);
            titulo.setAlignment(Element.ALIGN_JUSTIFIED);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 2);
            document.add(preface);



            preface = new Paragraph();
            titulo = new Paragraph("Firma y Huella Dactilar del empleado: ____________________", cont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            preface.add(titulo);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);




            preface = new Paragraph();
            addEmptyLine(preface, 4);
            Paragraph firmas = new Paragraph("ABOG. OMAR JOSÉ YÁNEZ VILLANUEVA       CAP. (EJNB) AMANCIO J. ALDARIZ ADRIANZA", catFont2);
            firmas.setAlignment(Element.ALIGN_CENTER);
            preface.add(firmas);
            preface.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 1);
            Paragraph nombres = new Paragraph(" C.I. 6.077.038	                                                    C.I.  13.224.880", catFont2);
            nombres.setAlignment(Element.ALIGN_CENTER);
            preface.add(nombres);
            addEmptyLine(preface, 1);
            document.add(preface);


            preface = new Paragraph();
            addEmptyLine(preface, 1);
            Paragraph fechax = new Paragraph("Fecha " + fecha, celda);
            fechax.setAlignment(Element.ALIGN_CENTER);
            preface.add(fechax);
            preface.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 1);
            Paragraph cs = new Paragraph("cs: " + ultimoCanje.getId().toString());
            cs.setAlignment(Element.ALIGN_CENTER);
            cs.setFont(celda);
            preface.add(cs);
            document.add(preface);


            imagex = Image.getInstance(context.getExternalContext().getRealPath("/resources/images/img/government.png"));
            imagex.scaleAbsolute(document.getPageSize().getWidth() - 150f, 40f);
            imagex.setAbsolutePosition(73f, 70f);
            document.add(imagex);


            document.close();

            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
            context.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(PuntajeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(PuntajeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String comprobarImagen(String cedula) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String ruta1;

        ruta1 = ec.getRealPath("/resources/images/usuarios/" + cedula + ".JPG");
        File f = new File(ruta1);
        boolean existe = f.exists();


        if (!existe) {

            return "/faces/resources/images/usuarios/noimage.jpg";
        }
        return "/faces/resources/images/usuarios/" + cedula + ".JPG";
    }

    public String comprobarImagen2(String cedula) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String ruta1;

        ruta1 = ec.getRealPath("/resources/images/usuarios/" + cedula + ".JPG");
        File f = new File(ruta1);
        boolean existe = f.exists();


        if (!existe) {

            return "/resources/images/usuarios/noimage.jpg";
        }
        return "/resources/images/usuarios/" + cedula + ".JPG";
    }

    public String fecha() {

        jbVarios jvar = new jbVarios();

        String fecha = jvar.Hoy("dd-MM-yyyy hh:mm");
        return fecha;
    }

    public void prepareComprobante() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);


        sesion.setAttribute("imprimeC", currentcanje);



        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/canje/comprobanteI.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void aceptar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();





        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/canje/canjePuntos.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void ConsultarPuntaje() {
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);

        String cod = (String) sesion.getAttribute("codSupervisor");

        puntaje = this.puntajeEmpleado(cod);

        deducciones = this.deduccionesEmpleado(cod);

        saldo = String.valueOf(Float.parseFloat(puntaje) - Float.parseFloat(deducciones));

        seleccionado = this.isSeleccionado(cod);

        if (seleccionado != null) {
            canjes = this.retornatodosCanjes(seleccionado);
            Saldoc = seleccionado.getPuntosAcumulados().subtract(seleccionado.getPuntosDisponibles()).toString();

        }

        sesion.setAttribute("listacanjes", canjes);

    }

    public void calcularTasa() {
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);

        seleccionado = (Seleccionado) sesion.getAttribute("selec");

        calculo = String.valueOf(Float.parseFloat(cantidad) * seleccionado.getIdCorte().getTasaCambio().floatValue());

    }

    public void mostrarMensaje(int _opcMensaje, String _cabeceraMensaje, String _cuerpomensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        switch (_opcMensaje) {
            case 0: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 1: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 2: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 3: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
        }
    }

    private String puntajeEmpleado(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.puntajeEmpleado(idEmpleado);
    }

    private String deduccionesEmpleado(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.deduccionesEmpleado(idEmpleado);
    }

    private boolean actualizarSeleccionado(com.pangea.evap.services.Seleccionado eseleccionado, java.lang.String puntos) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.actualizarSeleccionado(eseleccionado, puntos);
    }

    private void actualizarCorte(com.pangea.evap.services.Seleccionado eseleccionado, java.lang.String puntos) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.actualizarCorte(eseleccionado, puntos);
    }

    private boolean procesarCanje(com.pangea.evap.services.Seleccionado eseleccionado, java.lang.String puntos) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.procesarCanje(eseleccionado, puntos);
    }

    private Canje retornaultimoCanje(com.pangea.evap.services.Seleccionado eseleccionado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaultimoCanje(eseleccionado);
    }

    private java.util.List<com.pangea.evap.services.Canje> retornatodosCanjes(com.pangea.evap.services.Seleccionado eseleccionado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornatodosCanjes(eseleccionado);
    }

    private Seleccionado isSeleccionado(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.isSeleccionado(idEmpleado);
    }
}
