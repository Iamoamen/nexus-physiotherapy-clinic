package com.nexus.clinic.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller — maps HTTP error codes to Thymeleaf error pages.
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message    = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("errorMessage", message);
        model.addAttribute("pageTitle", "Error – Nexus Clinic");

        if (statusCode != null) {
            int code = Integer.parseInt(statusCode.toString());
            model.addAttribute("statusCode", code);

            if (code == HttpStatus.NOT_FOUND.value())       return "error/404";
            if (code == HttpStatus.FORBIDDEN.value())       return "error/403";
            if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) return "error/500";
        }
        return "error/500";
    }
}
