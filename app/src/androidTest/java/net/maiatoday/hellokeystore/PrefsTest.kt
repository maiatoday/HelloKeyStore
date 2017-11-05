package net.maiatoday.hellokeystore

import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.SecureRandom
import java.util.*

/**
 * Created by maia on 2017/11/05.
 */
class PrefsTest {
    lateinit var prefs: Prefs

    @Before
    fun setUp() {
        prefs = Prefs(InstrumentationRegistry.getTargetContext())
    }

    @After
    fun tearDown() {
        prefs.clear()
    }

    @Test
    fun ivTest() {
        val testIv1 = kotlin.ByteArray(0)
        prefs.iv = testIv1
        assert(Arrays.equals(testIv1, prefs.iv))

        val r = SecureRandom()
        val testIv2 = ByteArray(12)
        r.nextBytes(testIv2)
        prefs.iv = testIv2
        assert(Arrays.equals(testIv2, prefs.iv))

    }

    @Test
    fun encryptedMessageTest() {
        val test1 = kotlin.ByteArray(0)
        prefs.secretMessageEncrypted = test1
        assert(Arrays.equals(test1, prefs.secretMessageEncrypted))

        val r = SecureRandom()
        val test2 = ByteArray(256)
        r.nextBytes(test2)
        prefs.secretMessageEncrypted = test2
        assert(Arrays.equals(test2, prefs.secretMessageEncrypted))

    }



}