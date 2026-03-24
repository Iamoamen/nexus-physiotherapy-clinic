package com.nexus.clinic.controller;

import com.nexus.clinic.dto.AppointmentForm;
import com.nexus.clinic.repository.*;
import com.nexus.clinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ServiceRepository  serviceRepository;
    private final TherapistRepository therapistRepository;
    private final BranchRepository   branchRepository;

    @GetMapping
    public String bookingPage(Model model) {
        model.addAttribute("appointmentForm", new AppointmentForm());
        populateFormModel(model);
        model.addAttribute("pageTitle", "Book Appointment | Nexus Clinic");
        return "pages/book-appointment";
    }

    @PostMapping
    public String submitBooking(@Valid @ModelAttribute AppointmentForm form,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            populateFormModel(model);
            return "pages/book-appointment";
        }
        appointmentService.bookAppointment(form);
        redirectAttrs.addFlashAttribute("bookingSuccess", true);
        return "redirect:/book/success";
    }

    @GetMapping("/success")
    public String bookingSuccess() {
        return "pages/booking-success";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("services",   serviceRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("therapists", therapistRepository.findByActiveTrueOrderBySortOrderAsc());
        model.addAttribute("branches",   branchRepository.findByActiveTrueOrderByMainBranchDesc());
        model.addAttribute("timeSlots",  List.of(
            "8:00 AM","9:00 AM","10:00 AM","11:00 AM",
            "12:00 PM","1:00 PM","2:00 PM","3:00 PM",
            "4:00 PM","5:00 PM","6:00 PM","7:00 PM"
        ));
    }
}
