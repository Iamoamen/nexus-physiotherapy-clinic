package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.Therapist;
import com.nexus.clinic.repository.TherapistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/team")
@RequiredArgsConstructor
public class AdminTherapistController {

    private final TherapistRepository therapistRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("therapists", therapistRepository.findAll());
        model.addAttribute("newTherapist", new Therapist());
        model.addAttribute("adminPageTitle", "Team");
        model.addAttribute("adminTitleKey", "admin.team");
        return "admin/team";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Therapist therapist, RedirectAttributes attrs) {
        therapistRepository.save(therapist);
        attrs.addFlashAttribute("msg", "Therapist saved.");
        return "redirect:/admin/team";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        therapistRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "Therapist removed.");
        return "redirect:/admin/team";
    }
}
