package ru.wtfdev.kitty.utils

import java.security.SecureRandom

object StringUtils {
    private val AB = "0123456789abcdefghijklmnopqrstuvwxyz".toLowerCase()
    private val rnd = SecureRandom()

    fun randomString(len: Int): String? {
        val sb = StringBuilder(len)
        for (i in 0 until len) {
            sb.append(AB.get(rnd.nextInt(AB.length)))
        }
        return sb.toString().toLowerCase()
    }
}