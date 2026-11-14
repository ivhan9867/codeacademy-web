// CodeAcademy Facilitator Dashboard Logic
// Supports Live Firebase Firestore Sync (collection: project_submissions) & Local Demo Mode

const DEFAULT_PIN = "admin123";
const STORAGE_KEY = "codeacademy_facilitator_submissions_v2";
const FORUM_STORAGE_KEY = "codeacademy_facilitator_forum_v1";
const AUTH_KEY = "codeacademy_facilitator_logged_in";

const SEED_FORUM_POSTS = [
  {
    postId: "demo_p1",
    courseId: "web-html-01",
    authorId: "101",
    authorName: "Ahmad Fauzi",
    content: "Permisi kak, kapan sebaiknya pakai tag <article> dibanding <section> di HTML5?",
    timestamp: Date.now() - 1000 * 60 * 90,
    likeCount: 3,
    authorLevel: 4,
    isMentor: false,
    replyToPostId: null,
    replyToAuthorName: null,
    replyToContentSnippet: null
  },
  {
    postId: "demo_p1_r1",
    courseId: "web-html-01",
    authorId: "facilitator-mentor",
    authorName: "Fasilitator CodeAcademy",
    content: "Gunakan <article> jika kontennya bisa berdiri sendiri (seperti postingan blog atau kartu produk), dan <section> untuk mengelompokkan bagian tematik dalam halaman.",
    timestamp: Date.now() - 1000 * 60 * 60,
    likeCount: 5,
    authorLevel: 99,
    isMentor: true,
    replyToPostId: "demo_p1",
    replyToAuthorName: "Ahmad Fauzi",
    replyToContentSnippet: "Permisi kak, kapan sebaiknya pakai tag <article> dibanding <section>..."
  }
];

