package com.nexus.clinic.service;

import com.nexus.clinic.dto.AppointmentForm;
import com.nexus.clinic.entity.*;
import com.nexus.clinic.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository     serviceRepository;
    private final TherapistRepository   therapistRepository;
    private final BranchRepository      branchRepository;
    private final EmailService          emailService;

    public Appointment bookAppointment(AppointmentForm form) {
        Appointment apt = Appointment.builder()
            .patientName(form.getPatientName())
            .phone(form.getPhone())
            .email(form.getEmail())
            .preferredDate(form.getPreferredDate())
            .preferredTime(form.getPreferredTime())
            .message(form.getMessage())
            .status(AppointmentStatus.PENDING)
            .build();

        if (form.getServiceId()   != null) serviceRepository  .findById(form.getServiceId())  .ifPresent(apt::setService);
        if (form.getTherapistId() != null) therapistRepository.findById(form.getTherapistId()).ifPresent(apt::setTherapist);
        if (form.getBranchId()    != null) branchRepository   .findById(form.getBranchId())   .ifPresent(apt::setBranch);

        Appointment saved = appointmentRepository.save(apt);

        // Email to patient (if email provided)
        if (saved.getEmail() != null && !saved.getEmail().isBlank())
            emailService.sendBookingReceived(saved);

        // Email to clinic inbox
        emailService.notifyAdminNewBooking(saved);

        log.info("Appointment booked id={}", saved.getId());
        return saved;
    }

    /**
     * Update appointment status and send the appropriate email to the patient.
     * CONFIRMED → sends booking_confirmed template
     * CANCELLED  → sends booking_cancelled template
     */
    public void updateStatus(Long id, AppointmentStatus newStatus) {
        appointmentRepository.findById(id).ifPresent(apt -> {
            apt.setStatus(newStatus);
            appointmentRepository.save(apt);

            if (apt.getEmail() != null && !apt.getEmail().isBlank()) {
                if (newStatus == AppointmentStatus.CONFIRMED)
                    emailService.sendBookingConfirmed(apt);
                else if (newStatus == AppointmentStatus.CANCELLED)
                    emailService.sendBookingCancelled(apt);
            }
        });
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointments(int page, int size) {
        return appointmentRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAppointmentsByStatus(AppointmentStatus status, int page, int size) {
        return appointmentRepository.findByStatusOrderByCreatedAtDesc(status, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }
}
