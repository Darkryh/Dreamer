package com.ead.project.dreamer.ui.player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.ead.project.dreamer.R
import com.ead.project.dreamer.data.commons.Constants
import com.ead.project.dreamer.data.database.model.Chapter
import com.ead.project.dreamer.data.database.model.VideoModel
import com.ead.project.dreamer.data.utils.DataStore
import com.ead.project.dreamer.databinding.ActivityInterstitialAdBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdActivity : AppCompatActivity() {

    private var interstitialAd : InterstitialAd?= null
    private lateinit var chapter: Chapter
    private lateinit var videoList : List<VideoModel>
    private var isExternalPlayer = false

    private lateinit var binding: ActivityInterstitialAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initVariables()
        initAds()
    }

    private fun initVariables() {
        chapter = intent.extras!!.getParcelable(Constants.REQUESTED_CHAPTER)!!
        videoList = intent.extras!!.getParcelableArrayList(Constants.PLAY_VIDEO_LIST)!!
        isExternalPlayer = DataStore.readBoolean(Constants.PREFERENCE_EXTERNAL_PLAYER)
    }

    private fun initAds() {
        try {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                this,
                getString(R.string.ad_unit_id_interstitial_player),
                adRequest,object : InterstitialAdLoadCallback() {

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        this@InterstitialAdActivity.interstitialAd = interstitialAd
                        initListener()
                        showAds()
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        this@InterstitialAdActivity.interstitialAd = null
                    }
                })
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun showAds() {
        interstitialAd?.show(this)
    }

    private fun initListener() {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Constants.quantityAdPlus()
                interstitialAd = null
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                if (!isExternalPlayer) {
                    startActivity(
                        Intent(this@InterstitialAdActivity,
                            PlayerActivity::class.java)
                            .putExtra(Constants.REQUESTED_CHAPTER, chapter)
                            .putParcelableArrayListExtra(
                                Constants.PLAY_VIDEO_LIST,
                                videoList as java.util.ArrayList<out Parcelable>
                            )
                    )
                }
                else {
                    startActivity(
                        Intent(this@InterstitialAdActivity,
                            PlayerExternalActivity::class.java)
                            .putExtra(Constants.REQUESTED_CHAPTER, chapter)
                            .putParcelableArrayListExtra(
                                Constants.PLAY_VIDEO_LIST,
                                videoList as java.util.ArrayList<out Parcelable>
                            )
                    )
                }
                finish()
            }
        }
    }

}