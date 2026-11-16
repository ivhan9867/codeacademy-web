// ==========================================
// CodeAcademy - Portal Fasilitator App Logic
// Sinkronisasi Real-time Firestore & Firebase Auth
// ==========================================

const COURSE_LABELS = {
  "fe-web-foundation-00": "Fondasi Web: Dari Nol ke Baris Kode Pertama",
  "fe-css-02": "CSS Layout Modern: Flexbox & Grid",
  "fe-js-03": "JavaScript Fundamentals",
  // Legacy fallback aliases (jika ada dokumen lama)
  "web-html-01": "Fondasi Web & HTML5 Semantik",
  "web-css-01": "CSS3 Modern & Flexbox/Grid",
  "web-js-01": "JavaScript ES6 & DOM"
};

function getCourseLabel(courseId) {
  return COURSE_LABELS[courseId] || courseId || "Kursus WebDev";
}

const RUBRICS_BY_COURSE = {
  "fe-web-foundation-00": [
    { id: "semantik", title: "Struktur HTML5 Semantik", desc: "Memakai header, nav, main, section, dan footer dengan hierarki yang jelas.", weight: 30 },
    { id: "aksesibilitas", title: "Aksesibilitas & Metadata", desc: "Memiliki tag title, meta viewport, label form, dan atribut alt gambar yang tepat.", weight: 25 },
    { id: "styling", title: "Presentasi & Keterbacaan Visual", desc: "Tipografi terbaca, kontras warna baik, dan jarak elemen tertata rapi.", weight: 25 },
    { id: "clean", title: "Kerapihan & Validitas Kode", desc: "Tag berpasangan benar, indentasi konsisten, tanpa elemen usang.", weight: 20 }
  ],
  "fe-css-02": [
    { id: "layout", title: "Penguasaan Flexbox & CSS Grid", desc: "Mampu menyusun struktur kolom/baris modern tanpa float/tabel.", weight: 35 },
    { id: "responsif", title: "Tampilan Responsif (Mobile-First)", desc: "Layout beradaptasi rapi pada viewport ponsel maupun desktop.", weight: 25 },
    { id: "tokens", title: "CSS Custom Properties & Konsistensi", desc: "Menggunakan variabel warna/spasi serta spacing konsisten.", weight: 20 },
    { id: "polish", title: "Estetika, Hover State & Kerapihan", desc: "Interaksi halus, kontras aksesibel, dan kode CSS terstruktur.", weight: 20 }
  ],
  "fe-js-03": [
    { id: "dom", title: "Manipulasi DOM & Event Listener", desc: "Mengubah elemen halaman secara dinamis merespons aksi pengguna.", weight: 35 },
    { id: "logika", title: "Logika Program & Validasi Input", desc: "Kondisional, fungsi, dan penanganan input kosong/tidak valid berjalan akurat.", weight: 30 },
    { id: "state", title: "Pengelolaan Data (Array/Object)", desc: "Struktur data bersih dengan method modern (map, filter, find).", weight: 20 },
    { id: "clean", title: "Kebersihan & Bebas Error Runtime", desc: "Kode mudah dibaca dan bebas error di Console saat dijalankan.", weight: 15 }
  ]
};

function getRubricsForCourse(courseId) {
  return RUBRICS_BY_COURSE[courseId] || RUBRICS_BY_COURSE["fe-web-foundation-00"];
}

// Data Demo Offline (hanya aktif jika user memilih Mode Demo Offline)
const DEMO_SUBMISSIONS = [
  {
    id: "demo_andi_fe_web",
    accountId: "demo_user_andi",
    firebaseUid: "",
    userName: "Andi Pratama",
    userEmail: "andi.pratama@example.id",
    courseId: "fe-web-foundation-00",
    courseTitle: "Fondasi Web: Dari Nol ke Baris Kode Pertama",
    status: "PENDING_REVIEW",
    finalQuizScore: 90,
    facilitatorScore: null,
    attemptCount: 1,
    submittedAt: Date.now() - 3600000 * 2,
    projectNotes: "Saya membangun landing page profil portfolio responsif memakai HTML5 semantik.",
    projectUrl: "https://github.com/contoh/portfolio-andi",
    checkedRubricIdsJson: '["semantik","aksesibilitas","styling","clean"]',
    workspaceCode: `<!DOCTYPE html>
<html lang="id">
<head>
  <style>
    body { font-family: system-ui, sans-serif; margin: 0; background: #F0FDFA; color: #0F172A; }
    header { background: #0D9488; color: white; padding: 28px 24px; text-align: center; }
    main { max-width: 720px; margin: 24px auto; padding: 0 20px; }
    .card { background: white; padding: 22px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
    footer { text-align: center; color: #64748B; font-size: 13px; padding: 20px; }
  </style>
</head>
<body>
  <header>
    <h1>Andi Pratama - Web Portfolio</h1>
    <p>Calon Front-End Developer</p>
  </header>
  <main>
    <article class="card">
      <h2>Tentang Saya</h2>
      <p>Belajar HTML5 Semantik & CSS Grid di CodeAcademy Android.</p>
    </article>
  </main>
  <footer>&copy; 2026 Andi Pratama</footer>
</body>
</html>`
  },
  {
    id: "demo_siti_fe_css",
    accountId: "demo_user_siti",
    firebaseUid: "",
    userName: "Siti Aminah",
    userEmail: "siti.aminah@example.id",
    courseId: "fe-css-02",
    courseTitle: "CSS Layout Modern: Flexbox & Grid",
    status: "APPROVED",
    finalQuizScore: 88,
    facilitatorScore: 92,
    attemptCount: 1,
    submittedAt: Date.now() - 86400000,
    reviewedAt: Date.now() - 43200000,
    facilitatorName: "Fasilitator Demo",
    feedback: "Layout CSS Grid sangat responsif dan rapi di viewport mobile maupun desktop.",
    certificateId: "CC-2026110101-A9K3P2",
    checkedRubricIdsJson: '["layout","responsif","tokens","polish"]',
    projectNotes: "Memakai CSS Grid auto-fit untuk kartu produk.",
    projectUrl: "",
    workspaceCode: `<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(160px,1fr));gap:12px;padding:16px;font-family:sans-serif;">
  <div style="background:#0F766E;color:#fff;padding:16px;border-radius:10px;">Kartu 1</div>
  <div style="background:#115E59;color:#fff;padding:16px;border-radius:10px;">Kartu 2</div>
</div>`
  },
  {
    id: "demo_budi_fe_js",
    accountId: "demo_user_budi",
    firebaseUid: "",
    userName: "Budi Santoso",
    userEmail: "budi.santoso@example.id",
    courseId: "fe-js-03",
    courseTitle: "JavaScript Fundamentals",
    status: "REVISION_REQUESTED",
    finalQuizScore: 76,
    facilitatorScore: 60,
    attemptCount: 1,
    submittedAt: Date.now() - 86400000 * 2,
    reviewedAt: Date.now() - 86400000,
    facilitatorName: "Fasilitator Demo",
    feedback: "Validasi input form belum menangani field kosong. Silakan lengkapi event listener submit.",
    checkedRubricIdsJson: '["dom"]',
    projectNotes: "Daftar tugas sederhana.",
    projectUrl: "",
    workspaceCode: `<div style="padding:16px;font-family:sans-serif;">
  <h3>To-Do List Sederhana</h3>
  <input id="item" placeholder="Tambah tugas..." />
  <button onclick="document.getElementById('list').innerHTML += '<li>' + document.getElementById('item').value + '</li>'">Tambah</button>
  <ul id="list"></ul>
</div>`
  }
];

