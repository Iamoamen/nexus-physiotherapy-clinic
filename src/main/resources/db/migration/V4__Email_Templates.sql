-- ============================================================
-- V4: Email Templates
-- Editable from the admin dashboard.
-- Supports {{name}}, {{date}}, {{time}}, {{service}} placeholders.
-- ============================================================

CREATE TABLE IF NOT EXISTS email_templates (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)   NOT NULL UNIQUE,   -- machine key e.g. 'booking_confirmation'
    name_en     VARCHAR(150)  NOT NULL,           -- human label in English
    name_ar     VARCHAR(150)  NOT NULL,           -- human label in Arabic
    subject_en  VARCHAR(250)  NOT NULL,
    subject_ar  VARCHAR(250)  NOT NULL,
    body_en     TEXT          NOT NULL,
    body_ar     TEXT          NOT NULL,
    active      BOOLEAN       NOT NULL DEFAULT TRUE,
    updated_at  DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── Booking confirmation (sent to patient) ────────────────────
INSERT INTO email_templates (code, name_en, name_ar, subject_en, subject_ar, body_en, body_ar) VALUES (
  'booking_confirmation',
  'Booking Confirmation (to Patient)',
  'تأكيد الحجز (للمريض)',
  'Appointment Request Received – Nexus Clinic',
  'تم استلام طلب موعدك – نيكسوس كلينيك',
  'Dear {{name}},

Thank you for choosing Nexus Clinic.

We have received your appointment request for:
  Date:    {{date}}
  Time:    {{time}}
  Service: {{service}}

Our team will confirm your booking within 2 hours during working hours.

If you have any questions, please call us at +20 103 541 1305 or reply to this email.

Best regards,
Nexus Clinic Team
Heliopolis, Cairo
#MoveBeyond',
  'عزيزي {{name}}،

شكراً لاختيارك نيكسوس كلينيك.

لقد استلمنا طلب حجزك للموعد التالي:
  التاريخ:  {{date}}
  الوقت:    {{time}}
  الخدمة:   {{service}}

سيتواصل معك فريقنا لتأكيد الحجز خلال ساعتين في أوقات العمل.

للاستفسار يرجى الاتصال على: 01035411305 أو الرد على هذا البريد.

مع تحيات،
فريق نيكسوس كلينيك
مصر الجديدة، القاهرة
#MoveBeyond'
);

-- ── Admin notification (sent to clinic) ──────────────────────
INSERT INTO email_templates (code, name_en, name_ar, subject_en, subject_ar, body_en, body_ar) VALUES (
  'admin_new_booking',
  'New Booking Alert (to Admin)',
  'تنبيه حجز جديد (للإدارة)',
  'New Appointment: {{name}} – {{date}}',
  'حجز جديد: {{name}} – {{date}}',
  'New appointment request received.

Patient:  {{name}}
Phone:    {{phone}}
Email:    {{email}}
Date:     {{date}}
Time:     {{time}}
Service:  {{service}}
Message:  {{message}}

Login to manage: https://admin.nexusclinic.eg/admin/appointments',
  'تم استلام طلب حجز جديد.

المريض:   {{name}}
الهاتف:   {{phone}}
البريد:   {{email}}
التاريخ:  {{date}}
الوقت:    {{time}}
الخدمة:   {{service}}
رسالة:    {{message}}

تسجيل الدخول للإدارة: https://admin.nexusclinic.eg/admin/appointments'
);

-- ── Status: Confirmed (sent to patient when admin confirms) ───
INSERT INTO email_templates (code, name_en, name_ar, subject_en, subject_ar, body_en, body_ar) VALUES (
  'booking_confirmed',
  'Booking Confirmed (to Patient)',
  'تأكيد الموعد (للمريض)',
  'Your Appointment is Confirmed – Nexus Clinic',
  'تم تأكيد موعدك – نيكسوس كلينيك',
  'Dear {{name}},

Great news! Your appointment has been confirmed.

  Date:    {{date}}
  Time:    {{time}}
  Service: {{service}}
  Address: Heliopolis, Cairo

Please arrive 10 minutes early. If you need to reschedule, call us at least 24 hours in advance.

See you soon!
Nexus Clinic Team
+20 103 541 1305',
  'عزيزي {{name}}،

أخبار رائعة! تم تأكيد موعدك.

  التاريخ:  {{date}}
  الوقت:    {{time}}
  الخدمة:   {{service}}
  العنوان:  مصر الجديدة، القاهرة

يرجى الحضور قبل 10 دقائق. للتغيير أو الإلغاء يرجى الاتصال قبل 24 ساعة.

نراك قريباً!
فريق نيكسوس كلينيك
01035411305'
);

-- ── Status: Cancelled (sent to patient) ──────────────────────
INSERT INTO email_templates (code, name_en, name_ar, subject_en, subject_ar, body_en, body_ar) VALUES (
  'booking_cancelled',
  'Booking Cancelled (to Patient)',
  'إلغاء الموعد (للمريض)',
  'Appointment Cancelled – Nexus Clinic',
  'تم إلغاء موعدك – نيكسوس كلينيك',
  'Dear {{name}},

Your appointment scheduled for {{date}} at {{time}} has been cancelled.

To book a new appointment, visit nexusclinic.eg or call +20 103 541 1305.

We apologise for any inconvenience.

Nexus Clinic Team',
  'عزيزي {{name}}،

تم إلغاء موعدك المحدد في {{date}} الساعة {{time}}.

لحجز موعد جديد زر nexusclinic.eg أو اتصل على 01035411305.

نعتذر عن أي إزعاج.

فريق نيكسوس كلينيك'
);
