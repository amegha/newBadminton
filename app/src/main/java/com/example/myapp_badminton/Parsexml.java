package com.example.myapp_badminton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Karthik PS OptiPace Technologies on 11/20/2017.
 */

public class Parsexml {
    static HashMap<String, List<String>> stateCityMap, cityLocationMap, locationAcademyMap;
    static ArrayList stateList, cityList;
    private static int tempj = 0;

    public static void parse_xml_file(String tag) {
        stateCityMap = new HashMap<>();
        cityLocationMap = new HashMap<>();
        locationAcademyMap = new HashMap<>();
        stateList = new ArrayList();
        cityList = new ArrayList();

        try {

            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = newDocumentBuilder.parse(new ByteArrayInputStream(tag.getBytes()));

            NodeList nList = null;//states
            NodeList cList = null; //karnataka, andra ,tamilnadu
            NodeList stateChildList = null;
            NodeList cityChildList = null;
            nList = doc.getElementsByTagName("states");
            getChild(doc, nList);

            for (int i = 0; i < stateList.size(); i++) {
                nList = doc.getElementsByTagName(stateList.get(i).toString());
                for (int temp = 0; temp < nList.getLength(); temp++) { //karnataka,telangana,tamil
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        cList = eElement.getChildNodes();
                        for (int j = 0; j < cList.getLength(); j++) {
                            System.out.println("state child node " + cList.item(j).getNodeName()); //blore and belagavi
//                            if (tempj == j) {
                                cityList.add(cList.item(j).getNodeName());
//                                tempj++;
//                            }
//                    getChild(doc, doc.getElementsByTagName(cList.item(i).getNodeName()));
                        }
                        stateCityMap.put(stateList.get(i).toString(), cityList);

                    }
//                    System.out.println("state child node " +stateList.get(i).toString() ); //blore and belagavi
                }
//                stateCityMap.put(stateList.get(i).toString(), cityList);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("cityStateMap: "+Collections.singletonList(stateCityMap));

    }

    private static void getChild(Document doc, NodeList nList) {
        NodeList cList;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                cList = eElement.getChildNodes();
                for (int i = 0; i < cList.getLength(); i++) {
                    System.out.println("child node " + cList.item(i).getNodeName());
                    stateList.add(cList.item(i).getNodeName());
//                    getChild(doc, doc.getElementsByTagName(cList.item(i).getNodeName()));
                }

            }

        }
    }

}


