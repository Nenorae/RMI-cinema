document.addEventListener("DOMContentLoaded", function () {
  // Update tahun di footer secara dinamis
  const currentYearSpan = document.querySelector("footer #current-year"); // Lebih spesifik
  if (currentYearSpan) {
    // Cek null jika footer tidak di semua halaman
    currentYearSpan.textContent = new Date().getFullYear();
  } else {
    // Fallback untuk th:text di footer fragment
    const footerYear = document.querySelector('footer span[th\\:text*="dates.year"]');
    if (footerYear && footerYear.textContent.trim() === "") {
      // Jika th:text tidak dirender oleh JS, biarkan Thymeleaf yang handle
    } else if (footerYear) {
      // jika ada tapi kosong, isi (jarang terjadi jika Thymeleaf aktif)
      // footerYear.textContent = new Date().getFullYear();
    }
  }

  // Contoh interaksi tombol CTA jika ada di halaman
  const ctaButton = document.querySelector(".cta-button");
  if (ctaButton) {
    ctaButton.addEventListener("click", function (e) {
      // Jika ini link <a>, biarkan default action jika href valid
      if (this.tagName === "BUTTON" || (this.tagName === "A" && this.getAttribute("href") === "#")) {
        e.preventDefault();
        alert("Anda mengklik tombol CTA! Fitur ini akan segera hadir atau sudah mengarah ke halaman lain.");
      }
    });
  }

  console.log("Website Cinema KW Super siap! (v2)");
});
