/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.evap;


import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ivan2car
 */
public abstract class AbstractFacade<T> {

   private Class<T> entityClass;

   public AbstractFacade(Class<T> entityClass) {
      this.entityClass = entityClass;
   }

   protected abstract EntityManager getEntityManager();

   public void create(T entity) {
      getEntityManager().persist(entity);
     

   }

   public void edit(T entity) {
      getEntityManager().merge(entity);


   }

   public void remove(T entity) {
      getEntityManager().remove(getEntityManager().merge(entity));
   }

   public T find(Object id) {
      return getEntityManager().find(entityClass, id);
   }

   public List<T> findAll() {
      javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
      cq.select(cq.from(entityClass));
      return getEntityManager().createQuery(cq).getResultList();
   }

   public List<T> findRange(int[] range) {
      javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
      cq.select(cq.from(entityClass));
      javax.persistence.Query q = getEntityManager().createQuery(cq);
      q.setMaxResults(range[1] - range[0]);
      q.setFirstResult(range[0]);
      return q.getResultList();
   }

   public int count() {
      javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
      javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
      cq.select(getEntityManager().getCriteriaBuilder().count(rt));
      javax.persistence.Query q = getEntityManager().createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
   }

   public List<T> findRange(String _sortField, boolean _sortOrder, int[] _range, Map<String, String> _filters) {

      return findRange(_sortField, _sortOrder, _range, _filters, "");
   }

   public List<T> findRange(String _sortField, boolean _sortOrder, int[] _range, Map<String, String> _filters, String cadena) {
      javax.persistence.Query q;
//        String condi = getCondicion(entityClass.getSimpleName());
//        System.out.println(condi);

      System.out.println("cadena en facade: " + cadena);

//        if (entityClass.getSimpleName().compareTo("Mvcli001m") == 0) {
//            StringBuffer sentencia = new StringBuffer("select u").append(" from ").append(entityClass.getSimpleName()).append(" u ").append(" where u.mvofc001m.mvofc001mPK.codof in ('4286','4284','4288')");
      StringBuffer sentencia = new StringBuffer("select u").append(" from ").append(entityClass.getSimpleName()).append(" u ");
      /**
       * Para desconcatenar un mapa
       */
      if (_filters != null) {
         Iterator it = _filters.entrySet().iterator();
         if (_filters.size() > 0) {
            sentencia.append(" where ");
         }

         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();

            // si el filtro es de tipo date no se le aplica UPPER
            if (e.getValue() instanceof java.util.Date) {
               System.out.println("-------------se recibe par date: " + e.getValue().toString());
               String dateValue = new SimpleDateFormat("yyyy/MM/dd").format(e.getValue());
               sentencia.append(" u.").append(e.getKey()).append(" IS NOT NULL AND");
               sentencia.append(" u.").append(e.getKey()).append(" = \"").append(dateValue).append("\" and ");
            } else {
               sentencia.append(" UPPER(u.").append(e.getKey()).append(") like '").append(e.getValue().toString().toUpperCase()).append("%' and ");
            }
            System.out.println(e.getKey() + " Paramo " + e.getValue());
         }
         int lastIndexOfAnd = sentencia.lastIndexOf("and");
         if (lastIndexOfAnd != -1) {
            sentencia = new StringBuffer(sentencia.substring(0, lastIndexOfAnd));
         } else {
            //System.out.println("no tiene and");
         }
      }

      // esto se hace posterior a la carga de filtros
      //si sentencia tiene where --> cambiar el where por and
      //si sentencia no tiene where  -->dejar el where
      //-----------------
      if (cadena != null && !cadena.equals("")) {


         if (sentencia.indexOf("where") != -1) {       //si contiene where
            sentencia.append(" and ");
            sentencia.append(cadena);
            sentencia.append(" ");
         } else {
            sentencia.append(" where ");
            sentencia.append(cadena);
            sentencia.append(" ");
         }
      }
      if (_sortField != null && _sortField != null) {
         sentencia.append(" order by u.").append(_sortField);
         if (_sortOrder) {
            sentencia.append(" asc");
         } else {
            sentencia.append(" desc");
         }
      }
      System.out.println(sentencia);

