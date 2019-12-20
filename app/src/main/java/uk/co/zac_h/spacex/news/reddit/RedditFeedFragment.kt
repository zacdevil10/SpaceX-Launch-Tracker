package uk.co.zac_h.spacex.news.reddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_reddit_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.model.reddit.SubredditPostModel
import uk.co.zac_h.spacex.news.adapters.RedditAdapter

class RedditFeedFragment : Fragment(), RedditFeedView {

    private var presenter: RedditFeedPresenter? = null

    private lateinit var redditAdapter: RedditAdapter
    private val posts = ArrayList<SubredditPostModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_reddit_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RedditFeedPresenterImpl(this, RedditFeedInteractorImpl())

        redditAdapter = RedditAdapter(posts)

        reddit_recycler.apply {
            layoutManager = LinearLayoutManager(this@RedditFeedFragment.context)
            adapter = redditAdapter
        }

        presenter?.getSub()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun updateRecycler(subredditData: SubredditModel) {
        posts.clear()
        posts.addAll(subredditData.data.children)

        redditAdapter.notifyDataSetChanged()
    }

}
