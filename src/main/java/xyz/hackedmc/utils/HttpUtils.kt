/*
 * NekoCat Hacked Client
 * Powered By HackedMC
 * https://HackedMC.xyz/NekoCat
 */
package xyz.hackedmc.utils

import org.apache.commons.io.FileUtils
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * NekoCat Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
object HttpUtils {

    private const val DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"

    init {
        HttpURLConnection.setFollowRedirects(true)
    }

    private fun make(url: String, method: String,
                     agent: String = DEFAULT_AGENT): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 10000
        httpConnection.readTimeout = 10000

        httpConnection.setRequestProperty("User-Agent", agent)

        httpConnection.instanceFollowRedirects = true
        httpConnection.doOutput = true

        return httpConnection
    }

    @Throws(IOException::class)
    fun request(url: String, method: String,
                agent: String = DEFAULT_AGENT): String {
        val connection = make(url, method, agent)

        return connection.inputStream.reader().readText()
    }

    @Throws(IOException::class)
    fun requestStream(url: String, method: String,
                      agent: String = DEFAULT_AGENT): InputStream? {
        val connection = make(url, method, agent)

        return connection.inputStream
    }

    @Throws(IOException::class)
    @JvmStatic
    fun get(url: String) = request(url, "GET")

    @Throws(IOException::class)
    @JvmStatic
    fun download(url: String, file: File) = FileUtils.copyInputStreamToFile(make(url, "GET").inputStream, file)

    @Throws(IOException::class)
    fun HtmlContent(str: String?): String {
        val url = URL(str)
        val httpConn = url.openConnection() as HttpURLConnection
        val input = InputStreamReader(
            httpConn
                .inputStream, "utf-8"
        )
        val bufReader = BufferedReader(input)
        var line: String? = ""
        val contentBuf = StringBuilder()
        while (bufReader.readLine().also { line = it } != null) {
            contentBuf.append(line)
        }
        return contentBuf.toString()
    }
}