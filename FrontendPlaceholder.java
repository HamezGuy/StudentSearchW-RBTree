import java.util.List;
import java.util.Scanner;

/**
 * This class is a placeholder for the frontend
 *
 * @author Ruiuxan Tu
 */
public class FrontendPlaceholder implements IFrontend {
    IDataLoader loader;
    IBackend backend;
    Scanner scanner;

    public FrontendPlaceholder(IDataLoader loader, IBackend backend) {
        this.loader = loader;
        this.backend = backend;
        this.scanner = new Scanner(System.in);
    }

    public FrontendPlaceholder(String input, IDataLoader loader, IBackend backend) {
        this.loader = loader;
        this.backend = backend;
        this.scanner = new Scanner(input);
    }

    @Override
    public void runCommandLoop() {
        System.out.println("Welcome to EasyBadgerSIS");

        try {
            readData();
        } catch (Exception e) {
            System.out.println("Read data failed, use empty data instead");
        }

        while (true) {
            displayCommandMenu();
            try {
                String command = scanner.nextLine();
                switch (Integer.parseInt(command)) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        searchByName();
                        break;
                    case 3:
                        searchByScoreInterval();
                        break;
                    case 4:
                        removeByName();
                        break;
                    case 5:
                        removeByScore();
                        break;
                    case 6:
                        System.out.println("--------------------------------");
                        System.out.println("Bye bye");
                        return;
                }
            } catch (Exception e) {
                System.out.println("Invalid input or internal error");
            }
        }
    }

    @Override
    public void readData() {
        List<IStudent> students = loader.read();
        for (IStudent student : students)
            backend.addStudent(student);
    }

    @Override
    public void displayCommandMenu() {
        System.out.println("--------------------------------");
        System.out.println("[Command Menu]");
        System.out.println("1. Add a student");
        System.out.println("2. Search by name");
        System.out.println("3. Search by score interval");
        System.out.println("4. Remove by name");
        System.out.println("5. Remove by score interval");
        System.out.println("6. Exit");
        System.out.println("--------------------------------");
        System.out.println("There are " + backend.getNumberOfStudents() + " students in the system");
        System.out.print("Enter a command: ");
    }

    @Override
    public void addStudent() {
        System.out.println("--------------------------------");
        System.out.println("[Add a student]");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter score: ");
        double score = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter biography: ");
        String bio = scanner.nextLine();
        IStudent student = new Student(name, score, bio);
        backend.addStudent(student);
        loader.write(backend.getStudentsByName());
    }

    @Override
    public void searchByName() {
        System.out.println("--------------------------------");
        System.out.println("[Search by name]");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("--------------------------------");
        List<IStudent> result = backend.searchByName(name);
        System.out.println("Result: " + result.size() + " students");
        System.out.println(result);
    }

    @Override
    public void searchByScoreInterval() {
        System.out.println("--------------------------------");
        System.out.println("[Search by score interval]");
        System.out.print("Enter lower bound: ");
        double lower = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter upper bound: ");
        double upper = Double.parseDouble(scanner.nextLine());
        System.out.println("--------------------------------");
        List<IStudent> result = backend.searchByScoreInterval(lower, upper);
        System.out.println("Result: " + result.size() + " students");
        System.out.println(result);
    }

    @Override
    public void removeByName() {
        System.out.println("--------------------------------");
        System.out.println("[Remove by name]");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        backend.removeByName(name);
        loader.write(backend.getStudentsByName());
    }

    @Override
    public void removeByScore() {
        System.out.println("--------------------------------");
        System.out.println("[Remove by score interval]");
        System.out.print("Enter lower bound: ");
        double lower = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter upper bound: ");
        double upper = Double.parseDouble(scanner.nextLine());
        backend.removeByScore(lower, upper);
        loader.write(backend.getStudentsByScore());
    }
}