// Initial Demo Submissions (Offline / Local Mock Fallback for WebDev Curriculum)
const SEED_SUBMISSIONS = [
  {
    docId: "101_fe-web-foundation-00",
    submissionId: "sub_01",
    accountId: 101,
    studentName: "Ahmad Fauzi",
    studentEmail: "ahmad.fauzi@kampus.ac.id",
    courseId: "fe-web-foundation-00",
    courseTitle: "Fondasi Web: Dari Nol ke Baris Kode Pertama",
    status: "PENDING_REVIEW",
    finalQuizScore: 90,
    facilitatorScore: null,
    facilitatorFeedback: "",
    facilitatorName: "",
    attemptNumber: 1,
    submittedAt: Date.now() - 1000 * 60 * 35,
    projectUrl: "https://github.com/ahmadfauzi/web-portfolio-landing",
    studentNotes: "Halo Fasilitator, saya telah menyelesaikan proyek pembuatan web portofolio responsif dengan HTML semantik dan styling CSS modern. Mohon masukannya.",
    checkedRubricIdsJson: JSON.stringify(["r1", "r2", "r3"]),
    workspaceCode: `<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Portofolio Ahmad Fauzi</title>
  <style>
    body { font-family: 'Segoe UI', sans-serif; margin: 0; background: #0f172a; color: #f8fafc; }
    header { background: #1e293b; padding: 20px; text-align: center; border-bottom: 2px solid #14b8a6; }
    .hero { padding: 40px 20px; text-align: center; }
    .btn { background: #14b8a6; color: white; padding: 10px 20px; border-radius: 8px; text-decoration: none; font-weight: bold; }
    .card-grid { display: flex; flex-wrap: wrap; gap: 16px; justify-content: center; padding: 20px; }
    .card { background: #1e293b; padding: 20px; border-radius: 12px; width: 260px; border: 1px solid #334155; }
  </style>
</head>
<body>
  <header><h1>Ahmad Fauzi - Web Developer</h1></header>
  <div class="hero">
    <h2>Membangun Antarmuka Modern & Responsif</h2>
    <p>Mahasiswa Informatika peminatan Front-End Engineering.</p>
    <a href="#kontak" class="btn">Hubungi Saya</a>
  </div>
  <div class="card-grid">
    <div class="card"><h3>Proyek 1</h3><p>Landing page profil UMKM lokal.</p></div>
    <div class="card"><h3>Proyek 2</h3><p>Kalkulator interaktif JavaScript.</p></div>
  </div>
</body>
</html>`,
    rubrics: [
      { id: "r1", title: "Struktur HTML Semantik", desc: "Menggunakan tag semantik (header, nav, main, footer)", weight: 30 },
      { id: "r2", title: "Styling CSS & Tata Letak", desc: "Desain visual rapi, kontras warna baik", weight: 35 },
      { id: "r3", title: "Responsivitas Layar", desc: "Tampilan fleksibel pada mobile dan desktop", weight: 35 }
    ]
  },
  {
    docId: "102_fe-css-02",
    submissionId: "sub_02",
    accountId: 102,
    studentName: "Budi Santoso",
    studentEmail: "budi.santoso@kampus.ac.id",
    courseId: "fe-css-02",
    courseTitle: "CSS Layout: Flexbox & Grid",
    status: "REVISION_REQUESTED",
    finalQuizScore: 85,
    facilitatorScore: 65,
    facilitatorFeedback: "Penggunaan CSS Grid sudah tepat, namun pada layar mobile (< 600px) kartu masih melebar keluar layar. Mohon tambahkan media query atau minmax(250px, 1fr).",
    facilitatorName: "Ir. Rizky Pratama, M.Kom",
    attemptNumber: 1,
    submittedAt: Date.now() - 1000 * 60 * 60 * 12,
    projectUrl: "https://github.com/budisantoso/css-grid-gallery",
    studentNotes: "Galeri foto responsif menggunakan CSS Grid dan efek hover transisi halus.",
    checkedRubricIdsJson: JSON.stringify(["r1"]),
    workspaceCode: `<!DOCTYPE html>
<html lang="id">
<head>
  <style>
    body { font-family: sans-serif; padding: 20px; background: #f8fafc; }
    .gallery { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; }
    .box { background: #1A6368; color: white; padding: 28px; border-radius: 10px; text-align: center; font-weight: bold; }
  </style>
</head>
<body>
  <h2>Galeri Flexbox & Grid</h2>
  <div class="gallery">
    <div class="box">Item 1</div>
    <div class="box">Item 2</div>
    <div class="box">Item 3</div>
  </div>
</body>
</html>`,
    rubrics: [
      { id: "r1", title: "Implementasi Flexbox & Grid", desc: "Tata letak 2 dimensi rapi dan sejajar", weight: 40 },
      { id: "r2", title: "Media Query Responsif", desc: "Adaptif di mobile dan desktop", weight: 30 },
      { id: "r3", title: "Estetika & Transisi", desc: "Kontras warna dan efek hover konsisten", weight: 30 }
    ]
  },
  {
    docId: "103_fe-js-03",
    submissionId: "sub_03",
    accountId: 103,
    studentName: "Siti Rahmawati",
    studentEmail: "siti.rahma@kampus.ac.id",
    courseId: "fe-js-03",
    courseTitle: "JavaScript Fundamentals & DOM Interaktif",
    status: "APPROVED",
    finalQuizScore: 95,
    facilitatorScore: 92,
    facilitatorFeedback: "Luar biasa! Manipulasi DOM dengan addEventListener sangat bersih dan input kosong sudah divalidasi dengan baik.",
    facilitatorName: "Dr. Hendra Wijaya, S.T., M.T.",
    attemptNumber: 1,
    submittedAt: Date.now() - 1000 * 60 * 60 * 24,
    projectUrl: "https://github.com/sitirahma/js-todo-interaktif",
    studentNotes: "Aplikasi Todo List interaktif menggunakan vanilla JavaScript dan DOM manipulation.",
    checkedRubricIdsJson: JSON.stringify(["r1", "r2", "r3"]),
    workspaceCode: `<!DOCTYPE html>
<html lang="id">
<head>
  <style>
    body { font-family: sans-serif; background: #0f172a; color: white; padding: 24px; }
    input { padding: 8px 12px; border-radius: 6px; border: none; }
    button { padding: 8px 14px; border-radius: 6px; border: none; background: #14b8a6; color: white; font-weight: bold; cursor: pointer; }
    li { margin-top: 8px; }
  </style>
</head>
<body>
  <h3>Daftar Tugas Harian</h3>
  <input id="taskInput" placeholder="Tulis tugas baru...">
  <button id="addBtn">Tambah</button>
  <ul id="taskList"></ul>
  <script>
    document.getElementById('addBtn').addEventListener('click', () => {
      const input = document.getElementById('taskInput');
      if (!input.value.trim()) return;
      const li = document.createElement('li');
      li.textContent = input.value.trim();
      document.getElementById('taskList').appendChild(li);
      input.value = '';
    });
  </script>
</body>
</html>`,
    rubrics: [
      { id: "r1", title: "Manipulasi DOM Modern", desc: "Menggunakan querySelector dan addEventListener", weight: 35 },
      { id: "r2", title: "Logika & Validasi Input", desc: "Menangani input kosong atau tidak valid", weight: 35 },
      { id: "r3", title: "Kebersihan Kode JS", desc: "Variabel let/const rapi dan bebas error console", weight: 30 }
    ]
  }
];

// App State
let submissions = [];
let activeSubmission = null;
let currentFilter = "all";
let currentCourseFilter = "all";
let searchQuery = "";
let firestoreDb = null;
let isFirebaseConnected = false;

// Forum State
let forumPosts = [];
let activeView = "submissions";
let forumCourseFilter = "all";
let forumSearchQuery = "";

// Initialize
document.addEventListener("DOMContentLoaded", () => {
  checkAuth();
  setupEventListeners();
  initDataSource();
});

function checkAuth() {
  const isAuth = sessionStorage.getItem(AUTH_KEY) === "true";
  const authOverlay = document.getElementById("authOverlay");
  if (isAuth) {
    authOverlay.style.display = "none";
  } else {
    authOverlay.style.display = "flex";
  }
}

function handleLogin(e) {
  e.preventDefault();
  const pinInput = document.getElementById("pinInput").value;
  if (pinInput === DEFAULT_PIN || pinInput === "fasilitator2026") {
    sessionStorage.setItem(AUTH_KEY, "true");
    document.getElementById("authOverlay").style.display = "none";
    showToast("Selamat datang, Fasilitator CodeAcademy!");
  } else {
    document.getElementById("authError").hidden = false;
    document.getElementById("pinInput").select();
  }
}

