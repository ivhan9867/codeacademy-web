// ==========================================
// CodeAcademy - Staff Portal App Logic
// Real-time Firestore Sync & Firebase Auth
// ==========================================

function isIndonesian() {
  return window.CodeAcademyWebI18n ? window.CodeAcademyWebI18n.getLang() === "id" : false;
}

function L(enText, idText) {
  return isIndonesian() ? idText : enText;
}

const COURSE_LABELS_EN = {
  "fe-web-foundation-00": "Web Foundation: From Zero to First Code",
  "fe-css-02": "Modern CSS Layout: Flexbox & Grid",
  "fe-js-03": "JavaScript Fundamentals",
  "web-html-01": "Web Foundation & Semantic HTML5",
  "web-css-01": "Modern CSS3 & Flexbox/Grid",
  "web-js-01": "JavaScript ES6 & DOM"
};

const COURSE_LABELS_ID = {
  "fe-web-foundation-00": "Fondasi Web: Dari Nol ke Baris Kode Pertama",
  "fe-css-02": "CSS Layout Modern: Flexbox & Grid",
  "fe-js-03": "JavaScript Fundamentals",
  "web-html-01": "Fondasi Web & HTML5 Semantik",
  "web-css-01": "CSS3 Modern & Flexbox/Grid",
  "web-js-01": "JavaScript ES6 & DOM"
};

function getCourseLabel(courseId) {
  const map = isIndonesian() ? COURSE_LABELS_ID : COURSE_LABELS_EN;
  return map[courseId] || courseId || L("WebDev Course", "Kursus WebDev");
}

const RUBRICS_BY_COURSE_EN = {
  "fe-web-foundation-00": [
    { id: "semantik", title: "Semantic HTML5 Structure", desc: "Uses header, nav, main, section, and footer with clear document hierarchy.", weight: 30 },
    { id: "aksesibilitas", title: "Accessibility & Metadata", desc: "Includes proper title, meta viewport, form labels, and image alt attributes.", weight: 25 },
    { id: "styling", title: "Visual Presentation & Readability", desc: "Legible typography, solid color contrast, and well-proportioned spacing.", weight: 25 },
    { id: "clean", title: "Code Cleanliness & Validity", desc: "Properly closed tags, consistent indentation, and no deprecated elements.", weight: 20 }
  ],
  "fe-css-02": [
    { id: "layout", title: "Flexbox & CSS Grid Mastery", desc: "Builds modern row/column layouts cleanly without float or table hacks.", weight: 35 },
    { id: "responsif", title: "Responsive Design (Mobile-First)", desc: "Layout adapts smoothly across mobile and desktop viewports.", weight: 25 },
    { id: "tokens", title: "CSS Custom Properties & Consistency", desc: "Uses color/spacing custom properties with consistent visual rhythm.", weight: 20 },
    { id: "polish", title: "Visual Polish, Hover States & Clean CSS", desc: "Smooth interactions, accessible contrast, and organized stylesheet structure.", weight: 20 }
  ],
  "fe-js-03": [
    { id: "dom", title: "DOM Manipulation & Event Listeners", desc: "Dynamically updates page elements in response to user interactions.", weight: 35 },
    { id: "logika", title: "Program Logic & Input Validation", desc: "Conditionals, functions, and empty/invalid input handling work accurately.", weight: 30 },
    { id: "state", title: "Data Management (Arrays & Objects)", desc: "Clean data handling with modern array methods (map, filter, find).", weight: 20 },
    { id: "clean", title: "Code Clarity & Zero Runtime Errors", desc: "Readable code that executes cleanly with no Console errors.", weight: 15 }
  ]
};

const RUBRICS_BY_COURSE_ID = {
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
  const bank = isIndonesian() ? RUBRICS_BY_COURSE_ID : RUBRICS_BY_COURSE_EN;
  return bank[courseId] || bank["fe-web-foundation-00"];
}

// ==========================================
// STATE APPLICATION
// ==========================================
let db = null;
let auth = null;
let isFirebaseReady = false;
let currentFacilitatorUser = null;
let currentAccessRole = "facilitator";
let unsubSubmissions = null;
let unsubForum = null;
let unsubAnnouncements = null;
let unsubCertificates = null;
let unsubTeam = null;
let unsubStudents = null;
let unsubRatings = null;

let submissions = [];
let forumPosts = [];
let announcements = [];
let certificates = [];
let facilitatorsList = [];
let studentUsersList = [];
let submissionRatings = [];

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
const authStatusEl = document.getElementById("authStatus");
const authError = document.getElementById("authError");
let verifyingSessionPromise = null;
const activeReplyTargetByThread = new Map();
const PROFILE_STORAGE_KEY = "ca_fac_profile_v1";
const ACCESS_REQUEST_RECIPIENT = "ivhanafikaprila@gmail.com";

function loadSavedFacilitatorProfile() {
  try {
    const raw = localStorage.getItem(PROFILE_STORAGE_KEY);
    return raw ? JSON.parse(raw) : {};
  } catch {
    return {};
  }
}

function saveLocalFacilitatorProfile(profile) {
  try {
    localStorage.setItem(PROFILE_STORAGE_KEY, JSON.stringify(profile));
  } catch {
    // ignore storage errors
  }
}

function updateHuddlyGreeting(displayName) {
  const greetingEl = document.getElementById("huddlyGreetingTitle");
  if (!greetingEl) return;
  const hour = new Date().getHours();
  let timeGreeting = L("Good evening", "Selamat malam");
  if (hour >= 4 && hour < 12) timeGreeting = L("Good morning", "Selamat pagi");
  else if (hour >= 12 && hour < 15) timeGreeting = L("Good afternoon", "Selamat siang");
  else if (hour >= 15 && hour < 18) timeGreeting = L("Good afternoon", "Selamat sore");
  const shortName = (displayName || L("Facilitator", "Fasilitator")).trim().split("—")[0].trim();
  greetingEl.textContent = `${timeGreeting}, ${shortName}`;
}

function applyFacilitatorProfileToUi(displayName, role = "") {
  const cleanName = (displayName || L("CodeAcademy Facilitator", "Fasilitator CodeAcademy")).trim();
  if (currentFacilitatorUser) {
    currentFacilitatorUser.displayName = cleanName;
    currentFacilitatorUser.role = role;
  }
  if (facilitatorNameInput) {
    facilitatorNameInput.value = cleanName;
  }
  if (facilitatorIdentityChip) {
    facilitatorIdentityChip.textContent = `👤 ${cleanName}${role ? ` · ${role}` : ""}`;
    facilitatorIdentityChip.style.display = "inline-flex";
  }
  updateHuddlyGreeting(cleanName);
}

function applyRoleAccessUi(accessRole) {
  currentAccessRole = accessRole === "super_admin" ? "super_admin" : "facilitator";
  const isAdmin = currentAccessRole === "super_admin";
  document.querySelectorAll("[data-admin-only='true']").forEach((el) => {
    el.style.display = isAdmin ? "" : "none";
  });
  const roleBadge = document.getElementById("roleBadge");
  if (roleBadge) {
    roleBadge.textContent = isAdmin ? "👑 Super Admin" : L("🎓 Facilitator", "🎓 Fasilitator");
    roleBadge.className = `role-badge ${isAdmin ? "super-admin" : "facilitator"}`;
    roleBadge.style.display = "inline-flex";
  }
  if (!isAdmin && ["certificates", "team", "analytics"].includes(activeView)) {
    switchView("submissions");
  }
}
const btnGoogleSignIn = document.getElementById("btnGoogleSignIn");
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
    showAuthError("SDK Firebase tidak tersedia. Periksa koneksi internet dan konfigurasi Firebase.");
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
        hasUser: Boolean(user),
        uid: user?.uid || null,
        email: user?.email || null,
        emailVerified: user?.emailVerified || false
      });
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

async function syncStaffPublicUserProfile(uid, displayName, bio, accessRole) {
  if (!isFirebaseReady || !db || !uid) return;
  const isAdmin = accessRole === "super_admin";
  try {
    await db.collection("users").doc(uid).set({
      userId: uid,
      firebaseUid: uid,
      name: displayName,
      bio: bio || (isAdmin ? "System Architect & Super Admin CodeAcademy." : "Mentor & Reviewer Resmi CodeAcademy."),
      role: isAdmin ? "Super Admin" : "Fasilitator",
      accessRole: isAdmin ? "super_admin" : "facilitator",
      headlineTitle: isAdmin ? "System Architect" : "Official Facilitator",
      level: 99,
      totalXp: isAdmin ? 99999 : 9999,
      updatedAt: Date.now()
    }, { merge: true });
  } catch (err) {
    console.warn("[FacilitatorAuth] syncStaffPublicUserProfile warning:", err?.message || err);
  }
}

async function verifyAndEnterFacilitatorSession(user) {
  if (verifyingSessionPromise) {
    console.log("[FacilitatorAuth] Reusing active verification promise");
    return verifyingSessionPromise;
  }
  verifyingSessionPromise = (async () => {
    const email = (user.email || "").trim().toLowerCase();
    console.log("[FacilitatorAuth] verifyAndEnterFacilitatorSession start:", {
      uid: user.uid,
      email,
      emailVerified: user.emailVerified
    });

    if (!email) {
      const msg = L("Account does not have a valid email address.", "Akun tidak memiliki alamat email yang valid.");
      setAuthStatus("");
      showAuthError(msg);
      await auth.signOut();
      return;
    }

    try {
      setAuthStatus(L(`Authenticated (${email}). Refreshing session token...`, `Akun terautentikasi (${email}). Menyegarkan token sesi...`));
      try {
        await user.getIdToken(true);
      } catch (tokErr) {
        console.warn("[FacilitatorAuth] getIdToken(true) warning:", tokErr?.code || tokErr?.message);
      }

      setAuthStatus(L(`Verifying staff access in Firestore: facilitators/${email}...`, `Memverifikasi izin fasilitator di Firestore: facilitators/${email}...`));
      console.log(`[FacilitatorAuth] Reading Firestore allowlist: facilitators/${email}`);
      let allowDoc;
      try {
        allowDoc = await db.collection("facilitators").doc(email).get({ source: "server" });
      } catch (serverErr) {
        console.warn("[FacilitatorAuth] Server fetch fallback to default:", serverErr?.code || serverErr?.message);
        allowDoc = await db.collection("facilitators").doc(email).get();
      }
      console.log("[FacilitatorAuth] allowDoc result:", {
        exists: allowDoc.exists,
        data: allowDoc.exists ? allowDoc.data() : null
      });

      if (!allowDoc.exists) {
        const msg = L(
          `Access denied: Firestore document facilitators/${email} was not found. Use the "Request Access" button below to register your email.`,
          `Akses ditolak: dokumen Firestore facilitators/${email} tidak ditemukan. Gunakan tombol "Ajukan Akses" di bawah form login untuk mendaftarkan email Anda.`
        );
        setAuthStatus("");
        showAuthError(msg);
        await auth.signOut();
        return;
      }

      if (!user.emailVerified) {
        console.warn("[FacilitatorAuth] Warning: user.emailVerified === false; Firestore write rules require email_verified == true.");
      }

      const profileData = allowDoc.data() || {};
      const savedLocal = loadSavedFacilitatorProfile();
      const resolvedDisplayName = savedLocal.name || profileData.name || user.displayName || L("CodeAcademy Facilitator", "Fasilitator CodeAcademy");
      const resolvedRole = savedLocal.role || profileData.role || "";
      const accessRole = profileData.accessRole === "super_admin" ? "super_admin" : "facilitator";
      currentFacilitatorUser = {
        uid: user.uid,
        email,
        displayName: resolvedDisplayName,
        role: resolvedRole,
        bio: savedLocal.bio || profileData.bio || "",
        accessRole
      };
      isFirebaseReady = true;

      await syncStaffPublicUserProfile(
        user.uid,
        resolvedDisplayName,
        currentFacilitatorUser.bio,
        accessRole
      );

      console.log("[FacilitatorAuth] Verified! Hiding authOverlay and entering dashboard:", currentFacilitatorUser);
      setAuthStatus("");
      hideAuthOverlay();
      applyRoleAccessUi(accessRole);
      switchView(activeView || "submissions");
      applyFacilitatorProfileToUi(resolvedDisplayName, resolvedRole);

      modeBadge.textContent = "● Live Firebase";
      modeBadge.className = "badge-mode live";

      renderAllViews();
      startRealtimeListeners();
      showToast(L(`Signed in as ${email}`, `Masuk sebagai ${email}`));
    } catch (err) {
      console.error("[FacilitatorAuth] Verify facilitator failed:", err.code, err.message, err);
      setAuthStatus("");
      showAuthError(
        L(
          `Failed to verify staff permissions (${err.code || err.message || "permission-denied"}). Please ensure the latest Firestore Rules are published.`,
          `Gagal memverifikasi izin fasilitator (${err.code || err.message || "permission-denied"}). Pastikan Firestore Rules terbaru sudah di-Publish.`
        )
      );
      await auth.signOut();
    } finally {
      verifyingSessionPromise = null;
    }
  })();
  return verifyingSessionPromise;
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
        showToast(L("Failed to sync submissions: ", "Gagal sinkronisasi submission: ") + error.message);
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
        showToast(L("Failed to load forum: ", "Gagal memuat forum: ") + error.message);
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

  unsubRatings = db
    .collection("submission_ratings")
    .orderBy("ratedAt", "desc")
    .onSnapshot(
      (snapshot) => {
        submissionRatings = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
        renderTable();
        renderRatingsSummary();
        renderAnalytics();
      },
      (error) => {
        console.error("Ratings listener error:", error);
      }
    );

  if (currentAccessRole === "super_admin") {
    unsubTeam = db.collection("facilitators").onSnapshot(
      (snapshot) => {
        facilitatorsList = snapshot.docs.map((doc) => ({ email: doc.id, ...doc.data() }));
        renderTeam();
      },
      (error) => console.error("Team listener error:", error)
    );
    unsubStudents = db.collection("users").limit(100).onSnapshot(
      (snapshot) => {
        studentUsersList = snapshot.docs
          .map((doc) => ({ id: doc.id, ...doc.data() }))
          .sort((a, b) => (Number(b.totalXp) || 0) - (Number(a.totalXp) || 0));
        renderAnalytics();
      },
      (error) => console.error("Users listener error:", error)
    );
  }
}

