package uk.co.zac_h.spacex.crew.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.crew.CrewView
import uk.co.zac_h.spacex.databinding.GridItemCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew

class CrewAdapter(
    private val view: CrewView,
    private val crew: List<Crew>
) : RecyclerView.Adapter<CrewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        GridItemCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = crew[position]

        holder.binding.apply {
            root.transitionName = person.id

            Glide.with(root).load(person.image).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.startTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.startTransition()
                    return false
                }
            }).into(image)

            title.text = person.name

            crewCard.setOnClickListener {
                MainActivity.currentPosition = position
                holder.bind(person)
            }
        }
    }

    override fun getItemCount(): Int = crew.size

    inner class ViewHolder(val binding: GridItemCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Crew) {
            binding.root.findNavController().navigate(
                R.id.action_crew_fragment_to_crew_details_fragment,
                bundleOf("crew" to crew, "position" to adapterPosition),
                null,
                FragmentNavigatorExtras(binding.root to person.id)
            )
        }
    }
}