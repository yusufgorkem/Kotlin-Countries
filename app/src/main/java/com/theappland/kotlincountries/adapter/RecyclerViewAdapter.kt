package com.theappland.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.theappland.kotlincountries.R
import com.theappland.kotlincountries.databinding.RecyclerRowBinding
import com.theappland.kotlincountries.model.CountryModel
import com.theappland.kotlincountries.view.CountriesFragmentDirections
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerViewAdapter(private val countryList : ArrayList<CountryModel>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>()
    ,CountryClickListener {
    class RecyclerViewHolder(var view : RecyclerRowBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.recycler_row,parent,false)
        val view = DataBindingUtil.inflate<RecyclerRowBinding>(inflater,R.layout.recycler_row,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.view.country = countryList[position]
        holder.view.listener = this

        /*
        holder.view.countryFlagImageView.downloadFromUrl(countryList[position].countryFlagUrl,
            placeHolderProgressBar(holder.view.context))
        holder.view.countryNameTextView.text = countryList[position].countryName
        holder.view.countryRegionTextView.text = countryList[position].countryRegion

        holder.view.setOnClickListener {
            val action = CountriesFragmentDirections.actionCountriesFragmentToCountryDetailsFragment(countryList[position].uid)
            Navigation.findNavController(it).navigate(action)
        }
         */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountries(newCountryList : List<CountryModel>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uid = v.countryUidTextView.text.toString().toInt()
        val action = CountriesFragmentDirections.actionCountriesFragmentToCountryDetailsFragment(uid)
        Navigation.findNavController(v).navigate(action)
    }
}