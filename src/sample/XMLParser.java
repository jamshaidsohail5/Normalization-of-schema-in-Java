package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Acer on 5/29/2017.
 */
public class XMLParser {
        public static String parse(String filename) throws ParserConfigurationException, IOException, SAXException {

            String inputLine = filename;

            File fXmlFile = new File(inputLine);
            boolean flag;
            flag = false;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            StringBuilder sb = new StringBuilder(1000);
            String relationname;
            relationname = "";
            String a ="";
            String r;
            r = "";
            NodeList nList;
            NodeList nList1;
            NodeList nList2;
            nList = doc.getElementsByTagName("Entity");
            Node nNode;
            Node nNode1;
            Node nNode2;
            Node nNode3;
            Element eElement;
            Element eElement2;
            Element eElement3;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    eElement = (Element) nNode;
                    relationname = eElement.getAttribute("name");
                    sb.append("Create Table " + relationname + "(\n");
                    nList1 = eElement.getChildNodes();
                    for(int  i = 0 ; i <nList1.getLength();i++)
                    {
                        nNode1 = nList1.item(i);
                        if(nNode1.getNodeName().compareTo("Attribute_Groups")==0)
                        {
                            nList2 = nNode1.getChildNodes();

                            for(int j = 0;j<nList2.getLength();j++)
                            {
                                nNode2 = nList2.item(j);
                                if(nNode2.getNodeName() =="Attribute") {

                                    NodeList nList3;
                                    nList3 = nNode2.getChildNodes();
                                    for (int k = 0; k < nList3.getLength(); k++)
                                    {
                                        nNode3 = nList3.item(k);
                                        // System.out.println(nNode3.getNodeName());
                                        if(nNode3.getNodeName()=="AttributeProps")
                                        {
                                            NodeList nList4;
                                            nList4 = nNode3.getChildNodes();
                                            for(int l =0;l<nList4.getLength();l++)
                                            {
                                                Node nNode4;
                                                nNode4 = nList4.item(l);
                                                if(nNode4.getNodeName() == "Name")
                                                {
                                                    sb.append(nNode4.getTextContent() + " ");
                                                }
                                                if(nNode4.getNodeName()=="Logical_Data_Type")
                                                {
                                                    sb.append(nNode4.getTextContent() + ",\n");
                                                }
                                            }
                                        }
                                        else if(nNode3.getNodeName().compareTo("History_Information_Groups")==0)
                                        {
                                            NodeList nList5;
                                            nList5 = nNode3.getChildNodes();
                                            for(int m = 0;m<nList5.getLength();m++)
                                            {
                                                Node nNode5;
                                                nNode5 = nList5.item(m);
                                                if(nNode5.getNodeName()=="History_Information")
                                                {
                                                    NodeList nList6;
                                                    nList6 = nNode5.getChildNodes();
                                                    for(int n = 0;n<nList6.getLength();n++)
                                                    {
                                                        Node nNode6 = nList6.item(n);
                                                        if(nNode6.getNodeName()=="History_InformationProps")
                                                        {
                                                            NodeList nlist7 = nNode6.getChildNodes();
                                                            for(int o = 0;o<nlist7.getLength();o++)
                                                            {
                                                                Node nNode7  = nlist7.item(o);
                                                                if(nNode7.getNodeName()=="Owner_Path")
                                                                {
                                                                    String attribute = nNode7.getTextContent();
                                                                    String find = ".";
                                                                    r = attribute.substring(attribute.indexOf(find)+find.length());
                                                                    //System.out.println(r);
                                                                }
                                                                else if(nNode7.getNodeName() == "Description")
                                                                {
                                                                    String sentence = nNode7.getTextContent();
                                                                    String search  = "foreign key";
                                                                    if (sentence.indexOf(search.toLowerCase()) != -1) {
                                                                        String ss = nNode7.getTextContent();
                                                                        String[] parts = ss.split(" ");
                                                                        StringBuilder s = new StringBuilder(parts[4]);
                                                                        s.deleteCharAt(s.length()-1);
                                                                        parts[4] = s.toString();
                                                                        String t = ".";
                                                                        String u = r.substring(r.indexOf(t)+t.length());
                                                                        String temp1 = r.substring(r.indexOf(".") + 1);
                                                                        sb.append("FOREIGN KEY (" + temp1 +") REFERENCES "+parts[4]+"("+u+"),");
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        else if(nNode1.getNodeName().compareTo("Key_Group_Groups")==0)
                        {
                            flag = false;
                            NodeList nList8 =nNode1.getChildNodes();
                            for(int p = 0;p<nList8.getLength();p++)
                            {
                                Node nNode8 = nList8.item(p);
                                if(nNode8.getNodeName().compareTo("Key_Group")==0)
                                {
                                    if(nNode8.getNodeType() == Node.ELEMENT_NODE)
                                    {
                                        eElement2 = (Element) nNode8;
                                        String checkingkey = eElement2.getAttribute("name");
                                        String search  = "XPK";
                                        if (checkingkey.indexOf(search) != -1) {
                                            NodeList nList9 = nNode8.getChildNodes();
                                            for (int q = 0; q < nList9.getLength(); q++)
                                            {
                                                Node nNode9 = nList9.item(q);
                                                if(nNode9.getNodeName().compareTo("Key_Group_Member_Groups")==0)
                                                {
                                                    NodeList nList10 = nNode9.getChildNodes();
                                                    for(int s = 0;s<nList10.getLength();s++)
                                                    {
                                                        Node nNode10 = nList10.item(s);
                                                        if(nNode10.getNodeName().compareTo("Key_Group_Member")==0)
                                                        {
                                                            if(nNode10.getNodeType() == Node.ELEMENT_NODE)
                                                            {
                                                                if(flag == false)
                                                                {

                                                                    eElement3 = (Element) nNode10;
                                                                    String name = eElement3.getAttribute("name");
                                                                    sb.append("PRIMARY KEY ("+name+")");
                                                                    flag = true;

                                                                }
                                                                else if(flag == true)
                                                                {
                                                                    eElement3 = (Element) nNode10;
                                                                    String name = eElement3.getAttribute("name");
                                                                    sb.deleteCharAt(sb.length()-1);
                                                                    sb.append(", " + name +")");

                                                                }

                                                            }
                                                        }

                                                    }
                                                    sb.append(");\n");

                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return sb.toString();
        }
    }
