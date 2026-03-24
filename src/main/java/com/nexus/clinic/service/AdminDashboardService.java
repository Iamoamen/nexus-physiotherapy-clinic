package com.nexus.clinic.service;

import com.nexus.clinic.dto.DashboardStats;
import com.nexus.clinic.entity.*;
import com.nexus.clinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final AppointmentRepository    appointmentRepository;
    private final TestimonialRepository    testimonialRepository;
    private final BlogPostRepository       blogPostRepository;
    private final PatientInquiryRepository inquiryRepository;

    public DashboardStats getStats() {
        LocalDateTime monthStart = LocalDateTime.now()
            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        long pendingT = testimonialRepository
            .findAllByOrderByCreatedAtDesc(Pageable.unpaged())
            .stream().filter(t -> !t.isApproved()).count();

        long draftPosts = blogPostRepository.findAll()
            .stream().filter(p -> p.getStatus() == PostStatus.DRAFT).count();

        return DashboardStats.builder()
            .totalAppointments(appointmentRepository.count())
            .pendingAppointments(appointmentRepository.countByStatus(AppointmentStatus.PENDING))
            .confirmedAppointments(appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED))
            .thisMonthAppointments(appointmentRepository.countSince(monthStart))
            .totalTestimonials(testimonialRepository.count())
            .pendingTestimonials(pendingT)
            .totalBlogPosts(blogPostRepository.count())
            .unpublishedPosts(draftPosts)
            .totalInquiries(inquiryRepository.count())
            .unreadInquiries(inquiryRepository.countByRepliedFalse())
            .build();
    }
}
