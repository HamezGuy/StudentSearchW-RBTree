import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataLoader implements IDataLoader{

    protected String filepath = "data.xml";

    DataLoader(String filepath)
    {
        this.filepath = filepath;
    }

    @Override
    public List<IStudent> read() {

        try{
            List<IStudent> listOfStudents = new ArrayList<IStudent>();

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentFactory.newDocumentBuilder();

            Document document = builder.parse(filepath);
            document.getDocumentElement().normalize(); //root of the xml file

            NodeList rowList = document.getElementsByTagName("row");
            //TODO reimpliment after new dataset is made 
            //note that bio is not yet in there 
           

            for(int i = 0; i < rowList.getLength(); i++)
            {
                Node node = rowList.item(i);
                
                Student student = new Student();
                
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    
                    String conversion = element.getElementsByTagName("score").item(0).getTextContent();
                    conversion = conversion.replaceAll("%", "");

                    student = new Student(element.getElementsByTagName("name").item(0).getTextContent(), 
                    Double.parseDouble(conversion), element.getElementsByTagName("bio").item(0).getTextContent());

                }

                listOfStudents.add(student);
            }

            return listOfStudents;
        }
        catch (Exception e)
        {
            System.out.println("issue with parsing, exiting program");
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public void write(List<IStudent> list) {
        try {
          DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder newDocument = documentBuilderFactory.newDocumentBuilder();
          Document doc = newDocument.newDocument();
    
          Element root = doc.createElement("root");
          doc.appendChild(root);
    
          for (int i = 0; i < list.size(); i++) {
            Element student = doc.createElement("row");
            root.appendChild(student);
    
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(list.get(i).getName()));
            student.appendChild(name);
            Element score = doc.createElement("score");
            score.appendChild(doc.createTextNode(Double.toString(list.get(i).getScore())));
            student.appendChild(score);
            Element bio = doc.createElement("bio");
            bio.appendChild(doc.createTextNode(list.get(i).getBio()));
            student.appendChild(bio);
          }
    
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          DOMSource domSource = new DOMSource(doc);
          StreamResult streamResult = new StreamResult(new File(filepath));
    
          transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
          e.printStackTrace();
        }
      }


    
}