const DEMO_FORUM_POSTS = [
  {
    postId: "demo_thread_1",
    courseId: "fe-web-foundation-00",
    authorId: "demo_user_andi",
    authorName: "Andi Pratama",
    authorIsMentor: false,
    authorLevel: 3,
    content: "Apakah untuk proyek akhir wajib memakai <main> dan <section>, atau cukup <div> saja?",
    timestamp: Date.now() - 3600000 * 5,
    likeCount: 3,
    replyToPostId: null,
    rootThreadId: null
  },
  {
    postId: "demo_thread_2",
    courseId: "fe-css-02",
    authorId: "demo_user_siti",
    authorName: "Siti Aminah",
    authorIsMentor: false,
    authorLevel: 5,
    content: "Tips cepat bedain kapan pakai Flexbox (1 dimensi) vs CSS Grid (2 dimensi) biar layout nggak berantakan?",
    timestamp: Date.now() - 3600000 * 2,
    likeCount: 5,
    replyToPostId: null,
    rootThreadId: null
  }
];

const DEMO_ANNOUNCEMENTS = [
  {
    id: "demo_ann_1",
    title: "Panduan Penilaian Proyek Akhir (40/60)",
    body: "Nilai akhir sertifikat dihitung dari 40% Kuis Aplikasi + 60% Skor Proyek Fasilitator (minimal 70 untuk terbit sertifikat).",
    courseId: "all",
    courseTitle: "Semua Kelas / Umum",
    authorName: "Tim Fasilitator CodeAcademy",
    createdAt: Date.now() - 3600000 * 12
  }
];

const DEMO_CERTIFICATES = [
  {
    certId: "CC-2026110101-A9K3P2",
    accountId: "demo_user_siti",
    userName: "Siti Aminah",
    courseId: "fe-css-02",
    courseTitle: "CSS Layout Modern: Flexbox & Grid",
    quizScore: 88,
    facilitatorScore: 92,
    finalScore: 90,
    issuedAt: Date.now() - 43200000,
    facilitatorName: "Fasilitator Demo"
  }
];

// ==========================================
// STATE APPLICATION
// ==========================================
let db = null;
let auth = null;
let isFirebaseReady = false;
let isDemoMode = false;
let currentFacilitatorUser = null;
let unsubSubmissions = null;
let unsubForum = null;
let unsubAnnouncements = null;
let unsubCertificates = null;

let submissions = [];
let forumPosts = [];
let announcements = [];
let certificates = [];

let activeView = "submissions";
let currentSubmission = null;
let filterStatus = "all";
let filterCourse = "all";
let searchQuery = "";
let forumFilterCourse = "all";
let forumSearchQuery = "";
let forumOnlyUnanswered = false;
let certFilterCourse = "all";
let certSearchQuery = "";

// DOM Elements
const authOverlay = document.getElementById("authOverlay");
const authForm = document.getElementById("authForm");
const emailInput = document.getElementById("emailInput");
const passwordInput = document.getElementById("passwordInput");
const authError = document.getElementById("authError");
const btnGoogleSignIn = document.getElementById("btnGoogleSignIn");
const btnEnterDemo = document.getElementById("btnEnterDemo");
const btnLogout = document.getElementById("btnLogout");
const modeBadge = document.getElementById("modeBadge");
const facilitatorIdentityChip = document.getElementById("facilitatorIdentityChip");

const searchInput = document.getElementById("searchInput");
const courseFilter = document.getElementById("courseFilter");
const filterPills = document.querySelectorAll(".btn-filter-pill[data-status]");
const submissionsTableBody = document.getElementById("submissionsTableBody");

const statTotal = document.getElementById("statTotal");
const statPending = document.getElementById("statPending");
const statRevision = document.getElementById("statRevision");
const statApproved = document.getElementById("statApproved");
const submissionsPendingBadge = document.getElementById("submissionsPendingBadge");

const viewTabs = document.querySelectorAll(".view-tab");
const submissionsView = document.getElementById("submissionsView");
const forumView = document.getElementById("forumView");
const announcementsView = document.getElementById("announcementsView");
const certificatesView = document.getElementById("certificatesView");
const pageEyebrow = document.getElementById("pageEyebrow");
const pageTitle = document.getElementById("pageTitle");
const pageSubtitle = document.getElementById("pageSubtitle");

const forumTotalBadge = document.getElementById("forumTotalBadge");
const forumSearchInput = document.getElementById("forumSearchInput");
const forumCourseFilter = document.getElementById("forumCourseFilter");
const btnForumUnansweredFilter = document.getElementById("btnForumUnansweredFilter");
const forumNewPostCourse = document.getElementById("forumNewPostCourse");
const forumNewPostInput = document.getElementById("forumNewPostInput");
const btnCreateForumPost = document.getElementById("btnCreateForumPost");
const forumThreadsList = document.getElementById("forumThreadsList");

const announcementsTotalBadge = document.getElementById("announcementsTotalBadge");
const announcementTitleInput = document.getElementById("announcementTitle");
const announcementCourseSelect = document.getElementById("announcementCourse");
const announcementBodyInput = document.getElementById("announcementBody");
const btnPublishAnnouncement = document.getElementById("btnPublishAnnouncement");
const announcementsList = document.getElementById("announcementsList");

const certTotalBadge = document.getElementById("certTotalBadge");
const certSearchInput = document.getElementById("certSearchInput");
const certCourseFilter = document.getElementById("certCourseFilter");
const certificatesList = document.getElementById("certificatesList");

// Modal DOM Elements
const reviewModal = document.getElementById("reviewModal");
const btnCloseModal = document.getElementById("btnCloseModal");
const btnCancelModal = document.getElementById("btnCancelModal");
const modalStudentName = document.getElementById("modalStudentName");
const modalCourseTitle = document.getElementById("modalCourseTitle");
const modalStudentEmail = document.getElementById("modalStudentEmail");
const modalStudentNotes = document.getElementById("modalStudentNotes");
const btnProjectUrl = document.getElementById("btnProjectUrl");
const modalWorkspaceCode = document.getElementById("modalWorkspaceCode");
const modalLivePreview = document.getElementById("modalLivePreview");
const btnTabCode = document.getElementById("btnTabCode");
const btnTabPreview = document.getElementById("btnTabPreview");
const codePane = document.getElementById("codePane");
const previewPane = document.getElementById("previewPane");
const rubricsContainer = document.getElementById("rubricsContainer");
const facilitatorNameInput = document.getElementById("facilitatorName");
const scoreNumber = document.getElementById("scoreNumber");
const scoreSlider = document.getElementById("scoreSlider");
const modalQuizScore = document.getElementById("modalQuizScore");
const calculatedFinalScore = document.getElementById("calculatedFinalScore");
const facilitatorFeedback = document.getElementById("facilitatorFeedback");
const feedbackTags = document.querySelectorAll(".btn-feedback-tag");
const btnRequestRevision = document.getElementById("btnRequestRevision");
const btnApprove = document.getElementById("btnApprove");
const toastMsg = document.getElementById("toastMsg");

// ==========================================
// INITIALIZATION
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
  initFirebaseApp();
  attachEventListeners();
});

