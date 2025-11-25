package model;

import java.io.Serializable;

public class Developer extends Employee implements IDebuggable,IWorkable {
    private int overtimeHours;

    public Developer() {
    }

    public Developer(String id, String name, int age, double basicSalary, Device device, int overtimeHours) {
        super(id, name, age, basicSalary, device);
        this.overtimeHours = overtimeHours;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    @Override
    public double calculateSalary() {
        return getBasicSalary() + getBASE_SALARY_RATE() * overtimeHours;
    }

    @Override
    public void work() {
        System.out.println("Developer work");
    }

    @Override
    public void fixBug() {
        System.out.println("Fixing bug");
    }

    @Override
    public String toString() {
        return "Developer{" +
                "overtimeHours=" + overtimeHours +
                ", BASE_SALARY_RATE=" + BASE_SALARY_RATE + super.toString() + ", " + calculateSalary() +
                '}';
    }
}
