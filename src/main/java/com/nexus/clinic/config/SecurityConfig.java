package com.nexus.clinic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.admin.default-password}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ── Authorisation ─────────────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )

            // ── CSRF ──────────────────────────────────────────────────────
            // Only disabled for the health-check GET — everything else is protected.
            // The public booking form uses Thymeleaf which includes the CSRF token automatically.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/v1/health")
            )

            // ── Login ─────────────────────────────────────────────────────
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // ── Logout ────────────────────────────────────────────────────
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // ── Session ───────────────────────────────────────────────────
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()   // rotate session ID on login
                .maximumSessions(1)                    // only one admin session at a time
            )

            // ── Security headers ──────────────────────────────────────────
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(cto -> {})
                .xssProtection(xss -> {})
                .referrerPolicy(ref ->
                    ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000))
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' cdn.jsdelivr.net cdnjs.cloudflare.com unpkg.com; " +
                    "style-src 'self' 'unsafe-inline' cdn.jsdelivr.net cdnjs.cloudflare.com fonts.googleapis.com; " +
                    "font-src 'self' fonts.gstatic.com cdnjs.cloudflare.com; " +
                    "img-src 'self' data: https:; " +
                    "frame-src 'none'; " +
                    "object-src 'none';"
                ))
            );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode(adminPassword))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Required for maximumSessions(1) to work correctly. */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
