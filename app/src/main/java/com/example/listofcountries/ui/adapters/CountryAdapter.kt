package com.example.listofcountries.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.listofcountries.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listofcountries.data.model.CountryItem
import com.example.listofcountries.databinding.ItemCountryBinding

class CountryAdapter(
    private val countryList: MutableList<CountryItem> = mutableListOf()
) : RecyclerView.Adapter<CountryAdapter.CountryItemHolder>() {


    inner class CountryItemHolder(itemView: View) : ViewHolder(itemView) {
        val binding = ItemCountryBinding.bind(itemView)
        fun updateUI(countryItem: CountryItem) {
            binding.apply {
                tvNameRegion.text = "${countryItem.name}, ${countryItem.region}"
                tvCode.text = countryItem.code
                tvCapital.text = countryItem.capital
            }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryItemHolder {
        return CountryItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CountryItemHolder,
        position: Int
    ) {
        holder.updateUI(countryList[position])
    }

    override fun getItemCount() = countryList.size

    fun update(newList: List<CountryItem>) {
        countryList.clear()
        countryList.addAll(newList)
        notifyDataSetChanged()
    }



}