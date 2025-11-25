package service;

import model.Employee;

import java.util.List;

public interface IEmployeeService {
    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(String id);
    public List<Employee> getEmployeeByName(String name);
}
