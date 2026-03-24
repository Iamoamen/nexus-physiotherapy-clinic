package com.nexus.clinic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("pages/login");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(new RequestUriInterceptor());
    }
}
