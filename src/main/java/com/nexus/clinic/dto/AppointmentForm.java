package com.nexus.clinic.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppointmentForm {
    @NotBlank(message = "Name is required") @Size(min = 2, max = 100)
    private String patientName;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[+0-9]{7,15}$", message = "Invalid phone number")
    private String phone;
    @Email(message = "Invalid email address")
    private String email;
    private Long serviceId;
    private Long therapistId;
    private Long branchId;
    @NotNull(message = "Please select a date")
    @Future(message = "Date must be in the future")
    private LocalDate preferredDate;
    @NotBlank(message = "Please select a time")
    private String preferredTime;
    @Size(max = 500)
    private String message;
}
