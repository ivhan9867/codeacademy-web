# Kontrak Integrasi Web Dashboard Fasilitator (Firebase Firestore)

Dokumen ini mendefinisikan skema koleksi cloud **Firebase Firestore** yang dipakai bersama antara aplikasi mobile **CodeAcademy (Android Kotlin)** dan **Portal Web CodeAcademy (`public/facilitator/`, `public/verify.html`)**, serta dijaga oleh `firestore.rules`.

---

## 1. Koleksi & Document ID

| Koleksi | Format Document ID | Fungsi |
| :--- | :--- | :--- |
| `project_submissions` | `{firebaseUidOrAccountId}_{courseId}` | Pengajuan & evaluasi Proyek Akhir |
| `certificates` | `CC-yyyyMMxxxx-xxxxxx` | Sertifikat kelulusan resmi (dipakai `verify.html` & Android) |
| `forum_posts` | `{postId}` | Thread diskusi & balasan per kelas |
| `announcements` | `{announcementId}` | Pengumuman fasilitator yang disinkronkan ke Notifikasi Android |
| `facilitators` | `{email_lowercase}` | Allowlist fasilitator terverifikasi Firebase Auth |
| `users` | `{accountIdOrFirebaseUid}` | Peringkat XP & profil publik siswa |

Daftar `courseId` aktif pada kurikulum Front-End Web Development:
- `fe-web-foundation-00` — *Fondasi Web: Dari Nol ke Baris Kode Pertama*
- `fe-css-02` — *CSS Layout Modern: Flexbox & Grid*
- `fe-js-03` — *JavaScript Fundamentals*

---

## 2. Skema `project_submissions/{id}`

| Field | Tipe Firestore | Diisi Oleh | Deskripsi |
| :--- | :--- | :--- | :--- |
| `submissionId` | `string` | Mobile App | ID lokal submission |
| `accountId` | `number` | Mobile App | ID akun Room lokal |
| `firebaseUid` | `string` | Mobile App | UID Firebase Auth siswa |
| `courseId` | `string` | Mobile App | ID kelas |
| `courseTitle` | `string` | Mobile App | Nama kelas saat dikirim |
| `studentName` | `string` | Mobile App | Nama siswa (juga kompatibel `userName` pada data demo web) |
| `studentEmail` | `string` | Mobile App | Email siswa |
| `finalQuizScore` | `number` | Mobile App | Skor terbaik kuis akhir (`0–100`, bobot **40%**) |
| `status` | `string` | Mobile & Web | `"PENDING_REVIEW"`, `"REVISION_REQUESTED"`, atau `"APPROVED"` |
| `workspaceCode` | `string` | Mobile App | Kode HTML/CSS/JS dari Workspace Editor |
| `projectUrl` | `string?` | Mobile App | Tautan repositori / demo eksternal (harus `http:`/`https:`) |
| `studentNotes` | `string` | Mobile App | Catatan penjelasan siswa |
| `checkedRubricIdsJson` | `string` | Mobile & Web | Array JSON ID rubrik yang terpenuhi, mis. `["semantik","clean"]` |
| `attemptNumber` | `number` | Mobile App | Nomor percobaan pengiriman |
| `submittedAt` | `number` | Mobile App | Epoch millis saat siswa mengirim |
| `facilitatorScore` | `number?` | Web Fasilitator | Skor proyek fasilitator (`0–100`, bobot **60%**) |
| `facilitatorFeedback` | `string?` | Web Fasilitator | Catatan evaluasi / revisi (web juga menulis `feedback` untuk kompatibilitas) |
| `facilitatorName` | `string?` | Web Fasilitator | Nama penilai |
| `reviewedAt` | `number?` | Web Fasilitator | Epoch millis saat penilaian disimpan |
| `certificateId` | `string?` | Web & Mobile | ID sertifikat `CC-...` yang diterbitkan saat `APPROVED` |
| `updatedAt` | `number` | Mobile & Web | Timestamp pembaruan terakhir |

---

## 3. Rumus Nilai Akhir & Siklus Status

- **Rumus Nilai Akhir:**
  $$\text{Final Score} = \text{round}(0.40 \times \text{finalQuizScore} + 0.60 \times \text{facilitatorScore})$$
- **Ambang Kelulusan:** $\text{Final Score} \ge 70$
- Saat Web Fasilitator menyetujui submission (`APPROVED`, nilai akhir $\ge 70$):
  1. Web menghasilkan atau memakai `certificateId` berformat `CC-yyyyMMxxxx-xxxxxx`.
  2. Web menulis dokumen ke `certificates/{certificateId}` dan memperbarui `project_submissions/{id}` secara atomik.
  3. Aplikasi Android menerima pembaruan melalui `attachProjectSubmissionListener`, mengadopsi `certificateId` dari server agar cocok dengan Portal Verifikasi, menyimpan ke Room, memberi **+150 XP**, dan menampilkan notifikasi kelulusan.