function initFirebaseApp() {
  const activeConfig = window.CODEACADEMY_FIREBASE_CONFIG || window.firebaseConfig;
  console.log("[FacilitatorAuth] initFirebaseApp:", {
    hasFirebaseGlobal: typeof firebase !== "undefined",
    hasConfig: Boolean(activeConfig),
    projectId: activeConfig?.projectId
  });
  if (typeof firebase === "undefined" || !activeConfig) {
    showAuthError("SDK Firebase tidak tersedia. Anda tetap dapat mencoba Mode Demo Offline.");
    return;
  }
  try {
    if (!firebase.apps.length) {
      firebase.initializeApp(activeConfig);
    }
    db = firebase.firestore();
    auth = firebase.auth();

    auth.getRedirectResult()
      .then((res) => {
        if (res && res.user) {
          console.log("[FacilitatorAuth] getRedirectResult user:", res.user.email);
        }
      })
      .catch((err) => {
        console.error("[FacilitatorAuth] getRedirectResult error:", err.code, err.message);
        showAuthError("Login Google redirect gagal: " + (err.message || err.code));
      });

    auth.onAuthStateChanged(async (user) => {
      console.log("[FacilitatorAuth] onAuthStateChanged:", {
        isDemoMode,
        hasUser: Boolean(user),
        uid: user?.uid || null,
        email: user?.email || null,
        emailVerified: user?.emailVerified || false
      });
      if (isDemoMode) return;
      if (!user) {
        stopRealtimeListeners();
        showAuthOverlay(true);
        return;
      }
      await verifyAndEnterFacilitatorSession(user);
    });
  } catch (e) {
    console.error("[FacilitatorAuth] Firebase init failed:", e);
    showAuthError("Gagal menginisialisasi Firebase: " + (e.message || e));
  }
}

async function verifyAndEnterFacilitatorSession(user) {
  const email = (user.email || "").trim().toLowerCase();
  console.log("[FacilitatorAuth] verifyAndEnterFacilitatorSession start:", {
    uid: user.uid,
    email,
    emailVerified: user.emailVerified
  });

  if (!email) {
    const msg = "Akun tidak memiliki alamat email yang valid.";
    showAuthError(msg);
    await auth.signOut();
    return;
  }

  try {
    console.log(`[FacilitatorAuth] Reading Firestore allowlist: facilitators/${email}`);
    const allowDoc = await db.collection("facilitators").doc(email).get();
    console.log("[FacilitatorAuth] allowDoc result:", {
      exists: allowDoc.exists,
      data: allowDoc.exists ? allowDoc.data() : null
    });

    if (!allowDoc.exists) {
      const msg = `Akses ditolak: dokumen Firestore facilitators/${email} tidak ditemukan. Minta admin menambahkan dokumen tersebut di Firebase Console.`;
      showAuthError(msg);
      await auth.signOut();
      return;
    }

    if (!user.emailVerified) {
      console.warn("[FacilitatorAuth] Warning: user.emailVerified === false; Firestore write rules require email_verified == true.");
    }

    const profileData = allowDoc.data() || {};
    currentFacilitatorUser = {
      uid: user.uid,
      email,
      displayName: profileData.name || user.displayName || "Fasilitator CodeAcademy"
    };
    isDemoMode = false;
    isFirebaseReady = true;

    console.log("[FacilitatorAuth] Verified! Hiding authOverlay and entering dashboard:", currentFacilitatorUser);
    hideAuthOverlay();
    switchView(activeView || "submissions");
    if (profileData.name || user.displayName) {
      facilitatorNameInput.value = currentFacilitatorUser.displayName;
    }

    modeBadge.textContent = "● Live Firebase";
    modeBadge.className = "badge-mode live";
    facilitatorIdentityChip.textContent = email;
    facilitatorIdentityChip.style.display = "inline-flex";

    renderAllViews();
    startRealtimeListeners();
    showToast(`Masuk sebagai ${email}`);
  } catch (err) {
    console.error("[FacilitatorAuth] Verify facilitator failed:", err.code, err.message, err);
    showAuthError(
      "Gagal memverifikasi izin fasilitator (" + (err.code || err.message || "permission-denied") + "). Pastikan Firestore Rules terbaru sudah di-Publish."
    );
    await auth.signOut();
  }
}

function enterDemoOfflineMode() {
  stopRealtimeListeners();
  isDemoMode = true;
  isFirebaseReady = false;
  currentFacilitatorUser = {
    uid: "demo-facilitator",
    email: "demo-offline@codeacademy.local",
    displayName: "Fasilitator Demo (Offline)"
  };
  hideAuthOverlay();
  modeBadge.textContent = "● Demo Offline";
  modeBadge.className = "badge-mode";
  facilitatorIdentityChip.textContent = "Mode Demo Offline";
  facilitatorIdentityChip.style.display = "inline-flex";
  facilitatorNameInput.value = currentFacilitatorUser.displayName;

  submissions = DEMO_SUBMISSIONS.map((s) => ({ ...s }));
  forumPosts = DEMO_FORUM_POSTS.map((p) => ({ ...p }));
  announcements = DEMO_ANNOUNCEMENTS.map((a) => ({ ...a }));
  certificates = DEMO_CERTIFICATES.map((c) => ({ ...c }));

  renderAllViews();
  showToast("Masuk Mode Demo Offline (perubahan tidak dikirim ke Firebase).");
}

function startRealtimeListeners() {
  stopRealtimeListeners();
  if (!isFirebaseReady || !db) return;

  unsubSubmissions = db
    .collection("project_submissions")
    .orderBy("submittedAt", "desc")
    .onSnapshot(
      (snapshot) => {
        submissions = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
        updateStats();
        renderTable();
      },
      (error) => {
        console.error("Submissions listener error:", error);
        showToast("Gagal sinkronisasi submission: " + error.message);
      }
    );

  unsubForum = db
    .collection("forum_posts")
    .orderBy("timestamp", "desc")
    .onSnapshot(
      (snapshot) => {
        forumPosts = snapshot.docs.map((doc) => ({ postId: doc.id, ...doc.data() }));
        renderForum();
      },
      (error) => {
        console.error("Forum listener error:", error);
        showToast("Gagal memuat forum: " + error.message);
      }
    );

  unsubAnnouncements = db
    .collection("announcements")
    .orderBy("createdAt", "desc")
    .onSnapshot(
      (snapshot) => {
        announcements = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
        renderAnnouncements();
      },
      (error) => {
        console.error("Announcements listener error:", error);
      }
    );

  unsubCertificates = db
    .collection("certificates")
    .orderBy("issuedAt", "desc")
    .onSnapshot(
      (snapshot) => {
        certificates = snapshot.docs.map((doc) => ({ certId: doc.id, ...doc.data() }));
        renderCertificates();
      },
      (error) => {
        console.error("Certificates listener error:", error);
      }
    );
}

function stopRealtimeListeners() {
  if (typeof unsubSubmissions === "function") unsubSubmissions();
  if (typeof unsubForum === "function") unsubForum();
  if (typeof unsubAnnouncements === "function") unsubAnnouncements();
  if (typeof unsubCertificates === "function") unsubCertificates();
  unsubSubmissions = null;
  unsubForum = null;
  unsubAnnouncements = null;
  unsubCertificates = null;
}

function renderAllViews() {
  updateStats();
  renderTable();
  renderForum();
  renderAnnouncements();
  renderCertificates();
}

function showAuthOverlay(preserveError = false) {
  console.log("[FacilitatorAuth] showAuthOverlay", { preserveError });
  authOverlay.classList.remove("hidden");
  authOverlay.style.display = "flex";
  authOverlay.setAttribute("aria-hidden", "false");
  if (!preserveError) {
    authError.classList.remove("show");
  }
  facilitatorIdentityChip.style.display = "none";
}

function hideAuthOverlay() {
  console.log("[FacilitatorAuth] hideAuthOverlay -> dashboard visible");
  authOverlay.classList.add("hidden");
  authOverlay.style.display = "none";
  authOverlay.setAttribute("aria-hidden", "true");
  authError.classList.remove("show");
}

function showAuthError(msg) {
  authError.textContent = msg;
  authError.classList.add("show");
}

