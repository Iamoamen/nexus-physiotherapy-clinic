package com.nexus.clinic.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intercepts every request and injects the current request URI
 * into the model as {@code currentUri}.
 *
 * This is the Thymeleaf 3.1-compatible replacement for the removed
 * {@code #httpServletRequest} and {@code #request} expression objects.
 *
 * Usage in templates:
 *   th:classappend="${currentUri == '/'} ? 'active'"
 *   th:classappend="${currentUri.startsWith('/about')} ? 'active'"
 *   th:href="${'https://nexusclinic.eg' + currentUri}"
 */
public class RequestUriInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("currentUri", request.getRequestURI());
        }
    }
}
