package com.ead.project.dreamer.data.database.model.server

import com.ead.project.dreamer.data.database.model.Player
import com.ead.project.dreamer.data.database.model.Server
import com.ead.project.dreamer.data.utils.receiver.DreamerRequest
import com.ead.project.dreamer.data.database.model.VideoModel
import org.jsoup.Jsoup

class Streamtape(embeddedUrl:String) : Server(embeddedUrl) {

    override fun onPreExtract() {
        super.onPreExtract()
        player = Player.Streamtape
    }

    override fun onExtract() {
        super.onExtract()
        try {
            val source = Jsoup.connect(url)
                .userAgent(DreamerRequest.userAgent())
                .followRedirects(true)
                .get()

            val rawReference = source.select("div#ideoolink").text()
            val token = source.select("script")[7].html().lines()[2]
                .substringAfter("&token=")
                .substringBefore("').substring(2).substring(1);")

            val reference = rawReference.removeRange(0,1)
                .replaceAfter("&token=",token)
                .replace("streamtape.com","stape.fun")

            url = "https://$reference"

            videoList.add(VideoModel("Default",url))
            breakOperation()
        } catch (e: Exception) { e.printStackTrace() }
    }
}