// ==========================================
// EVENT LISTENERS
// ==========================================
function attachEventListeners() {
  authForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!auth) {
      showAuthError("Firebase Auth tidak tersedia. Gunakan Mode Demo Offline.");
      return;
    }
    authError.classList.remove("show");
    const email = emailInput.value.trim();
    const password = passwordInput.value;
    try {
      await auth.signInWithEmailAndPassword(email, password);
    } catch (err) {
      console.error("Email sign-in error:", err);
      showAuthError("Gagal masuk: " + (err.message || err.code));
    }
  });

  btnGoogleSignIn.addEventListener("click", async () => {
    console.log("[FacilitatorAuth] Google Sign-In clicked");
    if (!auth) {
      showAuthError("Firebase Auth tidak tersedia. Gunakan Mode Demo Offline.");
      return;
    }
    authError.classList.remove("show");
    try {
      const provider = new firebase.auth.GoogleAuthProvider();
      provider.setCustomParameters({ prompt: "select_account" });
      const cred = await auth.signInWithPopup(provider);
      console.log("[FacilitatorAuth] signInWithPopup resolved:", cred?.user?.email);
    } catch (err) {
      console.error("[FacilitatorAuth] Google sign-in error:", err.code, err.message);
      if (err.code === "auth/popup-blocked") {
        showAuthError("Popup login diblokir browser. Mencoba redirect Google Sign-In...");
        try {
          const provider = new firebase.auth.GoogleAuthProvider();
          await auth.signInWithRedirect(provider);
          return;
        } catch (redirErr) {
          showAuthError("Login Google gagal: " + (redirErr.message || redirErr.code));
        }
      } else {
        showAuthError("Login Google gagal: " + (err.message || err.code));
      }
    }
  });

  btnEnterDemo.addEventListener("click", () => {
    enterDemoOfflineMode();
  });

  btnLogout.addEventListener("click", async () => {
    stopRealtimeListeners();
    isDemoMode = false;
    isFirebaseReady = false;
    currentFacilitatorUser = null;
    if (auth && auth.currentUser) {
      await auth.signOut();
    }
    passwordInput.value = "";
    showAuthOverlay();
  });

  // View Switcher Tabs
  viewTabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      switchView(tab.dataset.view);
    });
  });

  // Submission Search & Filters
  searchInput.addEventListener("input", (e) => {
    searchQuery = e.target.value.toLowerCase();
    renderTable();
  });

  courseFilter.addEventListener("change", (e) => {
    filterCourse = e.target.value;
    renderTable();
  });

  filterPills.forEach((pill) => {
    pill.addEventListener("click", () => {
      filterPills.forEach((p) => p.classList.remove("active"));
      pill.classList.add("active");
      filterStatus = pill.dataset.status;
      renderTable();
    });
  });

  // Event delegation for Submissions Table Review button
  submissionsTableBody.addEventListener("click", (e) => {
    const btn = e.target.closest("[data-action='open-review']");
    if (btn && btn.dataset.id) {
      openReviewModal(btn.dataset.id);
    }
  });

  // Forum Search & Filters
  forumSearchInput.addEventListener("input", (e) => {
    forumSearchQuery = e.target.value.toLowerCase();
    renderForum();
  });

  forumCourseFilter.addEventListener("change", (e) => {
    forumFilterCourse = e.target.value;
    renderForum();
  });

  btnForumUnansweredFilter.addEventListener("click", () => {
    forumOnlyUnanswered = !forumOnlyUnanswered;
    btnForumUnansweredFilter.classList.toggle("active", forumOnlyUnanswered);
    btnForumUnansweredFilter.setAttribute("aria-pressed", String(forumOnlyUnanswered));
    renderForum();
  });

  btnCreateForumPost.addEventListener("click", () => createFacilitatorTopic());

  // Event delegation for Forum Delete & Reply forms
  forumThreadsList.addEventListener("click", (e) => {
    const delBtn = e.target.closest("[data-action='delete-post']");
    if (delBtn && delBtn.dataset.postId) {
      deleteForumPost(delBtn.dataset.postId);
    }
  });

  forumThreadsList.addEventListener("submit", (e) => {
    const form = e.target.closest("form[data-action='reply-thread']");
    if (form && form.dataset.threadId) {
      e.preventDefault();
      replyToThread(form);
    }
  });

  // Announcements
  btnPublishAnnouncement.addEventListener("click", () => publishAnnouncement());
  announcementsList.addEventListener("click", (e) => {
    const delBtn = e.target.closest("[data-action='delete-announcement']");
    if (delBtn && delBtn.dataset.id) {
      deleteAnnouncement(delBtn.dataset.id);
    }
  });

  // Certificates Filters
  certSearchInput.addEventListener("input", (e) => {
    certSearchQuery = e.target.value.toLowerCase();
    renderCertificates();
  });
  certCourseFilter.addEventListener("change", (e) => {
    certFilterCourse = e.target.value;
    renderCertificates();
  });

  // Modal Close & Keyboard Accessibility
  btnCloseModal.addEventListener("click", closeReviewModal);
  btnCancelModal.addEventListener("click", closeReviewModal);
  reviewModal.addEventListener("click", (e) => {
    if (e.target === reviewModal) closeReviewModal();
  });
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && reviewModal.classList.contains("open")) {
      closeReviewModal();
    }
  });

  // Modal Code vs Live Preview Tabs
  btnTabCode.addEventListener("click", () => {
    btnTabCode.classList.add("active");
    btnTabPreview.classList.remove("active");
    codePane.style.display = "block";
    previewPane.style.display = "none";
  });

  btnTabPreview.addEventListener("click", () => {
    btnTabPreview.classList.add("active");
    btnTabCode.classList.remove("active");
    previewPane.style.display = "block";
    codePane.style.display = "none";
  });

  // Score Slider & Number Input Sync
  scoreSlider.addEventListener("input", (e) => {
    scoreNumber.value = e.target.value;
    recalculateFinalScore();
  });

  scoreNumber.addEventListener("input", (e) => {
    let val = parseInt(e.target.value, 10);
    if (isNaN(val)) return;
    val = Math.max(0, Math.min(100, val));
    scoreSlider.value = String(val);
    recalculateFinalScore();
  });

  scoreNumber.addEventListener("blur", (e) => {
    let val = parseInt(e.target.value, 10);
    if (isNaN(val)) val = parseInt(scoreSlider.value, 10) || 0;
    val = Math.max(0, Math.min(100, val));
    e.target.value = String(val);
    scoreSlider.value = String(val);
    recalculateFinalScore();
  });

  // Quick Feedback Tags
  feedbackTags.forEach((btn) => {
    btn.addEventListener("click", () => {
      const txt = btn.dataset.text || "";
      if (!facilitatorFeedback.value.includes(txt)) {
        facilitatorFeedback.value = (facilitatorFeedback.value ? facilitatorFeedback.value + "\n" : "") + txt;
      }
    });
  });

  // Actions: Revisi & Approve
  btnRequestRevision.addEventListener("click", () => executeReview("REVISION_REQUESTED"));
  btnApprove.addEventListener("click", () => executeReview("APPROVED"));
}

