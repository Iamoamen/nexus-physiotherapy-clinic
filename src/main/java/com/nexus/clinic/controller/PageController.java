package com.nexus.clinic.controller;

import com.nexus.clinic.dto.ContactForm;
import com.nexus.clinic.entity.Condition;
import com.nexus.clinic.entity.PostStatus;
import com.nexus.clinic.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ServiceRepository     serviceRepository;
    private final ConditionRepository   conditionRepository;
    private final TherapistRepository   therapistRepository;
    private final TestimonialRepository testimonialRepository;
    private final BlogPostRepository    blogPostRepository;
    private final FAQRepository         faqRepository;
    private final BranchRepository      branchRepository;
    private final PatientInquiryRepository inquiryRepository;

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("therapists", therapistRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("pageTitle", "About Nexus Clinic | Our Story & Mission");
        model.addAttribute("metaDescription",
            "Learn about Nexus Clinic, our team of expert physiotherapists and our clinical approach: Clear. Focused. Individualized.");
        return "pages/about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("services", serviceRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("pageTitle", "Our Services | Nexus Physiotherapy Clinic");
        model.addAttribute("metaDescription",
            "Explore our range of physiotherapy and rehabilitation services in Heliopolis, Cairo.");
        return "pages/services";
    }

    @GetMapping("/services/{slug}")
    public String serviceDetail(@PathVariable String slug, Model model) {
        return serviceRepository.findBySlug(slug).map(service -> {
            model.addAttribute("service", service);
            model.addAttribute("pageTitle", service.getNameEn() + " | Nexus Clinic");
            return "pages/service-detail";
        }).orElse("redirect:/services");
    }

    @GetMapping("/conditions")
    public String conditions(Model model) {
        List<Condition> all = conditionRepository.findByActiveTrueOrderByBodyAreaAsc();
        Map<String, List<Condition>> grouped = new LinkedHashMap<>();
        for (Condition c : all) {
            grouped.computeIfAbsent(c.getBodyArea(), k -> new ArrayList<>()).add(c);
        }
        model.addAttribute("conditionsByArea", grouped);
        model.addAttribute("pageTitle", "Conditions We Treat | Nexus Clinic");
        return "pages/conditions";
    }

    @GetMapping("/team")
    public String team(Model model) {
        model.addAttribute("therapists", therapistRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("pageTitle", "Our Team | Nexus Physiotherapy");
        return "pages/team";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("posts", blogPostRepository.findByStatusOrderByPublishedDateDesc(
            PostStatus.PUBLISHED, PageRequest.of(0, 9)));
        model.addAttribute("pageTitle", "Health Tips & Blog | Nexus Clinic");
        return "pages/blog";
    }

    @GetMapping("/blog/{slug}")
    public String blogPost(@PathVariable String slug, Model model) {
        return blogPostRepository.findBySlug(slug).map(post -> {
            model.addAttribute("post", post);
            model.addAttribute("pageTitle", post.getTitleEn() + " | Nexus Clinic Blog");
            return "pages/blog-post";
        }).orElse("redirect:/blog");
    }

    @GetMapping("/testimonials")
    public String testimonials(Model model) {
        model.addAttribute("testimonials", testimonialRepository.findByApprovedTrueOrderByCreatedAtDesc(
            PageRequest.of(0, 20)));
        model.addAttribute("pageTitle", "Patient Success Stories | Nexus Clinic");
        return "pages/testimonials";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("faqs", faqRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("pageTitle", "Frequently Asked Questions | Nexus Clinic");
        return "pages/faq";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("branches", branchRepository.findByActiveTrueOrderByMainBranchDesc());
        model.addAttribute("contactForm", new ContactForm());
        model.addAttribute("pageTitle", "Contact Us | Nexus Clinic Heliopolis");
        return "pages/contact";
    }

    @PostMapping("/contact")
    public String submitContact(@Valid @ModelAttribute ContactForm form,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("branches", branchRepository.findByActiveTrueOrderByMainBranchDesc());
            return "pages/contact";
        }

        com.nexus.clinic.entity.PatientInquiry inquiry =
            com.nexus.clinic.entity.PatientInquiry.builder()
                .name(form.getName())
                .email(form.getEmail())
                .phone(form.getPhone())
                .subject(form.getSubject())
                .message(form.getMessage())
                .replied(false)
                .build();
        inquiryRepository.save(inquiry);

        redirectAttrs.addFlashAttribute("successMsg",
            "Your message has been sent! We'll get back to you within 24 hours.");
        return "redirect:/contact";
    }
}
