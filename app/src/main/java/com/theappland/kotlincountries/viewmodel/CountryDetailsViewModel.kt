package com.theappland.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.theappland.kotlincountries.database.CountryDatabase
import com.theappland.kotlincountries.model.CountryModel
import kotlinx.coroutines.launch

class CountryDetailsViewModel(application: Application) : BaseViewModel(application) {
    val countryLiveData = MutableLiveData<CountryModel>()

    fun getDataFromRoom(uid : Int) {
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            val country = dao.getCountry(uid)
            countryLiveData.value = country
        }
    }
}