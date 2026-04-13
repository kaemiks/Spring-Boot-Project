package org.example.springbootv3.service;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.example.springbootv3.repository.EmployeeRepository;
import org.example.springbootv3.repository.GroupRepository;
import org.example.springbootv3.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pracownik", "id", id));
    }

    public Employee saveEmployee(Employee employee) {
        // Sprawdź czy grupa istnieje
        if (employee.getGroup() != null && employee.getGroup().getId() != null) {
            Group group = groupRepository.findById(employee.getGroup().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupa", "id", employee.getGroup().getId()));
            employee.setGroup(group);
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());

        // Sprawdź czy grupa istnieje
        if (employeeDetails.getGroup() != null && employeeDetails.getGroup().getId() != null) {
            Group group = groupRepository.findById(employeeDetails.getGroup().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupa", "id", employeeDetails.getGroup().getId()));
            employee.setGroup(group);
        }

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    public List<Employee> getEmployeesByGroupId(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupa", "id", groupId));
        return employeeRepository.findByGroupId(groupId);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }
}