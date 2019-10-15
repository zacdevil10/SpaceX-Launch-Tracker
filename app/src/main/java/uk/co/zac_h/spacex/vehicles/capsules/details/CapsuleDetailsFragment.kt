package uk.co.zac_h.spacex.vehicles.capsules.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_capsule_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CapsulesModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort
import uk.co.zac_h.spacex.vehicles.adapters.CapsuleMissionsAdapter

class CapsuleDetailsFragment : Fragment() {

    private var capsule: CapsulesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        capsule = arguments?.getParcelable("capsule") as CapsulesModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_capsule_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        capsule?.let {
            capsule_details_type_text.text = it.type
            capsule_details_text.text = it.details
            capsule_details_status_text.text = it.status.capitalize()
            capsule_details_date_text.text = it.originalLaunchUnix?.formatDateMillisShort() ?: "TBD"
            capsule_details_reuse_text.text = it.reuseCount.toString()
            capsule_details_landing_text.text = it.landings.toString()

            if (it.missions.isNotEmpty()) capsule_details_missions_recycler.apply {
                layoutManager = LinearLayoutManager(this@CapsuleDetailsFragment.context)
                setHasFixedSize(true)
                adapter = CapsuleMissionsAdapter(context, it.missions)
            } else capsule_details_no_mission_label.visibility = View.VISIBLE
        }
    }
}