function switchView(view) {
  activeView = view;
  viewTabs.forEach((tab) => {
    const isActive = tab.dataset.view === view;
    tab.classList.toggle("active", isActive);
    tab.setAttribute("aria-selected", String(isActive));
  });

  submissionsView.style.display = view === "submissions" ? "block" : "none";
  forumView.style.display = view === "forum" ? "block" : "none";
  announcementsView.style.display = view === "announcements" ? "block" : "none";
  certificatesView.style.display = view === "certificates" ? "block" : "none";

  if (view === "submissions") {
    pageEyebrow.textContent = "Evaluasi Proyek Akhir";
    pageTitle.textContent = "Antrean Penilaian Peserta";
    pageSubtitle.textContent = "Tinjau kode workspace, cek pratinjau langsung, beri skor rubrik (40% Kuis + 60% Proyek), dan terbitkan sertifikat.";
  } else if (view === "forum") {
    pageEyebrow.textContent = "Moderasi & Bimbingan";
    pageTitle.textContent = "Forum Diskusi Kelas";
    pageSubtitle.textContent = "Jawab pertanyaan peserta secara real-time dengan lencana Fasilitator, atau moderasi diskusi yang kurang sesuai.";
  } else if (view === "announcements") {
    pageEyebrow.textContent = "Broadcast Informasi";
    pageTitle.textContent = "Pengumuman Kelas ke Aplikasi";
    pageSubtitle.textContent = "Terbitkan pengumuman penting yang langsung muncul di tab Notifikasi seluruh peserta Android.";
  } else if (view === "certificates") {
    pageEyebrow.textContent = "Registri Kelulusan";
    pageTitle.textContent = "Sertifikat Terbit";
    pageSubtitle.textContent = "Pantau sertifikat berformat CC-... yang terverifikasi secara publik di Portal Verifikasi.";
  }
}

// ==========================================
// SUBMISSIONS STATS & TABLE
// ==========================================
function updateStats() {
  let total = submissions.length;
  let pending = 0;
  let revision = 0;
  let approved = 0;

  submissions.forEach((s) => {
    const st = (s.status || "").toUpperCase();
    if (st === "PENDING_REVIEW") pending++;
    else if (st === "REVISION_REQUESTED") revision++;
    else if (st === "APPROVED") approved++;
  });

  statTotal.textContent = String(total);
  statPending.textContent = String(pending);
  statRevision.textContent = String(revision);
  statApproved.textContent = String(approved);
  submissionsPendingBadge.textContent = String(pending);
}

function renderTable() {
  const filtered = submissions.filter((s) => {
    const st = (s.status || "").toUpperCase();
    const matchStatus = filterStatus === "all" || st === filterStatus;
    const matchCourse = filterCourse === "all" || s.courseId === filterCourse;
    const courseLabel = getCourseLabel(s.courseId).toLowerCase();
    const displayName = (s.studentName || s.userName || "").toLowerCase();
    const displayEmail = (s.studentEmail || s.userEmail || "").toLowerCase();
    const textMatch =
      !searchQuery ||
      displayName.includes(searchQuery) ||
      displayEmail.includes(searchQuery) ||
      (s.courseTitle || "").toLowerCase().includes(searchQuery) ||
      courseLabel.includes(searchQuery);
    return matchStatus && matchCourse && textMatch;
  });

  if (filtered.length === 0) {
    submissionsTableBody.innerHTML = `
      <tr>
        <td colspan="7">
          <div class="empty-state">
            <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/><polyline points="13 2 13 9 20 9"/></svg>
            <h4>Tidak ada pengajuan proyek ditemukan</h4>
            <p>Ubah filter pencarian atau tunggu peserta mengirimkan proyek dari aplikasi Android.</p>
          </div>
        </td>
      </tr>
    `;
    return;
  }

  submissionsTableBody.innerHTML = filtered
    .map((s) => {
      const badgeClass = getStatusBadgeClass(s.status);
      const badgeLabel = getStatusLabel(s.status);
      const dateStr = formatDate(s.submittedAt);
      const courseName = s.courseTitle || getCourseLabel(s.courseId);
      const studentName = s.studentName || s.userName || "Peserta";
      const studentEmail = s.studentEmail || s.userEmail || s.accountId || "-";
      const attempt = Number(s.attemptNumber ?? s.attemptCount) || 1;

      return `
      <tr>
        <td>
          <div class="student-info">
            <span class="student-name">${escapeHtml(studentName)}</span>
            <span class="student-email">${escapeHtml(studentEmail)}</span>
          </div>
        </td>
        <td>
          <span class="course-title">${escapeHtml(courseName)}</span>
        </td>
        <td>
          <strong>${Number(s.finalQuizScore) || 0}</strong>/100
        </td>
        <td>
          <span class="status-badge ${badgeClass}">${badgeLabel}</span>
        </td>
        <td style="font-size: 12.5px; color: var(--muted);">${escapeHtml(dateStr)}</td>
        <td style="font-size: 12.5px; color: var(--muted);">Ke-${attempt}</td>
        <td style="text-align: right;">
          <button type="button" class="btn-review" data-action="open-review" data-id="${escapeHtml(s.id)}">
            ${s.status === "PENDING_REVIEW" ? "Nilai Sekarang" : "Lihat / Ubah"}
          </button>
        </td>
      </tr>
    `;
    })
    .join("");
}

function getStatusBadgeClass(status) {
  const st = (status || "").toUpperCase();
  if (st === "PENDING_REVIEW") return "pending";
  if (st === "REVISION_REQUESTED") return "revision";
  if (st === "APPROVED") return "approved";
  return "";
}

function getStatusLabel(status) {
  const st = (status || "").toUpperCase();
  if (st === "PENDING_REVIEW") return "Menunggu Review";
  if (st === "REVISION_REQUESTED") return "Perlu Revisi";
  if (st === "APPROVED") return "Disetujui";
  return st || "-";
}

// ==========================================
// REVIEW MODAL LOGIC
// ==========================================
function openReviewModal(submissionId) {
  currentSubmission = submissions.find((s) => s.id === submissionId);
  if (!currentSubmission) return;

  const studentName = currentSubmission.studentName || currentSubmission.userName || "Peserta";
  const studentEmail = currentSubmission.studentEmail || currentSubmission.userEmail || currentSubmission.accountId || "-";
  modalStudentName.textContent = studentName;
  const courseName = currentSubmission.courseTitle || getCourseLabel(currentSubmission.courseId);
  modalCourseTitle.textContent = `${courseName} • ID: ${currentSubmission.id}`;
  modalStudentEmail.textContent = studentEmail;
  modalStudentNotes.textContent =
    currentSubmission.studentNotes || currentSubmission.projectNotes || "Tidak ada catatan khusus dari peserta.";

  const safeProjectUrl = sanitizeExternalUrl(currentSubmission.projectUrl);
  if (safeProjectUrl) {
    btnProjectUrl.href = safeProjectUrl;
    btnProjectUrl.style.display = "inline-block";
  } else {
    btnProjectUrl.href = "#";
    btnProjectUrl.style.display = "none";
  }

  const code = currentSubmission.workspaceCode || "<!-- Tidak ada kode HTML/CSS/JS yang dilampirkan -->";
  modalWorkspaceCode.textContent = code;

  // Inject strict CSP into sandboxed iframe srcdoc
  const previewCsp =
    '<meta http-equiv="Content-Security-Policy" content="default-src \'none\'; style-src \'unsafe-inline\'; script-src \'unsafe-inline\'; img-src data: https:;">';
  modalLivePreview.srcdoc = previewCsp + "\n" + code;

  btnTabCode.click();

  // Render Rubric Checkboxes & pre-populate from checkedRubricIdsJson
  let savedCheckedIds = [];
  if (currentSubmission.checkedRubricIdsJson) {
    try {
      const parsed = JSON.parse(currentSubmission.checkedRubricIdsJson);
      if (Array.isArray(parsed)) savedCheckedIds = parsed;
    } catch (_) {
      savedCheckedIds = [];
    }
  }
  const hasSavedRubrics = savedCheckedIds.length > 0;
  const rubrics = getRubricsForCourse(currentSubmission.courseId);

  rubricsContainer.innerHTML = rubrics
    .map((r) => {
      const isChecked = hasSavedRubrics ? savedCheckedIds.includes(r.id) : true;
      return `
    <label class="rubric-item">
      <input type="checkbox" class="rubric-checkbox" data-weight="${r.weight}" value="${escapeHtml(r.id)}" ${isChecked ? "checked" : ""}>
      <div class="rubric-info">
        <h5>${escapeHtml(r.title)} (${r.weight}%)</h5>
        <p>${escapeHtml(r.desc)}</p>
      </div>
    </label>
  `;
    })
    .join("");

  document.querySelectorAll(".rubric-checkbox").forEach((cb) => {
    cb.addEventListener("change", calculateScoreFromRubrics);
  });

  const initialProjScore =
    currentSubmission.facilitatorScore !== null && currentSubmission.facilitatorScore !== undefined
      ? Number(currentSubmission.facilitatorScore)
      : 85;
  scoreSlider.value = String(initialProjScore);
  scoreNumber.value = String(initialProjScore);

  modalQuizScore.textContent = String(Number(currentSubmission.finalQuizScore) || 0);
  recalculateFinalScore();

  facilitatorFeedback.value = currentSubmission.facilitatorFeedback || currentSubmission.feedback || "";
  if (currentSubmission.facilitatorName) {
    facilitatorNameInput.value = currentSubmission.facilitatorName;
  } else if (currentFacilitatorUser?.displayName) {
    facilitatorNameInput.value = currentFacilitatorUser.displayName;
  }

  reviewModal.classList.add("open");
}

