package com.nexus.clinic.repository;

import com.nexus.clinic.entity.BlogPost;
import com.nexus.clinic.entity.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByStatusOrderByPublishedDateDesc(PostStatus status, Pageable pageable);
    Optional<BlogPost> findBySlug(String slug);
    List<BlogPost> findTop3ByStatusOrderByPublishedDateDesc(PostStatus status);
}
