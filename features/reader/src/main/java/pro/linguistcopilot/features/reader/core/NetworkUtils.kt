package pro.linguistcopilot.features.reader.core

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import cn.hutool.core.lang.Validator
import okhttp3.internal.publicsuffix.PublicSuffixDatabase
import splitties.systemservices.connectivityManager
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object NetworkUtils {

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    fun isAvailable(): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            val mWiFiNetworkInfo = connectivityManager.activeNetworkInfo
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.type == ConnectivityManager.TYPE_WIFI ||
                        mWiFiNetworkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                        mWiFiNetworkInfo.type == ConnectivityManager.TYPE_ETHERNET ||
                        mWiFiNetworkInfo.type == ConnectivityManager.TYPE_VPN
            }
        } else {
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val nc = connectivityManager.getNetworkCapabilities(network)
                if (nc != null) {
                    return nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                }
            }
        }
        return false
    }

    private val notNeedEncoding: BitSet by lazy {
        val bitSet = BitSet(256)
        for (i in 'a'.code..'z'.code) {
            bitSet.set(i)
        }
        for (i in 'A'.code..'Z'.code) {
            bitSet.set(i)
        }
        for (i in '0'.code..'9'.code) {
            bitSet.set(i)
        }
        for (char in "+-_.$:()!*@&#,[]") {
            bitSet.set(char.code)
        }
        return@lazy bitSet
    }

    fun hasUrlEncoded(str: String): Boolean {
        var needEncode = false
        var i = 0
        while (i < str.length) {
            val c = str[i]
            if (notNeedEncoding.get(c.code)) {
                i++
                continue
            }
            if (c == '%' && i + 2 < str.length) {
                val c1 = str[++i]
                val c2 = str[++i]
                if (isDigit16Char(c1) && isDigit16Char(c2)) {
                    i++
                    continue
                }
            }
            needEncode = true
            break
        }
        return !needEncode
    }

    private fun isDigit16Char(c: Char): Boolean {
        return c in '0'..'9' || c in 'A'..'F' || c in 'a'..'f'
    }

    fun getAbsoluteURL(baseURL: String?, relativePath: String): String {
        if (baseURL.isNullOrEmpty()) return relativePath.trim()
        var absoluteUrl: URL? = null
        try {
            absoluteUrl = URL(baseURL.substringBefore(","))
        } catch (e: Exception) {
        }
        return getAbsoluteURL(absoluteUrl, relativePath)
    }

    fun getAbsoluteURL(baseURL: URL?, relativePath: String): String {
        val relativePathTrim = relativePath.trim()
        if (baseURL == null) return relativePathTrim
        if (relativePathTrim.isAbsUrl()) return relativePathTrim
        if (relativePathTrim.isDataUrl()) return relativePathTrim
        if (relativePathTrim.startsWith("javascript")) return ""
        var relativeUrl = relativePathTrim
        try {
            val parseUrl = URL(baseURL, relativePath)
            relativeUrl = parseUrl.toString()
            return relativeUrl
        } catch (e: Exception) {
        }
        return relativeUrl
    }

    fun getBaseUrl(url: String?): String? {
        url ?: return null
        if (url.startsWith("http://", true)
            || url.startsWith("https://", true)
        ) {
            val index = url.indexOf("/", 9)
            return if (index == -1) {
                url
            } else url.substring(0, index)
        }
        return null
    }

    fun getSubDomain(url: String): String {
        val baseUrl = getBaseUrl(url) ?: return url
        return kotlin.runCatching {
            val mURL = URL(baseUrl)
            val host: String = mURL.host
            if (isIPAddress(host)) return host
            PublicSuffixDatabase.get().getEffectiveTldPlusOne(host) ?: host
        }.getOrDefault(baseUrl)
    }

    fun getLocalIPAddress(): InetAddress? {
        var enumeration: Enumeration<NetworkInterface>? = null
        try {
            enumeration = NetworkInterface.getNetworkInterfaces()
        } catch (e: SocketException) {
        }

        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                val nif = enumeration.nextElement()
                val addresses = nif.inetAddresses
                if (addresses != null) {
                    while (addresses.hasMoreElements()) {
                        val address = addresses.nextElement()
                        if (!address.isLoopbackAddress && isIPv4Address(address.hostAddress)) {
                            return address
                        }
                    }
                }
            }
        }
        return null
    }

    fun isIPv4Address(input: String?): Boolean {
        return input != null && Validator.isIpv4(input)
    }

    fun isIPv6Address(input: String?): Boolean {
        return input != null && Validator.isIpv6(input)
    }

    fun isIPAddress(input: String?): Boolean {
        return isIPv4Address(input) || isIPv6Address(input)
    }

}

fun String?.isAbsUrl() =
    this?.let {
        it.startsWith("http://", true) || it.startsWith("https://", true)
    } ?: false

fun String?.isDataUrl() =
    this?.let {
        dataUriRegex.matches(it)
    } ?: false

val dataUriRegex = Regex("data:.*?;base64,(.*)")
