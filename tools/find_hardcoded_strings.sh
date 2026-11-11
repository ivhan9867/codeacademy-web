#!/usr/bin/env bash

# tools/find_hardcoded_strings.sh
# Counts hardcoded UI text literals in Kotlin files for the Active Tree by default,
# and displays ignored tree files separately as "diabaikan (tidak dihitung)".

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR" || {
    echo "ERROR: Gagal berpindah ke direktori proyek: $PROJECT_DIR" >&2
    exit 1
}

SRC_DIR="$PROJECT_DIR/app/src/main/java/com/example"

if [ ! -d "$SRC_DIR" ]; then
    echo "ERROR: Direktori sumber tidak ditemukan: $SRC_DIR" >&2
    exit 1
fi

python3 - << 'EOF'
import os
import re
import sys

src_dir = "app/src/main/java/com/example"

if not os.path.isdir(src_dir):
    sys.stderr.write(f"ERROR: Direktori sumber '{src_dir}' tidak ditemukan dari working directory '{os.getcwd()}'.\n")
    sys.exit(1)

# Subpackages classified as Ignored / Unused Tree (Single Source of Truth: tools/unused_subpackages.txt)
unused_config_file = "tools/unused_subpackages.txt"
if os.path.exists(unused_config_file):
    with open(unused_config_file, "r", encoding="utf-8") as f:
        unused_dirs = {line.strip() for line in f if line.strip() and not line.strip().startswith("#")}
else:
    unused_dirs = {
        "app/src/main/java/com/example/ui/navigation",
        "app/src/main/java/com/example/ui/screens/auth",
        "app/src/main/java/com/example/ui/screens/home",
        "app/src/main/java/com/example/ui/screens/catalog",
        "app/src/main/java/com/example/ui/screens/profile",
        "app/src/main/java/com/example/ui/screens/social",
        "app/src/main/java/com/example/ui/screens/forum",
        "app/src/main/java/com/example/ui/screens/quiz",
        "app/src/main/java/com/example/ui/screens/lesson",
        "app/src/main/java/com/example/ui/screens/playground",
        "app/src/main/java/com/example/ui/screens/aitutor",
        "app/src/main/java/com/example/ui/screens/notifications",
        "app/src/main/java/com/example/ui/screens/ar",
        "app/src/main/java/com/example/ui/screens/leaderboard",
        "app/src/main/java/com/example/data/repository",
        "app/src/main/java/com/example/data/model",
    }

# Regex to match hardcoded UI text parameters
ui_pattern = re.compile(
    r'(?:Text\s*\(\s*text\s*=\s*|Text\s*\(\s*|text\s*=\s*|title\s*=\s*|label\s*=\s*|placeholder\s*=\s*|contentDescription\s*=\s*|subtitle\s*=\s*|description\s*=\s*)"([^"\n]{2,})"'
)

active_counts = {}
ignored_counts = {}
active_files_scanned = 0

for root, dirs, files in os.walk(src_dir):
    for file in files:
        if not file.endswith(".kt"):
            continue
        filepath = os.path.join(root, file)
        relpath = filepath.replace("\\", "/")

        is_ignored = False
        for udir in unused_dirs:
            if relpath.startswith(udir):
                is_ignored = True
                break

        if "ui/viewmodel/" in relpath and not relpath.endswith("SocialViewModel.kt"):
            is_ignored = True

        if not is_ignored:
            active_files_scanned += 1

        with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
            content = f.read()

        matches = ui_pattern.findall(content)
        valid_matches = []
        for m in matches:
            if m.startswith("http") or m.startswith("com.") or m.startswith("ic_") or m.startswith("asset:"):
                continue
            valid_matches.append(m)

        count = len(valid_matches)
        if count > 0:
            if is_ignored:
                ignored_counts[relpath] = count
            else:
                active_counts[relpath] = count

if active_files_scanned == 0:
    sys.stderr.write(f"ERROR: Jumlah file aktif yang dipindai adalah 0 di '{src_dir}'. Pemindaian gagal.\n")
    sys.exit(1)

print("### Pohon Aktif (Hitungan Resmi)")
print("| No | File | Jumlah Literal Hardcoded |")
print("|---|---|---|")
sorted_active = sorted(active_counts.items(), key=lambda x: x[1], reverse=True)
total_active = 0
for idx, (f, c) in enumerate(sorted_active, 1):
    print(f"| {idx} | `{f}` | {c} |")
    total_active += c
print(f"| | **TOTAL POHON AKTIF** | **{total_active}** |\n")

print("### Pohon Diabaikan (Tidak Dihitung)")
print("| No | File | Keterangan Status |")
print("|---|---|---|")
sorted_ignored = sorted(ignored_counts.items(), key=lambda x: x[1], reverse=True)
total_ignored = 0
for idx, (f, c) in enumerate(sorted_ignored, 1):
    print(f"| {idx} | `{f}` | diabaikan (tidak dihitung) |")
    total_ignored += c
print(f"| | **TOTAL POHON DIABAIKAN** | **{total_ignored} (diabaikan)** |\n")

print(f"**HITUNGAN RESMI POHON AKTIF**: {total_active}")
EOF
