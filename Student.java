/**
 * Student class
 *
 * This class stores student profile
 *
 * @author Ruixuan Tu, Zhilin Du
 */
public class Student implements IStudent {
    protected final String name;
    protected final double score;
    protected final String bio;

    public Student(){
        this.name = null;
        this.score = 0;
        this.bio = null;
    }

    /**
     * Constructor
     *
     * @param name  name of the student
     * @param score score of the student
     * @param bio   biography of the student
     */
    public Student(String name, double score, String bio) {
        this.name = name;
        this.score = score;
        this.bio = bio;
    }

    /**
     * Getter for name
     *
     * @return name of the student
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for score
     *
     * @return score of the student
     */
    @Override
    public double getScore() {
        return this.score;
    }

    /**
     * Getter for bio
     *
     * @return biography of the student
     */
    @Override
    public String getBio() {
        return this.bio;
    }

    /**
     * toString method
     *
     * @return string representation of a Student object
     */
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", bio='" + bio + '\'' +
                '}';
    }
}
