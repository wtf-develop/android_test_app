package ru.wtfdev.kitty.utils

import java.security.SecureRandom
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    private val AB = "0123456789abcdefghijklmnopqrstuvwxyz".toLowerCase()
    private val rnd = SecureRandom()

    fun randomString(len: Int): String {
        val sb = StringBuilder(len)
        for (i in 0 until len) {
            sb.append(AB[rnd.nextInt(AB.length)])
        }
        return sb.toString().toLowerCase()
    }


    fun extractImgUrl(text: String?): String {
        if (text == null) return ""
        val containedUrls = mutableListOf<String>()
        val urlRegex = "((https?|ftp):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
        val pattern: Pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher: Matcher = pattern.matcher(text)

        while (urlMatcher.find()) {
            val founded = text.substring(urlMatcher.start(0), urlMatcher.end(0)).trim()
            val low = founded.toLowerCase()
            if (low.endsWith(".jpg")) return founded
            if (low.endsWith(".png")) return founded
            if (low.endsWith(".jpeg")) return founded
            containedUrls.add(founded)
        }

        if (containedUrls.size < 1) return ""
        return containedUrls[0]
    }
}