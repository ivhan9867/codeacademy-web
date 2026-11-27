// ==========================================
// CodeAcademy - Certificate Verification Logic
// Direct Firestore lookup without wildcard bypass
// ==========================================

let verifyDb = null;
let lastVerifiedRecord = null;
let lastVerifiedId = "";

function vLang() {
  return window.CodeAcademyWebI18n ? window.CodeAcademyWebI18n.getLang() : "en";
}

function vT(key, vars) {
  return window.CodeAcademyWebI18n ? window.CodeAcademyWebI18n.t(key, vars) : key;
}

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

  window.addEventListener("ca-lang-change", () => {
    if (lastVerifiedRecord) {
      renderVerifiedRecord(lastVerifiedRecord, lastVerifiedId);
    }
  });

  const btnPrint = document.getElementById("btnPrintCert");
  if (btnPrint) {
    btnPrint.addEventListener("click", () => window.print());
  }

  const btnCopyLink = document.getElementById("btnCopyVerifyLink");
  if (btnCopyLink) {
    btnCopyLink.addEventListener("click", async () => {
      const id = lastVerifiedId || document.getElementById("resCertId")?.textContent?.trim() || "";
      const url = id
        ? `${window.location.origin}${window.location.pathname}?id=${encodeURIComponent(id)}`
        : window.location.href;
      try {
        await navigator.clipboard.writeText(url);
        const orig = vT("verify_btn_copy_link");
        btnCopyLink.textContent = vT("verify_copied_link");
        setTimeout(() => {
          btnCopyLink.textContent = orig;
        }, 2200);
      } catch (_) {
        window.prompt("Copy link:", url);
      }
    });
  }
});

function renderVerifiedRecord(record, id) {
  const fallbackName = vLang() === "id" ? "PESERTA" : "STUDENT";
  const fallbackFac = vLang() === "id" ? "Tim Fasilitator CodeAcademy" : "CodeAcademy Facilitator Team";
  document.getElementById("resRecipientName").textContent = (record.userName || fallbackName).toUpperCase();
  document.getElementById("resCertId").textContent = record.certId || id;
  document.getElementById("resCourseTitle").textContent = record.courseTitle || record.courseId || "-";

  const finalScore = Number(record.finalScore) || 0;
  const predicate =
    finalScore >= 90
      ? vT("verify_pred_distinction")
      : finalScore >= 80
        ? vT("verify_pred_very_good")
        : vT("verify_pred_good");
  document.getElementById("resScore").textContent = `${finalScore}/100 (${predicate})`;

  document.getElementById("resDate").textContent = formatIssuedDate(record.issuedAt);
  document.getElementById("resFacilitator").textContent = record.facilitatorName || fallbackFac;
  document.getElementById("resStatusBadge").textContent = vT("verify_status_ok");
  document.getElementById("resRegistrySource").textContent = vT("verify_reg_src");
}

async function verifyCert(rawId) {
  const id = (rawId || "").trim().toUpperCase();
  const resultCard = document.getElementById("certResult");
  const notFoundCard = document.getElementById("certNotFound");
  const submitBtn = document.getElementById("btnVerifySubmit");
  const notFoundHint = document.getElementById("notFoundHint");

  if (!id) return;

  submitBtn.disabled = true;
  submitBtn.textContent = vT("verify_checking");

  try {
    let record = null;

    if (verifyDb) {
      const snap = await verifyDb.collection("certificates").doc(id).get();
      if (snap.exists) {
        record = snap.data();
      }
    }

    if (record) {
      lastVerifiedRecord = record;
      lastVerifiedId = id;
      notFoundCard.classList.remove("show");
      renderVerifiedRecord(record, id);
      resultCard.classList.add("show");
    } else {
      lastVerifiedRecord = null;
      lastVerifiedId = "";
      resultCard.classList.remove("show");
      notFoundHint.textContent = vT("verify_not_found_db");
      notFoundCard.classList.add("show");
    }
  } catch (err) {
    console.error("Verify lookup error:", err);
    lastVerifiedRecord = null;
    resultCard.classList.remove("show");
    notFoundHint.textContent = vT("verify_network_err", { msg: err.message || "error" });
    notFoundCard.classList.add("show");
  } finally {
    submitBtn.disabled = false;
    submitBtn.textContent = vT("verify_btn");
  }
}

function formatIssuedDate(ts) {
  if (!ts) return "-";
  const d = new Date(ts);
  if (isNaN(d.getTime())) return "-";
  const locale = vLang() === "id" ? "id-ID" : "en-US";
  return d.toLocaleDateString(locale, {
    day: "numeric",
    month: "long",
    year: "numeric"
  });
}
