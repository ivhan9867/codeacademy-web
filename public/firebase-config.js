// Konfigurasi Firebase Web SDK untuk CodeAcademy Portal
// Ambil nilai ini dari Firebase Console -> Project Settings -> Your apps (Web app)
// Gunakan Project Firebase yang SAMA dengan file google-services.json di aplikasi Android.

window.CODEACADEMY_FIREBASE_CONFIG = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_PROJECT_ID.appspot.com",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID"
};

window.isFirebaseConfigured = function () {
  const cfg = window.CODEACADEMY_FIREBASE_CONFIG;
  return Boolean(cfg && cfg.projectId && cfg.projectId !== "YOUR_PROJECT_ID" && cfg.apiKey && cfg.apiKey !== "YOUR_API_KEY");
};
