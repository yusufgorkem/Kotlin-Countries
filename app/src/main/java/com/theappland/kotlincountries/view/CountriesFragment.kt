package com.theappland.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.theappland.kotlincountries.R
import com.theappland.kotlincountries.adapter.RecyclerViewAdapter
import com.theappland.kotlincountries.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.fragment_countries.*

class CountriesFragment : Fragment() {
    private lateinit var countriesViewModel : CountriesViewModel
    private var countryAdapter = RecyclerViewAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countriesViewModel = ViewModelProvider(this)[CountriesViewModel::class.java]
        countriesViewModel.refreshData()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = countryAdapter

        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.visibility = View.GONE
            countryLoading.visibility = View.VISIBLE
            countriesViewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        countriesViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                countryAdapter.updateCountries(countries)
            }
        })

        countriesViewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it) {
                    countryLoading.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    countryLoading.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        })
    }
}