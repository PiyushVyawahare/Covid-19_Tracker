package com.example.covid_19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity() {
    var state_name : String? = null
    var district_name : String? = null
    val cityArray : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val states = resources.getStringArray(R.array.States)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val districtSpinner = findViewById<Spinner>(R.id.districtSpinner)
        if ( spinner != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                    state_name = states[position]
                    if(state_name!="Select"){
                        fetchData()
                    }
                    else{
                        cityArray.clear()
                        cityArray.add("Select")
                    }
                    temp()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }

    }
    fun temp(){
        val districtSpinner = findViewById<Spinner>(R.id.districtSpinner)
        if(districtSpinner!=null) {
            val dAadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityArray)
            districtSpinner.adapter = dAadapter
            districtSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    district_name = cityArray[p2]
//                    Toast.makeText(this@MainActivity, "$state_name $district_name", Toast.LENGTH_LONG).show()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun fetchData() {
        val url = "https://data.covid19india.org/state_district_wise.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val stateJsonObject = it.getJSONObject(state_name)
                val districtJsonObject = stateJsonObject.getJSONObject("districtData")
                val keys : Iterator<String> = districtJsonObject.keys()
                if(cityArray.isNotEmpty())
                    cityArray.clear()
                cityArray.add("Select")
                while(keys.hasNext()){
                    cityArray.add(keys.next())
                }
            },
            Response.ErrorListener {
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun onButtonClicked(view: View) {
        if(state_name == "Select" || district_name == "Select")
            Toast.makeText(this@MainActivity, "Select State or City", Toast.LENGTH_LONG).show()
        else{
            val intent = Intent(this, CovidData::class.java)
            intent.putExtra(CovidData.STATE_EXTRA, state_name)
            intent.putExtra(CovidData.DISTRICT_EXTRA, district_name)
            startActivity(intent)
        }
    }
}