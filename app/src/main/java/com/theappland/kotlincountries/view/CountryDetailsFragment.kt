package com.theappland.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.theappland.kotlincountries.R
import com.theappland.kotlincountries.databinding.FragmentCountryDetailsBinding
import com.theappland.kotlincountries.viewmodel.CountryDetailsViewModel

class CountryDetailsFragment : Fragment() {
    private lateinit var countryDetailsViewModel: CountryDetailsViewModel
    private var countryUid = 0
    private lateinit var dataBinding : FragmentCountryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_country_details,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUid = CountryDetailsFragmentArgs.fromBundle(it).uid
        }

        countryDetailsViewModel = ViewModelProvider(this)[CountryDetailsViewModel::class.java]
        countryDetailsViewModel.getDataFromRoom(countryUid)

        observeLiveData()
    }

    private fun observeLiveData() {
        countryDetailsViewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                dataBinding.selectedCountry = country

                /*
                context?.let { context ->
                    selectedCountryImageView.downloadFromUrl(country.countryFlagUrl, placeHolderProgressBar(context))
                }
                selectedCountryNameTextView.text = country.countryName
                selectedCountryCapitalTextView.text = country.countryCapital
                selectedCountryRegionTextView.text = country.countryRegion
                selectedCountryCurrencyTextView.text = country.countryCurrency
                selectedCountryLanguageTextView.text = country.countryLanguage
                 */
            }
        })
    }
}