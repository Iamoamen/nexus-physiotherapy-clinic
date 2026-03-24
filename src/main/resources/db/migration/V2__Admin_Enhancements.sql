-- V2__Admin_Enhancements.sql
-- Adds any missing indexes and admin-needed adjustments

-- Index on appointments for fast status filtering
ALTER TABLE appointments ADD INDEX idx_status (status);
ALTER TABLE appointments ADD INDEX idx_created_at (created_at);
ALTER TABLE appointments ADD INDEX idx_preferred_date (preferred_date);

-- Index on blog posts for slug lookups
ALTER TABLE blog_posts ADD INDEX idx_slug (slug(191));
ALTER TABLE blog_posts ADD INDEX idx_status_date (status, published_date);

-- Index on services for slug
ALTER TABLE services ADD INDEX idx_services_slug (slug(100));

-- Index on conditions for body area filtering
ALTER TABLE conditions ADD INDEX idx_body_area (body_area);

-- Index on testimonials for approved+featured
ALTER TABLE testimonials ADD INDEX idx_approved_featured (approved, featured);
