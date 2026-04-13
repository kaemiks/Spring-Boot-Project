package org.example.springbootv3.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.example.springbootv3.entity.Employee;
import org.example.springbootv3.entity.Group;
import org.example.springbootv3.repository.EmployeeRepository;
import org.example.springbootv3.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GroupRepository groupRepository;

    /**
     * Import pracowników z pliku CSV
     */
    public List<Employee> importEmployeesFromCsv(MultipartFile file) throws IOException, CsvException {
        List<Employee> employees = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> records = csvReader.readAll();

            // Pomiń nagłówek (pierwszy wiersz)
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);

                if (record.length >= 4) {
                    String firstName = record[0].trim();
                    String lastName = record[1].trim();
                    String email = record[2].trim();
                    String groupName = record[3].trim();

                    // Znajdź lub utwórz grupę
                    Group group = groupRepository.findByName(groupName)
                            .orElseGet(() -> {
                                Group newGroup = new Group(groupName, "Grupa utworzona z importu CSV");
                                return groupRepository.save(newGroup);
                            });

                    // Sprawdź czy email już istnieje
                    if (!employeeRepository.existsByEmail(email)) {
                        Employee employee = new Employee(firstName, lastName, email, group);
                        employees.add(employeeRepository.save(employee));
                    }
                }
            }
        }

        return employees;
    }

    /**
     * Eksport pracowników do CSV
     */
    public void exportEmployeesToCsv(List<Employee> employees, OutputStream outputStream) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Nagłówki
            String[] headers = {"Imię", "Nazwisko", "Email", "Grupa", "Opis grupy"};
            csvWriter.writeNext(headers);

            // Dane pracowników
            for (Employee employee : employees) {
                String[] record = {
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getGroup().getName(),
                        employee.getGroup().getDescription() != null ? employee.getGroup().getDescription() : ""
                };
                csvWriter.writeNext(record);
            }
        }
    }

    /**
     * Generuj CSV z wszystkimi pracownikami
     */
    public ByteArrayOutputStream generateEmployeesCsv() throws IOException {
        List<Employee> employees = employeeRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exportEmployeesToCsv(employees, outputStream);
        return outputStream;
    }

    /**
     * Walidacja pliku CSV
     */
    public boolean isValidCsvFile(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return "text/csv".equals(contentType) ||
                "application/vnd.ms-excel".equals(contentType) ||
                file.getOriginalFilename().endsWith(".csv");
    }

    /**
     * Import grup z CSV
     */
    public List<Group> importGroupsFromCsv(MultipartFile file) throws IOException, CsvException {
        List<Group> groups = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> records = csvReader.readAll();

            // Pomiń nagłówek
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);

                if (record.length >= 2) {
                    String name = record[0].trim();
                    String description = record[1].trim();

                    // Sprawdź czy grupa już istnieje
                    if (!groupRepository.existsByName(name)) {
                        Group group = new Group(name, description);
                        groups.add(groupRepository.save(group));
                    }
                }
            }
        }

        return groups;
    }
}