package com.example.week5_day3_lap6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @NotEmpty(message = " Id must not be empty")
    @Size(min = 3 , message = "Id length must be at least 3 characters")
    private String id;


    @NotEmpty(message = " Name must not be empty")
    @Size(min=5 , message = " Name length must be at least 5 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = " name must contain jest charecter")
    private String name;



    @Pattern(regexp = "^05\\d{8}$", message = "The mobile number must be starting with 05 and consisting of 10 digits")
    private String phoneNumber;



    @Email(message = " you must Enter email Correct")
    private String email;

    @NotNull(message = " Age cannot be empty")
    @Min(value = 26 , message = " age must be more than 25")
    private int age;


    @NotEmpty(message = " Position must be not empty")
    @Pattern(regexp = "^(?i)(supervisor|coordinator)$", message = "Position must be supervisor OR coordinator")
    private String position;



    private boolean onLeave =false;

    @NotNull(message = " Hire Date be empty")
    @PastOrPresent(message = "Start date must be today or in the Past ")
    private LocalDate hireDate;

    @NotNull(message = " Annual Leave Date be empty")
    @Min(value = 1 , message = " Annual leave must be at least 1")
    private int annualLeave;


}
