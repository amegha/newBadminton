package com.example.myapp_badminton;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Karthik PS OptiPace Technologies on 11/20/2017.
 */

public class Parsexml {
    static Map stateCityMap,cityLocationMap,locationAcademyMap;
    public static void parse_xml_file(String tag) {
        stateCityMap=new HashMap<String,String>();
        cityLocationMap=new HashMap<String,String>();
        locationAcademyMap=new HashMap<String,String>();

        try {

            String msg = "<message>HELLO!</message>";
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = newDocumentBuilder.parse(new ByteArrayInputStream(msg.getBytes()));

            NodeList nList = null;
            NodeList cList = null;
            nList = doc.getElementsByTagName("states");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                   Element eElement = (Element) nNode;
                   cList=eElement.getChildNodes();
                   for(int i=0;i<cList.getLength();i++){
                       System.out.println(cList.item(i));
                   }

               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


