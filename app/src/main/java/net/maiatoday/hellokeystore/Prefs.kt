package net.maiatoday.hellokeystore

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by maia on 2017/10/28.
 */
class Prefs (context: Context) {
    val PREFS_FILENAME = "net.maiatoday.hellokeystore.prefs"
    val SECRET_MESSAGE_ENCRYPTED = "secret_message"
    val KEY_ALIAS = "key_alias"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var secretMessageEncrypted: String
        get() = prefs.getString(SECRET_MESSAGE_ENCRYPTED, "")
        set(value) = prefs.edit().putString(SECRET_MESSAGE_ENCRYPTED, value).apply()

    var keyAlias: String
        get() = prefs.getString(KEY_ALIAS, "HelloKeyStore")
        set(value) = prefs.edit().putString(KEY_ALIAS, value).apply()
}