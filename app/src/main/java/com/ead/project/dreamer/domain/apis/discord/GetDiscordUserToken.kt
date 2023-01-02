package com.ead.project.dreamer.domain.apis.discord

import androidx.lifecycle.MutableLiveData
import com.ead.project.dreamer.app.DreamerApp
import com.ead.project.dreamer.data.AnimeRepository
import com.ead.project.dreamer.data.models.discord.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GetDiscordUserToken @Inject constructor(
    private val repository: AnimeRepository
) {

    fun livedata () : MutableLiveData<AccessToken?> = getAccessToken()

    private var accessToken : MutableLiveData<AccessToken?>?= null

    private fun getAccessToken() : MutableLiveData<AccessToken?> {
        val discordService = repository.getDiscordService(repository.getDiscordUserTokenRetrofit())
        val response : Call<AccessToken?> = discordService.getAccessToken()
        response.enqueue(object : Callback<AccessToken?> {
            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
                try { if (response.isSuccessful) accessToken?.value = response.body() }
                catch ( e : Exception) { e.printStackTrace() }
            }
            override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                DreamerApp.showLongToast(t.cause?.message.toString())
            }
        })
        return accessToken?:MutableLiveData<AccessToken?>().also { accessToken = it }
    }
}