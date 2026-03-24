package com.nexus.clinic.controller.admin;

import com.nexus.clinic.repository.AppointmentRepository;
import com.nexus.clinic.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final AppointmentRepository appointmentRepository;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("recentAppointments",
            appointmentRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10)));
        model.addAttribute("adminPageTitle", "Dashboard");
        model.addAttribute("adminTitleKey", "admin.dashboard");
        return "admin/dashboard";
    }
}
