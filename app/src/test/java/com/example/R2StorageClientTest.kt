package com.example

import com.example.data.storage.R2StorageClient
import org.junit.Assert.*
import org.junit.Test

class R2StorageClientTest {

    @Test
    fun r2Client_defaultCredentials_areConfigured() {
        // Verify that default credentials and endpoints are present and valid
        val accountId = R2StorageClient.getAccountId()
        val accessKey = R2StorageClient.getAccessKeyId()
        val secretKey = R2StorageClient.getSecretAccessKey()
        val bucketName = R2StorageClient.getBucketName()

        assertNotNull(accountId)
        assertTrue(accountId.isNotEmpty())
        assertNotNull(accessKey)
        assertTrue(accessKey.isNotEmpty())
        assertNotNull(secretKey)
        assertTrue(secretKey.isNotEmpty())
        assertNotNull(bucketName)
        assertTrue(bucketName.isNotEmpty())
        assertTrue(R2StorageClient.isConfigured())
    }

    @Test
    fun r2Client_publicUrlResolution_works() {
        val objectKey = "avatars/user123_456.jpg"
        val publicUrl = R2StorageClient.resolvePublicUrl(objectKey)
        assertNotNull(publicUrl)
        assertTrue(publicUrl.contains(objectKey))
    }

    @Test
    fun r2Client_presignedUrlGeneration_containsRequiredParams() {
        val objectKey = "chats/conv_1/photo_123.jpg"
        val presignedUrl = R2StorageClient.generatePresignedGetUrl(objectKey, expiresInSeconds = 3600)
        assertNotNull(presignedUrl)
        assertTrue(presignedUrl.startsWith("https://"))
        assertTrue(presignedUrl.contains("X-Amz-Algorithm=AWS4-HMAC-SHA256"))
        assertTrue(presignedUrl.contains("X-Amz-Signature="))
        assertTrue(presignedUrl.contains("X-Amz-SignedHeaders=host"))
    }
}
