package net.maiatoday.keystorehelper

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import java.util.*

/**
 * Created by maia on 2017/11/05.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class KeyStoreHelperKtTest {
    val keyAlias:String = "bleargh"

    @Before
    fun setUp() {
        generateKeyPair(InstrumentationRegistry.getTargetContext(), keyAlias)
    }

    @After
    fun tearDown() {
        deleteKeyPair(keyAlias)
    }

    @Test
    fun generateKeyPairTest() {
        //GIVEN an alias string
        val alias = "ttt"
        //WHEN we generate a key
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias)
        //THEN the key entry is accessible and not null
        val keyEntry1 = accessAlias(alias)
        assertNotNull(keyEntry1)

        //GIVEN an existing key entry
        //WHEN we generate the same alias
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias)

        //THEN the same key is returned when it is accessed.
        val keyEntry2 = accessAlias(alias)
        //TODO need to check that we didn't regenerate a keypair

        deleteKeyPair(alias)
    }


    @Test
    fun encryptTest() {
        generateKeyPair(InstrumentationRegistry.getTargetContext(), keyAlias)
        val clearText = "Testing123"
        val encryptedCombo = encrypt(clearText.toByteArray(), keyAlias)
        assert(encryptedCombo.cipherBytes.isNotEmpty())
        assert(encryptedCombo.iv.isNotEmpty())
        assert(encryptedCombo.cipherBytes!= clearText.toByteArray())
    }

    @Test
    fun decryptTest() {
        generateKeyPair(InstrumentationRegistry.getTargetContext(), keyAlias)
        val clearText = "Testing123Testing123Testing123"
        val encryptedCombo = encrypt(clearText.toByteArray(), keyAlias)
        val testClearText = decrypt(encryptedCombo, keyAlias)
        assertEquals(clearText, String(testClearText))
    }

    @Test
    fun generateIVTest() {
        val iv = generateIV()
        assert(iv.size != 0)
        val iv2 = generateIV()
        assertFalse(Arrays.equals(iv, iv2))
    }

    @Test
    fun deleteAliasTest() {
        val alias = "mopsy"
        //GIVEN an alias in the keystore
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias)
        val keyEntry1 = accessAlias(alias)
        assertNotNull(keyEntry1)

        //WHEN you delete it
        deleteKeyPair(alias)

        //THEN it is no longer accessible
        val keyEntry2 = accessAlias(alias)
        assertNull(keyEntry2)
    }

    @Test
    fun listAliasTest() {
        val alias1 = "flopsy"
        val alias2 = "mopsy"
        val alias3 = "cottontail"
        //GIVEN a key store with some aliases
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias1)
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias2)
        generateKeyPair(InstrumentationRegistry.getTargetContext(), alias3)
        //WHEN you obtain a list of aliases
        val aliasEnumeration = listAliases()
        //THEN the alias are in the list
        val list = aliasEnumeration?.toList() ?: listOf("")
        assert(list.contains(alias1))
        assert(list.contains(alias2))
        assert(list.contains(alias3))

        //WHEN the aliases are deleted and you obtain the list
        deleteKeyPair(alias1)
        deleteKeyPair(alias2)
        deleteKeyPair(alias3)
        val aliasEnumeration2 = listAliases()
        //THEN the aliases are no longer in the list
        val listDeleted = aliasEnumeration2?.toList() ?: listOf("")
        assert(!listDeleted.contains(alias1))
        assert(!listDeleted.contains(alias2))
        assert(!listDeleted.contains(alias3))
    }

}