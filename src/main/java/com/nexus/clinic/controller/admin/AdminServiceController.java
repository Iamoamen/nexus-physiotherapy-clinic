package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.Service;
import com.nexus.clinic.repository.ServiceRepository;
import com.nexus.clinic.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("newService", new Service());
        model.addAttribute("adminPageTitle", "Services");
        model.addAttribute("adminTitleKey", "admin.services");
        return "admin/services";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Service service, RedirectAttributes attrs) {
        // Sanitize HTML descriptions — rendered via th:utext in service-detail page
        service.setDescriptionEn(HtmlSanitizer.sanitize(service.getDescriptionEn()));
        service.setDescriptionAr(HtmlSanitizer.sanitize(service.getDescriptionAr()));
        service.setNameEn(HtmlSanitizer.stripTags(service.getNameEn()));
        service.setNameAr(HtmlSanitizer.stripTags(service.getNameAr()));
        serviceRepository.save(service);
        attrs.addFlashAttribute("msg", "Service saved.");
        return "redirect:/admin/services";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        serviceRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "Service deleted.");
        return "redirect:/admin/services";
    }
}