function closeReviewModal() {
  reviewModal.classList.remove("open");
  currentSubmission = null;
}

function calculateScoreFromRubrics() {
  let totalCheckedWeight = 0;
  document.querySelectorAll(".rubric-checkbox").forEach((cb) => {
    if (cb.checked) totalCheckedWeight += parseInt(cb.dataset.weight || "0", 10);
  });
  scoreSlider.value = String(totalCheckedWeight);
  scoreNumber.value = String(totalCheckedWeight);
  recalculateFinalScore();
}

function recalculateFinalScore() {
  const quiz = parseInt(modalQuizScore.textContent || "0", 10) || 0;
  const proj = Math.max(0, Math.min(100, parseInt(scoreSlider.value || "0", 10) || 0));
  const final = Math.round(quiz * 0.4 + proj * 0.6);
  calculatedFinalScore.textContent = String(final);
  calculatedFinalScore.style.color = final >= 70 ? "var(--teal-deep)" : "var(--bad)";
  return final;
}

async function executeReview(newStatus) {
  if (!currentSubmission) return;

  const projScore = Math.max(0, Math.min(100, parseInt(scoreSlider.value || "0", 10) || 0));
  const finalScore = recalculateFinalScore();
  const feedback = facilitatorFeedback.value.trim();
  const facName = facilitatorNameInput.value.trim() || "Tim Fasilitator CodeAcademy";

  if (newStatus === "REVISION_REQUESTED" && feedback.length < 5) {
    alert("Mohon tuliskan catatan evaluasi yang jelas agar peserta mengetahui hal yang harus diperbaiki.");
    facilitatorFeedback.focus();
    return;
  }

  if (newStatus === "APPROVED" && finalScore < 70) {
    alert(`Nilai Akhir saat ini ${finalScore}/100 (minimal 70 untuk lulus & menerbitkan sertifikat). Tingkatkan skor proyek atau minta revisi.`);
    return;
  }

  const checkedIds = Array.from(document.querySelectorAll(".rubric-checkbox"))
    .filter((cb) => cb.checked)
    .map((cb) => cb.value);

  const nowTs = Date.now();
  const existingCertId = currentSubmission.certificateId || "";
  const certId = newStatus === "APPROVED" ? existingCertId || generateCertificateId() : existingCertId;

  const finalFeedbackText =
    feedback ||
    (newStatus === "APPROVED" ? "Proyek memenuhi standar kelulusan CodeAcademy." : "");
  const payload = {
    status: newStatus,
    facilitatorScore: projScore,
    facilitatorFeedback: finalFeedbackText,
    feedback: finalFeedbackText,
    facilitatorName: facName,
    checkedRubricIdsJson: JSON.stringify(checkedIds),
    reviewedAt: nowTs,
    updatedAt: nowTs
  };
  if (certId) {
    payload.certificateId = certId;
  }

  btnApprove.disabled = true;
  btnRequestRevision.disabled = true;

  try {
    const courseTitle = currentSubmission.courseTitle || getCourseLabel(currentSubmission.courseId);
    const studentName = currentSubmission.studentName || currentSubmission.userName || "Peserta CodeAcademy";
    const certPayload =
      newStatus === "APPROVED"
        ? {
            certId,
            accountId: currentSubmission.accountId || currentSubmission.id,
            firebaseUid: currentSubmission.firebaseUid || "",
            userName: studentName,
            courseId: currentSubmission.courseId,
            courseTitle,
            quizScore: Number(currentSubmission.finalQuizScore) || 0,
            facilitatorScore: projScore,
            finalScore,
            issuedAt: nowTs,
            facilitatorName: facName
          }
        : null;

    if (isFirebaseReady && db && !isDemoMode) {
      const batch = db.batch();
      const subRef = db.collection("project_submissions").doc(currentSubmission.id);
      batch.update(subRef, payload);

      if (certPayload) {
        const certRef = db.collection("certificates").doc(certId);
        batch.set(certRef, certPayload, { merge: true });
      }
      await batch.commit();
    } else {
      const idx = submissions.findIndex((s) => s.id === currentSubmission.id);
      if (idx !== -1) {
        submissions[idx] = { ...submissions[idx], ...payload };
      }
      if (certPayload) {
        const existingIdx = certificates.findIndex((c) => c.certId === certId);
        if (existingIdx !== -1) certificates[existingIdx] = certPayload;
        else certificates.unshift(certPayload);
        renderCertificates();
      }
      updateStats();
      renderTable();
    }

    const msg =
      newStatus === "APPROVED"
        ? `Proyek ${studentName} Disetujui! Sertifikat ${certId} diterbitkan.`
        : `Permintaan Revisi dikirim ke ${studentName}.`;

    closeReviewModal();
    showToast(msg);
  } catch (err) {
    console.error("Failed to save review:", err);
    showToast("Gagal menyimpan evaluasi: " + err.message);
  } finally {
    btnApprove.disabled = false;
    btnRequestRevision.disabled = false;
  }
}

