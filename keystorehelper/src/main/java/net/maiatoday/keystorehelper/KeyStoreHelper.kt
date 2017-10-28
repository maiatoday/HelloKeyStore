package net.maiatoday.keystorehelper

import android.content.Context
import android.os.Build
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import android.security.keystore.KeyProperties
import android.security.KeyPairGeneratorSpec
import java.math.BigInteger
import java.security.KeyPairGenerator
import javax.security.auth.x500.X500Principal
import android.security.keystore.KeyGenParameterSpec
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

// https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3

private val AndroidKeyStore = "AndroidKeyStore"
private val AES_MODE = "AES/GCM/NoPadding"

fun generateKeyPair(context: Context, keyAlias: String) {
//    val keyStore = accessKeyStore()
//    val keyExists = keyStore?.containsAlias(keyAlias)
//    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//        // Generate the RSA key pairs
//        if (!keyExists) {
//            // Generate a key pair for encryption
//            val start = Calendar.getInstance()
//            val end = Calendar.getInstance()
//            end.add(Calendar.YEAR, 30)
//            val spec = KeyPairGeneratorSpec.Builder(context)
//                    .setAlias(keyAlias)
//                    .setSubject(X500Principal("CN=" + keyAlias))
//                    .setSerialNumber(BigInteger.TEN)
//                    .setStartDate(start.time)
//                    .setEndDate(end.time)
//                    .build()
//            val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
//            kpg.initialize(spec)
//            kpg.generateKeyPair()
//        }
//    } else {
//        if (!keyStore?.containsAlias(keyAlias)) {
//            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
//            keyGenerator.init(
//                    KeyGenParameterSpec.Builder(keyAlias,
//                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//                            .setRandomizedEncryptionRequired(false)
//                            .build())
//            keyGenerator.generateKey()
//        }
//    }
}


/**
 * Created by maia on 2017/10/28.
 */
@Throws(Exception::class)
fun rsaEncrypt(secret: ByteArray, keyAlias: String): ByteArray {
//
//    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//        val privateKeyEntry = accessPrivateKey(keyAlias)
//        // Encrypt the text
//        val inputCipher = Cipher.getInstance(AES_MODE, "AndroidOpenSSL")
//        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey())
//
//        val outputStream = ByteArrayOutputStream()
//        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
//        cipherOutputStream.write(secret)
//        cipherOutputStream.close()
//
//        return outputStream.toByteArray()
//    } else {
//        val c = Cipher.getInstance(AES_MODE)
//        c.init(Cipher.ENCRYPT_MODE, accessPrivateKey(keyAlias), GCMParameterSpec(128, FIXED_IV.getBytes()))
//        val encodedBytes = c.doFinal(secret)
//        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
//    }
    val bytes = ByteArray(4)
    bytes[0] = 'x'.toByte()
    bytes[1] = 'o'.toByte()
    bytes[2] = 'x'.toByte()
    bytes[3] = 'o'.toByte()
    return bytes //TODO fix
}

@Throws(Exception::class)
fun rsaDecrypt(encrypted: ByteArray, keyAlias: String): ByteArray {
//
//    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//        val privateKeyEntry = accessPrivateKey(keyAlias)
//        val output = Cipher.getInstance(AES_MODE, "AndroidOpenSSL")
//        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey())
//        val cipherInputStream = CipherInputStream(
//                ByteArrayInputStream(encrypted), output)
//        val values: ArrayList<Byte> = ArrayList()
//        var nextByte: Int
//        while ((nextByte = cipherInputStream.read()) != -1) {
//            values.add(nextByte.toByte())
//        }
//
//        val bytes = ByteArray(values.size())
//        for (i in bytes.indices) {
//            bytes[i] = values.get(i).byteValue()
//        }
//        return bytes
//
//    } else {
//        val c = Cipher.getInstance(AES_MODE)
//        c.init(Cipher.DECRYPT_MODE, accessPrivateKey(keyAlias), GCMParameterSpec(128, FIXED_IV.getBytes()))
//        return c.doFinal(encrypted)
//    }
    val bytes = ByteArray(4)
    bytes[0] = 'a'.toByte()
    bytes[1] = 'h'.toByte()
    bytes[2] = 'a'.toByte()
    bytes[3] = 'i'.toByte()
    return bytes //TODO fix
}


private fun accessPrivateKey(keyAlias: String): KeyStore.PrivateKeyEntry {
    val keyStore = accessKeyStore()
    return keyStore?.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
}

private fun accessKeyStore(): KeyStore? {
    val keyStore = KeyStore.getInstance(AndroidKeyStore)
    keyStore.load(null)
    return keyStore
}
