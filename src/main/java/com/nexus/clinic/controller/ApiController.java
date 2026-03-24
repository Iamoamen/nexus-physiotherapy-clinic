package com.nexus.clinic.controller;

import com.nexus.clinic.dto.AppointmentForm;
import com.nexus.clinic.entity.*;
import com.nexus.clinic.repository.*;
import com.nexus.clinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Public REST API — read-only data for the public website frontend.
 * No admin endpoints here — those live in the secured /admin/** Thymeleaf controllers.
 *
 * CSRF is disabled only for /api/v1/health (GET, no state change).
 * All POST endpoints (appointments) go through the Thymeleaf form which
 * includes the CSRF token automatically.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final ServiceRepository       serviceRepository;
    private final TherapistRepository     therapistRepository;
    private final AppointmentService      appointmentService;
    private final BranchRepository        branchRepository;
    private final WorkingHoursRepository  workingHoursRepository;
    private final TestimonialRepository   testimonialRepository;
    private final FAQRepository           faqRepository;

    @GetMapping("/services")
    public ResponseEntity<List<Service>> getServices() {
        return ResponseEntity.ok(serviceRepository.findByActiveTrueOrderBySortOrderAsc());
    }

    @GetMapping("/therapists")
    public ResponseEntity<List<Therapist>> getTherapists() {
        return ResponseEntity.ok(therapistRepository.findByActiveTrueOrderBySortOrderAsc());
    }

    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> getBranches() {
        return ResponseEntity.ok(branchRepository.findByActiveTrueOrderByMainBranchDesc());
    }

    @GetMapping("/branches/{branchId}/hours")
    public ResponseEntity<List<WorkingHours>> getHours(@PathVariable Long branchId) {
        return ResponseEntity.ok(workingHoursRepository.findByBranchIdOrderByDayOfWeekAsc(branchId));
    }

    @GetMapping("/testimonials")
    public ResponseEntity<List<Testimonial>> getTestimonials() {
        return ResponseEntity.ok(
            testimonialRepository.findByApprovedTrueAndFeaturedTrueOrderByCreatedAtDesc());
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<FAQ>> getFaqs() {
        return ResponseEntity.ok(faqRepository.findByActiveTrueOrderBySortOrderAsc());
    }

    @PostMapping("/appointments")
    public ResponseEntity<Map<String, Object>> submitAppointment(
            @Valid @RequestBody AppointmentForm form) {
        try {
            Appointment saved = appointmentService.bookAppointment(form);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Appointment request received. We will confirm within 2 hours.",
                "appointmentId", saved.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Booking failed. Please try again."
                // FIX: never expose internal exception messages to client
            ));
        }
    }

    /** Simple liveness probe for Docker HEALTHCHECK and uptime monitors. */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
        // FIX: removed service name and version — no info leakage
    }
}