function handleLogout() {
  if (confirm("Apakah Anda yakin ingin keluar dari Portal Fasilitator?")) {
    sessionStorage.removeItem(AUTH_KEY);
    window.location.reload();
  }
}

function initDataSource() {
  const modeBadge = document.getElementById("modeBadge");

  if (typeof firebase !== "undefined" && typeof window.isFirebaseConfigured === "function" && window.isFirebaseConfigured()) {
    try {
      if (!firebase.apps.length) {
        firebase.initializeApp(window.CODEACADEMY_FIREBASE_CONFIG);
      }
      firestoreDb = firebase.firestore();
      isFirebaseConnected = true;

      if (modeBadge) {
        modeBadge.textContent = "● Live Firestore Sync";
        modeBadge.classList.add("live");
      }

      // Real-time listener matching Android Firestore collection 'project_submissions'
      firestoreDb.collection("project_submissions")
        .orderBy("submittedAt", "desc")
        .onSnapshot(
          (snapshot) => {
            const remoteItems = [];
            snapshot.forEach((doc) => {
              const d = doc.data();
              // Filter out purely empty DRAFTs unless reviewed
              if (d.status && d.status !== "DRAFT") {
                remoteItems.push({
                  docId: doc.id,
                  submissionId: d.submissionId || doc.id,
                  accountId: d.accountId || 0,
                  studentName: d.studentName || "Siswa",
                  studentEmail: d.studentEmail || "",
                  courseId: d.courseId || "",
                  courseTitle: d.courseTitle || d.courseId || "Web Development",
                  status: d.status || "PENDING_REVIEW",
                  finalQuizScore: d.finalQuizScore || 0,
                  facilitatorScore: d.facilitatorScore ?? null,
                  facilitatorFeedback: d.facilitatorFeedback || "",
                  facilitatorName: d.facilitatorName || "",
                  attemptNumber: d.attemptNumber || 1,
                  submittedAt: d.submittedAt || Date.now(),
                  reviewedAt: d.reviewedAt || null,
                  projectUrl: d.projectUrl || "",
                  studentNotes: d.studentNotes || "",
                  checkedRubricIdsJson: d.checkedRubricIdsJson || "[]",
                  workspaceCode: d.workspaceCode || ""
                });
              }
            });
            submissions = remoteItems;
            renderDashboard();
          },
          (error) => {
            console.warn("Firestore snapshot error, fallback ke local:", error);
            if (modeBadge) {
              modeBadge.textContent = "● Offline Fallback";
              modeBadge.classList.remove("live");
            }
            loadLocalFallback();
          }
        );
      // Real-time listener matching Android Firestore collection 'forum_posts'
      attachForumRealtimeListener();
      return;
    } catch (err) {
      console.warn("Gagal inisialisasi Firebase, menggunakan Demo Mode:", err);
    }
  }

  // Local Demo Fallback when Firebase Web Config is not yet configured
  if (modeBadge) {
    modeBadge.textContent = "● Demo Mode (Lokal)";
    modeBadge.classList.remove("live");
  }
  loadLocalFallback();
  loadLocalForumFallback();
}

function loadLocalFallback() {
  const localData = localStorage.getItem(STORAGE_KEY);
  if (localData) {
    try {
      submissions = JSON.parse(localData);
    } catch {
      submissions = [...SEED_SUBMISSIONS];
      saveLocalSubmissions();
    }
  } else {
    submissions = [...SEED_SUBMISSIONS];
    saveLocalSubmissions();
  }
  renderDashboard();
}

function saveLocalSubmissions() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(submissions));
}

