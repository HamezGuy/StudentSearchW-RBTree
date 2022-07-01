import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Backend class for the backend of the application
 *
 * @author Ruixuan Tu
 */
public class SISBackend implements IBackend {
    protected IStudentRBTree<String, StudentWrapper<String>> nameRBT;
    protected IStudentRBTree<Double, StudentWrapper<Double>> scoreRBT;

    /**
     * Constructor
     */
    SISBackend() {
        nameRBT = new StudentRBTreePlaceholder<>();
        scoreRBT = new StudentRBTreePlaceholder<>();
    }

    /**
     * Add a student to the database
     *
     * @param student the student to be added
     */
    @Override
    public void addStudent(IStudent student) {
        String name = student.getName();
        double score = student.getScore();
        nameRBT.add(name, new StudentWrapper<>(name, student));
        scoreRBT.add(score, new StudentWrapper<>(score, student));
        getNumberOfStudents();
    }

    /**
     * Retrieve number of students in database
     *
     * @return the number of students in the database
     */
    @Override
    public int getNumberOfStudents() {
        int nameSize = nameRBT.size();
        int scoreSize = scoreRBT.size();
        if (nameSize != scoreSize)
            throw new IllegalArgumentException("The number of students in the database is not equal");
        return nameSize;
    }

    /**
     * Retrieve all students from the database in alphabetical order
     *
     * @return all students in the database sorted by name
     */
    @Override
    public List<IStudent> getStudentsByName() {
        Iterator<IStudentRBTreeNode<String, StudentWrapper<String>>> it = nameRBT.iterator();
        List<IStudent> result = new ArrayList<>();
        while (it.hasNext()) {
            IStudentRBTreeNode<String, StudentWrapper<String>> next = it.next();
            List<StudentWrapper<String>> wrappedList = next.getValues();
            wrappedList.stream().map(wrappedStudent -> wrappedStudent.student).forEach(result::add);
        }
        return result;
    }

    /**
     * Retrieve all students from the database in numerical order
     *
     * @return all students in the database sorted by score
     */
    @Override
    public List<IStudent> getStudentsByScore() {
        Iterator<IStudentRBTreeNode<Double, StudentWrapper<Double>>> it = scoreRBT.iterator();
        List<IStudent> result = new ArrayList<>();
        while (it.hasNext()) {
            IStudentRBTreeNode<Double, StudentWrapper<Double>> next = it.next();
            List<StudentWrapper<Double>> wrappedList = next.getValues();
            wrappedList.stream().map(wrappedStudent -> wrappedStudent.student).forEach(result::add);
        }
        return result;
    }

    /**
     * Retrieve students with a specific name from the database
     *
     * @param name the name of students to look up in the database
     * @return a list of students with the specified name
     */
    @Override
    public List<IStudent> searchByName(String name) {
        Iterator<IStudentRBTreeNode<String, StudentWrapper<String>>> search = nameRBT.search(name);
        List<IStudent> result = new ArrayList<>();
        if (search != null)
            while (search.hasNext()) {
                IStudentRBTreeNode<String, StudentWrapper<String>> next = search.next();
                if (next.getKey().equals(name)) {
                    List<StudentWrapper<String>> wrappedList = next.getValues();
                    wrappedList.stream().map(wrappedStudent -> wrappedStudent.student).forEach(result::add);
                } else break;
            }
        return result;
    }

    /**
     * Retrieve students with any score in an interval (inclusive) from the database
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     * @return a list of students with any score in the specified interval
     */
    @Override
    public List<IStudent> searchByScoreInterval(double lower, double upper) {
        Iterator<IStudentRBTreeNode<Double, StudentWrapper<Double>>> search = scoreRBT.search(lower);
        List<IStudent> result = new ArrayList<>();
        if (search != null) {
            while (search.hasNext()) {
                IStudentRBTreeNode<Double, StudentWrapper<Double>> next = search.next();
                if (next.getKey() >= lower && next.getKey() <= upper) {
                    List<StudentWrapper<Double>> wrappedList = next.getValues();
                    wrappedList.stream().map(wrappedStudent -> wrappedStudent.student).forEach(result::add);
                } else break;
            }
        }
        return result;
    }

    /**
     * Remove students with a specific name from the database
     *
     * @param name the name of students to remove from the database
     */
    @Override
    public void removeByName(String name) {
        List<IStudent> toRemove = searchByName(name);
        nameRBT.removeByKey(name);
        toRemove.forEach(student -> scoreRBT.removeByValue(new StudentWrapper<>(student.getScore(), student)));
        if (nameRBT.size() != scoreRBT.size())
            throw new IllegalArgumentException("remove failed: non-equal size");
    }

    /**
     * Remove students with any score in an interval (inclusive) from the database
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     */
    @Override
    public void removeByScore(double lower, double upper) {
        List<IStudent> toRemove = searchByScoreInterval(lower, upper);
        toRemove.forEach(student -> {
            scoreRBT.removeByKey(student.getScore());
            nameRBT.removeByValue(new StudentWrapper<>(student.getName(), student));
        });
        if (nameRBT.size() != scoreRBT.size())
            throw new IllegalArgumentException("remove failed: non-equal size");
    }
}
