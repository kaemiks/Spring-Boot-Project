package org.example.springbootv3.controller;

import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.example.springbootv3.dto.EmployeeRequestDTO;
import org.example.springbootv3.dto.EmployeeResponseDTO;
import org.example.springbootv3.service.EmployeeService;
import org.example.springbootv3.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeResponseDTO> response = employees.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        EmployeeResponseDTO response = convertToResponseDTO(employee);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody EmployeeRequestDTO employeeRequest) {
        System.out.println("Otrzymany JSON pracownika: " + employeeRequest);

        // Znajdź grupę
        Group group = null;
        if (employeeRequest.getGroup() != null && employeeRequest.getGroup().getId() != null) {
            group = groupService.getGroupById(employeeRequest.getGroup().getId());
            System.out.println("Znaleziona grupa: " + group.getName());
        }

        // Utwórz pracownika
        Employee employee = new Employee(
                employeeRequest.getFirstName(),
                employeeRequest.getLastName(),
                employeeRequest.getEmail(),
                group
        );

        Employee savedEmployee = employeeService.saveEmployee(employee);
        EmployeeResponseDTO response = convertToResponseDTO(savedEmployee);

        System.out.println("Zapisany pracownik: " + response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO employeeRequest) {
        // Znajdź grupę
        Group group = null;
        if (employeeRequest.getGroup() != null && employeeRequest.getGroup().getId() != null) {
            group = groupService.getGroupById(employeeRequest.getGroup().getId());
        }

        Employee employee = new Employee(
                employeeRequest.getFirstName(),
                employeeRequest.getLastName(),
                employeeRequest.getEmail(),
                group
        );

        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        EmployeeResponseDTO response = convertToResponseDTO(updatedEmployee);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByGroup(@PathVariable Long groupId) {
        List<Employee> employees = employeeService.getEmployeesByGroupId(groupId);
        List<EmployeeResponseDTO> response = employees.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Metoda pomocnicza do konwersji - WAŻNE: nie serializuje Group
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getGroup() != null ? employee.getGroup().getName() : null,
                employee.getGroup() != null ? employee.getGroup().getId() : null
        );
    }
}