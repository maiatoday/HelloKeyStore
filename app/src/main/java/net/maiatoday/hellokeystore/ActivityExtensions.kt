package net.maiatoday.hellokeystore

import android.app.Activity
import android.widget.Toast

/**
 * Created by maia on 2017/11/05.
 */

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}