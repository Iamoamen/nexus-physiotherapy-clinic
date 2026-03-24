# NEXUS CLINIC — Complete Full-Stack Website
## Spring Boot 3.2 · Thymeleaf · Bootstrap 5 · MySQL · Bilingual AR/EN

> **#MoveBeyond** — Production-ready physiotherapy clinic web application

---

## Quick Start

### Prerequisites: Java 17+, Maven 3.8+, MySQL 8.0+

```bash
# 1. Create MySQL database
mysql -u root -p -e "CREATE DATABASE nexus_clinic CHARACTER SET utf8mb4; CREATE USER 'nexus_user'@'localhost' IDENTIFIED BY 'nexus_pass'; GRANT ALL ON nexus_clinic.* TO 'nexus_user'@'localhost';"

# 2. Run application
./mvnw spring-boot:run

# 3. Access
#   Website:   http://localhost:8080
#   Admin:     http://localhost:8080/admin  (admin / nexus@admin2025)
#   API:       http://localhost:8080/api/v1/health
```

---

## Docker (Recommended for Production)

```bash
cp .env.example .env          # Fill in secrets
./mvnw clean package -DskipTests
docker-compose up -d
```

---

## Complete File Count

| Category | Count |
|----------|-------|
| Public pages (Thymeleaf) | 15 |
| Admin pages | 9 |
| Error pages | 3 |
| Java source files | 15 |
| CSS files | 3 |
| JS files | 2 |
| SQL migrations | 3 |
| Test cases | 12 |

---

## Key Features

- Online appointment booking with validation and email confirmation
- Admin dashboard: manage appointments, services, conditions, team, blog, FAQs, testimonials, inquiries
- Bilingual EN/AR with RTL support (switch via ?lang=ar)
- Scheduled tasks: daily reminders, no-show marking, weekly digest
- REST API at /api/v1/* for AJAX and future mobile app
- SEO: sitemap.xml, robots.txt, structured data (schema.org MedicalBusiness)
- Docker + Nginx production deployment with HTTPS and rate limiting
- WhatsApp integration (wa.me links + optional CallMeBot API)
- 12 unit and integration tests

---

## Admin Access

URL: /admin
Username: admin
Password: Set in application.properties → app.admin.default-password

---

## Clinic Config (application.properties)

app.clinic.phone=+20103-5411305
app.clinic.whatsapp=+201035411305
app.clinic.email=info@nexusclinic.eg
app.clinic.address=Heliopolis, Cairo, Egypt

---

Built for Nexus Clinic, Heliopolis, Cairo — #MoveBeyond
Instagram: @nexus.physioclinic
