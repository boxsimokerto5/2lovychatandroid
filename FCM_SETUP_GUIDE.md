# Panduan Notifikasi FCM Lovy Chat via Supabase Edge Function

File kredensial Service Account Firebase:
- Project ID: `lovy-chat`
- Client Email: `firebase-adminsdk-fbsvc@lovy-chat.iam.gserviceaccount.com`

---

## Langkah 1: Buat Edge Function di Supabase

1. Di komputer / terminal Anda dengan Supabase CLI:
```bash
supabase functions new notify-fcm
```
Atau jika membuat langsung via Supabase Dashboard / GitHub:

2. Masukkan kode berikut ke dalam `supabase/functions/notify-fcm/index.ts`:

```typescript
import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from "https://esm.sh/@supabase/supabase-js@2"
import { create } from "https://deno.land/x/djwt@v2.8/mod.ts"

// Service Account Credentials dari Firebase
const SERVICE_ACCOUNT = {
  type: "service_account",
  project_id: "lovy-chat",
  private_key_id: "a24eba43904675e8ada3581dee226680c5b3c8d2",
  private_key: `-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDYyPUlcElJ6+aF\n8Rv3Xtoq1oDk1JERBXPLzZjkFW4w2DALleASgk2+wMxdIItnGxj+zK/r8zu+9MIP\nJO1/ewQXeiZt/agEZyE6XtI0xq+XT0RD3xp+Fiu+Pp5FDUSl5/QeA3oAsNsFwJWq\nab0hJTsRtmGrD4NgMVjn1hjrFEqqazHtMHGC+3lYbbEcLK8OYeAwrhsDw2nP5lXR\naigqGvicSrW0cdSTG8cNZ3kfHAs5UbLq2J9RFfF6jANoJ0BYCpV1t2XBlwerUYfX\np6kq3sB1yejfP2MANP+jN+5EEhwLfCQPk3Bp9/PjQJ6POzcrS7XBUxrsOeCv5u6R\neDakJ+4DAgMBAAECggEAVbNoOqM/zfHs4bG2SR5d6EaBkTU5nj2GECFY2n5gX/3v\nTH01JowN2SXWBNDrW1j6kixwNbqkOXMATsVeAKIstW1MSPpY1FjC6ZT3y/ZqD0q+\nZYiLLCJuq5iMsCa1J1NiDgV401IXXBhM8qA3rSVPS0rLJLmOydXoXEqm8jRE6kaY\n99msfgMKhPCr1re5MZ5NRl2o16S87k3eiB3P+5RjDSdaIy5VOHI2lis2T3SG/grw\nSIjq4qNKwIU1Z+ArA5tuPpF4vIKSVJX3HGXbVJ1lzKwJAOg2RRzuawCy5af7xzn+\nRq2P1PoVVP2GaUbCjrY7hGO3DMVz1Zhp+paVV659lQKBgQD1+nmXDYgv9BGm2KBz\n7Ie0KLy9c9WjlKvHgCwhUOkjX2fPdJg3EbQQuxX4vTbpgZ3vhWSi+6ILG+49oxEg\nV9Hl3y5z8Xsx0+zm+0kx3j68STOJQFSSGVZuTOV9apBXZxicR7cz7Jns++QHcM6J\n2WE23ztNKQgZe8t7mG+QJTG+7wKBgQDhnf+tZmGwPict58w9kcdKZLmy/+YPwUKz\nDgYdUtrYTO8UaGgYIUY0pTswyPHg7XrWBakSXiLlqjsA5sUQHpdMF5AZrX4oNQ0s\n3rYLGTegCkhOuZaz8AgvlflLzC8EGa6+dcauUpLm1aB34x9YeQWXgVdyiXXNZf5a\nnwhiX8yCLQKBgQCBkhFeilhEulJaCx5qMhxBwHu7aDsPUg8ypceZPR+x7F2ooAhW\nadLqRUKLplA4rHhfZWrfl6GCLJJQkYdB/ECqz+eOV5PHaZUPDys9Q47Ua9Lj56kk\n1If8zjDM4cdq4vnJyNUuWuGyPfWeHcQORR26Y9i/CnAjzFwhnOiXi6AqrQKBgQCG\nNoFEW6U9PJv9+OhMsA3HuYembggj0ymkbnFAvGsnRQzsLPfPcuvaoXhGmyMYVO0/\np/pdzNYhnVSgQqdz4V7LizDTtZtYu8ZsrMfKbmPitnsxKcH2pJGaDTNR3dMSM1z6\nPAHG7aQp4WcmKlwZO1USPYURw6fqWgQnUHeiXa7AuQKBgQCB+fzqApGwxgD+oTvA\nXXXLNl3g2oj8+j2CKhc/85FWSu9B1EGZN22uOlTkw3zzD7YlZVXqMxqyCOdUN7Bc\nq6JCzdaqpwDGvd9tp8SLviL7/qUaipKj+cJf8PrXt61Is5F5V/ZHOFawXuZVy9x5\nJ5g5B3V/4Yik1OQxePGg20JR2w==\n-----END PRIVATE KEY-----\n",
  client_email: "firebase-adminsdk-fbsvc@lovy-chat.iam.gserviceaccount.com"
}

