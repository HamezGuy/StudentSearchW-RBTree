/**
 * The main class to trigger all parts of the SIS application
 *
 * @author Ruixuan Tu
 */
public class SISApp {
    /**
     * The entry point of the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IDataLoader loader = new DataLoader("data.xml");

        IBackend backend = new SISBackend();

        // Frontend from red team does not work as it does not read data, use FrontendPlaceholder instead
        IFrontend frontend = new FrontendPlaceholder(loader, backend);

        frontend.runCommandLoop();
    }
}
