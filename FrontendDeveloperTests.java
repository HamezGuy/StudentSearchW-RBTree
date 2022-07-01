import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;

import org.junit.*;

public class FrontendDeveloperTests {
    static Frontend frontend = new Frontend(new Backend_placeholder());
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    /**
     * Test if the display command functions properly.
     */
    @Test
    public void displayCommandMenuTester() {
        final String testString = "Main Menu:\n>>>>>>>>>>>>>>>>>>>>\n"
        + "View Information\n"
        + "     [1]Search grades by Name \n"
        + "     [2]Search Student's name by grade range \n"
        + "Editorial Information\n"
        + "     [3]Add Student information\n"
        + "     [4]Delete Student Information by name\n"
        + "     [5]Delete Student Information by grade range\n"
        + "Enter[6] or [q] to quit\n"
        + ">>>>>>>>>>>>>>>>>>>>\n"
        + "Choose a command from the menu above:\n";

        frontend.displayCommandMenu();
        assertEquals(testString,getOutput());
    }

    /**
     * Test if the add student UI properly.
     */
    @Test
    public void addStudentTester() {
        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Add Student information\n"
        + "--------------------\n"
        + "Enter the name:\n"
        + "Enter the grades:\n"
        + ">>>>>>>Finish>>>>>>>\n"
        + ">>>>>>>>>>>>>>>>>>>>\n";

        provideInput("name1\n98.88");
        frontend.addStudent();

        assertEquals(Expectations, getOutput());
    }

    /**
     * Test if the search by name UI properly.
     */
    @Test
    public void searchByNameTester() {
        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Search grades by Name\n"
        + "--------------------\n"
        + "Enter the name:\n"
        + ">>>>>>>>>>>>>>>>>>>>\n"
        + "Found 2 matches.\n"
        + "[1] Austin@1234 --> Grade:100.0\n"
        + "[2] Austin@4321 --> Grade:99.99\n"
        + ">>>>>>>>>>>>>>>>>>>>\n";

        provideInput("Austin");
        frontend.searchByName();

        assertEquals(Expectations, getOutput());
    }

    /**
     * Test if the search by score interval UI properly.
     */
    @Test
    public void searchByScoreIntervalTester() {
        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Search Student's name by grade range\n"
        + "--------------------\n"
        + "Enter the Lower:\n"
        + "Enter the Upper:\n"
        + ">>>>>>>>>>>>>>>>>>>>\n"
        + "Found 2 matches.\n"
        + "[1] Austin@1234 --> Grade:100.0\n"
        + "[2] Austin@4321 --> Grade:99.99\n"
        + ">>>>>>>>>>>>>>>>>>>>\n";

        provideInput("110\n90");
        frontend.searchByScoreInterval();

        assertEquals(Expectations, getOutput());
    }

    /**
     * Test if the remove by name UI properly.
     */
    @Test
    public void removeByNameTester() {
        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Delete Student information\n"
        + "--------------------\n"
        + "Enter the name:\n"
        + ">>>>>>>Finish>>>>>>>\n"
        + ">>>>>>>>>>>>>>>>>>>>\n";

        provideInput("name1\n98.88");
        frontend.removeByName();

        assertEquals(Expectations, getOutput());
    }

    /**
     * Test if the remove by score UI properly.
     */
    @Test
    public void removeByScoreTester() {
        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Delete Student Information by grade range\n"
        + "--------------------\n"
        + "Enter the Lower:\n"
        + "Enter the Upper:\n"
        + ">>>>>>>Finish>>>>>>>\n"
        + ">>>>>>>>>>>>>>>>>>>>\n";

        provideInput("110\n90");
        frontend.removeByScore();

        assertEquals(Expectations, getOutput());
    }

    /**
     * Test if the IStudent to String function properly.
     */
    @Test
    public void displayTester(){
        ArrayList<IStudent> list = new ArrayList<>();
        list.add(new Student_placeholder("Austin", "1234", 100));
        list.add(new Student_placeholder("Austin", "4321", 99.99));
        frontend.display(list);

        final String Expectations = ">>>>>>>>>>>>>>>>>>>>\n"
        + "Found 2 matches.\n"
        + "[1] Austin@1234 --> Grade:100.0\n"
        + "[2] Austin@4321 --> Grade:99.99\n";
        
        assertEquals(Expectations, getOutput());
    }
}