// Fungsi generate Google OAuth2 Access Token untuk FCM v1
async function getAccessToken() {
  const pemHeader = "-----BEGIN PRIVATE KEY-----"
  const pemFooter = "-----END PRIVATE KEY-----"
  const pemContents = SERVICE_ACCOUNT.private_key
    .replace(pemHeader, "")
    .replace(pemFooter, "")
    .replace(/\s/g, "")
  const binaryDer = Uint8Array.from(atob(pemContents), c => c.charCodeAt(0))

  const key = await crypto.subtle.importKey(
    "pkcs8",
    binaryDer.buffer,
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"]
  )

  const now = Math.floor(Date.now() / 1000)
  const jwt = await create(
    { alg: "RS256", typ: "JWT" },
    {
      iss: SERVICE_ACCOUNT.client_email,
      sub: SERVICE_ACCOUNT.client_email,
      aud: "https://oauth2.googleapis.com/token",
      iat: now,
      exp: now + 3600,
      scope: "https://www.googleapis.com/auth/firebase.messaging"
    },
    key
  )

  const tokenRes = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({
      grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
      assertion: jwt
    })
  })

  const tokenData = await tokenRes.json()
  return tokenData.access_token
}

serve(async (req) => {
  try {
    const payload = await req.json()
    const record = payload.record // Baris baru dari tabel chat_messages

    if (!record || !record.receiver_id || !record.text) {
      return new Response(JSON.stringify({ status: "skipped", reason: "no record/receiver" }), { status: 200 })
    }

    const supabase = createClient(
      Deno.env.get("SUPABASE_URL")!,
      Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!
    )

    // Cari FCM Token penerima di nearby_users atau app_accounts
    let { data: receiver } = await supabase
      .from("nearby_users")
      .select("fcm_token, name")
      .eq("id", record.receiver_id)
      .single()

    if (!receiver?.fcm_token) {
      const { data: acc } = await supabase
        .from("app_accounts")
        .select("fcm_token, display_name")
        .eq("id", record.receiver_id)
        .single()
      if (acc?.fcm_token) {
        receiver = { fcm_token: acc.fcm_token, name: acc.display_name }
      }
    }

    if (!receiver?.fcm_token) {
      return new Response(JSON.stringify({ status: "no_fcm_token" }), { status: 200 })
    }

    // Ambil nama pengirim
    const { data: sender } = await supabase
      .from("nearby_users")
      .select("name")
      .eq("id", record.sender_id)
      .single()
    const senderName = sender?.name || "Teman Lovy"

    // Dapatkan Google Access Token
    const accessToken = await getAccessToken()

    // Kirim pesan lewat Google FCM v1 API
    const fcmRes = await fetch(`https://fcm.googleapis.com/v1/projects/${SERVICE_ACCOUNT.project_id}/messages:send`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${accessToken}`
      },
      body: JSON.stringify({
        message: {
          token: receiver.fcm_token,
          data: {
            title: senderName,
            body: record.text,
            conversationId: record.conversation_id || "",
            senderName: senderName
          },
          android: {
            priority: "HIGH",
            notification: {
              title: senderName,
              body: record.text,
              sound: "default",
              channel_id: "lovy_messages_channel"
            }
          }
        }
      })
    })

    const result = await fcmRes.json()
    return new Response(JSON.stringify({ status: "success", fcm: result }), { status: 200 })
  } catch (err) {
    return new Response(JSON.stringify({ error: err.message }), { status: 500 })
  }
})
```

---

## Langkah 2: Buat Database Webhook di Supabase Dashboard

1. Buka dashboard Supabase > Masuk menu **Database** > **Webhooks** (atau **Integrations** > **Webhooks**).
2. Klik **Create a new webhook**.
3. Isi kolom:
   - **Name:** `notify_on_new_message`
   - **Table:** `chat_messages`
   - **Events:** Centang **Insert** saja
   - **Type:** HTTP Request
   - **Method:** POST
   - **URL:** URL Edge function Anda (contoh: `https://<project-ref>.supabase.co/functions/v1/notify-fcm`)
   - **HTTP Headers:**
     - `Content-Type`: `application/json`
     - `Authorization`: `Bearer <SUPABASE_ANON_KEY>`
4. Klik **Save Webhook**.

Selesai! Setiap ada pesan obrolan baru masuk, Supabase akan otomatis mengeksekusi FCM v1 dan HP penerima yang sedang *sleep* akan langsung bangun dan bergetar menampilkan nama pengirim serta isi pesannya.
