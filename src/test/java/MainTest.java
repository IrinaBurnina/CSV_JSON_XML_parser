import org.junit.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class MainTest {
    List<Employee> expectedList;
    Main main;

    @BeforeClass
    public static void beforeAll() {
        System.out.println("Tests are started.");
    }

    @AfterClass
    public static void afterAll() {
        System.out.println("Tests are completed.");
    }

    @After
    public void afterEach() {
        main = null;
        System.out.println("Main test is completed.");
    }

    @Before
    public void beforeEachTest() {
        System.out.println("Main test is started.");
        expectedList = new ArrayList<>();
        main = new Main();
        expectedList.add(new Employee(1, "John", "Smith", "USA", 25));
        expectedList.add(new Employee(2, "Ivan", "Petrov", "RU", 23));
    }

    @Test
    public void listToJsonTest() {
        String result = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                "{\"id\":2,\"firstName\":\"Ivan\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        String expected = main.listToJson(expectedList);
        assertThat(expected, equalTo(result));
    }

    @Test
    public void parseCSVTest() {
        String[] columnMappingTest = {"id", "firstName", "lastName", "country", "age"};
        String fileNameTest = "data.csv";
        List<Employee> result = main.parseCSV(columnMappingTest, fileNameTest);
        assertThat(expectedList.toString(), equalTo(result.toString()));
    }

    @Test
    public void parseXMLTest() throws ParserConfigurationException, IOException, SAXException {
        String fileNameTest = "data.xml";
        List<Employee> result = main.parseXML(fileNameTest);
        assertThat(expectedList.toString(), equalTo(result.toString()));
    }
}

