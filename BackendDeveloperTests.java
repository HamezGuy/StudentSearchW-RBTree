import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for backend developer code (SISBackend and SISApp)
 *
 * @author Ruixuan Tu
 */
public class BackendDeveloperTests {
    protected SISBackend backend;
    protected List<IStudent> expectedList, actualList;
    protected InputStream in;
    protected PrintStream out;
    protected ByteArrayInputStream testIn;
    protected ByteArrayOutputStream testOut;

    @BeforeAll
    static void initialize() throws IOException {
        Files.copy(Path.of("data-orig.xml"), Path.of("data.xml"), StandardCopyOption.REPLACE_EXISTING);
    }

    @BeforeEach
    public void setUp() {
        backend = new SISBackend();
        expectedList = new ArrayList<>();
        actualList = new ArrayList<>();
        in = System.in;
        out = System.out;
    }

    /**
     * Backend: Add and Normal Query Test
     */
    @Test
    public void test01() {
        Student a = new Student("A", 100.0, "hello");
        Student b = new Student("b", 95.66, "world");
        backend.addStudent(a);
        backend.addStudent(b);

        expectedList.clear();
        expectedList.add(a);
        actualList = backend.searchByName("A");
        assertEquals(expectedList, actualList);

        actualList = backend.searchByScoreInterval(90, 100);
        expectedList.clear();
        expectedList.add(b);
        expectedList.add(a);
        assertEquals(expectedList, actualList);

        actualList = backend.searchByScoreInterval(100, -90);
        expectedList.clear();
        assertEquals(expectedList, actualList);
    }

    /**
     * Backend: Empty Backend Test
     */
    @Test
    public void test02() {
        expectedList.clear();

        actualList = backend.searchByName("");
        assertEquals(expectedList, actualList);

        actualList = backend.searchByName("A");
        assertEquals(expectedList, actualList);

        actualList = backend.searchByScoreInterval(0, 1000000);
        assertEquals(expectedList, actualList);

        backend.removeByName("");
        actualList = backend.searchByScoreInterval(0, -1);
        assertEquals(expectedList, actualList);

        backend.removeByScore(0, 1000000);
        actualList = backend.searchByScoreInterval(-1000000, 1000000);
        assertEquals(expectedList, actualList);
    }

    /**
     * Backend: Add and Remove Test
     */
    @Test
    public void test03() {
        Student a = new Student("A", 100.0, "konnichiwa");
        Student b = new Student("b", 95.66, "sekai");
        Student c = new Student("Diana", 22637261, "guanzhu jiaran, dundun jiechan");
        Student d = new Student("senpai", 114.514, "24");
        backend.addStudent(a);
        backend.addStudent(b);
        backend.addStudent(c);
        backend.addStudent(d);
        assertEquals(4, backend.getNumberOfStudents());

        backend.removeByName(a.getName());
        expectedList.clear();
        actualList = backend.searchByName("A");
        assertEquals(expectedList, actualList);
        assertEquals(3, backend.getNumberOfStudents());

        actualList = backend.searchByScoreInterval(90, 114.514);
        expectedList.clear();
        expectedList.add(b);
        expectedList.add(d);
        assertEquals(expectedList, actualList);
        assertEquals(3, backend.getNumberOfStudents());

        actualList = backend.searchByScoreInterval(90, 100000000);
        expectedList.add(c);
        assertEquals(expectedList, actualList);
        assertEquals(3, backend.getNumberOfStudents());

        backend.removeByScore(100, 100000000);
        expectedList.clear();
        expectedList.add(b);
        actualList = backend.searchByScoreInterval(0, 100000000);
        assertEquals(expectedList, actualList);
        assertEquals(1, backend.getNumberOfStudents());
    }