function setupEventListeners() {
  document.getElementById("authForm").addEventListener("submit", handleLogin);
  document.getElementById("btnLogout").addEventListener("click", handleLogout);

  // Search input
  document.getElementById("searchInput").addEventListener("input", (e) => {
    searchQuery = e.target.value.toLowerCase();
    renderTable();
  });

  // View Switcher (Proyek Akhir vs Forum Diskusi)
  document.querySelectorAll(".view-tab").forEach((tab) => {
    tab.addEventListener("click", () => {
      switchView(tab.dataset.view);
    });
  });

  const forumSearchInput = document.getElementById("forumSearchInput");
  if (forumSearchInput) {
    forumSearchInput.addEventListener("input", (e) => {
      forumSearchQuery = e.target.value.toLowerCase();
      renderForumThreads();
    });
  }

  const forumCourseFilterEl = document.getElementById("forumCourseFilter");
  if (forumCourseFilterEl) {
    forumCourseFilterEl.addEventListener("change", (e) => {
      forumCourseFilter = e.target.value;
      renderForumThreads();
    });
  }

  const btnCreateForumPost = document.getElementById("btnCreateForumPost");
  if (btnCreateForumPost) {
    btnCreateForumPost.addEventListener("click", createFacilitatorPost);
  }

  // Course filter
  document.getElementById("courseFilter").addEventListener("change", (e) => {
    currentCourseFilter = e.target.value;
    renderTable();
  });

  // Status pills
  document.querySelectorAll(".btn-filter-pill").forEach((btn) => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".btn-filter-pill").forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");
      currentFilter = btn.dataset.status;
      renderTable();
    });
  });

  // Code vs Live Preview tabs
  const btnTabCode = document.getElementById("btnTabCode");
  const btnTabPreview = document.getElementById("btnTabPreview");
  const codePane = document.getElementById("codePane");
  const previewPane = document.getElementById("previewPane");

  if (btnTabCode && btnTabPreview) {
    btnTabCode.addEventListener("click", () => {
      btnTabCode.classList.add("active");
      btnTabPreview.classList.remove("active");
      codePane.style.display = "block";
      previewPane.style.display = "none";
    });
    btnTabPreview.addEventListener("click", () => {
      btnTabPreview.classList.add("active");
      btnTabCode.classList.remove("active");
      codePane.style.display = "none";
      previewPane.style.display = "block";
      if (activeSubmission) {
        const iframe = document.getElementById("modalLivePreview");
        iframe.srcdoc = activeSubmission.workspaceCode || "<p style='font-family:sans-serif;padding:16px;'>Tidak ada kode HTML untuk dipratinjau.</p>";
      }
    });
  }

  // Score slider
  const scoreSlider = document.getElementById("scoreSlider");
  const scoreNumber = document.getElementById("scoreNumber");
  scoreSlider.addEventListener("input", (e) => {
    scoreNumber.value = e.target.value;
    updateCalculatedScore();
  });
  scoreNumber.addEventListener("input", (e) => {
    let val = parseInt(e.target.value) || 0;
    val = Math.max(0, Math.min(100, val));
    scoreSlider.value = val;
    updateCalculatedScore();
  });

  // Quick feedback tags
  document.querySelectorAll(".btn-feedback-tag").forEach((tag) => {
    tag.addEventListener("click", () => {
      const fbInput = document.getElementById("facilitatorFeedback");
      const current = fbInput.value.trim();
      fbInput.value = current ? `${current}\n${tag.dataset.text}` : tag.dataset.text;
    });
  });

  // Decision buttons
  document.getElementById("btnRequestRevision").addEventListener("click", () => executeReview("REVISION_REQUESTED"));
  document.getElementById("btnApprove").addEventListener("click", () => executeReview("APPROVED"));
  document.getElementById("btnCloseModal").addEventListener("click", closeModal);
  document.getElementById("btnCancelModal").addEventListener("click", closeModal);
}

function renderDashboard() {
  updateStats();
  renderTable();
}

function updateStats() {
  const total = submissions.length;
  const pending = submissions.filter((s) => s.status === "PENDING_REVIEW").length;
  const revision = submissions.filter((s) => s.status === "REVISION_REQUESTED").length;
  const approved = submissions.filter((s) => s.status === "APPROVED").length;

  document.getElementById("statTotal").textContent = total;
  document.getElementById("statPending").textContent = pending;
  document.getElementById("statRevision").textContent = revision;
  document.getElementById("statApproved").textContent = approved;
}

