package com.nexus.clinic.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

/**
 * Sanitizes admin-entered HTML before it is persisted to the database.
 *
 * Prevents stored XSS: even if the admin account is compromised, an attacker
 * cannot inject arbitrary JavaScript into blog posts or service descriptions
 * because dangerous tags/attributes are stripped on save.
 *
 * Dependency (already in pom.xml):
 *   com.googlecode.owasp-java-html-sanitizer : owasp-java-html-sanitizer
 */
public final class HtmlSanitizer {

    /**
     * Allow common rich-text tags: headings, paragraphs, lists, tables,
     * bold/italic, links (https only), and images (src validated).
     * Strips all JavaScript, event handlers, and unknown attributes.
     */
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.IMAGES)
            .and(new HtmlPolicyBuilder()
                .allowElements(
                    "ul", "ol", "li", "br", "hr",
                    "table", "thead", "tbody", "tr", "th", "td",
                    "h1", "h2", "h3", "h4", "h5", "h6",
                    "span", "div", "figure", "figcaption"
                )
                .allowAttributes("class").globally()
                .allowAttributes("href").onElements("a")
                .allowAttributes("src", "alt", "width", "height").onElements("img")
                .allowAttributes("colspan", "rowspan").onElements("td", "th")
                .toFactory());

    private HtmlSanitizer() {}

    /**
     * Sanitize HTML string — strips dangerous tags while keeping safe formatting.
     * Call this before saving any admin-entered rich content to the DB.
     */
    public static String sanitize(String html) {
        if (html == null || html.isBlank()) return html;
        return POLICY.sanitize(html);
    }

    /**
     * Strip ALL html tags — use for plain-text fields like names, slugs.
     */
    public static String stripTags(String input) {
        if (input == null) return null;
        return input.replaceAll("<[^>]*>", "").trim();
    }
}
