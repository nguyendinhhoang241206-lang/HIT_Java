package model;

public class Tester extends Employee implements IWorkable{
    private int bugsDetectedCount;

    public Tester() {
    }

    public Tester(String id, String name, int age, double basicSalary, Device device, int bugsDetectedCount) {
        super(id, name, age, basicSalary, device);
        this.bugsDetectedCount = bugsDetectedCount;
    }
    @Override
    public double calculateSalary(){
        return getBasicSalary() + getBASE_SALARY_RATE() * bugsDetectedCount;
    }
    @Override
    public void work() {
        System.out.println("Tester work");
    }

    @Override
    public String toString() {
        return "Tester{" +
                "bugsDetectedCount=" + bugsDetectedCount +
                ", BASE_SALARY_RATE=" + BASE_SALARY_RATE +  super.toString() + ", " + calculateSalary() +
                '}';
    }
}