function renderTable() {
  const tbody = document.getElementById("submissionsTableBody");
  tbody.innerHTML = "";

  const filtered = submissions.filter((s) => {
    const matchStatus = currentFilter === "all" || s.status === currentFilter;
    const matchCourse = currentCourseFilter === "all" || s.courseId === currentCourseFilter;
    const matchSearch =
      !searchQuery ||
      s.studentName.toLowerCase().includes(searchQuery) ||
      s.studentEmail.toLowerCase().includes(searchQuery) ||
      s.courseTitle.toLowerCase().includes(searchQuery);
    return matchStatus && matchCourse && matchSearch;
  });

  if (filtered.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="7" style="text-align: center; padding: 40px; color: var(--text-muted);">
          Belum ada berkas submission yang sesuai dengan kriteria filter.
        </td>
      </tr>
    `;
    return;
  }

  filtered.forEach((sub) => {
    const tr = document.createElement("tr");

    let statusClass = "pending";
    let statusText = "Menunggu Review";
    if (sub.status === "APPROVED") {
      statusClass = "approved";
      statusText = "Disetujui";
    } else if (sub.status === "REVISION_REQUESTED") {
      statusClass = "revision";
      statusText = "Perlu Revisi";
    } else if (sub.status === "DRAFT") {
      statusClass = "draft";
      statusText = "Draf Siswa";
    }

    const timeAgo = getTimeAgo(sub.submittedAt);
    const initials = (sub.studentName || "S")
      .split(" ")
      .map((n) => n[0])
      .slice(0, 2)
      .join("")
      .toUpperCase();

    tr.innerHTML = `
      <td>
        <div class="student-col">
          <div class="student-avatar">${initials}</div>
          <div>
            <div class="student-name">${escapeHtml(sub.studentName)}</div>
            <div class="student-email">${escapeHtml(sub.studentEmail)}</div>
          </div>
        </div>
      </td>
      <td>
        <span class="course-badge">${escapeHtml(sub.courseTitle)}</span>
      </td>
      <td><span style="font-family: var(--mono); font-weight: 700; color: var(--teal-deep);">${sub.finalQuizScore}%</span></td>
      <td><span class="status-badge ${statusClass}">${statusText}</span></td>
      <td style="color: var(--muted); font-size: 12.5px;">${timeAgo}</td>
      <td><span style="font-family: var(--mono); font-size: 12.5px; font-weight: 600; color: var(--ink);">Ke-${sub.attemptNumber || 1}</span></td>
      <td style="text-align: right;">
        <button class="btn-action-review" onclick="openReviewModal('${escapeHtml(sub.docId || sub.submissionId)}')">
          <svg class="i" viewBox="0 0 24 24" aria-hidden="true"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
          Tinjau
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function openReviewModal(idKey) {
  activeSubmission = submissions.find((s) => s.docId === idKey || s.submissionId === idKey);
  if (!activeSubmission) return;

  document.getElementById("modalStudentName").textContent = activeSubmission.studentName;
  document.getElementById("modalStudentEmail").textContent = activeSubmission.studentEmail;
  document.getElementById("modalCourseTitle").textContent = activeSubmission.courseTitle;
  document.getElementById("modalQuizScore").textContent = `${activeSubmission.finalQuizScore}%`;
  document.getElementById("modalStudentNotes").textContent =
    activeSubmission.studentNotes || "Tidak ada catatan khusus dari siswa.";

  // Project URL
  const btnProjUrl = document.getElementById("btnProjectUrl");
  if (activeSubmission.projectUrl) {
    btnProjUrl.href = activeSubmission.projectUrl;
    btnProjUrl.style.display = "inline-flex";
    btnProjUrl.textContent = "🔗 Buka Repositori / Demo Eksternal";
  } else {
    btnProjUrl.style.display = "none";
  }

  // Workspace Code & reset preview tab to Code
  const codeEl = document.getElementById("modalWorkspaceCode");
  codeEl.textContent = activeSubmission.workspaceCode || "// Siswa belum melampirkan kode teks.";
  const btnTabCode = document.getElementById("btnTabCode");
  const btnTabPreview = document.getElementById("btnTabPreview");
  if (btnTabCode && btnTabPreview) {
    btnTabCode.classList.add("active");
    btnTabPreview.classList.remove("active");
    document.getElementById("codePane").style.display = "block";
    document.getElementById("previewPane").style.display = "none";
  }

  // Rubrics
  renderRubrics(activeSubmission);

  // Score & Feedback
  const initialScore = activeSubmission.facilitatorScore ?? 85;
  document.getElementById("scoreSlider").value = initialScore;
  document.getElementById("scoreNumber").value = initialScore;
  document.getElementById("facilitatorFeedback").value = activeSubmission.facilitatorFeedback || "";
  document.getElementById("facilitatorName").value =
    activeSubmission.facilitatorName || "Tim Fasilitator CodeAcademy";

  updateCalculatedScore();

  document.getElementById("reviewModal").classList.add("open");
}

function renderRubrics(sub) {
  const container = document.getElementById("rubricsContainer");
  container.innerHTML = "";

  const rubrics = sub.rubrics || [
    { id: "r1", title: "Kesesuaian Spesifikasi WebDev", desc: "Memenuhi seluruh kebutuhan fitur HTML/CSS/JS", weight: 35 },
    { id: "r2", title: "Kualitas & Kerapian Kode", desc: "Struktur semantik bersih dan terorganisir", weight: 35 },
    { id: "r3", title: "Responsivitas & Interaktivitas", desc: "Tampilan adaptif dan bebas error console", weight: 30 }
  ];

  let checkedSet = new Set();
  try {
    if (sub.checkedRubricIdsJson) {
      checkedSet = new Set(JSON.parse(sub.checkedRubricIdsJson));
    }
  } catch {}

  rubrics.forEach((r) => {
    const isChecked = checkedSet.has(r.id);
    const item = document.createElement("div");
    item.className = "rubric-item";
    item.innerHTML = `
      <input type="checkbox" id="rubric_${r.id}" ${isChecked ? "checked" : ""}>
      <div class="rubric-info">
        <h5>${escapeHtml(r.title)} <span style="color: var(--teal-primary); font-size: 11px;">(${r.weight}%)</span></h5>
        <p>${escapeHtml(r.desc)}</p>
      </div>
    `;
    container.appendChild(item);
  });
}

function updateCalculatedScore() {
  if (!activeSubmission) return;
  const quizScore = activeSubmission.finalQuizScore || 0;
  const facScore = parseInt(document.getElementById("scoreNumber").value) || 0;

  // Formula matching Android CourseUnlockUtils.computeFinalCertificateScore: round(0.40 * quiz + 0.60 * fac)
  const finalScore = Math.round(0.4 * quizScore + 0.6 * facScore);
  const finalEl = document.getElementById("calculatedFinalScore");
  finalEl.textContent = `${finalScore}%`;

  if (finalScore >= 85) {
    finalEl.style.color = "var(--success)";
  } else if (finalScore >= 70) {
    finalEl.style.color = "var(--teal-primary)";
  } else {
    finalEl.style.color = "var(--danger)";
  }
}

async function executeReview(newStatus) {
  if (!activeSubmission) return;

  const facScore = parseInt(document.getElementById("scoreNumber").value) || 0;
  const feedback = document.getElementById("facilitatorFeedback").value.trim();
  const facName = document.getElementById("facilitatorName").value.trim() || "Fasilitator";

  if (newStatus === "REVISION_REQUESTED" && !feedback) {
    alert("Harap berikan catatan revisi agar siswa mengetahui apa yang perlu diperbaiki.");
    return;
  }

  const reviewedAt = Date.now();
  const targetStudentName = activeSubmission.studentName;

  if (isFirebaseConnected && firestoreDb && activeSubmission.docId) {
    try {
      await firestoreDb.collection("project_submissions").doc(activeSubmission.docId).set(
        {
          status: newStatus,
          facilitatorScore: facScore,
          facilitatorFeedback: feedback,
          facilitatorName: facName,
          reviewedAt: reviewedAt,
          updatedAt: Date.now()
        },
        { merge: true }
      );
      closeModal();
      if (newStatus === "APPROVED") {
        showToast(`Proyek ${targetStudentName} disetujui dan tersinkron real-time ke Android.`);
      } else {
        showToast(`Permintaan revisi dikirim real-time ke ${targetStudentName}.`);
      }
      return;
    } catch (err) {
      console.error("Gagal menulis ke Firestore:", err);
      alert("Gagal menyimpan ke Firestore: " + err.message);
      return;
    }
  }

  // Fallback Local Demo Mode
  activeSubmission.status = newStatus;
  activeSubmission.facilitatorScore = facScore;
  activeSubmission.facilitatorFeedback = feedback;
  activeSubmission.facilitatorName = facName;
  activeSubmission.reviewedAt = reviewedAt;

  saveLocalSubmissions();
  renderDashboard();
  closeModal();

  if (newStatus === "APPROVED") {
    showToast(`[Demo Lokal] Proyek ${targetStudentName} disetujui.`);
  } else {
    showToast(`[Demo Lokal] Permintaan revisi dikirim ke ${targetStudentName}.`);
  }
}

function closeModal() {
  document.getElementById("reviewModal").classList.remove("open");
  const iframe = document.getElementById("modalLivePreview");
  if (iframe) iframe.srcdoc = "";
  activeSubmission = null;
}

function copyWorkspaceCode() {
  const code = document.getElementById("modalWorkspaceCode").textContent;
  navigator.clipboard.writeText(code).then(() => {
    showToast("Kode tersalin ke clipboard!");
  });
}

function showToast(msg) {
  const t = document.getElementById("toastMsg");
  t.textContent = msg;
  t.style.display = "block";
  setTimeout(() => {
    t.style.display = "none";
  }, 3500);
}

function getTimeAgo(timestamp) {
  if (!timestamp) return "Baru saja";
  const diff = Date.now() - timestamp;
  const mins = Math.floor(diff / (1000 * 60));
  if (mins < 1) return "Baru saja";
  if (mins < 60) return `${mins} mnt lalu`;
  const hours = Math.floor(mins / 60);
  if (hours < 24) return `${hours} jam lalu`;
  const days = Math.floor(hours / 24);
  return `${days} hari lalu`;
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
}


// ==========================================
// FORUM DISKUSI REAL-TIME & MODERASI
// ==========================================
function switchView(view) {
  activeView = view;
  const submissionsView = document.getElementById("submissionsView");
  const forumView = document.getElementById("forumView");
  const pageEyebrow = document.getElementById("pageEyebrow");
  const pageTitle = document.getElementById("pageTitle");
  const pageSubtitle = document.getElementById("pageSubtitle");

  document.querySelectorAll(".view-tab").forEach((t) => {
    const isActive = t.dataset.view === view;
    t.classList.toggle("active", isActive);
    t.setAttribute("aria-selected", isActive ? "true" : "false");
  });

  if (view === "forum") {
    submissionsView.style.display = "none";
    forumView.style.display = "block";
    if (pageEyebrow) pageEyebrow.textContent = "Moderasi & Tanya Jawab Kelas";
    if (pageTitle) pageTitle.textContent = "Forum Diskusi Peserta";
    if (pageSubtitle) pageSubtitle.textContent = "Balas pertanyaan peserta secara real-time sebagai Fasilitator atau hapus pesan yang melanggar aturan.";
    renderForumThreads();
  } else {
    submissionsView.style.display = "block";
    forumView.style.display = "none";
    if (pageEyebrow) pageEyebrow.textContent = "Evaluasi Proyek Akhir";
    if (pageTitle) pageTitle.textContent = "Antrean Penilaian Peserta";
    if (pageSubtitle) pageSubtitle.textContent = "Tinjau kode workspace, cek pratinjau langsung, beri skor rubrik, dan terbitkan sertifikat.";
  }
}

function attachForumRealtimeListener() {
  if (!firestoreDb) return;
  firestoreDb.collection("forum_posts")
    .orderBy("timestamp", "desc")
    .limit(120)
    .onSnapshot(
      (snapshot) => {
        const items = [];
        snapshot.forEach((doc) => {
          const d = doc.data();
          items.push({
            postId: d.postId || doc.id,
            courseId: d.courseId || "",
            authorId: d.authorId || "",
            authorName: d.authorName || "Siswa",
            content: d.content || "",
            timestamp: d.timestamp || Date.now(),
            likeCount: d.likeCount || 0,
            authorLevel: d.authorLevel || 1,
            isMentor: Boolean(d.isMentor),
            replyToPostId: d.replyToPostId || null,
            replyToAuthorName: d.replyToAuthorName || null,
            replyToContentSnippet: d.replyToContentSnippet || null
          });
        });
        forumPosts = items;
        updateForumCountBadge();
        if (activeView === "forum") renderForumThreads();
      },
      (err) => {
        console.warn("Firestore forum listener error:", err);
        loadLocalForumFallback();
      }
    );
}

function loadLocalForumFallback() {
  const raw = localStorage.getItem(FORUM_STORAGE_KEY);
  if (raw) {
    try {
      forumPosts = JSON.parse(raw);
    } catch {
      forumPosts = [...SEED_FORUM_POSTS];
      saveLocalForumPosts();
    }
  } else {
    forumPosts = [...SEED_FORUM_POSTS];
    saveLocalForumPosts();
  }
  updateForumCountBadge();
  if (activeView === "forum") renderForumThreads();
}

function saveLocalForumPosts() {
  localStorage.setItem(FORUM_STORAGE_KEY, JSON.stringify(forumPosts));
}

function updateForumCountBadge() {
  const badge = document.getElementById("forumTotalBadge");
  if (badge) badge.textContent = String(forumPosts.length);
}

function courseLabel(courseId) {
  const map = {
    "fe-web-foundation-00": "Fondasi Web",
    "web-html-01": "HTML5 Semantic",
    "web-css-01": "CSS3 Modern",
    "web-js-01": "JavaScript ES6"
  };
  return map[courseId] || courseId || "Umum";
}

function resolveForumRootId(post, byId) {
  let curr = post;
  const seen = new Set();
  while (curr.replyToPostId && !seen.has(curr.postId)) {
    seen.add(curr.postId);
    const parent = byId[curr.replyToPostId];
    if (!parent) break;
    curr = parent;
  }
  return curr.postId;
}

function renderForumThreads() {
  const container = document.getElementById("forumThreadsList");
  if (!container) return;

  const byId = {};
  forumPosts.forEach((p) => { byId[p.postId] = p; });

  const topLevel = forumPosts.filter((p) => !p.replyToPostId);
  const repliesByRoot = {};
  forumPosts.filter((p) => p.replyToPostId).forEach((r) => {
    const rootId = resolveForumRootId(r, byId);
    if (!repliesByRoot[rootId]) repliesByRoot[rootId] = [];
    repliesByRoot[rootId].push(r);
  });

  const filtered = topLevel.filter((p) => {
    if (forumCourseFilter !== "all" && p.courseId !== forumCourseFilter) return false;
    if (forumSearchQuery) {
      const childMatch = (repliesByRoot[p.postId] || []).some(
        (r) => r.content.toLowerCase().includes(forumSearchQuery) || r.authorName.toLowerCase().includes(forumSearchQuery)
      );
      const selfMatch = p.content.toLowerCase().includes(forumSearchQuery) || p.authorName.toLowerCase().includes(forumSearchQuery);
      if (!selfMatch && !childMatch) return false;
    }
    return true;
  });

  if (filtered.length === 0) {
    container.innerHTML = `<div class="forum-empty">Belum ada diskusi yang sesuai dengan filter saat ini.</div>`;
    return;
  }

  container.innerHTML = filtered.map((post) => {
    const replies = (repliesByRoot[post.postId] || []).slice().sort((a, b) => a.timestamp - b.timestamp);
    const initial = escapeHtml((post.authorName || "S").charAt(0).toUpperCase());
    const mentorBadge = post.isMentor ? `<span class="forum-mentor-badge">Fasilitator</span>` : `<span class="forum-course-pill">Lv.${post.authorLevel || 1}</span>`;
    const repliesHtml = replies.map((r) => {
      const rInitial = escapeHtml((r.authorName || "S").charAt(0).toUpperCase());
      const rBadge = r.isMentor ? `<span class="forum-mentor-badge">Fasilitator</span>` : `<span class="forum-course-pill">Lv.${r.authorLevel || 1}</span>`;
      const replyTargetText = r.replyToAuthorName ? `<div style="font-size:11.5px;color:var(--teal-deep);font-weight:700;margin-bottom:4px;">Membalas @${escapeHtml(r.replyToAuthorName)}</div>` : "";
      return `
        <div class="forum-reply-card ${r.isMentor ? "mentor" : ""}">
          <div class="forum-post-head" style="margin-bottom:6px;">
            <div class="forum-author-meta">
              <span class="forum-avatar ${r.isMentor ? "mentor" : ""}" style="width:26px;height:26px;font-size:11px;">${rInitial}</span>
              <strong>${escapeHtml(r.authorName)}</strong>
              ${rBadge}
              <span style="font-size:12px;color:var(--muted);">${getTimeAgo(r.timestamp)}</span>
            </div>
            <button type="button" class="btn-danger-ghost" data-delete-post="${escapeHtml(r.postId)}">Hapus</button>
          </div>
          ${replyTargetText}
          <div style="font-size:13.5px;line-height:1.5;white-space:pre-wrap;">${escapeHtml(r.content)}</div>
        </div>
      `;
    }).join("");

    return `
      <article class="forum-thread-card">
        <div class="forum-post-head">
          <div class="forum-author-meta">
            <span class="forum-avatar ${post.isMentor ? "mentor" : ""}">${initial}</span>
            <div>
              <div style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
                <strong>${escapeHtml(post.authorName)}</strong>
                ${mentorBadge}
                <span class="forum-course-pill">${escapeHtml(courseLabel(post.courseId))}</span>
              </div>
              <div style="font-size:12px;color:var(--muted);margin-top:2px;">${getTimeAgo(post.timestamp)} • ${replies.length} Balasan • ${post.likeCount || 0} Suka</div>
            </div>
          </div>
          <button type="button" class="btn-danger-ghost" data-delete-post="${escapeHtml(post.postId)}">Hapus Topik</button>
        </div>

        <div class="forum-post-body">${escapeHtml(post.content)}</div>

        ${replies.length > 0 ? `<div class="forum-replies-wrap">${repliesHtml}</div>` : ""}

        <form class="forum-reply-form" data-reply-form="${escapeHtml(post.postId)}" data-course-id="${escapeHtml(post.courseId)}" data-author-name="${escapeHtml(post.authorName)}">
          <input type="text" placeholder="Tulis balasan sebagai Fasilitator untuk @${escapeHtml(post.authorName)}..." required>
          <button type="submit" class="btn-primary-solid">Balas</button>
        </form>
      </article>
    `;
  }).join("");

  container.querySelectorAll("[data-delete-post]").forEach((btn) => {
    btn.addEventListener("click", () => deleteForumPost(btn.dataset.deletePost));
  });

  container.querySelectorAll("[data-reply-form]").forEach((form) => {
    form.addEventListener("submit", (e) => {
      e.preventDefault();
      const input = form.querySelector("input");
      const text = input.value.trim();
      if (!text) return;
      const parentId = form.dataset.replyForm;
      const parentPost = byId[parentId];
      submitFacilitatorReply(
        parentId,
        form.dataset.courseId,
        form.dataset.authorName,
        parentPost ? parentPost.content : "",
        text
      );
      input.value = "";
    });
  });
}

async function createFacilitatorPost() {
  const input = document.getElementById("forumNewPostInput");
  const courseSelect = document.getElementById("forumNewPostCourse");
  const content = (input?.value || "").trim();
  if (!content) {
    showToast("Tulis isi topik terlebih dahulu.");
    return;
  }
  const postId = `fac_${Date.now()}_${Math.random().toString(36).slice(2, 7)}`;
  const payload = {
    postId,
    courseId: courseSelect.value,
    authorId: "facilitator-mentor",
    authorName: "Fasilitator CodeAcademy",
    content,
    timestamp: Date.now(),
    likeCount: 0,
    authorLevel: 99,
    isMentor: true,
    replyToPostId: null,
    replyToAuthorName: null,
    replyToContentSnippet: null
  };

  if (isFirebaseConnected && firestoreDb) {
    try {
      await firestoreDb.collection("forum_posts").doc(postId).set(payload);
      input.value = "";
      showToast("Topik fasilitator berhasil dikirim ke aplikasi!");
    } catch (err) {
      console.error(err);
      showToast("Gagal mengirim topik ke Firestore.");
    }
  } else {
    forumPosts.unshift(payload);
    saveLocalForumPosts();
    updateForumCountBadge();
    renderForumThreads();
    input.value = "";
    showToast("[Demo Lokal] Topik fasilitator ditambahkan.");
  }
}

async function submitFacilitatorReply(parentPostId, courseId, parentAuthorName, parentContent, replyText) {
  const postId = `fac_rep_${Date.now()}_${Math.random().toString(36).slice(2, 7)}`;
  const snippet = parentContent.length > 60 ? parentContent.slice(0, 57) + "..." : parentContent;
  const payload = {
    postId,
    courseId,
    authorId: "facilitator-mentor",
    authorName: "Fasilitator CodeAcademy",
    content: replyText,
    timestamp: Date.now(),
    likeCount: 0,
    authorLevel: 99,
    isMentor: true,
    replyToPostId: parentPostId,
    replyToAuthorName: parentAuthorName,
    replyToContentSnippet: snippet
  };

  if (isFirebaseConnected && firestoreDb) {
    try {
      await firestoreDb.collection("forum_posts").doc(postId).set(payload);
      showToast("Balasan Fasilitator terkirim real-time ke aplikasi!");
    } catch (err) {
      console.error(err);
      showToast("Gagal mengirim balasan ke Firestore.");
    }
  } else {
    forumPosts.push(payload);
    saveLocalForumPosts();
    updateForumCountBadge();
    renderForumThreads();
    showToast("[Demo Lokal] Balasan Fasilitator terkirim.");
  }
}

async function deleteForumPost(postId) {
  if (!postId) return;
  if (!confirm("Hapus pesan diskusi ini? Jika ini topik utama, semua balasannya juga akan dihapus.")) return;

  const childIds = forumPosts.filter((p) => p.replyToPostId === postId).map((p) => p.postId);
  const idsToDelete = [postId, ...childIds];

  if (isFirebaseConnected && firestoreDb) {
    try {
      const batch = firestoreDb.batch();
      idsToDelete.forEach((id) => {
        batch.delete(firestoreDb.collection("forum_posts").doc(id));
      });
      await batch.commit();
      showToast("Pesan diskusi berhasil dihapus.");
    } catch (err) {
      console.error(err);
      showToast("Gagal menghapus pesan dari Firestore.");
    }
  } else {
    const deleteSet = new Set(idsToDelete);
    forumPosts = forumPosts.filter((p) => !deleteSet.has(p.postId));
    saveLocalForumPosts();
    updateForumCountBadge();
    renderForumThreads();
    showToast("[Demo Lokal] Pesan diskusi dihapus.");
  }
}
