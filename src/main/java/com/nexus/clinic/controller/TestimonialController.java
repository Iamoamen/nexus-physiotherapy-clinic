package com.nexus.clinic.controller;

import com.nexus.clinic.entity.Testimonial;
import com.nexus.clinic.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialRepository testimonialRepository;

    @PostMapping("/testimonials/submit")
    public String submit(@RequestParam String patientName,
                         @RequestParam(required = false) String condition,
                         @RequestParam String contentEn,
                         @RequestParam(defaultValue = "5") int rating,
                         RedirectAttributes attrs) {

        Testimonial t = Testimonial.builder()
                .patientName(patientName)
                .condition(condition)
                .contentEn(contentEn)
                .rating(Math.min(5, Math.max(1, rating)))
                .approved(false)
                .featured(false)
                .build();

        testimonialRepository.save(t);
        attrs.addFlashAttribute("reviewSubmitted", true);
        return "redirect:/testimonials";
    }
}
