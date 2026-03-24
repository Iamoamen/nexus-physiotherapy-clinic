/* ============================================================
   NEXUS CLINIC – admin.js
   Admin panel specific JavaScript: AJAX actions, DataTable init,
   inline confirmations, and UX polish.
============================================================ */

document.addEventListener('DOMContentLoaded', () => {

  // ── Auto-dismiss flash alerts after 5s ──────────────────
  document.querySelectorAll('.alert:not(.alert-permanent)').forEach(alert => {
    setTimeout(() => {
      const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
      if (bsAlert) bsAlert.close();
    }, 5000);
  });

  // ── Confirm-before-submit on data-confirm buttons ────────
  document.querySelectorAll('[data-confirm]').forEach(btn => {
    btn.addEventListener('click', function (e) {
      if (!confirm(this.dataset.confirm || 'Are you sure?')) {
        e.preventDefault();
        e.stopPropagation();
      }
    });
  });

  // ── Mobile sidebar toggle ────────────────────────────────
  const toggleBtn   = document.getElementById('sidebarToggleBtn');
  const sidebar     = document.getElementById('adminSidebar');
  const backdrop    = document.getElementById('sidebarBackdrop');

  if (toggleBtn && sidebar) {
    toggleBtn.addEventListener('click', () => {
      sidebar.classList.toggle('show');
      if (backdrop) backdrop.classList.toggle('show');
    });
  }
  if (backdrop) {
    backdrop.addEventListener('click', () => {
      sidebar.classList.remove('show');
      backdrop.classList.remove('show');
    });
  }

  // ── Slug auto-generation from title inputs ───────────────
  function toSlug(str) {
    return str.toLowerCase()
              .trim()
              .replace(/\s+/g, '-')
              .replace(/[^a-z0-9-]/g, '');
  }

  const titleInputs = [
    { src: 'sNameEn',    dest: 'sSlug'    },
    { src: 'blogTitleEn', dest: 'blogSlug' },
    { src: 'condNameEn', dest: 'condSlug' },
  ];
  titleInputs.forEach(({ src, dest }) => {
    const srcEl  = document.getElementById(src);
    const destEl = document.getElementById(dest);
    if (srcEl && destEl && !destEl.value) {
      srcEl.addEventListener('input', () => {
        destEl.value = toSlug(srcEl.value);
      });
    }
  });

  // ── AJAX appointment status update ───────────────────────
  // Replaces form POST with fetch so table updates without full reload
  document.querySelectorAll('[data-ajax-status]').forEach(btn => {
    btn.addEventListener('click', async function (e) {
      e.preventDefault();
      const id     = this.dataset.id;
      const status = this.dataset.status;
      const csrf   = document.querySelector('meta[name="_csrf"]')?.content
                  || document.querySelector('input[name="_csrf"]')?.value;

      if (!confirm(`Mark this appointment as ${status}?`)) return;

      try {
        const res = await fetch(`/api/v1/admin/appointments/${id}/status`, {
          method: 'PATCH',
          headers: {
            'Content-Type':  'application/json',
            'X-CSRF-TOKEN':  csrf || '',
          },
          body: JSON.stringify({ status }),
        });
        const data = await res.json();
        if (data.success) {
          // Update the badge in the row without page reload
          const row    = this.closest('tr');
          const badge  = row?.querySelector('.appointment-status-badge');
          if (badge) {
            badge.className = `badge rounded-pill badge-${status.toLowerCase()}`;
            badge.textContent = status;
          }
          // Flash success toast
          showToast(`Appointment marked as ${status}.`, 'success');
          // Remove action buttons that no longer apply
          this.closest('.btn-group')?.querySelectorAll(`[data-status="${status}"]`).forEach(b => b.remove());
        } else {
          showToast('Update failed: ' + (data.error || 'Unknown error'), 'danger');
        }
      } catch (err) {
        showToast('Network error. Please refresh and try again.', 'danger');
        console.error(err);
      }
    });
  });

  // ── Toast notification helper ────────────────────────────
  function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      container.style.cssText = 'position:fixed;top:16px;right:16px;z-index:9999;display:flex;flex-direction:column;gap:8px;';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `alert alert-${type} alert-dismissible shadow-sm mb-0`;
    toast.style.cssText = 'min-width:260px;max-width:340px;font-size:0.88rem;animation:fadeSlideDown 0.3s ease;';
    toast.innerHTML = `
      <i class="fa-solid fa-${type === 'success' ? 'circle-check' : 'triangle-exclamation'} me-2"></i>
      ${message}
      <button type="button" class="btn-close" onclick="this.closest('.alert').remove()"></button>
    `;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
  }

  // ── Table search filter ──────────────────────────────────
  const searchInput = document.getElementById('tableSearch');
  if (searchInput) {
    searchInput.addEventListener('input', function () {
      const q     = this.value.toLowerCase();
      const rows  = document.querySelectorAll('tbody tr[data-searchable]');
      rows.forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  }

  // ── Character counter for textareas ─────────────────────
  document.querySelectorAll('textarea[maxlength]').forEach(ta => {
    const max     = parseInt(ta.getAttribute('maxlength'));
    const counter = document.createElement('div');
    counter.className = 'text-muted small text-end mt-1';
    counter.textContent = `0 / ${max}`;
    ta.parentNode.insertBefore(counter, ta.nextSibling);
    ta.addEventListener('input', () => {
      counter.textContent = `${ta.value.length} / ${max}`;
      counter.style.color = ta.value.length > max * 0.9 ? '#dc3545' : '';
    });
  });

  // ── Highlight active nav link from URL ──────────────────
  const path = window.location.pathname;
  document.querySelectorAll('.admin-nav-link').forEach(link => {
    const href = link.getAttribute('href');
    if (href && path.startsWith(href) && href !== '/admin') {
      link.classList.add('active');
    }
    if (href === '/admin' && path === '/admin') {
      link.classList.add('active');
    }
  });

});
