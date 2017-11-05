package net.maiatoday.hellokeystore

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64

/**
 * Created by maia on 2017/10/28.
 */
class Prefs(context: Context) {
    val PREFS_FILENAME = "net.maiatoday.hellokeystore.prefs"
    val SECRET_MESSAGE_ENCRYPTED = "secret_message"
    val KEY_ALIAS = "key_alias"
    val IV = "iv"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var secretMessageEncrypted: ByteArray
        get() = prefs.getString(SECRET_MESSAGE_ENCRYPTED, "").toByteArray(Charsets.ISO_8859_1)
        set(value) = prefs.edit().putString(SECRET_MESSAGE_ENCRYPTED, String(value, Charsets.ISO_8859_1)).apply()

    var keyAlias: String
        get() = prefs.getString(KEY_ALIAS, "HelloKeyStore")
        set(value) = prefs.edit().putString(KEY_ALIAS, value).apply()


    var iv: ByteArray
        get() = prefs.getString(IV, "").toByteArray(Charsets.ISO_8859_1)
        set(value) = prefs.edit().putString(IV, String(value, Charsets.ISO_8859_1)).apply()

    fun clear() {
        prefs.edit().clear().apply()
    }
}