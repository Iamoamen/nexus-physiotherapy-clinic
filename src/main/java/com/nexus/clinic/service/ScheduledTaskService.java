package com.nexus.clinic.service;

import com.nexus.clinic.entity.Appointment;
import com.nexus.clinic.entity.AppointmentStatus;
import com.nexus.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final AppointmentRepository appointmentRepository;
    private final JavaMailSender         mailSender;

    @Value("${app.clinic.email}")
    private String clinicEmail;

    /** Daily 9:00 AM — email confirmed patients with tomorrow's appointment */
    @Scheduled(cron = "0 0 9 * * *", zone = "Africa/Cairo")
    @Transactional
    public void sendAppointmentReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        log.info("Sending reminders for {}", tomorrow);

        // Fetch all confirmed appointments for tomorrow
        appointmentRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 200))
            .stream()
            .filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED)
            .filter(a -> tomorrow.equals(a.getPreferredDate()))
            .filter(a -> a.getEmail() != null && !a.getEmail().isBlank())
            .forEach(this::sendReminderEmail);
    }

    /** Daily 11:00 PM — mark past confirmed appointments as NO_SHOW */
    @Scheduled(cron = "0 0 23 * * *", zone = "Africa/Cairo")
    @Transactional
    public void markNoShows() {
        LocalDate today = LocalDate.now();
        appointmentRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 500))
            .stream()
            .filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED)
            .filter(a -> a.getPreferredDate() != null && a.getPreferredDate().isBefore(today))
            .forEach(a -> {
                a.setStatus(AppointmentStatus.NO_SHOW);
                appointmentRepository.save(a);
            });
    }

    /** Every Monday 8:00 AM — weekly admin digest email */
    @Scheduled(cron = "0 0 8 * * MON", zone = "Africa/Cairo")
    public void sendWeeklyAdminDigest() {
        try {
            long pending   = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
            long thisWeek  = appointmentRepository.countSince(LocalDateTime.now().minusDays(7));

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(clinicEmail);
            msg.setSubject("Weekly Nexus Clinic Digest");
            msg.setText(
                "Weekly Summary — Nexus Clinic\n" +
                "Pending appointments : " + pending  + "\n" +
                "New this week        : " + thisWeek + "\n\n" +
                "Dashboard: https://nexusclinic.eg/admin\n\n" +
                "#MoveBeyond"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Weekly digest failed: {}", e.getMessage());
        }
    }

    private void sendReminderEmail(Appointment apt) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(apt.getEmail());
            msg.setFrom(clinicEmail);
            msg.setSubject("Appointment Reminder – Nexus Clinic Tomorrow");
            msg.setText(
                "Dear " + apt.getPatientName() + ",\n\n" +
                "Reminder: you have an appointment at Nexus Clinic tomorrow.\n" +
                "Date    : " + apt.getPreferredDate() + "\n" +
                "Time    : " + apt.getPreferredTime() + "\n" +
                "Service : " + (apt.getService() != null ? apt.getService().getNameEn() : "Physiotherapy") + "\n\n" +
                "Location: Heliopolis, Cairo\n" +
                "Phone   : +20103-5411305\n\n" +
                "Nexus Clinic Team\n#MoveBeyond"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Reminder email failed for {}: {}", apt.getEmail(), e.getMessage());
        }
    }
}
