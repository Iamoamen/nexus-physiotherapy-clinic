package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.EmailTemplate;
import com.nexus.clinic.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/email-templates")
@RequiredArgsConstructor
public class AdminEmailTemplateController {

    private final EmailTemplateRepository repo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", repo.findAll());
        model.addAttribute("adminPageTitle", "Email Templates");
        model.addAttribute("adminTitleKey", "admin.email-templates");
        return "admin/email-templates";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        EmailTemplate tpl = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        model.addAttribute("tpl", tpl);
        model.addAttribute("adminPageTitle", "Edit Email Template");
        model.addAttribute("adminTitleKey", "admin.email-templates");
        return "admin/email-template-edit";
    }

    @PostMapping("/{id}/save")
    public String save(@PathVariable Long id,
                       @RequestParam String subjectEn,
                       @RequestParam String subjectAr,
                       @RequestParam String bodyEn,
                       @RequestParam String bodyAr,
                       @RequestParam(defaultValue = "false") boolean active,
                       RedirectAttributes attrs) {

        repo.findById(id).ifPresent(tpl -> {
            tpl.setSubjectEn(subjectEn);
            tpl.setSubjectAr(subjectAr);
            tpl.setBodyEn(bodyEn);
            tpl.setBodyAr(bodyAr);
            tpl.setActive(active);
            repo.save(tpl);
        });

        attrs.addFlashAttribute("msg", "Email template saved.");
        return "redirect:/admin/email-templates";
    }
}
