/* ============================================================
   NEXUS CLINIC – main.js
============================================================ */

document.addEventListener('DOMContentLoaded', () => {

  // ---- Init AOS ----
  if (typeof AOS !== 'undefined') {
    AOS.init({
      duration: 800,
      easing: 'ease-out-cubic',
      once: true,
      offset: 60,
    });
  }

  // ---- Sticky Navbar scroll effect ----
  const navbar = document.getElementById('mainNavbar');
  if (navbar) {
    const onScroll = () => {
      navbar.classList.toggle('scrolled', window.scrollY > 50);
    };
    window.addEventListener('scroll', onScroll, { passive: true });
    onScroll();
  }

  // ---- Testimonials Swiper ----
  if (typeof Swiper !== 'undefined' && document.querySelector('.testimonialSwiper')) {
    new Swiper('.testimonialSwiper', {
      slidesPerView: 1,
      spaceBetween: 24,
      loop: true,
      autoplay: { delay: 5000, disableOnInteraction: false },
      pagination: {
        el: '.testimonial-pagination',
        clickable: true,
      },
      navigation: {
        prevEl: '.testimonial-prev',
        nextEl: '.testimonial-next',
      },
      breakpoints: {
        768: { slidesPerView: 2 },
        1200: { slidesPerView: 3 },
      },
      // RTL support
      rtl: document.documentElement.dir === 'rtl',
    });
  }

  // ---- Set min date on booking form ----
  const dateInput = document.querySelector('input[type="date"]');
  if (dateInput && !dateInput.min) {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    dateInput.min = tomorrow.toISOString().split('T')[0];
  }

  // ---- Smooth scroll for anchor links ----
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
      const target = document.querySelector(this.getAttribute('href'));
      if (target) {
        e.preventDefault();
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  });

  // ---- Counter animation ----
  const counters = document.querySelectorAll('.trust-num');
  if (counters.length) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const el = entry.target;
          const target = parseInt(el.textContent.replace(/\D/g,''));
          const suffix = el.textContent.replace(/[\d]/g,'');
          let current = 0;
          const step = Math.ceil(target / 60);
          const interval = setInterval(() => {
            current += step;
            if (current >= target) { current = target; clearInterval(interval); }
            el.textContent = current + suffix;
          }, 24);
          observer.unobserve(el);
        }
      });
    }, { threshold: 0.5 });
    counters.forEach(c => observer.observe(c));
  }

  // ---- Auto-dismiss flash alerts ----
  const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
  alerts.forEach(alert => {
    setTimeout(() => {
      const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
      if (bsAlert) bsAlert.close();
    }, 6000);
  });

  // ---- Admin: Confirm delete ----
  document.querySelectorAll('[data-confirm]').forEach(btn => {
    btn.addEventListener('click', function(e) {
      if (!confirm(this.dataset.confirm || 'Are you sure?')) {
        e.preventDefault();
      }
    });
  });

  // ---- Admin sidebar toggle (mobile) ----
  const sidebarToggle = document.getElementById('sidebarToggle');
  const adminSidebar  = document.getElementById('adminSidebar');
  if (sidebarToggle && adminSidebar) {
    sidebarToggle.addEventListener('click', () => {
      adminSidebar.classList.toggle('show');
    });
  }

  // ---- Booking form: therapist filter by service (optional) ----
  const serviceSelect   = document.getElementById('serviceId');
  const therapistSelect = document.getElementById('therapistId');
  if (serviceSelect && therapistSelect) {
    serviceSelect.addEventListener('change', function() {
      // Could fetch therapists by service via AJAX if needed
      // For now, shows all therapists
    });
  }

});
