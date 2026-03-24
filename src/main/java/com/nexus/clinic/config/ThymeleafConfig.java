package com.nexus.clinic.config;

import com.nexus.clinic.util.WhatsAppNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Exposes utility beans to Thymeleaf templates.
 *
 * Usage in templates:
 *   th:href="${@whatsAppNotifier.buildBookingLink('Sports Rehab')}"
 *   th:href="${@whatsAppNotifier.buildEnquiryLink()}"
 */
@Configuration
public class ThymeleafConfig {
    // WhatsAppNotifier is already a @Component, so it's available as @whatsAppNotifier
    // in Thymeleaf Spring EL expressions. No extra bean needed.
    // This class is a placeholder for any future Thymeleaf config additions.
}
