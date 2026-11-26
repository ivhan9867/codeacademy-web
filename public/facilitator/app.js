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

let submissions = [];
let forumPosts = [];
let announcements = [];
let certificates = [];
let facilitatorsList = [];
let studentUsersList = [];

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
  let timeGreeting = "Selamat malam";
  if (hour >= 4 && hour < 11) timeGreeting = "Selamat pagi";
  else if (hour >= 11 && hour < 15) timeGreeting = "Selamat siang";
  else if (hour >= 15 && hour < 18) timeGreeting = "Selamat sore";
  const shortName = (displayName || "Fasilitator").trim().split("—")[0].trim();
  greetingEl.textContent = `${timeGreeting}, ${shortName}`;
}

function applyFacilitatorProfileToUi(displayName, role = "") {
  const cleanName = (displayName || "Fasilitator CodeAcademy").trim();
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
    roleBadge.textContent = isAdmin ? "👑 Super Admin" : "🎓 Fasilitator";
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
      const msg = "Akun tidak memiliki alamat email yang valid.";
      setAuthStatus("");
      showAuthError(msg);
      await auth.signOut();
      return;
    }

    try {
      setAuthStatus(`Akun terautentikasi (${email}). Menyegarkan token sesi...`);
      try {
        await user.getIdToken(true);
      } catch (tokErr) {
        console.warn("[FacilitatorAuth] getIdToken(true) warning:", tokErr?.code || tokErr?.message);
      }

      setAuthStatus(`Memverifikasi izin fasilitator di Firestore: facilitators/${email}...`);
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
        const msg = `Akses ditolak: dokumen Firestore facilitators/${email} tidak ditemukan. Gunakan tombol "Ajukan Akses" di bawah form login untuk mendaftarkan email Anda.`;
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
      const resolvedDisplayName = savedLocal.name || profileData.name || user.displayName || "Fasilitator CodeAcademy";
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
      showToast(`Masuk sebagai ${email}`);
    } catch (err) {
      console.error("[FacilitatorAuth] Verify facilitator failed:", err.code, err.message, err);
      setAuthStatus("");
      showAuthError(
        "Gagal memverifikasi izin fasilitator (" + (err.code || err.message || "permission-denied") + "). Pastikan Firestore Rules terbaru sudah di-Publish."
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
  unsubSubmissions = null;
  unsubForum = null;
  unsubAnnouncements = null;
  unsubCertificates = null;
  unsubTeam = null;
  unsubStudents = null;
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
      showAuthError("Firebase Auth tidak tersedia. Muat ulang halaman dan coba lagi.");
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

  attachAccessRequestModalListeners();

  btnGoogleSignIn.addEventListener("click", async () => {
    console.log("[FacilitatorAuth] Google Sign-In clicked");
    if (!auth) {
      showAuthError("Firebase Auth tidak tersedia. Muat ulang halaman dan coba lagi.");
      return;
    }
    authError.classList.remove("show");
    setAuthStatus("Membuka popup login Google...");
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
        showAuthError("Popup login Google diblokir oleh browser. Izinkan popup untuk localhost di ikon gembok/address bar browser lalu klik lagi, atau gunakan Email & Kata Sandi.");
      } else if (err.code === "auth/popup-closed-by-user") {
        showAuthError("Jendela login Google ditutup sebelum selesai.");
      } else if (err.code === "auth/unauthorized-domain") {
        showAuthError(`Domain "${window.location.hostname}" belum ditambahkan di Firebase Console → Authentication → Settings → Authorized domains.`);
      } else {
        showAuthError("Login Google gagal (" + (err.code || "error") + "): " + (err.message || err.code));
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
      showToast("Nama fasilitator minimal 2 karakter.");
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
    showToast("Profil fasilitator berhasil diperbarui.");
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

  document.querySelectorAll(".quick-chip-btn[data-quick-prompt]").forEach((chip) => {
    chip.addEventListener("click", () => {
      const quickInput = document.getElementById("huddlyQuickInput");
      if (quickInput) quickInput.value = chip.dataset.quickPrompt || "";
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
      showToast("Masukkan alamat email Google yang valid.");
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
        showToast(`Anggota ${email} disimpan sebagai ${accessRole}.`);
      } catch (err) {
        showToast("Gagal menyimpan anggota: " + err.message);
      }
    } else {
      const idx = facilitatorsList.findIndex((f) => f.email === email);
      if (idx >= 0) facilitatorsList[idx] = { ...facilitatorsList[idx], ...payload };
      else facilitatorsList.unshift(payload);
      if (emailEl) emailEl.value = "";
      if (nameEl) nameEl.value = "";
      renderTeam();
      showToast(`Anggota ${email} ditambahkan (Mode Demo).`);
    }
  });

  document.getElementById("teamTableBody")?.addEventListener("click", async (e) => {
    const promoteBtn = e.target.closest("[data-action='promote-team']");
    if (promoteBtn && promoteBtn.dataset.email) {
      if (currentAccessRole !== "super_admin") {
        showToast("Hanya Super Admin yang dapat menaikkan peran tim.");
        return;
      }
      const email = promoteBtn.dataset.email;
      const target = facilitatorsList.find((f) => f.email === email);
      if (target?.accessRole === "super_admin") {
        showToast("Akun ini sudah berstatus Super Admin.");
        return;
      }
      if (!confirm(`Naikkan peran ${target?.name || email} (${email}) menjadi Super Admin? Sesama Super Admin tidak dapat diturunkan kembali.`)) {
        return;
      }
      if (isFirebaseReady && db) {
        try {
          await db.collection("facilitators").doc(email).update({
            accessRole: "super_admin",
            updatedAt: Date.now()
          });
          showToast(`${target?.name || email} berhasil dinaikkan menjadi Super Admin.`);
        } catch (err) {
          showToast("Gagal menaikkan peran: " + err.message);
        }
      }
      return;
    }

    const delBtn = e.target.closest("[data-action='remove-team']");
    if (!delBtn || !delBtn.dataset.email) return;
    const email = delBtn.dataset.email;
    if (email === currentFacilitatorUser?.email) {
      showToast("Tidak dapat menghapus akun sendiri.");
      return;
    }
    const target = facilitatorsList.find((f) => f.email === email);
    if (target?.accessRole === "super_admin") {
      showToast("Sesama Super Admin dilindungi dan tidak dapat diturunkan atau dihapus.");
      return;
    }
    if (!confirm(`Hapus akses portal untuk ${email}?`)) return;
    if (isFirebaseReady && db) {
      try {
        await db.collection("facilitators").doc(email).delete();
        showToast(`Akses ${email} dihapus.`);
      } catch (err) {
        showToast("Gagal menghapus anggota: " + err.message);
      }
    }
  });

  document.getElementById("btnExportCertCsv")?.addEventListener("click", () => {
    downloadCsv(
      "codeacademy-sertifikat.csv",
      ["Cert ID", "Nama", "Email", "Kursus", "Skor Akhir", "Diterbitkan"],
      certificates.map((c) => [c.certId, c.userName, c.userEmail, getCourseLabel(c.courseId), c.finalScore ?? c.scorePercent ?? "", formatDate(c.issuedAt)])
    );
  });

  certificatesList?.addEventListener("click", async (e) => {
    const revokeBtn = e.target.closest("[data-action='revoke-cert']");
    if (!revokeBtn || !revokeBtn.dataset.certId) return;
    if (currentAccessRole !== "super_admin") return;
    const certId = revokeBtn.dataset.certId;
    if (!confirm(`Cabut dan hapus sertifikat ${certId} secara permanen?`)) return;
    if (isFirebaseReady && db) {
      try {
        await db.collection("certificates").doc(certId).delete();
        showToast(`Sertifikat ${certId} dicabut.`);
      } catch (err) {
        showToast("Gagal mencabut sertifikat: " + err.message);
      }
    } else {
      certificates = certificates.filter((c) => c.certId !== certId);
      renderCertificates();
      renderAnalytics();
      showToast(`Sertifikat ${certId} dicabut (Mode Demo).`);
    }
  });

  document.getElementById("btnExportGradesCsv")?.addEventListener("click", () => {
    downloadCsv(
      "codeacademy-nilai-proyek.csv",
      ["Nama", "Email", "Kursus", "Status", "Skor Akhir", "Fasilitator"],
      submissions.map((s) => [s.userName, s.userEmail, getCourseLabel(s.courseId), s.status, s.finalScore ?? "", s.facilitatorName || ""])
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

  document.querySelectorAll(".ban-chip-btn[data-ban-reason]").forEach((chip) => {
    chip.addEventListener("click", () => {
      if (banReasonInput) banReasonInput.value = chip.dataset.banReason || "";
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
      if (nameEl) nameEl.textContent = target.name || "Siswa";
      if (emailEl) emailEl.textContent = target.email || `UID: ${target.id}`;
      if (banReasonInput) banReasonInput.value = target.banReason || "";
      if (banModal) banModal.hidden = false;
      return;
    }

    const unbanBtn = e.target.closest("[data-action='unban-user']");
    if (unbanBtn && unbanBtn.dataset.uid) {
      const uid = unbanBtn.dataset.uid;
      const target = studentUsersList.find((u) => u.id === uid);
      if (!confirm(`Cabut pembekuan akun untuk ${target?.name || uid}?`)) return;
      if (isFirebaseReady && db) {
        try {
          await db.collection("users").doc(uid).set({
            isBanned: false,
            banReason: "",
            bannedAt: null,
            bannedBy: "",
            unbannedAt: Date.now()
          }, { merge: true });
          showToast(`Akses akun ${target?.name || uid} diaktifkan kembali.`);
        } catch (err) {
          showToast("Gagal mencabut ban: " + err.message);
        }
      } else {
        studentUsersList = studentUsersList.map((u) =>
          u.id === uid ? { ...u, isBanned: false, banReason: "", bannedAt: null } : u
        );
        renderAnalytics();
        showToast(`Akun ${target?.name || uid} diaktifkan kembali (Mode Demo).`);
      }
    }
  });

  document.getElementById("btnConfirmBanUser")?.addEventListener("click", async () => {
    if (currentAccessRole !== "super_admin" || !banTargetUserId) return;
    const reason = (banReasonInput?.value || "").trim();
    if (reason.length < 5) {
      showToast("Tuliskan alasan pembekuan minimal 5 karakter.");
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
        showToast(`Akun ${target?.name || uid} berhasil dibekukan.`);
      } catch (err) {
        showToast("Gagal membekukan akun: " + err.message);
      }
    } else {
      studentUsersList = studentUsersList.map((u) =>
        u.id === uid ? { ...u, ...banPayload } : u
      );
      closeBanModal();
      renderAnalytics();
      showToast(`Akun ${target?.name || uid} dibekukan (Mode Demo).`);
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
    pageEyebrow.textContent = "Evaluasi Proyek Akhir";
    pageTitle.textContent = "Antrean Penilaian Peserta";
    pageSubtitle.textContent = "Tinjau kode workspace, cek pratinjau langsung, beri skor rubrik (40% Kuis + 60% Proyek), dan terbitkan sertifikat.";
  } else if (view === "forum") {
    pageEyebrow.textContent = "Moderasi & Bimbingan";
    pageTitle.textContent = "Forum Diskusi Kelas";
    pageSubtitle.textContent = "Jawab pertanyaan peserta secara real-time dengan lencana Fasilitator, pin jawaban terverifikasi, atau balas spesifik.";
  } else if (view === "announcements") {
    pageEyebrow.textContent = "Broadcast Informasi";
    pageTitle.textContent = "Pengumuman Kelas ke Aplikasi";
    pageSubtitle.textContent = "Terbitkan pengumuman penting yang langsung muncul di tab Notifikasi seluruh peserta Android.";
  } else if (view === "certificates") {
    pageEyebrow.textContent = "Panel Super Admin · Registri Kelulusan";
    pageTitle.textContent = "Sertifikat & Audit Kelulusan";
    pageSubtitle.textContent = "Pantau sertifikat berformat CC-... yang terverifikasi secara publik di Portal Verifikasi atau ekspor CSV.";
  } else if (view === "team") {
    pageEyebrow.textContent = "Panel Super Admin · Manajemen Akses";
    pageTitle.textContent = "Tim Fasilitator & Admin";
    pageSubtitle.textContent = "Kelola daftar email Google yang diizinkan mengakses Portal Staff dan atur perannya.";
  } else if (view === "analytics") {
    pageEyebrow.textContent = "Panel Super Admin · Analitik Global";
    pageTitle.textContent = "Ringkasan Platform & Direktori Siswa";
    pageSubtitle.textContent = "Pantau statistik kelulusan, keaktifan forum, progres siswa, dan unduh laporan CSV.";
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
      const studentName = topSub.studentName || topSub.userName || "Peserta";
      const courseName = topSub.courseTitle || getCourseLabel(topSub.courseId);
      if (badgeEl) badgeEl.textContent = `${pendingList.length} Antrean Menunggu Review`;
      titleEl.textContent = `${studentName} — ${courseName}`;
      metaEl.textContent = `Skor Kuis ${Number(topSub.finalQuizScore) || 0}/100 • Dikirim ${formatDate(topSub.submittedAt)} • Percobaan ke-${Number(topSub.attemptNumber ?? topSub.attemptCount) || 1}`;
      const avatarClasses = ["av-1", "av-2", "av-3", "av-4"];
      const sampleAvatars = pendingList.slice(0, 3).map((s, idx) => {
        const init = getStudentInitials(s.studentName || s.userName || "Peserta");
        return `<span class="av ${avatarClasses[idx % 4]}">${escapeHtml(init)}</span>`;
      });
      if (pendingList.length > 3) {
        sampleAvatars.push(`<span class="av av-4">+${pendingList.length - 3}</span>`);
      }
      avatarsEl.innerHTML = `${sampleAvatars.join("")}<span class="av-note">${pendingList.length} proyek siap dinilai</span>`;
      spotlightBtn.textContent = "Mulai Review →";
      spotlightBtn.onclick = () => openReviewModal(topSub.id);
    } else {
      if (badgeEl) badgeEl.textContent = "Antrean Bersih";
      titleEl.textContent = "Semua Proyek Akhir Sudah Ditinjau";
      metaEl.textContent = `${submissions.length} total pengajuan tersimpan. Pantau forum diskusi atau buat pengumuman baru untuk kelas.`;
      avatarsEl.innerHTML = `<span class="av av-1">✓</span><span class="av-note">0 proyek tertunda saat ini</span>`;
      spotlightBtn.textContent = "Buka Forum Kelas →";
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
            <span class="rail-item-title">${escapeHtml(ann.title || "Pengumuman Kelas")}</span>
            <span class="rail-item-sub">${escapeHtml((ann.message || ann.body || "").slice(0, 68))}</span>
          </span>
        </button>
      `);
    });

    roots.forEach((t) => {
      const init = getStudentInitials(t.authorName || "P");
      itemsHtml.push(`
        <button type="button" class="rail-item" data-rail-view="forum">
          <span class="rail-item-avatar">${escapeHtml(init)}</span>
          <span>
            <span class="rail-item-title">${escapeHtml(t.authorName || "Peserta")} · Forum</span>
            <span class="rail-item-sub">${escapeHtml((t.content || "").slice(0, 70))}</span>
          </span>
        </button>
      `);
    });

    highlightsEl.innerHTML = itemsHtml.length > 0
      ? itemsHtml.join("")
      : '<div class="rail-empty">Belum ada aktivitas forum atau pengumuman terbaru.</div>';

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
        <span class="action-row-left"><span class="action-dot pending"></span>Review proyek menunggu</span>
        <span class="action-pill ${pendingList.length > 0 ? "urgent" : ""}">${pendingList.length} proyek</span>
      </button>
      <button type="button" class="action-row-btn" data-action-jump="forum">
        <span class="action-row-left"><span class="action-dot forum"></span>Pertanyaan forum belum dijawab</span>
        <span class="action-pill ${unansweredCount > 0 ? "urgent" : ""}">${unansweredCount} topik</span>
      </button>
      <button type="button" class="action-row-btn" data-action-jump="revision">
        <span class="action-row-left"><span class="action-dot revision"></span>Peserta dalam status revisi</span>
        <span class="action-pill">${revisionList.length} revisi</span>
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
            <h4>Tidak ada pengajuan proyek ditemukan</h4>
            <p>Ubah filter pencarian atau tunggu peserta mengirimkan proyek dari aplikasi Android.</p>
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
      const studentName = s.studentName || s.userName || "Peserta";
      const studentEmail = s.studentEmail || s.userEmail || s.accountId || "-";
      const attempt = Number(s.attemptNumber ?? s.attemptCount) || 1;
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
  if (st === "PENDING_REVIEW") return "Menunggu Review";
  if (st === "REVISION_REQUESTED") return "Perlu Revisi";
  if (st === "APPROVED") return "Disetujui";
  return st || "-";
}

// ==========================================
function renderTeam() {
  const badge = document.getElementById("teamTotalBadge");
  if (badge) badge.textContent = String(facilitatorsList.length);
  const tbody = document.getElementById("teamTableBody");
  if (!tbody) return;
  if (facilitatorsList.length === 0) {
    tbody.innerHTML = '<tr><td colspan="4" class="empty-row">Belum ada data tim fasilitator.</td></tr>';
    return;
  }
  tbody.innerHTML = facilitatorsList
    .map((m) => {
      const isAdmin = m.accessRole === "super_admin";
      const isSelf = m.email === currentFacilitatorUser?.email;
      const actionsHtml = isAdmin
        ? `<span class="team-protected-pill">${isSelf ? "Akun Anda (Super Admin)" : "Super Admin (Dilindungi)"}</span>`
        : `<div class="team-actions-row">
             <button type="button" class="btn-promote-ghost" data-action="promote-team" data-email="${escapeHtml(m.email)}">⬆ Jadikan Super Admin</button>
             <button type="button" class="btn-danger-ghost" data-action="remove-team" data-email="${escapeHtml(m.email)}">Cabut Akses</button>
           </div>`;
      return `
        <tr>
          <td><strong>${escapeHtml(m.name || m.email)}</strong><div style="font-size:12px;color:var(--muted);">${escapeHtml(m.email)}</div></td>
          <td><span class="role-badge ${isAdmin ? "super-admin" : "facilitator"}">${isAdmin ? "👑 Super Admin" : "🎓 Fasilitator"}</span></td>
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
      <div class="stat-card"><div class="stat-info"><p>Rasio Lulus Proyek</p><h3>${passRate}%</h3></div></div>
      <div class="stat-card"><div class="stat-info"><p>Topik Forum Terverifikasi</p><h3>${verifiedThreads} / ${rootsCount}</h3></div></div>
      <div class="stat-card"><div class="stat-info"><p>Total Sertifikat Terbit</p><h3>${certificates.length}</h3></div></div>
      <div class="stat-card"><div class="stat-info"><p>Siswa Cloud (${bannedCount} Ban)</p><h3>${studentUsersList.length}</h3></div></div>
    `;
  }
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
    tbody.innerHTML = '<tr><td colspan="5" class="empty-row">Tidak ada data siswa yang sesuai filter.</td></tr>';
    return;
  }
  tbody.innerHTML = filteredStudents
    .slice(0, 60)
    .map((u) => {
      const isBanned = u.isBanned === true;
      const persona = u.headlineTitle ? `<span class="persona-tag-mini">${escapeHtml(u.headlineTitle)}</span><br>` : "";
      const emailLine = u.email ? escapeHtml(u.email) : `UID: ${escapeHtml(u.id || "-")}`;
      const statusHtml = isBanned
        ? `<span class="ban-status-pill banned">⛔ Dibekukan</span>${u.banReason ? `<div class="ban-reason-note">${escapeHtml(u.banReason)}</div>` : ""}`
        : `<span class="ban-status-pill active">● Aktif</span>`;
      const actionBtn = isBanned
        ? `<button type="button" class="btn-unban-user" data-action="unban-user" data-uid="${escapeHtml(u.id)}">Cabut Ban</button>`
        : `<button type="button" class="btn-danger-ghost" data-action="open-ban" data-uid="${escapeHtml(u.id)}">Bekukan</button>`;
      return `
      <tr>
        <td>
          <strong>${escapeHtml(u.name || "Siswa")}</strong>
          <div style="font-size:12px;color:var(--muted);">${emailLine}</div>
        </td>
        <td>${persona}<strong>${Number(u.totalXp) || 0} XP</strong></td>
        <td>Lv.${Number(u.level) || 1} • 🔥 ${Number(u.currentStreak) || 0}h</td>
        <td>${statusHtml}</td>
        <td style="text-align:right;">${actionBtn}</td>
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
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
        </div>
        <h4>${forumOnlyUnanswered ? "Semua Pertanyaan Sudah Terjawab" : "Belum Ada Diskusi"}</h4>
        <p>${forumOnlyUnanswered ? "Semua pertanyaan diskusi sudah mendapat jawaban Fasilitator." : "Belum ada topik diskusi yang sesuai dengan filter ini."}</p>
      </div>
    `;
    return;
  }

  forumThreadsList.innerHTML = filtered
    .map((thread) => {
      const replies = (repliesByRoot.get(thread.postId) || [])
        .slice()
        .sort((a, b) => ((b.isVerifiedAnswer ? 1 : 0) - (a.isVerifiedAnswer ? 1 : 0)) || (a.timestamp - b.timestamp));
      const initial = (thread.authorName || "P").trim().charAt(0).toUpperCase();
      const courseName = getCourseLabel(thread.courseId);
      const hasVerifiedAnswer = replies.some((r) => r.isVerifiedAnswer === true);

      const repliesHtml = replies
        .map((r) => {
          const rInitial = (r.authorName || "P").trim().charAt(0).toUpperCase();
          const rIsMentor = r.authorIsMentor === true || r.isMentor === true;
          const rIsVerified = r.isVerifiedAnswer === true;
          const rIsHelpful = r.isHelpful === true;
          const hasSpecificQuote = Boolean(r.replyToAuthorName && r.replyToPostId && r.replyToPostId !== thread.postId);
          const quoteHtml = hasSpecificQuote
            ? `<div class="forum-reply-quote">Membalas <strong>@${escapeHtml(r.replyToAuthorName)}</strong>${r.replyToContentSnippet ? `: “${escapeHtml(r.replyToContentSnippet)}”` : ""}</div>`
            : "";
          return `
        <div class="forum-reply-card ${rIsMentor ? "mentor" : ""} ${rIsVerified ? "verified" : ""}">
          <div class="forum-post-head" style="margin-bottom: 6px;">
            <div class="forum-author-meta">
              <div class="forum-avatar ${rIsMentor ? "mentor" : ""}" style="width:26px;height:26px;font-size:11px;">${escapeHtml(rInitial)}</div>
              <div>
                <strong style="font-size:13px;">${escapeHtml(r.authorName || "Peserta")}</strong>
                ${rIsMentor ? '<span class="forum-mentor-badge">Fasilitator</span>' : ""}
                ${rIsVerified ? '<span class="forum-verified-badge">✓ Jawaban Terverifikasi</span>' : ""}
                ${rIsHelpful ? '<span class="forum-helpful-badge">💡 Membantu</span>' : ""}
                <span style="font-size:11.5px; color:var(--muted); margin-left:6px;">${escapeHtml(formatDate(r.timestamp))}</span>
              </div>
            </div>
            <div class="forum-reply-actions">
              <button type="button" class="btn-action-chip ${rIsVerified ? "active-verified" : ""}" data-action="toggle-verified" data-post-id="${escapeHtml(r.postId)}" title="Pin sebagai Jawaban Terverifikasi Fasilitator">${rIsVerified ? "✓ Terpin" : "📌 Pin"}</button>
              <button type="button" class="btn-action-chip ${rIsHelpful ? "active-helpful" : ""}" data-action="toggle-helpful" data-post-id="${escapeHtml(r.postId)}" title="Tandai jawaban membantu">💡</button>
              <button type="button" class="btn-reply-ghost" data-action="target-reply" data-thread-id="${escapeHtml(thread.postId)}" data-post-id="${escapeHtml(r.postId)}">Balas</button>
              <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(r.postId)}">Hapus</button>
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
            <span>Membalas <strong>@${escapeHtml(activeTarget.authorName)}</strong>${activeTarget.contentSnippet ? `: “${escapeHtml(activeTarget.contentSnippet)}”` : ""}</span>
            <button type="button" class="forum-reply-target-clear" data-action="clear-reply-target" data-thread-id="${escapeHtml(thread.postId)}" aria-label="Batal balas spesifik">×</button>
          </div>`
        : "";
      const replyPlaceholder = activeTarget
        ? `Balas @${activeTarget.authorName}...`
        : "Balas sebagai Fasilitator (muncul langsung di aplikasi)...";
      return `
      <article class="forum-thread-card">
        <div class="forum-post-head">
          <div class="forum-author-meta">
            <div class="forum-avatar ${tIsMentor ? "mentor" : ""}">${escapeHtml(initial)}</div>
            <div>
              <strong>${escapeHtml(thread.authorName || "Peserta")}</strong>
              ${tIsMentor ? '<span class="forum-mentor-badge">Fasilitator</span>' : `<span style="font-size:11.5px;color:var(--muted);">• Lv.${Number(thread.authorLevel) || 1}</span>`}
              <span class="forum-course-pill">${escapeHtml(courseName)}</span>
              ${hasVerifiedAnswer ? '<span class="forum-resolved-pill">✓ Terjawab</span>' : ""}
              <div style="font-size:12px; color:var(--muted);">${escapeHtml(formatDate(thread.timestamp))} • ${Number(thread.likeCount) || 0} suka • ${replies.length} balasan</div>
            </div>
          </div>
          <button type="button" class="btn-danger-ghost" data-action="delete-post" data-post-id="${escapeHtml(thread.postId)}">Hapus Thread</button>
        </div>
        <div class="forum-post-body">${escapeHtml(thread.content || "")}</div>
        ${replies.length > 0 ? `<div class="forum-replies-wrap">${repliesHtml}</div>` : ""}
        ${targetBarHtml}
        <form class="forum-reply-form" data-action="reply-thread" data-thread-id="${escapeHtml(thread.postId)}">
          <input type="text" placeholder="${escapeHtml(replyPlaceholder)}" required>
          <button type="submit" class="btn-primary-solid">Balas</button>
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
    replyToAuthorName: resolvedTarget.authorName || "Peserta",
    replyToContentSnippet: (resolvedTarget.content || resolvedTarget.contentSnippet || "").slice(0, 80),
    rootThreadId: root.rootThreadId || root.postId
  };

  if (isFirebaseReady && db) {
    try {
      await db.collection("forum_posts").doc(postId).set(replyDoc);
      activeReplyTargetByThread.delete(rootPostId);
      input.value = "";
      showToast("Balasan fasilitator terkirim.");
    } catch (err) {
      console.error("Reply thread error:", err);
      showToast("Gagal mengirim balasan: " + err.message);
    }
  } else {
    activeReplyTargetByThread.delete(rootPostId);
    forumPosts.push(replyDoc);
    input.value = "";
    renderForum();
    showToast("Balasan ditambahkan (Mode Demo Offline).");
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
          ? (nextVal ? "Jawaban dipin & ditandai Terverifikasi Fasilitator." : "Pin jawaban terverifikasi dilepas.")
          : (nextVal ? "Ditandai sebagai Jawaban Membantu." : "Tanda Membantu dilepas.")
      );
    } catch (err) {
      console.error("Toggle forum flag error:", err);
      showToast("Gagal memperbarui status jawaban: " + err.message);
    }
  } else {
    target[field] = nextVal;
    renderForum();
    showToast(
      field === "isVerifiedAnswer"
        ? (nextVal ? "Jawaban dipin (Mode Demo Offline)." : "Pin dilepas (Mode Demo Offline).")
        : (nextVal ? "Ditandai Membantu (Mode Demo Offline)." : "Tanda Membantu dilepas (Mode Demo Offline).")
    );
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
    announcementsList.innerHTML = `
      <div class="forum-empty">
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
        </div>
        <h4>Belum Ada Pengumuman</h4>
        <p>Terbitkan pengumuman di atas agar langsung muncul di layar Notifikasi peserta Android.</p>
      </div>
    `;
    return;
  }

  announcementsList.innerHTML = announcements
    .map((ann) => {
      const courseLabel = ann.courseId && ann.courseId !== "all" ? getCourseLabel(ann.courseId) : "Semua Kelas / Umum";
      const annText = ann.message || ann.body || "";
      return `
      <article class="announcement-item">
        <div style="flex: 1; min-width: 240px;">
          <div style="display:flex; align-items:center; gap:8px; flex-wrap:wrap; margin-bottom:6px;">
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(ann.title || "Pengumuman")}</strong>
            <span class="forum-course-pill">${escapeHtml(courseLabel)}</span>
          </div>
          <p style="font-size:13.5px; color:var(--ink); line-height:1.5; white-space:pre-wrap;">${escapeHtml(annText)}</p>
          <div style="font-size:12px; color:var(--muted); margin-top:8px;">
            Oleh ${escapeHtml(ann.authorName || "Fasilitator")} • ${escapeHtml(formatDate(ann.createdAt))}
          </div>
        </div>
        <button type="button" class="btn-danger-ghost" data-action="delete-announcement" data-id="${escapeHtml(ann.id)}">Hapus</button>
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
    showToast("Isi judul dan pesan pengumuman dengan lengkap.");
    return;
  }

  const annId = "ann_" + Date.now() + "_" + Math.random().toString(36).slice(2, 6);
  const docData = {
    id: annId,
    title,
    message: body,
    body,
    courseId,
    courseTitle: courseId === "all" ? "Semua Kelas / Umum" : getCourseLabel(courseId),
    authorName: facilitatorNameInput.value.trim() || currentFacilitatorUser?.displayName || "Tim Fasilitator CodeAcademy",
    authorEmail: currentFacilitatorUser?.email || "",
    firebaseUid: currentFacilitatorUser?.uid || "",
    createdAt: Date.now()
  };

  if (isFirebaseReady && db) {
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
  if (isFirebaseReady && db) {
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
    certificatesList.innerHTML = `
      <div class="forum-empty">
        <div class="empty-state-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24"><circle cx="12" cy="8" r="6"/><path d="M15.477 12.89 17 22l-5-3-5 3 1.523-9.11"/></svg>
        </div>
        <h4>Belum Ada Sertifikat Terbit</h4>
        <p>Sertifikat resmi berawalan CC- akan otomatis muncul di sini saat proyek akhir peserta disetujui.</p>
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
            <strong style="font-size:15px; color:var(--ink);">${escapeHtml(c.userName || "Peserta")}</strong>
          </div>
          <div style="font-size:13.5px; color:var(--ink); font-weight:600;">${escapeHtml(courseName)}</div>
          <div style="font-size:12px; color:var(--muted); margin-top:4px;">
            Nilai Akhir: <strong>${Number(c.finalScore) || 0}/100</strong> (Kuis ${Number(c.quizScore) || 0} • Proyek ${Number(c.facilitatorScore) || 0}) • Terbit ${escapeHtml(formatDate(c.issuedAt))}
          </div>
        </div>
        <div class="forum-reply-actions">
          <a href="${verifyHref}" target="_blank" rel="noopener noreferrer" class="btn-review" style="text-decoration:none;">
            Buka Halaman Verifikasi ↗
          </a>
          ${currentAccessRole === "super_admin" ? `<button type="button" class="btn-danger-ghost" data-action="revoke-cert" data-cert-id="${escapeHtml(c.certId || "")}">Cabut</button>` : ""}
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

    const mailtoHref = `mailto:${ACCESS_REQUEST_RECIPIENT}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(
      buildFormattedAccessEmailText(payload)
    )}`;

    if (btnSubmit) {
      btnSubmit.disabled = true;
      btnSubmit.textContent = "Mengirim pengajuan...";
    }
    setFeedback("info", `Mengirim pengajuan akses (${escapeHtml(requestId)}) ke pengelola CodeAcademy...`);

    try {
      const response = await fetch(`https://formsubmit.co/ajax/${ACCESS_REQUEST_RECIPIENT}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        },
        body: JSON.stringify({
          _subject: subject,
          _replyto: applicantEmail,
          _template: "table",
          _captcha: "false",
          "ID Referensi": requestId,
          "Peran Diajukan": selectedRole,
          "Nama Pemohon": fullName,
          "Email Didaftarkan": applicantEmail,
          "Instansi / Jabatan": affiliation,
          "Waktu Pengajuan": submittedAt,
          "Catatan Pemohon": reason,
          "URL Portal": portalUrl
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      form.reset();
      setFeedback(
        "success",
        `<strong>Pengajuan terkirim (${escapeHtml(requestId)}).</strong><br>` +
          `Permintaan akses <strong>${escapeHtml(selectedRole)}</strong> untuk <strong>${escapeHtml(applicantEmail)}</strong> telah dikirim ke <strong>${ACCESS_REQUEST_RECIPIENT}</strong>. Anda akan dihubungi setelah akun diaktifkan.`
      );
      showToast(`Pengajuan akses ${requestId} berhasil dikirim.`);
    } catch (err) {
      console.warn("[AccessRequest] Direct form submit failed, offering mailto fallback:", err);
      setFeedback(
        "error",
        `Pengiriman otomatis sedang tidak tersedia (${escapeHtml(err.message || "offline")}). ` +
          `<a href="${mailtoHref}" style="color:#0F766E;font-weight:700;text-decoration:underline;">Klik di sini untuk mengirim via aplikasi email Anda</a>.`
      );
    } finally {
      if (btnSubmit) {
        btnSubmit.disabled = false;
        btnSubmit.textContent = "Kirim Pengajuan Akses";
      }
    }
  });
}
