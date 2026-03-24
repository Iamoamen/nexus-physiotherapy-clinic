package com.nexus.clinic.controller.admin;

import com.nexus.clinic.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/testimonials")
@RequiredArgsConstructor
public class AdminTestimonialController {

    private final TestimonialRepository testimonialRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("testimonials",
            testimonialRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 50)));
        model.addAttribute("adminPageTitle", "Testimonials");
        model.addAttribute("adminTitleKey", "admin.testimonials");
        return "admin/testimonials";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes attrs) {
        testimonialRepository.findById(id).ifPresent(t -> {
            t.setApproved(true);
            testimonialRepository.save(t);
        });
        attrs.addFlashAttribute("msg", "Testimonial approved.");
        return "redirect:/admin/testimonials";
    }

    @PostMapping("/{id}/feature")
    public String feature(@PathVariable Long id, RedirectAttributes attrs) {
        testimonialRepository.findById(id).ifPresent(t -> {
            t.setFeatured(!t.isFeatured());
            testimonialRepository.save(t);
        });
        return "redirect:/admin/testimonials";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        testimonialRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "Testimonial deleted.");
        return "redirect:/admin/testimonials";
    }
}
