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
    modifier: Modifier = Modifier
) {
    var urlInput by remember(currentUrl) { mutableStateOf(currentUrl) }
    var keyInput by remember(currentAnonKey) { mutableStateOf(currentAnonKey) }
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sqlSchema = """
-- Lovy Chat Supabase Schema
-- Jalankan di SQL Editor di dashboard Supabase Anda:

create table if not exists nearby_users (
    id text primary key,
    name text not null,
    gender text not null,
    distance_meters int default 100,
    bio text,
    avatar_hex bigint default 3046706,
    is_online boolean default true
);

create table if not exists ocean_bottles (
    id text primary key,
    sender_id text not null,
    sender_name text not null,
    sender_gender text not null,
    content text not null,
    created_at bigint not null,
    location_hint text default 'Lautan Nusantara',
    avatar_hex bigint default 33679
);

create table if not exists chat_messages (
    id text primary key,
    conversation_id text not null,
    sender_id text not null,
    text text not null,
    created_at bigint not null
);

create table if not exists moments (
    id text primary key,
    author_id text not null,
    author_name text not null,
    content text not null,
    likes_count int default 0,
    comments_count int default 0,
    created_at bigint not null,
    author_avatar_hex bigint default 4222123520
);

-- Buka policy Read & Insert untuk public (anon)
alter table nearby_users enable row level security;
create policy "Allow anon read nearby" on nearby_users for select using (true);
create policy "Allow anon insert nearby" on nearby_users for insert with check (true);

alter table ocean_bottles enable row level security;
create policy "Allow anon all bottles" on ocean_bottles for all using (true) with check (true);

alter table chat_messages enable row level security;
create policy "Allow anon all messages" on chat_messages for all using (true) with check (true);

alter table moments enable row level security;
create policy "Allow anon all moments" on moments for all using (true) with check (true);
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Koneksi Supabase",
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
                        Text(
                            text = if (isConnected) "Supabase Terhubung Aktif" else "Supabase Belum Terhubung",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = connectionStatusMessage ?: if (isConnected) "Database cloud siap digunakan untuk pesan & botol lautan." else "Aplikasi saat ini menggunakan database cadangan lokal.",
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
                        text = "Project URL Supabase",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        placeholder = { Text("https://xxxxxxxxxxxxxxxxxxxx.supabase.co", fontSize = 13.sp) },
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
                        text = "Anon (Public) Key",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        placeholder = { Text("eyJh...... (Anon Public Key)", fontSize = 13.sp) },
                        minLines = 2,
                        maxLines = 3,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = EmeraldGreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_supabase_key")
                    )

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
                                    snackbarHostState.showSnackbar("Kredensial Supabase berhasil disimpan!")
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
                            text = "Skrip SQL Tabel Supabase",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )

                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(sqlSchema))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Skrip SQL berhasil disalin ke clipboard!")
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Salin SQL", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Jalankan skrip ini sekali saja di dashboard Supabase (SQL Editor) untuk menyiapkan tabel Lovy Chat:",
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
        }
    }
}
