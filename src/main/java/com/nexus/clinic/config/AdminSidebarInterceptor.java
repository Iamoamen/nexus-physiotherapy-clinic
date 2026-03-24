package com.nexus.clinic.config;

import com.nexus.clinic.entity.AppointmentStatus;
import com.nexus.clinic.repository.AppointmentRepository;
import com.nexus.clinic.repository.PatientInquiryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class AdminSidebarInterceptor implements HandlerInterceptor {

    private final AppointmentRepository    appointmentRepository;
    private final PatientInquiryRepository inquiryRepository;

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView == null || modelAndView.getViewName() == null) return;
        if (!modelAndView.getViewName().startsWith("admin/"))          return;

        try {
            modelAndView.addObject("pendingCount",
                appointmentRepository.countByStatus(AppointmentStatus.PENDING));
            modelAndView.addObject("unreadInquiries",
                inquiryRepository.countByRepliedFalse());
        } catch (Exception ignored) {
            // Don't break the page if counts fail
        }
    }
}
