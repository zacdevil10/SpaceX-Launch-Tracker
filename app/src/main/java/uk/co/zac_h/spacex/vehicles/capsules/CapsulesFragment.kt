package uk.co.zac_h.spacex.vehicles.capsules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_capsules.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CapsulesModel
import uk.co.zac_h.spacex.vehicles.adapters.CapsulesAdapter

class CapsulesFragment : Fragment(), CapsulesView {

    private lateinit var presenter: CapsulesPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_capsules, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CapsulesPresenterImpl(this, CapsulesInteractorImpl())

        presenter.getCapsules()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    override fun updateCapsules(capsules: List<CapsulesModel>) {
        capsules_recycler.apply {
            layoutManager = LinearLayoutManager(this@CapsulesFragment.context)
            setHasFixedSize(true)
            adapter = CapsulesAdapter(capsules)
        }
    }

    override fun showProgress() {
        capsules_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        capsules_progress_bar.visibility = View.GONE
    }

    override fun error(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
