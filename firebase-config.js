// Konfigurasi Firebase Web SDK untuk CodeAcademy Portal
// Terhubung otomatis ke Firebase Project: codeacademy-6a361 (sama dengan app/google-services.json)

window.CODEACADEMY_FIREBASE_CONFIG = {
  apiKey: "AIzaSyDNx48ix_LleFX6_gcJmYAubdbTLYNfBrs",
  authDomain: "codeacademy-6a361.firebaseapp.com",
  projectId: "codeacademy-6a361",
  storageBucket: "codeacademy-6a361.firebasestorage.app",
  messagingSenderId: "823377846566",
  appId: "1:823377846566:android:c826094ec2ea97bb0ee1e7"
};

// Alias kompatibilitas untuk modul web portal
window.firebaseConfig = window.CODEACADEMY_FIREBASE_CONFIG;

window.isFirebaseConfigured = function () {
  const cfg = window.CODEACADEMY_FIREBASE_CONFIG;
  return Boolean(cfg && cfg.projectId && cfg.projectId !== "YOUR_PROJECT_ID" && cfg.apiKey && cfg.apiKey !== "YOUR_API_KEY");
};
