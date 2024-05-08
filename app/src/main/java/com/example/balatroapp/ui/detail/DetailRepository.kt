package com.example.balatroapp.ui.detail

import com.example.balatroapp.ui.list.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class DetailRepository @Inject constructor() {
    companion object {
        const val baseUrl = "https://balatrogame.fandom.com/wiki"
    }

    suspend fun getCard(link: String): CardDetail? {
        val url = baseUrl + link
        return getDetail(url)
    }

    // 기사 url 로부터 News 를 추출
    private suspend fun getDetail(newsUrl: String): CardDetail? {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(newsUrl).get()// 오래걸림 (0.4초 이상)
                val rows = doc.select("#mw-content-text > div.mw-parser-output > aside")

                val title = rows[0].getElementsByAttributeValue("data-source", "title").text()
                val imgUrl = rows[0].select("img")[0].attr("src")
                val effectDesc = rows[0].getElementsByAttributeValue("data-source", "effect").text()
                val rarityDesc = rows[0].getElementsByAttributeValue("data-source", "rarity").text()
                val buyPrice = rows[0].getElementsByAttributeValue("data-source", "buyprice").text()
                val sellPrice = rows[0].getElementsByAttributeValue("data-source", "sellprice").text()

                CardDetail(
                    title,
                    imgUrl,
                    effectDesc,
                    rarityDesc, buyPrice, sellPrice
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


}