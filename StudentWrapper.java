import java.util.Objects;

/**
 * Student Wrapper for comparing students
 *
 * @param <T> key type
 * @author Ruixuan Tu
 */
public class StudentWrapper<T extends Comparable<T>> implements Comparable<StudentWrapper<T>> {
    public T key;
    public IStudent student;

    /**
     * Constructor
     *
     * @param key     key to construct with
     * @param student student to construct with
     */
    public StudentWrapper(T key, IStudent student) {
        this.key = key;
        this.student = student;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(StudentWrapper<T> o) {
        return key.compareTo(o.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentWrapper<?> that = (StudentWrapper<?>) o;
        return Objects.equals(key, that.key) && Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, student);
    }

    @Override
    public String toString() {
        return "StudentWrapper{" +
                "key=" + key +
                ", student=" + student +
                '}';
    }
}
