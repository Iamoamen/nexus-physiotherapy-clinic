package com.nexus.clinic.dto;

import lombok.*;

@Data @Builder
public class DashboardStats {
    private long totalAppointments;
    private long pendingAppointments;
    private long confirmedAppointments;
    private long thisMonthAppointments;
    private long totalTestimonials;
    private long pendingTestimonials;
    private long totalBlogPosts;
    private long unpublishedPosts;
    private long totalInquiries;
    private long unreadInquiries;
}
