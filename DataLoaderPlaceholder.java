import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a data loader placeholder for Student objects
 *
 * @author Ruixuan Tu
 */
public class DataLoaderPlaceholder implements IDataLoader {
    protected String url;

    public DataLoaderPlaceholder(String url) {
        this.url = url;
    }

    @Override
    public void write(List<IStudent> list) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        for (IStudent student : list) {
            Element row = root.addElement("row");
            row.addElement("name").setText(student.getName());
            row.addElement("score").setText(String.valueOf(student.getScore()));
            row.addElement("bio").setText(student.getBio());
        }
        try {
            FileWriter fileWriter = new FileWriter(url);
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fileWriter, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<IStudent> read() {
        List<IStudent> list = new ArrayList<>();
        try {
            Document document = new SAXReader().read(url);
            Element root = document.getRootElement();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element row = it.next();
                String name = row.elementText("name");
                double score = Double.parseDouble(row.elementText("score"));
                String bio = row.elementText("bio");
                list.add(new Student(name, score, bio));
            }
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }
        return list;
    }
}