function stopRealtimeListeners() {
  if (typeof unsubSubmissions === "function") unsubSubmissions();
  if (typeof unsubForum === "function") unsubForum();
  if (typeof unsubAnnouncements === "function") unsubAnnouncements();
  if (typeof unsubCertificates === "function") unsubCertificates();
  if (typeof unsubTeam === "function") unsubTeam();
  if (typeof unsubStudents === "function") unsubStudents();
  if (typeof unsubRatings === "function") unsubRatings();
  unsubSubmissions = null;
  unsubForum = null;
  unsubAnnouncements = null;
  unsubCertificates = null;
  unsubTeam = null;
  unsubStudents = null;
  unsubRatings = null;
}

function renderAllViews() {
  updateStats();
  renderTable();
  renderForum();
  renderAnnouncements();
  renderCertificates();
  renderTeam();
  renderAnalytics();
  renderHuddlyDashboardMeta();
  renderRatingsSummary();
}

function showAuthOverlay(preserveError = false) {
  console.log("[FacilitatorAuth] showAuthOverlay", { preserveError });
  authOverlay.hidden = false;
  authOverlay.classList.remove("hidden");
  authOverlay.style.setProperty("display", "flex", "important");
  authOverlay.setAttribute("aria-hidden", "false");
  if (!preserveError) {
    authError.classList.remove("show");
  }
  facilitatorIdentityChip.style.display = "none";
}

function hideAuthOverlay() {
  console.log("[FacilitatorAuth] hideAuthOverlay -> dashboard visible");
  authOverlay.classList.add("hidden");
  authOverlay.hidden = true;
  authOverlay.style.setProperty("display", "none", "important");
  authOverlay.setAttribute("aria-hidden", "true");
  authError.classList.remove("show");
  setAuthStatus("");
}

