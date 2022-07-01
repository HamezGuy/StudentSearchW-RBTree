import java.util.List;

/**
 * An instance of a class that implements the following interface can be used
 * to add and retrieve the database of students within the SIS app.
 */
public interface IBackend {
    /**
     * Add a student to the database
     *
     * @param student the student to be added
     */
    public void addStudent(IStudent student);

    /**
     * Retrieve number of students in database
     *
     * @return the number of students in the database
     */
    public int getNumberOfStudents();

    /**
     * Retrieve all students from the database in alphabetical order
     *
     * @return all students in the database sorted by name
     */
    public List<IStudent> getStudentsByName();

    /**
     * Retrieve all students from the database in numerical order
     *
     * @return all students in the database sorted by score
     */
    public List<IStudent> getStudentsByScore();

    /**
     * Retrieve students with a specific name from the database
     *
     * @param name the name of students to look up in the database
     * @return a list of students with the specified name
     */
    public List<IStudent> searchByName(String name);

    /**
     * Retrieve students with any score in an interval (inclusive) from the database
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     * @return a list of students with any score in the specified interval
     */
    public List<IStudent> searchByScoreInterval(double lower, double upper);

    /**
     * Remove students with a specific name from the database
     *
     * @param name the name of students to remove from the database
     */
    public void removeByName(String name);

    /**
     * Remove students with any score in an interval (inclusive) from the database
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     */
    public void removeByScore(double lower, double upper);
}
