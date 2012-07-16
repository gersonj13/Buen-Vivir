package com.pangea.borrar.fenixTools.util;

/*
import java.io.*;
import java.util.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.*;

public class XmlUtil {
  Document doc = null;
  Element rootElem = null;
  public XmlUtil(String rootElement) {
    try {
      rootElem = new Element(rootElement) ;
      doc = new Document(rootElem);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public boolean save(String nombreArchivo) {
    try {
      XMLOutputter out = new XMLOutputter();
      out.setEncoding("UTF-8");
      out.setNewlines(true);
      out.setIndent(" ");
      out.setIndent(true);
      FileOutputStream fos = new FileOutputStream(nombreArchivo);
      out.output(doc, fos);
      fos.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean load(String nombreArchivo) {
    try {
      DOMBuilder in = new DOMBuilder();
      doc = in.build(new File(nombreArchivo));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public String getElement(String nombreTag) {
    String valorTag = "";
    try {
      valorTag = doc.getRootElement().getChildText(nombreTag);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return valorTag;
  }

  public boolean addElement(String nombreTag, String valorTag) {
    try {
      Element elem = new Element(nombreTag);
      elem.addContent(valorTag);
      rootElem.addContent(elem);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean cambiarValor(String nombreTag, String valorTag){
    try {
      Element elem = doc.getRootElement().getChild(nombreTag);
      elem.setText(valorTag);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    try {
      XmlUtil xml = new XmlUtil("POZO");
      xml.addElement("WHP_LP_TOLERENCE", "1");
      xml.addElement("H_ALARM_TOLERENCE", "2");
      xml.addElement("HH_ALARM_TOLERENCE", "3");
      xml.addElement("L_ALARM_TOLERENCE", "4");
      xml.addElement("LL_ALARM_TOLERENCE", "5");
      xml.addElement("WHP_INC_TOLERENCE", "6");
      xml.addElement("LT_INC_TOLERENCE", "7");
      xml.addElement("LT_DROP_TOLERENCE", "8");
      xml.addElement("LT_TOLERENCE", "9");
      xml.addElement("WHP_DROP_TOLERENCE", "10");
      xml.save("c:/prueba.xml");

      xml = new XmlUtil("POZO");
      xml.load("c:/prueba.xml");
      System.out.println(xml.getElement("WHP_DROP_TOLERENCE"));
      System.out.println(xml.getElement("LT_TOLERENCE"));
      System.out.println(xml.getElement("LT_DROP_TOLERENCE"));
      System.out.println(xml.getElement("LT_INC_TOLERENCE"));
      System.out.println(xml.getElement("WHP_INC_TOLERENCE"));
      System.out.println(xml.getElement("LL_ALARM_TOLERENCE"));
      System.out.println(xml.getElement("L_ALARM_TOLERENCE"));
      System.out.println(xml.getElement("HH_ALARM_TOLERENCE"));
      System.out.println(xml.getElement("H_ALARM_TOLERENCE"));
      System.out.println(xml.getElement("WHP_LP_TOLERENCE"));

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}*/