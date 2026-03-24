package com.nexus.clinic.controller;

import com.nexus.clinic.dto.AppointmentForm;
import com.nexus.clinic.entity.Condition;
import com.nexus.clinic.entity.PostStatus;
import com.nexus.clinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ServiceRepository     serviceRepository;
    private final ConditionRepository   conditionRepository;
    private final TherapistRepository   therapistRepository;
    private final TestimonialRepository testimonialRepository;
    private final BlogPostRepository    blogPostRepository;
    private final FAQRepository         faqRepository;
    private final BranchRepository      branchRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("services",     serviceRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("conditions",   conditionRepository.findByActiveTrueOrderByBodyAreaAsc());
        model.addAttribute("therapists",   therapistRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("testimonials", testimonialRepository.findByApprovedTrueAndFeaturedTrueOrderByCreatedAtDesc());
        model.addAttribute("recentPosts",  blogPostRepository.findTop3ByStatusOrderByPublishedDateDesc(PostStatus.PUBLISHED));
        model.addAttribute("faqs",         faqRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("branches",     branchRepository.findByActiveTrueOrderByMainBranchDesc());
        model.addAttribute("appointmentForm", new AppointmentForm());
        model.addAttribute("pageTitle", "Nexus Clinic – #MoveBeyond | Physiotherapy in Heliopolis");
        model.addAttribute("metaDescription",
            "Nexus Clinic offers expert physiotherapy, sports rehab & pain management in Heliopolis, Cairo. Book your appointment today.");
        return "pages/home";
    }
}
