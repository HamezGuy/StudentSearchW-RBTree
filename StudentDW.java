public class StudentDW implements IStudent {
    public String firstName;
    public String lastName;
    public String studentName;
    public double testScore;
    public String bio;
    @Override
    public String getName() {

        return studentName;
    }
    @Override
    public double getScore() {
        // TODO Auto-generated method stub
        return testScore;
    }
    @Override
    public String getBio() {
        // TODO Auto-generated method stub
        return bio;
    }

}
