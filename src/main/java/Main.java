import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import netscape.javascript.JSObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json,"data.json");
        System.out.println(json);
        String jsonXML=listToJson(parseXML("data.xml"));
        writeString(jsonXML,"data2.json");
        System.out.println(jsonXML);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<JSObject>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node staff = doc.getDocumentElement();
            NodeList nodeList = staff.getChildNodes();
            String[] content;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Employee empl = new Employee();
                    System.out.println("Node current:   " + node.getNodeName());
                    NodeList child = node.getChildNodes();
                    for (int j = 0; j < child.getLength(); j++) {
                        Node node1 = child.item(j);
                        if (Node.ELEMENT_NODE == node1.getNodeType()) {
                            System.out.println("Node child:  " + j+"  "+node1.getNodeName());
                            content = new String[child.getLength() - 1];
                            content[j] = node1.getTextContent();
                            System.out.println(content[j]);
                            if (j == 1) {
                                empl.id = Integer.parseInt(node1.getTextContent());
                            } else if (j == 3) {
                                empl.firstName = node1.getTextContent();
                            } else if (j == 5) {
                                empl.lastName = node1.getTextContent();
                            } else if (j == 7) {
                                empl.country = node1.getTextContent();
                            } else if (j == 9) {
                                empl.age = Integer.parseInt(node1.getTextContent());
                            }
                        }
                    }list.add(empl);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return list;
    }
}
