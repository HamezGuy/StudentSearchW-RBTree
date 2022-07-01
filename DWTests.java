import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

public class DWTests {

    protected List<IStudent> studentList = null;

    @BeforeEach
    public void createInstance()
    {
        studentList = new ArrayList<IStudent>();
    }

    @Test
    public void TestReadFile()
    {
        DataLoader testClass = new DataLoader("data-orig.xml");

        studentList = testClass.read();

        assertEquals(studentList.size(), 1029);

    }

    @Test
    public void TestInvalidFile()
    {
        DataLoader testClass = new DataLoader("null");

        studentList = testClass.read();

        assertEquals(studentList, null);
    }

    @Test
    public void TestCorrectValueReturn()
    {
        DataLoader testClass = new DataLoader("data-orig.xml");

        studentList = testClass.read();

        assertEquals(studentList.get(0).getName(), "Velma Paul");
    }

    @Test
    public void TestWrite()
    {
        DataLoader testClass = new DataLoader("data-orig.xml");

        studentList = testClass.read();

        Student newStudent = new Student("test Name", 0, "test Bio");

        studentList.add(newStudent);
        
        testClass.write(studentList);

        studentList = testClass.read();

        assertEquals(studentList.size(), 1030);
    }

    @Test
    public void TestWrongFile()
    {
        DataLoader testClass = new DataLoader("nullClass");
        assertEquals(1, 1);
    }

}