function setAuthStatus(msg) {
  if (!authStatusEl) return;
  if (msg) {
    authStatusEl.textContent = msg;
    authStatusEl.classList.add("show");
  } else {
    authStatusEl.textContent = "";
    authStatusEl.classList.remove("show");
  }
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
      showAuthError(L("Firebase Auth is unavailable. Please reload the page and try again.", "Firebase Auth tidak tersedia. Muat ulang halaman dan coba lagi."));
      return;
    }
    authError.classList.remove("show");
    const email = emailInput.value.trim();
    const password = passwordInput.value;
    try {
      await auth.signInWithEmailAndPassword(email, password);
    } catch (err) {
      console.error("Email sign-in error:", err);
      showAuthError(L("Sign-in failed: ", "Gagal masuk: ") + (err.message || err.code));
    }
  });

  attachAccessRequestModalListeners();

  // Live re-render dynamic views when EN | ID toggle changes
  window.addEventListener("ca-lang-change", () => {
    switchView(activeView || "submissions");
    if (currentFacilitatorUser) {
      updateHuddlyGreeting(currentFacilitatorUser.displayName);
      applyRoleAccessUi(currentAccessRole);
    }
    renderAllViews();
    if (currentSubmission && reviewModal.classList.contains("open")) {
      openReviewModal(currentSubmission.id);
    }
  });

  btnGoogleSignIn.addEventListener("click", async () => {
    console.log("[FacilitatorAuth] Google Sign-In clicked");
    if (!auth) {
      showAuthError(L("Firebase Auth is unavailable. Please reload the page and try again.", "Firebase Auth tidak tersedia. Muat ulang halaman dan coba lagi."));
      return;
    }
    authError.classList.remove("show");
    setAuthStatus(L("Opening Google sign-in popup...", "Membuka popup login Google..."));
    try {
      await auth.setPersistence(firebase.auth.Auth.Persistence.LOCAL);
      const provider = new firebase.auth.GoogleAuthProvider();
      provider.setCustomParameters({ prompt: "select_account" });
      const cred = await auth.signInWithPopup(provider);
      console.log("[FacilitatorAuth] signInWithPopup resolved:", cred?.user?.email);
      if (cred?.user) {
        await verifyAndEnterFacilitatorSession(cred.user);
      }
    } catch (err) {
      console.error("[FacilitatorAuth] Google sign-in error:", err.code, err.message);
      setAuthStatus("");
      if (err.code === "auth/popup-blocked") {
        showAuthError(L(
          "Google sign-in popup was blocked by your browser. Allow popups in the address bar and click again, or sign in with Email & Password.",
          "Popup login Google diblokir oleh browser. Izinkan popup di address bar browser lalu klik lagi, atau gunakan Email & Kata Sandi."
        ));
      } else if (err.code === "auth/popup-closed-by-user") {
        showAuthError(L("Google sign-in window was closed before finishing.", "Jendela login Google ditutup sebelum selesai."));
      } else if (err.code === "auth/unauthorized-domain") {
        showAuthError(L(
          `Domain "${window.location.hostname}" is not added in Firebase Console → Authentication → Settings → Authorized domains.`,
          `Domain "${window.location.hostname}" belum ditambahkan di Firebase Console → Authentication → Settings → Authorized domains.`
        ));
      } else {
        showAuthError(L("Google sign-in failed", "Login Google gagal") + " (" + (err.code || "error") + "): " + (err.message || err.code));
      }
    }
  });

  btnLogout.addEventListener("click", async () => {
    stopRealtimeListeners();
    isFirebaseReady = false;
    currentFacilitatorUser = null;
    currentAccessRole = "facilitator";
    const roleBadge = document.getElementById("roleBadge");
    if (roleBadge) roleBadge.style.display = "none";
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

  // Event delegation for Forum Delete, Pin/Helpful, Targeted Reply & Reply forms
  forumThreadsList.addEventListener("click", (e) => {
    const delBtn = e.target.closest("[data-action='delete-post']");
    if (delBtn && delBtn.dataset.postId) {
      deleteForumPost(delBtn.dataset.postId);
      return;
    }
    const verifyBtn = e.target.closest("[data-action='toggle-verified']");
    if (verifyBtn && verifyBtn.dataset.postId) {
      toggleForumPostFlag(verifyBtn.dataset.postId, "isVerifiedAnswer");
      return;
    }
    const helpfulBtn = e.target.closest("[data-action='toggle-helpful']");
    if (helpfulBtn && helpfulBtn.dataset.postId) {
      toggleForumPostFlag(helpfulBtn.dataset.postId, "isHelpful");
      return;
    }
    const targetBtn = e.target.closest("[data-action='target-reply']");
    if (targetBtn && targetBtn.dataset.threadId && targetBtn.dataset.postId) {
      const threadId = targetBtn.dataset.threadId;
      const postId = targetBtn.dataset.postId;
      const targetPost = forumPosts.find((p) => p.postId === postId);
      if (targetPost) {
        activeReplyTargetByThread.set(threadId, {
          postId: targetPost.postId,
          authorName: targetPost.authorName || "Peserta",
          contentSnippet: (targetPost.content || "").slice(0, 80)
        });
        renderForum();
        const input = forumThreadsList.querySelector(
          `form[data-thread-id="${CSS.escape(threadId)}"] input`
        );
        if (input) input.focus();
      }
      return;
    }
    const clearBtn = e.target.closest("[data-action='clear-reply-target']");
    if (clearBtn && clearBtn.dataset.threadId) {
      activeReplyTargetByThread.delete(clearBtn.dataset.threadId);
      renderForum();
    }
  });

  forumThreadsList.addEventListener("submit", (e) => {
    const form = e.target.closest("form[data-action='reply-thread']");
    if (form && form.dataset.threadId) {
      e.preventDefault();
      replyToThread(form);
    }
  });

  const profileModal = document.getElementById("profileModal");
  const btnCloseProfileModal = document.getElementById("btnCloseProfileModal");
  const btnCancelProfileModal = document.getElementById("btnCancelProfileModal");
  const btnSaveProfile = document.getElementById("btnSaveProfile");
  const profileNameInput = document.getElementById("profileNameInput");
  const profileRoleInput = document.getElementById("profileRoleInput");
  const profileBioInput = document.getElementById("profileBioInput");

  const closeProfileModal = () => {
    if (profileModal) profileModal.hidden = true;
  };
  if (facilitatorIdentityChip) {
    facilitatorIdentityChip.addEventListener("click", () => {
      const saved = loadSavedFacilitatorProfile();
      if (profileNameInput) profileNameInput.value = saved.name || currentFacilitatorUser?.displayName || facilitatorNameInput?.value || "";
      if (profileRoleInput) profileRoleInput.value = saved.role || currentFacilitatorUser?.role || "";
      if (profileBioInput) profileBioInput.value = saved.bio || currentFacilitatorUser?.bio || "";
      if (profileModal) profileModal.hidden = false;
    });
  }
  btnCloseProfileModal?.addEventListener("click", closeProfileModal);
  btnCancelProfileModal?.addEventListener("click", closeProfileModal);
  btnSaveProfile?.addEventListener("click", async () => {
    const newName = (profileNameInput?.value || "").trim();
    const newRole = (profileRoleInput?.value || "").trim();
    const newBio = (profileBioInput?.value || "").trim();
    if (newName.length < 2) {
      showToast(L("Display name must be at least 2 characters.", "Nama fasilitator minimal 2 karakter."));
      return;
    }
    const nextProfile = { name: newName, role: newRole, bio: newBio, updatedAt: Date.now() };
    saveLocalFacilitatorProfile(nextProfile);
    if (currentFacilitatorUser) {
      currentFacilitatorUser.bio = newBio;
    }
    applyFacilitatorProfileToUi(newName, newRole);
    if (isFirebaseReady && db && currentFacilitatorUser?.email) {
      try {
        await db.collection("facilitators").doc(currentFacilitatorUser.email).set(nextProfile, { merge: true });
        await syncStaffPublicUserProfile(
          currentFacilitatorUser.uid,
          newName,
          newBio,
          currentFacilitatorUser.accessRole || "facilitator"
        );
      } catch (err) {
        console.warn("Save facilitator profile to Firestore warning:", err);
      }
    }
    closeProfileModal();
    showToast(L("Staff profile updated.", "Profil fasilitator berhasil diperbarui."));
  });

  // Huddly Quick Shortcuts & Companion Rail
  document.getElementById("btnQuickForumUnanswered")?.addEventListener("click", () => {
    forumOnlyUnanswered = true;
    btnForumUnansweredFilter?.classList.add("active");
    btnForumUnansweredFilter?.setAttribute("aria-pressed", "true");
    switchView("forum");
    renderForum();
  });

  document.getElementById("btnQuickNewAnnouncement")?.addEventListener("click", () => {
    switchView("announcements");
    announcementTitleInput?.focus();
  });

  document.querySelectorAll("[data-jump-view]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const targetView = btn.dataset.jumpView;
      if (targetView) switchView(targetView);
    });
  });

  document.querySelectorAll(".quick-chip-btn").forEach((chip) => {
    chip.addEventListener("click", () => {
      const quickInput = document.getElementById("huddlyQuickInput");
      const promptText = isIndonesian()
        ? (chip.dataset.quickPromptId || chip.dataset.quickPrompt || "")
        : (chip.dataset.quickPromptEn || chip.dataset.quickPrompt || "");
      if (quickInput) quickInput.value = promptText;
      quickInput?.focus();
    });
  });

  document.getElementById("btnHuddlyQuickGo")?.addEventListener("click", () => {
    const quickInput = document.getElementById("huddlyQuickInput");
    const text = (quickInput?.value || "").trim();
    switchView("announcements");
    if (text && announcementTitleInput) {
      announcementTitleInput.value = text;
      announcementBodyInput?.focus();
    } else {
      announcementTitleInput?.focus();
    }
  });

  // Super Admin: Team Management & CSV Exports
  document.getElementById("btnAddTeamMember")?.addEventListener("click", async () => {
    const emailEl = document.getElementById("teamEmailInput");
    const nameEl = document.getElementById("teamNameInput");
    const roleEl = document.getElementById("teamRoleSelect");
    const email = (emailEl?.value || "").trim().toLowerCase();
    const name = (nameEl?.value || "").trim();
    const accessRole = roleEl?.value === "super_admin" ? "super_admin" : "facilitator";
    if (!email.includes("@")) {
      showToast(L("Enter a valid Google email address.", "Masukkan alamat email Google yang valid."));
      return;
    }
    const payload = {
      email,
      name: name || email.split("@")[0],
      accessRole,
      updatedAt: Date.now()
    };
    if (isFirebaseReady && db) {
      try {
        await db.collection("facilitators").doc(email).set(payload, { merge: true });
        if (emailEl) emailEl.value = "";
        if (nameEl) nameEl.value = "";
        showToast(L(`Saved ${email} as ${accessRole}.`, `Anggota ${email} disimpan sebagai ${accessRole}.`));
      } catch (err) {
        showToast(L("Failed to save member: ", "Gagal menyimpan anggota: ") + err.message);
      }
    } else {
      const idx = facilitatorsList.findIndex((f) => f.email === email);
      if (idx >= 0) facilitatorsList[idx] = { ...facilitatorsList[idx], ...payload };
      else facilitatorsList.unshift(payload);
      if (emailEl) emailEl.value = "";
      if (nameEl) nameEl.value = "";
      renderTeam();
      showToast(L(`Added ${email} (Offline Mode).`, `Anggota ${email} ditambahkan (Mode Demo).`));
    }
  });

  document.getElementById("teamTableBody")?.addEventListener("click", async (e) => {
    const promoteBtn = e.target.closest("[data-action='promote-team']");
    if (promoteBtn && promoteBtn.dataset.email) {
      if (currentAccessRole !== "super_admin") {
        showToast(L("Only Super Admins can promote team members.", "Hanya Super Admin yang dapat menaikkan peran tim."));
        return;
      }
      const email = promoteBtn.dataset.email;
      const target = facilitatorsList.find((f) => f.email === email);
      if (target?.accessRole === "super_admin") {
        showToast(L("This account is already a Super Admin.", "Akun ini sudah berstatus Super Admin."));
        return;
      }
      if (!confirm(L(
        `Promote ${target?.name || email} (${email}) to Super Admin? Fellow Super Admins cannot be demoted later.`,
        `Naikkan peran ${target?.name || email} (${email}) menjadi Super Admin? Sesama Super Admin tidak dapat diturunkan kembali.`
      ))) {
        return;
      }
      if (isFirebaseReady && db) {
        try {
          await db.collection("facilitators").doc(email).update({
            accessRole: "super_admin",
            updatedAt: Date.now()
          });
          showToast(L(`${target?.name || email} promoted to Super Admin.`, `${target?.name || email} berhasil dinaikkan menjadi Super Admin.`));
        } catch (err) {
          showToast(L("Failed to promote role: ", "Gagal menaikkan peran: ") + err.message);
        }
      }
      return;
    }

    const delBtn = e.target.closest("[data-action='remove-team']");
    if (!delBtn || !delBtn.dataset.email) return;
    const email = delBtn.dataset.email;
    if (email === currentFacilitatorUser?.email) {
      showToast(L("You cannot remove your own account.", "Tidak dapat menghapus akun sendiri."));
      return;
    }
    const target = facilitatorsList.find((f) => f.email === email);
    if (target?.accessRole === "super_admin") {
      showToast(L("Fellow Super Admins are protected and cannot be demoted or removed.", "Sesama Super Admin dilindungi dan tidak dapat diturunkan atau dihapus."));
      return;
    }
    if (!confirm(L(`Revoke portal access for ${email}?`, `Hapus akses portal untuk ${email}?`))) return;
    if (isFirebaseReady && db) {
      try {
        await db.collection("facilitators").doc(email).delete();
        showToast(L(`Access revoked for ${email}.`, `Akses ${email} dihapus.`));
      } catch (err) {
        showToast(L("Failed to remove member: ", "Gagal menghapus anggota: ") + err.message);
      }
    }
  });

  document.getElementById("btnExportCertCsv")?.addEventListener("click", () => {
    downloadCsv(
      L("codeacademy-certificates.csv", "codeacademy-sertifikat.csv"),
      L(
        ["Cert ID", "Name", "Email", "Course", "Final Score", "Issued At"],
        ["Cert ID", "Nama", "Email", "Kursus", "Skor Akhir", "Diterbitkan"]
      ),
      certificates.map((c) => [c.certId, c.userName, c.userEmail, getCourseLabel(c.courseId), c.finalScore ?? c.scorePercent ?? "", formatDate(c.issuedAt)])
    );
  });

  certificatesList?.addEventListener("click", async (e) => {
    const revokeBtn = e.target.closest("[data-action='revoke-cert']");
    if (!revokeBtn || !revokeBtn.dataset.certId) return;
    if (currentAccessRole !== "super_admin") return;
    const certId = revokeBtn.dataset.certId;
    if (!confirm(L(`Permanently revoke and delete certificate ${certId}?`, `Cabut dan hapus sertifikat ${certId} secara permanen?`))) return;
    if (isFirebaseReady && db) {
      try {
        await db.collection("certificates").doc(certId).delete();
        showToast(L(`Certificate ${certId} revoked.`, `Sertifikat ${certId} dicabut.`));
      } catch (err) {
        showToast(L("Failed to revoke certificate: ", "Gagal mencabut sertifikat: ") + err.message);
      }
    } else {
      certificates = certificates.filter((c) => c.certId !== certId);
      renderCertificates();
      renderAnalytics();
      showToast(L(`Certificate ${certId} revoked (Offline Mode).`, `Sertifikat ${certId} dicabut (Mode Demo).`));
    }
  });

  document.getElementById("btnExportGradesCsv")?.addEventListener("click", () => {
    downloadCsv(
      L("codeacademy-project-grades.csv", "codeacademy-nilai-proyek.csv"),
      L(
        ["Name", "Email", "Course", "Status", "Final Score", "Reviewer"],
        ["Nama", "Email", "Kursus", "Status", "Skor Akhir", "Fasilitator"]
      ),
      submissions.map((s) => [s.studentName || s.userName || "", s.studentEmail || s.userEmail || "", getCourseLabel(s.courseId), s.status, s.finalScore ?? s.facilitatorScore ?? "", s.facilitatorName || ""])
    );
  });

  // Super Admin: Student Search, Filter & Ban Controls
  let banTargetUserId = null;
  const banModal = document.getElementById("banUserModal");
  const banReasonInput = document.getElementById("banReasonInput");
  const closeBanModal = () => {
    banTargetUserId = null;
    if (banModal) banModal.hidden = true;
  };
  document.getElementById("btnCloseBanModal")?.addEventListener("click", closeBanModal);
  document.getElementById("btnCancelBanModal")?.addEventListener("click", closeBanModal);

  document.querySelectorAll(".ban-chip-btn").forEach((chip) => {
    chip.addEventListener("click", () => {
      const reasonTxt = isIndonesian()
        ? (chip.dataset.banReasonId || chip.dataset.banReason || "")
        : (chip.dataset.banReasonEn || chip.dataset.banReason || "");
      if (banReasonInput) banReasonInput.value = reasonTxt;
      banReasonInput?.focus();
    });
  });

  document.getElementById("studentSearchInput")?.addEventListener("input", () => {
    renderAnalytics();
  });

  document.querySelectorAll(".btn-student-filter[data-student-status]").forEach((pill) => {
    pill.addEventListener("click", () => {
      document.querySelectorAll(".btn-student-filter[data-student-status]").forEach((p) => p.classList.remove("active"));
      pill.classList.add("active");
      renderAnalytics();
    });
  });

  document.getElementById("studentsTableBody")?.addEventListener("click", async (e) => {
    if (currentAccessRole !== "super_admin") return;
    const banBtn = e.target.closest("[data-action='open-ban']");
    if (banBtn && banBtn.dataset.uid) {
      const target = studentUsersList.find((u) => u.id === banBtn.dataset.uid);
      if (!target) return;
      banTargetUserId = target.id;
      const nameEl = document.getElementById("banTargetName");
      const emailEl = document.getElementById("banTargetEmail");
      if (nameEl) nameEl.textContent = target.name || L("Student", "Siswa");
      if (emailEl) emailEl.textContent = target.email || `UID: ${target.id}`;
      if (banReasonInput) banReasonInput.value = target.banReason || "";
      if (banModal) banModal.hidden = false;
      return;
    }

    const unbanBtn = e.target.closest("[data-action='unban-user']");
    if (unbanBtn && unbanBtn.dataset.uid) {
      const uid = unbanBtn.dataset.uid;
      const target = studentUsersList.find((u) => u.id === uid);
      if (!confirm(L(`Restore account access for ${target?.name || uid}?`, `Cabut pembekuan akun untuk ${target?.name || uid}?`))) return;
      if (isFirebaseReady && db) {
        try {
          await db.collection("users").doc(uid).set({
            isBanned: false,
            banReason: "",
            bannedAt: null,
            bannedBy: "",
            unbannedAt: Date.now()
          }, { merge: true });
          showToast(L(`Account ${target?.name || uid} restored.`, `Akses akun ${target?.name || uid} diaktifkan kembali.`));
        } catch (err) {
          showToast(L("Failed to restore account: ", "Gagal mencabut ban: ") + err.message);
        }
      } else {
        studentUsersList = studentUsersList.map((u) =>
          u.id === uid ? { ...u, isBanned: false, banReason: "", bannedAt: null } : u
        );
        renderAnalytics();
        showToast(L(`Account ${target?.name || uid} restored (Offline Mode).`, `Akun ${target?.name || uid} diaktifkan kembali (Mode Demo).`));
      }
    }
  });

  document.getElementById("btnConfirmBanUser")?.addEventListener("click", async () => {
    if (currentAccessRole !== "super_admin" || !banTargetUserId) return;
    const reason = (banReasonInput?.value || "").trim();
    if (reason.length < 5) {
      showToast(L("Please enter a suspension reason (at least 5 characters).", "Tuliskan alasan pembekuan minimal 5 karakter."));
      return;
    }
    const uid = banTargetUserId;
    const target = studentUsersList.find((u) => u.id === uid);
    const adminIdentity = currentFacilitatorUser?.email || "super_admin";
    const banPayload = {
      isBanned: true,
      banReason: reason,
      bannedAt: Date.now(),
      bannedBy: adminIdentity
    };
    if (isFirebaseReady && db) {
      try {
        await db.collection("users").doc(uid).set(banPayload, { merge: true });
        closeBanModal();
        showToast(L(`Account ${target?.name || uid} has been suspended.`, `Akun ${target?.name || uid} berhasil dibekukan.`));
      } catch (err) {
        showToast(L("Failed to suspend account: ", "Gagal membekukan akun: ") + err.message);
      }
    } else {
      studentUsersList = studentUsersList.map((u) =>
        u.id === uid ? { ...u, ...banPayload } : u
      );
      closeBanModal();
      renderAnalytics();
      showToast(L(`Account ${target?.name || uid} suspended (Offline Mode).`, `Akun ${target?.name || uid} dibekukan (Mode Demo).`));
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
      const txt = isIndonesian()
        ? (btn.dataset.textId || btn.dataset.text || "")
        : (btn.dataset.textEn || btn.dataset.text || "");
      if (txt && !facilitatorFeedback.value.includes(txt)) {
        facilitatorFeedback.value = (facilitatorFeedback.value ? facilitatorFeedback.value + "\n" : "") + txt;
      }
    });
  });

  // Actions: Revisi & Approve
  btnRequestRevision.addEventListener("click", () => executeReview("REVISION_REQUESTED"));
  btnApprove.addEventListener("click", () => executeReview("APPROVED"));
}

