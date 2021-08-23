package com.example.showtweetsonmap

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder
import java.util.*
import java.util.List


class TweetsViewModelNew(application: Application) : AndroidViewModel(application) {

    var tweets: MutableLiveData<List<Status>> = MutableLiveData()
    val tempStatusList: ArrayList<Status> = ArrayList()
    private val tweetFilterQuery = FilterQuery()
    private val twitterStream: TwitterStream

    init {
        twitterStream = getTwitterStream(application)
    }

    fun startStreaming(
        searchTerm: String
    ) {
       // if (tweets == null) {
           // tweets = MutableLiveData<List<Status>>()
            val listener: StatusListener = object : StatusListener {
                override fun onException(ex: Exception?) {
                    Log.e(TAG, "An exception occurred while getting tweets stream$ex")
                }

                override fun onStatus(status: Status) {
                    Log.e(TAG, "Tweets data received " + status.user)
                    tempStatusList.add(status)
                    if(tempStatusList.size < 100){
                     //   twitterStream.shutdown()

                        tweets?.let{
                            it.postValue(tempStatusList)
                        }
                    }
                }

                override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice?) {
                }

                override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {
                }

                override fun onScrubGeo(userId: Long, upToStatusId: Long) {
                }

                override fun onStallWarning(warning: StallWarning?) {
                }
            }


            var keywords = arrayOf(searchTerm)
            tweetFilterQuery.track(*keywords);
            twitterStream.addListener(listener);

            //twitterStream.filter(tweetFilterQuery);
            twitterStream.sample()
      //  }
     //   return tweets
    }

    private fun getTwitterStream(context: Context): TwitterStream {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
        cb.setOAuthConsumerKey(context.resources.getString(R.string.twitter_api_key))
        cb.setOAuthConsumerSecret(context.resources.getString(R.string.twitter_api_secret))
        cb.setOAuthAccessToken(context.resources.getString(R.string.twitter_access_token))
        cb.setOAuthAccessTokenSecret(context.resources.getString(R.string.twitter_access_token_secret))
        return TwitterStreamFactory(cb.build()).getInstance()
    }


    companion object {
        private const val TAG = "TweetsViewModel"
    }
}

private fun <T> MutableLiveData<T>?.postValue(arrayList: T) {

}

private fun <T> MutableLiveData<T>.postValue(tempStatusList: ArrayList<Status>) {

}