// ==========================================
// FORUM DISKUSI VIEW & MODERATION
// ==========================================
function renderForum() {
  const roots = forumPosts.filter((p) => !p.replyToPostId && !p.rootThreadId);
  const repliesByRoot = new Map();
  forumPosts.forEach((p) => {
    const key = p.rootThreadId || p.replyToPostId;
    if (!key) return;
    if (!repliesByRoot.has(key)) repliesByRoot.set(key, []);
    repliesByRoot.get(key).push(p);
  });

  if (forumTotalBadge) {
    forumTotalBadge.textContent = String(roots.length);
  }

  const filtered = roots.filter((p) => {
    const matchCourse = forumFilterCourse === "all" || p.courseId === forumFilterCourse;
    const matchQuery =
      !forumSearchQuery ||
      (p.content || "").toLowerCase().includes(forumSearchQuery) ||
      (p.authorName || "").toLowerCase().includes(forumSearchQuery) ||
      getCourseLabel(p.courseId).toLowerCase().includes(forumSearchQuery);
    const threadReplies = repliesByRoot.get(p.postId) || [];
    const isMentorFlag = (post) => post.authorIsMentor === true || post.isMentor === true;
    const hasMentorReply = isMentorFlag(p) || threadReplies.some(isMentorFlag);
    const matchUnanswered = !forumOnlyUnanswered || !hasMentorReply;
    return matchCourse && matchQuery && matchUnanswered;
  });

  if (filtered.length === 0) {
    forumThreadsList.innerHTML = `
      <div class="forum-empty">
        <p>${forumOnlyUnanswered ? "Semua pertanyaan diskusi sudah mendapat jawaban Fasilitator." : "Belum ada topik diskusi yang sesuai dengan filter ini."}</p>
      </div>
    `;
    return;
  }

  forumThreadsList.innerHTML = filtered
    .map((thread) => {
      const replies = (repliesByRoot.get(thread.postId) || []).slice().sort((a, b) => a.timestamp - b.timestamp);
      const initial = (thread.authorName || "P").trim().charAt(0).toUpperCase();
      const courseName = getCourseLabel(thread.courseId);

      const repliesHtml = replies
        .map((r) => {
          const rInitial = (r.authorName || "P").trim().charAt(0).toUpperCase();
          const rIsMentor = r.authorIsMentor === true || r.isMentor === true;
          return `
        <div class="forum-reply-card ${rIsMentor ? "mentor" : ""}">
          <div class="forum-post-head" style="margin-bottom: 6px;">
            <div class="forum-author-meta">
              <div class="forum-avatar ${rIsMentor ? "mentor" : ""}" style="width:26px;height:26px;font-size:11px;">${escapeHtml(rInitial)}</div>
              <div>
                <strong style="font-size:13px;">${escapeHtml(r.authorName || "Peserta")}</strong>
                ${rIsMentor ? '<span class="forum-mentor-badge">Fasilitator</span>' : ""}
                <span style="font-size:11.5px; color:var(--muted); margin-left:6px;">${escapeHtml(formatDate(r.timestamp))}</span>
              </div>
            </div>
            <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(r.postId)}">Hapus</button>
          </div>
          <div style="font-size:13.5px; line-height:1.5; white-space:pre-wrap;">${escapeHtml(r.content || "")}</div>
        </div>
      `;
        })
        .join("");

      const tIsMentor = thread.authorIsMentor === true || thread.isMentor === true;
      return `
      <article class="forum-thread-card">
        <div class="forum-post-head">
          <div class="forum-author-meta">
            <div class="forum-avatar ${tIsMentor ? "mentor" : ""}">${escapeHtml(initial)}</div>
            <div>
              <strong>${escapeHtml(thread.authorName || "Peserta")}</strong>
              ${tIsMentor ? '<span class="forum-mentor-badge">Fasilitator</span>' : `<span style="font-size:11.5px;color:var(--muted);">• Lv.${Number(thread.authorLevel) || 1}</span>`}
              <span class="forum-course-pill">${escapeHtml(courseName)}</span>
              <div style="font-size:12px; color:var(--muted);">${escapeHtml(formatDate(thread.timestamp))} • ${Number(thread.likeCount) || 0} suka • ${replies.length} balasan</div>
            </div>
          </div>
          <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(thread.postId)}">Hapus Thread</button>
        </div>
        <div class="forum-post-body">${escapeHtml(thread.content || "")}</div>
        ${replies.length > 0 ? `<div class="forum-replies-wrap">${repliesHtml}</div>` : ""}
        <form class="forum-reply-form" data-action="reply-thread" data-thread-id="${escapeHtml(thread.postId)}">
          <input type="text" placeholder="Balas sebagai Fasilitator (muncul langsung di aplikasi)..." required>
          <button type="submit" class="btn-primary-solid">Balas</button>
        </form>
      </article>
    `;
    })
    .join("");
}

async function createFacilitatorTopic() {
  const content = (forumNewPostInput.value || "").trim();
  const courseId = forumNewPostCourse.value || "fe-web-foundation-00";
  if (content.length < 4) {
    showToast("Tulis isi topik diskusi minimal 4 karakter.");
    return;
  }

  const facName = facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || "Tim Fasilitator CodeAcademy";
  const postId = "fac_" + Date.now() + "_" + Math.random().toString(36).slice(2, 7);
  const uid = currentFacilitatorUser?.uid || "facilitator_web";

  const docData = {
    postId,
    courseId,
    firebaseUid: uid,
    authorId: "facilitator_" + facName.toLowerCase().replace(/[^a-z0-9]/g, "_"),
    authorName: facName,
    authorLevel: 99,
    authorIsMentor: true,
    isMentor: true,
    content,
    timestamp: Date.now(),
    likeCount: 0,
    likedByAccountIds: [],
    replyToPostId: null,
    replyToAuthorName: null,
    replyToContentSnippet: null,
    rootThreadId: null
  };

  if (isFirebaseReady && db && !isDemoMode) {
    try {
      await db.collection("forum_posts").doc(postId).set(docData);
      forumNewPostInput.value = "";
      showToast("Topik fasilitator berhasil diterbitkan ke aplikasi.");
    } catch (err) {
      console.error("Create topic error:", err);
      showToast("Gagal membuat topik: " + err.message);
    }
  } else {
    forumPosts.unshift(docData);
    forumNewPostInput.value = "";
    renderForum();
    showToast("Topik ditambahkan (Mode Demo Offline).");
  }
}

async function replyToThread(form) {
  const rootPostId = form.dataset.threadId;
  const input = form.querySelector("input");
  const content = (input?.value || "").trim();
  if (!content) return;

  const root = forumPosts.find((p) => p.postId === rootPostId);
  if (!root) return;

  const facName = facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || "Tim Fasilitator CodeAcademy";
  const postId = "fac_reply_" + Date.now() + "_" + Math.random().toString(36).slice(2, 7);
  const uid = currentFacilitatorUser?.uid || "facilitator_web";

  const replyDoc = {
    postId,
    courseId: root.courseId,
    firebaseUid: uid,
    authorId: "facilitator_" + facName.toLowerCase().replace(/[^a-z0-9]/g, "_"),
    authorName: facName,
    authorLevel: 99,
    authorIsMentor: true,
    isMentor: true,
    content,
    timestamp: Date.now(),
    likeCount: 0,
    likedByAccountIds: [],
    replyToPostId: root.postId,
    replyToAuthorName: root.authorName || "Peserta",
    replyToContentSnippet: (root.content || "").slice(0, 80),
    rootThreadId: root.rootThreadId || root.postId
  };

  if (isFirebaseReady && db && !isDemoMode) {
    try {
      await db.collection("forum_posts").doc(postId).set(replyDoc);
      input.value = "";
      showToast("Balasan fasilitator terkirim.");
    } catch (err) {
      console.error("Reply thread error:", err);
      showToast("Gagal mengirim balasan: " + err.message);
    }
  } else {
    forumPosts.push(replyDoc);
    input.value = "";
    renderForum();
    showToast("Balasan ditambahkan (Mode Demo Offline).");
  }
}

async function deleteForumPost(postId) {
  if (!confirm("Hapus postingan/thread diskusi ini secara permanen?")) return;

  // Kumpulkan target dan seluruh balasan turunannya secara rekursif
  const toDeleteIds = new Set([postId]);
  let expanded = true;
  while (expanded) {
    expanded = false;
    forumPosts.forEach((p) => {
      if (!toDeleteIds.has(p.postId)) {
        if (toDeleteIds.has(p.replyToPostId) || toDeleteIds.has(p.rootThreadId)) {
          toDeleteIds.add(p.postId);
          expanded = true;
        }
      }
    });
  }

  if (isFirebaseReady && db && !isDemoMode) {
    try {
      const ids = Array.from(toDeleteIds);
      for (let i = 0; i < ids.length; i += 400) {
        const batch = db.batch();
        ids.slice(i, i + 400).forEach((id) => {
          batch.delete(db.collection("forum_posts").doc(id));
        });
        await batch.commit();
      }
      showToast(`Postingan (${ids.length} item) berhasil dihapus.`);
    } catch (err) {
      console.error("Delete post error:", err);
      showToast("Gagal menghapus: " + err.message);
    }
  } else {
    forumPosts = forumPosts.filter((p) => !toDeleteIds.has(p.postId));
    renderForum();
    showToast("Postingan dihapus (Mode Demo Offline).");
  }
}

