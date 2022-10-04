package com.ead.project.dreamer.data.database.model.server

import com.ead.project.dreamer.data.database.model.Player
import com.ead.project.dreamer.data.database.model.Server
import com.ead.project.dreamer.data.database.model.VideoModel
import com.ead.project.dreamer.data.utils.PatternManager
import com.ead.project.dreamer.data.utils.receiver.DreamerRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.text.StringEscapeUtils
import org.json.JSONObject

class Okru(embeddedUrl:String) : Server(embeddedUrl) {


    override fun onPreExtract() {
        super.onPreExtract()
        player = Player.Okru
    }

    override fun onExtract() {
        super.onExtract()
        try {
            url = url.replace("//","https://")

            val response = OkHttpClient()
                .newCall(
                    Request.Builder().url(url)
                        .header("User-Agent", DreamerRequest.getSpecificUserAgent(0)).build())
                .execute()

            url = PatternManager.singleMatch(
                response.body!!.string(),
                "data-options=\"(.*?)\"")!!

            url = StringEscapeUtils.unescapeHtml4(url)

            val json = JSONObject(url)
                .getJSONObject("flashvars")
                .getString("metadata")

            val objectData = JSONObject(json).getJSONArray("videos")
            var video: VideoModel

            for (pos in 0 until objectData.length()) {
                val url: String = objectData.getJSONObject(pos).getString("url")
                video = when (objectData.getJSONObject(pos).getString("name")) {
                    "mobile" -> VideoModel("144p", url)
                    "lowest" -> VideoModel("240p", url)
                    "low"    -> VideoModel("360p", url)
                    "sd"     -> VideoModel("480p", url)
                    "hd"     -> VideoModel("720p", url)
                    "full"   -> VideoModel("1080p", url)
                    "quad"   -> VideoModel("2000p", url)
                    "ultra"  -> VideoModel("4000p", url)
                    else     -> VideoModel("Default", url)
                }
                videoList.add(video)
            }
            breakOperation()
        } catch (e : Exception) {
            e.printStackTrace()
        }


    }
}