    /**
     * Backend: Same Key Test with Add and Remove
     */
    @Test
    public void test04() {
        Student a = new Student("A", 100.0, "konnichiwa");
        Student b = new Student("B", 95.66, "sekai");
        Student c = new Student("A", 22637261, "guanzhu jiaran, dundun jiechan");
        Student d = new Student("B", 114.514, "24");
        backend.addStudent(a);
        backend.addStudent(b);
        backend.addStudent(c);
        backend.addStudent(d);
        assertEquals(4, backend.getNumberOfStudents());

        actualList = backend.searchByScoreInterval(90, 114.514);
        expectedList.clear();
        expectedList.add(b);
        expectedList.add(a);
        expectedList.add(d);
        assertEquals(expectedList, actualList);
        assertEquals(4, backend.getNumberOfStudents());

        actualList = backend.searchByScoreInterval(90, 100000000);
        expectedList.add(c);
        assertEquals(expectedList, actualList);
        assertEquals(4, backend.getNumberOfStudents());

        backend.removeByScore(101, 100000000);
        expectedList.clear();
        expectedList.add(b);
        expectedList.add(a);
        actualList = backend.searchByScoreInterval(0, 100000000);
        assertEquals(expectedList, actualList);
        assertEquals(2, backend.getNumberOfStudents());

        backend.removeByName(a.getName());
        expectedList.clear();
        actualList = backend.searchByName("A");
        assertEquals(expectedList, actualList);
        expectedList.clear();
        expectedList.add(b);
        actualList = backend.searchByName("B");
        assertEquals(1, backend.getNumberOfStudents());

        backend.removeByName(b.getName());
        expectedList.clear();
        actualList = backend.searchByName("B");
        assertEquals(expectedList, actualList);
        assertEquals(0, backend.getNumberOfStudents());
    }

    /**
     * Backend: Exception Test
     */
    @Test
    public void test05() {
        Student a = new Student("A", 100.0, "konnichiwa");
        Student b = new Student("B", 95.66, "sekai");
        Student c = new Student("C", 22637261, "guanzhu jiaran, dundun jiechan");
        Student d = new Student("D", 114.514, "24");
        backend.addStudent(a);
        backend.addStudent(b);
        backend.addStudent(c);
        backend.addStudent(d);
        assertEquals(4, backend.getNumberOfStudents());

        backend.nameRBT.add(a.getName(), new StudentWrapper<>(a.getName(), a));
        assertThrows(IllegalArgumentException.class, () -> backend.getNumberOfStudents());
        assertThrows(IllegalArgumentException.class, () -> backend.addStudent(a));
        assertThrows(IllegalArgumentException.class, () -> backend.removeByName(""));
        assertThrows(IllegalArgumentException.class, () -> backend.removeByScore(0, 0));

        backend.removeByName(a.getName());
        assertEquals(3, backend.getNumberOfStudents());
    }

    /**
     * App: Launch Test
     */
    @Test
    public void test06() {
        testIn = new ByteArrayInputStream("6".getBytes());
        System.setIn(testIn);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        SISApp.main(new String[0]);
        String expectedOut = "Welcome to EasyBadgerSIS\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1029 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "Bye bye\n";
        assertEquals(expectedOut, testOut.toString());

        System.setIn(in);
        System.setOut(out);
    }

