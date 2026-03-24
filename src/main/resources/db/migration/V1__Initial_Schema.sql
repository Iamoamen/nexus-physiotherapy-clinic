-- ============================================================
-- V1__Initial_Schema.sql – Nexus Clinic Database
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ---- Branches ----
CREATE TABLE branches (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_en     VARCHAR(100) NOT NULL,
    name_ar     VARCHAR(100) NOT NULL,
    address_en  VARCHAR(255),
    address_ar  VARCHAR(255),
    phone       VARCHAR(20),
    whatsapp    VARCHAR(20),
    google_maps_embed TEXT,
    latitude    VARCHAR(30),
    longitude   VARCHAR(30),
    active      BOOLEAN DEFAULT TRUE,
    main_branch BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Working Hours ----
CREATE TABLE working_hours (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week  VARCHAR(20) NOT NULL,
    open_time    VARCHAR(10),
    close_time   VARCHAR(10),
    closed       BOOLEAN DEFAULT FALSE,
    branch_id    BIGINT,
    CONSTRAINT fk_wh_branch FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Services ----
CREATE TABLE services (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_en         VARCHAR(150) NOT NULL,
    name_ar         VARCHAR(150) NOT NULL,
    description_en  TEXT,
    description_ar  TEXT,
    icon_class      VARCHAR(100),
    image_url       VARCHAR(255),
    slug            VARCHAR(150) UNIQUE,
    sort_order      INT DEFAULT 0,
    active          BOOLEAN DEFAULT TRUE,
    price_from      DECIMAL(10,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Conditions ----
CREATE TABLE conditions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_en         VARCHAR(150) NOT NULL,
    name_ar         VARCHAR(150) NOT NULL,
    description_en  TEXT,
    description_ar  TEXT,
    body_area       VARCHAR(50),
    icon_class      VARCHAR(100),
    slug            VARCHAR(150) UNIQUE,
    active          BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Therapists ----
CREATE TABLE therapists (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_en             VARCHAR(100) NOT NULL,
    name_ar             VARCHAR(100) NOT NULL,
    title_en            VARCHAR(150),
    title_ar            VARCHAR(150),
    specialization      VARCHAR(200),
    photo_url           VARCHAR(255),
    bio_en              TEXT,
    bio_ar              TEXT,
    instagram_url       VARCHAR(255),
    linkedin_url        VARCHAR(255),
    years_experience    INT,
    active              BOOLEAN DEFAULT TRUE,
    sort_order          INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Appointments ----
CREATE TABLE appointments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_name    VARCHAR(100) NOT NULL,
    phone           VARCHAR(20) NOT NULL,
    email           VARCHAR(150),
    service_id      BIGINT,
    therapist_id    BIGINT,
    branch_id       BIGINT,
    preferred_date  DATE,
    preferred_time  VARCHAR(20),
    message         TEXT,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_apt_service   FOREIGN KEY (service_id)   REFERENCES services(id)   ON DELETE SET NULL,
    CONSTRAINT fk_apt_therapist FOREIGN KEY (therapist_id) REFERENCES therapists(id) ON DELETE SET NULL,
    CONSTRAINT fk_apt_branch    FOREIGN KEY (branch_id)    REFERENCES branches(id)   ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Testimonials ----
CREATE TABLE testimonials (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_name    VARCHAR(100),
    patient_name_ar VARCHAR(100),
    content_en      TEXT,
    content_ar      TEXT,
    rating          INT DEFAULT 5,
    condition_name  VARCHAR(150),
    approved        BOOLEAN DEFAULT FALSE,
    featured        BOOLEAN DEFAULT FALSE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Blog Posts ----
CREATE TABLE blog_posts (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_en        VARCHAR(255),
    title_ar        VARCHAR(255),
    slug            VARCHAR(255) UNIQUE,
    summary_en      TEXT,
    summary_ar      TEXT,
    content_en      LONGTEXT,
    content_ar      LONGTEXT,
    cover_image_url VARCHAR(255),
    author_name     VARCHAR(100),
    category        VARCHAR(100),
    status          VARCHAR(20) DEFAULT 'DRAFT',
    published_date  DATE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- FAQs ----
CREATE TABLE faqs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_en VARCHAR(500),
    question_ar VARCHAR(500),
    answer_en   TEXT,
    answer_ar   TEXT,
    category    VARCHAR(100),
    sort_order  INT DEFAULT 0,
    active      BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Patient Inquiries ----
CREATE TABLE patient_inquiries (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100),
    email       VARCHAR(150),
    phone       VARCHAR(20),
    subject     VARCHAR(255),
    message     TEXT,
    replied     BOOLEAN DEFAULT FALSE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- SEED DATA
-- ============================================================

-- Branch
INSERT INTO branches (name_en, name_ar, address_en, address_ar, phone, whatsapp, main_branch, active)
VALUES ('Nexus Clinic – Heliopolis', 'نيكسوس كلينيك – مصر الجديدة',
        'Heliopolis, Cairo, Egypt', 'مصر الجديدة، القاهرة، مصر',
        '01035411305', '201035411305', TRUE, TRUE);

-- Working Hours
INSERT INTO working_hours (day_of_week, open_time, close_time, branch_id) VALUES
('MONDAY',    '9:00 AM', '8:00 PM', 1),
('TUESDAY',   '9:00 AM', '8:00 PM', 1),
('WEDNESDAY', '9:00 AM', '8:00 PM', 1),
('THURSDAY',  '9:00 AM', '8:00 PM', 1),
('FRIDAY',    '9:00 AM', '1:00 PM', 1),
('SATURDAY',  '9:00 AM', '5:00 PM', 1),
('SUNDAY',    NULL,      NULL,      1);
UPDATE working_hours SET closed = TRUE WHERE day_of_week = 'SUNDAY';

-- Services
INSERT INTO services (name_en, name_ar, description_en, description_ar, icon_class, slug, sort_order) VALUES
('Sports Rehabilitation', 'إعادة تأهيل رياضي',
 'Targeted programs to get athletes back to peak performance after injury.',
 'برامج موجهة لإعادة الرياضيين إلى أفضل أداء بعد الإصابة.',
 'fa-solid fa-person-running', 'sports-rehabilitation', 1),
('Post-Surgical Rehab', 'تأهيل ما بعد الجراحة',
 'Structured rehabilitation following orthopedic and spinal surgeries.',
 'إعادة تأهيل منظمة بعد جراحات العظام والعمود الفقري.',
 'fa-solid fa-syringe', 'post-surgical-rehab', 2),
('Manual Therapy', 'العلاج اليدوي',
 'Hands-on techniques to restore movement and reduce pain.',
 'تقنيات يدوية لاستعادة الحركة وتقليل الألم.',
 'fa-solid fa-hand-holding-heart', 'manual-therapy', 3),
('Dry Needling', 'إبر التحفيز العضلي',
 'Evidence-based dry needling to release muscle trigger points.',
 'إبر تحفيزية للعضلات وفق أحدث الأدلة العلمية.',
 'fa-solid fa-syringe', 'dry-needling', 4),
('Chronic Pain Management', 'إدارة الألم المزمن',
 'Comprehensive programmes for long-term pain relief and function.',
 'برامج شاملة للتخفيف من الألم المزمن وتحسين الوظيفة.',
 'fa-solid fa-shield-heart', 'chronic-pain-management', 5),
('Performance Training', 'تدريب الأداء',
 'Strength, conditioning, and injury prevention for serious athletes.',
 'تقوية وتكييف بدني لمنع الإصابات للرياضيين الجادين.',
 'fa-solid fa-dumbbell', 'performance-training', 6);

-- Conditions
INSERT INTO conditions (name_en, name_ar, body_area, icon_class, slug, active) VALUES
('Knee Pain', 'ألم الركبة', 'knee', 'fa-solid fa-person-walking', 'knee-pain', TRUE),
('ACL Injury', 'إصابة الرباط الصليبي', 'knee', 'fa-solid fa-bandage', 'acl-injury', TRUE),
('Shoulder Impingement', 'التضيق الكتفي', 'shoulder', 'fa-solid fa-person', 'shoulder-impingement', TRUE),
('Rotator Cuff Tear', 'تمزق الكافة المدورة', 'shoulder', 'fa-solid fa-person', 'rotator-cuff-tear', TRUE),
('Lower Back Pain', 'ألم أسفل الظهر', 'spine', 'fa-solid fa-spine', 'lower-back-pain', TRUE),
('Disc Herniation', 'انزلاق غضروفي', 'spine', 'fa-solid fa-spine', 'disc-herniation', TRUE),
('Ankle Instability', 'عدم ثبات الكاحل', 'ankle', 'fa-solid fa-person-walking', 'ankle-instability', TRUE),
('Plantar Fasciitis', 'التهاب اللفافة الأخمصية', 'ankle', 'fa-solid fa-shoe-prints', 'plantar-fasciitis', TRUE),
('Tennis Elbow', 'مرفق التنس', 'elbow', 'fa-solid fa-hand', 'tennis-elbow', TRUE),
('Neck Pain', 'ألم الرقبة', 'neck', 'fa-solid fa-head-side', 'neck-pain', TRUE);

-- FAQs
INSERT INTO faqs (question_en, question_ar, answer_en, answer_ar, sort_order, active) VALUES
('Do I need a referral to book an appointment?',
 'هل أحتاج إلى تحويل طبي لحجز موعد؟',
 'No, you can book directly with us without a referral.',
 'لا، يمكنك الحجز مباشرة معنا دون الحاجة إلى تحويل طبي.',
 1, TRUE),
('How long is a typical session?',
 'ما مدة الجلسة العادية؟',
 'Most sessions last 45–60 minutes. Initial assessments may take up to 75 minutes.',
 'تستغرق معظم الجلسات من 45 إلى 60 دقيقة. قد يستغرق التقييم الأولي حتى 75 دقيقة.',
 2, TRUE),
('What should I wear to my appointment?',
 'ماذا يجب أن أرتدي في موعدي؟',
 'Wear loose, comfortable clothing that allows access to the area being treated.',
 'ارتدِ ملابس فضفاضة ومريحة تتيح الوصول إلى المنطقة المعالجة.',
 3, TRUE),
('Do you offer home visit services?',
 'هل تقدمون خدمات الزيارات المنزلية؟',
 'Yes, we offer home visit physiotherapy for patients who cannot travel to the clinic.',
 'نعم، نقدم خدمات العلاج الطبيعي المنزلية للمرضى الذين لا يستطيعون التنقل.',
 4, TRUE),
('How many sessions will I need?',
 'كم عدد الجلسات التي سأحتاجها؟',
 'This varies per condition. Your therapist will create a personalised plan after the initial assessment.',
 'يختلف ذلك حسب الحالة. سيضع معالجك خطة شخصية بعد التقييم الأولي.',
 5, TRUE);

-- Testimonials
INSERT INTO testimonials (patient_name, content_en, rating, condition_name, approved, featured) VALUES
('Ahmed M.', 'After my ACL surgery, the team at Nexus helped me return to football faster than I ever expected. Professional, focused, and genuinely caring.', 5, 'ACL Rehabilitation', TRUE, TRUE),
('Sara K.', 'My chronic back pain of 3 years is finally manageable. The individualised approach made all the difference.', 5, 'Chronic Back Pain', TRUE, TRUE),
('Omar H.', 'Best physio experience in Cairo. Modern techniques, clean facility, and a team that truly listens.', 5, 'Shoulder Impingement', TRUE, TRUE);
