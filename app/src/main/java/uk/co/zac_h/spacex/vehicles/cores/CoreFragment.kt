package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_core.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CoreModel
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : Fragment(), CoreView {

    private lateinit var presenter: CorePresenter

    private lateinit var coreAdapter: CoreAdapter
    private val coresArray = ArrayList<CoreModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_core, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CorePresenterImpl(this, CoreInteractorImpl())

        coreAdapter = CoreAdapter(context, coresArray)

        core_recycler.apply {
            layoutManager = LinearLayoutManager(this@CoreFragment.context)
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        if (coresArray.isEmpty()) presenter.getCores()
    }

    override fun updateCores(cores: List<CoreModel>) {
        coresArray.clear()
        coresArray.addAll(cores)

        coreAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        core_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        core_progress_bar.visibility = View.GONE
    }

    override fun error(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
