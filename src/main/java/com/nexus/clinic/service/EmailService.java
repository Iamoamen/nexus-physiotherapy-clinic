package com.nexus.clinic.service;

import com.nexus.clinic.entity.Appointment;
import com.nexus.clinic.entity.EmailTemplate;
import com.nexus.clinic.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender            mailSender;
    private final EmailTemplateRepository   templateRepo;

    @Value("${app.clinic.email}") private String clinicEmail;

    // ── Public API ────────────────────────────────────────────

    /** Sent to patient immediately after they submit a booking. */
    @Async
    public void sendBookingReceived(Appointment apt) {
        send("booking_confirmation", apt.getEmail(), apt, detectLocale(apt));
    }

    /** Sent to the clinic inbox when a new booking arrives. */
    @Async
    public void notifyAdminNewBooking(Appointment apt) {
        send("admin_new_booking", clinicEmail, apt, Locale.ENGLISH);
    }

    /** Sent to patient when admin changes status to CONFIRMED. */
    @Async
    public void sendBookingConfirmed(Appointment apt) {
        send("booking_confirmed", apt.getEmail(), apt, detectLocale(apt));
    }

    /** Sent to patient when admin changes status to CANCELLED. */
    @Async
    public void sendBookingCancelled(Appointment apt) {
        send("booking_cancelled", apt.getEmail(), apt, detectLocale(apt));
    }

    // ── Core send ─────────────────────────────────────────────

    private void send(String code, String to, Appointment apt, Locale locale) {
        if (to == null || to.isBlank()) return;

        Optional<EmailTemplate> opt = templateRepo.findByCode(code);
        if (opt.isEmpty()) {
            log.warn("Email template not found: {}", code);
            return;
        }

        EmailTemplate tpl = opt.get();
        if (!tpl.isActive()) return;

        boolean ar = Locale.forLanguageTag("ar").getLanguage()
                           .equals(locale.getLanguage());

        String subject = fill(ar ? tpl.getSubjectAr() : tpl.getSubjectEn(), apt);
        String body    = fill(ar ? tpl.getBodyAr()    : tpl.getBodyEn(),    apt);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setFrom(clinicEmail);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("Email sent: code={} to={}", code, to);
        } catch (Exception e) {
            log.warn("Email failed: code={} error={}", code, e.getMessage());
        }
    }

    // ── Placeholder substitution ──────────────────────────────

    /**
     * Replaces {{name}}, {{date}}, {{time}}, {{service}},
     * {{phone}}, {{email}}, {{message}} in any template string.
     */
    private String fill(String template, Appointment apt) {
        if (template == null) return "";

        String service = apt.getService() != null ? apt.getService().getNameEn() : "—";
        String date    = apt.getPreferredDate() != null ? apt.getPreferredDate().toString() : "—";
        String time    = apt.getPreferredTime() != null ? apt.getPreferredTime() : "—";

        return template
            .replace("{{name}}",    safe(apt.getPatientName()))
            .replace("{{date}}",    date)
            .replace("{{time}}",    time)
            .replace("{{service}}", service)
            .replace("{{phone}}",   safe(apt.getPhone()))
            .replace("{{email}}",   safe(apt.getEmail()))
            .replace("{{message}}", apt.getMessage() != null ? apt.getMessage() : "—");
    }

    private String safe(String s) { return s != null ? s : ""; }

    /**
     * Detect locale from appointment — Arabic names suggest Arabic locale.
     * You can extend this by storing a preferred language on the Appointment.
     */
    private Locale detectLocale(Appointment apt) {
        // If patient name contains Arabic chars → use Arabic template
        if (apt.getPatientName() != null &&
            apt.getPatientName().chars().anyMatch(c -> Character.UnicodeBlock.of(c)
                == Character.UnicodeBlock.ARABIC)) {
            return Locale.forLanguageTag("ar");
        }
        return Locale.ENGLISH;
    }
}
