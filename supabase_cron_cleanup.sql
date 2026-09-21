-- ============================================================================
-- LOVY CHAT: SKRIP SQL CRONJOB & PEMBERSIHAN OTOMATIS (VERSI AMAN)
-- Jalankan skrip ini di: Supabase Dashboard -> SQL Editor -> New Query -> Run
-- ============================================================================

-- 1. AKTIFKAN EKSTENSI PG_CRON
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- 2. BERSIHKAN DATA YATIM (ORPHAN) TERLEBIH DAHULU AGAR TIDAK ADA ERROR CONSTRAINT
DELETE FROM nearby_users WHERE id NOT IN (SELECT id FROM app_accounts);
DELETE FROM moments WHERE author_id NOT IN (SELECT id FROM app_accounts);
DELETE FROM ocean_bottles WHERE sender_id NOT IN (SELECT id FROM app_accounts);
DELETE FROM chat_messages WHERE sender_id NOT IN (SELECT id FROM app_accounts);

-- 3. SETUP ON DELETE CASCADE
DO $$
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
END $$;

-- 4. FUNGSI PEMBERSIHAN OTOMATIS (FUNGSI CRON)
CREATE OR REPLACE FUNCTION purge_lovy_inactive_and_deleted_data()
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
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

    -- [D] Bersihkan data yatim (jika ada data lama yang tersisa)
    DELETE FROM nearby_users WHERE id NOT IN (SELECT id FROM app_accounts);
END;
$$;

-- 5. JADWAL CRONJOB (SETIAP HARI PUKUL 03:00 UTC)
SELECT cron.unschedule('daily-purge-lovy-inactive-data') 
WHERE EXISTS (
    SELECT 1 FROM cron.job WHERE jobname = 'daily-purge-lovy-inactive-data'
);

SELECT cron.schedule(
    'daily-purge-lovy-inactive-data',
    '0 3 * * *',
    $$SELECT purge_lovy_inactive_and_deleted_data()$$
);