    /**
     * App: Search Interval Test
     */
    @Test
    public void test07() {
        testIn = new ByteArrayInputStream("3\n60.22\n65.33\n6".getBytes());
        System.setIn(testIn);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        SISApp.main(new String[0]);
        String expectedOut = "Welcome to EasyBadgerSIS\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1029 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "[Search by score interval]\n" +
                "Enter lower bound: Enter upper bound: --------------------------------\n" +
                "Result: 142 students\n" +
                "[Student{name='Junior  Mcdowell', score=60.25, bio='241'}, Student{name='Rosalinde Jeeves', score=60.25, bio='241'}, Student{name='Darius Paladini', score=60.25, bio='241'}, Student{name='Aurea Settle', score=60.25, bio='241'}, Student{name='Cameron Saleway', score=60.25, bio='241'}, Student{name='Karel Bedson', score=60.25, bio='241'}, Student{name='Sibby Grzelewski', score=60.25, bio='241'}, Student{name='Vincent Aust', score=60.25, bio='241'}, Student{name='Kristine Triner', score=60.25, bio='241'}, Student{name='Corie Manuaud', score=60.25, bio='241'}, Student{name='Melly Gaish', score=60.25, bio='241'}, Student{name='Gisella Davidek', score=60.25, bio='241'}, Student{name='Selestina Lapsley', score=60.25, bio='241'}, Student{name='Roberta Yeldon', score=60.25, bio='241'}, Student{name='Dominique Greiswood', score=60.25, bio='241'}, Student{name='Starlin Shurmer', score=60.25, bio='241'}, Student{name='Matty Mussard', score=60.25, bio='241'}, Student{name='Steffi Jacobssen', score=60.25, bio='241'}, Student{name='Rayshell Soan', score=60.25, bio='241'}, Student{name='Nate Headland', score=60.25, bio='241'}, Student{name='Boycie Massy', score=60.25, bio='241'}, Student{name='Rupert Cragell', score=60.25, bio='241'}, Student{name='Trenton Figgs', score=60.25, bio='241'}, Student{name='Isa Levins', score=60.25, bio='241'}, Student{name='Clarance Klugel', score=60.25, bio='241'}, Student{name='Barb Barraclough', score=60.25, bio='241'}, Student{name='Far McMichael', score=60.25, bio='241'}, Student{name='Alphonse Cure', score=60.25, bio='241'}, Student{name='Zeb Greim', score=60.25, bio='241'}, Student{name='Uriah Wigfall', score=60.25, bio='241'}, Student{name='Edithe Carncross', score=60.25, bio='241'}, Student{name='Amy Ottawell', score=60.25, bio='241'}, Student{name='Khalil Botwood', score=60.25, bio='241'}, Student{name='Zackariah Swarbrigg', score=60.25, bio='241'}, Student{name='Weidar Fellos', score=60.25, bio='241'}, Student{name='Zehra  Tolomelli', score=61.0, bio='244'}, Student{name='Dukey Rubinivitz', score=61.0, bio='244'}, Student{name='Patrice Jeanneau', score=61.0, bio='244'}, Student{name='Renaldo Tolomelli', score=61.0, bio='244'}, Student{name='Gussie Jedrzaszkiewicz', score=61.0, bio='244'}, Student{name='Rosalia Lockart', score=61.0, bio='244'}, Student{name='Norbert Carabine', score=61.0, bio='244'}, Student{name='Robyn Pleming', score=61.0, bio='244'}, Student{name='Thurston Slane', score=61.0, bio='244'}, Student{name='Court Henbury', score=61.0, bio='244'}, Student{name='Melisenda Gaspar', score=61.0, bio='244'}, Student{name='Kathy Costy', score=61.0, bio='244'}, Student{name='Colette Hall', score=61.0, bio='244'}, Student{name='Stormie Keates', score=61.0, bio='244'}, Student{name='Blinny Itzig', score=61.0, bio='244'}, Student{name='Chelsae Rimmer', score=61.0, bio='244'}, Student{name='Karyl Horburgh', score=61.0, bio='244'}, Student{name='Donn Pietersma', score=61.0, bio='244'}, Student{name='Abran Gilogly', score=61.0, bio='244'}, Student{name='Consuela Leaning', score=61.0, bio='244'}, Student{name='Charmain Mellonby', score=61.0, bio='244'}, Student{name='Linell Dunseith', score=61.0, bio='244'}, Student{name='Pen Lory', score=61.0, bio='244'}, Student{name='Wernher Beville', score=61.0, bio='244'}, Student{name='Connie Stannard', score=61.0, bio='244'}, Student{name='Adella Thonason', score=61.0, bio='244'}, Student{name='Rudie Osan', score=61.0, bio='244'}, Student{name='Eddy Bartoszinski', score=61.0, bio='244'}, Student{name='Julius O'Scollee', score=61.0, bio='244'}, Student{name='Rianon Gjerde', score=61.0, bio='244'}, Student{name='Abagael Wasmer', score=61.0, bio='244'}, Student{name='Claudian Burree', score=61.0, bio='244'}, Student{name='Mirelle Hadlington', score=61.0, bio='244'}, Student{name='Armand Greaves', score=61.0, bio='244'}, Student{name='Claybourne Petrenko', score=61.0, bio='244'}, Student{name='Louis  Underwood', score=62.75, bio='251'}, Student{name='Maurits Caughtry', score=62.75, bio='251'}, Student{name='Mikey Stollwerk', score=62.75, bio='251'}, Student{name='Juline Drivers', score=62.75, bio='251'}, Student{name='Chance Hearthfield', score=62.75, bio='251'}, Student{name='Lesley Visick', score=62.75, bio='251'}, Student{name='Lyndsie Maudlin', score=62.75, bio='251'}, Student{name='Marten Cockley', score=62.75, bio='251'}, Student{name='Clayborne Palombi', score=62.75, bio='251'}, Student{name='Kirbee Breissan', score=62.75, bio='251'}, Student{name='Dudley Doumenc', score=62.75, bio='251'}, Student{name='La verne Trowill', score=62.75, bio='251'}, Student{name='Noe Grimsdale', score=62.75, bio='251'}, Student{name='Melisenda Burnsides', score=62.75, bio='251'}, Student{name='Mirna Deavall', score=62.75, bio='251'}, Student{name='Leila Whistan', score=62.75, bio='251'}, Student{name='Dara Ferrier', score=62.75, bio='251'}, Student{name='Cecilio Turneux', score=62.75, bio='251'}, Student{name='Abbot Heckney', score=62.75, bio='251'}, Student{name='Garnet Zecchini', score=62.75, bio='251'}, Student{name='Padriac Georger', score=62.75, bio='251'}, Student{name='Katrina Badsworth', score=62.75, bio='251'}, Student{name='Mannie Chrystie', score=62.75, bio='251'}, Student{name='Christian Dumigan', score=62.75, bio='251'}, Student{name='Herc Kitchinham', score=62.75, bio='251'}, Student{name='Farleigh Wellington', score=62.75, bio='251'}, Student{name='Aylmer Conklin', score=62.75, bio='251'}, Student{name='Gabi McGaugey', score=62.75, bio='251'}, Student{name='Archibold Rowbottam', score=62.75, bio='251'}, Student{name='Rosemary Tummasutti', score=62.75, bio='251'}, Student{name='Clerkclaude Bigg', score=62.75, bio='251'}, Student{name='Kare Zimmermanns', score=62.75, bio='251'}, Student{name='Sancho Week', score=62.75, bio='251'}, Student{name='Vladamir Sinkins', score=62.75, bio='251'}, Student{name='Brunhilda Kennifeck', score=62.75, bio='251'}, Student{name='Shelbi Tesimon', score=62.75, bio='251'}, Student{name='Velma  Paul', score=63.0, bio='252'}, Student{name='Lonnie Tebbett', score=63.0, bio='252'}, Student{name='Virginie Behne', score=63.0, bio='252'}, Student{name='Grata Dedam', score=63.0, bio='252'}, Student{name='Kris Cochrane', score=63.0, bio='252'}, Student{name='Derick Kings', score=63.0, bio='252'}, Student{name='Reginauld Kibbel', score=63.0, bio='252'}, Student{name='Engracia BURWIN', score=63.0, bio='252'}, Student{name='Marlo Donegan', score=63.0, bio='252'}, Student{name='Raynor Bister', score=63.0, bio='252'}, Student{name='Marybeth Bickardike', score=63.0, bio='252'}, Student{name='Rosmunda Ebrall', score=63.0, bio='252'}, Student{name='Kendrick Charnock', score=63.0, bio='252'}, Student{name='Alvie O'Gavin', score=63.0, bio='252'}, Student{name='Lauritz Connealy', score=63.0, bio='252'}, Student{name='Davide Rodell', score=63.0, bio='252'}, Student{name='Kevan Elwell', score=63.0, bio='252'}, Student{name='Nessa Norris', score=63.0, bio='252'}, Student{name='Jenine Haggata', score=63.0, bio='252'}, Student{name='Fabio Catonne', score=63.0, bio='252'}, Student{name='Donavon Salsberg', score=63.0, bio='252'}, Student{name='Hesther Mattiato', score=63.0, bio='252'}, Student{name='Poul Kuzma', score=63.0, bio='252'}, Student{name='Lewiss Ailward', score=63.0, bio='252'}, Student{name='Jule Bartaloni', score=63.0, bio='252'}, Student{name='Gayle Bartoletti', score=63.0, bio='252'}, Student{name='Gale Abdey', score=63.0, bio='252'}, Student{name='Salmon Florence', score=63.0, bio='252'}, Student{name='Maximo Liddel', score=63.0, bio='252'}, Student{name='Laurence Treske', score=63.0, bio='252'}, Student{name='Farleigh Thurstan', score=63.0, bio='252'}, Student{name='Shelbi Machin', score=63.0, bio='252'}, Student{name='Nicole Calderwood', score=63.0, bio='252'}, Student{name='Jaye Margett', score=63.0, bio='252'}, Student{name='Morley Abdy', score=63.0, bio='252'}, Student{name='Krispin Clifton', score=63.0, bio='252'}]\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1029 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "Bye bye\n";
        assertEquals(expectedOut, testOut.toString());

        System.setIn(in);
        System.setOut(out);
    }

