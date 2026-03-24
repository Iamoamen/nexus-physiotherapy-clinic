package com.nexus.clinic.util;

import com.nexus.clinic.entity.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * WhatsApp notification utilities.
 *
 * Two modes supported:
 *   1. wa.me deep link (no API key needed) — opens WhatsApp with pre-filled message
 *   2. WhatsApp Business API via CallMeBot (free tier, 3 msg/min)
 *
 * Configure in application.properties:
 *   app.whatsapp.mode=callmebot       (or "link")
 *   app.whatsapp.callmebot.apikey=    (from callmebot.com)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WhatsAppNotifier {

    @Value("${app.whatsapp.mode:link}")
    private String mode;

    @Value("${app.whatsapp.callmebot.apikey:}")
    private String callmebotApiKey;

    @Value("${app.clinic.whatsapp}")
    private String clinicWhatsapp;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Build a wa.me deep link with a pre-filled message.
     * Used in "Book Now" and "WhatsApp Us" buttons throughout the site.
     */
    public String buildBookingLink(String service) {
        String message = String.format(
            "Hello! I'd like to book an appointment for %s at Nexus Clinic.",
            service != null ? service : "physiotherapy"
        );
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "https://wa.me/" + clinicWhatsapp + "?text=" + encoded;
    }

    /**
     * Build a general enquiry WhatsApp link.
     */
    public String buildEnquiryLink() {
        String message = "Hello, I have a question about Nexus Clinic.";
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "https://wa.me/" + clinicWhatsapp + "?text=" + encoded;
    }

    /**
     * Send a WhatsApp notification via CallMeBot API (free tier).
     * Only fires if mode=callmebot and API key is configured.
     *
     * To set up:
     * 1. Send "I allow callmebot to send me messages" to +34 644 34 69 61
     * 2. You'll receive your API key
     * 3. Set app.whatsapp.callmebot.apikey=<your_key>
     */
    @Async
    public void sendAdminAlert(String message) {
        if (!"callmebot".equals(mode) || callmebotApiKey.isBlank()) {
            log.debug("WhatsApp API not configured — skipping notification");
            return;
        }
        try {
            String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String url = String.format(
                "https://api.callmebot.com/whatsapp.php?phone=%s&text=%s&apikey=%s",
                clinicWhatsapp, encoded, callmebotApiKey
            );
            restTemplate.getForObject(url, String.class);
            log.info("WhatsApp notification sent via CallMeBot");
        } catch (Exception e) {
            log.warn("WhatsApp notification failed: {}", e.getMessage());
        }
    }

    /**
     * Notify admin of a new appointment booking.
     */
    @Async
    public void notifyNewAppointment(Appointment apt) {
        String message = String.format(
            "🔔 New appointment at Nexus Clinic!\n" +
            "Patient: %s\n" +
            "Phone: %s\n" +
            "Date: %s at %s\n" +
            "Service: %s\n" +
            "Log in to confirm: nexusclinic.eg/admin",
            apt.getPatientName(),
            apt.getPhone(),
            apt.getPreferredDate(),
            apt.getPreferredTime(),
            apt.getService() != null ? apt.getService().getNameEn() : "General"
        );
        sendAdminAlert(message);
    }
}
