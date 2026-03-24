package com.nexus.clinic.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ContactForm {
    @NotBlank(message = "Name is required") private String name;
    @Email(message = "Invalid email") private String email;
    @NotBlank(message = "Phone is required") private String phone;
    @NotBlank(message = "Subject is required") private String subject;
    @NotBlank(message = "Message is required") @Size(min = 10, max = 1000) private String message;
}
