package com.example.datacovid19

import kotlinx.android.synthetic.main.list_negara.view.*

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    private var negara: ArrayList<Negara>,
    private val clickListener: (Negara) -> Unit
) : RecyclerView.Adapter<CountryAdapter.ViewHolder>(), Filterable {

    var countryFilterList = ArrayList<Negara>()

    init {
        countryFilterList = negara
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_negara, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countryFilterList[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(negara: Negara, clickListener: (Negara) -> Unit) {
            //binding widget pada layout model
            val country = itemView.countryName
            val cTotalCase = itemView.country_total_case
            val cTotalRecovered = itemView.country_total_recovered
            val cTotalDeath = itemView.country_total_deaths
            val flag = itemView.img_flag_circle
            //membuat format separator seribu
            val formatter = DecimalFormat("#,####")
            //menginjeksi data ke widget
            country.text = negara.Country
            cTotalCase.text = formatter.format(negara.TotalConfirmed.toDouble())
            cTotalRecovered.text =
                formatter.format(negara.TotalRecovered.toDouble())
            cTotalDeath.text = formatter.format(negara.TotalDeaths.toDouble())
            Glide.with(itemView)
                .load("https://www.countryflags.io/" + negara.CountryCode + "/flat/64.png")
                .into(flag)
            //menjadikan data respon untuk di clik
            country.setOnClickListener { clickListener(negara) }
            cTotalCase.setOnClickListener { clickListener(negara) }
            cTotalRecovered.setOnClickListener { clickListener(negara) }
            cTotalDeath.setOnClickListener { clickListener(negara) }
            flag.setOnClickListener { clickListener(negara) }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    negara
                } else {
                    val resultList = ArrayList<Negara>()
                    for (row in negara) {
                        if (row.Country.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = countryFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Negara>
                notifyDataSetChanged()
            }
        }
    }
}