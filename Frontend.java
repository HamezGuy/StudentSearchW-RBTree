import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Frontend implements IFrontend{

    protected boolean isQuit = false;

    private IBackend backend;

    public Frontend(IBackend backend) {
        this.backend = backend;
      }


    @Override
    public void runCommandLoop() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("==============================\nWelcome to EasyBadgerSIS\n==============================");
        while(!isQuit){
            displayCommandMenu();
            try {
                String next = scnr.nextLine();
                switch(next) {
                    case "1":
                        searchByName();
                        System.out.println("Enter any key to continue:");
                        scnr.nextLine();
                        break;
                    case "2":
                        searchByScoreInterval();
                        System.out.println("Enter any key to continue:");
                        scnr.nextLine();
                        break;
                    case "3":
                        addStudent();
                        System.out.println("Enter any key to continue:");
                        scnr.nextLine();
                        break;
                    case "4":
                        removeByName();
                        System.out.println("Enter any key to continue:");
                        scnr.nextLine();
                        break;
                    case "5":
                        removeByScore();
                        System.out.println("Enter any key to continue:");
                        scnr.nextLine();
                        break;
                    case "6": case "q": case "Q":
                        scnr.close();
                        isQuit = true;
                        break;
                } 
            } catch (Exception e) {
                System.out.println("\n*************************\nInvalid input or internal error\n*************************\n");
            }
            
        }
        
    }

    @Override
    public void readData() {
        // did not implement by red frontend developer
    }

    @Override
    public void displayCommandMenu() {
        System.out.println("Main Menu:\n>>>>>>>>>>>>>>>>>>>>\n"
        + "View Information\n"
        + "     [1]Search grades by Name \n"
        + "     [2]Search Student's name by grade range \n"
        + "Editorial Information\n"
        + "     [3]Add Student information\n"
        + "     [4]Delete Student Information by name\n"
        + "     [5]Delete Student Information by grade range\n"
        + "Enter[6] or [q] to quit\n"
        + ">>>>>>>>>>>>>>>>>>>>\n"
        + "Choose a command from the menu above:");
    }

    @Override
    public void addStudent() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(">>>>>>>>>>>>>>>>>>>>\nAdd Student information\n--------------------");  
        System.out.println("Enter the name:");
        String name = scnr.nextLine();
        System.out.println("Enter the grades:");
        Double grades = scnr.nextDouble();
        Random random = new Random();
        String bio = "" + random.nextInt(10000);
        ArrayList<IStudent> list = new ArrayList<>();
        list.add(new Student_placeholder(name, bio, grades));
        System.out.println(">>>>>>>Finish>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        scnr.close();
    }

    @Override
    public void searchByName() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(">>>>>>>>>>>>>>>>>>>>\nSearch grades by Name\n--------------------");
        System.out.println("Enter the name:");
        List<IStudent> list = backend.searchByName(scnr.nextLine());
        display(list);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        scnr.close();
    }

    @Override
    public void searchByScoreInterval() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(">>>>>>>>>>>>>>>>>>>>\nSearch Student's name by grade range\n--------------------");
        System.out.println("Enter the Lower:");
        double lower = Double.valueOf(scnr.nextLine().toString());
        System.out.println("Enter the Upper:");
        double upper = Double.valueOf(scnr.nextLine().toString());
        List<IStudent> list = backend.searchByScoreInterval(lower, upper);
        display(list);
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        scnr.close();
    }

    @Override
    public void removeByName() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(">>>>>>>>>>>>>>>>>>>>\nDelete Student information\n--------------------");
        System.out.println("Enter the name:");
        String name = scnr.nextLine().toString();
        backend.removeByName(name);
        System.out.println(">>>>>>>Finish>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        scnr.close();
    }

    @Override
    public void removeByScore() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(">>>>>>>>>>>>>>>>>>>>\nDelete Student Information by grade range\n--------------------");
        System.out.println("Enter the Lower:");
        double lower = Double.valueOf(scnr.nextLine().toString());
        System.out.println("Enter the Upper:");
        double upper = Double.valueOf(scnr.nextLine().toString());
        backend.removeByScore(lower, upper);
        System.out.println(">>>>>>>Finish>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        scnr.close();
    }

    public void display(List<IStudent> student) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println("Found " + student.size() + " matches.");
        int count = 0;
        for(IStudent i : student) {
          count++;
          String str = "[" + count + "] " + i.getName() + "@" + i.getBio() + " --> Grade:"
        + i.getScore();
          System.out.println(str);
        }
      }

} 
