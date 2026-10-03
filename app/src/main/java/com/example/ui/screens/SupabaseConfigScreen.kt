package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupabaseConfigScreen(
    currentUrl: String,
    currentAnonKey: String,
    isConnected: Boolean,
    connectionStatusMessage: String?,
    isTestingConnection: Boolean,
    onBack: () -> Unit,
    onSaveCredentials: (String, String) -> Unit,
    onTestConnection: () -> Unit,
    onClearCredentials: () -> Unit,
    onUpdateRemoteVersion: ((Int, Int, String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val currentAppVersionCode = com.example.BuildConfig.VERSION_CODE
    val currentAppVersionName = com.example.BuildConfig.VERSION_NAME
    val sharedPrefs = remember { context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE) }
    
    var minCodeInput by remember { mutableStateOf(sharedPrefs.getInt("remote_min_version_code", 1).toString()) }
    var latestCodeInput by remember { mutableStateOf(sharedPrefs.getInt("remote_latest_version_code", 1).toString()) }
    var latestNameInput by remember { mutableStateOf(sharedPrefs.getString("remote_latest_version_name", "1.0") ?: "1.0") }

    var urlInput by remember(currentUrl) { mutableStateOf(currentUrl) }
    var keyInput by remember(currentAnonKey) { mutableStateOf(currentAnonKey) }
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sqlSchema = """
-- =======================================================
-- LOVY CHAT: SKRIP LENGKAP STRUKTUR DATABASE SUPABASE
-- Jalankan skrip ini di SQL Editor di dashboard Supabase Anda.
-- Skrip ini aman dijalankan berulang kali (tidak akan menghapus data yang ada).
-- =======================================================

-- 1. TABEL PENGGUNA SEKITAR (nearby_users)
create table if not exists nearby_users (
    id text primary key,
    name text not null,
    gender text not null,
    distance_meters int default 100,
    bio text,
    avatar_hex bigint default 3046706,
    is_online boolean default true,
    last_active_at bigint default 0,
    avatar_url text,
    city text default 'Indonesia',
    fcm_token text
);
alter table nearby_users add column if not exists name text;
alter table nearby_users add column if not exists gender text;
alter table nearby_users add column if not exists distance_meters int default 100;
alter table nearby_users add column if not exists bio text;
alter table nearby_users add column if not exists avatar_hex bigint default 3046706;
alter table nearby_users add column if not exists is_online boolean default true;
alter table nearby_users add column if not exists last_active_at bigint default 0;
alter table nearby_users add column if not exists avatar_url text;
alter table nearby_users add column if not exists city text default 'Indonesia';
alter table nearby_users add column if not exists fcm_token text;

-- 2. TABEL BOTOL LAUTAN (ocean_bottles)
create table if not exists ocean_bottles (
    id text primary key,
    sender_id text not null,
    sender_name text not null,
    sender_gender text not null,
    content text not null,
    created_at bigint not null,
    location_hint text default 'Lautan Nusantara',
    avatar_hex bigint default 33679,
    avatar_url text
);
alter table ocean_bottles add column if not exists sender_id text;
alter table ocean_bottles add column if not exists sender_name text;
alter table ocean_bottles add column if not exists sender_gender text;
alter table ocean_bottles add column if not exists content text;
alter table ocean_bottles add column if not exists created_at bigint;
alter table ocean_bottles add column if not exists location_hint text default 'Lautan Nusantara';
alter table ocean_bottles add column if not exists avatar_hex bigint default 33679;
alter table ocean_bottles add column if not exists avatar_url text;

-- 3. TABEL PESAN CHAT (chat_messages)
create table if not exists chat_messages (
    id text primary key,
    conversation_id text not null,
    sender_id text not null,
    receiver_id text,
    text text not null,
    created_at bigint not null,
    deleted_for_sender boolean default false,
    deleted_for_receiver boolean default false,
    image_url text,
    is_read boolean default false
);
alter table chat_messages add column if not exists conversation_id text;
alter table chat_messages add column if not exists sender_id text;
alter table chat_messages add column if not exists receiver_id text;
alter table chat_messages add column if not exists text text;
alter table chat_messages add column if not exists created_at bigint;
alter table chat_messages add column if not exists deleted_for_sender boolean default false;
alter table chat_messages add column if not exists deleted_for_receiver boolean default false;
alter table chat_messages add column if not exists image_url text;
alter table chat_messages add column if not exists is_read boolean default false;
alter table chat_messages add column if not exists reply_to_id text;
alter table chat_messages add column if not exists reply_to_sender text;
alter table chat_messages add column if not exists reply_to_text text;

-- 4. TABEL MOMEN SOSIAL (moments)
create table if not exists moments (
    id text primary key,
    author_id text not null,
    author_name text not null,
    content text not null,
    likes_count int default 0,
    comments_count int default 0,
    created_at bigint not null,
    author_avatar_hex bigint default 4222123520,
    image_url text,
    author_avatar_url text,
    location_tag text
);
alter table moments add column if not exists author_id text;
alter table moments add column if not exists author_name text;
alter table moments add column if not exists content text;
alter table moments add column if not exists likes_count int default 0;
alter table moments add column if not exists comments_count int default 0;
alter table moments add column if not exists created_at bigint;
alter table moments add column if not exists author_avatar_hex bigint default 4222123520;
alter table moments add column if not exists image_url text;
alter table moments add column if not exists author_avatar_url text;
alter table moments add column if not exists location_tag text;

-- 5. TABEL AKUN PENGGUNA (app_accounts)
create table if not exists app_accounts (
    id text primary key,
    username text unique not null,
    password_hash text,
    display_name text not null,
    gender text default 'FEMALE',
    bio text default '',
    avatar_url text,
    google_id text unique,
    google_email text,
    created_at bigint not null,
    last_login_at bigint not null,
    fcm_token text
);
alter table app_accounts add column if not exists username text;
alter table app_accounts add column if not exists password_hash text;
alter table app_accounts add column if not exists display_name text;
alter table app_accounts add column if not exists gender text default 'FEMALE';
alter table app_accounts add column if not exists bio text default '';
alter table app_accounts add column if not exists avatar_url text;
alter table app_accounts add column if not exists google_id text;
alter table app_accounts add column if not exists google_email text;
alter table app_accounts add column if not exists created_at bigint;
alter table app_accounts add column if not exists last_login_at bigint;
alter table app_accounts add column if not exists fcm_token text;

-- 6. TABEL LAPORAN PENGGUNA & MOMEN (user_reports)
create table if not exists user_reports (
    id text primary key,
    reporter_id text,
    target_id text,
    target_name text,
    report_type text not null, -- 'USER' atau 'MOMENT'
    reason text not null,
    notes text,
    created_at bigint not null
);
alter table user_reports add column if not exists reporter_id text;
alter table user_reports add column if not exists target_id text;
alter table user_reports add column if not exists target_name text;
alter table user_reports add column if not exists report_type text;
alter table user_reports add column if not exists reason text;
alter table user_reports add column if not exists notes text;
alter table user_reports add column if not exists created_at bigint;

-- 7. HAK AKSES PERIZINAN (GRANTS & RLS)
grant usage on schema public to anon, authenticated;
grant all on all tables in schema public to anon, authenticated;
grant all on all sequences in schema public to anon, authenticated;

alter table nearby_users enable row level security;
drop policy if exists "Allow anon all nearby" on nearby_users;
create policy "Allow anon all nearby" on nearby_users for all using (true) with check (true);

alter table ocean_bottles enable row level security;
drop policy if exists "Allow anon all bottles" on ocean_bottles;
create policy "Allow anon all bottles" on ocean_bottles for all using (true) with check (true);

alter table chat_messages enable row level security;
drop policy if exists "Allow anon all messages" on chat_messages;
create policy "Allow anon all messages" on chat_messages for all using (true) with check (true);

alter table moments enable row level security;
drop policy if exists "Allow anon all moments" on moments;
create policy "Allow anon all moments" on moments for all using (true) with check (true);

alter table app_accounts enable row level security;
drop policy if exists "Allow anon all accounts" on app_accounts;
create policy "Allow anon all accounts" on app_accounts for all using (true) with check (true);

alter table user_reports enable row level security;
drop policy if exists "Allow anon all reports" on user_reports;
create policy "Allow anon all reports" on user_reports for all using (true) with check (true);

-- 8. INDEKS PERFORMA CEPAT (INDEXES)
create index if not exists idx_accounts_username on app_accounts (username);
create index if not exists idx_accounts_google on app_accounts (google_email);
create index if not exists idx_chat_messages_conv on chat_messages (conversation_id, created_at asc);
create index if not exists idx_chat_messages_sender on chat_messages (sender_id);
create index if not exists idx_chat_messages_receiver on chat_messages (receiver_id);
create index if not exists idx_ocean_bottles_created on ocean_bottles (created_at desc);
create index if not exists idx_moments_created on moments (created_at desc);
create index if not exists idx_user_reports_created on user_reports (created_at desc);

-- 9. AKTIFKAN SUPABASE REALTIME (INSTANT WEBSOCKET SUBSCRIPTION)
alter publication supabase_realtime add table chat_messages;
    """.trimIndent()

    val sqlCronCleanup = """
-- ============================================================================
-- LOVY CHAT: SKRIP SQL CRONJOB & PEMBERSIHAN OTOMATIS (VERSI AMAN)
-- Jalankan skrip ini di: Supabase Dashboard -> SQL Editor -> New Query -> Run
-- ============================================================================

-- 1. AKTIFKAN EKSTENSI PG_CRON
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- 2. BERSIHKAN DATA YATIM (ORPHAN) & DATA TANPA NAMA TERLEBIH DAHULU AGAR TIDAK ADA ERROR CONSTRAINT
DELETE FROM nearby_users WHERE id NOT IN (SELECT id FROM app_accounts) OR name IS NULL OR TRIM(name) = '';
DELETE FROM moments WHERE author_id NOT IN (SELECT id FROM app_accounts);
DELETE FROM ocean_bottles WHERE sender_id NOT IN (SELECT id FROM app_accounts);
DELETE FROM chat_messages WHERE sender_id NOT IN (SELECT id FROM app_accounts);

-- 3. SETUP ON DELETE CASCADE
DO ${"$$"}
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'nearby_users') THEN
        ALTER TABLE nearby_users DROP CONSTRAINT IF EXISTS fk_nearby_users_account;
        ALTER TABLE nearby_users ADD CONSTRAINT fk_nearby_users_account 
            FOREIGN KEY (id) REFERENCES app_accounts(id) ON DELETE CASCADE;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'moments') THEN
        ALTER TABLE moments DROP CONSTRAINT IF EXISTS fk_moments_author;
        ALTER TABLE moments ADD CONSTRAINT fk_moments_author 
            FOREIGN KEY (author_id) REFERENCES app_accounts(id) ON DELETE CASCADE;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'ocean_bottles') THEN
        ALTER TABLE ocean_bottles DROP CONSTRAINT IF EXISTS fk_bottles_sender;
        ALTER TABLE ocean_bottles ADD CONSTRAINT fk_bottles_sender 
            FOREIGN KEY (sender_id) REFERENCES app_accounts(id) ON DELETE CASCADE;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'chat_messages') THEN
        ALTER TABLE chat_messages DROP CONSTRAINT IF EXISTS fk_messages_sender;
        ALTER TABLE chat_messages ADD CONSTRAINT fk_messages_sender 
            FOREIGN KEY (sender_id) REFERENCES app_accounts(id) ON DELETE CASCADE;
    END IF;
END ${"$$"};

-- 4. FUNGSI PEMBERSIHAN OTOMATIS (FUNGSI CRON)
CREATE OR REPLACE FUNCTION purge_lovy_inactive_and_deleted_data()
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS ${"$$"}
DECLARE
    fifteen_days_ago_epoch BIGINT;
BEGIN
    -- Waktu 15 hari yang lalu dalam milidetik (epoch ms)
    fifteen_days_ago_epoch := (EXTRACT(EPOCH FROM (NOW() - INTERVAL '15 days')) * 1000)::BIGINT;

    -- [A] Hapus akun yang tidak login/aktif lebih dari 15 hari
    -- Karena ada ON DELETE CASCADE, semua nearby, moments, bottles, dan chats milik user ini otomatis terhapus!
    DELETE FROM app_accounts
    WHERE last_login_at < fifteen_days_ago_epoch;

    -- [B] Hapus FISIK pesan chat jika KEDUA belah pihak sudah menghapus
    DELETE FROM chat_messages
    WHERE deleted_for_sender = TRUE AND deleted_for_receiver = TRUE;

    -- [C] Hapus pesan usang (> 30 hari) yang sudah dihapus oleh pengirim
    DELETE FROM chat_messages
    WHERE deleted_for_sender = TRUE 
      AND created_at < (EXTRACT(EPOCH FROM (NOW() - INTERVAL '30 days')) * 1000)::BIGINT;

    -- [D] Bersihkan data yatim & data tanpa nama (jika ada data lama/dummy yang tersisa)
    DELETE FROM nearby_users WHERE id NOT IN (SELECT id FROM app_accounts) OR name IS NULL OR TRIM(name) = '';
END;
${"$$"};

-- 5. JADWAL CRONJOB (SETIAP HARI PUKUL 03:00 UTC)
SELECT cron.unschedule('daily-purge-lovy-inactive-data') 
WHERE EXISTS (
    SELECT 1 FROM cron.job WHERE jobname = 'daily-purge-lovy-inactive-data'
);

SELECT cron.schedule(
    'daily-purge-lovy-inactive-data',
    '0 3 * * *',
    ${"$$"}SELECT purge_lovy_inactive_and_deleted_data()${"$$"}
);
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Koneksi Cloud & Sinkronisasi",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("supabase_btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldGreen)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isConnected) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) EmeraldGreen else Color(0xFFFB8C00))
                    ) {
                        Icon(
                            imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        val isPocketBaseActive = com.example.data.supabase.SupabaseClient.isPocketBase()
                        Text(
                            text = if (isConnected) {
                                if (isPocketBaseActive) "PocketBase + Centrifugo Aktif" else "Layanan Supabase Aktif"
                            } else {
                                "Layanan Cloud Belum Terhubung"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = connectionStatusMessage ?: if (isConnected) {
                                if (isPocketBaseActive) "Database: PocketBase (173.249.59.183:8090) • Realtime: Centrifugo (Port 8000)" else "Penyimpanan cloud aktif untuk pesan & cerita."
                            } else {
                                "Aplikasi saat ini berjalan dalam mode penyimpanan perangkat."
                            },
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Input Form Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Konfigurasi Kredensial",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // URL
                    Text(
                        text = "PocketBase Server URL",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        placeholder = { Text("http://173.249.59.183:8090", fontSize = 13.sp) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = EmeraldGreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_supabase_url")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Anon Key
                    Text(
                        text = "Access Token / API Key (Opsional untuk PocketBase)",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        placeholder = { Text("Kosongkan untuk server PocketBase default", fontSize = 13.sp) },
                        minLines = 2,
                        maxLines = 3,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = EmeraldGreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_supabase_key")
                    )

                    // Presets
                    Text(
                        text = "Pilihan Cepat Server:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                urlInput = "http://173.249.59.183:8090"
                                keyInput = ""
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("PocketBase VPS", fontSize = 11.5.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                urlInput = "https://azcxvjjcjytfqwhfcbui.supabase.co"
                                keyInput = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF6Y3h2ampjanl0ZnF3aGZjYnVpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODk2NzgzMTgsImV4cCI6MjEwNTI1NDMxOH0.h8M71nfUKA6fd69yKZIHIwBH1ssI1vHq_1bNYCPQmhY"
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Supabase Cloud", fontSize = 11.5.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                onSaveCredentials(urlInput.trim(), keyInput.trim())
                                scope.launch {
                                    snackbarHostState.showSnackbar("Pengaturan cloud berhasil disimpan!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_save_supabase")
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onTestConnection,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_test_supabase")
                        ) {
                            if (isTestingConnection) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isTestingConnection) "Menguji..." else "Tes Koneksi", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (currentUrl.isNotBlank() || currentAnonKey.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                onClearCredentials()
                                urlInput = ""
                                keyInput = ""
                                scope.launch {
                                    snackbarHostState.showSnackbar("Kredensial kustom dihapus, kembali ke default.")
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hapus Kredensial")
                        }
                    }
                }
            }

            // Card Pengaturan Versi & Force Update
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Kontrol Versi & Force Update",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Text(
                                text = "Versi aplikasi saat ini: v$currentAppVersionName (Kode $currentAppVersionCode)",
                                fontSize = 11.5.sp,
                                color = NeutralMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Atur batas versi minimal pengguna. Jika versi di HP pengguna lebih kecil dari min_code, aplikasi akan terkunci otomatis dan mewajibkan update via Google Play Store.",
                        fontSize = 11.5.sp,
                        color = NeutralDark,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Input min_code
                    Text(
                        text = "Kode Versi Minimal Wajib (min_code):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = minCodeInput,
                        onValueChange = { minCodeInput = it.filter { char -> char.isDigit() } },
                        placeholder = { Text("Contoh: 2") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input latest_code
                    Text(
                        text = "Kode Versi Terbaru (latest_code):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = latestCodeInput,
                        onValueChange = { latestCodeInput = it.filter { char -> char.isDigit() } },
                        placeholder = { Text("Contoh: 2") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input latest_name
                    Text(
                        text = "Label Versi Terbaru (latest_name):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = latestNameInput,
                        onValueChange = { latestNameInput = it },
                        placeholder = { Text("Contoh: 1.1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val minVal = minCodeInput.toIntOrNull() ?: 1
                            val latestVal = latestCodeInput.toIntOrNull() ?: minVal
                            val nameVal = latestNameInput.ifBlank { "1.0" }
                            onUpdateRemoteVersion?.invoke(minVal, latestVal, nameVal)
                            scope.launch {
                                snackbarHostState.showSnackbar("Force Update berhasil disinkronkan ke Cloud (min_code: $minVal)!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Terapkan Force Update ke Cloud", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SQL Schema Helper Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Struktur Sinkronisasi Data",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )

                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(sqlSchema))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Skrip struktur data berhasil disalin!")
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Salin Skrip", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skrip inisialisasi struktur data cloud untuk sinkronisasi pesan & profil Lovy Chat:",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF263238))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = sqlSchema,
                            color = Color(0xFF80CBC4),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // SQL Cronjob & Auto-Cleanup Helper Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cronjob & Pembersihan Otomatis",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Text(
                                text = "Hapus akun inaktif > 15 hari, pesan usang, dan data yatim",
                                fontSize = 11.5.sp,
                                color = NeutralMedium
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(sqlCronCleanup))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Skrip cronjob pembersihan berhasil disalin!")
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Salin Skrip", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Jalankan skrip ini di SQL Editor dashboard Supabase. Skrip ini memasang ekstensi pg_cron, foreign key CASCADE, dan fungsi otomatisasi harian (pukul 03:00 UTC) untuk menghapus akun tidak aktif > 15 hari dan membersihkan riwayat obrolan usang.",
                        fontSize = 12.sp,
                        color = NeutralMedium,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF263238))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = sqlCronCleanup,
                            color = Color(0xFFFFCC80),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
