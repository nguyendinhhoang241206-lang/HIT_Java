package service.impl;

import model.Employee;
import service.IEmployeeService;

import java.util.List;

public class IEmployeeServiceImpl implements IEmployeeService {
    List<Employee> employees;
    @Override
    public List<Employee> getAllEmployees() {
        for(Employee e : employees){
            System.out.println(e.toString());
            return employees;
        }
        return null;
    }

    @Override
    public Employee getEmployeeById(String id) {
        for(Employee e : employees){
            if(e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    @Override
    public List<Employee> getEmployeeByName(String name) {
        for(Employee e : employees){
            if(e.getName().equals(name)){
                System.out.println(e.toString());
                return employees;
            }
        }
        return null;
    }
}