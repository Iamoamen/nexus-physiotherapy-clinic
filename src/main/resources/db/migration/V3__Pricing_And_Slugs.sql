-- V3__Pricing_And_Slugs.sql
-- Ensures all service slugs and additional seed data are consistent

-- Update navbar links in working database (idempotent)
UPDATE services SET slug = 'sports-rehabilitation'  WHERE name_en = 'Sports Rehabilitation'   AND (slug IS NULL OR slug = '');
UPDATE services SET slug = 'post-surgical-rehab'    WHERE name_en = 'Post-Surgical Rehab'     AND (slug IS NULL OR slug = '');
UPDATE services SET slug = 'manual-therapy'         WHERE name_en = 'Manual Therapy'          AND (slug IS NULL OR slug = '');
UPDATE services SET slug = 'dry-needling'           WHERE name_en = 'Dry Needling'            AND (slug IS NULL OR slug = '');
UPDATE services SET slug = 'chronic-pain-management' WHERE name_en = 'Chronic Pain Management' AND (slug IS NULL OR slug = '');
UPDATE services SET slug = 'performance-training'   WHERE name_en = 'Performance Training'    AND (slug IS NULL OR slug = '');

-- Add a sample blog post
INSERT INTO blog_posts (title_en, title_ar, slug, summary_en, summary_ar, author_name, category, status, published_date)
VALUES (
  '5 Exercises to Relieve Knee Pain at Home',
  '٥ تمارين لتخفيف ألم الركبة في المنزل',
  '5-exercises-to-relieve-knee-pain',
  'Knee pain is one of the most common complaints we see at Nexus Clinic. Here are 5 physiotherapist-approved exercises you can start doing today.',
  'ألم الركبة من أكثر الشكاوى شيوعاً في نيكسوس كلينيك. إليك ٥ تمارين معتمدة من معالجينا يمكنك البدء بها اليوم.',
  'Dr. Ahmed Hassan',
  'Rehabilitation',
  'PUBLISHED',
  CURDATE()
) ON DUPLICATE KEY UPDATE title_en = title_en;

-- Add another blog post
INSERT INTO blog_posts (title_en, title_ar, slug, summary_en, summary_ar, author_name, category, status, published_date)
VALUES (
  'Understanding Shoulder Impingement: Causes & Treatment',
  'فهم متلازمة التضيق الكتفي: الأسباب والعلاج',
  'understanding-shoulder-impingement',
  'Shoulder impingement affects thousands of Egyptians each year. Learn what causes it and how physiotherapy can help you recover without surgery.',
  'التضيق الكتفي يؤثر على آلاف المصريين سنوياً. تعرف على أسبابه وكيف يساعدك العلاج الطبيعي على التعافي دون جراحة.',
  'Dr. Sara Khalil',
  'Sports Medicine',
  'PUBLISHED',
  DATE_SUB(CURDATE(), INTERVAL 7 DAY)
) ON DUPLICATE KEY UPDATE title_en = title_en;

-- Add a third blog post (draft)
INSERT INTO blog_posts (title_en, title_ar, slug, summary_en, summary_ar, author_name, category, status)
VALUES (
  'Desk Workers: 7 Posture Fixes You Need Today',
  'العمل على المكتب: ٧ إصلاحات للوضعية تحتاجها اليوم',
  'desk-workers-posture-fixes',
  'Spending long hours at a desk can wreak havoc on your posture and spine. Our physiotherapists share the top 7 corrections you can make immediately.',
  'قضاء ساعات طويلة أمام المكتب يمكن أن يضر بوضعيتك وعمودك الفقري. معالجونا يشاركون أبرز ٧ تصحيحات يمكنك تطبيقها فوراً.',
  'Dr. Omar Youssef',
  'Prevention',
  'DRAFT'
) ON DUPLICATE KEY UPDATE title_en = title_en;