function switchView(view) {
  if (currentAccessRole !== "super_admin" && ["certificates", "team", "analytics"].includes(view)) {
    view = "submissions";
  }
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
  const teamView = document.getElementById("teamView");
  const analyticsView = document.getElementById("analyticsView");
  if (teamView) teamView.style.display = view === "team" ? "block" : "none";
  if (analyticsView) analyticsView.style.display = view === "analytics" ? "block" : "none";

  if (view === "submissions") {
    pageEyebrow.textContent = L("Final Project Evaluation", "Evaluasi Proyek Akhir");
    pageTitle.textContent = L("Review Queue", "Antrean Penilaian Peserta");
    pageSubtitle.textContent = L(
      "Inspect workspace code, test live previews, grade rubrics (40% Quiz + 60% Project), and issue verified certificates.",
      "Tinjau kode workspace, cek pratinjau langsung, beri skor rubrik (40% Kuis + 60% Proyek), dan terbitkan sertifikat."
    );
  } else if (view === "forum") {
    pageEyebrow.textContent = L("Moderation & Mentoring", "Moderasi & Bimbingan");
    pageTitle.textContent = L("Class Discussion Forum", "Forum Diskusi Kelas");
    pageSubtitle.textContent = L(
      "Answer learner questions in real time with a Facilitator badge, pin verified solutions, or reply directly.",
      "Jawab pertanyaan peserta secara real-time dengan lencana Fasilitator, pin jawaban terverifikasi, atau balas spesifik."
    );
  } else if (view === "announcements") {
    pageEyebrow.textContent = L("Broadcast Center", "Broadcast Informasi");
    pageTitle.textContent = L("Class Announcements", "Pengumuman Kelas ke Aplikasi");
    pageSubtitle.textContent = L(
      "Publish announcements that appear immediately in every learner's Android Notifications tab.",
      "Terbitkan pengumuman penting yang langsung muncul di tab Notifikasi seluruh peserta Android."
    );
  } else if (view === "certificates") {
    pageEyebrow.textContent = L("Super Admin · Graduation Registry", "Panel Super Admin · Registri Kelulusan");
    pageTitle.textContent = L("Certificates & Graduation Audit", "Sertifikat & Audit Kelulusan");
    pageSubtitle.textContent = L(
      "Track issued CC-... credentials verified publicly on the Verification Portal or export CSV.",
      "Pantau sertifikat berformat CC-... yang terverifikasi secara publik di Portal Verifikasi atau ekspor CSV."
    );
  } else if (view === "team") {
    pageEyebrow.textContent = L("Super Admin · Access Management", "Panel Super Admin · Manajemen Akses");
    pageTitle.textContent = L("Facilitator & Admin Team", "Tim Fasilitator & Admin");
    pageSubtitle.textContent = L(
      "Manage authorized Google emails for the Staff Portal and configure team roles.",
      "Kelola daftar email Google yang diizinkan mengakses Portal Staff dan atur perannya."
    );
  } else if (view === "analytics") {
    pageEyebrow.textContent = L("Super Admin · Platform Analytics", "Panel Super Admin · Analitik Global");
    pageTitle.textContent = L("Platform Overview & Student Directory", "Ringkasan Platform & Direktori Siswa");
    pageSubtitle.textContent = L(
      "Monitor pass rates, forum resolution, learner progress, account status, and export CSV grade reports.",
      "Pantau statistik kelulusan, keaktifan forum, progres siswa, dan unduh laporan CSV."
    );
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

function getStudentInitials(name) {
  const parts = String(name || "P").trim().split(/\s+/).filter(Boolean);
  if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
  return (parts[0] || "P").slice(0, 2).toUpperCase();
}

function renderHuddlyDashboardMeta() {
  const badgeEl = document.getElementById("huddlySpotlightBadge");
  const titleEl = document.getElementById("huddlySpotlightTitle");
  const metaEl = document.getElementById("huddlySpotlightMeta");
  const avatarsEl = document.getElementById("huddlySpotlightAvatars");
  const spotlightBtn = document.getElementById("btnSpotlightReview");

  const pendingList = submissions.filter((s) => (s.status || "").toUpperCase() === "PENDING_REVIEW");
  const revisionList = submissions.filter((s) => (s.status || "").toUpperCase() === "REVISION_REQUESTED");

  if (titleEl && metaEl && avatarsEl && spotlightBtn) {
    if (pendingList.length > 0) {
      const topSub = pendingList[0];
      const studentName = topSub.studentName || topSub.userName || L("Student", "Peserta");
      const courseName = topSub.courseTitle || getCourseLabel(topSub.courseId);
      const attemptNum = Number(topSub.attemptNumber ?? topSub.attemptCount) || 1;
      if (badgeEl) badgeEl.textContent = L(`${pendingList.length} Pending Review`, `${pendingList.length} Antrean Menunggu Review`);
      titleEl.textContent = `${studentName} — ${courseName}`;
      metaEl.textContent = L(
        `Quiz Score ${Number(topSub.finalQuizScore) || 0}/100 • Submitted ${formatDate(topSub.submittedAt)} • Attempt #${attemptNum}`,
        `Skor Kuis ${Number(topSub.finalQuizScore) || 0}/100 • Dikirim ${formatDate(topSub.submittedAt)} • Percobaan ke-${attemptNum}`
      );
      const avatarClasses = ["av-1", "av-2", "av-3", "av-4"];
      const sampleAvatars = pendingList.slice(0, 3).map((s, idx) => {
        const init = getStudentInitials(s.studentName || s.userName || "S");
        return `<span class="av ${avatarClasses[idx % 4]}">${escapeHtml(init)}</span>`;
      });
      if (pendingList.length > 3) {
        sampleAvatars.push(`<span class="av av-4">+${pendingList.length - 3}</span>`);
      }
      avatarsEl.innerHTML = `${sampleAvatars.join("")}<span class="av-note">${L(`${pendingList.length} project(s) ready to grade`, `${pendingList.length} proyek siap dinilai`)}</span>`;
      spotlightBtn.textContent = L("Start Review →", "Mulai Review →");
      spotlightBtn.onclick = () => openReviewModal(topSub.id);
    } else {
      if (badgeEl) badgeEl.textContent = L("Queue Clear", "Antrean Bersih");
      titleEl.textContent = L("All Final Projects Reviewed", "Semua Proyek Akhir Sudah Ditinjau");
      metaEl.textContent = L(
        `${submissions.length} total submissions recorded. Check class discussions or publish a new announcement.`,
        `${submissions.length} total pengajuan tersimpan. Pantau forum diskusi atau buat pengumuman baru untuk kelas.`
      );
      avatarsEl.innerHTML = `<span class="av av-1">✓</span><span class="av-note">${L("0 pending submissions right now", "0 proyek tertunda saat ini")}</span>`;
      spotlightBtn.textContent = L("Open Class Forum →", "Buka Forum Kelas →");
      spotlightBtn.onclick = () => switchView("forum");
    }
  }

  const highlightsEl = document.getElementById("huddlyHighlightsList");
  if (highlightsEl) {
    const roots = forumPosts.filter((p) => !p.replyToPostId && !p.rootThreadId).slice(0, 2);
    const latestAnn = announcements.slice(0, 1);
    const itemsHtml = [];

    latestAnn.forEach((ann) => {
      itemsHtml.push(`
        <button type="button" class="rail-item" data-rail-view="announcements">
          <span class="rail-item-avatar">AN</span>
          <span>
            <span class="rail-item-title">${escapeHtml(ann.title || L("Class Announcement", "Pengumuman Kelas"))}</span>
            <span class="rail-item-sub">${escapeHtml((ann.message || ann.body || "").slice(0, 68))}</span>
          </span>
        </button>
      `);
    });

    roots.forEach((t) => {
      const init = getStudentInitials(t.authorName || "S");
      itemsHtml.push(`
        <button type="button" class="rail-item" data-rail-view="forum">
          <span class="rail-item-avatar">${escapeHtml(init)}</span>
          <span>
            <span class="rail-item-title">${escapeHtml(t.authorName || L("Student", "Peserta"))} · Forum</span>
            <span class="rail-item-sub">${escapeHtml((t.content || "").slice(0, 70))}</span>
          </span>
        </button>
      `);
    });

    highlightsEl.innerHTML = itemsHtml.length > 0
      ? itemsHtml.join("")
      : `<div class="rail-empty">${L("No recent forum or announcement activity yet.", "Belum ada aktivitas forum atau pengumuman terbaru.")}</div>`;

    highlightsEl.querySelectorAll("[data-rail-view]").forEach((btn) => {
      btn.addEventListener("click", () => switchView(btn.dataset.railView));
    });
  }

  const actionEl = document.getElementById("huddlyActionItems");
  if (actionEl) {
    const roots = forumPosts.filter((p) => !p.replyToPostId && !p.rootThreadId);
    const repliesByThread = new Map();
    forumPosts.filter((p) => p.replyToPostId || p.rootThreadId).forEach((r) => {
      const key = r.rootThreadId || r.replyToPostId;
      if (!repliesByThread.has(key)) repliesByThread.set(key, []);
      repliesByThread.get(key).push(r);
    });
    const unansweredCount = roots.filter((t) => {
      const reps = repliesByThread.get(t.postId) || [];
      return !reps.some((r) => r.authorIsMentor === true || r.isMentor === true);
    }).length;

    actionEl.innerHTML = `
      <button type="button" class="action-row-btn" data-action-jump="pending">
        <span class="action-row-left"><span class="action-dot pending"></span>${L("Pending project reviews", "Review proyek menunggu")}</span>
        <span class="action-pill ${pendingList.length > 0 ? "urgent" : ""}">${pendingList.length} ${L("projects", "proyek")}</span>
      </button>
      <button type="button" class="action-row-btn" data-action-jump="forum">
        <span class="action-row-left"><span class="action-dot forum"></span>${L("Unanswered forum questions", "Pertanyaan forum belum dijawab")}</span>
        <span class="action-pill ${unansweredCount > 0 ? "urgent" : ""}">${unansweredCount} ${L("topics", "topik")}</span>
      </button>
      <button type="button" class="action-row-btn" data-action-jump="revision">
        <span class="action-row-left"><span class="action-dot revision"></span>${L("Students awaiting revision", "Peserta dalam status revisi")}</span>
        <span class="action-pill">${revisionList.length} ${L("revisions", "revisi")}</span>
      </button>
    `;

    actionEl.querySelectorAll("[data-action-jump]").forEach((btn) => {
      btn.addEventListener("click", () => {
        const mode = btn.dataset.actionJump;
        if (mode === "forum") {
          forumOnlyUnanswered = unansweredCount > 0;
          btnForumUnansweredFilter?.classList.toggle("active", forumOnlyUnanswered);
          switchView("forum");
          renderForum();
        } else if (mode === "pending" || mode === "revision") {
          const targetStatus = mode === "pending" ? "PENDING_REVIEW" : "REVISION_REQUESTED";
          filterStatus = targetStatus;
          filterPills.forEach((p) => p.classList.toggle("active", p.dataset.status === targetStatus));
          renderTable();
          document.querySelector(".table-card")?.scrollIntoView({ behavior: "smooth", block: "nearest" });
        }
      });
    });
  }
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
            <div class="empty-state-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24"><path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/><polyline points="13 2 13 9 20 9"/></svg>
            </div>
            <h4>${L("No project submissions found", "Tidak ada pengajuan proyek ditemukan")}</h4>
            <p>${L("Adjust your search filters or wait for learners to submit projects from the Android app.", "Ubah filter pencarian atau tunggu peserta mengirimkan proyek dari aplikasi Android.")}</p>
          </div>
        </td>
      </tr>
    `;
    renderHuddlyDashboardMeta();
    return;
  }

  let highlightedTopPending = false;
  submissionsTableBody.innerHTML = filtered
    .map((s) => {
      const badgeClass = getStatusBadgeClass(s.status);
      const badgeLabel = getStatusLabel(s.status);
      const dateStr = formatDate(s.submittedAt);
      const courseName = s.courseTitle || getCourseLabel(s.courseId);
      const studentName = s.studentName || s.userName || L("Student", "Peserta");
      const studentEmail = s.studentEmail || s.userEmail || s.accountId || "-";
      const attempt = Number(s.attemptNumber ?? s.attemptCount) || 1;
      const ratingInfo = getRatingForSubmission(s);
      const ratingPillHtml = ratingInfo
        ? `<span class="rating-star-pill" title="${escapeHtml(ratingInfo.comment || "")}">★ ${Number(ratingInfo.rating)}/5</span>`
        : "";
      const isPendingTop = (s.status || "").toUpperCase() === "PENDING_REVIEW" && !highlightedTopPending;
      if (isPendingTop) highlightedTopPending = true;

      return `
      <tr class="${isPendingTop ? "row-priority-lime" : ""}">
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
          <div class="status-cell-stack">
            <span class="status-badge ${badgeClass}">${badgeLabel}</span>
            ${ratingPillHtml}
          </div>
        </td>
        <td style="font-size: 12.5px; color: var(--muted); white-space: nowrap;">${escapeHtml(dateStr)}</td>
        <td style="font-size: 12.5px; color: var(--muted); white-space: nowrap;">${L(`#${attempt}`, `Ke-${attempt}`)}</td>
        <td style="text-align: right;">
          <button type="button" class="btn-review" data-action="open-review" data-id="${escapeHtml(s.id)}">
            ${s.status === "PENDING_REVIEW" ? L("Grade Now", "Nilai Sekarang") : L("Inspect / Edit", "Lihat / Ubah")}
          </button>
        </td>
      </tr>
    `;
    })
    .join("");
  renderHuddlyDashboardMeta();
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
  if (st === "PENDING_REVIEW") return L("Pending Review", "Menunggu Review");
  if (st === "REVISION_REQUESTED") return L("Revision Requested", "Perlu Revisi");
  if (st === "APPROVED") return L("Approved", "Disetujui");
  return st || "-";
}

// ==========================================
function renderTeam() {
  const badge = document.getElementById("teamTotalBadge");
  if (badge) badge.textContent = String(facilitatorsList.length);
  const tbody = document.getElementById("teamTableBody");
  if (!tbody) return;
  if (facilitatorsList.length === 0) {
    tbody.innerHTML = `<tr><td colspan="4" class="empty-row">${L("No staff team members recorded yet.", "Belum ada data tim fasilitator.")}</td></tr>`;
    return;
  }
  tbody.innerHTML = facilitatorsList
    .map((m) => {
      const isAdmin = m.accessRole === "super_admin";
      const isSelf = m.email === currentFacilitatorUser?.email;
      const actionsHtml = isAdmin
        ? `<span class="team-protected-pill">${isSelf ? L("Your Account (Super Admin)", "Akun Anda (Super Admin)") : L("Super Admin (Protected)", "Super Admin (Dilindungi)")}</span>`
        : `<div class="team-actions-row">
             <button type="button" class="btn-promote-ghost" data-action="promote-team" data-email="${escapeHtml(m.email)}">${L("⬆ Promote to Super Admin", "⬆ Jadikan Super Admin")}</button>
             <button type="button" class="btn-danger-ghost" data-action="remove-team" data-email="${escapeHtml(m.email)}">${L("Revoke Access", "Cabut Akses")}</button>
           </div>`;
      return `
        <tr>
          <td><strong>${escapeHtml(m.name || m.email)}</strong><div style="font-size:12px;color:var(--muted);">${escapeHtml(m.email)}</div></td>
          <td><span class="role-badge ${isAdmin ? "super-admin" : "facilitator"}">${isAdmin ? "👑 Super Admin" : L("🎓 Facilitator", "🎓 Fasilitator")}</span></td>
          <td>${escapeHtml(m.role || "-")}</td>
          <td>${actionsHtml}</td>
        </tr>
      `;
    })
    .join("");
}

function renderAnalytics() {
  const badge = document.getElementById("studentsTotalBadge");
  if (badge) badge.textContent = String(studentUsersList.length);
  const cardsEl = document.getElementById("adminAnalyticsCards");
  const bannedCount = studentUsersList.filter((u) => u.isBanned === true).length;
  if (cardsEl) {
    const approvedCount = submissions.filter((s) => (s.status || "").toUpperCase() === "APPROVED").length;
    const passRate = submissions.length ? Math.round((approvedCount / submissions.length) * 100) : 0;
    const rootsCount = forumPosts.filter((p) => !p.replyToPostId && !p.rootThreadId).length;
    const verifiedThreads = new Set(forumPosts.filter((p) => p.isVerifiedAnswer === true).map((p) => p.rootThreadId || p.replyToPostId || p.postId)).size;
    cardsEl.innerHTML = `
      <div class="stat-card">
        <div class="stat-top">
          <span class="stat-label">${L("Project Pass Rate", "Rasio Lulus Proyek")}</span>
          <span class="stat-pill ok">${L("Approved", "Lulus")}</span>
        </div>
        <div class="stat-bottom">
          <h3>${passRate}%</h3>
          <div class="stat-icon approved" aria-hidden="true">
            <svg class="i" viewBox="0 0 24 24"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
          </div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-top">
          <span class="stat-label">${L("Verified Forum Topics", "Topik Forum Terverifikasi")}</span>
          <span class="stat-pill neutral">Forum</span>
        </div>
        <div class="stat-bottom">
          <h3>${verifiedThreads} / ${rootsCount}</h3>
          <div class="stat-icon all" aria-hidden="true">
            <svg class="i" viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
          </div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-top">
          <span class="stat-label">${L("Issued Certificates", "Total Sertifikat Terbit")}</span>
          <span class="stat-pill lime">CC-ID</span>
        </div>
        <div class="stat-bottom">
          <h3>${certificates.length}</h3>
          <div class="stat-icon pending" aria-hidden="true">
            <svg class="i" viewBox="0 0 24 24"><circle cx="12" cy="8" r="6"/><path d="M15.477 12.89 17 22l-5-3-5 3 1.523-9.11"/></svg>
          </div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-top">
          <span class="stat-label">${L("Cloud Students", "Siswa Cloud")}</span>
          <span class="stat-pill ${bannedCount > 0 ? "warn" : "neutral"}">${bannedCount} ${L("Suspended", "Ban")}</span>
        </div>
        <div class="stat-bottom">
          <h3>${studentUsersList.length}</h3>
          <div class="stat-icon ${bannedCount > 0 ? "revision" : "all"}" aria-hidden="true">
            <svg class="i" viewBox="0 0 24 24"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
          </div>
        </div>
      </div>
      ${(() => {
        const ratings = getAllNormalizedRatings();
        const avg = ratings.length
          ? (ratings.reduce((acc, r) => acc + r.rating, 0) / ratings.length).toFixed(1)
          : "—";
        return `
      <div class="stat-card">
        <div class="stat-top">
          <span class="stat-label">${L("Review Satisfaction", "Kepuasan Review")}</span>
          <span class="stat-pill ok">${ratings.length} ${L("Ratings", "Rating")}</span>
        </div>
        <div class="stat-bottom">
          <h3>★ ${avg}</h3>
          <div class="stat-icon approved" aria-hidden="true">
            <svg class="i" viewBox="0 0 24 24"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
          </div>
        </div>
      </div>`;
      })()}
    `;
  }
  renderRatingsSummary();
  const tbody = document.getElementById("studentsTableBody");
  if (!tbody) return;
  const searchTerm = (document.getElementById("studentSearchInput")?.value || "").trim().toLowerCase();
  const activeStatusPill = document.querySelector(".btn-student-filter.active")?.dataset.studentStatus || "all";

  const filteredStudents = studentUsersList.filter((u) => {
    const isBanned = u.isBanned === true;
    if (activeStatusPill === "active" && isBanned) return false;
    if (activeStatusPill === "banned" && !isBanned) return false;
    if (!searchTerm) return true;
    return (
      (u.name || "").toLowerCase().includes(searchTerm) ||
      (u.email || "").toLowerCase().includes(searchTerm) ||
      (u.headlineTitle || "").toLowerCase().includes(searchTerm) ||
      (u.id || "").toLowerCase().includes(searchTerm)
    );
  });

  if (filteredStudents.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" class="empty-row">${L("No matching student records found.", "Tidak ada data siswa yang sesuai filter.")}</td></tr>`;
    return;
  }
  tbody.innerHTML = filteredStudents
    .slice(0, 60)
    .map((u) => {
      const isBanned = u.isBanned === true;
      const persona = u.headlineTitle ? `<span class="persona-tag-mini">${escapeHtml(u.headlineTitle)}</span><br>` : "";
      const emailLine = u.email ? escapeHtml(u.email) : `UID: ${escapeHtml(u.id || "-")}`;
      const statusHtml = isBanned
        ? `<span class="ban-status-pill banned">${L("⛔ Suspended", "⛔ Dibekukan")}</span>${u.banReason ? `<div class="ban-reason-note">${escapeHtml(u.banReason)}</div>` : ""}`
        : `<span class="ban-status-pill active">${L("● Active", "● Aktif")}</span>`;
      const actionBtn = isBanned
        ? `<button type="button" class="btn-unban-user" data-action="unban-user" data-uid="${escapeHtml(u.id)}">${L("Unban", "Cabut Ban")}</button>`
        : `<button type="button" class="btn-danger-ghost" data-action="open-ban" data-uid="${escapeHtml(u.id)}">${L("Suspend", "Bekukan")}</button>`;
      return `
      <tr>
        <td>
          <strong>${escapeHtml(u.name || L("Student", "Siswa"))}</strong>
          <div style="font-size:12px;color:var(--muted);">${emailLine}</div>
        </td>
        <td>${persona}<strong>${Number(u.totalXp) || 0} XP</strong></td>
        <td style="white-space: nowrap;">Lv.${Number(u.level) || 1} • 🔥 ${Number(u.currentStreak) || 0}${L("d", "h")}</td>
        <td>${statusHtml}</td>
        <td style="text-align:right; white-space: nowrap;">${actionBtn}</td>
      </tr>
    `;
    })
    .join("");
}

function downloadCsv(filename, headers, rows) {
  const escapeCell = (val) => `"${String(val ?? "").replace(/"/g, '""')}"`;
  const csv = [headers.map(escapeCell).join(","), ...rows.map((r) => r.map(escapeCell).join(","))].join("\n");
  const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

// ==========================================
// REVIEW MODAL LOGIC
// ==========================================
function openReviewModal(submissionId) {
  currentSubmission = submissions.find((s) => s.id === submissionId);
  if (!currentSubmission) return;

  const studentName = currentSubmission.studentName || currentSubmission.userName || L("Student", "Peserta");
  const studentEmail = currentSubmission.studentEmail || currentSubmission.userEmail || currentSubmission.accountId || "-";
  modalStudentName.textContent = studentName;
  const courseName = currentSubmission.courseTitle || getCourseLabel(currentSubmission.courseId);
  modalCourseTitle.textContent = `${courseName} • ID: ${currentSubmission.id}`;
  modalStudentEmail.textContent = studentEmail;
  modalStudentNotes.textContent =
    currentSubmission.studentNotes || currentSubmission.projectNotes || L("No additional notes provided by the student.", "Tidak ada catatan khusus dari peserta.");

  const safeProjectUrl = sanitizeExternalUrl(currentSubmission.projectUrl);
  if (safeProjectUrl) {
    btnProjectUrl.href = safeProjectUrl;
    btnProjectUrl.style.display = "inline-block";
  } else {
    btnProjectUrl.href = "#";
    btnProjectUrl.style.display = "none";
  }

  const code = currentSubmission.workspaceCode || L("<!-- No HTML/CSS/JS code attached -->", "<!-- Tidak ada kode HTML/CSS/JS yang dilampirkan -->");
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

  // Populate post-approval Student Review Rating section if student rated this review
  const modalRatingSection = document.getElementById("modalStudentRatingSection");
  const modalRatingStars = document.getElementById("modalStudentRatingStars");
  const modalRatingTags = document.getElementById("modalStudentRatingTags");
  const modalRatingComment = document.getElementById("modalStudentRatingComment");
  if (modalRatingSection) {
    const ratingInfo = getRatingForSubmission(currentSubmission);
    if (ratingInfo) {
      modalRatingSection.style.display = "block";
      if (modalRatingStars) {
        modalRatingStars.textContent = `${"★".repeat(ratingInfo.rating)}${"☆".repeat(5 - ratingInfo.rating)} ${ratingInfo.rating}/5`;
      }
      if (modalRatingTags) {
        modalRatingTags.innerHTML = (ratingInfo.tags || [])
          .map((tag) => `<span class="rating-tag-chip">${escapeHtml(getRatingTagLabel(tag))}</span>`)
          .join("");
      }
      if (modalRatingComment) {
        modalRatingComment.textContent = ratingInfo.comment
          ? `"${ratingInfo.comment}"`
          : L("Student submitted a star rating without an additional comment.", "Siswa memberikan rating bintang tanpa komentar tambahan.");
      }
    } else {
      modalRatingSection.style.display = "none";
    }
  }

  reviewModal.classList.add("open");
}

// ==========================================
// STUDENT REVIEW RATINGS HELPERS & RENDERER
// ==========================================
function getRatingTagLabel(key) {
  const map = {
    clear_feedback: L("Clear Feedback", "Umpan Balik Jelas"),
    fast_review: L("Fast Review", "Respons Cepat"),
    constructive_tips: L("Constructive Tips", "Saran Konstruktif"),
    fair_grading: L("Fair Grading", "Penilaian Adil"),
    very_motivating: L("Very Motivating", "Sangat Memotivasi")
  };
  return map[key] || key;
}

function normalizeRatingTags(raw) {
  if (Array.isArray(raw)) return raw.filter(Boolean).map(String);
  if (typeof raw === "string" && raw.trim().startsWith("[")) {
    try {
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed.filter(Boolean).map(String) : [];
    } catch (_) {
      return [];
    }
  }
  return [];
}

function getAllNormalizedRatings() {
  const byKey = new Map();
  submissionRatings.forEach((r) => {
    const score = Number(r.rating);
    if (!score || score < 1 || score > 5) return;
    const key = r.submissionDocId || r.submissionId || r.id;
    byKey.set(key, {
      id: r.id || key,
      submissionId: r.submissionId || key,
      submissionDocId: r.submissionDocId || r.id || key,
      studentName: r.studentName || L("Student", "Peserta"),
      courseId: r.courseId || "",
      courseTitle: r.courseTitle || getCourseLabel(r.courseId),
      facilitatorName: r.facilitatorName || "",
      rating: Math.max(1, Math.min(5, Math.round(score))),
      comment: (r.comment || "").trim(),
      tags: normalizeRatingTags(r.tags),
      ratedAt: Number(r.ratedAt) || 0
    });
  });

  submissions.forEach((s) => {
    const score = Number(s.studentRating);
    if (!score || score < 1 || score > 5) return;
    const key = s.id || s.submissionId;
    if (!byKey.has(key) && (!s.submissionId || !byKey.has(s.submissionId))) {
      byKey.set(key, {
        id: key,
        submissionId: s.submissionId || key,
        submissionDocId: s.id || key,
        studentName: s.studentName || s.userName || L("Student", "Peserta"),
        courseId: s.courseId || "",
        courseTitle: s.courseTitle || getCourseLabel(s.courseId),
        facilitatorName: s.facilitatorName || "",
        rating: Math.max(1, Math.min(5, Math.round(score))),
        comment: (s.studentRatingComment || "").trim(),
        tags: normalizeRatingTags(s.studentRatingTags || s.studentRatingTagsJson),
        ratedAt: Number(s.ratedAt) || 0
      });
    }
  });

  return Array.from(byKey.values()).sort((a, b) => (b.ratedAt || 0) - (a.ratedAt || 0));
}

function getRatingForSubmission(sub) {
  if (!sub) return null;
  const direct = Number(sub.studentRating);
  if (direct >= 1 && direct <= 5) {
    return {
      rating: Math.round(direct),
      comment: (sub.studentRatingComment || "").trim(),
      tags: normalizeRatingTags(sub.studentRatingTags || sub.studentRatingTagsJson),
      ratedAt: Number(sub.ratedAt) || 0
    };
  }
  const match = submissionRatings.find(
    (r) =>
      (r.submissionDocId && r.submissionDocId === sub.id) ||
      (r.submissionId && sub.submissionId && r.submissionId === sub.submissionId) ||
      r.id === sub.id
  );
  if (match && Number(match.rating) >= 1 && Number(match.rating) <= 5) {
    return {
      rating: Math.round(Number(match.rating)),
      comment: (match.comment || "").trim(),
      tags: normalizeRatingTags(match.tags),
      ratedAt: Number(match.ratedAt) || 0
    };
  }
  return null;
}

function renderRatingsSummary() {
  const ratings = getAllNormalizedRatings();
  const avgText = ratings.length
    ? `★ ${(ratings.reduce((sum, r) => sum + r.rating, 0) / ratings.length).toFixed(1)} (${ratings.length})`
    : "★ —";

  const railAvgEl = document.getElementById("railRatingsAvg");
  const railListEl = document.getElementById("railRatingsList");
  const anaBadgeEl = document.getElementById("analyticsRatingsBadge");
  const anaFeedEl = document.getElementById("analyticsRatingsFeed");

  if (railAvgEl) railAvgEl.textContent = avgText;
  if (anaBadgeEl) anaBadgeEl.textContent = avgText;

  const renderCardHtml = (r) => {
    const tagsHtml = (r.tags || [])
      .slice(0, 3)
      .map((t) => `<span class="rating-tag-chip">${escapeHtml(getRatingTagLabel(t))}</span>`)
      .join("");
    const commentHtml = r.comment
      ? `<p class="rating-feed-comment">"${escapeHtml(r.comment)}"</p>`
      : "";
    const metaParts = [r.courseTitle || getCourseLabel(r.courseId)];
    if (r.facilitatorName) metaParts.push(r.facilitatorName);
    if (r.ratedAt) metaParts.push(formatDate(r.ratedAt));
    return `
      <article class="rating-feed-item">
        <div class="rating-feed-top">
          <span class="rating-feed-student">${escapeHtml(r.studentName)}</span>
          <span class="rating-star-pill">★ ${r.rating}/5</span>
        </div>
        <div class="rating-feed-meta">${escapeHtml(metaParts.join(" • "))}</div>
        ${tagsHtml ? `<div class="rating-tags-row">${tagsHtml}</div>` : ""}
        ${commentHtml}
      </article>
    `;
  };

  if (railListEl) {
    if (ratings.length === 0) {
      railListEl.innerHTML = `<div class="rail-empty">${L("No student review ratings yet.", "Belum ada rating review dari siswa.")}</div>`;
    } else {
      railListEl.innerHTML = ratings.slice(0, 4).map(renderCardHtml).join("");
    }
  }

  if (anaFeedEl) {
    if (ratings.length === 0) {
      anaFeedEl.innerHTML = `<div class="rail-empty">${L("No student ratings submitted yet.", "Belum ada rating yang dikirim siswa.")}</div>`;
    } else {
      anaFeedEl.innerHTML = ratings.slice(0, 12).map(renderCardHtml).join("");
    }
  }
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
  const facName = facilitatorNameInput.value.trim() || L("CodeAcademy Facilitator Team", "Tim Fasilitator CodeAcademy");

  if (newStatus === "REVISION_REQUESTED" && feedback.length < 5) {
    alert(L("Please write clear revision notes so the student knows what to improve.", "Mohon tuliskan catatan evaluasi yang jelas agar peserta mengetahui hal yang harus diperbaiki."));
    facilitatorFeedback.focus();
    return;
  }

  if (newStatus === "APPROVED" && finalScore < 70) {
    alert(L(
      `Current Final Score is ${finalScore}/100 (minimum 70 required to pass & issue a certificate). Raise the project score or request revision.`,
      `Nilai Akhir saat ini ${finalScore}/100 (minimal 70 untuk lulus & menerbitkan sertifikat). Tingkatkan skor proyek atau minta revisi.`
    ));
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
    (newStatus === "APPROVED" ? L("Project meets CodeAcademy graduation standards.", "Proyek memenuhi standar kelulusan CodeAcademy.") : "");
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

    if (isFirebaseReady && db) {
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
        ? L(`Approved ${studentName}'s project! Certificate ${certId} issued.`, `Proyek ${studentName} Disetujui! Sertifikat ${certId} diterbitkan.`)
        : L(`Revision request sent to ${studentName}.`, `Permintaan Revisi dikirim ke ${studentName}.`);

    closeReviewModal();
    showToast(msg);
  } catch (err) {
    console.error("Failed to save review:", err);
    showToast(L("Failed to save evaluation: ", "Gagal menyimpan evaluasi: ") + err.message);
  } finally {
    btnApprove.disabled = false;
    btnRequestRevision.disabled = false;
  }
}

// ==========================================
// FORUM DISCUSSION VIEW & MODERATION
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
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
        </div>
        <h4>${forumOnlyUnanswered ? L("All Questions Answered", "Semua Pertanyaan Sudah Terjawab") : L("No Discussions Yet", "Belum Ada Diskusi")}</h4>
        <p>${forumOnlyUnanswered ? L("Every discussion topic has received a Facilitator reply.", "Semua pertanyaan diskusi sudah mendapat jawaban Fasilitator.") : L("No discussion threads match your current filter.", "Belum ada topik diskusi yang sesuai dengan filter ini.")}</p>
      </div>
    `;
    return;
  }

  forumThreadsList.innerHTML = filtered
    .map((thread) => {
      const replies = (repliesByRoot.get(thread.postId) || [])
        .slice()
        .sort((a, b) => ((b.isVerifiedAnswer ? 1 : 0) - (a.isVerifiedAnswer ? 1 : 0)) || (a.timestamp - b.timestamp));
      const initial = (thread.authorName || "S").trim().charAt(0).toUpperCase();
      const courseName = getCourseLabel(thread.courseId);
      const hasVerifiedAnswer = replies.some((r) => r.isVerifiedAnswer === true);

      const repliesHtml = replies
        .map((r) => {
          const rInitial = (r.authorName || "S").trim().charAt(0).toUpperCase();
          const rIsMentor = r.authorIsMentor === true || r.isMentor === true;
          const rIsVerified = r.isVerifiedAnswer === true;
          const rIsHelpful = r.isHelpful === true;
          const hasSpecificQuote = Boolean(r.replyToAuthorName && r.replyToPostId && r.replyToPostId !== thread.postId);
          const quoteHtml = hasSpecificQuote
            ? `<div class="forum-reply-quote">${L("Replying to", "Membalas")} <strong>@${escapeHtml(r.replyToAuthorName)}</strong>${r.replyToContentSnippet ? `: “${escapeHtml(r.replyToContentSnippet)}”` : ""}</div>`
            : "";
          return `
        <div class="forum-reply-card ${rIsMentor ? "mentor" : ""} ${rIsVerified ? "verified" : ""}">
          <div class="forum-post-head" style="margin-bottom: 6px;">
            <div class="forum-author-meta">
              <div class="forum-avatar ${rIsMentor ? "mentor" : ""}" style="width:26px;height:26px;font-size:11px;">${escapeHtml(rInitial)}</div>
              <div>
                <strong style="font-size:13px;">${escapeHtml(r.authorName || L("Student", "Peserta"))}</strong>
                ${rIsMentor ? `<span class="forum-mentor-badge">${L("Facilitator", "Fasilitator")}</span>` : ""}
                ${rIsVerified ? `<span class="forum-verified-badge">${L("✓ Verified Answer", "✓ Jawaban Terverifikasi")}</span>` : ""}
                ${rIsHelpful ? `<span class="forum-helpful-badge">${L("💡 Helpful", "💡 Membantu")}</span>` : ""}
                <span style="font-size:11.5px; color:var(--muted); margin-left:6px;">${escapeHtml(formatDate(r.timestamp))}</span>
              </div>
            </div>
            <div class="forum-reply-actions">
              <button type="button" class="btn-action-chip ${rIsVerified ? "active-verified" : ""}" data-action="toggle-verified" data-post-id="${escapeHtml(r.postId)}" title="${L("Pin as Facilitator Verified Answer", "Pin sebagai Jawaban Terverifikasi Fasilitator")}">${rIsVerified ? L("✓ Pinned", "✓ Terpin") : "📌 Pin"}</button>
              <button type="button" class="btn-action-chip ${rIsHelpful ? "active-helpful" : ""}" data-action="toggle-helpful" data-post-id="${escapeHtml(r.postId)}" title="${L("Mark answer as helpful", "Tandai jawaban membantu")}">💡</button>
              <button type="button" class="btn-reply-ghost" data-action="target-reply" data-thread-id="${escapeHtml(thread.postId)}" data-post-id="${escapeHtml(r.postId)}">${L("Reply", "Balas")}</button>
              <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(r.postId)}">${L("Delete", "Hapus")}</button>
            </div>
          </div>
          ${quoteHtml}
          <div style="font-size:13.5px; line-height:1.5; white-space:pre-wrap;">${escapeHtml(r.content || "")}</div>
        </div>
      `;
        })
        .join("");

      const tIsMentor = thread.authorIsMentor === true || thread.isMentor === true;
      const activeTarget = activeReplyTargetByThread.get(thread.postId);
      const targetBarHtml = activeTarget
        ? `<div class="forum-reply-target-bar">
            <span>${L("Replying to", "Membalas")} <strong>@${escapeHtml(activeTarget.authorName)}</strong>${activeTarget.contentSnippet ? `: “${escapeHtml(activeTarget.contentSnippet)}”` : ""}</span>
            <button type="button" class="forum-reply-target-clear" data-action="clear-reply-target" data-thread-id="${escapeHtml(thread.postId)}" aria-label="${L("Cancel targeted reply", "Batal balas spesifik")}">×</button>
          </div>`
        : "";
      const replyPlaceholder = activeTarget
        ? L(`Reply to @${activeTarget.authorName}...`, `Balas @${activeTarget.authorName}...`)
        : L("Reply as Facilitator (appears immediately in the app)...", "Balas sebagai Fasilitator (muncul langsung di aplikasi)...");
      return `
      <article class="forum-thread-card">
        <div class="forum-post-head">
          <div class="forum-author-meta">
            <div class="forum-avatar ${tIsMentor ? "mentor" : ""}">${escapeHtml(initial)}</div>
            <div>
              <strong>${escapeHtml(thread.authorName || L("Student", "Peserta"))}</strong>
              ${tIsMentor ? `<span class="forum-mentor-badge">${L("Facilitator", "Fasilitator")}</span>` : `<span style="font-size:11.5px;color:var(--muted);">• Lv.${Number(thread.authorLevel) || 1}</span>`}
              <span class="forum-course-pill">${escapeHtml(courseName)}</span>
              ${hasVerifiedAnswer ? `<span class="forum-resolved-pill">${L("✓ Resolved", "✓ Terjawab")}</span>` : ""}
              <div style="font-size:12px; color:var(--muted);">${escapeHtml(formatDate(thread.timestamp))} • ${Number(thread.likeCount) || 0} ${L("likes", "suka")} • ${replies.length} ${L("replies", "balasan")}</div>
            </div>
          </div>
          <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(thread.postId)}">${L("Delete Thread", "Hapus Thread")}</button>
        </div>
        <div class="forum-post-body">${escapeHtml(thread.content || "")}</div>
        ${replies.length > 0 ? `<div class="forum-replies-wrap">${repliesHtml}</div>` : ""}
        ${targetBarHtml}
        <form class="forum-reply-form" data-action="reply-thread" data-thread-id="${escapeHtml(thread.postId)}">
          <input type="text" placeholder="${escapeHtml(replyPlaceholder)}" required>
          <button type="submit" class="btn-primary-solid">${L("Reply", "Balas")}</button>
        </form>
      </article>
    `;
    })
    .join("");
  renderHuddlyDashboardMeta();
}

async function createFacilitatorTopic() {
  const content = (forumNewPostInput.value || "").trim();
  const courseId = forumNewPostCourse.value || "fe-web-foundation-00";
  if (content.length < 4) {
    showToast(L("Please write at least 4 characters for the topic.", "Tulis isi topik diskusi minimal 4 karakter."));
    return;
  }

  const facName = facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || L("CodeAcademy Facilitator Team", "Tim Fasilitator CodeAcademy");
  const postId = "fac_" + Date.now() + "_" + Math.random().toString(36).slice(2, 7);
  const uid = currentFacilitatorUser?.uid || "facilitator_web";

  const docData = {
    postId,
    courseId,
    firebaseUid: uid,
    authorId: uid,
    authorName: facName,
    authorLevel: 99,
    authorIsMentor: true,
    isMentor: true,
    authorAccessRole: currentFacilitatorUser?.accessRole || "facilitator",
    content,
    timestamp: Date.now(),
    likeCount: 0,
    likedByAccountIds: [],
    replyToPostId: null,
    replyToAuthorName: null,
    replyToContentSnippet: null,
    rootThreadId: null
  };

  if (isFirebaseReady && db) {
    try {
      await db.collection("forum_posts").doc(postId).set(docData);
      forumNewPostInput.value = "";
      showToast(L("Facilitator topic published to the app.", "Topik fasilitator berhasil diterbitkan ke aplikasi."));
    } catch (err) {
      console.error("Create topic error:", err);
      showToast(L("Failed to create topic: ", "Gagal membuat topik: ") + err.message);
    }
  } else {
    forumPosts.unshift(docData);
    forumNewPostInput.value = "";
    renderForum();
    showToast(L("Topic added (Offline Mode).", "Topik ditambahkan (Mode Demo Offline)."));
  }
}

async function replyToThread(form) {
  const rootPostId = form.dataset.threadId;
  const input = form.querySelector("input");
  const content = (input?.value || "").trim();
  if (!content) return;

  const root = forumPosts.find((p) => p.postId === rootPostId);
  if (!root) return;

  const facName = facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || L("CodeAcademy Facilitator Team", "Tim Fasilitator CodeAcademy");
  const postId = "fac_reply_" + Date.now() + "_" + Math.random().toString(36).slice(2, 7);
  const uid = currentFacilitatorUser?.uid || "facilitator_web";
  const customTarget = activeReplyTargetByThread.get(rootPostId);
  const resolvedTarget = customTarget
    ? forumPosts.find((p) => p.postId === customTarget.postId) || customTarget
    : root;

  const replyDoc = {
    postId,
    courseId: root.courseId,
    firebaseUid: uid,
    authorId: uid,
    authorName: facName,
    authorLevel: 99,
    authorIsMentor: true,
    isMentor: true,
    authorAccessRole: currentFacilitatorUser?.accessRole || "facilitator",
    content,
    timestamp: Date.now(),
    likeCount: 0,
    likedByAccountIds: [],
    replyToPostId: resolvedTarget.postId,
    replyToAuthorName: resolvedTarget.authorName || L("Student", "Peserta"),
    replyToContentSnippet: (resolvedTarget.content || resolvedTarget.contentSnippet || "").slice(0, 80),
    rootThreadId: root.rootThreadId || root.postId
  };

  if (isFirebaseReady && db) {
    try {
      await db.collection("forum_posts").doc(postId).set(replyDoc);
      activeReplyTargetByThread.delete(rootPostId);
      input.value = "";
      showToast(L("Facilitator reply sent.", "Balasan fasilitator terkirim."));
    } catch (err) {
      console.error("Reply thread error:", err);
      showToast(L("Failed to send reply: ", "Gagal mengirim balasan: ") + err.message);
    }
  } else {
    activeReplyTargetByThread.delete(rootPostId);
    forumPosts.push(replyDoc);
    input.value = "";
    renderForum();
    showToast(L("Reply added (Offline Mode).", "Balasan ditambahkan (Mode Demo Offline)."));
  }
}

async function toggleForumPostFlag(postId, field) {
  const target = forumPosts.find((p) => p.postId === postId);
  if (!target) return;
  const nextVal = !(target[field] === true);
  if (isFirebaseReady && db) {
    try {
      await db.collection("forum_posts").doc(postId).update({ [field]: nextVal });
      showToast(
        field === "isVerifiedAnswer"
          ? (nextVal ? L("Pinned as Facilitator Verified Answer.", "Jawaban dipin & ditandai Terverifikasi Fasilitator.") : L("Verified answer pin removed.", "Pin jawaban terverifikasi dilepas."))
          : (nextVal ? L("Marked as Helpful Answer.", "Ditandai sebagai Jawaban Membantu.") : L("Helpful badge removed.", "Tanda Membantu dilepas."))
      );
    } catch (err) {
      console.error("Toggle forum flag error:", err);
      showToast(L("Failed to update answer status: ", "Gagal memperbarui status jawaban: ") + err.message);
    }
  } else {
    target[field] = nextVal;
    renderForum();
    showToast(
      field === "isVerifiedAnswer"
        ? (nextVal ? L("Answer pinned (Offline Mode).", "Jawaban dipin (Mode Demo Offline).") : L("Pin removed (Offline Mode).", "Pin dilepas (Mode Demo Offline)."))
        : (nextVal ? L("Marked Helpful (Offline Mode).", "Ditandai Membantu (Mode Demo Offline).") : L("Helpful badge removed (Offline Mode).", "Tanda Membantu dilepas (Mode Demo Offline)."))
    );
  }
}

async function deleteForumPost(postId) {
  if (!confirm(L("Permanently delete this discussion post/thread?", "Hapus postingan/thread diskusi ini secara permanen?"))) return;

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

  if (isFirebaseReady && db) {
    try {
      const ids = Array.from(toDeleteIds);
      for (let i = 0; i < ids.length; i += 400) {
        const batch = db.batch();
        ids.slice(i, i + 400).forEach((id) => {
          batch.delete(db.collection("forum_posts").doc(id));
        });
        await batch.commit();
      }
      showToast(L(`Deleted ${ids.length} post(s).`, `Postingan (${ids.length} item) berhasil dihapus.`));
    } catch (err) {
      console.error("Delete post error:", err);
      showToast(L("Failed to delete: ", "Gagal menghapus: ") + err.message);
    }
  } else {
    forumPosts = forumPosts.filter((p) => !toDeleteIds.has(p.postId));
    renderForum();
    showToast(L("Post deleted (Offline Mode).", "Postingan dihapus (Mode Demo Offline)."));
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
    announcementsList.innerHTML = `
      <div class="forum-empty">
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
        </div>
        <h4>${L("No Announcements Yet", "Belum Ada Pengumuman")}</h4>
        <p>${L("Publish an announcement above to deliver it straight to learners' Android Notifications.", "Terbitkan pengumuman di atas agar langsung muncul di layar Notifikasi peserta Android.")}</p>
      </div>
    `;
    return;
  }

  announcementsList.innerHTML = announcements
    .map((ann) => {
      const courseLabel = ann.courseId && ann.courseId !== "all" ? getCourseLabel(ann.courseId) : L("All Courses / General", "Semua Kelas / Umum");
      const annText = ann.message || ann.body || "";
      return `
      <article class="announcement-item">
        <div style="flex: 1; min-width: 240px;">
          <div style="display:flex; align-items:center; gap:8px; flex-wrap:wrap; margin-bottom:6px;">
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(ann.title || L("Announcement", "Pengumuman"))}</strong>
            <span class="forum-course-pill">${escapeHtml(courseLabel)}</span>
          </div>
          <p style="font-size:13.5px; color:var(--ink); line-height:1.5; white-space:pre-wrap;">${escapeHtml(annText)}</p>
          <div style="font-size:12px; color:var(--muted); margin-top:8px;">
            ${L("By", "Oleh")} ${escapeHtml(ann.authorName || L("Facilitator", "Fasilitator"))} • ${escapeHtml(formatDate(ann.createdAt))}
          </div>
        </div>
        <button type="button" class="btn-danger-ghost" data-action="delete-announcement" data-id="${escapeHtml(ann.id)}">${L("Delete", "Hapus")}</button>
      </article>
    `;
    })
    .join("");
  renderHuddlyDashboardMeta();
}

async function publishAnnouncement() {
  const title = (announcementTitleInput.value || "").trim();
  const body = (announcementBodyInput.value || "").trim();
  const courseId = announcementCourseSelect.value || "all";

  if (title.length < 3 || body.length < 5) {
    showToast(L("Please fill in both announcement title and message.", "Isi judul dan pesan pengumuman dengan lengkap."));
    return;
  }

  const annId = "ann_" + Date.now() + "_" + Math.random().toString(36).slice(2, 6);
  const docData = {
    id: annId,
    title,
    message: body,
    body,
    courseId,
    courseTitle: courseId === "all" ? L("All Courses / General", "Semua Kelas / Umum") : getCourseLabel(courseId),
    authorName: facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || L("CodeAcademy Facilitator Team", "Tim Fasilitator CodeAcademy"),
    authorEmail: currentFacilitatorUser?.email || "",
    firebaseUid: currentFacilitatorUser?.uid || "",
    createdAt: Date.now()
  };

  if (isFirebaseReady && db) {
    try {
      await db.collection("announcements").doc(annId).set(docData);
      announcementTitleInput.value = "";
      announcementBodyInput.value = "";
      showToast(L("Announcement published to the Android app.", "Pengumuman berhasil diterbitkan ke aplikasi Android."));
    } catch (err) {
      console.error("Publish announcement error:", err);
      showToast(L("Failed to publish announcement: ", "Gagal menerbitkan pengumuman: ") + err.message);
    }
  } else {
    announcements.unshift(docData);
    announcementTitleInput.value = "";
    announcementBodyInput.value = "";
    renderAnnouncements();
    showToast(L("Announcement added (Offline Mode).", "Pengumuman ditambahkan (Mode Demo Offline)."));
  }
}

async function deleteAnnouncement(id) {
  if (!confirm(L("Delete this announcement?", "Hapus pengumuman ini?"))) return;
  if (isFirebaseReady && db) {
    try {
      await db.collection("announcements").doc(id).delete();
      showToast(L("Announcement deleted.", "Pengumuman dihapus."));
    } catch (err) {
      console.error("Delete announcement error:", err);
      showToast(L("Failed to delete announcement: ", "Gagal menghapus pengumuman: ") + err.message);
    }
  } else {
    announcements = announcements.filter((a) => a.id !== id);
    renderAnnouncements();
    showToast(L("Announcement deleted (Offline Mode).", "Pengumuman dihapus (Mode Demo Offline)."));
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
    certificatesList.innerHTML = `
      <div class="forum-empty">
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><circle cx="12" cy="8" r="6"/><path d="M15.477 12.89 17 22l-5-3-5 3 1.523-9.11"/></svg>
        </div>
        <h4>${L("No Certificates Issued Yet", "Belum Ada Sertifikat Terbit")}</h4>
        <p>${L("Official CC- credentials appear here automatically once a student's final project is approved.", "Sertifikat resmi berawalan CC- akan otomatis muncul di sini saat proyek akhir peserta disetujui.")}</p>
      </div>
    `;
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
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(c.userName || L("Student", "Peserta"))}</strong>
          </div>
          <div style="font-size:13.5px; color:var(--ink); font-weight:600;">${escapeHtml(courseName)}</div>
          <div style="font-size:12px; color:var(--muted); margin-top:4px;">
            ${L("Final Score", "Nilai Akhir")}: <strong>${Number(c.finalScore) || 0}/100</strong> (${L("Quiz", "Kuis")} ${Number(c.quizScore) || 0} • ${L("Project", "Proyek")} ${Number(c.facilitatorScore) || 0}) • ${L("Issued", "Terbit")} ${escapeHtml(formatDate(c.issuedAt))}
          </div>
        </div>
        <div class="forum-reply-actions">
          <a href="${verifyHref}" target="_blank" rel="noopener noreferrer" class="btn-review" style="text-decoration:none;">
            ${L("Open Verification Page ↗", "Buka Halaman Verifikasi ↗")}
          </a>
          ${currentAccessRole === "super_admin" ? `<button type="button" class="btn-danger-ghost" data-action="revoke-cert" data-cert-id="${escapeHtml(c.certId || "")}">${L("Revoke", "Cabut")}</button>` : ""}
        </div>
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
  return d.toLocaleDateString(isIndonesian() ? "id-ID" : "en-US", {
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

// ==========================================
// ACCESS REQUEST MODAL (ADMIN / FASILITATOR)
// ==========================================
function generateAccessRequestId() {
  const now = new Date();
  const y = now.getFullYear();
  const m = String(now.getMonth() + 1).padStart(2, "0");
  const d = String(now.getDate()).padStart(2, "0");
  const rand = Math.random().toString(36).substring(2, 6).toUpperCase();
  return `REQ-${y}${m}${d}-${rand}`;
}

function buildFormattedAccessEmailText(payload) {
  return [
    "PENGAJUAN AKSES PORTAL CODEACADEMY",
    "========================================",
    `ID Referensi     : ${payload.requestId}`,
    `Peran Diajukan   : ${payload.role}`,
    `Nama Lengkap     : ${payload.name}`,
    `Email Didaftarkan: ${payload.email}`,
    `Instansi/Jabatan : ${payload.affiliation}`,
    `Waktu Pengajuan  : ${payload.submittedAt}`,
    `URL Portal       : ${payload.portalUrl}`,
    "",
    "Catatan Pemohon:",
    payload.reason,
    "",
    "----------------------------------------",
    "Tindak lanjut: Masuk ke Portal Admin CodeAcademy > Manajemen Tim,",
    `lalu tambahkan ${payload.email} sebagai ${payload.role}.`
  ].join("\n");
}

function attachAccessRequestModalListeners() {
  const modal = document.getElementById("accessRequestModal");
  const btnOpen = document.getElementById("btnOpenAccessRequest");
  const btnClose = document.getElementById("btnCloseAccessRequest");
  const btnCancel = document.getElementById("btnCancelAccessRequest");
  const form = document.getElementById("accessRequestForm");
  const feedbackEl = document.getElementById("reqFeedback");
  const btnSubmit = document.getElementById("btnSubmitAccessRequest");

  if (!modal || !btnOpen || !form) return;

  const setFeedback = (type, html) => {
    if (!feedbackEl) return;
    feedbackEl.className = type ? `req-feedback ${type}` : "req-feedback";
    feedbackEl.innerHTML = html || "";
  };

  const openModal = () => {
    setFeedback("", "");
    modal.classList.add("open");
    modal.setAttribute("aria-hidden", "false");
    const nameInput = document.getElementById("reqFullName");
    if (nameInput) nameInput.focus();
  };

  const closeModal = () => {
    modal.classList.remove("open");
    modal.setAttribute("aria-hidden", "true");
  };

  btnOpen.addEventListener("click", openModal);
  if (btnClose) btnClose.addEventListener("click", closeModal);
  if (btnCancel) btnCancel.addEventListener("click", closeModal);
  modal.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fullName = (document.getElementById("reqFullName")?.value || "").trim();
    const applicantEmail = (document.getElementById("reqEmail")?.value || "").trim().toLowerCase();
    const affiliation = (document.getElementById("reqAffiliation")?.value || "").trim();
    const reason = (document.getElementById("reqReason")?.value || "").trim();
    const selectedRole = form.querySelector('input[name="reqRole"]:checked')?.value || "Fasilitator";

    if (!fullName || !applicantEmail || !affiliation || !reason) {
      setFeedback("error", "Harap lengkapi seluruh kolom pengajuan terlebih dahulu.");
      return;
    }

    const requestId = generateAccessRequestId();
    const submittedAt = new Date().toLocaleString("id-ID", {
      dateStyle: "medium",
      timeStyle: "short"
    }) + " WIB";
    const portalUrl = window.location.href.split("#")[0];
    const subject = `[CodeAcademy Portal] Pengajuan Akses ${selectedRole} — ${fullName} (${requestId})`;

    const payload = {
      requestId,
      role: selectedRole,
      name: fullName,
      email: applicantEmail,
      affiliation,
      reason,
      submittedAt,
      portalUrl
    };

    const emailBody = buildFormattedAccessEmailText(payload);
    const mailtoHref = `mailto:${ACCESS_REQUEST_RECIPIENT}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(emailBody)}`;
    const gmailWebHref = `https://mail.google.com/mail/?view=cm&fs=1&to=${encodeURIComponent(
      ACCESS_REQUEST_RECIPIENT
    )}&su=${encodeURIComponent(subject)}&body=${encodeURIComponent(emailBody)}`;
    const isId = (window.CodeAcademyWebI18n?.getLang?.() || "en") === "id";

    if (btnSubmit) {
      btnSubmit.disabled = true;
      btnSubmit.textContent = isId ? "Menyimpan pengajuan..." : "Submitting request...";
    }
    setFeedback(
      "info",
      isId
        ? `Menyimpan pengajuan akses (${escapeHtml(requestId)})...`
        : `Saving access request (${escapeHtml(requestId)})...`
    );

    let savedToFirestore = false;
    if (db) {
      try {
        await db.collection("access_requests").doc(requestId).set({
          requestId,
          name: fullName,
          email: applicantEmail,
          role: selectedRole,
          affiliation,
          reason,
          portalUrl,
          recipientEmail: ACCESS_REQUEST_RECIPIENT,
          status: "PENDING",
          createdAt: Date.now()
        });
        savedToFirestore = true;
      } catch (fsErr) {
        console.warn("[AccessRequest] Firestore save skipped:", fsErr?.code || fsErr?.message);
      }
    }

    const statusLine = savedToFirestore
      ? isId
        ? `Tercatat di sistem CodeAcademy (${escapeHtml(requestId)}). Klik tombol di bawah untuk langsung mengirim email konfirmasi ke <strong>${ACCESS_REQUEST_RECIPIENT}</strong>:`
        : `Recorded in CodeAcademy registry (${escapeHtml(requestId)}). Click below to send the formatted email directly to <strong>${ACCESS_REQUEST_RECIPIENT}</strong>:`
      : isId
        ? `Template pengajuan (${escapeHtml(requestId)}) siap dikirim ke <strong>${ACCESS_REQUEST_RECIPIENT}</strong>. Pilih metode pengiriman di bawah:`
        : `Formatted request (${escapeHtml(requestId)}) is ready for <strong>${ACCESS_REQUEST_RECIPIENT}</strong>. Choose how to send below:`;

    setFeedback(
      "success",
      `<strong>${isId ? "Pengajuan Siap Dikirim" : "Request Ready to Send"} (${escapeHtml(requestId)})</strong><br>` +
        `${statusLine}` +
        `<div style="display:flex;gap:8px;margin-top:10px;flex-wrap:wrap;">` +
        `<a href="${gmailWebHref}" target="_blank" rel="noopener" style="display:inline-flex;align-items:center;padding:8px 13px;border-radius:9px;background:#181A1F;color:#FFFFFF;font-weight:700;font-size:12px;text-decoration:none;">${isId ? "Kirim via Gmail Web" : "Send via Gmail Web"}</a>` +
        `<a href="${mailtoHref}" style="display:inline-flex;align-items:center;padding:8px 13px;border-radius:9px;border:1px solid #CBD5E1;background:#FFFFFF;color:#0F172A;font-weight:600;font-size:12px;text-decoration:none;">${isId ? "Aplikasi Email" : "Email App"}</a>` +
        `<button type="button" id="btnCopyAccessReqText" style="display:inline-flex;align-items:center;padding:8px 13px;border-radius:9px;border:1px solid #CBD5E1;background:#FFFFFF;color:#0F766E;font-weight:700;font-size:12px;cursor:pointer;">${isId ? "Salin Isi Email" : "Copy Email Text"}</button>` +
        `</div>`
    );

    const copyBtn = document.getElementById("btnCopyAccessReqText");
    if (copyBtn) {
      copyBtn.addEventListener("click", async () => {
        try {
          await navigator.clipboard.writeText(`To: ${ACCESS_REQUEST_RECIPIENT}\nSubject: ${subject}\n\n${emailBody}`);
          copyBtn.textContent = isId ? "Tersalin!" : "Copied!";
          showToast(isId ? "Isi email pengajuan disalin." : "Access request email copied.");
        } catch (_) {}
      });
    }

    showToast(isId ? `Pengajuan ${requestId} siap dikirim.` : `Request ${requestId} ready.`);

    if (btnSubmit) {
      btnSubmit.disabled = false;
      btnSubmit.textContent = isId ? "Kirim Pengajuan Akses" : "Submit Access Request";
    }
  });
}
