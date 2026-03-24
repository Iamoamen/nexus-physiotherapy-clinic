package com.nexus.clinic.config;

import com.nexus.clinic.repository.AppointmentRepository;
import com.nexus.clinic.repository.PatientInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class AdminInterceptorConfig implements WebMvcConfigurer {

    private final AppointmentRepository    appointmentRepository;
    private final PatientInquiryRepository inquiryRepository;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new AdminSidebarInterceptor(appointmentRepository, inquiryRepository))
            .addPathPatterns("/admin/**");
    }
}
