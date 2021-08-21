package com.example.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class CovidData : AppCompatActivity() {
    companion object{
        const val DISTRICT_EXTRA = "district_extra"
        const val STATE_EXTRA = "state_extra"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_data)

        fetcdata()
    }

    private fun fetcdata() {


        val state = intent.getStringExtra(STATE_EXTRA)
        val district = intent.getStringExtra(DISTRICT_EXTRA)

        val districtName = findViewById<TextView>(R.id.districtName)
        val activeNumber = findViewById<TextView>(R.id.activeNumber)
        val confirmedNumber = findViewById<TextView>(R.id.confirmedNumber)
        val deceasedNumber = findViewById<TextView>(R.id.deceasedNumber)
        val recoveredNumber = findViewById<TextView>(R.id.recoveredNumber)
        val confirmedDeltaNumber = findViewById<TextView>(R.id.confirmedDeltaNumber)
        val deceasedDeltaNumber = findViewById<TextView>(R.id.deceasedDeltaNumber)
        val recoveredDeltaNumber = findViewById<TextView>(R.id.recoveredDeltaNumber)

        districtName.setText("$district, $state")

        val url = "https://data.covid19india.org/state_district_wise.json"
        val jsonObjectRequest1 = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {

                val stateJsonObject = it.getJSONObject(state)
                val districtJsonObject = stateJsonObject.getJSONObject("districtData")
                val cityJsonObject = districtJsonObject.getJSONObject(district)
                var mactive = cityJsonObject.getInt("active")
                activeNumber.setText(mactive.toString())


                var mconfirmed = cityJsonObject.getInt("confirmed")
                confirmedNumber.setText(mconfirmed.toString())

                var mdeceased = cityJsonObject.getInt("deceased")
                deceasedNumber.setText(mdeceased.toString())

                var mrecovered = cityJsonObject.getInt("recovered")
                recoveredNumber.setText(mrecovered.toString())

                val deltaJsonObject = cityJsonObject.getJSONObject("delta")
                confirmedDeltaNumber.setText(deltaJsonObject.getInt("confirmed").toString())
                deceasedDeltaNumber.setText(deltaJsonObject.getInt("deceased").toString())
                recoveredDeltaNumber.setText(deltaJsonObject.getInt("recovered").toString())

            },
            Response.ErrorListener {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest1)
    }
}