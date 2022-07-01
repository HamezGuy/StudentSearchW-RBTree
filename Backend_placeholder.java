import java.util.ArrayList;
import java.util.List;

public class Backend_placeholder implements IBackend{

    @Override
    public void addStudent(IStudent student) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getNumberOfStudents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<IStudent> getStudentsByName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IStudent> getStudentsByScore() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IStudent> searchByName(String name) {
        ArrayList<IStudent> list = new ArrayList<>();
        list.add(new Student_placeholder("Austin", "1234", 100));
        list.add(new Student_placeholder("Austin", "4321", 99.99));
        return list;
    }

    @Override
    public List<IStudent> searchByScoreInterval(double lower, double upper) {
        ArrayList<IStudent> list = new ArrayList<>();
        list.add(new Student_placeholder("Austin", "1234", 100));
        list.add(new Student_placeholder("Austin", "4321", 99.99));
        return list;
    }

    @Override
    public void removeByName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeByScore(double lower, double upper) {
        // TODO Auto-generated method stub
        
    }
    
}
