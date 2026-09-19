# PANDUAN UPLOAD LOVY CHAT KE GOOGLE PLAY STORE

## 1. INFORMASI IDENTITAS APLIKASI
- **Package Name / Application ID:** `com.lovychat.gecckocreator`
- **Version Code:** `1`
- **Version Name:** `1.0`
- **Min SDK:** `24` (Android 7.0 Nougat ke atas)
- **Target SDK / Compile SDK:** `36` (Sesuai ketentuan Google Play terbaru)
- **Nama Aplikasi (Launcher):** `Lovy Chat`

---

## 2. KREDENSIAL SIGNATURE KEY (KEYSTORE)
File keystore telah dibuat secara otomatis dan digunakan untuk menandatangani AAB & APK:
- **Nama File Keystore:** `release.jks` (Lokasi: root project)
- **Keystore Format:** PKCS12 (RSA 2048-bit, valid hingga tahun 2054)
- **Key Alias:** `upload`
- **Keystore Password:** `LovyChatRelease2026`
- **Key Password:** `LovyChatRelease2026`
- **Certificate SHA-1:** `27:E9:73:FC:27:83:8C:9E:9A:E3:E0:00:2C:CF:B0:CB:3B:64:C8:79`
- **Certificate SHA-256:** `8B:7D:22:62:F4:0D:8D:C8:62:46:20:5F:14:AE:1F:8D:36:03:74:85:09:31:09:5B:26:71:A0:20:C3:59:BD:43`

> ⚠️ **PENTING:** Simpan file `release.jks` atau teks isi `release.keystore.base64` di tempat aman. Kunci ini wajib digunakan untuk setiap update aplikasi selanjutnya di Google Play Console!

---

## 3. FILE HASIL BUILD YANG TELAH SELESAI
1. **Google Play Store Bundle (AAB):**
   - Path: `app/build/outputs/bundle/release/app-release.aab`
   - Ukuran: ~20 MB
   - Status: Sudah di-signing dengan `release.jks`, siap langsung di-upload ke menu Production / Testing di Google Play Console.

2. **Universal APK (Untuk Tes di HP Fisik Langsung):**
   - Path: `app/build/outputs/apk/release/app-release.apk`
   - Ukuran: ~21 MB
   - Status: Sudah di-signing dengan `release.jks`.

---

## 4. WORKFLOW CI/CD GITHUB ACTIONS (.YML)
File workflow otomatis telah dibuat di:
`.github/workflows/android-release.yml`

Setiap kali Anda push kode ke GitHub, GitHub Actions akan:
1. Membaca keystore dari file atau dari GitHub Secrets `KEYSTORE_BASE64`.
2. Menjalankan Gradle task `:app:bundleRelease` dan `:app:assembleRelease`.
3. Mengunggah `app-release.aab` dan `app-release.apk` ke tab Artifacts GitHub Actions untuk didownload kapan saja.

### Secrets yang dapat diatur di GitHub (Opsional jika ingin override):
- `KEYSTORE_BASE64`: Isi teks dari file `release.keystore.base64`
- `STORE_PASSWORD`: `LovyChatRelease2026`
- `KEY_ALIAS`: `upload`
- `KEY_PASSWORD`: `LovyChatRelease2026`
