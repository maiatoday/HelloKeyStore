package net.maiatoday.keystorehelper

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal
import java.security.Security
import kotlin.collections.ArrayList


// https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3

private val AndroidKeyStore = "AndroidKeyStore"
private val RSA_MODE = "RSA/ECB/PKCS1Padding"
private val AES_MODE = "AES/GCM/NoPadding"

fun generateKeyPair(context: Context, keyAlias: String) {
    val keyStore = accessKeyStore()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        // Generate the RSA key pairs
        if (!keyStore.containsAlias(keyAlias)) {
            // Generate a key pair for encryption
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 30)
            val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(keyAlias)
                    .setSubject(X500Principal("CN=" + keyAlias))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
            val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
            kpg.initialize(spec)
            kpg.generateKeyPair()
        }
    } else {
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
            keyGenerator.init(
                    KeyGenParameterSpec.Builder(keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            //     .setUserAuthenticationRequired(true)
                            .build())
            keyGenerator.generateKey()
        }
    }
}

fun deleteKeyPair(keyAlias: String) {
    val keyStore = accessKeyStore()
    if (keyStore.containsAlias(keyAlias)) {
        keyStore.deleteEntry(keyAlias)
    }
}


fun listAliases(): Enumeration<String>? {
    val keyStore = accessKeyStore()
    return keyStore.aliases()
}


/**
 * Created by maia on 2017/10/28.
 */
@Throws(Exception::class)
fun encrypt(clearBytes: ByteArray, keyAlias: String): EncryptedCombo {
    if (!keyAlias.isEmpty()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val keyEntry = accessPrivateKeyEntry(keyAlias)
            if (keyEntry != null) {
                // Encrypt the text
                val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
                inputCipher.init(Cipher.ENCRYPT_MODE, keyEntry.certificate?.publicKey)
                val outputStream = ByteArrayOutputStream()
                val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
                cipherOutputStream.write(clearBytes)
                cipherOutputStream.close()

                return EncryptedCombo(outputStream.toByteArray(), inputCipher.iv ?: kotlin.ByteArray(0))
            }
        } else {
            val keyEntry = accessSecretKeyEntry(keyAlias)
            if (keyEntry != null) {
                val c: Cipher = Cipher.getInstance(AES_MODE)
                val iv = generateIV()
                c.init(Cipher.ENCRYPT_MODE, keyEntry.secretKey, GCMParameterSpec(128, iv))
                val encodedBytes = c.doFinal(clearBytes)
                return EncryptedCombo(encodedBytes, iv)
            }
        }

    }
    return EncryptedCombo(kotlin.ByteArray(0), kotlin.ByteArray(0))
}


@Throws(Exception::class)
fun decrypt(encryptedCombo: EncryptedCombo, keyAlias: String): ByteArray {
    if (!keyAlias.isEmpty()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val keyEntry = accessPrivateKeyEntry(keyAlias)
            if (keyEntry != null) {
                val output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
                output.init(Cipher.DECRYPT_MODE, keyEntry.privateKey)
                val cipherInputStream = CipherInputStream(
                        ByteArrayInputStream(encryptedCombo.cipherBytes), output)

                val values = cipherInputStream.readBytes()
                cipherInputStream.close()
                return values
            }

        } else {
            val keyEntry = accessSecretKeyEntry(keyAlias)
            if (keyEntry != null) {
                val c = Cipher.getInstance(AES_MODE)
                c.init(Cipher.DECRYPT_MODE, keyEntry.secretKey, GCMParameterSpec(128, encryptedCombo.iv))
                return c.doFinal(encryptedCombo.cipherBytes)
            }

        }

    }
    return kotlin.ByteArray(0)
}

fun accessKeyStore(): KeyStore {
    val keyStore = KeyStore.getInstance(AndroidKeyStore)
    keyStore.load(null)
    return keyStore
}

fun accessPrivateKeyEntry(keyAlias: String): KeyStore.PrivateKeyEntry? {
    val keyStore = accessKeyStore()
    val entry = keyStore.getEntry(keyAlias, null)
    return entry as? KeyStore.PrivateKeyEntry
}

fun accessSecretKeyEntry(keyAlias: String): KeyStore.SecretKeyEntry? {
    val keyStore = accessKeyStore()
    val entry = keyStore.getEntry(keyAlias, null)
    return entry as? KeyStore.SecretKeyEntry
}

fun accessAlias(keyAlias: String): KeyStore.Entry? {
    val keyStore = accessKeyStore()
    return keyStore.getEntry(keyAlias, null)
}

fun generateIV(): ByteArray {
    val r = SecureRandom()
    val ivBytes = ByteArray(12)
    r.nextBytes(ivBytes)
    return ivBytes
}

data class EncryptedCombo(val cipherBytes: ByteArray, val iv: ByteArray)

fun listSupportedAlgorithms(): List<String> {
    val providerServiceList = ArrayList<String>()
    val providers = Security.getProviders()
    for (provider in providers) {
        val services = provider.services
        for (service in services) {
            providerServiceList.add("provider: $provider.name --- algorithm: $service.algorithm")
        }
    }
    return providerServiceList
}
