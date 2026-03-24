package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.FAQ;
import com.nexus.clinic.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class AdminFAQController {

    private final FAQRepository faqRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("faqs",
            faqRepository.findAll(Sort.by("sortOrder").ascending()));
        model.addAttribute("adminPageTitle", "FAQs");
        model.addAttribute("adminTitleKey", "admin.faqs");
        return "admin/faqs";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute FAQ faq, RedirectAttributes attrs) {
        faqRepository.save(faq);
        attrs.addFlashAttribute("msg", "FAQ saved successfully.");
        return "redirect:/admin/faqs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        faqRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "FAQ deleted.");
        return "redirect:/admin/faqs";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        faqRepository.findById(id).ifPresent(f -> {
            f.setActive(!f.isActive());
            faqRepository.save(f);
        });
        return "redirect:/admin/faqs";
    }
}
