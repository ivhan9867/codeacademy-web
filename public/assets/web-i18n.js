(function () {
  const STORAGE_KEY = "ca_web_lang_v1";

  const DICT = {
    en: {
      // Shared Nav
      nav_portal: "Portal",
      nav_app: "App",
      nav_manual: "Manual",
      nav_verify: "Verify Certificate",
      // Landing Page (index.html)
      landing_title: "CodeAcademy — Practical coding on your smartphone",
      hero_h1: "Practical coding on your smartphone",
      hero_lead:
        "Courses, interactive code exercises, and final projects in one mobile app. This portal is used by facilitators to grade submissions, and by anyone to verify certificates.",
      hero_cta_portal: "Facilitator & Admin Portal",
      hero_cta_verify: "Verify Certificate",
      phone_greet: "Hello, Ahmad!",
      phone_sub: "Ready to code today",
      phone_streak: "7 days",
      phone_banner_title: "Practical Mobile Coding",
      phone_banner_cta: "Start learning",
      phone_continue: "Continue learning",
      phone_course: "Web Foundation: HTML & CSS",
      phone_course_meta: "Module 5 of 8 · 60% complete",
      phone_missions: "Daily quests",
      phone_m1: "Complete 1 module",
      phone_m2: "Ask AI Tutor 1x",
      phone_tab_home: "Home",
      phone_tab_courses: "Courses",
      phone_tab_progress: "Progress",
      phone_tab_community: "Community",
      phone_tab_profile: "Profile",
      portal_h2: "Three things you can do here",
      portal_fac_title: "Facilitator & Admin Portal",
      portal_fac_tag: "Login Required",
      portal_fac_desc:
        "Dedicated staff workspace: review final projects, moderate discussions, and manage team roles & certificates as Super Admin.",
      portal_fac_go: "Open staff portal",
      portal_ver_title: "Certificate Verification",
      portal_ver_desc:
        "Enter a credential ID to confirm that a certificate is authentic and registered.",
      portal_ver_go: "Check ID",
      portal_man_title: "Application Manual",
      portal_man_desc:
        "21-chapter guide complete with Use Case, Flowcharts, User Flow, and screen-by-screen walkthroughs.",
      portal_man_read: "Read on web",
      portal_man_docx: "Download Word (.docx)",
      app_h2: "Inside the app",
      app_sub:
        "Designed for mobile screens, from reading structured lessons to submitting your final project.",
      feat_1_t: "Structured courses",
      feat_1_d: "Bite-sized modules with local progress tracking and interactive glossaries.",
      feat_2_t: "Live playground",
      feat_2_d: "Write HTML, CSS, and JavaScript and preview the output directly on your phone.",
      feat_3_t: "AI Tutor",
      feat_3_d: "Ask questions or attach code screenshots/photos for instant guided help.",
      feat_4_t: "Section quizzes",
      feat_4_d: "Test your understanding per section with detailed explanations.",
      feat_5_t: "Final projects",
      feat_5_d: "Upload your project files or ZIP directly for facilitator review.",
      feat_6_t: "Digital certificates",
      feat_6_d: "Every issued certificate carries a unique publicly verifiable ID.",
      footer_copy: "© 2026 CodeAcademy Mobile — Mobile Application Development Final Project",
      footer_manual: "Application manual",
      // Verify Page (verify.html)
      verify_title: "Verify Certificate — CodeAcademy",
      verify_h1: "Verify certificate authenticity",
      verify_lead:
        "Enter the credential ID printed on the certificate (format CC-yyyyMMxxxx-xxxxxx). The portal checks directly against CodeAcademy's Firestore registry.",
      verify_label: "Credential ID",
      verify_btn: "Check ID",
      verify_status_ok: "Authentic certificate registered in Firestore",
      verify_what: "Completed the full curriculum and final project evaluation on CodeAcademy.",
      verify_dt_id: "Credential ID",
      verify_dt_course: "Course",
      verify_dt_score: "Final Score",
      verify_dt_date: "Issued On",
      verify_dt_fac: "Reviewer",
      verify_dt_status: "Status",
      verify_reg_src: "Registered in Firestore database",
      verify_not_found_t: "ID not found",
      verify_not_found_d:
        "Double-check the spelling. Official CodeAcademy IDs start with CC- (e.g. CC-2026110101-A9K3P2) and only pass if recorded in Firestore.",
      verify_side_h2: "How to verify",
      verify_step_1: "Find the credential ID starting with CC- on the Android app's digital certificate.",
      verify_step_2: "Type the ID exactly as shown, then click Check ID.",
      verify_step_3: "Match the recipient name, course title, and final score returned from the database.",
      // Facilitator Auth & Access Request (facilitator/index.html)
      fac_title: "Facilitator & Admin Portal — CodeAcademy",
      auth_eyebrow: "CODEACADEMY STAFF PORTAL",
      auth_h2: "Sign in to Workspace",
      auth_desc:
        "Use a registered Firebase Facilitator or Super Admin account to access the review queue, forum, announcements, and certificates.",
      auth_email_label: "Staff Email",
      auth_email_ph: "facilitator@domain.com",
      auth_pass_label: "Password",
      auth_pass_ph: "Enter your password",
      auth_submit: "Sign in with Email",
      auth_divider: "or continue with",
      auth_google: "Sign in with Google",
      auth_req_title: "Need portal access?",
      auth_req_desc: "Submit an access request to join as an Admin or Facilitator.",
      auth_req_btn: "Request Access",
      auth_back: "← Back to main homepage",
      modal_eyebrow: "CODEACADEMY STAFF ACCESS",
      modal_h3: "Request Portal Access",
      modal_desc:
        "Fill out the form below to request official staff access. Your request is saved to the portal registry and prepared for email verification.",
      modal_role_label: "Requested Role",
      modal_role_fac_t: "Facilitator",
      modal_role_fac_d: "Review final projects, grade rubrics, and moderate student discussions.",
      modal_role_adm_t: "Admin (Super Admin)",
      modal_role_adm_d: "Full access to grading, team management, users, and portal announcements.",
      modal_name_label: "Full Name",
      modal_name_ph: "Full name & title (optional)",
      modal_email_label: "Email to Register",
      modal_email_ph: "name@university.edu",
      modal_aff_label: "Institution / Position",
      modal_aff_ph: "e.g. Lecturer — Informatics Engineering",
      modal_reason_label: "Purpose / Notes",
      modal_reason_ph: "Briefly explain your role or evaluation purpose on CodeAcademy...",
      modal_cancel: "Cancel",
      modal_submit: "Submit Access Request",
      // Showcase & Portal Workspace (EN)
      showcase_pill: "CodeAcademy Staff Workspace",
      showcase_heading: "Manage project reviews, class forums & certificates in one workspace.",
      tab_projects: "Projects",
      tab_forum: "Forum",
      tab_announcements: "Announcements",
      tab_certificates: "Certificates",
      tab_team: "Team",
      tab_analytics: "Analytics",
      nav_verify_short: "Verify",
      nav_logout: "Sign out",
      hdr_eyebrow: "Final Project Evaluation",
      hdr_title: "Review Queue",
      hdr_sub: "Inspect code submissions, score rubrics, and issue verified certificates.",
      btn_unanswered_forum: "Unanswered Forum",
      btn_new_announcement: "+ New Announcement",
      spot_kicker: "Next Priority Review",
      spot_pill: "40/60 Rubric • Live Sandbox",
      spot_loading: "Loading submission queue...",
      spot_cta: "Start Review →",
      stat_total: "Total Submissions",
      stat_pending: "Pending Review",
      stat_priority: "Priority",
      stat_revision: "Revision Requested",
      stat_followup: "Follow-up",
      stat_approved: "Approved",
      stat_certified: "Certified",
      search_ph: "Search student, email, or course...",
      filter_all_courses: "All Courses",
      filter_all: "All",
      filter_pending: "Pending",
      filter_revision: "Revision",
      filter_approved: "Approved",
      th_student: "Student",
      th_course: "Course",
      th_quiz: "Quiz",
      th_status: "Status",
      th_submitted: "Submitted",
      th_attempts: "Attempt",
      th_action: "Action",
      table_loading: "Loading submissions...",
      rail_highlights: "Class Activity",
      rail_see_all: "View all",
      rail_loading: "Loading recent activity...",
      rail_actions: "Action Items",
      rail_tasks_loading: "Loading tasks..."
    },
    id: {
      nav_portal: "Portal",
      nav_app: "Aplikasi",
      nav_manual: "Manual",
      nav_verify: "Verifikasi sertifikat",
      landing_title: "CodeAcademy — Belajar coding praktis di smartphone",
      hero_h1: "Belajar coding praktis di smartphone",
      hero_lead:
        "Kelas, latihan kode, dan proyek akhir dalam satu aplikasi. Portal ini dipakai fasilitator untuk menilai proyek, dan siapa pun untuk memeriksa sertifikat.",
      hero_cta_portal: "Portal Fasilitator & Admin",
      hero_cta_verify: "Verifikasi sertifikat",
      phone_greet: "Halo, Ahmad!",
      phone_sub: "Semangat belajar coding hari ini",
      phone_streak: "7 hari",
      phone_banner_title: "Belajar Coding Praktis di Smartphone",
      phone_banner_cta: "Mulai belajar",
      phone_continue: "Lanjutkan belajar",
      phone_course: "Fondasi Web: HTML & CSS",
      phone_course_meta: "Modul 5 dari 8 · 60% selesai",
      phone_missions: "Misi harian",
      phone_m1: "Selesaikan 1 modul",
      phone_m2: "Tanya AI Tutor 1x",
      phone_tab_home: "Beranda",
      phone_tab_courses: "Kelas",
      phone_tab_progress: "Progres",
      phone_tab_community: "Komunitas",
      phone_tab_profile: "Profil",
      portal_h2: "Tiga hal yang bisa dilakukan di sini",
      portal_fac_title: "Portal Fasilitator & Admin",
      portal_fac_tag: "Perlu Login",
      portal_fac_desc:
        "Workspace khusus staf: review proyek akhir, moderasi forum, serta panel Super Admin untuk tim & sertifikat.",
      portal_fac_go: "Masuk portal staf",
      portal_ver_title: "Verifikasi sertifikat",
      portal_ver_desc:
        "Masukkan ID kredensial untuk memastikan sertifikat itu asli dan terdaftar.",
      portal_ver_go: "Periksa ID",
      portal_man_title: "Manual aplikasi",
      portal_man_desc:
        "Panduan 21 bab lengkap dengan Use Case, Flowchart, User Flow, dan mockup setiap layar beserta langkah penggunaannya.",
      portal_man_read: "Baca di web",
      portal_man_docx: "Unduh Word (.docx)",
      app_h2: "Isi aplikasinya",
      app_sub:
        "Semua dirancang untuk layar ponsel, dari membaca materi sampai mengirim proyek akhir.",
      feat_1_t: "Kelas bertahap",
      feat_1_d: "Materi dipecah jadi modul pendek dengan progres tersimpan & glosarium interaktif.",
      feat_2_t: "Playground live",
      feat_2_d: "Tulis HTML, CSS, dan JS, lalu lihat hasilnya langsung di ponsel.",
      feat_3_t: "AI Tutor",
      feat_3_d: "Tanya saat buntu atau unggah foto/screenshot kode untuk bimbingan langsung.",
      feat_4_t: "Kuis per modul",
      feat_4_d: "Uji pemahaman per bagian lengkap dengan pembahasan detail.",
      feat_5_t: "Proyek akhir",
      feat_5_d: "Unggah file proyek atau ZIP untuk dinilai fasilitator lewat portal ini.",
      feat_6_t: "Sertifikat digital",
      feat_6_d: "Setiap sertifikat punya ID unik yang bisa diperiksa siapa saja.",
      footer_copy: "© 2026 CodeAcademy Mobile — Proyek Tugas Akhir Pengembangan Aplikasi Perangkat Bergerak",
      footer_manual: "Manual aplikasi",
      verify_title: "Verifikasi sertifikat — CodeAcademy",
      verify_h1: "Cek keaslian sertifikat",
      verify_lead:
        "Masukkan ID kredensial yang tercetak di sertifikat (format CC-yyyyMMxxxx-xxxxxx). Portal memeriksa langsung ke basis data Firestore CodeAcademy.",
      verify_label: "ID kredensial",
      verify_btn: "Periksa ID",
      verify_status_ok: "Sertifikat asli dan terdaftar di Firestore",
      verify_what: "Menyelesaikan seluruh kurikulum dan evaluasi proyek akhir CodeAcademy.",
      verify_dt_id: "ID Kredensial",
      verify_dt_course: "Kelas",
      verify_dt_score: "Nilai akhir",
      verify_dt_date: "Diterbitkan",
      verify_dt_fac: "Penilai",
      verify_dt_status: "Status",
      verify_reg_src: "Terdaftar di basis data Firestore",
      verify_not_found_t: "ID tidak ditemukan",
      verify_not_found_d:
        "Periksa kembali penulisannya. Format ID resmi sertifikat CodeAcademy diawali CC- (misalnya CC-2026110101-A9K3P2) dan hanya lolos jika tercatat di Firestore.",
      verify_side_h2: "Cara memeriksa",
      verify_step_1: "Cari ID kredensial berawalan CC- di sertifikat digital aplikasi Android.",
      verify_step_2: "Ketik ID tersebut persis seperti tertulis, lalu pilih Periksa ID.",
      verify_step_3: "Cocokkan nama peserta, nama kelas, serta nilai akhir yang muncul dari basis data.",
      fac_title: "Portal Fasilitator & Admin — CodeAcademy",
      auth_eyebrow: "PORTAL STAF CODEACADEMY",
      auth_h2: "Masuk ke Workspace",
      auth_desc:
        "Gunakan akun Firebase Fasilitator atau Super Admin terdaftar untuk mengakses antrean review, forum, pengumuman, dan sertifikat.",
      auth_email_label: "Email Fasilitator",
      auth_email_ph: "fasilitator@domain.id",
      auth_pass_label: "Kata Sandi",
      auth_pass_ph: "Masukkan kata sandi",
      auth_submit: "Masuk dengan Email",
      auth_divider: "atau lanjutkan dengan",
      auth_google: "Masuk dengan Google",
      auth_req_title: "Belum memiliki akses portal?",
      auth_req_desc: "Ajukan pendaftaran akun sebagai Admin atau Fasilitator CodeAcademy.",
      auth_req_btn: "Ajukan Akses",
      auth_back: "← Kembali ke beranda utama",
      modal_eyebrow: "AKSES STAF CODEACADEMY",
      modal_h3: "Ajukan Akses Portal",
      modal_desc:
        "Isi formulir di bawah ini untuk mengajukan akses resmi. Permintaan tersimpan ke sistem dan siap dikirim ke pengelola CodeAcademy.",
      modal_role_label: "Peran yang Diajukan",
      modal_role_fac_t: "Fasilitator",
      modal_role_fac_d: "Review proyek akhir, penilaian rubrik, dan moderasi forum peserta.",
      modal_role_adm_t: "Admin (Super Admin)",
      modal_role_adm_d: "Akses penuh penilaian, manajemen tim, peserta, dan pengumuman portal.",
      modal_name_label: "Nama Lengkap",
      modal_name_ph: "Nama lengkap & gelar (opsional)",
      modal_email_label: "Email yang Didaftarkan",
      modal_email_ph: "nama@domain.ac.id",
      modal_aff_label: "Instansi / Jabatan",
      modal_aff_ph: "Contoh: Dosen Penguji — Teknik Informatika",
      modal_reason_label: "Tujuan Pengajuan",
      modal_reason_ph: "Jelaskan singkat kebutuhan akses Anda pada portal CodeAcademy...",
      modal_cancel: "Batal",
      modal_submit: "Kirim Pengajuan Akses",
      // Showcase & Portal Workspace (ID)
      showcase_pill: "CodeAcademy Staff Workspace",
      showcase_heading: "Kelola evaluasi proyek, forum kelas & sertifikat dalam satu kendali.",
      tab_projects: "Proyek",
      tab_forum: "Forum",
      tab_announcements: "Pengumuman",
      tab_certificates: "Sertifikat",
      tab_team: "Tim",
      tab_analytics: "Analitik",
      nav_verify_short: "Verifikasi",
      nav_logout: "Keluar",
      hdr_eyebrow: "Evaluasi Proyek Akhir",
      hdr_title: "Antrean Penilaian",
      hdr_sub: "Tinjau kode peserta, beri skor rubrik, dan terbitkan sertifikat.",
      btn_unanswered_forum: "Forum Belum Dijawab",
      btn_new_announcement: "+ Pengumuman Baru",
      spot_kicker: "Prioritas Berikutnya",
      spot_pill: "Rubrik 40/60 • Live Sandbox",
      spot_loading: "Memuat antrean proyek...",
      spot_cta: "Mulai Review →",
      stat_total: "Total Pengajuan",
      stat_pending: "Menunggu Review",
      stat_priority: "Prioritas",
      stat_revision: "Perlu Revisi",
      stat_followup: "Tindak lanjut",
      stat_approved: "Disetujui",
      stat_certified: "Sertifikat",
      search_ph: "Cari nama peserta, email, atau kursus...",
      filter_all_courses: "Semua Kursus",
      filter_all: "Semua",
      filter_pending: "Pending",
      filter_revision: "Revisi",
      filter_approved: "Approved",
      th_student: "Peserta",
      th_course: "Kursus",
      th_quiz: "Kuis",
      th_status: "Status",
      th_submitted: "Dikirim",
      th_attempts: "Percobaan",
      th_action: "Aksi",
      table_loading: "Memuat data pengajuan...",
      rail_highlights: "Aktivitas Kelas",
      rail_see_all: "Lihat semua",
      rail_loading: "Memuat aktivitas terbaru...",
      rail_actions: "Daftar Tugas",
      rail_tasks_loading: "Memuat tugas..."
    }
  };

  function getLang() {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved === "id" || saved === "en") return saved;
    } catch (_) {}
    return "en"; // Default: English
  }

  function applyLang(lang) {
    const active = lang === "id" ? "id" : "en";
    const dict = DICT[active];
    document.documentElement.lang = active;
    try {
      localStorage.setItem(STORAGE_KEY, active);
    } catch (_) {}

    const titleKey = document.body?.getAttribute("data-i18n-title");
    if (titleKey && dict[titleKey]) {
      document.title = dict[titleKey];
    }

    document.querySelectorAll("[data-i18n]").forEach((el) => {
      const key = el.getAttribute("data-i18n");
      if (dict[key]) el.textContent = dict[key];
    });

    document.querySelectorAll("[data-i18n-placeholder]").forEach((el) => {
      const key = el.getAttribute("data-i18n-placeholder");
      if (dict[key]) el.setAttribute("placeholder", dict[key]);
    });

    document.querySelectorAll(".lang-slide-toggle").forEach((toggle) => {
      toggle.setAttribute("data-lang", active);
      toggle.setAttribute("aria-checked", active === "id" ? "true" : "false");
    });
  }

  function init() {
    const current = getLang();
    applyLang(current);

    document.querySelectorAll(".lang-slide-toggle").forEach((toggle) => {
      toggle.addEventListener("click", () => {
        const next = getLang() === "en" ? "id" : "en";
        applyLang(next);
      });
    });
  }

  window.CodeAcademyWebI18n = { getLang, applyLang };

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