      q = getEntityManager().createQuery(sentencia.toString());
      q.setMaxResults(_range[1] - _range[0]);
      q.setFirstResult(_range[0]);


      return q.getResultList();
   }

/*  public java.util.Date getCurrentDateTime() {
      java.util.Date currentDate = null;
      Connection con = new conexion().conectar();
      Select sel2 = new Select(con);
      ResultSet rs2 = null;
      StringBuilder query = new StringBuilder("SELECT CURRENT FROM Systables WHERE Tabid = 1");
      try {
         rs2 = sel2.ejecutarSelect(query.toString());
         if (rs2.next()) {
            currentDate = rs2.getDate(1);
         }
         rs2.close();
         sel2.close();
         con.close();
      } catch (Exception e) {
         try {
            con.close();
            throw new Exception(e.getMessage());
         } catch (Exception ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
         }
      } finally {
         rs2 = null;
         sel2 = null;
         con = null;
         return currentDate;
      }/*
      java.util.Date fecha = new Date();
      return fecha;
   }*/


   public int count(Map<String, String> _filters, String _condicionAdicional, Class _clase) {
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
//        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
//        javax.persistence.Query q = getEntityManager().createQuery(cq);
      System.out.println("contado con filtros y filtro adicional");
      javax.persistence.Query q = null;
    //  String condi = getCondicion(entityClass.getSimpleName());
  //    System.out.println(condi);

      if (entityClass.getSimpleName().compareTo("Mvcli001m") == 0) {
         StringBuffer sentencia = new StringBuffer("select count(u) ").append(" from ").append(entityClass.getSimpleName()).append(" u ").append(" where u.mvofc001m.mvofc001mPK.codof in ('4286','4284','4288')");
         /**
          * Para desconcatenar un mapa
          */
         Iterator it = _filters.entrySet().iterator();

         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            sentencia.append(" and u.").append(e.getKey()).append(" like '").append(e.getValue().toString().toUpperCase()).append("%'");
            System.out.println(e.getKey() + " Paramo " + e.getValue());
         }

         // agregando la condicion
         if (_condicionAdicional != null && !_condicionAdicional.equals("")) {

            // SE LIMPIAN LOS WHERE Y AND QUE TRAIGA LA CONDICION
            //  porque estos dependeran de los demas parametros
                /*_condicionAdicional = _condicionAdicional.replaceAll("where", "");
            _condicionAdicional = _condicionAdicional.replaceAll("WHERE", "");
            _condicionAdicional = _condicionAdicional.replaceAll("and", "");
            _condicionAdicional = _condicionAdicional.replaceAll("AND", "");*/

            if (sentencia.indexOf("where") != -1) {       //si contiene where
               sentencia.append(" and ");
               sentencia.append(_condicionAdicional);
               sentencia.append(" ");
            } else {
               sentencia.append(" where ");
               sentencia.append(_condicionAdicional);
               sentencia.append(" ");
            }
         }
         q = getEntityManager().createQuery(sentencia.toString());

      } else {
         if (entityClass.getSimpleName().compareTo("Ttcibd001001v") == 0) {
            /**
             * Para desconcatenar un mapa
             */
            StringBuffer sb = new StringBuffer("select count(u.bdsca) ").append(" from ").append(entityClass.getSimpleName()).append(" u ");

            if (_filters != null) {
               Iterator it = _filters.entrySet().iterator();

               if (_filters.size() > 0) {
                  sb.append(" where ");
               }

               while (it.hasNext()) {
                  Map.Entry e = (Map.Entry) it.next();
                  sb.append(" UPPER(u.").append(e.getKey()).append(") like '").append(e.getValue().toString().toUpperCase()).append("%' and ");
                  System.out.println(e.getKey() + " Paramo " + e.getValue());
               }

               int lastIndexOfAnd = sb.lastIndexOf("and");
               if (lastIndexOfAnd != -1) {
                  sb = new StringBuffer(sb.substring(0, lastIndexOfAnd));
               } else {
                  //System.out.println("no tiene and");
               }
            }
            if (_condicionAdicional != null && !_condicionAdicional.equals("")) {

               // SE LIMPIAN LOS WHERE Y AND QUE TRAIGA LA CONDICION
               //  porque estos dependeran de los demas parametros
                   /* _condicionAdicional = _condicionAdicional.replaceAll("where", "");
               _condicionAdicional = _condicionAdicional.replaceAll("WHERE", "");
               _condicionAdicional = _condicionAdicional.replaceAll("and", "");
               _condicionAdicional = _condicionAdicional.replaceAll("AND", "");
                */

               if (sb.indexOf("where") != -1) {       //si contiene where
                  sb.append(" and ");
                  sb.append(_condicionAdicional);
                  sb.append(" ");
               } else {
                  sb.append(" where ");
                  sb.append(_condicionAdicional);
                  sb.append(" ");
               }
            }

            System.out.println(sb.toString());
            q = getEntityManager().createQuery(sb.toString());
            // agregando la condicion
                /*if (condicionAdicional != null) {
            sentencia.append(" ").append(condicionAdicional).append(" ");
            }*/
         } else {
            if (entityClass.getSimpleName().compareTo("Ttibom010001v") == 0) {
               //q = getEntityManager().createQuery(
               StringBuffer sb = new StringBuffer("select count(u.bpono) ").append(" from ").append(entityClass.getSimpleName()).append(" u ");
               if (_filters != null) {
                  Iterator it = _filters.entrySet().iterator();

                  if (_filters.size() > 0) {
                     sb.append(" where ");
                  }

                  while (it.hasNext()) {
                     Map.Entry e = (Map.Entry) it.next();
                     sb.append(" UPPER(u.").append(e.getKey()).append(") like '").append(e.getValue().toString().toUpperCase()).append("%' and ");
                     System.out.println(e.getKey() + " Paramo " + e.getValue());
                  }

                  int lastIndexOfAnd = sb.lastIndexOf("and");
                  if (lastIndexOfAnd != -1) {
                     sb = new StringBuffer(sb.substring(0, lastIndexOfAnd));
                  } else {
                     //System.out.println("no tiene and");
                  }
               }
               if (_condicionAdicional != null && !_condicionAdicional.equals("")) {

                  // SE LIMPIAN LOS WHERE Y AND QUE TRAIGA LA CONDICION
                  //  porque estos dependeran de los demas parametros
                        /*_condicionAdicional = _condicionAdicional.replaceAll("where", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("WHERE", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("and", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("AND", "");
                   */

                  if (sb.indexOf("where") != -1) {       //si contiene where
                     sb.append(" and ");
                     sb.append(_condicionAdicional);
                     sb.append(" ");
                  } else {
                     sb.append(" where ");
                     sb.append(_condicionAdicional);
                     sb.append(" ");
                  }
               }
               System.out.println(sb.toString());
               q = getEntityManager().createQuery(sb.toString());
            } else {
               StringBuffer sb = new StringBuffer("select count(u) ").append(" from ").append(_clase.getSimpleName()).append(" u ");
               if (_filters != null) {
                  Iterator it = _filters.entrySet().iterator();

                  if (_filters.size() > 0) {
                     sb.append(" where ");
                  }

                  while (it.hasNext()) {
                     Map.Entry e = (Map.Entry) it.next();

                     // si el filtro es de tipo date no se le aplica UPPER
                     if (e.getValue() instanceof java.util.Date) {
                        System.out.println("-------------se recibe par date: " + e.getValue().toString());
                        String dateValue = new SimpleDateFormat("yyyy/MM/dd").format(e.getValue());
                        sb.append(" u.").append(e.getKey()).append(" IS NOT NULL AND");
                        sb.append(" u.").append(e.getKey()).append(" = \"").append(dateValue).append("\" and ");
                     } else {
                        sb.append(" UPPER(u.").append(e.getKey()).append(") like '").append(e.getValue().toString().toUpperCase()).append("%' and ");
                     }
                     System.out.println(e.getKey() + " Paramo " + e.getValue());
                  }

                  int lastIndexOfAnd = sb.lastIndexOf("and");
                  if (lastIndexOfAnd != -1) {
                     sb = new StringBuffer(sb.substring(0, lastIndexOfAnd));
                  } else {
                     //System.out.println("no tiene and");
                  }
               }
               if (_condicionAdicional != null && !_condicionAdicional.equals("")) {

                  // SE LIMPIAN LOS WHERE Y AND QUE TRAIGA LA CONDICION
                  //  porque estos dependeran de los demas parametros
                        /*_condicionAdicional = _condicionAdicional.replaceAll("where", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("WHERE", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("and", "");
                  _condicionAdicional = _condicionAdicional.replaceAll("AND", "");*/

                  if (sb.indexOf("where") != -1) {       //si contiene where
                     sb.append(" and ");
                     sb.append(_condicionAdicional);
                     sb.append(" ");
                  } else {
                     sb.append(" where ");
                     sb.append(_condicionAdicional);
                     sb.append(" ");
                  }
               }
               System.out.println(sb.toString());
               q = getEntityManager().createQuery(sb.toString());
            }
         }
      }
      System.out.println("mira:  " + q.getSingleResult());
      return ((Long) q.getSingleResult()).intValue();
   }

 /*  public String getCondicion(String _tab) {
      Connection con = new conexion().conectar();
      Select sel = new Select(con);
      ResultSet rs = null;
      ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
      HttpSession sesion = (HttpSession) ex.getSession(true);

      try {
         rs = sel.ejecutarSelect(
                 new StringBuilder("select * FROM sgper004v,sgusu001v").append(" where login='").append(sesion.getAttribute("Usuario")).append("'").append(" and sgper004v.idabd=sgusu001v.idabd").append(" and notbl='").append(_tab.toLowerCase()).append("'").toString());

         if (rs.next()) {
            {
               System.out.println(rs.getString("permi").trim());
               _tab = rs.getString("permi").trim().replace(_tab.toLowerCase(), "u");
               return _tab;
            }
         } else {
         }
         rs.close();
         sel.close();
         con.close();
      } catch (Exception e) {
         try {
            con.close();
         } catch (SQLException ex1) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex1);
         }
         System.out.println("****Exception: " + e.getMessage());
      } finally {
         rs = null;
         sel = null;
         con = null;
      }

      return null;
   }*/

   public int count(Map<String, String> _filters) {
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
//        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
//        javax.persistence.Query q = getEntityManager().createQuery(cq);

      javax.persistence.Query q;
   //   String condi = getCondicion(entityClass.getSimpleName());
//      System.out.println(condi);

      if (entityClass.getSimpleName().compareTo("Mvcli001m") == 0) {
         StringBuffer sentencia = new StringBuffer("select count(u) ").append(" from ").append(entityClass.getSimpleName()).append(" u ").append(" where u.mvofc001m.mvofc001mPK.codof in ('4286','4284','4288')");
         /**
          * Para desconcatenar un mapa
          */
         Iterator it = _filters.entrySet().iterator();

         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            sentencia.append(" and u.").append(e.getKey()).append(" like '").append(e.getValue().toString().toUpperCase()).append("%'");
            System.out.println(e.getKey() + " Paramo " + e.getValue());
         }

         System.out.println(sentencia);
         q = getEntityManager().createQuery(sentencia.toString());

      } else {
         if (entityClass.getSimpleName().compareTo("Ttcibd001001v") == 0) {
            q = getEntityManager().createQuery(new StringBuffer("select count(u.bdsca) ").append(" from ").append(entityClass.getSimpleName()).append(" u ").toString());
         } else {
            if (entityClass.getSimpleName().compareTo("Ttibom010001v") == 0) {
               q = getEntityManager().createQuery(new StringBuffer("select count(u.bpono) ").append(" from ").append(entityClass.getSimpleName()).append(" u ").toString());
            } else {
               q = getEntityManager().createQuery(new StringBuffer("select count(u) ").append(" from ").append(entityClass.getSimpleName()).append(" u ").toString());
            }
         }
      }
      System.out.println("mira:  " + q.getSingleResult());
      return ((Long) q.getSingleResult()).intValue();
   }

  /* public String getCurrentDate() {
      java.util.Date currentDate = getCurrentDateTime();
      SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
      return formato.format(currentDate);
   }
   * */
   

   public String getCodem() {
      ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
      HttpSession sesion = (HttpSession) ex.getSession(true);
      return (String) sesion.getAttribute("codem");
   }

   public String getNomUsu() {
      ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
      HttpSession sesion = (HttpSession) ex.getSession(true);
      return (String) sesion.getAttribute("NomUsu");
   }
}

