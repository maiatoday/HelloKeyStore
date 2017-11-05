package net.maiatoday.hellokeystore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.maiatoday.keystorehelper.EncryptedCombo
import net.maiatoday.keystorehelper.generateKeyPair
import net.maiatoday.keystorehelper.rsaDecrypt
import net.maiatoday.keystorehelper.rsaEncrypt

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        prefs = Prefs(this)
        keyAlias.setText(prefs.keyAlias)
//        val decryptedMessagexx = String(rsaDecrypt(EncryptedCombo(prefs.secretMessageEncrypted, prefs.iv), prefs.keyAlias))
        val decryptedMessage = "urk"
        secretMessageEncrypted.text = String(prefs.secretMessageEncrypted)
        secretMessagePlain.text = decryptedMessage
        secretMessage.setText(decryptedMessage)

        fab.setOnClickListener { view ->
            startActivity(Intent(this, KeysActivity::class.java))
        }
        buttonLock.setOnClickListener { view ->

            val keyAliasString = keyAlias.text.toString()
            val message = secretMessage.text.toString()
            if (!TextUtils.isEmpty(message) &&
                    !TextUtils.isEmpty(keyAliasString)) {
                // Generate a key pair if it doesn't exist in the KeyStore already
                generateKeyPair(this, keyAliasString)
                // Encrypt the message with the key accessing it with the alias
                val clearBytes:ByteArray = message.toByteArray()
                val encryptedCombo = rsaEncrypt(clearBytes, keyAliasString)
                // For this example store the encrypted message, the initialisation vector that was used and the key alias in the preferences
                prefs.secretMessageEncrypted = encryptedCombo.cipherBytes
                prefs.iv = encryptedCombo.iv
                prefs.keyAlias = keyAliasString
                // For visual feedback to the user show the encrypted message on the screen
                secretMessageEncrypted.text =  String(encryptedCombo.cipherBytes)
                val decryptedMessage = String(rsaDecrypt(encryptedCombo, prefs.keyAlias))
                secretMessagePlain.text = decryptedMessage
                val decryptedMessage2 = String(rsaDecrypt(EncryptedCombo(prefs.secretMessageEncrypted, prefs.iv), prefs.keyAlias))
                Log.d(TAG, "decrypted message from prefs = "+decryptedMessage2)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
