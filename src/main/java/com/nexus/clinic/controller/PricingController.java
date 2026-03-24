package com.nexus.clinic.controller;

import com.nexus.clinic.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PricingController {

    private final BranchRepository branchRepository;

    @GetMapping("/pricing")
    public String pricing(Model model) {
        model.addAttribute("branches", branchRepository.findByActiveTrueOrderByMainBranchDesc());
        model.addAttribute("pageTitle", "Pricing & Packages | Nexus Clinic");
        model.addAttribute("metaDescription",
            "Transparent physiotherapy pricing in Heliopolis, Cairo. Individual sessions and multi-session packages.");
        return "pages/pricing";
    }
}
