/**
 * Instances of classes that implement this interface can be used to drive a
 * console-based text user interface for the ShowSearcher App.
 */
public interface IFrontend {
    // constructor args (DataLoader, SISBackend) reads input from System.in
    // constructor args (String, DataLoader, SISBackend) reads input from String

    /**
     * This method drives the entire read, eval, print loop (repl) for the
     * EasyBadgerSIS App.  This loop will continue to run until the user
     * explicitly enters the quit command.
     */
    public void runCommandLoop();

    // the following helper methods will help support runCommandLoop():

    public void readData();

    public void displayCommandMenu(); // prints command options to System.out

    public void addStudent(); // reads the information of one student from System.in, adds to list

    public void searchByName(); // reads one string from System.in, displays results

    public void searchByScoreInterval(); // reads two double decimals from System.in, displays results

    public void removeByName(); // reads one string from System.in, remove all matches

    public void removeByScore(); // reads two double decimals from System.in, remove all matches
}
