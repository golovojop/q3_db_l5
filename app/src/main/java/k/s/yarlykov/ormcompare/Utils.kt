package k.s.yarlykov.ormcompare

import android.util.Log
import java.util.*

fun logIt(message: String, tag: String = "APP_TAG") {
    Log.e(tag, message)
}

fun String.rotate(step: Int): String {
    return map { ch -> ch }.apply {
        Collections.rotate(this, step)
    }.joinToString("")
}