// ==========================================
// ANNOUNCEMENTS VIEW
// ==========================================
function renderAnnouncements() {
  if (announcementsTotalBadge) {
    announcementsTotalBadge.textContent = String(announcements.length);
  }
  if (announcements.length === 0) {
    announcementsList.innerHTML = `<div class="forum-empty"><p>Belum ada pengumuman yang diterbitkan.</p></div>`;
    return;
  }

  announcementsList.innerHTML = announcements
    .map((ann) => {
      const courseLabel = ann.courseId && ann.courseId !== "all" ? getCourseLabel(ann.courseId) : "Semua Kelas / Umum";
      return `
      <article class="announcement-item">
        <div style="flex: 1; min-width: 240px;">
          <div style="display:flex; align-items:center; gap:8px; flex-wrap:wrap; margin-bottom:6px;">
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(ann.title || "Pengumuman")}</strong>
            <span class="forum-course-pill">${escapeHtml(courseLabel)}</span>
          </div>
          <p style="font-size:13.5px; color:var(--ink); line-height:1.5; white-space:pre-wrap;">${escapeHtml(ann.body || "")}</p>
          <div style="font-size:12px; color:var(--muted); margin-top:8px;">
            Oleh ${escapeHtml(ann.authorName || "Fasilitator")} • ${escapeHtml(formatDate(ann.createdAt))}
          </div>
        </div>
        <button type="button" class="btn-danger-ghost" data-action="delete-announcement" data-id="${escapeHtml(ann.id)}">Hapus</button>
      </article>
    `;
    })
    .join("");
}

async function publishAnnouncement() {
  const title = (announcementTitleInput.value || "").trim();
  const body = (announcementBodyInput.value || "").trim();
  const courseId = announcementCourseSelect.value || "all";

  if (title.length < 3 || body.length < 5) {
    showToast("Isi judul dan pesan pengumuman dengan lengkap.");
    return;
  }

  const annId = "ann_" + Date.now() + "_" + Math.random().toString(36).slice(2, 6);
  const docData = {
    id: annId,
    title,
    body,
    courseId,
    courseTitle: courseId === "all" ? "Semua Kelas / Umum" : getCourseLabel(courseId),
    authorName: facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || "Tim Fasilitator CodeAcademy",
    authorEmail: currentFacilitatorUser?.email || "",
    firebaseUid: currentFacilitatorUser?.uid || "",
    createdAt: Date.now()
  };

  if (isFirebaseReady && db && !isDemoMode) {
    try {
      await db.collection("announcements").doc(annId).set(docData);
      announcementTitleInput.value = "";
      announcementBodyInput.value = "";
      showToast("Pengumuman berhasil diterbitkan ke aplikasi Android.");
    } catch (err) {
      console.error("Publish announcement error:", err);
      showToast("Gagal menerbitkan pengumuman: " + err.message);
    }
  } else {
    announcements.unshift(docData);
    announcementTitleInput.value = "";
    announcementBodyInput.value = "";
    renderAnnouncements();
    showToast("Pengumuman ditambahkan (Mode Demo Offline).");
  }
}

async function deleteAnnouncement(id) {
  if (!confirm("Hapus pengumuman ini?")) return;
  if (isFirebaseReady && db && !isDemoMode) {
    try {
      await db.collection("announcements").doc(id).delete();
      showToast("Pengumuman dihapus.");
    } catch (err) {
      console.error("Delete announcement error:", err);
      showToast("Gagal menghapus pengumuman: " + err.message);
    }
  } else {
    announcements = announcements.filter((a) => a.id !== id);
    renderAnnouncements();
    showToast("Pengumuman dihapus (Mode Demo Offline).");
  }
}

// ==========================================
// CERTIFICATES REGISTRY VIEW
// ==========================================
function renderCertificates() {
  if (certTotalBadge) {
    certTotalBadge.textContent = String(certificates.length);
  }

  const filtered = certificates.filter((c) => {
    const matchCourse = certFilterCourse === "all" || c.courseId === certFilterCourse;
    const textMatch =
      !certSearchQuery ||
      (c.certId || "").toLowerCase().includes(certSearchQuery) ||
      (c.userName || "").toLowerCase().includes(certSearchQuery) ||
      (c.courseTitle || "").toLowerCase().includes(certSearchQuery);
    return matchCourse && textMatch;
  });

  if (filtered.length === 0) {
    certificatesList.innerHTML = `<div class="forum-empty"><p>Belum ada sertifikat yang sesuai filter.</p></div>`;
    return;
  }

  certificatesList.innerHTML = filtered
    .map((c) => {
      const courseName = c.courseTitle || getCourseLabel(c.courseId);
      const verifyHref = `../verify.html?id=${encodeURIComponent(c.certId || "")}`;
      return `
      <article class="cert-item">
        <div style="flex:1; min-width:240px;">
          <div style="display:flex; align-items:center; gap:10px; flex-wrap:wrap; margin-bottom:6px;">
            <span class="cert-id-badge">${escapeHtml(c.certId || "-")}</span>
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(c.userName || "Peserta")}</strong>
          </div>
          <div style="font-size:13.5px; color:var(--ink); font-weight:600;">${escapeHtml(courseName)}</div>
          <div style="font-size:12px; color:var(--muted); margin-top:4px;">
            Nilai Akhir: <strong>${Number(c.finalScore) || 0}/100</strong> (Kuis ${Number(c.quizScore) || 0} • Proyek ${Number(c.facilitatorScore) || 0}) • Terbit ${escapeHtml(formatDate(c.issuedAt))}
          </div>
        </div>
        <a href="${verifyHref}" target="_blank" rel="noopener noreferrer" class="btn-review" style="text-decoration:none;">
          Buka Halaman Verifikasi ↗
        </a>
      </article>
    `;
    })
    .join("");
}

// ==========================================
// HELPERS & UTILITIES
// ==========================================
function generateCertificateId() {
  const now = new Date();
  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, "0");
  const numPart = String(Math.floor(1000 + Math.random() * 9000));
  const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  let suffix = "";
  for (let i = 0; i < 6; i++) {
    suffix += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return `CC-${yyyy}${mm}${numPart}-${suffix}`;
}

function sanitizeExternalUrl(raw) {
  if (!raw || typeof raw !== "string") return null;
  try {
    const parsed = new URL(raw.trim());
    if (parsed.protocol === "http:" || parsed.protocol === "https:") {
      return parsed.href;
    }
    return null;
  } catch (_) {
    return null;
  }
}

function escapeHtml(str) {
  if (str === null || str === undefined) return "";
  return String(str).replace(/[&<>"'`/]/g, (tag) => {
    const map = {
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#39;",
      "`": "&#96;",
      "/": "&#x2F;"
    };
    return map[tag] || tag;
  });
}

function formatDate(timestamp) {
  if (!timestamp) return "-";
  const d = new Date(timestamp);
  if (isNaN(d.getTime())) return "-";
  return d.toLocaleDateString("id-ID", {
    day: "numeric",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
}

function showToast(msg) {
  toastMsg.textContent = msg;
  toastMsg.classList.add("show");
  setTimeout(() => {
    toastMsg.classList.remove("show");
  }, 3500);
}
