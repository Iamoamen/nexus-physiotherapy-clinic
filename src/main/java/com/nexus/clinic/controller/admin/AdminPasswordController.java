package com.nexus.clinic.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/admin/password")
@RequiredArgsConstructor
@Slf4j
public class AdminPasswordController {

    private final PasswordEncoder passwordEncoder;
    private final InMemoryUserDetailsManager userDetailsManager;
    private final MessageSource messageSource;


    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("adminTitleKey", "admin.password.change");
        model.addAttribute("adminPageTitle", "Change Password");
        return "admin/password";
    }

    @PostMapping
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttrs,
            Locale locale) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Validate current password
        UserDetails user = userDetailsManager.loadUserByUsername(username);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttrs.addFlashAttribute("errorMsg",
                messageSource.getMessage("admin.password.error.current-wrong", null, locale));
            return "redirect:/admin/password";
        }

        // Validate length
        if (newPassword.length() < 8) {
            redirectAttrs.addFlashAttribute("errorMsg",
                messageSource.getMessage("admin.password.error.too-short", null, locale));
            return "redirect:/admin/password";
        }

        // Validate mismatch
        if (!newPassword.equals(confirmPassword)) {
            redirectAttrs.addFlashAttribute("errorMsg",
                messageSource.getMessage("admin.password.error.mismatch", null, locale));
            return "redirect:/admin/password";
        }

        // Validate not same as current
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            redirectAttrs.addFlashAttribute("errorMsg",
                messageSource.getMessage("admin.password.error.same", null, locale));
            return "redirect:/admin/password";
        }

        // Update password
        UserDetails updated = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(passwordEncoder.encode(newPassword))
                .roles("ADMIN")
                .build();
        userDetailsManager.updateUser(updated);

        // Re-authenticate with new password
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(updated, null, updated.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        log.info("Admin password changed for user: {}", username);

        redirectAttrs.addFlashAttribute("msg",
            messageSource.getMessage("admin.password.success", null, locale));
        return "redirect:/admin/password";
    }
}
