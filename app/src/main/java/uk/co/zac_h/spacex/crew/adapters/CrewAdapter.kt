package uk.co.zac_h.spacex.crew.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CrewModel

class CrewAdapter(private val crew: List<CrewModel>) :
    RecyclerView.Adapter<CrewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.grid_item_crew,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = crew[position]

        holder.apply {
            itemView.transitionName = person.id

            Glide.with(itemView).load(person.image).into(image)

            title.text = person.name

            card.setOnClickListener { bind(person) }
        }
    }

    override fun getItemCount(): Int = crew.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_crew_card)
        val image: ImageView = itemView.findViewById(R.id.list_item_crew_image)
        val title: TextView = itemView.findViewById(R.id.list_item_crew_title)

        fun bind(person: CrewModel) {
            itemView.findNavController().navigate(
                R.id.action_crew_fragment_to_crew_details_fragment,
                bundleOf("crew" to crew, "position" to adapterPosition),
                null,
                FragmentNavigatorExtras(itemView to person.id)
            )
        }
    }
}