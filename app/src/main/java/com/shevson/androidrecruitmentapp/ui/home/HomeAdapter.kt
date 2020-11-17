package com.shevson.androidrecruitmentapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.shevson.androidrecruitmentapp.R
import com.shevson.androidrecruitmentapp.data.local.UserDbModel
import com.shevson.androidrecruitmentapp.util.loadAvatar
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_item.view.*


class HomeAdapter(private var items: List<Pair<UserDbModel, List<String>>>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(), Filterable {

    var itemsFiltered = ArrayList<Pair<UserDbModel, List<String>>>()

    init {
        itemsFiltered.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsFiltered[position])
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: Pair<UserDbModel, List<String>>) {
            containerView.user_login_txt.text =
                containerView.rootView.context.getString(R.string.login, item.first.login)
            containerView.user_avatar_img.loadAvatar(item.first.avatar_url)
            if (item.second.isNotEmpty()) {
                containerView.repo_name_1_txt.text = item.second.elementAtOrNull(0) ?: ""
                containerView.repo_name_2_txt.text = item.second.elementAtOrNull(1) ?: ""
                containerView.repo_name_3_txt.text = item.second.elementAtOrNull(2) ?: ""
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val searchString = p0.toString()
                val results = if (searchString.isEmpty()) {
                    items
                } else {
                    val results = ArrayList<Pair<UserDbModel, List<String>>>()
                    for (item in items) {
                        if (item.first.login.toLowerCase().contains(searchString.toLowerCase())
                            || item.second.any {
                                it.toLowerCase().contains(searchString.toLowerCase())
                            }
                        ) results.add(item)
                    }
                    results
                }
                val filterResults = FilterResults()
                filterResults.values = results
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                itemsFiltered = p1?.values as ArrayList<Pair<UserDbModel, List<String>>>
                notifyDataSetChanged()
            }
        }
    }

}