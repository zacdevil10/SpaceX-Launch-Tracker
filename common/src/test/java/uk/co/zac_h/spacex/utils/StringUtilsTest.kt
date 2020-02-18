package uk.co.zac_h.spacex.utils

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import uk.co.zac_h.spacex.model.twitter.TweetHashTagModel
import uk.co.zac_h.spacex.model.twitter.TweetMentionsModel
import uk.co.zac_h.spacex.model.twitter.TweetUrlModel

class StringUtilsTest {

    @Mock
    private lateinit var mTweetUrlModel: TweetUrlModel
    @Mock
    private lateinit var mTweetMentionsModel: TweetMentionsModel
    @Mock
    private lateinit var mTweetHashTagModel: TweetHashTagModel

    private val tweetUrlList = ArrayList<TweetUrlModel>()
    private val tweetMentionsList = ArrayList<TweetMentionsModel>()
    private val tweetHashTagList = ArrayList<TweetHashTagModel>()

    @Before
    fun setup() {
        mTweetUrlModel = mock(TweetUrlModel::class.java)
        mTweetMentionsModel = mock(TweetMentionsModel::class.java)
        mTweetHashTagModel = mock(TweetHashTagModel::class.java)

        tweetUrlList.add(mTweetUrlModel)
        tweetMentionsList.add(mTweetMentionsModel)
        tweetHashTagList.add(mTweetHashTagModel)
    }

    @Test
    fun formatATweetWithURLMentionAndHashTag() {
        Mockito.`when`(mTweetUrlModel.url).thenReturn("https://t.co/74yr873y")
        Mockito.`when`(mTweetUrlModel.displayUrl).thenReturn("zac-h.co.uk")
        Mockito.`when`(mTweetMentionsModel.screenName).thenReturn("MentionOne")
        Mockito.`when`(mTweetHashTagModel.tag).thenReturn("HashTagOne")

        assert(INPUT.formatWithUrls(tweetUrlList, tweetMentionsList, tweetHashTagList) == OUTPUT)
    }

    @Test
    fun `Format numbers to two decimal places`() {
        assert(1350.54.metricFormat() == "1,350.54")
        assert(1350.54342342.metricFormat() == "1,350.54")
        assert(1350.54742342.metricFormat() == "1,350.55")
    }

    companion object {
        const val INPUT = "Testing " +
                "#HashTagOne " +
                "@MentionOne " +
                "with a link to " +
                "https://t.co/74yr873y"

        const val OUTPUT = "Testing " +
                "<a href='https://twitter.com/hashtag/HashTagOne'>#HashTagOne</a> " +
                "<a href='https://twitter.com/MentionOne'>@MentionOne</a> " +
                "with a link to " +
                "<a href='https://t.co/74yr873y'>zac-h.co.uk</a>"
    }

}