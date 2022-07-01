public class Student_placeholder implements IStudent{

    protected String name;
    protected String Bio;
    protected double Score;

    public Student_placeholder(String name,String Bio,double Score){
        this.name = name;
        this.Bio = Bio;
        this.Score = Score;

    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public String getBio() {
        // TODO Auto-generated method stub
        return Bio;
    }

    @Override
    public double getScore() {
        // TODO Auto-generated method stub
        return Score;
    }
    
}