    /**
     * App: Add Student and Search Name Test
     */
    @Test
    public void test08() {
        testIn = new ByteArrayInputStream("1\nRuixuan Tu\n100.00\nbadger\n2\nRuixuan Tu\n6".getBytes());
        System.setIn(testIn);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        SISApp.main(new String[0]);
        String expectedOut = "Welcome to EasyBadgerSIS\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1029 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "[Add a student]\n" +
                "Enter name: Enter score: Enter biography: --------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1030 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "[Search by name]\n" +
                "Enter name: --------------------------------\n" +
                "Result: 1 students\n" +
                "[Student{name='Ruixuan Tu', score=100.0, bio='badger'}]\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1030 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "Bye bye\n";
        assertEquals(expectedOut, testOut.toString());

        System.setIn(in);
        System.setOut(out);
    }

    /**
     * App: Save and Remove Interval Test
     */
    @Test
    public void test09() {
        testIn = new ByteArrayInputStream("5\n100\n100.00\n6".getBytes());
        System.setIn(testIn);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        SISApp.main(new String[0]);
        String expectedOut = "Welcome to EasyBadgerSIS\n" +
                "--------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1030 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "[Remove by score interval]\n" +
                "Enter lower bound: Enter upper bound: --------------------------------\n" +
                "[Command Menu]\n" +
                "1. Add a student\n" +
                "2. Search by name\n" +
                "3. Search by score interval\n" +
                "4. Remove by name\n" +
                "5. Remove by score interval\n" +
                "6. Exit\n" +
                "--------------------------------\n" +
                "There are 1029 students in the system\n" +
                "Enter a command: --------------------------------\n" +
                "Bye bye\n";
        assertEquals(expectedOut, testOut.toString());

        System.setIn(in);
        System.setOut(out);
    }

