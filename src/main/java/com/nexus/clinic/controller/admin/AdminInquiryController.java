package com.nexus.clinic.controller.admin;

import com.nexus.clinic.repository.PatientInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final PatientInquiryRepository inquiryRepository;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("inquiries",
            inquiryRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 20)));
        model.addAttribute("adminPageTitle", "Inquiries");
        model.addAttribute("adminTitleKey", "admin.inquiries");
        return "admin/inquiries";
    }

    @PostMapping("/{id}/replied")
    public String markReplied(@PathVariable Long id, RedirectAttributes attrs) {
        inquiryRepository.findById(id).ifPresent(i -> {
            i.setReplied(true);
            inquiryRepository.save(i);
        });
        attrs.addFlashAttribute("msg", "Marked as replied.");
        return "redirect:/admin/inquiries";
    }
}
