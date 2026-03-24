package com.nexus.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SeoController {

    private static final String BASE = "https://nexusclinic.eg";

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    @ResponseBody
    public String sitemap() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
               url("/",            "weekly",  "1.0") +
               url("/about",       "monthly", "0.8") +
               url("/services",    "weekly",  "0.9") +
               url("/conditions",  "monthly", "0.8") +
               url("/team",        "monthly", "0.7") +
               url("/pricing",     "monthly", "0.7") +
               url("/testimonials","weekly",  "0.7") +
               url("/blog",        "weekly",  "0.8") +
               url("/faq",         "monthly", "0.6") +
               url("/contact",     "monthly", "0.8") +
               url("/book",        "monthly", "0.9") +
               "</urlset>";
    }

    @GetMapping(value = "/robots.txt", produces = "text/plain")
    @ResponseBody
    public String robots() {
        return "User-agent: *\n" +
               "Allow: /\n" +
               "Disallow: /admin/\n" +
               "Disallow: /login\n" +
               "Disallow: /api/admin/\n\n" +
               "Sitemap: " + BASE + "/sitemap.xml\n";
    }

    private String url(String path, String freq, String priority) {
        return "  <url><loc>" + BASE + path + "</loc>" +
               "<changefreq>" + freq + "</changefreq>" +
               "<priority>" + priority + "</priority></url>\n";
    }
}
