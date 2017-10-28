package net.maiatoday.hellokeystore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.maiatoday.keystorehelper.generateKeyPair
import net.maiatoday.keystorehelper.rsaDecrypt
import net.maiatoday.keystorehelper.rsaEncrypt

class MainActivity : AppCompatActivity() {

    lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        prefs = Prefs(this)
        keyAlias.setText(prefs.keyAlias)
        secretMessageEncrypted.text = prefs.secretMessageEncrypted
        secretMessagePlain.text = rsaDecrypt(prefs.secretMessageEncrypted.toByteArray(), prefs.keyAlias).toString()

        fab.setOnClickListener { view ->
            startActivity(Intent(this, KeysActivity::class.java))
        }
        buttonLock.setOnClickListener { view ->

            val keyAlias = keyAlias.text.toString()
            val message = secretMessage.text.toString()
            if (!TextUtils.isEmpty(message) &&
                    !TextUtils.isEmpty(keyAlias)) {
                generateKeyPair(this, keyAlias)
                val encryptedMessage = rsaEncrypt(message.toByteArray(), keyAlias)
                prefs.secretMessageEncrypted = encryptedMessage.toString()
                secretMessageEncrypted.text = encryptedMessage.toString()

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
