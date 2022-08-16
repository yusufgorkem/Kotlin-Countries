package com.theappland.kotlincountries.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.theappland.kotlincountries.model.CountryModel

@Dao
interface CountryDao {

    @Insert
    suspend fun insertAll(vararg countries: CountryModel) : List<Long>

    @Query("SELECT * FROM country")
    suspend fun getAllCountries() : List<CountryModel>

    @Query("SELECT * FROM country WHERE uid = :countryId")
    suspend fun getCountry(countryId : Int) : CountryModel

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}