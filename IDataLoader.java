import java.util.List;

public interface IDataLoader {
    // adds data to xml file
    public void write(List<IStudent> list);

    // reads data from XML file, returns a list
    public List<IStudent> read();
}
