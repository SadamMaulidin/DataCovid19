package com.example.datacovid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datacovid19.CountryAdapter
import com.example.datacovid19.DetailCountryActivity
import com.example.datacovid19.Negara
import com.example.datacovid19.R
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var adapters: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCountry()

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapters.filter.filter(newText)
                return false
            }

        })

    }

    private fun getCountry() {
        val okHttp = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.covid19api.com/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.getAllCountry().enqueue(object : Callback<AllCountry> {
            override fun onFailure(call: Call<AllCountry>, t: Throwable) {
                progress_Bar?.visibility = View.GONE
            }

            override fun onResponse(call: Call<AllCountry>, response: Response<AllCountry>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Data Success", Toast.LENGTH_SHORT).show()
                    val getDataListCorona = response.body()!!.Global
                    confirmed_globe.text = getDataListCorona.TotalConfirmed
                    recovered_globe.text = getDataListCorona.TotalRecovered
                    deaths_globe.text = getDataListCorona.TotalDeaths
                    recyclerViewCountry.apply {
                        setHasFixedSize(true)
                        progress_Bar.visibility = View.GONE
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapters =
                            CountryAdapter(response.body()!!.Countries as ArrayList<Negara>) { negara ->
                                itemClicked(negara)
                            }

                        adapter = adapters

                    }
                } else {
                    progress_Bar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Data Unreachable", Toast.LENGTH_SHORT).show()
                    //server ditemukan tapi tidak ada endpoint
                    //galat data
                    //dikeluarkan oleh apiservice
                }
            }


        })
    }

    private fun itemClicked(negara: Negara) {
        val pindahData = Intent(this, DetailCountryActivity::class.java)
        //"meletakkan data"
        pindahData.putExtra(DetailCountryActivity.EXTRA_COUNTRY, negara.Country)
        pindahData.putExtra(DetailCountryActivity.EXTRA_LATESTUPDATE, negara.Date)
        pindahData.putExtra(DetailCountryActivity.EXTRA_NEWCONFIRMED, negara.NewConfirmed)
        pindahData.putExtra(DetailCountryActivity.EXTRA_NEWDEATH, negara.NewDeaths)
        pindahData.putExtra(DetailCountryActivity.EXTRA_NEWRECOVERED, negara.NewRecovered)
        pindahData.putExtra(DetailCountryActivity.EXTRA_TOTALCONFIRMED, negara.TotalConfirmed)
        pindahData.putExtra(DetailCountryActivity.EXTRA_TOTALDEATH, negara.TotalDeaths)
        pindahData.putExtra(DetailCountryActivity.EXTRA_TOTALRECOVERED, negara.TotalRecovered)
        pindahData.putExtra(DetailCountryActivity.EXTRA_COUNTRYID, negara.CountryCode)
        startActivity(pindahData)
    }

    private fun goneLoading(){

    }
}
