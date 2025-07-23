package com.example.week5_day3_lap6.Controller;

import com.example.week5_day3_lap6.API.ApiResponse;
import com.example.week5_day3_lap6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
  
    public ResponseEntity<?> getAllEmployee() {

        if (employees.isEmpty()) {

            return ResponseEntity.status(404).body(new ApiResponse("Employee is empty"));
        }

        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee employee, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }


        for (Employee e : employees) {
            if (e.getId().equals(employee.getId())) {

                return ResponseEntity.badRequest().body(new ApiResponse("Employee already exists"));
            }
        }

        employees.add(employee);

        return ResponseEntity.status(201).body(new ApiResponse("Employee added successfully"));


    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> upDateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee, Errors errors) {

        if (errors.hasErrors()) {

            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        for (Employee e : employees) {


            if (e.getId().equals(id)) {


                for (Employee e1 : employees) {
                    if (!e1.getId().equals(id) && e1.getId().equals(employee.getId())) {
                        return ResponseEntity.badRequest().body(new ApiResponse("Employee already exists"));
                    }
                }
                employees.set(employees.indexOf(e), employee);
                return ResponseEntity.status(201).body(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        if (employees.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees, cannot be deleted"));
        }

        for (Employee e : employees) {
            if (e.getId().equals(id)) {
                employees.remove(e);
                return ResponseEntity.status(201).body(new ApiResponse("Employee deleted successfully"));
            }

        }

        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));

    }

    @GetMapping("searchbyposition/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position) {

        if (position.equalsIgnoreCase("coordinator") || position.equalsIgnoreCase("supervisor")) {
            ArrayList<Employee> list = new ArrayList<>();


            if (position.equalsIgnoreCase("coordinator")) {


                for (Employee e : employees) {

                    if (e.getPosition().equalsIgnoreCase("coordinator")) {
                        list.add(e);
                    }

                }
                return ResponseEntity.status(200).body(list);
            }

            if (position.equalsIgnoreCase("supervisor")) {


                for (Employee e : employees) {

                    if (e.getPosition().equalsIgnoreCase("supervisor")) {
                        list.add(e);
                    }

                }
                return ResponseEntity.status(200).body(list);
            }

        }
        return ResponseEntity.badRequest().body(new ApiResponse("Invalid position"));


    }


    @GetMapping("searchage/{min}/{max}")
    public ResponseEntity<?> getEmployeeByAge(@PathVariable int max, @PathVariable int min) {
        if (employees.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("There are no employees"));
        }


        if (max < 0 || min < 0 || min > max) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid range"));
        }

        ArrayList<Employee> list = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAge() >= min && e.getAge() <= max) {
                list.add(e);
            }
        }
        if (list.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No employees found in this range"));
        }
        return ResponseEntity.status(200).body(list);


    }


    @PutMapping("/applyleave/{id}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String id) {

        for (Employee e : employees) {


            if (e.getId().equals(id)) {

                if (e.isOnLeave()) {
                    return ResponseEntity.badRequest().body(new ApiResponse("Employee is already on leave"));
                }

                if (e.getAnnualLeave() <= 0) {
                    return ResponseEntity.badRequest().body(new ApiResponse("No annual leave remaining"));
                }


                e.setOnLeave(true);
                e.setAnnualLeave(e.getAnnualLeave() - 1);

                return ResponseEntity.ok(new ApiResponse("Leave applied successfully"));
            }
        }

        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }


    @GetMapping("/noannualleave")
    public ResponseEntity<?> getEmployeesWithNoAnnualLeave() {

        if (employees.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("There are no employees"));
        }

        ArrayList<Employee> result = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0) {
                result.add(e);
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse("All employees have annual leave remaining"));
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/promote/{requesterId}/{employeeId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String employeeId, @PathVariable String requesterId) {

        Employee requester = null;
        Employee emp = null;

        for (Employee e : employees) {
            if (e.getId().equals(requesterId)) {
                requester = e;
            }
            if (e.getId().equals(employeeId)) {
                emp = e;
            }
        }

        if (requester == null) {

            return ResponseEntity.status(404).body(new ApiResponse("Requester not found"));
        }

        if (emp == null) {

            return ResponseEntity.status(404).body(new ApiResponse("Employee to promote not found"));
        }

        if (emp.getPosition().equalsIgnoreCase("supervisor")) {

            return ResponseEntity.badRequest().body(new ApiResponse("Employee is already a supervisor"));
        }

        if (!requester.getPosition().equalsIgnoreCase("supervisor")) {
            return ResponseEntity.badRequest().body(new ApiResponse("Only supervisors can promote employees"));
        }

        if (emp.getAge() < 30) {

            return ResponseEntity.badRequest().body(new ApiResponse("Employee must be at least 30 years old to be promoted"));
        }

        if (emp.isOnLeave()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Cannot promote an employee who is on leave"));
        }

        emp.setPosition("supervisor");
        return ResponseEntity.ok(new ApiResponse("Employee promoted successfully"));
    }


}
