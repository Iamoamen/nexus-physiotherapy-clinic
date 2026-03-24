package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.AppointmentStatus;
import com.nexus.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String status,
                       Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("appointments",
                appointmentService.getAppointmentsByStatus(
                    AppointmentStatus.valueOf(status.toUpperCase()), page, 15));
            model.addAttribute("filterStatus", status);
        } else {
            model.addAttribute("appointments", appointmentService.getAllAppointments(page, 15));
        }
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("adminPageTitle", "Appointments");
        model.addAttribute("adminTitleKey", "admin.appointments");
        return "admin/appointments";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes attrs) {
        appointmentService.updateStatus(id, AppointmentStatus.valueOf(status.toUpperCase()));
        attrs.addFlashAttribute("msg", "Appointment status updated.");
        return "redirect:/admin/appointments";
    }
}
