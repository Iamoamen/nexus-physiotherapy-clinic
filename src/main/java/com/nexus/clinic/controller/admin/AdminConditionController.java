package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.Condition;
import com.nexus.clinic.repository.ConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/conditions")
@RequiredArgsConstructor
public class AdminConditionController {

    private final ConditionRepository conditionRepository;

    @GetMapping
    public String list(Model model) {
        List<Condition> all = conditionRepository.findAll(Sort.by("bodyArea", "nameEn"));
        Map<String, List<Condition>> grouped = new LinkedHashMap<>();
        for (Condition c : all) {
            grouped.computeIfAbsent(c.getBodyArea(), k -> new ArrayList<>()).add(c);
        }
        model.addAttribute("conditionsByArea", grouped);
        model.addAttribute("adminPageTitle", "Conditions");
        model.addAttribute("adminTitleKey", "admin.conditions");
        return "admin/conditions";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Condition condition, RedirectAttributes attrs) {
        conditionRepository.save(condition);
        attrs.addFlashAttribute("msg", "Condition saved.");
        return "redirect:/admin/conditions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        conditionRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "Condition deleted.");
        return "redirect:/admin/conditions";
    }
}
