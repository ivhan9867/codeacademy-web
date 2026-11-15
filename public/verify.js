// ==========================================
// CodeAcademy - Certificate Verification Logic
// Direct Firestore lookup without wildcard bypass
// ==========================================

const DEMO_SAMPLE_CERTS = {
  "CC-2026110101-A9K3P2": {
    certId: "CC-2026110101-A9K3P2",
    userName: "Siti Aminah",
    courseTitle: "CSS Layout Modern: Flexbox & Grid",
    quizScore: 88,
    facilitatorScore: 92,
    finalScore: 90,
    issuedAt: new Date("2026-11-12T10:00:00Z").getTime(),
    facilitatorName: "Fasilitator Demo CodeAcademy",
    isSampleDemo: true
  }
};

let verifyDb = null;

function initVerifyFirebase() {
  const cfg = window.CODEACADEMY_FIREBASE_CONFIG || window.firebaseConfig;
  if (typeof firebase === "undefined" || !cfg) return;
  try {
    if (!firebase.apps.length) {
      firebase.initializeApp(cfg);
    }
    verifyDb = firebase.firestore();
  } catch (err) {
    console.warn("Firestore init failed in verify.js:", err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  initVerifyFirebase();

  const urlParams = new URLSearchParams(window.location.search);
  const queryId = urlParams.get("id") || urlParams.get("certId");
  const input = document.getElementById("certIdInput");

  if (queryId) {
    input.value = queryId.trim().toUpperCase();
    verifyCert(input.value);
  }

  document.getElementById("verifyForm").addEventListener("submit", (e) => {
    e.preventDefault();
    const id = input.value.trim().toUpperCase();
    input.value = id;
    verifyCert(id);
  });

  document.querySelectorAll(".try button").forEach((btn) => {
    btn.addEventListener("click", () => {
      const sampleId = btn.dataset.id || "";
      input.value = sampleId;
      verifyCert(sampleId);
    });
  });
});

async function verifyCert(rawId) {
  const id = (rawId || "").trim().toUpperCase();
  const resultCard = document.getElementById("certResult");
  const notFoundCard = document.getElementById("certNotFound");
  const submitBtn = document.getElementById("btnVerifySubmit");
  const notFoundHint = document.getElementById("notFoundHint");

  if (!id) return;

  submitBtn.disabled = true;
  submitBtn.textContent = "Memeriksa...";

  try {
    let record = null;

    if (verifyDb) {
      const snap = await verifyDb.collection("certificates").doc(id).get();
      if (snap.exists) {
        record = snap.data();
      }
    }

    if (!record && DEMO_SAMPLE_CERTS[id]) {
      record = DEMO_SAMPLE_CERTS[id];
    }

    if (record) {
      notFoundCard.classList.remove("show");
      document.getElementById("resRecipientName").textContent = (record.userName || "PESERTA").toUpperCase();
      document.getElementById("resCertId").textContent = record.certId || id;
      document.getElementById("resCourseTitle").textContent = record.courseTitle || record.courseId || "-";

      const finalScore = Number(record.finalScore) || 0;
      const predicate =
        finalScore >= 90
          ? "Dengan Pujian"
          : finalScore >= 80
            ? "Sangat Memuaskan"
            : "Memuaskan";
      document.getElementById("resScore").textContent = `${finalScore}/100 (${predicate})`;

      document.getElementById("resDate").textContent = formatIssuedDate(record.issuedAt);
      document.getElementById("resFacilitator").textContent = record.facilitatorName || "Tim Fasilitator CodeAcademy";
      document.getElementById("resStatusBadge").textContent = record.isSampleDemo
        ? "Contoh Demo Tervalidasi (Offline Preview)"
        : "Sertifikat asli dan terdaftar di Firestore";
      document.getElementById("resRegistrySource").textContent = record.isSampleDemo
        ? "Data contoh demo lokal"
        : "Terdaftar resmi di Firestore CodeAcademy";

      resultCard.classList.add("show");
    } else {
      resultCard.classList.remove("show");
      notFoundHint.textContent =
        "ID sertifikat tidak ditemukan di basis data Firestore. Pastikan ID berawalan CC- diketik persis seperti yang tercetak di aplikasi.";
      notFoundCard.classList.add("show");
    }
  } catch (err) {
    console.error("Verify lookup error:", err);
    resultCard.classList.remove("show");
    notFoundHint.textContent =
      "Terjadi kendala jaringan atau izin saat memeriksa basis data Firestore (" + (err.message || "error") + ").";
    notFoundCard.classList.add("show");
  } finally {
    submitBtn.disabled = false;
    submitBtn.textContent = "Periksa ID";
  }
}

function formatIssuedDate(ts) {
  if (!ts) return "-";
  const d = new Date(ts);
  if (isNaN(d.getTime())) return "-";
  return d.toLocaleDateString("id-ID", {
    day: "numeric",
    month: "long",
    year: "numeric"
  });
}