    /**
     * DataLoader: Read Test
     */
    @Test
    public void test10() {
        IDataLoader dataLoaderP = new DataLoaderPlaceholder("data.xml");
        IDataLoader dataLoader = new DataLoader("data.xml");
        List<IStudent> resultDLP = dataLoaderP.read();
        List<IStudent> resultDL = dataLoader.read();
        assertEquals(resultDLP.toString(), resultDL.toString()); // placeholder should have correct result
    }

    /**
     * Frontend: Add Test
     */
    @Test
    public void test11() {
        // use "3 Anabal Durker1\n42.234\n6" to make work, not expected
        testIn = new ByteArrayInputStream("3\nAnabal Durker1\n42.234\n6".getBytes());
        System.setIn(testIn);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        IFrontend frontend = new Frontend(new SISBackend());
        frontend.addStudent();
        String expectedOut = ">>>>>>>>>>>>>>>>>>>>\n" +
                "Add Student information\n" +
                "--------------------\n" +
                "Enter the name:\n" +
                "Enter the grades:\n" +
                ">>>>>>>Finish>>>>>>>\n" +
                ">>>>>>>>>>>>>>>>>>>>\n";

        assertEquals(expectedOut, testOut.toString());

        System.setIn(in);
        System.setOut(out);
    }
}
