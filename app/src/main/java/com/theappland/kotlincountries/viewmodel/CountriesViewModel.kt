package com.theappland.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.theappland.kotlincountries.database.CountryDatabase
import com.theappland.kotlincountries.model.CountryModel
import com.theappland.kotlincountries.service.CountryAPIService
import com.theappland.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CountriesViewModel(application: Application) : BaseViewModel(application) {
    val countries = MutableLiveData<List<CountryModel>>()
    val countryLoading = MutableLiveData<Boolean>()
    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData() {
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value = true

        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CountryModel>>() {
                    override fun onSuccess(t: List<CountryModel>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun showCountries(countryList : List<CountryModel>) {
        countries.value = countryList
        countryLoading.value = false
    }

    private fun storeInSQLite(list: List<CountryModel>) {
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uid = listLong[i].toInt()
                i += 1
            }
            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}