package com.example.balatroapp.ui.list

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class ListRepository @Inject constructor() {
    suspend fun getCards(): List<Card> {
//        return (0..100).map {
//            Card(
//                name = "1",
//                imageUrl = "1",
//                description = "desc",
//            )
//        }
        return getCardsInternal()
    }


    suspend fun getCardsInternal(): List<Card> {
        val googleRssUrl = "https://balatrogame.fandom.com/wiki/Jokers"
        return getNewsFromUrl(googleRssUrl)
    }

    // 기사 url 로부터 News 를 추출
    private suspend fun getNewsFromUrl(newsUrl: String): List<Card> {
        return withContext(Dispatchers.IO) {
            try {
                setSSL()
                val doc = Jsoup.connect(newsUrl).get()// 오래걸림 (0.4초 이상)
                val rows = doc.select("table.sortable.fandom-table > tbody").select("tr")
                rows.subList(1, rows.size).mapNotNull {
                    val number = it.select("td")[0].text().toInt()
                    val url = getImageUrl(it.select("td")[1].select("img").select("img"))
                    val name = it.select("td")[1].text()
                    val cost = it.select("td")[2].text()
                    val rarity = it.select("td")[3].text()
                    val requirement = it.select("td")[5].text()
                    val effect = it.select("td")[5].text()

                    Log.d("kjh", "iomage $name $url")
                    Card(
                        name = name,
                        imageUrl = url,
                        description = requirement
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                listOf()
            }
        }
    }

    private fun getImageUrl(element: Elements): String {
        return element[0].attr("src").takeIf { it.startsWith("http") }
            ?: element.select("img").attr("data-src") ?: ""
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun setSSL() {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    // TODO Auto-generated method stub
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // TODO Auto-generated method stub
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // TODO Auto-generated method stub
                }
            }
        )
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    }

}