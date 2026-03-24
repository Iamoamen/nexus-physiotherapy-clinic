package com.nexus.clinic.controller.admin;

import com.nexus.clinic.entity.BlogPost;
import com.nexus.clinic.repository.BlogPostRepository;
import com.nexus.clinic.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/blog")
@RequiredArgsConstructor
public class AdminBlogController {

    private final BlogPostRepository blogPostRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts",
            blogPostRepository.findAll(Sort.by("createdAt").descending()));
        model.addAttribute("newPost", new BlogPost());
        model.addAttribute("adminPageTitle", "Blog Posts");
        model.addAttribute("adminTitleKey", "admin.blog");
        return "admin/blog";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BlogPost post, RedirectAttributes attrs) {
        // Sanitize HTML content before saving — prevents stored XSS
        post.setContentEn(HtmlSanitizer.sanitize(post.getContentEn()));
        post.setContentAr(HtmlSanitizer.sanitize(post.getContentAr()));
        post.setSummaryEn(HtmlSanitizer.stripTags(post.getSummaryEn()));
        post.setSummaryAr(HtmlSanitizer.stripTags(post.getSummaryAr()));
        post.setTitleEn(HtmlSanitizer.stripTags(post.getTitleEn()));
        post.setTitleAr(HtmlSanitizer.stripTags(post.getTitleAr()));
        blogPostRepository.save(post);
        attrs.addFlashAttribute("msg", "Post saved.");
        return "redirect:/admin/blog";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        blogPostRepository.deleteById(id);
        attrs.addFlashAttribute("msg", "Blog post deleted.");
        return "redirect:/admin/blog";
    }
}
